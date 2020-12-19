<p align="center">
  <img src="ic_launcher_round.png" width="250" title="hover text">
  <img src="Screenshot_20201219-094242_Aeon_Wallet[1].jpg" width="250" title="hover text">
  <img src="Screenshot_20201219-094348_Aeon_Wallet[1].jpg" width="250" title="hover text">
</p>

# aeonwallet

An original android wallet app built for Aeon. Connect to a node, load your wallet, and send coins. 

Be aware: this app is Apache licensed. By using it, you agree with the terms of that license. In particular pay attention to section 7 and 8, which assert there is NO WARRANTY that this app is safe to use or bug free, and in fact that by using this code you accept that none of the contributors shall be liable for any damages or monetary loss that results from your use of their code, even if due to bugs in that code. In short, according to the license the app is distributed under, there are no situations in which you could sue any of the developers (it's as if you wrote the entire app yourself). If you can't handle that, don't use this app.

## Download.

Find the apk files in https://github.com/ivoryguru/aeonwallet/tree/main/app/release. Most android phones will use the `app-arm64-v8a-release.apk`.

## Build instructions.

Some minor changes to aeon source code are necessary before compiling. Please see the changes https://github.com/ivoryguru/aeon/tree/aeonwallet.

Install these: `sudo apt-get install make python libtool g++ libncurses5 cmake build-essential git pkg-config libboost-all-dev libssl-dev libzmq3-dev libunbound-dev libsodium-dev libunwind-dev liblzma-dev libreadline-dev libldns-dev libexpat1-dev doxygen graphviz libpgm-dev libnorm-dev`

Build scripts require `ndk-r15c` in `/opt/android/`. See `env.sh`. https://developer.android.com/ndk/downloads/older_releases

Next, change directory to `external-libs` and run `make`. This will create the necessary lib files for the app.

After this is all completed, you will be able to open the app in Android Studio and build.

## How To Help.

Please report any issues using the app. 
Also, help translating and adding reliable nodes is appreciated. 
Translations available for 中文 aeon wallet.

## What is Aeon?

Aeon is a cryptocurrency based on the Cryptonote Protocol. It is intended to be a minimal implementation with a goal of less encryption bloat and faster hashing algorithms ([KangarooTwelve](https://keccak.team/kangarootwelve.html)) while maintining the same private transactions and balances. Find more information https://github.com/aeonix/aeon.

## Thanks.

Cheers to m2049r for the exemplary app xmrwallet and build scripts. https://github.com/m2049r/xmrwallet.
Also thanks to members of the Aeon community for their continued effort in realizing an ideal cryptocurrency.

## License

Copyright 2020 ivoryguru

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
