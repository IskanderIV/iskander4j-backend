#!/bin/bash
set -e
echo "docker-compose down"
docker-compose down
echo "remove ./data folder contained"
rm -r ./data/*
