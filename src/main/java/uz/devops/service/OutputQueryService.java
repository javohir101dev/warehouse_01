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
import uz.devops.domain.Output;
import uz.devops.repository.OutputRepository;
import uz.devops.service.criteria.OutputCriteria;
import uz.devops.service.dto.OutputDTO;
import uz.devops.service.mapper.OutputMapper;

/**
 * Service for executing complex queries for {@link Output} entities in the database.
 * The main input is a {@link OutputCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link OutputDTO} or a {@link Page} of {@link OutputDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class OutputQueryService extends QueryService<Output> {

    private final Logger log = LoggerFactory.getLogger(OutputQueryService.class);

    private final OutputRepository outputRepository;

    private final OutputMapper outputMapper;

    public OutputQueryService(OutputRepository outputRepository, OutputMapper outputMapper) {
        this.outputRepository = outputRepository;
        this.outputMapper = outputMapper;
    }

    /**
     * Return a {@link List} of {@link OutputDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<OutputDTO> findByCriteria(OutputCriteria criteria) {
        log.debug("find by criteria : {}", criteria.toString().replaceAll("[\n\r\t]", "_"));
        final Specification<Output> specification = createSpecification(criteria);
        return outputMapper.toDto(outputRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link OutputDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<OutputDTO> findByCriteria(OutputCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria.toString().replaceAll("[\n\r\t]", "_"), page);
        final Specification<Output> specification = createSpecification(criteria);
        return outputRepository.findAll(specification, page).map(outputMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(OutputCriteria criteria) {
        log.debug("count by criteria : {}", criteria.toString().replaceAll("[\n\r\t]", "_"));
        final Specification<Output> specification = createSpecification(criteria);
        return outputRepository.count(specification);
    }

    /**
     * Function to convert {@link OutputCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Output> createSpecification(OutputCriteria criteria) {
        Specification<Output> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Output_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Output_.name));
            }
            if (criteria.getDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDate(), Output_.date));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCode(), Output_.code));
            }
            if (criteria.getWarehouseId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getWarehouseId(),
                            root -> root.join(Output_.warehouse, JoinType.LEFT).get(Warehouse_.id)
                        )
                    );
            }
            if (criteria.getClientId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getClientId(), root -> root.join(Output_.client, JoinType.LEFT).get(Users_.id))
                    );
            }
        }
        return specification;
    }
}
