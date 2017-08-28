package sshephard.customer;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import javax.validation.Constraint;

/**
 * Custom validation annotation
 * Used to check that either email or username is present
 * Implementation in UsernameEmailValidator
 * @author s.shephard2
 *
 */
@Constraint(validatedBy = UsernameEmailValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface UsernameEmail {

	String message() default "Either username or email must be given";
	
	String username();
	
	String email();
	
	Class<?>[] groups() default {};
	Class<?>[] payload() default {};
}
