# curioso
Curioso is a service to monitor service status exposed in different kind of services.


## Curioso - Multi-Source Service Status Monitor
**Curioso** is a lightweight, extensible service monitoring tool designed to track and aggregate status information from various external services. It provides a unified interface to monitor health, uptime, and performance metrics across different platforms and protocols.

## Key Features
Multi-Source Monitoring: Supports monitoring of services exposed through various protocols (HTTP, gRPC, WebSockets, etc.)

Customizable Checks: Configure frequency, timeout thresholds, and success criteria for each service.

Unified Status Dashboard: Aggregate all service statuses in one convenient view

Alert Integration: Get notified when services go down or exhibit abnormal behavior

Historical Data: Track service uptime and performance trends over time

Extensible Architecture: Easily add support for new service types through plugins

## Use Cases
- API health monitoring
- Microservice status tracking
- Dependency availability checks
- SLA compliance monitoring
- Incident response automation

## Getting Started
- Clone the repository: git clone https://github.com/jpergentino/curioso.git
- Configure your services in services.yaml
- Run the monitor: java -jar curioso.jar

## Contribution
We welcome contributions! Please see our Contributing Guidelines for details.

