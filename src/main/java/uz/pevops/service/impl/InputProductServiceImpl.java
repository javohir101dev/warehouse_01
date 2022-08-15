package uz.pevops.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.pevops.domain.InputProduct;
import uz.pevops.repository.InputProductRepository;
import uz.pevops.service.InputProductService;
import uz.pevops.service.dto.InputProductDTO;
import uz.pevops.service.mapper.InputProductMapper;

/**
 * Service Implementation for managing {@link InputProduct}.
 */
@Service
@Transactional
public class InputProductServiceImpl implements InputProductService {

    private final Logger log = LoggerFactory.getLogger(InputProductServiceImpl.class);

    private final InputProductRepository inputProductRepository;

    private final InputProductMapper inputProductMapper;

    public InputProductServiceImpl(InputProductRepository inputProductRepository, InputProductMapper inputProductMapper) {
        this.inputProductRepository = inputProductRepository;
        this.inputProductMapper = inputProductMapper;
    }

    @Override
    public InputProductDTO save(InputProductDTO inputProductDTO) {
        log.debug("Request to save InputProduct : {}", inputProductDTO);
        InputProduct inputProduct = inputProductMapper.toEntity(inputProductDTO);
        inputProduct = inputProductRepository.save(inputProduct);
        return inputProductMapper.toDto(inputProduct);
    }

    @Override
    public InputProductDTO update(InputProductDTO inputProductDTO) {
        log.debug("Request to save InputProduct : {}", inputProductDTO);
        InputProduct inputProduct = inputProductMapper.toEntity(inputProductDTO);
        inputProduct = inputProductRepository.save(inputProduct);
        return inputProductMapper.toDto(inputProduct);
    }

    @Override
    public Optional<InputProductDTO> partialUpdate(InputProductDTO inputProductDTO) {
        log.debug("Request to partially update InputProduct : {}", inputProductDTO);

        return inputProductRepository
            .findById(inputProductDTO.getId())
            .map(existingInputProduct -> {
                inputProductMapper.partialUpdate(existingInputProduct, inputProductDTO);

                return existingInputProduct;
            })
            .map(inputProductRepository::save)
            .map(inputProductMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InputProductDTO> findAll(Pageable pageable) {
        log.debug("Request to get all InputProducts");
        return inputProductRepository.findAll(pageable).map(inputProductMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<InputProductDTO> findOne(Long id) {
        log.debug("Request to get InputProduct : {}", id);
        return inputProductRepository.findById(id).map(inputProductMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete InputProduct : {}", id);
        inputProductRepository.deleteById(id);
    }
}
