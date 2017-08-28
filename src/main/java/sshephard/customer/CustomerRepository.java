package sshephard.customer;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

/**
 * Customer Repository interface
 * Define methods to be implemented by Spring
 * @author s.shephard2
 *
 */
public interface CustomerRepository extends CrudRepository<Customer, Long>, JpaSpecificationExecutor<Customer> {

	/**
	 * Method returning a customer entity given a unique id
	 * @param id
	 * @return
	 */
	Customer findById(Long id);
}
