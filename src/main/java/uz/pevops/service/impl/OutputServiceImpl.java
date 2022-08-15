package uz.pevops.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.pevops.domain.Output;
import uz.pevops.repository.OutputRepository;
import uz.pevops.service.OutputService;
import uz.pevops.service.dto.OutputDTO;
import uz.pevops.service.mapper.OutputMapper;

/**
 * Service Implementation for managing {@link Output}.
 */
@Service
@Transactional
public class OutputServiceImpl implements OutputService {

    private final Logger log = LoggerFactory.getLogger(OutputServiceImpl.class);

    private final OutputRepository outputRepository;

    private final OutputMapper outputMapper;

    public OutputServiceImpl(OutputRepository outputRepository, OutputMapper outputMapper) {
        this.outputRepository = outputRepository;
        this.outputMapper = outputMapper;
    }

    @Override
    public OutputDTO save(OutputDTO outputDTO) {
        log.debug("Request to save Output : {}", outputDTO);
        Output output = outputMapper.toEntity(outputDTO);
        output = outputRepository.save(output);
        return outputMapper.toDto(output);
    }

    @Override
    public OutputDTO update(OutputDTO outputDTO) {
        log.debug("Request to save Output : {}", outputDTO);
        Output output = outputMapper.toEntity(outputDTO);
        output = outputRepository.save(output);
        return outputMapper.toDto(output);
    }

    @Override
    public Optional<OutputDTO> partialUpdate(OutputDTO outputDTO) {
        log.debug("Request to partially update Output : {}", outputDTO);

        return outputRepository
            .findById(outputDTO.getId())
            .map(existingOutput -> {
                outputMapper.partialUpdate(existingOutput, outputDTO);

                return existingOutput;
            })
            .map(outputRepository::save)
            .map(outputMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OutputDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Outputs");
        return outputRepository.findAll(pageable).map(outputMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OutputDTO> findOne(Long id) {
        log.debug("Request to get Output : {}", id);
        return outputRepository.findById(id).map(outputMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Output : {}", id);
        outputRepository.deleteById(id);
    }
}
