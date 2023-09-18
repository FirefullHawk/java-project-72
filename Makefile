setup:
	make -C app setup

clean:
	make -C app clean

build:
	make -C app build

start:
	make -C app run

install:
	make -C app install

start-dist:
	make -C app start-dist

lint:
	make -C lint

test:
	make -C test

report:
	make -C jacocoTestReport

check-updates:
	make -C check-updates

image-build:
	make -C image-build

image-push:
	make -C image-push

.PHONY: build

