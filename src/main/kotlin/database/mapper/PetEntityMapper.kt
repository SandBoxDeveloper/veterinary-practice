package database.mapper

import database.entity.PetEntity
import model.Customer
import model.Pet

class PetEntityMapper {

    fun map(pets: Map<String, Pet>, customers: Map<String, Customer>): List<PetEntity> {
        val petsOutput: MutableList<PetEntity> = mutableListOf()

        pets.forEach { entry ->
            val customerName = customers.values.first { it.id == entry.value.ownerId }.name
            petsOutput.add(
                PetEntity(
                    entry.value.name,
                    entry.value.type,
                    entry.value.id,
                    customerName,
                    entry.value.ownerId
                )
            )
        }

        return petsOutput
    }
}