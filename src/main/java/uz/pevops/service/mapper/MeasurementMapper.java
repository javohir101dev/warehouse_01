package uz.pevops.service.mapper;

import org.mapstruct.*;
import uz.pevops.domain.Measurement;
import uz.pevops.service.dto.MeasurementDTO;

/**
 * Mapper for the entity {@link Measurement} and its DTO {@link MeasurementDTO}.
 */
@Mapper(componentModel = "spring")
public interface MeasurementMapper extends EntityMapper<MeasurementDTO, Measurement> {}
