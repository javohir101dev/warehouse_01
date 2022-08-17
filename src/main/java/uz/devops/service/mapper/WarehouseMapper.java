package uz.devops.service.mapper;

import org.mapstruct.*;
import uz.devops.domain.Warehouse;
import uz.devops.service.dto.WarehouseDTO;

/**
 * Mapper for the entity {@link Warehouse} and its DTO {@link WarehouseDTO}.
 */
@Mapper(componentModel = "spring")
public interface WarehouseMapper extends EntityMapper<WarehouseDTO, Warehouse> {}
