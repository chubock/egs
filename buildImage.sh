#!/usr/bin/env bash

./gradlew clean bootJar
mkdir -p build/dependency && (cd build/dependency; jar -xf ../libs/*.jar)
docker build -t chubock/egs:$(./gradlew properties -q | grep version | awk '{print $2}') .
docker tag chubock/egs:$(./gradlew properties -q | grep version | awk '{print $2}') chubock/egs:latest
