package uz.pevops.service.mapper;

import org.mapstruct.*;
import uz.pevops.domain.Warehouse;
import uz.pevops.service.dto.WarehouseDTO;

/**
 * Mapper for the entity {@link Warehouse} and its DTO {@link WarehouseDTO}.
 */
@Mapper(componentModel = "spring")
public interface WarehouseMapper extends EntityMapper<WarehouseDTO, Warehouse> {}
