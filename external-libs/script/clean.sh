#!/usr/bin/env bash
# Author: m2049r https://github.com/m2049r/xmrwallet

set -e

source script/env.sh

rm -rf $EXTERNAL_LIBS_BUILD
mkdir -p $EXTERNAL_LIBS_BUILD/src
