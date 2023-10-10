package com.customermodelpackage;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Customer {

	private Long id;
	private String firstName;
	private Date birthDate;
	private String formattedBirthDate;
	private boolean gender;
	private String email;
	private String hobby;
	private int pastUser;
	private Boolean record;

}