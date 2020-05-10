@ECHO OFF
PUSHD %~dp0

SET powershell_command=^&{^
$ErrorActionPreference = 'Stop';^
[Net.ServicePointManager]::SecurityProtocol = [Enum]::ToObject([Net.SecurityProtocolType], 3072);^
$LauncherUrl = 'https://dl.runestar.org/launcher.ps1';^
$LauncherFile = 'launcher\launcher.ps1';^
New-Item -ItemType Directory -Path 'launcher' -Force ^| Out-Null;^
(New-Object System.Net.WebClient).DownloadFile($LauncherUrl, $LauncherFile);^
& $LauncherFile %*;^
exit $LastExitCode;^
}

powershell -NoProfile -ExecutionPolicy Bypass -Command "%powershell_command%" || PAUSE
POPD
