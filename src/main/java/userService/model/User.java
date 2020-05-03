package userService.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Validated
@NoArgsConstructor
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "userId")
	private int id;

	@Pattern(regexp = "^[\\p{L} .'-]+$", message = "a name can only contain letters and spaces")
	@Size(min = 1, max = 30)
	@NotEmpty(message = "{firstName.notEmpty}")
	private String firstName;

	@Pattern(regexp = "^[\\p{L} .'-]+$", message = "a name can only contain letters and spaces")
	@Size(min = 1, max = 30)
	@NotEmpty(message = "{lastName.notEmpty}")
	private String lastName;

	@ToString.Exclude
	@OneToMany(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.PERSIST)
	private List<Email> emails = new ArrayList<Email>();

	@ToString.Exclude
	@OneToMany(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.PERSIST)
	private List<PhoneNumber> phoneNumbers = new ArrayList<PhoneNumber>();

	public User(String firstName, String lastName, List<Email> emails, List<PhoneNumber> phoneNumbers) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.emails = emails;
		this.phoneNumbers = phoneNumbers;
	}

}
