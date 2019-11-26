build:
	docker build . -t quay.io/dgrigore/ssh-server

push: build
	docker push quay.io/dgrigore/ssh-server

.PHONY: build push
