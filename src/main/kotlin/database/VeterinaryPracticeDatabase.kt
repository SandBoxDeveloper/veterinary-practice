package database

import database.mapper.CustomerEntityMapper
import database.mapper.PetEntityMapper
import database.model.SearchQueryResult
import database.search.SearchDatabase
import model.Customer
import model.Pet

object VeterinaryPracticeDatabase: LocalDatabase {

    private val customerTable = HashMap<String, Customer>()
    private val petTable = HashMap<String, Pet>()

    // Injections
    private val customerEntityMapper: CustomerEntityMapper by lazy { CustomerEntityMapper() }
    private val petEntityMapper: PetEntityMapper by lazy { PetEntityMapper() }

    // Violation. Tightly coupled
    private val searchDatabase = SearchDatabase(this, customerEntityMapper, petEntityMapper)

    override fun insertCustomer(lineNumber: Int, customer: Customer) {
        return if (customerTable.containsKey(customer.id)) {
            println("Error on line ${lineNumber + 1}: Customer already exists with identifier ${customer.id}")
        } else {
            customerTable[customer.id] = customer
        }
    }

    override fun insertPet(lineNumber: Int, pet: Pet) {
        return if (!customerTable.containsKey(pet.ownerId)) {
            println("Error on line ${lineNumber + 1}: Owner with identifier ${pet.ownerId} does not exist")
        } else if (petTable.containsKey(pet.id)) {
            println("Error on line ${lineNumber + 1}: Pet already exists with identifier ${pet.id}")
        } else {
            petTable[pet.id] = pet
        }
    }

    override fun query(query: String): SearchQueryResult {
        return searchDatabase.queryAllDatabaseTable(query)
    }

    override fun getAllCustomers(): HashMap<String, Customer> {
        return customerTable
    }

    override fun getAllPets(): HashMap<String, Pet> {
        return petTable
    }

    // function made for testing
    // purposes only
    fun clear() {
        customerTable.clear()
        petTable.clear()
    }
}