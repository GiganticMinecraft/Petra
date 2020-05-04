#!/bin/bash

find build/libs -name "*.jar" | grep build/libs/${PLUGIN} | xargs -i cp {} ./server/plugins

exit