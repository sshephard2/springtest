package sshephard.customer;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Spring Boot REST controller
 * @author s.shephard2
 *
 */
@RestController
public class CustomerController {
	
	// Logger for customer.CustomerController
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private CustomerRepository repository;
	
	/**
	 * Health check route: GET / returns success
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/")
	public ResponseEntity<?> healthCheck() {
		logger.info("/ GET");
		return ResponseEntity.ok().build();
	}
	
	/**
	 * Search route: GET /customers
	 * @param requestParams
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/customers")
	public ResponseEntity<CustomerSearch> search(@RequestParam Map<String,String> requestParams) {
		logger.info("/customers GET");
		
		List<Specification<Customer>> searchSpecifications = new ArrayList<Specification<Customer>>();
		
		// name (which is a partial match against any of the first name, last name, and display name)
		String name = requestParams.get("name");
		if (name != null && !name.isEmpty()) {
			logger.info("name={}", name);
			// Add partial match of name to the list of search criteria
			searchSpecifications.add(CustomerSpecifications.partialMatchName(name));
		}
		
		// username (partial match)
		String username = requestParams.get("username");
		if (username != null && !username.isEmpty()) {
			logger.info("username={}", username);
			// Add partial match of username to the list of search criteria
			searchSpecifications.add(CustomerSpecifications.partialMatchUsername(username));
		}
		
		// email (partial match)
		String email = requestParams.get("email");
		if (email != null && !email.isEmpty()) {
			logger.info("email={}", email);
			// Add partial match of email to the list of search criteria
			searchSpecifications.add(CustomerSpecifications.partialMatchEmail(email));
		}
		
		// born_after (date formatted YYYY-MM-DD that will return customers who started on or after a particular date)
		String born_after = requestParams.get("born_after");
		if (born_after != null && !born_after.isEmpty()) {
			logger.info("born_after={}", born_after);
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			try {
				Date born_after_date = df.parse(born_after);
				// Add search for started on or after given date to list of search criteria
				searchSpecifications.add(CustomerSpecifications.bornAfter(born_after_date));
			} catch (ParseException e) {
				logger.error("Can't parse date {}", born_after);
			}
		}
		
		// If there are no search criteria, return a BAD REQUEST error
		if (searchSpecifications.isEmpty()) {
			return ResponseEntity.badRequest().build();
		}
		
		// AND together all the search criteria
        Specification<Customer> search = searchSpecifications.get(0);
        for (int i = 1; i < searchSpecifications.size(); i++) {
        	search = Specifications.where(search).and(searchSpecifications.get(i));
        }
		
        CustomerSearch searchResult = new CustomerSearch();
        searchResult.setCustomers(repository.findAll(search));
        return ResponseEntity.ok().body(searchResult);
        
	}
	
	/**
	 * Route to retrieve a customer: GET /customers/{id}
	 * @param customerId
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/customers/{customerId}")
	public ResponseEntity<Customer> customer(@PathVariable Long customerId) {
	
		logger.info("/customers/{} GET", customerId);
		Customer foundCustomer = repository.findById(customerId);
		if (foundCustomer != null) {
			return ResponseEntity.ok().body(foundCustomer);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	/**
	 * Route to create a customer: POST /customers
	 * @param customer
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/customers")
	public ResponseEntity<Customer> createCustomer(@Valid @RequestBody Customer customer) {
		
		logger.info("/customers {} POST", customer.toString());
			
		Customer createdCustomer;
		try {
			// Attempt to save the customer to the repository, may throw validation errors from the DB
			// e.g. uniqueness constraints on username, email
			createdCustomer = repository.save(customer);
			return ResponseEntity.ok().body(createdCustomer);
		} catch (Exception e) {
			logger.error("Repository save exception {}", e.getMessage());
		}
		return ResponseEntity.badRequest().build();
	}
}
