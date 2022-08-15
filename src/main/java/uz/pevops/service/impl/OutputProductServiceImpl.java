package uz.pevops.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.pevops.domain.OutputProduct;
import uz.pevops.repository.OutputProductRepository;
import uz.pevops.service.OutputProductService;
import uz.pevops.service.dto.OutputProductDTO;
import uz.pevops.service.mapper.OutputProductMapper;

/**
 * Service Implementation for managing {@link OutputProduct}.
 */
@Service
@Transactional
public class OutputProductServiceImpl implements OutputProductService {

    private final Logger log = LoggerFactory.getLogger(OutputProductServiceImpl.class);

    private final OutputProductRepository outputProductRepository;

    private final OutputProductMapper outputProductMapper;

    public OutputProductServiceImpl(OutputProductRepository outputProductRepository, OutputProductMapper outputProductMapper) {
        this.outputProductRepository = outputProductRepository;
        this.outputProductMapper = outputProductMapper;
    }

    @Override
    public OutputProductDTO save(OutputProductDTO outputProductDTO) {
        log.debug("Request to save OutputProduct : {}", outputProductDTO);
        OutputProduct outputProduct = outputProductMapper.toEntity(outputProductDTO);
        outputProduct = outputProductRepository.save(outputProduct);
        return outputProductMapper.toDto(outputProduct);
    }

    @Override
    public OutputProductDTO update(OutputProductDTO outputProductDTO) {
        log.debug("Request to save OutputProduct : {}", outputProductDTO);
        OutputProduct outputProduct = outputProductMapper.toEntity(outputProductDTO);
        outputProduct = outputProductRepository.save(outputProduct);
        return outputProductMapper.toDto(outputProduct);
    }

    @Override
    public Optional<OutputProductDTO> partialUpdate(OutputProductDTO outputProductDTO) {
        log.debug("Request to partially update OutputProduct : {}", outputProductDTO);

        return outputProductRepository
            .findById(outputProductDTO.getId())
            .map(existingOutputProduct -> {
                outputProductMapper.partialUpdate(existingOutputProduct, outputProductDTO);

                return existingOutputProduct;
            })
            .map(outputProductRepository::save)
            .map(outputProductMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OutputProductDTO> findAll(Pageable pageable) {
        log.debug("Request to get all OutputProducts");
        return outputProductRepository.findAll(pageable).map(outputProductMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OutputProductDTO> findOne(Long id) {
        log.debug("Request to get OutputProduct : {}", id);
        return outputProductRepository.findById(id).map(outputProductMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete OutputProduct : {}", id);
        outputProductRepository.deleteById(id);
    }
}
