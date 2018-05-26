FROM openjdk:8-jdk-alpine
MAINTAINER Swapnil Patil<Swapnil.patil1682@gmail.com>
#RUN apt-get update
VOLUME /tmp
ENV url https://api.lyrics.ovh/v1/coldplay/paradise
ENV lyricsUrl https://api.lyrics.ovh/v1/coldplay/paradise
COPY en-pos-maxent.bin /tmp
COPY target/gs-actuator-service-0.1.0.jar /tmp
EXPOSE 9100 9002
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/tmp/gs-actuator-service-0.1.0.jar"]

