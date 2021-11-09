package mapper

import model.Pet
import mapper.domain.PetMapperResult

class PetMapper {
    fun map(contents: List<String>): PetMapperResult {
        return if(contents.size < 5){
            PetMapperResult.Error("Mapping Failed due to invalid syntax")
        }
        else if (contents.size > 5) {
            PetMapperResult.Error("Pet mapper exception - contents has more than 5 fields")
        } else {
            PetMapperResult.Success(
                Pet(
                    id = contents[INDEX_PET_ID],
                    type = contents[INDEX_PET_TYPE],
                    name = contents[INDEX_PET_NAME],
                    ownerId = contents[INDEX_PET_OWNER_ID]
                )
            )
        }
    }

    private companion object {
        const val INDEX_PET_ID = 1
        const val INDEX_PET_TYPE = 2
        const val INDEX_PET_NAME = 3
        const val INDEX_PET_OWNER_ID = 4
    }
}