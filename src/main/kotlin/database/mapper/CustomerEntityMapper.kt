package database.mapper

import database.entity.CustomerEntity
import model.Customer
import model.Pet

class CustomerEntityMapper {

    fun map(customers: Map<String, Customer>, pets: Map<String, Pet>): List<CustomerEntity> {
        val customersOutput: MutableList<CustomerEntity> = mutableListOf()

        customers.forEach { entry: Map.Entry<String, Customer> ->
            val petNames = pets.filter {
                it.value.ownerId == entry.value.id
            }.values.joinToString(separator = "; ") { it.name }

            customersOutput.add(
                CustomerEntity(
                    entry.value.name,
                    entry.value.id,
                    petNames
                )
            )
        }

        return customersOutput
    }
}