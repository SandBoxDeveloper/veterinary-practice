package mapper

import model.Customer
import mapper.domain.CustomerMapperResult

class CustomerMapper {

    fun map(contents: List<String>): CustomerMapperResult {
        return if(contents.size < 3){
            CustomerMapperResult.Error("Mapping failed due to invalid syntax")
        } else if (contents.size > 3) {
            CustomerMapperResult.Error("Customer mapper exception - contents has more that 3 fields")
        } else {
            CustomerMapperResult.Success(
                Customer(
                    id = contents[INDEX_CUSTOMER_ID],
                    name = contents[INDEX_CUSTOMER_NAME]
                )
            )
        }
    }

    private companion object {
        const val INDEX_CUSTOMER_ID = 1
        const val INDEX_CUSTOMER_NAME = 2
    }
}