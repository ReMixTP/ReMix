IP=$(shell ifconfig -a | grep en0 -A10 | awk '/inet / {print $$2}')

all: docker

docker:
	docker build -t "remix/ui" .

run:
	docker run -d -v '$(shell pwd)/src':/var/www/html -p 8000:80 --rm "remix/ui"
