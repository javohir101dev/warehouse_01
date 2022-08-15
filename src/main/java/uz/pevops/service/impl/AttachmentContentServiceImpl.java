package uz.pevops.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.pevops.domain.AttachmentContent;
import uz.pevops.repository.AttachmentContentRepository;
import uz.pevops.service.AttachmentContentService;
import uz.pevops.service.dto.AttachmentContentDTO;
import uz.pevops.service.mapper.AttachmentContentMapper;

/**
 * Service Implementation for managing {@link AttachmentContent}.
 */
@Service
@Transactional
public class AttachmentContentServiceImpl implements AttachmentContentService {

    private final Logger log = LoggerFactory.getLogger(AttachmentContentServiceImpl.class);

    private final AttachmentContentRepository attachmentContentRepository;

    private final AttachmentContentMapper attachmentContentMapper;

    public AttachmentContentServiceImpl(
        AttachmentContentRepository attachmentContentRepository,
        AttachmentContentMapper attachmentContentMapper
    ) {
        this.attachmentContentRepository = attachmentContentRepository;
        this.attachmentContentMapper = attachmentContentMapper;
    }

    @Override
    public AttachmentContentDTO save(AttachmentContentDTO attachmentContentDTO) {
        log.debug("Request to save AttachmentContent : {}", attachmentContentDTO);
        AttachmentContent attachmentContent = attachmentContentMapper.toEntity(attachmentContentDTO);
        attachmentContent = attachmentContentRepository.save(attachmentContent);
        return attachmentContentMapper.toDto(attachmentContent);
    }

    @Override
    public AttachmentContentDTO update(AttachmentContentDTO attachmentContentDTO) {
        log.debug("Request to save AttachmentContent : {}", attachmentContentDTO);
        AttachmentContent attachmentContent = attachmentContentMapper.toEntity(attachmentContentDTO);
        attachmentContent = attachmentContentRepository.save(attachmentContent);
        return attachmentContentMapper.toDto(attachmentContent);
    }

    @Override
    public Optional<AttachmentContentDTO> partialUpdate(AttachmentContentDTO attachmentContentDTO) {
        log.debug("Request to partially update AttachmentContent : {}", attachmentContentDTO);

        return attachmentContentRepository
            .findById(attachmentContentDTO.getId())
            .map(existingAttachmentContent -> {
                attachmentContentMapper.partialUpdate(existingAttachmentContent, attachmentContentDTO);

                return existingAttachmentContent;
            })
            .map(attachmentContentRepository::save)
            .map(attachmentContentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AttachmentContentDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AttachmentContents");
        return attachmentContentRepository.findAll(pageable).map(attachmentContentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AttachmentContentDTO> findOne(Long id) {
        log.debug("Request to get AttachmentContent : {}", id);
        return attachmentContentRepository.findById(id).map(attachmentContentMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete AttachmentContent : {}", id);
        attachmentContentRepository.deleteById(id);
    }
}
