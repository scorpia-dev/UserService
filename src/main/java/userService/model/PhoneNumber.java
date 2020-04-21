package userService.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotEmpty;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Validated
@NoArgsConstructor
public class PhoneNumber {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
		
	@NotEmpty(message = "{number.notEmpty}")
	@Column(unique = true)
	private String number;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@JoinColumn(name="userId",nullable = false)
    private User user;

	   public int getUser_id(){
	        return user.getId();
	    }

}
