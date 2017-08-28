package sshephard.customer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Calendar;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import sshephard.customer.Customer;
import sshephard.customer.CustomerRepository;

/**
 * Unit tests for validation on creating and persisting Customers
 * @author s.shephard2
 *
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class CustomerRepositoryValidationTest {

	@Autowired
	private TestEntityManager entityManager;
	
	@Autowired
	private CustomerRepository customerRepository;
	
	/**
	 * Check first name for valid characters
	 */
	@Test
	public void invalidFirstNameCharacter() {
		
		// Create customer entity
		Customer customer = new Customer();
		customer.setFirst_name("Steph3n");
		customer.setLast_name("Shephard");
		customer.setUsername("sjshephard001");

		// Persist customer entity
		assertThatThrownBy(() -> {entityManager.persist(customer); entityManager.flush();})
			.isInstanceOf(javax.validation.ConstraintViolationException.class)
			.hasMessageContaining("without numbers");
	}
	
	/**
	 * Check last name for valid characters
	 */
	@Test
	public void invalidLastNameCharacter() {
		
		// Create customer entity
		Customer customer = new Customer();
		customer.setFirst_name("Stephen");
		customer.setLast_name("Sheph@rd");
		customer.setUsername("sjshephard002");

		// Persist customer entity
		assertThatThrownBy(() -> {entityManager.persist(customer); entityManager.flush();})
			.isInstanceOf(javax.validation.ConstraintViolationException.class)
			.hasMessageContaining("without numbers");
	}
	
	/**
	 * Check that last name is mandatory
	 */
	@Test
	public void missingLastName() {
		
		// Create customer entity
		Customer customer = new Customer();
		customer.setFirst_name("Stephen");
		customer.setUsername("sjshephard003");

		// Persist customer entity
		assertThatThrownBy(() -> {entityManager.persist(customer); entityManager.flush();})
			.isInstanceOf(javax.validation.ConstraintViolationException.class)
			.hasMessageContaining("may not be null");
	}
	
	/**
	 * Check that first name allows 25 characters
	 */
	@Test
	public void validLongFirstName() {
		
		// Create customer entity
		Customer customer = new Customer();
		customer.setFirst_name("Abcdefghijklmnopqrstuvwxy");
		customer.setLast_name("Shephard");
		customer.setUsername("sjshephard004");
		
		// Persist customer entity
		entityManager.persist(customer);
		entityManager.flush();
		
		assertThat(customerRepository.findOne(customer.getId()).getFirst_name())
			.isEqualTo("Abcdefghijklmnopqrstuvwxy");
	}
	
	/**
	 * Check that last name allows 25 characters
	 */
	@Test
	public void validLongLastName() {
		
		// Create customer entity
		Customer customer = new Customer();
		customer.setFirst_name("Stephen");
		customer.setLast_name("Abcdefghijklmnopqrstuvwxy");
		customer.setUsername("sjshephard005");

		
		// Persist customer entity
		entityManager.persist(customer);
		entityManager.flush();
		
		assertThat(customerRepository.findOne(customer.getId()).getLast_name())
			.isEqualTo("Abcdefghijklmnopqrstuvwxy");
	}
	
	/**
	 * Check that first name does not allow more than 25 characters
	 */
	@Test
	public void invalidLongFirstName() {
		
		// Create customer entity
		Customer customer = new Customer();
		customer.setFirst_name("Abcdefghijklmnopqrstuvwxyz");
		customer.setLast_name("Shephard");
		customer.setUsername("sjshephard006");
		
		// Persist customer entity
		assertThatThrownBy(() -> {entityManager.persist(customer); entityManager.flush();})
			.isInstanceOf(javax.validation.ConstraintViolationException.class)
			.hasMessageContaining("size must be between");
	}
	
	/**
	 * Check that last name does not allow more than 25 characters
	 */
	@Test
	public void invalidLongLastName() {
		
		// Create customer entity
		Customer customer = new Customer();
		customer.setFirst_name("Stephen");
		customer.setLast_name("Abcdefghijklmnopqrstuvwxyz");
		customer.setUsername("sjshephard007");
		
		// Persist customer entity
		assertThatThrownBy(() -> {entityManager.persist(customer); entityManager.flush();})
			.isInstanceOf(javax.validation.ConstraintViolationException.class)
			.hasMessageContaining("size must be between");
	}
	
	/**
	 * Check display name for valid characters
	 */
	@Test
	public void invalidDisplayNameCharacter() {
		// Create customer entity
		Customer customer = new Customer();
		customer.setFirst_name("Stephen");
		customer.setLast_name("Shephard");
		customer.setDisplay_name("123");
		customer.setUsername("sjshephard008");

		// Persist customer entity
		assertThatThrownBy(() -> {entityManager.persist(customer); entityManager.flush();})
			.isInstanceOf(javax.validation.ConstraintViolationException.class)
			.hasMessageContaining("without numbers");
	}
	
	/**
	 * Check that display name allows 60 characters
	 */
	@Test
	public void validLongDisplayName() {
		// Create customer entity
		Customer customer = new Customer();
		customer.setFirst_name("Stephen");
		customer.setLast_name("Shephard");
		customer.setDisplay_name("AbcdefghijklmnopqrstuvwxyAbcdefghijklmnopqrstuvwxyAbcdefghij");
		customer.setUsername("sjshephard009");
		
		// Persist customer entity
		entityManager.persist(customer);
		entityManager.flush();
		
		assertThat(customerRepository.findOne(customer.getId()).getDisplay_name())
			.isEqualTo("AbcdefghijklmnopqrstuvwxyAbcdefghijklmnopqrstuvwxyAbcdefghij");
	}
	
	/**
	 * Check that display name does not allow more than 60 characters
	 */
	@Test
	public void invalidLongDisplayName() {
		// Create customer entity
		Customer customer = new Customer();
		customer.setFirst_name("Stephen");
		customer.setLast_name("Shephard");
		customer.setDisplay_name("AbcdefghijklmnopqrstuvwxyAbcdefghijklmnopqrstuvwxyAbcdefghijk");
		customer.setUsername("sjshephard010");
		
		// Persist customer entity
		assertThatThrownBy(() -> {entityManager.persist(customer); entityManager.flush();})
			.isInstanceOf(javax.validation.ConstraintViolationException.class)
			.hasMessageContaining("size must be between");
	}
	
	/**
	 * Check valid email
	 */
	@Test
	public void validEmail() {
		// Create customer entity
		Customer customer = new Customer();
		customer.setFirst_name("Stephen");
		customer.setLast_name("Shephard");
		customer.setEmail("sjshephard@gmail.com");
		
		// Persist customer entity
		entityManager.persist(customer);
		entityManager.flush();
		
		assertThat(customerRepository.findOne(customer.getId()).getEmail())
			.isEqualTo("sjshephard@gmail.com");
	}
	
	/**
	 * Check invalid email
	 */
	@Test
	public void invalidEmail() {
		// Create customer entity
		Customer customer = new Customer();
		customer.setFirst_name("Stephen");
		customer.setLast_name("Shephard");
		customer.setEmail("sjshephard");
		
		// Persist customer entity
		assertThatThrownBy(() -> {entityManager.persist(customer); entityManager.flush();})
			.isInstanceOf(javax.validation.ConstraintViolationException.class)
			.hasMessageContaining("email address must be in the format");
	}
	
	/**
	 * One of email or username must be present
	 */
	@Test
	public void noEmailOrUsername() {
		// Create customer entity
		Customer customer = new Customer();
		customer.setFirst_name("Stephen");
		customer.setLast_name("Shephard");

		// Persist customer entity
		assertThatThrownBy(() -> {entityManager.persist(customer); entityManager.flush();})
			.isInstanceOf(javax.validation.ConstraintViolationException.class)
			.hasMessageContaining("username or email must be given");
	}
	
	/**
	 * Check for unique email
	 */
	@Test
	public void uniqueEmail() {
		// Create customer entity
		Customer customer1 = new Customer();
		customer1.setFirst_name("Stephen");
		customer1.setLast_name("Shephard");
		customer1.setEmail("sjshephard1@gmail.com");
		Calendar dob = Calendar.getInstance();
		dob.set(1971, Calendar.DECEMBER,18);
		customer1.setBirthdate(dob.getTime());
		
		// Persist customer entity
		entityManager.persist(customer1);
		entityManager.flush();
		
		// Create second customer entity
		Customer customer2 = new Customer();
		customer2.setFirst_name("John");
		customer2.setLast_name("Smith");
		customer2.setEmail("sjshephard1@gmail.com");
		customer2.setBirthdate(dob.getTime());
		
		// Persist customer entity
		assertThatThrownBy(() -> {entityManager.persist(customer2); entityManager.flush();})
			.isInstanceOf(javax.persistence.PersistenceException.class);
	}
	
	/**
	 * Check for unique username
	 */
	@Test
	public void uniqueUsername() {
		// Create customer entity
		Customer customer1 = new Customer();
		customer1.setFirst_name("Stephen");
		customer1.setLast_name("Shephard");
		customer1.setUsername("username001");
		Calendar dob = Calendar.getInstance();
		dob.set(1971, Calendar.DECEMBER,18);
		customer1.setBirthdate(dob.getTime());
		
		// Persist customer entity
		entityManager.persist(customer1);
		entityManager.flush();
		
		// Create second customer entity
		Customer customer2 = new Customer();
		customer2.setFirst_name("John");
		customer2.setLast_name("Smith");
		customer2.setUsername("username001");
		customer2.setBirthdate(dob.getTime());
		
		// Persist customer entity
		assertThatThrownBy(() -> {entityManager.persist(customer2); entityManager.flush();})
			.isInstanceOf(javax.persistence.PersistenceException.class);
	}
}
