# SpringBlog 🌱 
 <br><img src="https://github.com/leesiha/SpringBlog/assets/66323295/13e5284f-0990-488f-af5b-393a5e9d45bd" width="30" height="30"/> [Traces of study](https://smoggy-chameleon-a1b.notion.site/ab52c1464a8c464590ba3f5c77f90ede?pvs=4)

**Welcome to SpringBlog, a CLI application for Spring and Kotlin enthusiasts. 💻❤️**
<table>
<tr>
   <td>
         <img src="https://github.com/leesiha/SpringBlog/assets/66323295/b1608e87-5ceb-4453-956a-25c4abe14772" width="100%" height="50%"/>
   </td>
   <td>
      <img src="https://github.com/leesiha/SpringBlog/assets/66323295/2523da03-f7c3-426a-8913-905a6778f905" width="100%" height="50%"/>
   </td>
<tr>
</table>

## ⭐ Focus of the Project

The primary goal of this project is to provide a practical example of controller-to-service layer data communication. It was developed as part of server studies to understand and practice data flow between different layers of a server-side application.

## Package Descriptions:

- **controller**: This package contains the Controller classes responsible for handling user input from the CLI. They act as intermediaries between the user and the Service layer, processing commands and displaying results.

- **dto (Data Transfer Object)**: DTOs carry data between processes. They transfer data between the Service and Controller layers, ensuring only necessary data is exposed to the user.

- **exception**: The exception package contains custom exception classes that handle specific error scenarios. They provide meaningful error messages to end-users and aid in debugging.

- **repository**: Interfaces that extend Spring's `JpaRepository` or other repository interfaces. They define methods for database interactions, providing CRUD operations.

- **service**: The service package contains Service classes with the application's business logic. They interact with both the Repository and DTO to fetch, process, and return data to the Controller.


## 📂 Repository Structure
```
.
├── src
│   ├── main
│   │   ├── java
│   │   │   ├── com
│   │   │   │   ├── ✽ my pakage ✽
│   │   │   │   │   ├── controller
│   │   │   │   │   ├── dto
│   │   │   │   │   ├── exception
│   │   │   │   │   ├── model
│   │   │   │   │   ├── repository
│   │   │   │   │   └── service
│   │   ├── resources
│   │   │   ├── static
│   │   │   ├── templates
│   │   │   └── application.properties ✽ server settings ✽
│   └── test
│       ├── java
│       └── resources
├── .gitignore
├── pom.xml
└── README.md
```

## 🚀 Setup & Installation
1. **Clone the Repository**
   ```bash
   git clone https://github.com/leesiha/SpringBlog.git
2. **Navigate to the Project Directory**
   ```bash
   cd SpringBlog
4. **Install Dependencies (if any)**
   ```bash
   ./gradlew build
6. **Run the Project**
   ```bash
   ./gradlew run
> Note: Ensure you have the necessary environment set up according to your project's requirements. Happy coding! 🚀
