#!/bin/bash
# Prompt user for MySQL credentials
echo "Enter MySQL username:"
read username
echo "Enter MySQL password:"
read -s password

# Create the database
mysql -u $username -p$password -e "CREATE DATABASE IF NOT EXISTS your_database_name"

# Import the schema and data
mysql -u $username -p$password your_database_name < database/database_setup.sql

echo "Database setup completed."

chmod +x setup_database.sh