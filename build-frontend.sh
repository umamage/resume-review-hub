#!/bin/bash

# Frontend Build Script for Render
# Automatically runs on Render

set -e

echo "================================"
echo "Resume Review Hub - Frontend Build"
echo "================================"

echo ""
echo "Step 1: Installing dependencies..."
npm install

echo ""
echo "Step 2: Building application..."
npm run build

echo ""
echo "Step 3: Build complete!"
ls -lh dist/

echo ""
echo "================================"
echo "Build Complete!"
echo "================================"
