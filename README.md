# FlightTracker – README

A full **Java 21** real‑time flight tracking system built as **one Gradle multi‑module project** and deployed on **AWS Kubernetes** so anyone can access it publicly.

---

## 📦 Project Structure (Gradle Multi‑Module)

```
flight-tracker/
  build.gradle
  settings.gradle

  ingestion-service/
  api-service/
  webapp/
  common/
```

### **ingestion-service**

Fetch live flight data → detect land/depart events → write to PostgreSQL + Elasticsearch.

### **api-service**

Public REST API → search flights → real‑time updates using SSE/WebSocket.

### **webapp**

Java + Thymeleaf UI → visible to users → shows live flight map.

### **common**

Shared models, DTOs, utilities.

---

## 🛠️ Technology Used

* **Java 21**
* **Spring Boot (WebFlux + MVC)**
* **PostgreSQL** (flight storage)
* **Elasticsearch** (search)
* **Gradle** (build + modules)
* **Docker** (images)
* **Kubernetes** (AWS EKS)
* **SSE/WebSocket** (live updates)

---

## 🌍 Deployment (Public Access)

Each module is built by Gradle → packaged as Docker → deployed in Kubernetes.

### Kubernetes Service Types:

* `ingestion-service` → **ClusterIP** (internal only)
* `api-service` → **LoadBalancer** (public)
* `webapp` → **LoadBalancer** (public website)

### Internal service-to-service communication:

```
http://ingestion-service
http://api-service
http://postgres
http://elasticsearch
```

### Public user access:

```
https://yourdomain.com
```

---

## 🚀 Build & Deploy

### Build everything

```
./gradlew build
```

### Build Docker images

```
docker build -t flight-api ./api-service
```

### Deploy to Kubernetes

```
kubectl apply -f k8s/
```

---

## ✔ Summary

One Gradle project → multiple Java services → live flight tracking → deployed on AWS → accessible to everyone.
