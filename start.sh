#!/bin/bash

SERVER=''

# コンフィグファイルを読み込み
. ./server.conf

while true;do

# ”world"フォルダー削除
rm -rf server/world

# サーバー起動
cd ./server
java -Dfile.encording=UTF-8 -Xms2G -Xmx2G -jar ${SERVER}.jar
cd ../

echo "Restarting in 10 seconds..."
sleep 10s

done