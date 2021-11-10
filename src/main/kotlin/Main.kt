import database.LocalDatabase
import database.VeterinaryPracticeDatabase
import database.mapper.CustomerEntityMapper
import database.mapper.PetEntityMapper
import database.search.Search
import database.search.SearchDatabase
import mapper.CustomerMapper
import mapper.PetMapper
import presenter.Presenter
import presenter.VeterinaryPracticePresenter
import util.Reader
import view.VeterinaryPracticeConsoleView

// dependency injections
val fileReader: Reader by lazy { FileReader() }
val customerMapper: CustomerMapper by lazy { CustomerMapper() }
val petMapper: PetMapper by lazy { PetMapper() }
val localDatabase: LocalDatabase by lazy { VeterinaryPracticeDatabase }
val customerEntityMapper: CustomerEntityMapper by lazy { CustomerEntityMapper() }
val petEntityMapper: PetEntityMapper by lazy { PetEntityMapper() }
val searchDatabase: Search by lazy { SearchDatabase(localDatabase, customerEntityMapper, petEntityMapper) }
val presenter: Presenter by lazy {
    VeterinaryPracticePresenter(
        fileReader,
        customerMapper,
        petMapper,
        localDatabase,
        searchDatabase
    )
}

fun main() {
    val view = VeterinaryPracticeConsoleView(presenter)

    // greet user
    println("Hi, please enter as follows to use program... {path/filename} {search query}")

    // receive and store inputs
    val firstInput: String
    val secondInput: String

    try {
        val result = readLine().toString().split(' ', limit = 2)
        firstInput = result[0]
        secondInput = result[1]

        if (firstInput.isEmpty()) {
            println("You didn't enter the {path/filename}. Please try again.")
            return
        } else {
            view.startProgram(firstInput)
        }

        if (secondInput.isEmpty()) {
            println("You didn't enter a search query. Please try again.")
            return
        } else {
            view.searchProgram(secondInput)
        }

    } catch (exception: Exception) {
        println("Something went wrong! - did you enter both parameters?")
    }

    // use inputs to start program
    //view.startProgram(firstInput)
    //view.searchProgram(secondInput)
    //src/main/resources/vet_patient_file_happy_path_1.txt
    //src/main/resources/vet_patient_file_unhappy_path_1.txt
    //println("Program arguments: ${firstInput}, $secondInput")
}