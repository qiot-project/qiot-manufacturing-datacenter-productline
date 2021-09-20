#!/bin/sh

./mvnw clean package -U -Pnative -Dquarkus.native.container-build=true
docker rmi quay.io/qiotmanufacturing/datacenter-product-line:1.0.0-beta1 --force
docker build -f src/main/docker/Dockerfile.native -t quay.io/qiotmanufacturing/datacenter-product-line:1.0.0-beta1 .
docker push quay.io/qiotmanufacturing/datacenter-product-line:1.0.0-beta1