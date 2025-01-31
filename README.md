# ChainAbuse

ChainAbuse is a modular software application designed to analyze and identify fraudulent Bitcoin addresses by integrating with the ChainAbuse API. It incorporates advanced design patterns such as Mediator, Observer, Singleton, and Factory Method, ensuring the project is scalable, maintainable, and easy to extend.

---

## Table of Contents
- [Features](#features)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
- [Installation Guide and Running the Application in Eclipse](#installation-guide-and-running-the-application-in-eclipse)
  - [1. Import the Project](#1-import-the-project)
  - [2. Configure the Project's Build Path](#2-configure-the-projects-build-path)
  - [3. Configure the API Key](#3-configure-the-api-key)
  - [4. Set VM Arguments](#4-set-vm-arguments)
  - [5. Run the Application](#5-run-the-application)
  - [Troubleshooting](#troubleshooting)
- [Design Patterns in Use](#design-patterns-in-use)
- [Contributing](#contributing)
- [License](#license)

---

## Features

- **API Integration**: Connects to the ChainAbuse API to retrieve and analyze Bitcoin address data.
- **Design Patterns**: Implements key patterns for modular and maintainable code.
- **Automated JSON Parsing**: Efficiently handles API responses for quick data analysis.
- **Report Generation**: Automatically generates detailed reports based on analysis results.

---

## Getting Started

### Prerequisites

1. **Eclipse IDE**: Download and install the latest version of [Eclipse IDE](https://www.eclipse.org/downloads/).
2. **Java Development Kit (JDK)**: Ensure JDK 8 or higher is installed.
3. **Maven**: Eclipse should support Maven for dependency management.
4. **ChainAbuse API Key**: Register and obtain an API key from [ChainAbuse](https://www.chainabuse.com/).

---

## Installation Guide and Running the Application in Eclipse

### Watch the Video Guide
[![Watch the video](https://img.youtube.com/vi/alrE4E7z82s/0.jpg)](https://www.youtube.com/watch?v=alrE4E7z82s)
*This video demonstrates how to set up and run the ChainAbuse project in Eclipse.*

---

### 1. Import the Project
1. Open **Eclipse IDE**.
2. Navigate to `File > Open Projects from File System`.
3. In the dialog box, select the project folder:
   - Browse to the `ChainAbuse` project directory.
   - Click **Finish** to import the project into Eclipse.

---

### 2. Configure the Project's Build Path
1. Right-click on the project `ChainAbuse` in the Project Explorer and select **Properties**.
2. Navigate to **Java Build Path > Libraries**.
3. Perform the following steps to add the required JAR files:
   - **Modulepath**:
     1. Select the **Modulepath** entry and click **Add External JARs**.
     2. Navigate to `ChainAbuse-master/jars/javafx-sdk-17.0.1/lib`.
     3. Select all the `.jar` files in this folder and click **Open** to add them.
   - **Classpath**:
     1. Select the **Classpath** entry and click **Add External JARs**.
     2. Navigate to `ChainAbuse-master/jars`.
     3. Select the following JAR files and click **Open**:
        - `json-20210307.jar`
        - `poi-3.17.jar`

4. Click **Apply and Close** to save the build path changes.

---

### 3. Configure the API Key
1. **Register and Obtain an API Key**:
   - Visit [ChainAbuse](https://docs.chainabuse.com/reference/reports-1) to register and obtain an API key.

2. **Generate the Basic Authorization Header**:
   - In the **Basic** section of the ChainAbuse documentation:
     - Use your API key as the **username**.
     - Use your API key as the **password**.
   - Copy the **Basic Authorization Header** string generated in the documentation.

3. **Update the `api_json.json` File**:
   - Navigate to the file at `ChainAbuse-master\txt\api_json.json`.
   - Open the file in a text editor or IDE.
   - Paste the copied **Basic Authorization Header** string into the appropriate field in the JSON file.

---

### 4. Set VM Arguments
1. In the Project Explorer, navigate to the `src > client` folder.
2. Right-click on the `MainGui` class and select **Run As > Run Configurations**.
3. In the **Run Configurations** dialog:
   - Select the `MainGui` configuration.
   - Navigate to the **Arguments** tab.
   - In the **VM arguments** section, add the following:
     ```
     --module-path "path-to-ChainAbuse-master/jars" --add-modules javafx.controls,javafx.fxml
     ```
     Replace `path-to-ChainAbuse-master/jars` with the actual path to the `jars` directory on your system. For example:
     ```
     --module-path "C:\Users\emanu\Desktop\ChainAbuse-master\jars" --add-modules javafx.controls,javafx.fxml
     ```

4. Press **Apply** to save the configuration.

---

### 5. Run the Application
1. In the same **Run Configurations** dialog, click **Run**.
2. The application will launch, and the GUI will be displayed.

---

### Troubleshooting
- **Missing JAR Files**: Ensure all required JAR files are correctly added to the `Modulepath` and `Classpath`.
- **Invalid API Key**: Double-check the Basic Authorization Header in `api_json.json`.
- **JavaFX Errors**: Verify the `--module-path` points to the correct `jars` directory.

---

## Design Patterns in Use

- **Mediator**: Manages communication between components, reducing direct dependencies.
- **Observer**: Implements a subscription mechanism for events and notifications.
- **Singleton**: Ensures that a class has a single instance and provides a global access point.
- **Factory Method**: Abstracts the creation of objects for flexibility and scalability.

---

## Contributing

Contributions are welcome! To contribute:
1. Fork the repository on GitHub.
2. Clone your fork locally:
   ```bash
   git clone https://github.com/your-username/ChainAbuse.git
