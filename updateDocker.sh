#! /usr/bin/bash

git pull

# docker hub username
USERNAME=badnarrators
# image name
IMAGE=celos-be
# program version
version=`cat VERSION`
echo "username: $USERNAME"
echo "image: $IMAGE"
echo "version: $version"


docker build -t $USERNAME/$IMAGE:$version .
docker stop $IMAGE
docker wait $IMAGE
docker rm $IMAGE
docker run -d --rm -p 6900:8080 --name $IMAGE $USERNAME/$IMAGE:$version
docker tag $USERNAME/$IMAGE:$version $USERNAME/$IMAGE:latest