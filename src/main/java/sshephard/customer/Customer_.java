package sshephard.customer;

import java.util.Date;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

/**
 * Customer metamodel for specifications
 * @author s.shephard2
 *
 */
@StaticMetamodel(Customer.class)
public class Customer_ {

	public static volatile SingularAttribute<Customer, Long> id;
	public static volatile SingularAttribute<Customer, String> first_name;
	public static volatile SingularAttribute<Customer, String> last_name;
	public static volatile SingularAttribute<Customer, String> display_name;
	public static volatile SingularAttribute<Customer, String> username;
	public static volatile SingularAttribute<Customer, String> email;
	public static volatile SingularAttribute<Customer, Date> created_at;
	public static volatile SingularAttribute<Customer, Date> birthdate;

}
