#FROM registry.access.redhat.com/ubi8/ubi-minimal
FROM golang:1.17.8-alpine AS build-env
RUN mkdir /build
WORKDIR /build
COPY apps/todolist-mongo-go/*.go .
COPY apps/todolist-mongo-go/go.mod .
COPY apps/todolist-mongo-go/go.sum .
RUN go mod download
RUN go mod tidy
RUN CGO_ENABLED=0 GOOS=linux GOARCH=amd64 go build -v -a -installsuffix cgo -o app

FROM scratch
COPY --from=build-env /build/app /app
COPY apps/todolist-mongo-go/resources/ /resources/
COPY apps/todolist-mongo-go/index.html .
EXPOSE 8000
# use entrypoint for debug
#ENTRYPOINT ["tail", "-f", "/dev/null"]
CMD ["./app"]
