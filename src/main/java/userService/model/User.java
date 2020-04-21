package userService.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
//import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.validation.annotation.Validated;

//import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Validated
@NoArgsConstructor
public class User {

	public User(String firstName, String lastName, List<Email> emails, List<PhoneNumber> phoneNumbers) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.emails = emails;
		this.phoneNumbers = phoneNumbers;
	}

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name ="userId")
	private int id;

	@Pattern(regexp = "^[\\p{L} .'-]+$", message = "a name can only contain letters and spaces")
	@Size(min = 1, max = 30)
	@NotEmpty(message = "{firstName.notEmpty}")
	private String firstName;
	
	@Pattern(regexp = "^[\\p{L} .'-]+$", message = "a name can only contain letters and spaces")
	@Size(min = 1, max = 30)
	@NotEmpty(message = "{lastName.notEmpty}")
	private String lastName;


   
    @OneToMany(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
	private List<Email> emails = new ArrayList<Email>();
	
    @OneToMany(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.PERSIST)
	 private List<PhoneNumber> phoneNumbers = new ArrayList<PhoneNumber>();

}
