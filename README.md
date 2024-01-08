# Splitcoin Backend API
Welcome to the Splitcoin Backend API! This project is built using Spring Boot and provides endpoints for managing user accounts, groups, expenses, and settlements. The API is hosted on Render (https://splitcoinv2.onrender.com/) (Please note: the service may experience cold starts on the free account, so expect some delay initially).

# Authorization
JWT tokens are used for authentication. Tokens are generated and returned upon successful account creation or login. Include the token in the Authorization header for secured access to the API.

# Settlement Logic
The backend employs a sophisticated settlement logic based on a heap-based solution to efficiently resolve debts among users within a group. Here's a brief overview:

Heaps:
Min-Heap: Users who owe money are placed in this heap.
Max-Heap: Users who have paid money are placed in this heap.
Settlement Process:
At each iteration, the user owing the highest amount is extracted from the min-heap, along with the user who has paid the highest amount from the max-heap.
The API settles the amount between these two users.
The users are then placed back into their respective heaps.
If the debt amount becomes zero, the user is removed from the heap.
This settlement strategy ensures efficient and accurate debt settlement within the groups, minimizing the number of transactions.
