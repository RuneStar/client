#!/bin/sh

cd -- "$(dirname -- "${BASH_SOURCE:-$0}")"

zip -j runestar.zip runestar.cmd runestar.command ../LICENSE ../NOTICE
