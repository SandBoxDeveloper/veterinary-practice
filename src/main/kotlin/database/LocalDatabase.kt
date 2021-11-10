package database

import database.model.SearchQueryResult
import model.Customer
import model.Pet

interface LocalDatabase {
    fun insertCustomer(lineNumber: Int, customer: Customer)
    fun insertPet(lineNumber: Int, pet: Pet)
    fun query(query: String): SearchQueryResult
    fun getAllCustomers(): HashMap<String, Customer>
    fun getAllPets(): HashMap<String, Pet>
}