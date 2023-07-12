#Online Shop System
This repository contains a system for simulating online sales of various products. The system acts as an intermediary between customers and stores, facilitating the advertisement of products, order creation, order delivery from stores to customers, and payment processing.

#Description
The system enables stores to advertise their products, specifying the price and available quantity. Stores also have the option to define discounts that are applicable to all their products for a specific period of time.

Customers can create orders by selecting items from multiple stores and indicating the desired quantity. Upon submitting the order, the system verifies its feasibility and proceeds with the payment process. The system deducts the required amount from the customer's virtual account (within the system) and records each transaction as a separate entry. Once the payment is completed, the order is automatically sent to the customer. Orders can have three different states: "created" (order is created but not yet fulfilled), "sent" (order is complete and sent to the customer), and "arrived" (order has reached the customer's city).

Cities are connected through transportation lines, allowing bidirectional traffic. It is assumed that there is at most one transportation line between any two cities. The distance between cities, represented in transportation days, determines the time required for delivery. Since a single order may involve multiple stores, the transportation process occurs in two steps. First, the items from all the stores are transported to the store located in City A, which is the closest city to the customer's city (City B). City A must have a store but is not necessarily directly associated with the order. The second step involves transporting the order from City A to City B using the shortest route.

Upon the arrival of the order in the customer's city, the system automatically transfers the funds to the stores associated with the order. However, the system retains 5% of the total amount and records each transaction separately for each store.

The system grants an additional 2% discount on an order if the customer has made a purchase exceeding 10,000 in the past 30 days. In such cases, the system's profit margin decreases from 5% to 3%.

Store payments for incoming orders are handled by the trigger TR_TRANSFER_MONEY_TO_SHOPS.

The calculation of the final order price (including discounts) is performed by the stored procedure SP_FINAL_PRICE.

To maintain referential integrity, it is preferable to use the following constraints: ON UPDATE CASCADE, ON DELETE NO ACTION. Therefore, when deleting a specific row from a table, it should not delete other rows in different tables that reference the deleted row. Other rows should be explicitly deleted using alternative methods (if available), or the ON DELETE CASCADE referential integrity should be utilized.

Each primary key column (Id) that is not a foreign key should be an IDENTITY column.

The DECIMAL(10,3) data type should be used for decimal numbers. The default maximum length for all text columns in the tables is 100 characters, unless otherwise specified for a specific column.

#Documentation
The function documentation can be found in the documentation folder. Please refer to the index.html file for detailed information.
