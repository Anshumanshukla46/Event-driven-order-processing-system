### ✅ Run Zookeeper and Kafka via Docker

Open terminal and run:

# Zookeeper
docker run -d --name zookeeper -p 2181:2181 -e ZOOKEEPER_CLIENT_PORT=2181 confluentinc/cp-zookeeper:7.4.1

# Kafka
docker run -d --name kafka -p 9092:9092 -e KAFKA_ZOOKEEPER_CONNECT=zookeeper:2181 -e KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://localhost:9092 -e KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR=1 --link zookeeper confluentinc/cp-kafka:7.4.1


✅ After a few seconds, Kafka will be running on `localhost:9092`.

---

## 🧱 Step 2: Create Spring Boot REST API Project (order-api)

### 📦 Inside order-api

#### 2.1 Create `Order.java`
---

#### 2.2 Create `OrderController.java`
---

#### 2.3 Create Kafka Config
---

#### 2.4 Edit `application.yml`

Create this file in `src/main/resources`:


spring:
kafka:
bootstrap-servers: localhost:9092

---

### 🏁 Run the Project

1. Make sure Kafka is running (check Docker containers).
2. Start the Spring Boot app (`order-api`).
3. Test with Postman / curl:


POST http://localhost:8081/orders

{
"id": "1",
"product": github"
}

✅ Order will be sent to Kafka.

---
You should see both containers up:
- Zookeeper → `localhost:2181`
- Kafka → `localhost:9092`
---


Next Step: **Create the Consumer App (`order-consumer`)**

## ✅ Step 1: Create a Spring Boot Kafka Consumer Project

---

## 🏗 Step 2: Kafka Consumer Configuration

---

## 📥 Step 3: Create the Kafka Listener


## ✅ Step 4: application.yml

spring:
kafka:
bootstrap-servers: localhost:9092
consumer:
group-id: order-consumer-group
auto-offset-reset: earliest
key-deserializer: org.apache.kafka.common.serialization.StringDeserializer
value-deserializer: org.apache.kafka.common.serialization.StringDeserializer

---

## ✅ Step 5: Run Everything

- Start Zookeeper
- Start kafka
- Run your **Producer** (order-API service)
- Then run this **Consumer**

You should see logs like:


---
Some finding:
### 🧠 1. Problem you faced
When you tried to set retry mechanism in your Kafka consumer,
you copied older Spring Kafka 2.x code:
```java
factory.setErrorHandler(new SeekToCurrentErrorHandler(new FixedBackOff(5000L, 3)));
```
but **Spring Kafka 3.x** (which you're using) **has removed this style**.

That’s why you got these two errors:
- `setErrorHandler()` → method **no longer exists** ❌
- `SeekToCurrentErrorHandler` → class **no longer exists** ❌

---

### 🛠 2. Why it broke
Because **Spring Kafka team changed the retry error handling system** in version **3.0**.
They introduced a **new way**:
- **ErrorHandler** → **CommonErrorHandler**
- **SeekToCurrentErrorHandler** → **DefaultErrorHandler**

⚡ Basically:
> *Old error handling classes and methods were deleted and replaced with new ones.*

---

### 🏗 3. What the fix does
Now instead of using old stuff,
✅ you create a `DefaultErrorHandler` bean that tells Kafka:
- "If an error happens, **retry this message** 3 times with **5 seconds gap**."

✅ Then you set this error handler inside your Kafka container factory using:
```java
factory.setCommonErrorHandler(errorHandler());
```
✅ And that’s it!
Kafka will **automatically retry** your processing when a temporary error (like DB down) happens.
---

# 📦 Important Points to Remember

- You must have a `KafkaTemplate<Object, Object>` bean already configured (you usually have it if you're using `@KafkaListener` projects).
- Failed messages after retries will go to a new topic named automatically like:
  ➔ original-topic-name + `.DLT` suffix
  (e.g., `order-topic` → `order-topic.DLT`)

- In DLT topic, you can **analyze**, **reprocess**, or **alert** based on failed messages.

---
