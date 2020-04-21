package userService.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;
import javax.validation.constraints.Positive;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import userService.model.PhoneNumber;
import userService.model.User;
import userService.repositories.PhoneNumberRepository;
import userService.repositories.UserRepository;

@Service
public class PhoneNumberService {

	@Autowired
	PhoneNumberRepository phoneNumberRepository;
	
	@Autowired
	UserRepository userRepository;
	
	public PhoneNumber addNewPhoneNumber(PhoneNumber phoneNumber, int userId) {
		 List<PhoneNumber> phoneNumbers = new ArrayList<PhoneNumber>();
		 User user1= new User();
		 
		User user = userRepository.findById(userId).orElseThrow(
					() -> new EntityNotFoundException("the user with id " + userId + " was not found"));
		
		phoneNumber.setUser(user);
		
		PhoneNumber newPhoneNumber = phoneNumberRepository.save(phoneNumber);
		
		phoneNumbers.add(newPhoneNumber);
		user1.setPhoneNumbers(phoneNumbers);
		
		return newPhoneNumber;
	}

	public PhoneNumber updatePhoneNumber(PhoneNumber phoneNumber, @Positive int phoneNumberId) {
		PhoneNumber existingPhoneNumber = phoneNumberRepository.findById(phoneNumberId).orElseThrow(
				() -> new EntityNotFoundException("the phoneNumber with id " + phoneNumberId + " was not found"));

		existingPhoneNumber.setNumber(phoneNumber.getNumber());
		return phoneNumberRepository.save(existingPhoneNumber);
	}
	
}
