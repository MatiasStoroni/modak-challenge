# 📩 Notification Rate Limiter

A simple Java console application that simulates sending notifications with **rate-limiting rules**.  

It allows you to define limits (per notification type and time window) and prevents users from exceeding them.

---
## ✨ Features
- Define rate limit rules by **notification type** (e.g., `NEWS`, `STATUS`, etc.).  
- Supported time windows: **SECOND, MINUTE, HOUR, DAY**.  
- Interactive **console mode** to simulate sending notifications.  
- Prevents users from exceeding the allowed number of messages.  
- Unit tests included (with JaCoCo coverage).

---
## ⚙️ Requirements
- Java 17+  
- Maven 3.6+  
---
## 🚀 Run the App
1. Clone the repository:  
   `git clone https://github.com/yourusername/notification-rate-limiter.git`  
   `cd notification-rate-limiter`  
2. Build the project:  
   `mvn clean install`  
3. Run the app:  
   `mvn exec:java`  
---
## 🖥️ Console Usage
When running, the app will:  
1. Show the **existing rate limit rules**.  
2. Ask you for:
   - Notification type  
   - User ID  
   - Number of messages (max 6)  
#### Example session:  
`Existing rules:`
- `Type: news, Max: 3 per MINUTE`
- `Type: status, Max: 5 per HOUR`

`Enter notification type (or 'exit'):`
`news`  

`Enter userId:`
`user1`  

`Enter number of messages to send (max 6):`
`4`  

`✅ Sent notification #1 for user user1 (news)`
`✅ Sent notification #2 for user user1 (news)`
`❌ Rate limit exceeded for user user1 (news)`
`❌ Rate limit exceeded for user user1 (news)` 

---
## 🧪 Run Tests
To run all unit tests:
`mvn test`
#### Reports (JaCoCo)
Coverage report can be found at:
`target/site/jacoco/index.html`.  

---
## 📝 Notes for Reviewers

- The **console mode** is just a demo; in a real system, the `NotificationService` should be integrated with APIs or message queues.  
- Models and logic are covered with **JUnit + JaCoCo**.
- Max number of messages in one batch = **6** (to avoid spam).  
---
## 📂 Project Structure
`src/`
`├── main/java/com/challenge/notifications/`
`│   ├── model/         # Data models (RateLimitRule, NotificationEvent, TimeWindow)
`│   ├── service/       # NotificationService and implementation`
`│   └── NotificationApplication.java   # Main console entry point`
`└── test/java/...      # Unit tests`