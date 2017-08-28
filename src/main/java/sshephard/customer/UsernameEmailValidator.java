package sshephard.customer;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.BeanWrapperImpl;

/**
 * Custom validation to check that at least one of email or username is present
 * @author s.shephard2
 *
 */
public class UsernameEmailValidator implements ConstraintValidator<UsernameEmail, Object> {
	
	private String username;
	private String email;

	@Override
	public void initialize(UsernameEmail constraint) {
		this.username = constraint.username();
		this.email = constraint.email();		
	}

	@Override
	public boolean isValid(Object object, ConstraintValidatorContext context) {

		String usernameValue = (String) new BeanWrapperImpl(object).getPropertyValue(username);
		String emailValue = (String) new BeanWrapperImpl(object).getPropertyValue(email);
		
		if (usernameValue != null && !usernameValue.isEmpty()) {
				return true;
		}
		if (emailValue != null && !emailValue.isEmpty()) {
				return true;
		}
		return false;
	}
}
