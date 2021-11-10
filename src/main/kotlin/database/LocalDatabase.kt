package database

import model.Customer
import model.Pet

interface LocalDatabase {
    fun insertCustomer(lineNumber: Int, customer: Customer)
    fun insertPet(lineNumber: Int, pet: Pet)
    fun getAllCustomers(): HashMap<String, Customer>
    fun getAllPets(): HashMap<String, Pet>
}