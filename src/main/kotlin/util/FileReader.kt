import util.Reader
import java.io.File
import java.io.FileNotFoundException

class FileReader : Reader {

    override fun readFile(fileName: String): List<String> {
        var lines: List<String> = emptyList()

        try {
            lines = File(fileName).readLines()
        } catch (exception: Exception) {
            when (exception) {
                is FileNotFoundException -> println("Oops! - File not found.")
                else -> println("Sorry! - There is a problem reading this file. Please try again")
            }
        }

        return lines
    }
}