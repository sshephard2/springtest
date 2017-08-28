package sshephard.customer;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import sshephard.customer.Customer;
import sshephard.customer.CustomerRepository;
import sshephard.customer.CustomerSpecifications;

/**
 * Unit tests for repository searches
 * @author s.shephard2
 *
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class CustomerRepositorySearchTest {

	@Autowired
	private TestEntityManager entityManager;
	
	@Autowired
	private CustomerRepository customerRepository;
	
	/**
	 * Find by customer id
	 */
	@Test
	public void validFindById() {
		
		// Create customer entity
		Customer customer1 = new Customer();
		customer1.setFirst_name("Stephen");
		customer1.setLast_name("Shephard");
		customer1.setUsername("sjshephard");

		// Persist customer entity
		entityManager.persist(customer1);
		entityManager.flush();
		
		// Lookup customer by id
		Customer customer2 = customerRepository.findById(customer1.getId());
		
		// Test
		assertThat(customer2.getId()).isEqualTo(customer1.getId());
	}
	
	/**
	 * Non-existent customer id
	 */
	@Test
	public void invalidFindById() {
		// Lookup customer by id
		Customer customer = customerRepository.findById(6395202L);
		
		// Test
		assertThat(customer).isNull();
	}
	
	/**
	 * Search by partial match on username
	 */
	@Test
	public void validSearchUsername() {
		// Create customer entity
		Customer customer1 = new Customer();
		customer1.setFirst_name("Stephen");
		customer1.setLast_name("Shephard");
		customer1.setUsername("stephen");

		// Persist customer entity
		entityManager.persist(customer1);
		entityManager.flush();
		
		// Search by username
		List<Customer> results = customerRepository.findAll(CustomerSpecifications.partialMatchUsername("step"));
		
		// Test
		assertThat(results.get(0).getUsername()).isEqualTo(customer1.getUsername());
	}
	
	/**
	 * Invalid search by partial match on username
	 */
	@Test
	public void invalidSearchUsername() {	
		// Search by username
		List<Customer> results = customerRepository.findAll(CustomerSpecifications.partialMatchUsername("XXXX6395202L"));
		
		// Test
		assertThat(results.size()).isEqualTo(0);
	}
	
	/**
	 * Search by partial match on email
	 */
	@Test
	public void validSearchEmail() {
		// Create customer entity
		Customer customer1 = new Customer();
		customer1.setFirst_name("Stephen");
		customer1.setLast_name("Shephard");
		customer1.setEmail("stephenshephard@spring.com");

		// Persist customer entity
		entityManager.persist(customer1);
		entityManager.flush();
		
		// Search by email
		List<Customer> results = customerRepository.findAll(CustomerSpecifications.partialMatchEmail("step"));
		
		// Test
		assertThat(results.get(0).getEmail()).isEqualTo(customer1.getEmail());
	}
	
	/**
	 * Invalid search by partial match on email
	 */
	@Test
	public void invalidSearchEmail() {	
		// Search by email
		List<Customer> results = customerRepository.findAll(CustomerSpecifications.partialMatchEmail("XXXX6395202L"));
		
		// Test
		assertThat(results.size()).isEqualTo(0);
	}
	
	/**
	 * Search by partial match on name field
	 */
	@Test
	public void validSearchName() {
		// Create customer entity
		Customer customer1 = new Customer();
		customer1.setFirst_name("John");
		customer1.setLast_name("Smith");
		customer1.setDisplay_name("CEO");
		customer1.setUsername("username001");

		// Persist customer entity
		entityManager.persist(customer1);
		entityManager.flush();
		
		// Search by email
		List<Customer> results = customerRepository.findAll(CustomerSpecifications.partialMatchName("Smit"));
		
		// Test
		assertThat(results.get(0).getLast_name()).isEqualTo(customer1.getLast_name());
	}
	
	/**
	 * Invalid search by partial match on name field
	 */
	@Test
	public void invalidSearchName() {	
		// Search by email
		List<Customer> results = customerRepository.findAll(CustomerSpecifications.partialMatchName("XXXXX"));
		
		// Test
		assertThat(results.size()).isEqualTo(0);
	}
	
	/**
	 * Born after date test
	 */
	@Test
	public void searchBornAfter() {
		// Create customer entity
		Customer customer1 = new Customer();
		customer1.setFirst_name("Carmen");
		customer1.setLast_name("Jones");
		customer1.setUsername("username002");
		Calendar dob = Calendar.getInstance();
		dob.set(1971, Calendar.DECEMBER,18);
		customer1.setBirthdate(dob.getTime());
		
		// Persist customer entity
		entityManager.persist(customer1);
		entityManager.flush();
		
		// Search by birth date
		Calendar searchDate = Calendar.getInstance();
		searchDate.set(1970, Calendar.JANUARY,1);
		List<Customer> results = customerRepository.findAll(CustomerSpecifications.bornAfter(searchDate.getTime()));
		
		// Test
		assertThat(results.get(0).getId()).isEqualTo(customer1.getId());
	}

}