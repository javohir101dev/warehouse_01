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
import uz.devops.domain.AttachmentContent;
import uz.devops.repository.AttachmentContentRepository;
import uz.devops.service.criteria.AttachmentContentCriteria;
import uz.devops.service.dto.AttachmentContentDTO;
import uz.devops.service.mapper.AttachmentContentMapper;

/**
 * Service for executing complex queries for {@link AttachmentContent} entities in the database.
 * The main input is a {@link AttachmentContentCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AttachmentContentDTO} or a {@link Page} of {@link AttachmentContentDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AttachmentContentQueryService extends QueryService<AttachmentContent> {

    private final Logger log = LoggerFactory.getLogger(AttachmentContentQueryService.class);

    private final AttachmentContentRepository attachmentContentRepository;

    private final AttachmentContentMapper attachmentContentMapper;

    public AttachmentContentQueryService(
        AttachmentContentRepository attachmentContentRepository,
        AttachmentContentMapper attachmentContentMapper
    ) {
        this.attachmentContentRepository = attachmentContentRepository;
        this.attachmentContentMapper = attachmentContentMapper;
    }

    /**
     * Return a {@link List} of {@link AttachmentContentDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AttachmentContentDTO> findByCriteria(AttachmentContentCriteria criteria) {
        log.debug("find by criteria : {}", criteria.toString().replaceAll("[\n\r\t]", "_"));
        final Specification<AttachmentContent> specification = createSpecification(criteria);
        return attachmentContentMapper.toDto(attachmentContentRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link AttachmentContentDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AttachmentContentDTO> findByCriteria(AttachmentContentCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria.toString().replaceAll("[\n\r\t]", "_"), page);
        final Specification<AttachmentContent> specification = createSpecification(criteria);
        return attachmentContentRepository.findAll(specification, page).map(attachmentContentMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AttachmentContentCriteria criteria) {
        log.debug("count by criteria : {}", criteria.toString().replaceAll("[\n\r\t]", "_"));
        final Specification<AttachmentContent> specification = createSpecification(criteria);
        return attachmentContentRepository.count(specification);
    }

    /**
     * Function to convert {@link AttachmentContentCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AttachmentContent> createSpecification(AttachmentContentCriteria criteria) {
        Specification<AttachmentContent> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AttachmentContent_.id));
            }
            if (criteria.getAttachmentId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getAttachmentId(),
                            root -> root.join(AttachmentContent_.attachment, JoinType.LEFT).get(Attachment_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
