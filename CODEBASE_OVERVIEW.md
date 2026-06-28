# OneStitches - Project Overview

## Project Summary
'OneStitches' is a specialized SAAS platform for digitized embroidered clothing. It is currently in the early development phase, following a microservices architecture intended to scale with business growth.

## Technical Stack
- **Backend:** Spring Boot 3, Java 17
- **Frontend:** Next.js 14 (App Router), TypeScript
- **Infrastructure:** MongoDB, Kafka, Redis, Docker
- **Build System:** Unified Gradle build managing both Java and Node.js components

## Project Structure
```text
onestitches/
├── backend/                # Microservices directory
│   ├── inventory/          # Product catalog and stock management
│   └── order/              # Order processing service
├── frontend/               # Next.js web application
├── build.gradle            # Root build orchestrator
└── README.md               # Extensive design documentation and roadmap
```

## Key Components

### 1. Microservices Architecture
The project is designed as a suite of services:
- **Inventory Service**: Manages the product catalog (currently provides stubbed endpoints).
- **Order Service**: Handles order creation and tracking (scaffolded).
- **Planned Services**: Notification, QR tracking, and Real-time services are identified in the roadmap.

### 2. Modern Frontend
The frontend leverages Next.js 14 for server-side rendering and a modern UI experience. It currently features a functional product catalog and detail view that consumes data from the inventory microservice.

### 3. Unified Development Experience
A root `build.gradle` file allows for building both the backend and frontend components in a single workflow. A multi-stage `Dockerfile` is provided for containerized deployment.

## Note
While the `README.md` provides an exceptionally detailed architectural vision (HLD/LLD), the actual implementation is still in the early 'stub' and 'scaffolding' phase.
