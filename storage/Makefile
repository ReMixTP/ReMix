all: docker

volume:
	docker volume create remix_database

docker:
	docker build -t "remix/storage" .

run:
	docker run -d -p 3306:3306 --rm --mount type=volume,src=remix_database,dst=/var/lib/mysql "remix/storage"
	docker ps | grep "remix/storage"

attach:
	docker exec -it $(name) bash
