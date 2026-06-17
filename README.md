# Inventory Management System (Java Internship Project)

**Author:** Mummana Devi Prasad  
**Role:** Java Development Intern  
**Project Scope:** Object-Oriented Programming (OOP) & File-Based Persistence Systems  

---

## 1. Introduction
The **Inventory Management System** is a robust, console-driven Java application designed to help businesses manage, track, and optimize their product inventories. In modern enterprise environments, maintaining accurate stock levels, tracking product values, identifying low-stock scenarios, and archiving records securely are critical logistics challenges. This project simulates an enterprise-ready command-line tool equipped with core database persistence using file system operations, detailed financial auditing reports, and strict input integrity validation.

## 2. Abstract
This system is developed entirely in Java and relies on core Object-Oriented Design patterns rather than heavy external framework dependencies, ensuring high performance, ease of comprehension, and portable execution. Memory management is coordinated using Java's dynamic `ArrayList` collection, which is serialized and deserialized dynamically into a local comma-separated values (CSV) text file (`products.txt`). The program provides full Create, Read, Update, and Delete (CRUD) features, and incorporates safety structures that capture scanner errors and input type mismatches to prevent program failures during execution.

---

## 3. Features
- **Dynamic CRUD Operations:**
  - **Add Product:** Insert new stock items with auto-validation (ID duplicates, negative prices, negative stock numbers).
  - **View Products:** Display catalog data structured inside a clear ASCII console grid table.
  - **Search Product:** Retrieve records instantly using unique case-insensitive Product IDs.
  - **Update Details:** Modify details with a fallback helper (press Enter without entering text to retain existing values).
  - **Delete Product:** Safely delete products after confirming the selection with a `Y/N` verification check.
- **Data Persistence:** Automatically saves inventory state to `products.txt` upon modifications, and restores data from it on startup.
- **Low Stock Notification:** Triggers active, real-time warning logs for any product whose quantity drops below **5** units.
- **Auditing & Report Generation:** Calculates overall unique products, total counts of stock items, and full portfolio financial valuation.

---

## 4. Tools & Technologies Used
- **Language:** Java SE (Standard Edition) JDK 8 or higher.
- **Libraries/Core API:** `java.util.ArrayList`, `java.util.Scanner`, `java.io` (`FileReader`, `FileWriter`, `BufferedReader`, `BufferedWriter`, `File`).
- **Development Tooling:** Java CLI compiler (`javac`) and Java virtual machine runtime (`java`).
- **Operating Environment:** Platform independent (tested on Windows CMD, PowerShell, and bash).

---

## 5. OOP Concepts Demonstrated
This codebase serves as a reference implementation of standard OOP practices:
- **Encapsulation:** In `Product.java`, variables (`productId`, `productName`, `category`, `price`, `quantity`) are declared as `private`. Access is granted exclusively through public getter and setter methods. Setters include validation rules to prevent corrupt states (such as pricing a product below $0.00).
- **Abstruction & Modularization:** System behaviors are partitioned into logical layers. `Product` models data, `InventoryManager` manages collection logic, and `InventoryManagementSystem` coordinates input and user output streams.
- **Objects & Instantiation:** Objects are generated at runtime to mirror stock states, using parameterized constructors to establish initial properties safely.

---

## 6. File Handling Details
Persistence is implemented in `InventoryManager.java` using:
- **`FileWriter` and `BufferedWriter`:** Appends and writes serialized strings from `Product#toCsvString()` to `products.txt`. Buffered output streams ensure minimal disk access operations.
- **`FileReader` and `BufferedReader`:** Reads raw inventory records line-by-line. The application parses lines using a static factory method `Product#fromCsvString(String)` and builds the memory list on startup.
- **Resource Management:** Employs the Java **try-with-resources** syntax. This ensures file channels are automatically closed, preventing memory and filesystem descriptor leaks.

---

