#!/bin/bash -xe

PANDOC_VERSION="3.7.0.2"
PANDOC_FILE_NAME="pandoc-${PANDOC_VERSION}-1-amd64.deb"
wget "https://github.com/jgm/pandoc/releases/download/${PANDOC_VERSION}/${PANDOC_FILE_NAME}"
sudo dpkg -i "${PANDOC_FILE_NAME}"
pandoc --version
