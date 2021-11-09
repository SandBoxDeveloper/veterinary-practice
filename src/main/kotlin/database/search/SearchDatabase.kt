package database.search

import database.LocalDatabase
import database.entity.CustomerEntity
import database.entity.PetEntity
import database.mapper.CustomerEntityMapper
import database.mapper.PetEntityMapper
import database.model.MatchResult
import database.model.SearchQueryResult
import model.Customer
import model.Pet

// use name as the key in database
// remember why you choose to print from here and not pass it down
// remember program still prints all other messages
// handle when user only insert file but no query
class SearchDatabase(
    private val vetPracticeDatabase: LocalDatabase,
    private val customerEntityMapper: CustomerEntityMapper,
    private val petEntityMapper: PetEntityMapper,
) : Search {

    override fun queryAllDatabaseTable(query: String): SearchQueryResult {

        if (query.isEmpty() || query.isBlank()) {
            return SearchQueryResult.Error("you didn't enter a query, please try again")
        }

        return when (val result = checkTablesForAnyMatch(query.lowercase())) {
            is MatchResult.CustomerAndPet -> {
                val customers = getAllMatchingCustomerEntities(result.filteredCustomers)
                val pets = getAllMatchingPetEntities(result.filteredPets)
                SearchQueryResult.CustomerAndPetSuccess(customers, pets)
            }
            is MatchResult.Customer -> {
                SearchQueryResult.CustomerSuccess(getAllMatchingCustomerEntities(result.filteredCustomers))
            }
            is MatchResult.Pet -> {
                SearchQueryResult.PetSuccess(getAllMatchingPetEntities(result.filteredPets))
            }
            is MatchResult.NoMatch -> {
                SearchQueryResult.Error("no match found")
            }
        }
    }

    private fun checkTablesForAnyMatch(query: String): MatchResult {
        val doesAnyCustomersMatch = vetPracticeDatabase.getAllCustomers().entries.any {
            it.value.name.lowercase() == query
        }
        val doesAnyPetsMatch = vetPracticeDatabase.getAllPets().entries.any {
            it.value.name.lowercase() == query
        }

        return when {
            doesAnyCustomersMatch && doesAnyPetsMatch -> {
                val filteredCustomers = getMatchingResultsForCustomers(query)
                val filteredPets = getMatchingResultsForPets(query)
                MatchResult.CustomerAndPet(filteredCustomers, filteredPets)
            }
            doesAnyCustomersMatch -> {
                val filteredCustomers = getMatchingResultsForCustomers(query)
                MatchResult.Customer(filteredCustomers)
            }
            doesAnyPetsMatch -> {
                val filteredPets = getMatchingResultsForPets(query)
                MatchResult.Pet(filteredPets)
            }
            else -> {
                MatchResult.NoMatch
            }
        }
    }

    private fun getMatchingResultsForCustomers(query: String): Map<String, Customer> {
        return vetPracticeDatabase.getAllCustomers().filter { it.value.name.lowercase() == query }
    }

    private fun getMatchingResultsForPets(query: String): Map<String, Pet> {
        return vetPracticeDatabase.getAllPets().filter { it.value.name.lowercase() == query }
    }

    private fun getAllMatchingCustomerEntities(
        customers: Map<String, Customer>
    ): List<CustomerEntity> {

        val pets: Map<String, Pet> = vetPracticeDatabase.getAllPets()
        return customerEntityMapper.map(customers, pets)
    }

    private fun getAllMatchingPetEntities(
        pet: Map<String, Pet>
    ): List<PetEntity> {

        val customers: Map<String, Customer> = vetPracticeDatabase.getAllCustomers()
        return petEntityMapper.map(pet, customers)
    }
}