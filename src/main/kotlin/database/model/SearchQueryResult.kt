package database.model

import database.entity.CustomerEntity
import database.entity.PetEntity

sealed class SearchQueryResult {

    data class Success(
        val customers: List<CustomerEntity>,
        val pets: List<PetEntity>
    ) : SearchQueryResult()

    data class Error(
        val message: String
    ) : SearchQueryResult()
}