package com.bext.spring;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import com.bext.model.Address;
import com.bext.model.Customer;
import com.bext.model.Name;
import com.bext.model.Order;
import com.bext.model.OrderDto;

public class ModelMapperTest {

    @Test
	public void modelMapper_map() {
    	Order order = new Order(new Customer( new Name("Jose Alberto","Martinez")), new Address("Main Street", "CDMX"));
    	ModelMapper modelMapper = new ModelMapper();
    	OrderDto orderDto = modelMapper.map(order, OrderDto.class);
    	
		Assertions.assertEquals(order.getCustomer().getName().getFirstName(), orderDto.getCustomerFirstName());
		Assertions.assertEquals(order.getCustomer().getName().getLastName(), orderDto.getCustomerLastName());
		Assertions.assertEquals(order.getBillingAddress().getStreet(), orderDto.getAddressStreet());
		Assertions.assertEquals(order.getBillingAddress().getCity(), orderDto.getAddressCity());
	}

}
