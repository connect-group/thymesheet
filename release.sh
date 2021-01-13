#!/bin/bash
set -e -x

mvn clean deploy -P sonatype-oss-release
mvn nexus-staging:release

