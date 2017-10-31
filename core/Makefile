all: docker

docker: target/universal/remix-core-0.1.0.zip Dockerfile
	docker build -t "remix/core" .

target/universal/remix-core-0.1.0.zip: $(shell find src -type f) $(shell find project -type f) build.sbt
	sbt dist

run:
	docker run -p 9000:9000 -it --rm remix/core remix-core
