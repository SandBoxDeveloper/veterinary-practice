package database.model

import database.entity.CustomerEntity
import database.entity.PetEntity

sealed class SearchQueryResult {
    data class CustomerSuccess(
        val customers: List<CustomerEntity>
    ) : SearchQueryResult()

    data class PetSuccess(
        val pets: List<PetEntity>
    ) : SearchQueryResult()

    data class CustomerAndPetSuccess(
        val customers: List<CustomerEntity>,
        val pets: List<PetEntity>
    ) : SearchQueryResult()

    data class Error(
        val message: String
    ) : SearchQueryResult()
}