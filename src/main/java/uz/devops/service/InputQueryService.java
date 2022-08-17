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
import uz.devops.domain.Input;
import uz.devops.repository.InputRepository;
import uz.devops.service.criteria.InputCriteria;
import uz.devops.service.dto.InputDTO;
import uz.devops.service.mapper.InputMapper;

/**
 * Service for executing complex queries for {@link Input} entities in the database.
 * The main input is a {@link InputCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link InputDTO} or a {@link Page} of {@link InputDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class InputQueryService extends QueryService<Input> {

    private final Logger log = LoggerFactory.getLogger(InputQueryService.class);

    private final InputRepository inputRepository;

    private final InputMapper inputMapper;

    public InputQueryService(InputRepository inputRepository, InputMapper inputMapper) {
        this.inputRepository = inputRepository;
        this.inputMapper = inputMapper;
    }

    /**
     * Return a {@link List} of {@link InputDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<InputDTO> findByCriteria(InputCriteria criteria) {
        log.debug("find by criteria : {}", criteria.toString().replaceAll("[\n\r\t]", "_"));
        final Specification<Input> specification = createSpecification(criteria);
        return inputMapper.toDto(inputRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link InputDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<InputDTO> findByCriteria(InputCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria.toString().replaceAll("[\n\r\t]", "_"), page);
        final Specification<Input> specification = createSpecification(criteria);
        return inputRepository.findAll(specification, page).map(inputMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(InputCriteria criteria) {
        log.debug("count by criteria : {}", criteria.toString().replaceAll("[\n\r\t]", "_"));
        final Specification<Input> specification = createSpecification(criteria);
        return inputRepository.count(specification);
    }

    /**
     * Function to convert {@link InputCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Input> createSpecification(InputCriteria criteria) {
        Specification<Input> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Input_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Input_.name));
            }
            if (criteria.getDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDate(), Input_.date));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCode(), Input_.code));
            }
            if (criteria.getWarehouseId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getWarehouseId(), root -> root.join(Input_.warehouse, JoinType.LEFT).get(Warehouse_.id))
                    );
            }
            if (criteria.getSupplierId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getSupplierId(), root -> root.join(Input_.supplier, JoinType.LEFT).get(Users_.id))
                    );
            }
        }
        return specification;
    }
}
