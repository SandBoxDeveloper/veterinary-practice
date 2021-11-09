# Veterinary Practice

This application is to practice solution design, making use of SOLID principles where appropriate. This is a Kotlin console application, that ats a vet practice database.

As a console application, it takes two arguments, and prints a result. The first argument is a file, and the second is a search query.

The application creates a database that will store customers and pets and links between those pets and their owners (customers). The database is populated from an input file and stored in memory only. The data is not persisted to disk.

## Input arguments:
- Name/path to file to be parsed to populate database (in a specified format)
- Query string

## Expected output:
- Once the input file is parsed, and the database populated in memory, the application is able to query the database for customers or pets whose names match the query string, and output information about what it has found.
