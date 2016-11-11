#!/bin/sh

if [ -d "distribution" ]; then
  rm -r  distribution
fi


# build the project and the binaries
mvn clean package

# create the dir and copy the distribution file
mkdir distribution
cp target/*.zip distribution/