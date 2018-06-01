FROM maven:3.5.3-jdk-8
MAINTAINER Chris Sandison <chris@thinkdataworks.com>

ENV APPDIR /src

WORKDIR $APPDIR
RUN mkdir -p $APPDIR

RUN apt-get update -yqq && \
    apt-get install -yqq build-essential chrpath libssl-dev

COPY . $APPDIR

RUN mvn package -e && \
    ls -l target/ > target/target.txt