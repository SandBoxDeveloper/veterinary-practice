package util

interface Reader {
    fun readFile(fileName: String): List<String>
}