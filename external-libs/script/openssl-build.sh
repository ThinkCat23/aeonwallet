#!/usr/bin/env bash
# Author: m2049r https://github.com/m2049r/xmrwallet

set -e

source script/env.sh

cd $EXTERNAL_LIBS_BUILD_ROOT/android-openssl

./build-all-arch.sh
