package presenter

import database.LocalDatabase
import database.entity.CustomerEntity
import database.entity.PetEntity
import database.model.SearchQueryResult
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
import mapper.CustomerMapper
import mapper.PetMapper
import mapper.domain.CustomerMapperResult
import mapper.domain.PetMapperResult
import model.Customer
import model.Pet
import org.junit.Before
import org.junit.Test
import util.Reader
import view.ConsoleView
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class VeterinaryPracticePresenterTest {

    @RelaxedMockK
    lateinit var mockFileReader: Reader

    @RelaxedMockK
    lateinit var mockCustomerMapper: CustomerMapper

    @RelaxedMockK
    lateinit var mockPetMapper: PetMapper

    @RelaxedMockK
    lateinit var mockLocalDatabase: LocalDatabase

    @RelaxedMockK
    lateinit var mockView: ConsoleView

    private val outputStreamCaptor = ByteArrayOutputStream()

    private val subject by lazy {
        VeterinaryPracticePresenter(
            fileReader = mockFileReader,
            customerMapper = mockCustomerMapper,
            petMapper = mockPetMapper,
            vetPracticeDatabase = mockLocalDatabase
        ).apply {
            setupWithView(mockView)
        }
    }

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        System.setOut(PrintStream(outputStreamCaptor))
        subject.start(fileName = TEST_FILE_PATH)
    }

    @Test
    fun `start() - given file contents size is less than or equal to 1 then show view invalid error message`() {
        //given
        every { mockFileReader.readFile(TEST_FILE_PATH) } returns listOf("customer")

        //when
        subject.start(TEST_FILE_PATH)

        //then
        verify { mockView.showInvalidErrorMessage(1) }
    }

    @Test
    fun `start() - given file contents with first element labelled as 'Customer' and then mapping contents is successful then database insertCustomer is called`() {
        //given
        every { mockFileReader.readFile(TEST_FILE_PATH) } returns listOf("customer:1:Sam")
        every { mockCustomerMapper.map(any()) }.returns(CustomerMapperResult.Success(Customer("1", "Sam")))

        //when
        subject.start(TEST_FILE_PATH)

        //then
        verify { mockLocalDatabase.insertCustomer(0, Customer("1", "Sam")) }
    }

    @Test
    fun `start() - given file contents with first element labelled as 'Customer' and then mapping contents is NOT successful then show view error on line with message`() {
        //given
        every { mockFileReader.readFile(TEST_FILE_PATH) } returns listOf("customer:1:Sam:extra")
        every { mockCustomerMapper.map(any()) }.returns(CustomerMapperResult.Error("error mapping contents"))

        //when
        subject.start(TEST_FILE_PATH)

        //then
        verify { mockView.showErrorOnLineWithMessage(1, "error mapping contents") }
    }

    @Test
    fun `start() - given file contents with first element labelled as 'Pet' and then mapping contents is successful then database insertPet is called`() {
        //given
        every { mockFileReader.readFile(TEST_FILE_PATH) } returns listOf("pet:1:dog:Barney:1")
        every { mockPetMapper.map(any()) }.returns(PetMapperResult.Success(Pet("1", "Barney", "dog", "1")))

        //when
        subject.start(TEST_FILE_PATH)

        //then
        verify { mockLocalDatabase.insertPet(0, Pet("1", "Barney", "dog", "1")) }
    }

    @Test
    fun `start() - given file contents with first element labelled as 'Pet' and then mapping contents is NOT successful then show view error on line with message`() {
        //given
        every { mockFileReader.readFile(TEST_FILE_PATH) } returns listOf("pet:1:dog:Barney:1")
        every { mockPetMapper.map(any()) }.returns(PetMapperResult.Error("error mapping contents"))

        //when
        subject.start(TEST_FILE_PATH)

        //then
        verify { mockView.showErrorOnLineWithMessage(1, "error mapping contents") }
    }

    @Test
    fun `start() - given file contents first element is not 'Customer' or 'Pet' then show view invalid error message`() {
        //given
        every { mockFileReader.readFile(TEST_FILE_PATH) } returns listOf("random:1:Albert")

        //when
        subject.start(TEST_FILE_PATH)

        //then
        verify { mockView.showInvalidErrorMessage(1) }
    }

    @Test
    fun `search() - given query that matches to both a customer and a pet in the database then view customer and pet message is printed`() {
        //given
        val searchQuery = "albert"
        every {
            mockLocalDatabase.query(searchQuery)
        }.returns(
            SearchQueryResult.Success(customerEntities, petEntities)
        )

        //when
        subject.search(searchQuery)

        //then
        verify { mockView.showQueryResult(customerEntities, petEntities) }
    }

    @Test
    fun `search() - given query that matches a customer in the database then view customer is printed`() {
        //given
        val searchQuery = "sam"
        every { mockLocalDatabase.query(searchQuery) }.returns(SearchQueryResult.Success(customerEntities, emptyList()))

        //when
        subject.search(searchQuery)

        //then
        verify { mockView.showQueryResult(customers = customerEntities, pets = emptyList()) }
    }

    @Test
    fun `search() - given query that matches a pet in the database then pet is printed`() {
        //given
        val searchQuery = "sam"
        every { mockLocalDatabase.query(searchQuery) }.returns(SearchQueryResult.Success(emptyList(), petEntities))

        //when
        subject.search(searchQuery)

        //then
        verify { mockView.showQueryResult(emptyList(), petEntities) }
    }

    @Test
    fun `search() - given query that does not match either a customer or a pet in the database then view show error message printed`() {
        //given
        val searchQuery = "albert"
        every { mockLocalDatabase.query(searchQuery) }.returns(SearchQueryResult.Error("no match found"))

        //when
        subject.search(searchQuery)

        //then
        verify { mockView.showError("no match found") }
    }

    private companion object {
        const val TEST_FILE_PATH = "src/test/resources/test_vet_patient_file_happy_path_1.txt"
        val customerEntities = listOf(CustomerEntity("Sam", "1", ""))
        val petEntities = listOf(PetEntity("Johnny", "Fish", "1", "1", "Sam"))
    }
}