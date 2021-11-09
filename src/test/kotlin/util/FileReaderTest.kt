package util

import FileReader
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockkStatic
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.PrintStream

class FileReaderTest {

    @MockK
    lateinit var file: File

    private val standardOut = System.out

    private val outputStreamCaptor = ByteArrayOutputStream()

    private val subject by lazy { FileReader() }

    @Before
    fun setUp() {
        System.setOut(PrintStream(outputStreamCaptor))
    }

    @After
    fun tearDown() {
        System.setOut(standardOut)
    }

    @Test
    fun `readFile() - given file then return list of strings of the file's contents data`() {
        // given
        val fileName = "src/test/resources/test_vet_patient_file_happy_path_1.txt"
        val expectedOutputString: ArrayList<String> = arrayListOf(
            "customer:1:Sam",
            "customer:2:Voicu",
            "customer:3:Andre",
            "customer:4:Harry",
            "pet:1:dog:Barney:1",
            "pet:2:cat:Mr Fuzzy:1",
            "pet:3:cat:Kitkat:2",
            "pet:4:goldfish:Harry:3",
            "pet:5:dog:Bob:4"
        )

        // when
        val result = subject.readFile(fileName)

        // then
        assertThat(result).isEqualTo(expectedOutputString)
    }

    @Test
    fun `readFile() - given file name that does not exist then file not found message is printed`() {
        // given
        val fileName = "src/main/resources/vpd2_input_file_happy_path_1.txt"

        // when
        subject.readFile(fileName)

        // then
        assertThat(outputStreamCaptor.toString()).isEqualTo("Oops! - File not found.\n")
    }

    @Test
    fun `readFile() - given file name is empty then file not found message is printed`() {
        // given
        val fileName = ""

        // when
        subject.readFile(fileName)

        // then
        assertThat(outputStreamCaptor.toString()).isEqualTo("Oops! - File not found.\n")
    }

    //TODO: mock File, and let it return null pointer exception
    @Test
    fun `readFile() - given file name that does not exist then sorry there is a problem message is printed`() {
        // given
        val fileName = "src/test/"
        every { File("abc") } throws Exception()

        // when
        subject.readFile(fileName)

        // then
        assertThat(outputStreamCaptor.toString()).isEqualTo("Sorry! - There is a problem reading this file. Please try again")
    }
}