package uz.devops.service;

import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;
import uz.devops.domain.*; // for static metamodels
import uz.devops.domain.Attachment;
import uz.devops.repository.AttachmentRepository;
import uz.devops.service.criteria.AttachmentCriteria;
import uz.devops.service.dto.AttachmentDTO;
import uz.devops.service.mapper.AttachmentMapper;

/**
 * Service for executing complex queries for {@link Attachment} entities in the database.
 * The main input is a {@link AttachmentCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AttachmentDTO} or a {@link Page} of {@link AttachmentDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AttachmentQueryService extends QueryService<Attachment> {

    private final Logger log = LoggerFactory.getLogger(AttachmentQueryService.class);

    private final AttachmentRepository attachmentRepository;

    private final AttachmentMapper attachmentMapper;

    public AttachmentQueryService(AttachmentRepository attachmentRepository, AttachmentMapper attachmentMapper) {
        this.attachmentRepository = attachmentRepository;
        this.attachmentMapper = attachmentMapper;
    }

    /**
     * Return a {@link List} of {@link AttachmentDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AttachmentDTO> findByCriteria(AttachmentCriteria criteria) {
        log.debug("find by criteria : {}", criteria.toString().replaceAll("[\n\r\t]", "_"));
        final Specification<Attachment> specification = createSpecification(criteria);
        return attachmentMapper.toDto(attachmentRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link AttachmentDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AttachmentDTO> findByCriteria(AttachmentCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria.toString().replaceAll("[\n\r\t]", "_"), page);
        final Specification<Attachment> specification = createSpecification(criteria);
        return attachmentRepository.findAll(specification, page).map(attachmentMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AttachmentCriteria criteria) {
        log.debug("count by criteria : {}", criteria.toString().replaceAll("[\n\r\t]", "_"));
        final Specification<Attachment> specification = createSpecification(criteria);
        return attachmentRepository.count(specification);
    }

    /**
     * Function to convert {@link AttachmentCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Attachment> createSpecification(AttachmentCriteria criteria) {
        Specification<Attachment> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Attachment_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Attachment_.name));
            }
            if (criteria.getSizeAttachment() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSizeAttachment(), Attachment_.sizeAttachment));
            }
            if (criteria.getContentType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getContentType(), Attachment_.contentType));
            }
        }
        return specification;
    }
}
