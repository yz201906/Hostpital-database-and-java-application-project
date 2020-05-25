# Hostpital database and java application project
## Compile Instructions for Eclipse
1. Clone code from Git.
2. Launch Eclipse
3. Create a new workspace
4. File -> Open Projects from File System -> Directory -> Select directory code was saved to.
5. Press "Finish"
6. Debugger and running is available in Eclipse from here.

## Sample data
Oracle jdbc driver is provided under `/src/Database/`. Username and password strings needs to be changed accordingly.  
Database can be initialized by running the `initWithConstraints.sql`, the `drop.sql` will clear the database tables if desired. After initialization, run the `sampleData.sql` to add sample data to the database. 