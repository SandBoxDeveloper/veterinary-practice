package database.search

import database.model.SearchQueryResult

interface Search {
    fun queryAllDatabaseTable(query: String): SearchQueryResult
}