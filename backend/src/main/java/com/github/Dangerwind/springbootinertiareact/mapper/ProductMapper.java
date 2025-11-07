package com.github.Dangerwind.springbootinertiareact.mapper;

import com.github.Dangerwind.springbootinertiareact.dto.ProductDTO;
import com.github.Dangerwind.springbootinertiareact.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE
)
public abstract class ProductMapper {

    public abstract Product toModel(ProductDTO product);
    public abstract ProductDTO toDTO(Product product);
}
