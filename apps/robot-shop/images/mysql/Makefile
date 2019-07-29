container: check build-container
push: check push-container
run: check run-container

MYSQL_ROOT_PASSWORD ?= R00t@123

check:
ifndef IMG
	$(error IMG is undefined)
endif

build-container:
	docker build . -t ${IMG}

push-container:
	docker push ${IMG}

run-container:
	docker run --name robot-shop-mysql -e MYSQL_ROOT_PASSWORD=${MYSQL_ROOT_PASSWORD} -d -it ${IMG} 
