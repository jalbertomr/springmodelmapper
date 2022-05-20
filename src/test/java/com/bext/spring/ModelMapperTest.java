package com.bext.spring;

import com.bext.model.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import org.modelmapper.PropertyMap;
import org.modelmapper.ValidationException;
import org.modelmapper.convention.MatchingStrategies;
@Slf4j
public class ModelMapperTest {

    @Test
	public void modelMapper_map() {
    	Order order = new Order(new Customer( new Name("Jose Alberto","Martinez")), new Address("Main Street", "CDMX"));
    	ModelMapper modelMapper = new ModelMapper();
    	OrderDto orderDto = modelMapper.map(order, OrderDto.class);
    	
		Assertions.assertEquals(order.getCustomer().getName().getFirstName(), orderDto.getCustomerFirstName());
		Assertions.assertEquals(order.getCustomer().getName().getLastName(), orderDto.getCustomerLastName());
		Assertions.assertEquals(order.getBillingAddress().getStreet(), orderDto.getBillingAddressStreet());
		Assertions.assertEquals(order.getBillingAddress().getCity(), orderDto.getBillingAddressCity());
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
		Assertions.assertEquals(order.getBillingAddress().getStreet(), orderDto.getBillingAddressStreet());
		Assertions.assertEquals(order.getBillingAddress().getCity(), orderDto.getBillingAddressCity());
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
		Assertions.assertEquals(order.getBillingAddress().getStreet(), orderDto.getBillingAddressStreet());
		Assertions.assertEquals(order.getBillingAddress().getCity(), orderDto.getBillingAddressCity());
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

	@Test
	public void modelMapper_mapProjection(){
		OrderInfo orderInfo = new OrderInfo(new Customer( new Name("Jose Alberto", "")), new Address("Main Street",""));
		ModelMapper modelMapper = new ModelMapper();

		Order order = modelMapper.map(orderInfo, Order.class);

		log.info("orderInfo {}", orderInfo);
		log.info("order {}", order);
		Assertions.assertEquals(orderInfo.getCustomer().getName().getFirstName(), order.getCustomer().getName().getFirstName());
		Assertions.assertEquals(orderInfo.getBillingAddress().getStreet(), order.getBillingAddress().getStreet());
	}

	@Test
	public void modelMapper_mapDtoProjection(){
		OrderDto orderDto = new OrderDto( "Jose Alberto","Martinez", "Main Street", "CDMX","Beto","otherStreet");
		ModelMapper modelMapper = new ModelMapper();

		PropertyMap<OrderDto, Order> orderDtoOrderMap = new PropertyMap<OrderDto, Order>(){
			@Override
			protected void configure() {
				map().setBillingAddress( new Address(source.getBillingAddressStreet(),""));
			}
		};

		modelMapper.addMappings(orderDtoOrderMap);
		Order order = modelMapper.map(orderDto, Order.class);

		log.info("orderInfo {}", orderDto);
		log.info("order {}", order);
		Assertions.assertEquals(orderDto.getCustomerFirstName(), order.getCustomer().getName().getFirstName());
		Assertions.assertEquals(orderDto.getCustomerLastName(), order.getCustomer().getName().getLastName());
		Assertions.assertEquals(orderDto.getBillingAddressStreet(), order.getBillingAddress().getStreet());
		Assertions.assertEquals(orderDto.getBillingAddressCity(), order.getBillingAddress().getCity());
	}
}
