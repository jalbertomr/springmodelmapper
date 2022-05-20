#### ModelMapper

![model Entity & Dto](assets/images/ModelMapper_map.PNG)

###### ModelMapper.map

The mapping from many entities sources are done automatically to the destination, in this case a Dto.

    destination instance = modelMapper.map( source instance, destination class)


    ModelMapper mapperModel = new ModelMapper();
    OrderDto orderDto = modelMapper.map(order, OrderDto.class);

The JUnit test are as follow 

  [@Test](src/test/java/com.bext.spring/ModelMapperTest.java)
    

###### ModelMapper.map(source, destine).addMappings(...)

![model Entity & Dto](assets/images/ModelMapper_map_addMappings.PNG)