## 7. Steps Involved in Building the Project
1. **Requirements Gathering:** Map the entities, properties, and actions (Add, View, Update, Delete, Report).
2. **Schema & Model Design:** Implement `Product.java` with getters, setters, custom format printing, and CSV serializers.
3. **Business Logic Integration:** Code the database emulator in `InventoryManager.java` using `ArrayList`, file loading, and saving routines.
4. **Interactive CLI Design:** Implement the user loop in `InventoryManagementSystem.java` with input validators, preventing nextLine buffer bugs.
5. **Testing and Verification:** Execute test matrices verifying file inputs, invalid number handling, low-stock threshold triggers, and report tallies.
6. **Refactoring & Documentation:** Complete inline code comments and construct `README.md`.

---

## 8. GitHub Repository Structure
```text
Inventory-Management-System/
├── Product.java
├── InventoryManager.java
├── InventoryManagementSystem.java
├── products.txt
└── README.md
```

---

## 9. Compilation and Execution

Follow these steps to compile and run the project from your terminal:

```bash
# Step 1: Navigate to the directory where the source code is stored
cd "c:/Users/mumma/OneDrive/Desktop/java project"

# Step 2: Compile all Java source files
javac *.java

# Step 3: Run the main class
java InventoryManagementSystem
```

---

## 10. Sample Output

### System Startup and Main Menu
```text
==================================================
   Welcome to the Inventory Management System
==================================================

[ALERT] LOW STOCK WARNING (Quantity < 5)
==================================================================
+--------------+----------------------+-----------------+------------+----------+
| Product ID   | Product Name         | Category        | Price      | Quantity |
+--------------+----------------------+-----------------+------------+----------+
| PRD02        | Wireless Mouse       | Electronics     | $    25.50 |        3 |
| PRD04        | Notebook             | Stationery      | $     4.49 |        4 |
+--------------+----------------------+-----------------+------------+----------+

--- MAIN MENU ---
1. Add Product
2. View All Products
3. Search Product by ID
4. Update Product Details
5. Delete Product
6. Generate Inventory Report
7. Exit
Select an option (1-7):
```

### View All Products (Option 2)
```text
--- Current Inventory ---
+--------------+----------------------+-----------------+------------+----------+
| Product ID   | Product Name         | Category        | Price      | Quantity |
+--------------+----------------------+-----------------+------------+----------+
| PRD01        | Laptop               | Electronics     | $   999.99 |       10 |
| PRD02        | Wireless Mouse       | Electronics     | $    25.50 |        3 |
| PRD03        | Coffee Mug           | Kitchen         | $    12.99 |       15 |
| PRD04        | Notebook             | Stationery      | $     4.49 |        4 |
+--------------+----------------------+-----------------+------------+----------+

[ALERT] LOW STOCK WARNING (Quantity < 5)
==================================================================
+--------------+----------------------+-----------------+------------+----------+
| Product ID   | Product Name         | Category        | Price      | Quantity |
+--------------+----------------------+-----------------+------------+----------+
| PRD02        | Wireless Mouse       | Electronics     | $    25.50 |        3 |
| PRD04        | Notebook             | Stationery      | $     4.49 |        4 |
+--------------+----------------------+-----------------+------------+----------+
```

### Search Product (Option 3)
```text
--- Search Product ---
Enter Product ID to Search: PRD02

[FOUND] Product Details:
------------------------------------
ID          : PRD02
Name        : Wireless Mouse
Category    : Electronics
Price       : $25.50
Quantity    : 3
------------------------------------
Status      : Low Stock (3 left) [ALERT]
```

### Generate Inventory Report (Option 6)
```text
==================================================================
                   INVENTORY SUMMARY REPORT
==================================================================
Total Unique Products : 4
Total Items in Stock  : 32
Total Valuation       : $10,289.21
Low Stock Warnings    : 2 item(s)
==================================================================

[TIP] Please select Option 4 (Update Product) to restock low-stock items.
```

---

## 11. Conclusion
This Inventory Management System acts as a direct demonstration of building software using clean coding styles and strict structure standards. By keeping data processing routines (the manager class) separate from the interaction layer (the CLI system), the application maintains clean code boundaries, making it highly extensible. This framework is a perfect foundation for developers looking to scale command line applications into graphical user interfaces (GUIs) or integrate SQL database backends.
