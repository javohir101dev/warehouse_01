package uz.pevops.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.pevops.domain.Input;
import uz.pevops.repository.InputRepository;
import uz.pevops.service.InputService;
import uz.pevops.service.dto.InputDTO;
import uz.pevops.service.mapper.InputMapper;

/**
 * Service Implementation for managing {@link Input}.
 */
@Service
@Transactional
public class InputServiceImpl implements InputService {

    private final Logger log = LoggerFactory.getLogger(InputServiceImpl.class);

    private final InputRepository inputRepository;

    private final InputMapper inputMapper;

    public InputServiceImpl(InputRepository inputRepository, InputMapper inputMapper) {
        this.inputRepository = inputRepository;
        this.inputMapper = inputMapper;
    }

    @Override
    public InputDTO save(InputDTO inputDTO) {
        log.debug("Request to save Input : {}", inputDTO);
        Input input = inputMapper.toEntity(inputDTO);
        input = inputRepository.save(input);
        return inputMapper.toDto(input);
    }

    @Override
    public InputDTO update(InputDTO inputDTO) {
        log.debug("Request to save Input : {}", inputDTO);
        Input input = inputMapper.toEntity(inputDTO);
        input = inputRepository.save(input);
        return inputMapper.toDto(input);
    }

    @Override
    public Optional<InputDTO> partialUpdate(InputDTO inputDTO) {
        log.debug("Request to partially update Input : {}", inputDTO);

        return inputRepository
            .findById(inputDTO.getId())
            .map(existingInput -> {
                inputMapper.partialUpdate(existingInput, inputDTO);

                return existingInput;
            })
            .map(inputRepository::save)
            .map(inputMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InputDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Inputs");
        return inputRepository.findAll(pageable).map(inputMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<InputDTO> findOne(Long id) {
        log.debug("Request to get Input : {}", id);
        return inputRepository.findById(id).map(inputMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Input : {}", id);
        inputRepository.deleteById(id);
    }
}
