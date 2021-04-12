

DOCKER_VERSION = 0.0.2-SNAPSHOT

.PHONY: run_local
run_local:
	 PROCESS_VERSION="4.0.0-SNAPSHOT" PROCESS_ARTIFACTID="protech_process" PROCESS_GROUPID="es.bbva.dhbemrcu" ./mvnw spring-boot:run

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
	docker run -p 8080:8080  -p 8443:8443 -e PROCESS_VERSION="4.0.0-SNAPSHOT" -e PROCESS_ARTIFACTID="protech_process" -e PROCESS_GROUPID="es.bbva.dhbemrcu" globaldevtools.bbva.com:5000/hub/bpmaas/custom/orchestrator:$(DOCKER_VERSION)

.PHONY: run-bash
run-bash:
	docker run -i -t globaldevtools.bbva.com:5000/hub/bpmaas/custom/orchestrator:$(DOCKER_VERSION) /bin/bash
