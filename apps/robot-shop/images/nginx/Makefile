container: check build-container
push: check push-container
run: check run-container

check:
ifndef IMG
	$(error IMG is undefined)
endif

build-container:
	docker build . -t ${IMG}

push-container:
	docker push ${IMG}

run-container:
	docker run --name robot-shop-nginx -d -it ${IMG} 
