package database.mapper

import com.google.common.truth.Truth.assertThat
import database.entity.CustomerEntity
import io.mockk.MockKAnnotations
import model.Customer
import model.Pet
import org.junit.Before
import org.junit.Test

class CustomerEntityMapperTest {

    private val subject by lazy { CustomerEntityMapper() }

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `map() - given map of customers and map of pets then mapped list of CustomerEntity is returned`() {
        //given
        val customer = mutableMapOf(
            "3" to Customer(id = "3", name = "Andre"),
            "4" to Customer(id = "4", name = "Silvey")
        )
        val pets = mutableMapOf(
            "1" to Pet("1", "Albert", "Rabbit", "3"),
            "2" to Pet("2", "Bob", "Snake", "3")
        )
        val mappedExpectedListOfCustomerEntity = listOf(
            CustomerEntity("Andre", "3", "Albert; Bob"),
            CustomerEntity("Silvey", "4", "")
        )

        //when
        val result = subject.map(customer, pets)

        //then
        assertThat(result).isEqualTo(mappedExpectedListOfCustomerEntity)
    }

    @Test
    fun `map() - given empty map of customers and empty map of pets then empty mapped list of CustomerEntity is returned`() {
        //given
        val customer = emptyMap<String, Customer>()
        val pets = emptyMap<String, Pet>()

        //when
        val result = subject.map(customer, pets)

        //then
        assertThat(result).isEqualTo(emptyList<CustomerEntity>())
    }

    @Test
    fun `map() - given map of customers and empty map of pets then mapped list of CustomerEntity with no pets is returned`() {
        //given
        val customer = mutableMapOf(
            "3" to Customer(id = "3", name = "Andre"),
            "4" to Customer(id = "4", name = "Silvey")
        )
        val pets = emptyMap<String, Pet>()
        val mappedExpectedListOfCustomerEntity = listOf(
            CustomerEntity("Andre", "3", ""),
            CustomerEntity("Silvey", "4", "")
        )

        //when
        val result = subject.map(customer, pets)

        //then
        assertThat(result).isEqualTo(mappedExpectedListOfCustomerEntity)
    }
}