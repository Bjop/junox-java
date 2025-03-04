# junox-java

[![Issues](https://img.shields.io/github/issues/Bjop/junox-java)](https://github.com/Bjop/junox-java/issues)
[![License](https://img.shields.io/github/license/Bjop/junox-java)](LICENSE)
[![License](https://img.shields.io/github/contributors/Bjop/junox-java
)](LICENSE)


Welcome to the *junox-java* project! This project is a REST API designed to **upload, execute, and retrieve Jupyter notebooks**. The application is built using Jakarta EE (with jakarta imports), Spring MVC, and Lombok, and runs on Java SDK version 21. It demonstrates robust web application development using modern Java frameworks and best practices in software design.

## Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Getting Started](#getting-started)
- [Project Structure](#project-structure)
- [Usage](#usage)
- [Contributing](#contributing)
- [License](#license)
- [Buy Me a Coffee](#buy-me-a-coffee)

## Overview

*junox-java* is a REST API application that provides endpoints to:
- **Upload Jupyter Notebooks**: Securely upload notebook files to the server.
- **Execute Notebooks**: Run the uploaded Jupyter notebooks on demand.
- **Retrieve Results**: Access the execution results, logs, or output files via API calls.

This application leverages Jakarta EE for enterprise-grade features, Spring MVC for building a RESTful web service, and Lombok to reduce boilerplate code in Java.

## Features

- **Jupyter Notebook Management**: Upload, execute, and retrieve notebook files through REST APIs.
- **Jakarta EE Integration**: Utilizes jakarta libraries for robust enterprise Java development.
- **Spring MVC**: Implements a RESTful API architecture with ease.
- **Lombok**: Reduces boilerplate code with annotations for getters, setters, and constructors.
- **Modern Java**: Built on Java SDK version 21, taking advantage of the latest Java features.

## Getting Started

To start exploring the project, follow these steps:

1. **Clone the repository:**

   ```bash
   git clone https://github.com/Bjop/junox-java.git
   ```

2. **Navigate to the project directory:**

   ```bash
   cd junox-java
   ```

3. **Build the project:**

   Use your favorite build tool (e.g., Maven or Gradle). For example, using Maven:

   ```bash
   mvn clean install
   ```

4. **Run the application:**

   You can run the application using your IDE (IntelliJ IDEA 2024.3.4 Ultimate Edition on Windows 11) or via the command line if you have a configured environment.

## Usage

After setting up the project, you can explore the provided REST endpoints to manage Jupyter notebooks. You can:
- Upload a notebook file using a POST endpoint.
- Trigger notebook execution via a dedicated API call.
- Retrieve outputs or execution results using GET endpoints.

Refer to the API documentation for details on endpoint routes and payload formats.

## Contributing

Contributions are welcome! Please fork the repository and submit your pull requests. For major changes, please open an issue first to discuss what you would like to change.

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

## Buy Me a Coffee

If you find this project useful and appreciate the effort put into its development, consider supporting the project by buying me a coffee:

[Buy Me a Coffee](https://buymeacoffee.com/bjop)

Thank you for your support!