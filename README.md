
# 🏦 Bank Management System

A Spring Boot based Bank Management System to manage Banks, Accounts, Customers, Transactions and Addresses using REST APIs.

This project is built using Java, Spring Boot, Spring Data JPA and MySQL. It follows a proper layered architecture - Controller, Service, Repository and Entity.

---

## ✨ Features

- Create, Get, Update, Delete Bank
- Create, Get, Update, Delete Account
- Create, Get, Update, Delete Customer
- Manage Customer Address
- Deposit, Withdraw and Check Balance
- View Transaction History

---

## 🛠️ Tech Stack

- Java 17
- Spring Boot
- Spring Data JPA
- MySQL Database
- Maven
- Postman for API Testing

---

## 📁 Project Structure

src/main/java/com/bank/
├── controller - REST APIs
├── service - Business Logic
├── repository - Database Logic
└── entity - Tables / Models

src/main/resources/
└── http://application.properties - Database Configuration

---

## ▶️ How To Run This Project

1. Clone this project
2. Open in Eclipse / IntelliJ IDEA
3. Create MySQL database
4. Update username & password in application.properties
5. Run BankManagementSystemApplication.java
6. Project will run on http://localhost:8080

---

## 🔗 Sample APIs

| Method | Endpoint | Description |
| --- | --- | --- |
| GET | /banks | Get All Banks |
| POST | /banks | Create New Bank |
| GET | /accounts | Get All Accounts |
| POST | /accounts | Create New Account |
| GET | /customers | Get All Customers |
| POST | /customers | Create New Customer |

---
