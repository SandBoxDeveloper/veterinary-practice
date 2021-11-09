package database.mapper

import com.google.common.truth.Truth.assertThat
import database.entity.PetEntity
import io.mockk.MockKAnnotations
import model.Customer
import model.Pet
import org.junit.Before
import org.junit.Test

class PetEntityMapperTest {

    private val subject by lazy { PetEntityMapper() }

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `map() - given map of pets and map of customers then mapped list of PetEntity is returned`() {
        //given
        val pets = mutableMapOf(
            "1" to Pet("1", "Albert", "Rabbit", "3"),
            "2" to Pet("2", "Bob", "Snake", "3")
        )
        val customer = mutableMapOf(
            "3" to Customer(id = "3", name = "Andre"),
            "4" to Customer(id = "4", name = "Silvey")
        )
        val mappedExpectedListOfPetEntity = listOf(
            PetEntity("Albert", "Rabbit", "1", "Andre", "3"),
            PetEntity("Bob", "Snake", "2", "Andre", "3"),
        )

        //when
        val result = subject.map(pets, customer)

        //then
        assertThat(result).isEqualTo(mappedExpectedListOfPetEntity)
    }

    @Test
    fun `map() - given empty map of pets and empty map of customer then empty mapped list of PetEntity is returned`() {
        //given
        val pets = emptyMap<String, Pet>()
        val customer = emptyMap<String, Customer>()

        //when
        val result = subject.map(pets, customer)

        // then
        assertThat(result).isEqualTo(emptyList<PetEntity>())
    }

    @Test(expected = NoSuchElementException::class)
    fun `map() - given map of pets and empty map of customers then mapped list of PetEntity with no customers is returned`() {
        //given
        val pets = mutableMapOf(
            "1" to Pet("1", "Albert", "Rabbit", "3"),
            "2" to Pet("2", "Bob", "Snake", "3")
        )
        val customer = emptyMap<String, Customer>()

        //when
        subject.map(pets, customer)

        //then
        //error thrown as collection (customer) is empty
        //and logic in mapper can't search collection successfully
    }
}