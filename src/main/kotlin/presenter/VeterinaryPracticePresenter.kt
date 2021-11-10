package presenter

import database.LocalDatabase
import database.model.SearchQueryResult
import database.search.Search
import mapper.CustomerMapper
import mapper.PetMapper
import mapper.domain.CustomerMapperResult
import mapper.domain.PetMapperResult
import util.Reader
import view.ConsoleView

//1. Perform any viable logical operations on the data provided
//2. store or get the data from database or preferences via model
//3. alter the state of view
class VeterinaryPracticePresenter(
    private val fileReader: Reader,
    private val customerMapper: CustomerMapper,
    private val petMapper: PetMapper,
    private val vetPracticeDatabase: LocalDatabase,
    private val searchDatabase: Search
) : Presenter {

    private lateinit var view: ConsoleView

    override fun setupWithView(view: ConsoleView) {
        this.view = view
    }

    override fun start(fileName: String) {
        val fileContents: List<String> by lazy { fileReader.readFile(fileName) }
        fileContents.forEachIndexed { index, string ->
            val delimiter = ":"
            val dataInfo = string.split(delimiter)

            when {
                dataInfo.size <= 1 -> {
                    view.showInvalidErrorMessage(index + 1)
                }
                dataInfo.first() == CUSTOMER_INDEX -> {
                    saveCustomerData(index, dataInfo)
                }
                dataInfo.first() == PET_INDEX -> {
                    savePetData(index, dataInfo)
                }
                else -> {
                    view.showInvalidErrorMessage(index + 1)
                }
            }
        }
    }

    override fun search(query: String) {
        when (val result = searchDatabase.queryAllDatabaseTable(query)) {
            is SearchQueryResult.CustomerAndPetSuccess -> view.showCustomersAndPets(result.customers, result.pets)
            is SearchQueryResult.CustomerSuccess -> view.showCustomers(customers = result.customers)
            is SearchQueryResult.PetSuccess -> view.showPets(result.pets)
            is SearchQueryResult.Error -> view.showError(result.message)
        }
    }

    private fun saveCustomerData(index: Int, dataInfo: List<String>) {
        when (val result = customerMapper.map(dataInfo)) {
            is CustomerMapperResult.Error -> view.showErrorOnLineWithMessage(index + 1, result.message)
            is CustomerMapperResult.Success -> vetPracticeDatabase.insertCustomer(index, result.customer)
        }
    }

    private fun savePetData(index: Int, dataInfo: List<String>) {
        when (val result = petMapper.map(dataInfo)) {
            is PetMapperResult.Error -> view.showErrorOnLineWithMessage(index + 1, result.message)
            is PetMapperResult.Success -> vetPracticeDatabase.insertPet(index, result.pet)
        }
    }

    private companion object {
        const val CUSTOMER_INDEX = "customer"
        const val PET_INDEX = "pet"
    }

}