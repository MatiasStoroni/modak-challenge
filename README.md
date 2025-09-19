# 📩 Notification Rate Limiter API

A RESTful API built with Spring Boot that manages the sending of **notifications** with **rate-limiting rules**. It allows defining rate-limit rules by notification type and time window, ensuring the system does not exceed those limits.

The project follows a **layered architecture**, with a clear separation of concerns across different layers: **controllers** (handling HTTP requests), **services** (containing business logic), and **models** (representing data entities and DTOs).

---

## ✨ Features
- REST API for sending notifications with rate-limiting control.  
- Define rules by notification type and time window (MINUTE, HOUR, DAY, WEEK).  
- Supports multiple rules simultaneously (e.g., per-minute and per-hour limits).  
- Throws RateLimitExceededException when a rule is violated.
- In-memory persistence for notification events and rules (demo mode).  
- Unit tests with JUnit + Mockito.
- Docker-ready: Can be built and run in a minimal Docker container or via Docker Compose for easy testing and deployment.

---
## ⚙️ Requirements
- Java 17+  
- Maven 3.9+
- Docker & Docker Compose (optional, if running via Docker)
---

## 🚀 Run the API

### Option 1: Using Maven
1. **Clone the repository**:

`git clone https://github.com/MatiasStoroni/modak-challenge.git`

`cd modak-challenge`

2. **Build the project**:

`mvn clean install`

3. **Start the Spring Boot server**:

`mvn spring-boot:run`

4. **The server will be available at**:

`http://localhost:8080`

### Option 2: Using Docker

1. Make sure Docker and Docker Compose are installed.
2. Clone the repository:

`git clone https://github.com/MatiasStoroni/modak-challenge.git`

`cd modak-challenge`

4. Start the application using Docker Compose:

`docker-compose up --build`

4. The server will be available at:

`http://localhost:8080`

5. To stop the containers:

`docker-compose down`

---

## 📡 Main Endpoints

### Send notification
`POST /notifications/send`
```
JSON body example:  
{
    "notificationType": "NEWS",
    "userId": "user",
    "message": "News message number 1"
}
```
#### Responses:  
- **200 OK** → "Notification sent successfully"  
- **429 Too Many Requests** → "Rate limit exceeded for *{ NotificationType }* notifications sent to user *{ UserId }*"
- **400 Bad Request** - "Validation failed: *{ field }*: *{ field }* is required"

---

### Create rule
`POST /rules`
```
JSON body example:  
{
    "notificationType": "NEWS",
    "maxNotifications": 1,
    "timeWindow": "DAY"
}
```
#### Responses:  
- **200 OK** → { *Created Rule body* }  
- **400 Bad Request** - "Validation failed: *{ field }*: *{ field }* is required"

---

### Get active rules
`GET /rules` 
#### Example response: 
- **200 OK**
```
[
    {
        "id": 1,
        "notificationType": "NEWS",
        "maxNotifications": 1,
        "timeWindow": "DAY"
    },
    {
        "id": 2,
        "notificationType": "STATUS",
        "maxNotifications": 2,
        "timeWindow": "MINUTE"
    },
    {
        "id": 3,
        "notificationType": "MARKETING",
        "maxNotifications": 3,
        "timeWindow": "HOUR"
    }
]
```
---

### Get notification history
`GET /events`
#### Example response:  
- **200 OK**
```
[
    {
        "id": 1,
        "notificationType": "NEWS",
        "userId": "user",
        "timestamp": "2025-09-18T20:42:27.892732"
    },
    {
        "id": 2,
        "notificationType": "STATUS",
        "userId": "user",
        "timestamp": "2025-09-18T20:48:06.238313"
    }
]
```

---

## 🧪 Testing
**Run unit tests**:

`mvn clean test`

### Test Coverage
All business logic is covered by unit tests in `service/NotificationServiceTest.java`.
These tests use **Mockito** to isolate the service from its dependencies (`Gateway`, `RateLimitRuleService`, `NotificationEventService`).

Main test scenarios include:
- No rules applied → notification is sent successfully.
- Rule within limit → multiple notifications are allowed without reaching the limit.
- Rule exceeded → `RateLimitExceededException` is thrown.
- Existing event within time window → ensures a notification is not sent if a previous event exists.
- Different users → notifications are counted independently per user.
- Multiple rules for the same notification type → stricter rule applies.
---

## 📝 Notes
- This is a **demo** implementation: data is persisted in-memory. In production, it would connect to a database or external messaging system.
- **NotificationServiceImpl** encapsulates the core rate-limiting logic.
- **RateLimitRuleService** and **NotificationEventService** handle rules and events.
- **Gateway** is a **stub** simulating an external messaging integration.

---

📂 Project Structure
```
src/  
├── main/java/com/challenge/notifications/  
│   ├── model/         # Data models (RateLimitRule, NotificationEvent, TimeWindow)
│   ├── service/       # NotificationServiceImpl, RuleService, EventService  
│   ├── controller/    # REST controllers  
│   ├── exception/     # Custom exceptions and global handler  
│   └── NotificationApplication.java           # Main class  
└── test/java/com/challenge/notifications      # Unit tests  
```
