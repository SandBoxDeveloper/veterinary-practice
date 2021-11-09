package mapper

import com.google.common.truth.Truth.assertThat
import io.mockk.MockKAnnotations
import mapper.domain.CustomerMapperResult
import model.Customer
import org.junit.Before
import org.junit.Test

class CustomerMapperTest {

    private val subject by lazy { CustomerMapper() }

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `map() - given list of customer contents with less than 3 fields then customer mapper result error is returned`() {
        // given
        val customerDataWithInvalidNumberOfFields = listOf("customer", "3")
        val expectedCustomerObject = CustomerMapperResult.Error("Mapping failed due to invalid syntax")

        // when
        val customer = subject.map(customerDataWithInvalidNumberOfFields)

        // then
        assertThat(customer).isEqualTo(expectedCustomerObject)
    }

    @Test
    fun `map() - given list of customer contents with more than 3 fields then customer mapper result error is returned`() {
        // given
        val customerDataWithExtraField = listOf("customer", "3", "Andre", "some random field")
        val expectedCustomerObject = CustomerMapperResult.Error("Customer mapper exception - contents has more that 3 fields")

        // when
        val customer = subject.map(customerDataWithExtraField)

        // then
        assertThat(customer).isEqualTo(expectedCustomerObject)
    }

    @Test
    fun `map() - given list of customer contents then mapped customer objected is returned`() {
        // given
        val customerData = listOf("customer", "3", "Andre")
        val expectedCustomerObject = CustomerMapperResult.Success(Customer(id = "3", name = "Andre"))

        // when
        val customer = subject.map(customerData)

        // then
        assertThat(customer).isEqualTo(expectedCustomerObject)
    }
}