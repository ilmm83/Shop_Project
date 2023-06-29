package repository;

import com.shop.admin.customer.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CustomerRepositoryTest {

    @Spy
    CustomerRepository repository;


    @Test
    void canEnableCustomerById() {
        // given
        var customerId = 1L;

        willDoNothing().given(repository).enableCustomerById(customerId, true);

        // when
        repository.enableCustomerById(customerId, true);

        // then
        verify(repository).enableCustomerById(customerId, true);
    }
}
