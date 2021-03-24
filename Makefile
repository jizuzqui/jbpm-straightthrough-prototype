

DOCKER_VERSION = 0.0.1-SNAPSHOT

.PHONY: run_local
run_local:
	 ./mvnw spring-boot:run

.PHONY: build
build:
	 ./mvnw clean package

.PHONY: image
image: 
	docker build -t globaldevtools.bbva.com:5000/hub/bpmaas/custom/orchestrator:$(DOCKER_VERSION) .

.PHONY: push
push:
	docker push globaldevtools.bbva.com:5000/hub/bpmaas/custom/orchestrator:$(DOCKER_VERSION)

.PHONY: run
run:
	docker run -p 8080:8080  -p 8443:8443 globaldevtools.bbva.com:5000/hub/bpmaas/custom/orchestrator:$(DOCKER_VERSION)

.PHONY: run-bash
run-bash:
	docker run -i -t globaldevtools.bbva.com:5000/hub/bpmaas/custom/orchestrator:$(DOCKER_VERSION) /bin/bash
