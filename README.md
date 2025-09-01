# Dimediary

A Single-Page Application (SPA) designed to help users take control of their personal finances. This tool allows for the
easy tracking of expenses across different categories and provides insightful financial forecasts to help users plan for
their future.

As a **work in progress**, the project will continue to be developed with a focus on adding new features, such as
budgeting
tools, automated insights, and more detailed reporting, to empower users to not only track but actively improve their
financial well-being.

This project is based on the following JavaFX project: [DimediaryJavaFX](https://github.com/marcuskraft/DimeDiaryJavaFx)

---

## Getting Started 🚀

These instructions will get a copy of the project up and running on your local machine for development and testing
purposes.

### Prerequisites

Make sure you have the following installed:

* **Docker** and **Docker Compose**
* **Java 11**
* **Gradle 6.0.1**
* **Node.js 12** and **npm**

---

## Running the Backend

This project uses Gradle to manage its dependencies and build process.

1. Start the database with Docker Compose:
   ```bash
   docker-compose up -d
   ```
   This command will start a PostgreSQL container in the background.
2. Navigate to the `dimediary/` directory.
3. Build and run the backend using Gradle. Ensure you are using the correct Java and Gradle versions.

   **For Linux/Mac:**
    ```bash
    ./gradlew bootRun
    ```

   **For Windows:**
    ```bash
    ./gradlew.bat bootRun
    ```
   This command compiles the code and starts the backend application.

---

## Running the Frontend

The frontend is a Node.js application. Please use Node.js 12 because the frontend actually can only be built with that
old version.

1. Navigate to the `dimediary/frontend/` directory.
2. Install the necessary dependencies:
   ```bash
   npm install
   ```
3. Start the frontend application:
   ```bash
   npm start
   ```

---

## Running Tests

This project uses **Playwright for Java** for end-to-end testing, with Gradle as the build tool.

1. Ensure both the backend and frontend are running.
2. Navigate to the `playwright/` directory.
3. Run the tests using the appropriate Gradle command for your operating system:

   **For Linux/Mac:**
   ```bash
   ./gradlew systemTest
   ```

   **For Windows:**
   ```bash
   ./gradlew.bat systemTest
   ```

   These commands will execute all tests located in the default test directory (`src/test/java`).