package mapper

import com.google.common.truth.Truth.assertThat
import io.mockk.MockKAnnotations
import mapper.domain.PetMapperResult
import model.Pet
import org.junit.Before
import org.junit.Test

class PetMapperTest {

    private val subject by lazy { PetMapper() }

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `map() - given list of pet contents with less than 5 fields then pet mapper result error is returned`() {
        // given
        val petDataWithInvalidNumberOfFields = listOf("pet", "1", "dog", "Barney")
        val expectedPetObject = PetMapperResult.Error("Mapping Failed due to invalid syntax")

        // when
        val pet = subject.map(petDataWithInvalidNumberOfFields)

        // then
        assertThat(pet).isEqualTo(expectedPetObject)
    }

    @Test
    fun `map() - given list of pet contents with more than 5 fields then pet mapper result error is returned`() {
        // given
        val petDataWithExtraField = listOf("pet", "1", "dog", "Barney", "1", "some random field")
        val expectedPetObject = PetMapperResult.Error("Pet mapper exception - contents has more than 5 fields")

        // when
        val pet = subject.map(petDataWithExtraField)

        // then
        assertThat(pet).isEqualTo(expectedPetObject)
    }

    @Test
    fun `map() - given list of pet contents then mapped pet objected is returned`() {
        // given
        val petData = listOf("pet", "1", "dog", "Barney", "1")
        val expectedPetObject = PetMapperResult.Success(Pet(id = "1", type = "dog", name = "Barney", ownerId = "1"))

        // when
        val pet = subject.map(petData)

        // then
        assertThat(pet).isEqualTo(expectedPetObject)
    }
}