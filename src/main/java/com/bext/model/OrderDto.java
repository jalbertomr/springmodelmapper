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
	private String addressStreet;
	private String addressCity;
	private String customerAlternateFirstName;
	public String addressAlternateStreet;
}
