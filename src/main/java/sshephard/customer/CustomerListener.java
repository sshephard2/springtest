package sshephard.customer;

import java.util.Date;

import javax.persistence.PrePersist;

public class CustomerListener {

	/**
	 * Set default field values before persisting
	 */
	@PrePersist
	public void defaults(Customer c) {
		// Set created date to current date and time
		Date currentDate = new Date();		
		c.setCreated_at(currentDate);
		
		// If display name is null or empty, set it to first name and last names joined with a space
		if (c.getDisplay_name() == null || c.getDisplay_name().isEmpty()) {
			c.setDisplay_name(c.getFirst_name() + " " + c.getLast_name());
		}
	}
}
