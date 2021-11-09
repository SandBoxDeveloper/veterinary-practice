package mapper.domain

import model.Customer

sealed class CustomerMapperResult {
    data class Success(val customer: Customer) : CustomerMapperResult()
    data class Error(val message: String) : CustomerMapperResult()
}