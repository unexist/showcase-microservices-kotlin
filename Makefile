 .DEFAULT_GOAL := build

 PODNAME := showcase
 PG_USER := postgres
 PG_PASS := postgres

define JSON_TODO
curl -X 'POST' \
  'http://localhost:8080/todo' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "description": "Todo string",
  "title": "Todo string"
}'
endef
export JSON_TODO

# Tools
todo:
	@echo $$JSON_TODO | bash

list:
	@curl -X 'GET' 'http://localhost:8080/todo' -H 'accept: */*' | jq .

hurl:
	@hurl ./todo.hurl

# Build & run
build-all:
	mvn package

build-ktor:
	mvn package -pl todo-service-ktor

build-quarkus:
	mvn package -pl todo-service-quarkus

run-ktor:
	java -jar todo-service-ktor/target/todo-service-ktor-0.1-jar-with-dependencies.jar

run-quarkus:
	java -jar todo-service-quarkus/target/todo-service-ktor-0.1-jar-with-dependencies.jar

# Podman
pd-machine-init:
	podman machine init --memory=8192 --cpus=2 --disk-size=20

pd-machine-start:
	podman machine start

pd-machine-stop:
	podman machine stop

pd-machine-rm:
	@podman machine rm

pd-machine-recreate: pd-machine-rm pd-machine-init pd-machine-start

pd-pod-create:
	@podman pod create -n $(PODNAME) --network bridge \
		-p 5432:5432 -p 8500:8500

pd-pod-rm:
	podman pod rm -f $(PODNAME)

pd-pod-recreate: pd-pod-rm pd-pod-create

pd-consul:
	@podman run -dit --name consul --pod=$(PODNAME) \
		consul:latest

pd-postgres:
	@podman run -dit --name postgres --pod=$(PODNAME) \
		-e POSTGRES_USER=$(PG_USER) \
		-e POSTGRES_PASSWORD=$(PG_PASS) \
		postgres:latest
