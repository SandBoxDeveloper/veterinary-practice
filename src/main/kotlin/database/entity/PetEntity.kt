package database.entity

data class PetEntity(
    val pet: String,
    val type: String,
    val identifier: String,
    val owner: String,
    val ownerId: String
)