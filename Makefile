.PHONY: build run

build:
	mvn clean install
run:
	java -jar target/tasklist-0.0.1-SNAPSHOT.jar


