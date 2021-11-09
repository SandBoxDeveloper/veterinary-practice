package view

import database.entity.CustomerEntity
import database.entity.PetEntity

interface ConsoleView {
    fun startProgram(fileName: String)
    fun searchProgram(searchQuery: String)
    fun showQueryResult(customers: List<CustomerEntity>, pets: List<PetEntity>)
    fun showInvalidErrorMessage(lineNumber: Int)
    fun showErrorOnLineWithMessage(lineNumber: Int, message: String)
    fun showError(message: String)
}