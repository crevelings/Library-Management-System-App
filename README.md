## Database Setup

This project requires a MySQL database. To set it up:

1. Ensure you have MySQL installed on your machine.
2. Run the setup script:

   ```bash
   ./setup_database.sh
   ```

3. Set up the following environment variables:
   - `DB_USERNAME`: Your MySQL username
   - `DB_PASSWORD`: Your MySQL password
   - `DB_NAME`: Database name (e.g., `your_database_name`)
   - `DB_HOST`: Database host (e.g., `localhost`)

4. Start the application. The database will be ready for use.
