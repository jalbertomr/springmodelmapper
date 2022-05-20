#### ModelMapper Flattening and Projection

![model Entity & Dto](assets/images/ModelMapper_map.PNG)

###### ModelMapper.map Flattening

#### Flattening 

Flatenning translates information from multiple object to a single object.

The mapping from many entities sources are done automatically to the destination, in this case a Dto.

    destination instance = modelMapper.map( source instance, destination class)


    ModelMapper mapperModel = new ModelMapper();
    OrderDto orderDto = modelMapper.map(order, OrderDto.class);

The JUnit test are as follow 

  [@Test](src/test/java/com.bext.spring/ModelMapperTest.java)
    

###### ModelMapper.typeMap(source class, destination class).addMappings(...)

![model Entity & Dto](assets/images/ModelMapper_map_addMappings.PNG)

To add additional mapping to another fields use

		modelMapper.typeMap(Order.class, OrderDto.class).addMappings(mapper -> {
			mapper.map( src -> order.getCustomer().getName().getFirstName(), OrderDto::setCustomerAlternateFirstName);
			mapper.map( src -> order.getBillingAddress().getStreet(), OrderDto::setAddressAlternateStreet);
		});
		
Or alternatively use MatchingStrategy in single line

	    modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);		
	    
###### Validating Matching

To check if all the fields have a match use modelMatcher.validate(). if an unmatched exist the org.modelmapper.ValidationException.class
is thrown.

#### Projection

Projection translates information from a single object to multiple objects.

In Order to allow the modelMapper library work prety fine, the names of the fields must be the same.

![model Entity & Dto](assets/images/ModelMappernames.PNG)
