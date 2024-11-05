# Library Management System App

This project uses a MySQL database that connects through a JDBC .jar file. The project user interface is done through JavaFX and CSS with backend done through Java.
Project done through Intelij IDEA


## Database Setup

This project requires a MySQL database. To set it up:

1. Ensure you have MySQL installed on your machine.
   (Install MYSQL through here if needed: https://www.mysql.com/downloads/)
2. Make sure the mysql-connector.jar file in the "lib" directory is added to the library.
3. Run the setup script:

   ```bash
   ./setup_database.sh
   ```

4. Set up the following environment variables:
   - `DB_USERNAME`: Your MySQL username
   - `DB_PASSWORD`: Your MySQL password
   - `DB_NAME`: Database name (e.g., `your_database_name`)
   - `DB_HOST`: Database host (e.g., `localhost`)

5. Start the application through StartController. The database will be ready for use.
