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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * Spring Boot REST controller
 * @author s.shephard2
 *
 */
@RestController
@Api(value="customers")
public class CustomerController {
	
	// Logger for customer.CustomerController
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private CustomerRepository repository;
	
	/**
	 * Health check route: GET / returns success
	 * @return
	 */
	@ApiOperation(value = "Health check",
		    notes = "Returns OK status")
	@RequestMapping(method = RequestMethod.GET, value = "/", produces = "application/json")
	public ResponseEntity<?> healthCheck() {
		logger.info("/ GET");
		return ResponseEntity.ok().build();
	}
	
	/**
	 * Search route: GET /customers
	 * @param requestParams
	 * @return
	 */
	@ApiOperation(value = "Search for customers",
		    notes = "Search by any combination of name, username, email and born_after",
		    response = Customer.class,
		    responseContainer = "List")
	@RequestMapping(method = RequestMethod.GET, value = "/customers", produces = "application/json")
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
	@ApiOperation(value = "Retrieve specific customer",
		    notes = "Retrieve specific customer by customerid")
	@RequestMapping(method = RequestMethod.GET, value = "/customers/{customerId}", produces = "application/json")
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
	@ApiOperation(value = "Create customer",
		    notes = "Create a new customer")
	@RequestMapping(method = RequestMethod.POST, value = "/customers", produces = "application/json")
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
	
	/**
	 * Route to update a customer: PUT /customers
	 * @param customer
	 * @return
	 */
	@ApiOperation(value = "Update existing customer",
		    notes = "Update the values of an existing customer")
	@RequestMapping(method = RequestMethod.PUT, value = "/customers/{customerId}", produces = "application/json")
	public ResponseEntity<Customer> updateCustomer(@Valid @PathVariable Long customerId, @RequestBody Customer customer) {
		
		logger.info("/customers {} PUT {}", customerId, customer.toString());
			
		Customer updateCustomer = repository.findOne(customerId);
		updateCustomer.setFirst_name(customer.getFirst_name());
		updateCustomer.setLast_name(customer.getLast_name());
		updateCustomer.setEmail(customer.getEmail());
		updateCustomer.setUsername(customer.getUsername());
		try {
			// Attempt to save the customer to the repository, may throw validation errors from the DB
			// e.g. uniqueness constraints on username, email
			updateCustomer = repository.save(updateCustomer);
			return ResponseEntity.ok().body(updateCustomer);
		} catch (Exception e) {
			logger.error("Repository save exception {}", e.getMessage());
		}
		return ResponseEntity.badRequest().build();
	}
}
