package userService.controller;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import userService.model.PhoneNumber;
import userService.service.PhoneNumberService;

@RequestMapping("/phonenumber")
@RestController
@Validated
public class PhoneController {

	@Autowired
	PhoneNumberService phoneNumberService;

	@PutMapping("/update/{phoneNumberId}")
	public PhoneNumber updateExistingPhoneNumber(@PathVariable @Positive int phoneNumberId,
			@RequestBody @Valid PhoneNumber phoneNumber) {
		return phoneNumberService.updatePhoneNumber(phoneNumber, phoneNumberId);
	}

	@PutMapping("/add/{userId}")
	public PhoneNumber addNewPhoneNumber(@PathVariable @Positive int userId,
			@Valid @RequestBody PhoneNumber phoneNumber) {
		return phoneNumberService.addNewPhoneNumber(phoneNumber, userId);
	}

}
