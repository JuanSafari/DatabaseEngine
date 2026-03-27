# **Database Engine (Java)**
A lightweight, custom-built relational database engine implemented in Java, featuring a command-line interface and persistent storage.

### **Technical Highlights**

- SQL Parser & Tokenizer: A custom implementation that parses raw string queries into executable command objects, including SELECT, INSERT, UPDATE, DELETE, and CREATE.

- JSON Persistence: Uses the Jackson library to serialize and deserialize the database state to a json file.

- Query Execution: Supports complex operations such as filtering with WHERE clauses.

### **Tech Stack**

- Language: Java 21
- Libraries: Jackson Databind, Project Lombok
- Build System: Maven
