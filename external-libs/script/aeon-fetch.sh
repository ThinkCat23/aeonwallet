#!/usr/bin/env bash
# Author: m2049r https://github.com/m2049r/xmrwallet
# Aeon adaption: ivoryguru https://github.com/ivoryguru/aeonwallet

set -e

source script/env.sh

cd $EXTERNAL_LIBS_BUILD_ROOT

url="https://github.com/ivoryguru/aeon"
version="aeonwallet"

if [ ! -d "aeon" ]; then
  git clone ${url} -b ${version}
  cd aeon
  git submodule update --recursive --init
else
  cd aeon
  git fetch
  git checkout ${version}
  git pull
  git submodule update --recursive --init
fi
