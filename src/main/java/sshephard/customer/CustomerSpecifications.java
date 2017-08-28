package sshephard.customer;

import java.util.Date;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

/**
 * Separate search criteria for customers
 * Implemented as Specifications, so that they can be combined
 * @author s.shephard2
 *
 */
public class CustomerSpecifications {

	/**
	 * Partial match on username
	 * @param value
	 * @return
	 */
	public static Specification<Customer> partialMatchUsername(String value) {
		return new Specification<Customer> () {
			public Predicate toPredicate(Root<Customer> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				// Perform case-insensitive LIKE search
				return builder.like(builder.lower(root.get(Customer_.username)), "%" + value.toLowerCase() + "%");
		      }
		};
	}
	
	/**
	 * Partial match on email
	 */
	public static Specification<Customer> partialMatchEmail(String value) {
		return new Specification<Customer> () {
			public Predicate toPredicate(Root<Customer> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				// Perform case-insensitive LIKE search
				return builder.like(builder.lower(root.get(Customer_.email)), "%" + value.toLowerCase() + "%");
		      }
		};
	}
	
	/**
	 * Partial match against any of the first name, last name, and display name
	 * @param value
	 * @return
	 */
	public static Specification<Customer> partialMatchName(String value) {
		return new Specification<Customer> () {
			public Predicate toPredicate(Root<Customer> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				// Predicate for each name field is case-insensitive LIKE search
				String likeValue = "%" + value.toLowerCase() + "%";
				Predicate partialMatchFirstName = builder.like(builder.lower(root.get(Customer_.first_name)), likeValue);
				Predicate partialMatchLastName = builder.like(builder.lower(root.get(Customer_.last_name)), likeValue);
				Predicate partialMatchDisplayName = builder.like(builder.lower(root.get(Customer_.display_name)), likeValue);
				
				return builder.or(partialMatchFirstName, partialMatchLastName, partialMatchDisplayName);
		      }
		};
	}
	
	/**
	 * Match customers who were born after a particular date
	 * @param value
	 * @return
	 */
	public static Specification<Customer> bornAfter(Date value) {
		return new Specification<Customer> () {
			public Predicate toPredicate(Root<Customer> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				return builder.greaterThanOrEqualTo(root.get(Customer_.birthdate), value);
			}
		};
	}
}
