#!/bin/bash

SERVER=''

# コンフィグファイルを読み込み
. ./server.conf

while true;do

# プラグインをコピー
echo "Copying build plugin"
sh copy.sh

# サーバー起動
cd ./server
java -Dfile.encording=UTF-8 -Xms2G -Xmx2G -jar ${SERVER}.jar
cd ../

echo "Restarting in 3 seconds,please wait..."
sleep 3s

done