.DEFAULT_GOAL := build-run

setup:
	gradle wrapper --gradle-version 8.3

clean:
	./gradlew clean

build:
	./gradlew clean build

run-dev:
	APP_ENV=development ./gradlew run

install:
	./gradlew install

run-prod:
	APP_ENV=production ./gradlew run

lint:
	./gradlew checkstyleMain checkstyleTest

test:
	./gradlew test

report:
	./gradlew jacocoTestReport

check-updates:
	./gradlew dependencyUpdates

image-build:
	docker build -t firefullhawk/java-project-72:latest .

image-push:
	docker push firefullhawk/java-project-72:latest

.PHONY: build
