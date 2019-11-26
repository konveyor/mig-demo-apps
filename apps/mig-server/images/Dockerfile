FROM ubuntu:16.04
RUN apt-get update -y && apt-get install -y openssh-server rsync

COPY launch.sh /bin/
RUN chmod +x /bin/launch.sh

CMD /bin/launch.sh
