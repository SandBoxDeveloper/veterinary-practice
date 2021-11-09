package view

import database.entity.CustomerEntity
import database.entity.PetEntity
import presenter.Presenter

class VeterinaryPracticeConsoleView(private val presenter: Presenter) : ConsoleView {

    override fun startProgram(fileName: String) {
        presenter.setupWithView(this)
        presenter.start(fileName)
    }

    override fun searchProgram(searchQuery: String) {
        presenter.search(searchQuery)
    }

    override fun showQueryResult(customers: List<CustomerEntity>, pets: List<PetEntity>) {
        customers.forEach { customer ->
            println("Customer: ${customer.customer}")
            println("Identifier: ${customer.identifier}")
            println("Pets: ${customer.pets}")
            println()
        }
        pets.forEach { pet ->
            println("Pet: ${pet.pet}")
            println("Type: ${pet.type}")
            println("Identifier: ${pet.identifier}")
            println("Owner: ${pet.owner}")
            println("Owner ID: ${pet.ownerId}")
            println()
        }
    }

    override fun showInvalidErrorMessage(lineNumber: Int) {
        println("Error on line ${lineNumber}: Invalid syntax")
    }

    override fun showErrorOnLineWithMessage(lineNumber: Int, message: String) {
        println("Error on line ${lineNumber}: $message")
    }

    override fun showError(message: String) {
        println(message)
    }

}