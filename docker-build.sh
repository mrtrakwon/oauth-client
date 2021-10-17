#!/bin/bash

cd /home/geekseong/Projects/oauth-client
./gradlew clean build
docker rmi oauth-client:latest
docker build -t oauth-client .
