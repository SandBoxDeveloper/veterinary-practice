package mapper.domain

import model.Pet

sealed class PetMapperResult {
    data class Success(val pet: Pet) : PetMapperResult()
    data class Error(val message: String) : PetMapperResult()
}

