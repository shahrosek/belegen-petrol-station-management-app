# Belegen: Petrol Station Management Android App (SRS)

This repository contains the complete Software Requirements Specification (SRS) document for **Belegen**, a comprehensive mobile application designed to manage the daily operations of a petrol station. This project was developed as part of my university's Software Engineering course.

## 🎯 Objective
The goal of "Belegen" is to replace inefficient, paper-based record-keeping with a secure, reliable, and user-friendly Android application. The app serves as a centralized hub for managing sales, inventory, employee attendance, and contractor accounts.

## 📋 Key Features and Modules

* **Dashboard**: Provides the manager with a high-level overview of monthly sales summaries and current fuel inventory levels, including thresholds for re-ordering supplies.
* **Sales & Stock Management**:
    * Log daily sales for petrol, diesel, and lubricants for each filling point.
    * Update fuel and lubricant prices in real-time.
    * Record new stock arrivals to maintain accurate inventory.
* **Employee Management**:
    * Add, update, and view employee profiles.
    * A dedicated interface for managers to mark daily employee attendance.
    * Employees can log in to view their own attendance records.
* **Contractor & Cash Flow Management**:
    * Manage a list of contract-based customers.
    * Record payments made by contractors.
    * Log all business expenses (tyre shop, wages, borrowed money, etc.) in a central cash flow module.
* **User Authentication**: Secure login system for both Manager and Employee roles with password recovery options.

## 🛠️ Specified Tech Stack
* **Platform**: Android (min. API level 18 - Jelly Bean)
* **Language**: Java
* **IDE**: Android Studio
* **Database**: Google Firebase Firestore (for real-time data synchronization)
