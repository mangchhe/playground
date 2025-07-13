#!/bin/bash

echo "Stopping and removing containers (volumes intact)..."
docker-compose down

echo "Containers stopped!"