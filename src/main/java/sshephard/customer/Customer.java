package sshephard.customer;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * Customer entity
 * @author s.shephard2
 *
 */
@Entity
@EntityListeners(CustomerListener.class)
@UsernameEmail(email = "email", username = "username")
public class Customer {

	// An id that uniquely identifies the customer. This will be provided by the server when the customer is created and may not be changed.
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)    
	private Long id;
	
	// Username
	@Size(min = 0, max = 100)
	@Column(unique=true)
	private String username;
	
	// Email - should contain a superficially valid email
	@Size(min = 0, max = 100)
	@Column(unique=true)
	@Email(message = "The email address must be in the format of name@domain.com")
	private String email;
	
	// First name
    @Size(min = 0, max = 25)
    @Pattern(regexp = "[A-Za-z-' ]+", message = "Please use a name without numbers or special characters")
	private String first_name;
	
	// Last name - required to be non-blank
	@NotNull
    @Size(min = 1, max = 25)
    @Pattern(regexp = "[A-Za-z-' ]+", message = "Please use a name without numbers or special characters")
	private String last_name;
	
	// A display_name, which if not defined at creation should be the first name and last names joined with a space.
	@Size(min = 0, max = 60)
	@Pattern(regexp = "[A-Za-z-' ]+", message = "Please use a name without numbers or special characters")
	private String display_name;
	
	/* 
	 * The created_at datetime when the customer was added to the system, assigned by the system when the customer is created.
	 * Formatted 'YYYY-MM-DD HH:mm:ss' e.g. '2016-11-08 22:18:03' for 'November 8, 2016 at 10:18:03 PM'.
	 */
	@Past
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	private Date created_at;
	
	/* 
	 * The date of birth of the customer
	 * It should be formatted 'YYYY-MM-DD' -- for example, '2016-11-08' for 'November 8, 2016'.
	 */
	@JsonFormat(pattern="yyyy-MM-dd")
	@Temporal(TemporalType.DATE)
	private Date birthdate;
	
	/**
	 * Empty Javabeans constructor
	 */
	protected Customer() {
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the first_name
	 */
	public String getFirst_name() {
		return first_name;
	}

	/**
	 * @param first_name the first_name to set
	 */
	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	/**
	 * @return the last_name
	 */
	public String getLast_name() {
		return last_name;
	}

	/**
	 * @param last_name the last_name to set
	 */
	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	/**
	 * @return the display_name
	 */
	public String getDisplay_name() {
		return display_name;
	}

	/**
	 * @param display_name the display_name to set
	 */
	public void setDisplay_name(String display_name) {
		this.display_name = display_name;
	}

	/**
	 * @return the created_at
	 */
	public Date getCreated_at() {
		return created_at;
	}

	/**
	 * @param created_at the created_at to set
	 */
	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}
	
	/**
	 * @return the birthdate
	 */
	public Date getBirthdate() {
		return birthdate;
	}

	/**
	 * @param birthdate the birthdate to set
	 */
	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Customer [id=" + id + ", username=" + username + ", email=" + email + ", first_name=" + first_name
				+ ", last_name=" + last_name + ", display_name=" + display_name + ", created_at=" + created_at
				+ ", birthdate=" + birthdate + "]";
	}

}
