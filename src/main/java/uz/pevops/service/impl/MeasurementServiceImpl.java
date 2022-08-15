package uz.pevops.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.pevops.domain.Measurement;
import uz.pevops.repository.MeasurementRepository;
import uz.pevops.service.MeasurementService;
import uz.pevops.service.dto.MeasurementDTO;
import uz.pevops.service.mapper.MeasurementMapper;

/**
 * Service Implementation for managing {@link Measurement}.
 */
@Service
@Transactional
public class MeasurementServiceImpl implements MeasurementService {

    private final Logger log = LoggerFactory.getLogger(MeasurementServiceImpl.class);

    private final MeasurementRepository measurementRepository;

    private final MeasurementMapper measurementMapper;

    public MeasurementServiceImpl(MeasurementRepository measurementRepository, MeasurementMapper measurementMapper) {
        this.measurementRepository = measurementRepository;
        this.measurementMapper = measurementMapper;
    }

    @Override
    public MeasurementDTO save(MeasurementDTO measurementDTO) {
        log.debug("Request to save Measurement : {}", measurementDTO);
        Measurement measurement = measurementMapper.toEntity(measurementDTO);
        measurement = measurementRepository.save(measurement);
        return measurementMapper.toDto(measurement);
    }

    @Override
    public MeasurementDTO update(MeasurementDTO measurementDTO) {
        log.debug("Request to save Measurement : {}", measurementDTO);
        Measurement measurement = measurementMapper.toEntity(measurementDTO);
        measurement = measurementRepository.save(measurement);
        return measurementMapper.toDto(measurement);
    }

    @Override
    public Optional<MeasurementDTO> partialUpdate(MeasurementDTO measurementDTO) {
        log.debug("Request to partially update Measurement : {}", measurementDTO);

        return measurementRepository
            .findById(measurementDTO.getId())
            .map(existingMeasurement -> {
                measurementMapper.partialUpdate(existingMeasurement, measurementDTO);

                return existingMeasurement;
            })
            .map(measurementRepository::save)
            .map(measurementMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MeasurementDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Measurements");
        return measurementRepository.findAll(pageable).map(measurementMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MeasurementDTO> findOne(Long id) {
        log.debug("Request to get Measurement : {}", id);
        return measurementRepository.findById(id).map(measurementMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Measurement : {}", id);
        measurementRepository.deleteById(id);
    }
}
