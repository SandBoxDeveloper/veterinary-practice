package database.model

sealed class MatchResult {

    data class CustomerAndPet(
        val filteredCustomers: Map<String, model.Customer>,
        val filteredPets: Map<String, model.Pet>
    ) : MatchResult()

    object NoMatch : MatchResult()
}
