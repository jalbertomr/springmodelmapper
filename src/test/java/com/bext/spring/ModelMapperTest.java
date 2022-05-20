package com.bext.spring;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import com.bext.model.Address;
import com.bext.model.Customer;
import com.bext.model.Name;
import com.bext.model.Order;
import com.bext.model.OrderDto;
import org.modelmapper.ValidationException;
import org.modelmapper.convention.MatchingStrategies;

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

	@Test
	public void modelMapper_map_addMappings() {
		Order order = new Order(new Customer( new Name("Jose Alberto","Martinez")), new Address("Main Street", "CDMX"));
		ModelMapper modelMapper = new ModelMapper();

		modelMapper.typeMap(Order.class, OrderDto.class).addMappings(mapper -> {
			mapper.map( src -> order.getCustomer().getName().getFirstName(), OrderDto::setCustomerAlternateFirstName);
			mapper.map( src -> order.getBillingAddress().getStreet(), OrderDto::setAddressAlternateStreet);
		});

		OrderDto orderDto = modelMapper.map(order, OrderDto.class);

		Assertions.assertEquals(order.getCustomer().getName().getFirstName(), orderDto.getCustomerFirstName());
		Assertions.assertEquals(order.getCustomer().getName().getLastName(), orderDto.getCustomerLastName());
		Assertions.assertEquals(order.getBillingAddress().getStreet(), orderDto.getAddressStreet());
		Assertions.assertEquals(order.getBillingAddress().getCity(), orderDto.getAddressCity());
		Assertions.assertEquals(order.getCustomer().getName().getFirstName(), orderDto.getCustomerAlternateFirstName());
		Assertions.assertEquals(order.getBillingAddress().getStreet(), orderDto.getAddressAlternateStreet());
	}

	@Test
	public void modelMapper_MatchingStrategy() {
		Order order = new Order(new Customer( new Name("Jose Alberto","Martinez")), new Address("Main Street", "CDMX"));
		ModelMapper modelMapper = new ModelMapper();

		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);

		OrderDto orderDto = modelMapper.map(order, OrderDto.class);

		Assertions.assertEquals(order.getCustomer().getName().getFirstName(), orderDto.getCustomerFirstName());
		Assertions.assertEquals(order.getCustomer().getName().getLastName(), orderDto.getCustomerLastName());
		Assertions.assertEquals(order.getBillingAddress().getStreet(), orderDto.getAddressStreet());
		Assertions.assertEquals(order.getBillingAddress().getCity(), orderDto.getAddressCity());
		Assertions.assertEquals(order.getCustomer().getName().getFirstName(), orderDto.getCustomerAlternateFirstName());
		Assertions.assertEquals(order.getBillingAddress().getStreet(), orderDto.getAddressAlternateStreet());
	}

	@Test
	public void modelMapper_throwValidateException(){
		Order order = new Order(new Customer( new Name("Jose Alberto", "Martinez")), new Address("Main Street","CDMX"));
		ModelMapper modelMapper = new ModelMapper();

		modelMapper.createTypeMap(Order.class, OrderDto.class);
		try {
			modelMapper.validate();
		} catch (RuntimeException ex){
			Assertions.assertEquals(org.modelmapper.ValidationException.class, ex.getClass());
		}

	}

	@Test
	public void modelMapper_NotThrowValidateExceptionByMatchingStrategies(){
		Order order = new Order(new Customer( new Name("Jose Alberto", "Martinez")), new Address("Main Street","CDMX"));
		ModelMapper modelMapper = new ModelMapper();

		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
		modelMapper.validate();
		OrderDto orderDto = modelMapper.map(order, OrderDto.class);

		Assertions.assertEquals(order.getCustomer().getName().getFirstName(), orderDto.getCustomerFirstName());
		//...
	}

	@Test
	public void modelMapper_NotThrowValidateExceptionByAddMappings(){
		Order order = new Order(new Customer( new Name("Jose Alberto", "Martinez")), new Address("Main Street","CDMX"));
		ModelMapper modelMapper = new ModelMapper();

		modelMapper.typeMap(Order.class, OrderDto.class).addMappings(mapper -> {
			mapper.map( src -> order.getCustomer().getName().getFirstName(), OrderDto::setCustomerAlternateFirstName);
			mapper.map( src -> order.getBillingAddress().getStreet(), OrderDto::setAddressAlternateStreet);
		});

		modelMapper.validate();
		OrderDto orderDto = modelMapper.map(order, OrderDto.class);

		Assertions.assertEquals(order.getCustomer().getName().getFirstName(), orderDto.getCustomerFirstName());
		//...
	}
}
