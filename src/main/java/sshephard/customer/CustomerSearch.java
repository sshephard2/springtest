package sshephard.customer;

import java.util.List;

/**
 * Separate entity for customer search results
 * @author s.shephard2
 *
 */
public class CustomerSearch {
	
	// List of customers returned from search
	private List<Customer> customers;

	protected CustomerSearch() {
	}

	/**
	 * @return the customers
	 */
	public List<Customer> getCustomers() {
		return customers;
	}

	/**
	 * @param customers the customers to set
	 */
	public void setCustomers(List<Customer> customers) {
		this.customers = customers;
	}

}
