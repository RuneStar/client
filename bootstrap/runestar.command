#!/bin/sh

set -eu

cd -- "$(dirname -- "${BASH_SOURCE:-$0}")"

launcher_url=https://dl.runestar.org/launcher.sh

launcher_file=launcher/launcher.sh

mkdir -p launcher

if command -v curl >/dev/null
then
	curl -fLo "$launcher_file" "$launcher_url"
elif command -v wget >/dev/null
then
	wget -O "$launcher_file" "$launcher_url"
else
	echo 'missing required command: curl or wget'
	exit 1
fi

sh "$launcher_file" "$@"
