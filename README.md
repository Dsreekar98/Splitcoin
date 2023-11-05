**Splitwise API Project**
The Splitwise API project is built on Spring Boot and facilitates the management of group expenses among users. It enables the creation of groups, users, expenses, user expenses, and the settlement of debts within these groups. The project utilizes a MySQL database and Hibernate as the ORM tool.

**Endpoints**
User Creation

Endpoint: /groupid/{groupId}/createuser
Description: Creates a user within a specified group.
Group Creation

Endpoint: /creategroup
Description: Creates a new group for managing expenses.
Expense Creation

Endpoint: /groupid/{groupId}/createexpense
Description: Records an expense within a group.
User Expense Creation

Endpoint: /expense/{expenseId}/user/{userId}/createuserexpense
Description: Associates a user with a particular expense within the group.

**Settlement Logic**
The project employs a settlement logic based on a heap-based solution for resolving debts among the users. The algorithm manages two separate heaps:

Min-Heap: Users who owe money are placed in this heap.
Max-Heap: Users who have paid money are placed in this heap.
The settlement process operates as follows:

At each iteration, the user owing the highest amount is extracted from the min-heap, along with the user who has paid the highest amount from the max-heap.
The amount is settled between these two users.
The users are then placed back into their respective heaps.
If the debt amount becomes zero, the user is removed from the heap.
All transaction details are stored to provide a comprehensive overview to end-users.
This settlement strategy ensures efficient (lesser transactions) and accurate debt settlement within the groups.

**Database Configuration**
The project is configured to use a MySQL database, managed by Hibernate as the ORM tool. The database schema design should align with the entities used in the project for storing group, user, expense, and transaction details.

Requirements
Java 8 or later
MySQL Database
Dependencies managed by Maven
Running the Project
To run the Splitwise API project:

Configure the MySQL database settings in the application properties file.
Build and run the project using the preferred IDE or via the command line.
Utilize the provided endpoints to create groups, users, expenses, and manage settlements.
