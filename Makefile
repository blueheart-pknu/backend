# Variables
IMAGE_NAME := blueheart-be
CONTAINER_NAME := blueheart-be-container
VERSION := 1.0.0
PORT := 8080

# Build the Docker image
all:
	docker build -t $(IMAGE_NAME):$(VERSION) . && docker run --name $(CONTAINER_NAME) -p $(PORT):8080 -d $(IMAGE_NAME):$(VERSION)

# Run the application in a container
run:
	docker run --name $(CONTAINER_NAME) -p $(PORT):8080 -d $(IMAGE_NAME):$(VERSION)

# Stop and remove the container
stop:
	docker stop $(CONTAINER_NAME) || true
	docker rm $(CONTAINER_NAME) || true

# Clean up unused Docker resources
clean:
	docker system prune -f

# Push the image to a Docker registry
push:
	docker tag $(IMAGE_NAME):$(VERSION) your-dockerhub-username/$(IMAGE_NAME):$(VERSION)
	docker push your-dockerhub-username/$(IMAGE_NAME):$(VERSION)

# Display logs from the container
logs:
	docker logs -f $(CONTAINER_NAME)