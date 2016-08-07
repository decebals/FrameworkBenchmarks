#!/bin/bash

fw_depends java maven

mvn clean package

java -Xms2G -Xmx2G -server -XX:+UseNUMA -XX:+UseParallelGC -XX:+AggressiveOpts -jar target/pippo-undertow-0.1.0-SNAPSHOT.jar &