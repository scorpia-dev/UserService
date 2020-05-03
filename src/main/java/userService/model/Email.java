package userService.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Data
@Entity
@Validated
@NoArgsConstructor
public class Email {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@NotEmpty(message = "{mail.notEmpty}")
	@Column(unique = true)
	@javax.validation.constraints.Email
	private String mail;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@JoinColumn(name = "userId", nullable = false)
	private User user;

	public int getUser_id() {
		return user.getId();
	}

	public Email(String mail, User user) {
		this.mail = mail;
		this.user = user;
	}

}
