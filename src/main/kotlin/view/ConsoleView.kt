package view

import database.entity.CustomerEntity
import database.entity.PetEntity

interface ConsoleView {
    fun startProgram(fileName: String)
    fun searchProgram(searchQuery: String)
    fun showCustomers(customers: List<CustomerEntity>)
    fun showPets(pets: List<PetEntity>)
    fun showCustomersAndPets(customers: List<CustomerEntity>, pets: List<PetEntity>)
    fun showInvalidErrorMessage(lineNumber: Int)
    fun showErrorOnLineWithMessage(lineNumber: Int, message: String)
    fun showError(message: String)
}