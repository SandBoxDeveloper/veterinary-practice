package database.search

import com.google.common.truth.Truth.assertThat
import database.LocalDatabase
import database.entity.CustomerEntity
import database.entity.PetEntity
import database.mapper.CustomerEntityMapper
import database.mapper.PetEntityMapper
import database.model.SearchQueryResult
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import model.Customer
import model.Pet
import org.junit.Test
import kotlin.test.BeforeTest

class SearchDatabaseTest {

    @MockK
    lateinit var mockLocalDatabase: LocalDatabase

    @MockK
    lateinit var mockCustomerEntityMapper: CustomerEntityMapper

    @MockK
    lateinit var mockPetEntityMapper: PetEntityMapper

    private val subject by lazy { SearchDatabase(mockLocalDatabase, mockCustomerEntityMapper, mockPetEntityMapper) }

    @BeforeTest
    fun setUp() {
        MockKAnnotations.init(this)
        every { mockLocalDatabase.getAllCustomers() } returns hashMapOf(Pair("1", Customer("1", "Albert")))
        every { mockLocalDatabase.getAllPets() } returns hashMapOf(Pair("1", Pet("1", "Johnny", "Dog", "1")))
    }

    @Test
    fun `queryAllDatabaseTable() - given search query is empty SearchQueryResultError is returned with message`() {
        //given
        val query = ""

        //when
        val result = subject.queryAllDatabaseTable(query)

        //then
        assertThat(
            result
        ).isEqualTo(SearchQueryResult.Error("you didn't enter a query, please try again"))
    }

    @Test
    fun `queryAllDatabaseTable() - given search query is blank then SearchQueryResultError is returned with message`() {
        //given
        val query = " "

        //when
        val result = subject.queryAllDatabaseTable(query)

        //then
        assertThat(
            result
        ).isEqualTo(SearchQueryResult.Error("you didn't enter a query, please try again"))
    }

    @Test
    fun `queryAllDatabaseTable() - given query has a customer and pet match in the database then SearchQueryResultSuccess is returned with customer and pet`() {
        //given
        every { mockLocalDatabase.getAllCustomers() } returns hashMapOf(
            Pair("1", Customer("1", "Albert")),
            Pair("2", Customer("2", "Wilfred"))
        )
        every { mockLocalDatabase.getAllPets() } returns hashMapOf(
            Pair("3", Pet("3", "Wilfred", "Fish", "1"))
        )
        every { mockCustomerEntityMapper.map(any(), any()) } returns listOf(secondCustomerEntity)
        every { mockPetEntityMapper.map(any(), any()) } returns listOf(secondPetEntity)

        val query = "Wilfred"

        //when
        val result = subject.queryAllDatabaseTable(query)

        //then
        assertThat(
            result
        ).isEqualTo(SearchQueryResult.Success(customers = listOf(secondCustomerEntity), pets = listOf(secondPetEntity)))
    }

    @Test
    fun `queryAllDatabaseTable() - given query has a customer match only in the database then SearchQueryResultSuccess is returned with customer entity`() {
        //given
        val query = "Albert"
        every { mockCustomerEntityMapper.map(any(), any()) } returns listOf(customerEntity)
        every { mockPetEntityMapper.map(any(), any()) } returns emptyList()

        //when
        val result = subject.queryAllDatabaseTable(query)

        //then
        assertThat(
            result
        ).isEqualTo(SearchQueryResult.Success(customers = listOf(customerEntity), pets = emptyList()))
    }

    @Test
    fun `queryAllDatabaseTable() - given query has a pet match only in the database then SearchQueryResultSuccess is returned with pet entity`() {
        //given
        val query = "Johnny"
        every { mockPetEntityMapper.map(any(), any()) } returns listOf(petEntity)
        every { mockCustomerEntityMapper.map(any(), any()) } returns emptyList()

        //when
        val result = subject.queryAllDatabaseTable(query)

        //then
        assertThat(
            result
        ).isEqualTo(SearchQueryResult.Success(customers = emptyList(), pets = listOf(petEntity)))
    }

    @Test
    fun `queryAllDatabaseTable() - given query has no match at all in the database then SearchQueryResultError is returned with message`() {
        //given
        val query = "random name"

        //when
        val result = subject.queryAllDatabaseTable(query)

        //then
        assertThat(
            result
        ).isEqualTo(SearchQueryResult.Error("no match found"))
    }

    private companion object {
        val customerEntity = CustomerEntity("Albert", "1", "Johnny")
        val petEntity = PetEntity("Johnny", "Dog", "1", "Albert", "1")
        val secondCustomerEntity = CustomerEntity("Wilfred", "2", "")
        val secondPetEntity = PetEntity("Wilfred", "Fish", "3", "Albert", "1")
    }
}