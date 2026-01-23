#!/bin/bash

set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
K8S_DIR="${SCRIPT_DIR}/k8s"

usage() {
    echo "Usage: $0 [command]"
    echo ""
    echo "Commands:"
    echo "  build       Build all Docker images"
    echo "  push        Push images to registry"
    echo "  deploy      Deploy to Kubernetes"
    echo "  local       Start local development with docker-compose"
    echo "  stop        Stop local development environment"
    echo "  logs        Show logs from local environment"
    echo "  clean       Clean up resources"
    echo ""
}

build_images() {
    echo "Building Docker images..."
    docker build -t flight-tracker/api-service:latest ./api-service
    docker build -t flight-tracker/ingestion-service:latest ./ingestion-service
    docker build -t flight-tracker/webapp:latest ./webapp
    echo "Build complete!"
}

push_images() {
    REGISTRY=${1:-"ghcr.io"}
    OWNER=${2:-"$(git config user.name | tr '[:upper:]' '[:lower:]' | tr ' ' '-')"}

    echo "Pushing images to ${REGISTRY}/${OWNER}..."

    for service in api-service ingestion-service webapp; do
        docker tag flight-tracker/${service}:latest ${REGISTRY}/${OWNER}/flight-tracker-${service}:latest
        docker push ${REGISTRY}/${OWNER}/flight-tracker-${service}:latest
    done

    echo "Push complete!"
}

deploy_k8s() {
    NAMESPACE=${1:-"flight-tracker"}

    echo "Deploying to Kubernetes namespace: ${NAMESPACE}"

    kubectl apply -f "${K8S_DIR}/namespace.yml"
    kubectl apply -f "${K8S_DIR}/secrets.yml"
    kubectl apply -f "${K8S_DIR}/redis.yml"
    kubectl apply -f "${K8S_DIR}/postgres.yml"
    kubectl apply -f "${K8S_DIR}/elasticsearch.yml"
    kubectl apply -f "${K8S_DIR}/api-deployment.yml"
    kubectl apply -f "${K8S_DIR}/api-service.yml"
    kubectl apply -f "${K8S_DIR}/ingestion-deployment.yml"
    kubectl apply -f "${K8S_DIR}/ingestion-service.yml"
    kubectl apply -f "${K8S_DIR}/webapp-deployment.yml"
    kubectl apply -f "${K8S_DIR}/webapp-service.yml"
    kubectl apply -f "${K8S_DIR}/ingress.yml"

    echo "Deployment complete!"
    echo ""
    echo "Check status with: kubectl get pods -n ${NAMESPACE}"
}

start_local() {
    echo "Starting local development environment..."
    docker-compose up -d
    echo ""
    echo "Services starting..."
    echo "  API Service:    http://localhost:8080"
    echo "  Webapp:         http://localhost:3000"
    echo "  PostgreSQL:     localhost:5432"
    echo "  Redis:          localhost:6379"
    echo "  Elasticsearch:  http://localhost:9200"
    echo ""
    echo "View logs with: $0 logs"
}

stop_local() {
    echo "Stopping local development environment..."
    docker-compose down
    echo "Stopped!"
}

show_logs() {
    docker-compose logs -f
}

clean_up() {
    echo "Cleaning up..."
    docker-compose down -v --rmi local
    echo "Clean up complete!"
}

case "${1}" in
    build)
        build_images
        ;;
    push)
        push_images "${2}" "${3}"
        ;;
    deploy)
        deploy_k8s "${2}"
        ;;
    local)
        start_local
        ;;
    stop)
        stop_local
        ;;
    logs)
        show_logs
        ;;
    clean)
        clean_up
        ;;
    *)
        usage
        exit 1
        ;;
esac
