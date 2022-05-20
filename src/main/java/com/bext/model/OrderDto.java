package com.bext.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {
	private String customerFirstName;
	private String customerLastName;
	private String billingAddressStreet;
	private String billingAddressCity;
	private String customerAlternateFirstName;
	private String addressAlternateStreet;
}
