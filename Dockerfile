FROM maven:3.5.3-jdk-8 as build
MAINTAINER Chris Sandison <chris@thinkdataworks.com>

ENV APPDIR /src

WORKDIR $APPDIR
RUN mkdir -p $APPDIR

RUN apt-get update -yqq && \
    apt-get install -yqq build-essential chrpath libssl-dev

COPY . $APPDIR

RUN mvn package -e && \
    mvn javadoc:jar && \
    ls -l target/ > target/target.txt

FROM openjdk:8-jre-alpine

ENV APPDIR /app
ENV VERSION 1.2

WORKDIR $APPDIR
RUN mkdir -p $APPDIR

COPY --from=build "/src/target/namara-$VERSION-SNAPSHOT.jar" namara-$VERSION.jar
COPY --from=build "/src/target/target.txt" target.txt