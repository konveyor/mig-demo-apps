#FROM golang:1.17.8-alpine
FROM ubi8
USER root
RUN dnf install -y golang vim
COPY resources/ /resources/
COPY index.html .
COPY app .
# dev
#WORKDIR /go/src/github.com/weshayutin/todolist-mariadb-go
#
#COPY ./ .
#
#RUN chmod -R 777 ./
#RUN go mod download

EXPOSE 8000
# use entrypoint for debug
#ENTRYPOINT ["tail", "-f", "/dev/null"]
CMD ["./app"]
