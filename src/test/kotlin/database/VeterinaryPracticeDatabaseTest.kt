package database

import com.google.common.truth.Truth.assertThat
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import model.Customer
import model.Pet
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class VeterinaryPracticeDatabaseTest {

    @RelaxedMockK
    lateinit var mockCustomer: Customer

    @RelaxedMockK
    lateinit var mockPet: Pet

    private val outputStreamCaptor = ByteArrayOutputStream()

    private val subject by lazy { VeterinaryPracticeDatabase }

    @BeforeTest
    fun setUp() {
        MockKAnnotations.init(this)

        System.setOut(PrintStream(outputStreamCaptor))

        every { mockCustomer.name } returns "albert"
        every { mockCustomer.id } returns "1"
        every { mockPet.name } returns "batsy"
        every { mockPet.id } returns "1"
        every { mockPet.type } returns "fish"
        every { mockPet.ownerId } returns "1"

        subject.insertCustomer(1, mockCustomer)
        subject.insertPet(1, mockPet)
    }

    @AfterTest
    fun tearDown() {
        subject.clear()
    }

    @Test
    fun `insertCustomer() - when customer with id already exists`() {
        //given
        val newCustomerWithIdThatAlreadyExits = Customer("1", "Nikeky")

        //when
        subject.insertCustomer(2, newCustomerWithIdThatAlreadyExits)

        //then
        assertThat(
            outputStreamCaptor.toString().trim()
        ).isEqualTo("Error on line 3: Customer already exists with identifier 1")
    }

    @Test
    fun `insertCustomer() - successfully`() {
        //given
        val expectedCustomer = hashMapOf("1" to mockCustomer)

        //when
        subject.insertCustomer(2, mockCustomer)

        //then
        val customers: HashMap<String, Customer> = subject.getAllCustomers()
        assertThat(customers).isEqualTo(expectedCustomer)
    }

    @Test
    fun `insertPet() - when pet with owner id does not already exists`() {
        //given
        val newPetWithOwnerIdThatDoesNotAlreadyExits = Pet("1", "Sooty", "guinea pig", "20")

        //when
        subject.insertPet(2, newPetWithOwnerIdThatDoesNotAlreadyExits)

        //then
        assertThat(
            outputStreamCaptor.toString().trim()
        ).isEqualTo(
            "Error on line 3: Owner with identifier 20 does not exist"
        )
    }

    @Test
    fun `insertPet() - when pet with id already exists`() {
        //given
        val newPetWithIdThatAlreadyExits = Pet("1", "Sooty", "guinea pig", "1")

        //when
        subject.insertPet(2, newPetWithIdThatAlreadyExits)

        //then
        assertThat(
            outputStreamCaptor.toString().trim()
        ).isEqualTo(
            "Error on line 3: Pet already exists with identifier 1"
        )
    }

    @Test
    fun `insertPet() - successfully`() {
        //given
        val expectedPet = hashMapOf("1" to mockPet)

        //when
        subject.insertPet(2, mockPet)

        //then
        val pets: HashMap<String, Pet> = subject.getAllPets()
        assertThat(pets).isEqualTo(expectedPet)
    }

    @Test
    fun `getAllCustomers() - all customers in database are returned`() {
        //given
        val expectedCustomer = hashMapOf("1" to mockCustomer)

        //when
        val customers: HashMap<String, Customer> = subject.getAllCustomers()

        //then
        assertThat(customers).isEqualTo(expectedCustomer)
    }

    @Test
    fun `getAllPets() - all pets in database are returned`() {
        //given
        val expectedPet = hashMapOf("1" to mockPet)

        //when
        val pets: HashMap<String, Pet> = subject.getAllPets()

        //then
        assertThat(pets).isEqualTo(expectedPet)
    }
}