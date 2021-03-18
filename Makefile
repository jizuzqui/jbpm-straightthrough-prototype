
.PHONY: run_local
run_local:
	 ./mvnw spring-boot:run

.PHONY: build
build:
	 ./mvnw clean package

.PHONY: image
image: 
	docker build -t globaldevtools.bbva.com:5000/hub/bpmaas/custom/orchestrator:latest .

.PHONY: push
push:
	docker push globaldevtools.bbva.com:5000/hub/bpmaas/custom/orchestrator:latest

.PHONY: run
run:
	docker run -p 8080:8080  -p 8443:8443 globaldevtools.bbva.com:5000/hub/bpmaas/custom/orchestrator:latest

.PHONY: run-bash
run-bash:
	docker run -i -t globaldevtools.bbva.com:5000/hub/bpmaas/custom/orchestrator:latest /bin/bash
