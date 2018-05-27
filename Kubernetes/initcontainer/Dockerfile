FROM ubuntu:14.04
MAINTAINER Swapnil Patil<Swapnil.patil1682@gmail.com>
RUN apt-get update
VOLUME /tmp
COPY testpublicapi.sh /tmp
RUN chmod +x /tmp/testpublicapi.sh
ENTRYPOINT ["sh /tmp/testpublicapi.sh"]
