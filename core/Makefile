all: docker

docker: target/universal/remix-core-0.1.0.zip Dockerfile
	docker build -t "remix/core" .

target/universal/remix-core-0.1.0.zip: source $(shell find project -type f) build.sbt
	sbt dist -feature

source: src/ReMix.scala-nw
	notangle -RMakefile $< | unexpand --first-only > src/Makefile
	cd src && make source

run:
	docker run -p 9000:9000 -it --rm remix/core remix-core
