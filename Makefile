.DEFAULT_GOAL := build-run

setup:
	make -C app setup
		
clean:
	make -C app clean

build:
	make -C app build
	
start:
	make -C app start

install:
	make -C app install

run-dist:
	make -C app run-dist

run:
	make -C app run

test:
	make -C app test

report:
	make -C app report

lint:
	make -C app lint
	
	
check-updates:
	make -C app check-updates
	
image-build:
	make -C app image-build
	
image-push:
	make -C app image-push
	
build-run: build run

.PHONY: build
