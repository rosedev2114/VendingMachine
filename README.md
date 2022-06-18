# VendingMachine
Provides a Java Application Console Interface to a simulated Vending Machine.

# Technologies
- Java 18
- GSON
- JUnit
- Maven

# To Run
``` console
mvn install clean

mvn compile exec:java -Dexec.mainClass="com.ms3.project.vending.Main"
```
# Process
This was a fun exercise, that had some interesting problems to solve.

To start the project, I traditional think of problems in data first, My reasoning is that if you have the data modeled out correctly the solutions come intuitively.
The most obvious data to model was the explictly given inside of the JSON sample. (Config{Row, Column}, Item{Name, Price, Amount}).

More implict data models included a way to track Financial Transaction, as well as a Currency System.
(Transaction{Balance} MoneyEnum{Dollars, Coins}).

The VendingMachineService is a bit bloated, when I first created the object, I anticipated a simple event loop and simple mutations of the Static Transaction and Inventory. If I had more time available and experimenting with different messaging appoarches in the Event Loop until something came out more ergonomic. 

Overall an Enjoyable Project!
