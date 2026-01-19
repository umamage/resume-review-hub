#!/bin/bash

# Render Build Script for Resume Review Hub
# This script is referenced in render.yaml for the backend service

set -e

echo "================================"
echo "Resume Review Hub - Build Script"
echo "================================"

cd "$(dirname "$0")"

echo "Step 1: Checking Java version..."
java -version

echo ""
echo "Step 2: Checking Maven version..."
mvn --version

echo ""
echo "Step 3: Building application..."
mvn clean package -DskipTests -Dspring.profiles.active=prod

echo ""
echo "Step 4: Build successful!"
ls -lh target/resume-review-backend-1.0.0.jar

echo ""
echo "================================"
echo "Build Complete!"
echo "================================"
