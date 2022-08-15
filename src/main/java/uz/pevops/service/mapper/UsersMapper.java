package uz.pevops.service.mapper;

import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;
import uz.pevops.domain.Users;
import uz.pevops.domain.Warehouse;
import uz.pevops.service.dto.UsersDTO;
import uz.pevops.service.dto.WarehouseDTO;

/**
 * Mapper for the entity {@link Users} and its DTO {@link UsersDTO}.
 */
@Mapper(componentModel = "spring")
public interface UsersMapper extends EntityMapper<UsersDTO, Users> {
    @Mapping(target = "warehouses", source = "warehouses", qualifiedByName = "warehouseNameSet")
    UsersDTO toDto(Users s);

    @Mapping(target = "removeWarehouse", ignore = true)
    Users toEntity(UsersDTO usersDTO);

    @Named("warehouseName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    WarehouseDTO toDtoWarehouseName(Warehouse warehouse);

    @Named("warehouseNameSet")
    default Set<WarehouseDTO> toDtoWarehouseNameSet(Set<Warehouse> warehouse) {
        return warehouse.stream().map(this::toDtoWarehouseName).collect(Collectors.toSet());
    }
}
