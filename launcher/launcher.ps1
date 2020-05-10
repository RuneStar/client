#Requires -Version 2.0
Set-StrictMode -Version 2.0

$ErrorActionPreference = 'Stop'
$Global:ProgressPreference = 'SilentlyContinue'
[Net.ServicePointManager]::SecurityProtocol = [Enum]::ToObject([Net.SecurityProtocolType], 3072)

function Expand-Zip($Path, $DestinationPath) {
	if (Get-Command Expand-Archive -ErrorAction SilentlyContinue) {
		Expand-Archive -Path $Path -DestinationPath $DestinationPath -Force -Verbose
	} else {
		New-Item -ItemType Directory -Path $DestinationPath -Force | Out-Null
		$Shell = New-Object -COMObject 'Shell.Application'
		$Target = $Shell.NameSpace((Convert-Path $DestinationPath))
		$Zip = $Shell.NameSpace((Convert-Path $Path))
		$Target.CopyHere($Zip.Items(), 16)
	}
}

$JavaVersion = 11
$Arch = if ($Env:PROCESSOR_ARCHITECTURE -eq 'AMD64' -or $Env:PROCESSOR_ARCHITEW6432 -eq 'AMD64') { 'x64' } else { 'x32' }
$LauncherDir = "launcher"
$JreDir = "$LauncherDir\jre-windows-$Arch"
$JreId = 0
$JreVersionFile = "$JreDir\version"
$JreVersion = "$JavaVersion-$JreId"

if (!(Test-Path $JreVersionFile -PathType Leaf) -or ((Get-Content $JreVersionFile) -ne $JreVersion)) {
	if (Test-Path $JreDir) {
		Remove-Item $JreDir -Recurse
	}
}

if (!(Test-Path $JreDir)) {
	$TempDir = "$LauncherDir\temp"
	New-Item -ItemType Directory -Path $TempDir -Force | Out-Null
	$JdkArchive = "$TempDir\jdk-$JavaVersion-windows-$Arch.zip"
	if (!(Test-Path $JdkArchive)) {
		$JdkArchiveUrl = "https://api.adoptopenjdk.net/v3/binary/latest/$JavaVersion/ga/windows/$Arch/jdk/hotspot/normal/adoptopenjdk"
		"Downloading '$JdkArchiveUrl' to '$JdkArchive'"
		(New-Object System.Net.WebClient).DownloadFile($JdkArchiveUrl, $JdkArchive)
	}
	$JdkDir = "$TempDir\jdk-$JavaVersion-windows-$Arch"
	Expand-Zip -Path $JdkArchive -DestinationPath $JdkDir
	$JdkHome = Join-Path $JdkDir * -Resolve

	& "$JdkHome\bin\jlink" `
		"--verbose" `
		"--no-header-files" `
		"--no-man-pages" `
		"--strip-debug" `
		"--compress=2" `
		"--module-path" "$JdkHome\jmods" `
		"--add-modules" "java.desktop,java.management,java.naming,java.sql,java.net.http" `
		"--output" "$JreDir"

	if ($LastExitCode) { exit $LastExitCode }

	[System.IO.File]::WriteAllText($JreVersionFile, $JreVersion)

	Remove-Item $TempDir -Recurse
}

$ClientDir = 'client'
$ClientJar = "$ClientDir\client.jar"
$ClientVersionFile = "$ClientDir\version"
$NewClientVersionUrl = 'https://dl.runestar.org/client.jar.version'
$NewClientVersion = (New-Object System.Net.WebClient).DownloadString($NewClientVersionUrl)

if (!(Test-Path $ClientVersionFile -PathType Leaf) -or ((Get-Content $ClientVersionFile) -ne $NewClientVersion)) {
	New-Item -ItemType Directory -Path $ClientDir -Force | Out-Null
	$NewClientJarUrl = "https://dl.runestar.org/client-$NewClientVersion.jar"
	"Downloading '$NewClientJarUrl' to '$ClientJar'"
	(New-Object System.Net.WebClient).DownloadFile($NewClientJarUrl, $ClientJar)
	[System.IO.File]::WriteAllText($ClientVersionFile, $NewClientVersion)
}

& "$JreDir\bin\javaw" `
	"-Duser.home=$ClientDir" `
	"-jar" "$ClientJar"
