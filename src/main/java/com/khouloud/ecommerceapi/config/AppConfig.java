package com.khouloud.ecommerceapi.config;

import com.khouloud.ecommerceapi.dto.OrderItemDtoResponse;
import com.khouloud.ecommerceapi.dto.ProductDtoRequest;
import com.khouloud.ecommerceapi.model.OrderItem;
import com.khouloud.ecommerceapi.model.Product;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setAmbiguityIgnored(true);
        modelMapper.typeMap(OrderItem.class, OrderItemDtoResponse.class)
                .addMappings(m -> m.map(src -> src.getOrderItemId(), OrderItemDtoResponse::setOrderItemId))
                .addMappings(m -> m.map(src -> src.getProduct(), OrderItemDtoResponse::setProductDtoResponse))
                .addMappings(m -> m.map(src -> src.getOrder().getOrderId(), OrderItemDtoResponse::setOrderId));

        modelMapper.typeMap(ProductDtoRequest.class, Product.class)
                .addMappings(m -> m.map(src -> null, (dest, val) -> dest.setProductId(null)));

        return modelMapper;
    }

}
