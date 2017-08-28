package sshephard.customer;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import sshephard.customer.Customer;
import sshephard.customer.CustomerController;
import sshephard.customer.CustomerRepository;

/**
 * Unit tests for Customer REST controller
 * @author s.shephard2
 *
 */
@RunWith(SpringRunner.class)
@WebMvcTest(CustomerController.class)
public class CustomerRestControllerTest {

	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private CustomerRepository customerRepository;
	
	@Test
	public void getCustomer() throws Exception {
		
		Customer customer = new Customer();
		customer.setFirst_name("Stephen");
		customer.setLast_name("Shephard");
		customer.setUsername("sjshephard001");
		
		Mockito.when(customerRepository.findById(1L)).thenReturn(customer);
		
		mvc.perform(get("/customers/1")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.username", is(customer.getUsername())));
	}
}
