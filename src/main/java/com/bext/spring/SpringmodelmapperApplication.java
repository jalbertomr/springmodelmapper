package com.bext.spring;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.TypeMap;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.MatchingStrategy;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.bext.model.Address;
import com.bext.model.Customer;
import com.bext.model.Name;
import com.bext.model.Order;
import com.bext.model.OrderDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
public class SpringmodelmapperApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(SpringmodelmapperApplication.class, args).close();
	}

	@Override
	public void run(String... args) throws Exception {
		Order order = new Order(new Customer( new Name("Jose Alberto", "Martinez")), new Address("Main Street","CDMX"));
		ModelMapper modelMapper = new ModelMapper();

		/*modelMapper.typeMap(Order.class, OrderDto.class).addMappings(mapper -> {
			mapper.map( src -> order.getCustomer().getName().getFirstName(), OrderDto::setCustomerAlternateFirstName);
			mapper.map( src -> order.getBillingAddress().getStreet(), OrderDto::setAddressAlternateStreet);
		});*/

		PropertyMap<Order, OrderDto> orderCustomerMap = new PropertyMap<Order, OrderDto>() {
			@Override
			protected void configure() {
				map().setCustomerAlternateFirstName( source.getCustomer().getName().getFirstName());
				map().setAddressAlternateStreet( source.getBillingAddress().getStreet());
				//map(source.getBillingAddress().getStreet(), destination.addressAlternateStreet);
			}
		};

		modelMapper.addMappings(orderCustomerMap);

		OrderDto orderDto = modelMapper.map(order, OrderDto.class);
		log.info("orderDto mapped: {}",orderDto);
	}

}
