package userService.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Data
@Entity
@Validated
@NoArgsConstructor
public class PhoneNumber {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@NotEmpty(message = "{number.notEmpty}")
	@Pattern(regexp = "(^$|[0-9]{11})", message = "Phone number must be 11 digits")
	@Column(unique = true)
	private String number;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@JoinColumn(name = "userId", nullable = false)
	private User user;

	public int getUser_id() {
		return user.getId();
	}

	public PhoneNumber(String number, User user) {
		this.number = number;
		this.user = user;
	}

}
