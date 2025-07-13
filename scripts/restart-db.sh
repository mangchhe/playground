#!/bin/bash

echo "Stopping and removing containers & volumes..."
docker-compose down -v

echo "Starting containers..."
docker-compose up -d

echo "Containers are up and running!"