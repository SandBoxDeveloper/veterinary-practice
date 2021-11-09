package view

import com.google.common.truth.Truth.assertThat
import database.entity.CustomerEntity
import database.entity.PetEntity
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
import org.junit.Before
import org.junit.Test
import presenter.Presenter
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class VeterinaryPracticeConsoleViewTest {

    @RelaxedMockK
    lateinit var mockPresenter: Presenter

    private val outputStreamCaptor = ByteArrayOutputStream()

    private lateinit var subject: VeterinaryPracticeConsoleView

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        System.setOut(PrintStream(outputStreamCaptor))

        subject = VeterinaryPracticeConsoleView(mockPresenter).apply {
            startProgram("src/main/resources/vet_patient_file_happy_path_1.txt")
        }
    }

    @Test
    fun `startProgram() - verify presenter setupWithView() and start() functions are called`() {
        //given
        val fileName = "some_file_name.txt"

        //when
        subject.startProgram(fileName)

        //then
        verify { mockPresenter.setupWithView(subject) }
        verify { mockPresenter.start(fileName) }
    }

    @Test
    fun `searchProgram() - verify presenter search function is called`() {
        //given
        val searchQuery = "query"

        //when
        subject.searchProgram(searchQuery)

        //then
        verify { mockPresenter.search(searchQuery) }
    }

    @Test
    fun `showCustomers() - given customer information then customer details are printed`() {
        //when
        subject.showCustomers(listOf(singleCustomerEntity))

        //then
        assertThat(
            outputStreamCaptor.toString().trim()
        ).isEqualTo(
            "Customer: ${singleCustomerEntity.customer}\n" +
                    "Identifier: ${singleCustomerEntity.identifier}\n" +
                    "Pets: ${singleCustomerEntity.pets}"
        )
    }

    @Test
    fun `showCustomers() - given list of customer information then customer details are printed`() {
        //when
        subject.showCustomers(customerEntities)

        //then
        assertThat(
            outputStreamCaptor.toString().trim()
        ).isEqualTo(
            "Customer: ${customerEntities[0].customer}\n" +
                    "Identifier: ${customerEntities[0].identifier}\n" +
                    "Pets: ${customerEntities[0].pets}\n" +
                    "\n" +
                    "Customer: ${customerEntities[1].customer}\n" +
                    "Identifier: ${customerEntities[1].identifier}\n" +
                    "Pets: ${customerEntities[1].pets}"
        )
    }

    @Test
    fun `showPets() - given pet information then the pet details are printed`() {
        //when
        subject.showPets(listOf(singlePetEntity))

        //then
        assertThat(
            outputStreamCaptor.toString().trim()
        ).isEqualTo(
            "Pet: ${singlePetEntity.pet}\n" +
                    "Type: ${singlePetEntity.type}\n" +
                    "Identifier: ${singlePetEntity.identifier}\n" +
                    "Owner: ${singlePetEntity.owner}\n" +
                    "Owner ID: ${singlePetEntity.ownerId}"
        )
    }

    @Test
    fun `showPets() - given list of pet information then pet details are printed`() {
        //when
        subject.showPets(petEntities)

        //then
        assertThat(
            outputStreamCaptor.toString().trim()
        ).isEqualTo(
            "Pet: ${petEntities[0].pet}\n" +
                    "Type: ${petEntities[0].type}\n" +
                    "Identifier: ${petEntities[0].identifier}\n" +
                    "Owner: ${petEntities[0].owner}\n" +
                    "Owner ID: ${petEntities[0].ownerId}\n" +
                    "\n" +
                    "Pet: ${petEntities[1].pet}\n" +
                    "Type: ${petEntities[1].type}\n" +
                    "Identifier: ${petEntities[1].identifier}\n" +
                    "Owner: ${petEntities[1].owner}\n" +
                    "Owner ID: ${petEntities[1].ownerId}"
        )
    }

    @Test
    fun `showCustomersAndPets() - given customer and pet information then customer and pet details are printed`() {
        //when
        subject.showCustomersAndPets(listOf(singleCustomerEntity), listOf(singlePetEntity))

        //then
        assertThat(
            outputStreamCaptor.toString().trim()
        ).isEqualTo(
            "Customer: ${customerEntities[0].customer}\n" +
                    "Identifier: ${customerEntities[0].identifier}\n" +
                    "Pets: ${customerEntities[0].pets}\n" +
                    "\n" +
                    "Pet: ${singlePetEntity.pet}\n" +
                    "Type: ${singlePetEntity.type}\n" +
                    "Identifier: ${singlePetEntity.identifier}\n" +
                    "Owner: ${singlePetEntity.owner}\n" +
                    "Owner ID: ${singlePetEntity.ownerId}"
        )
    }

    @Test
    fun `showInvalidErrorMessage() - called with line as reference then message printed that includes line number`() {
        //when
        subject.showInvalidErrorMessage(1)

        //then
        assertThat(outputStreamCaptor.toString().trim()).isEqualTo("Error on line 1: Invalid syntax")
    }

    @Test
    fun `showErrorOnLineWithMessage() - called with line number and error message passed as reference then message printed that includes line number and message`() {
        //when
        subject.showErrorOnLineWithMessage(1, "error message")

        //then
        assertThat(outputStreamCaptor.toString().trim()).isEqualTo("Error on line 1: error message")
    }

    @Test
    fun `showError - called with error message passed a reference then message is printed`() {
        //when
        subject.showError("error message")

        //then
        assertThat(outputStreamCaptor.toString().trim()).isEqualTo("error message")
    }

    private companion object {
        val singleCustomerEntity = CustomerEntity("Albert", "1", "Johnny")

        val customerEntities = listOf(
            singleCustomerEntity,
            CustomerEntity("Hugo", "2", "Malt")
        )

        val singlePetEntity = PetEntity("Johnny", "Fish", "1", "Albert", "1")

        val petEntities = listOf(
            singlePetEntity,
            PetEntity("Malt", "Dog", "2", "Hugo", "2")
        )
    }
}