package database.model

sealed class MatchResult {
    data class CustomerAndPet(
        val filteredCustomers: Map<String, model.Customer>,
        val filteredPets: Map<String, model.Pet>
    ) : MatchResult()

    data class Customer(
        val filteredCustomers: Map<String, model.Customer>
    ) : MatchResult()

    data class Pet(
        val filteredPets: Map<String, model.Pet>
    ) : MatchResult()

    object NoMatch : MatchResult()
}
