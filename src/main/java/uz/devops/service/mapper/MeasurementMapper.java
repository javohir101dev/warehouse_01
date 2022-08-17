package uz.devops.service.mapper;

import org.mapstruct.*;
import uz.devops.domain.Measurement;
import uz.devops.service.dto.MeasurementDTO;

/**
 * Mapper for the entity {@link Measurement} and its DTO {@link MeasurementDTO}.
 */
@Mapper(componentModel = "spring")
public interface MeasurementMapper extends EntityMapper<MeasurementDTO, Measurement> {}
