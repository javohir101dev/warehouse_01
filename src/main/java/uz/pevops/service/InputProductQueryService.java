package uz.pevops.service;

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
import uz.pevops.domain.*; // for static metamodels
import uz.pevops.domain.InputProduct;
import uz.pevops.repository.InputProductRepository;
import uz.pevops.service.criteria.InputProductCriteria;
import uz.pevops.service.dto.InputProductDTO;
import uz.pevops.service.mapper.InputProductMapper;

/**
 * Service for executing complex queries for {@link InputProduct} entities in the database.
 * The main input is a {@link InputProductCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link InputProductDTO} or a {@link Page} of {@link InputProductDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class InputProductQueryService extends QueryService<InputProduct> {

    private final Logger log = LoggerFactory.getLogger(InputProductQueryService.class);

    private final InputProductRepository inputProductRepository;

    private final InputProductMapper inputProductMapper;

    public InputProductQueryService(InputProductRepository inputProductRepository, InputProductMapper inputProductMapper) {
        this.inputProductRepository = inputProductRepository;
        this.inputProductMapper = inputProductMapper;
    }

    /**
     * Return a {@link List} of {@link InputProductDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<InputProductDTO> findByCriteria(InputProductCriteria criteria) {
        log.debug("find by criteria : {}", criteria.toString().replaceAll("[\n\r\t]", "_"));
        final Specification<InputProduct> specification = createSpecification(criteria);
        return inputProductMapper.toDto(inputProductRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link InputProductDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<InputProductDTO> findByCriteria(InputProductCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria.toString().replaceAll("[\n\r\t]", "_"), page);
        final Specification<InputProduct> specification = createSpecification(criteria);
        return inputProductRepository.findAll(specification, page).map(inputProductMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(InputProductCriteria criteria) {
        log.debug("count by criteria : {}", criteria.toString().replaceAll("[\n\r\t]", "_"));
        final Specification<InputProduct> specification = createSpecification(criteria);
        return inputProductRepository.count(specification);
    }

    /**
     * Function to convert {@link InputProductCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<InputProduct> createSpecification(InputProductCriteria criteria) {
        Specification<InputProduct> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), InputProduct_.id));
            }
            if (criteria.getAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAmount(), InputProduct_.amount));
            }
            if (criteria.getPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrice(), InputProduct_.price));
            }
            if (criteria.getExpireDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getExpireDate(), InputProduct_.expireDate));
            }
            if (criteria.getProductId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getProductId(),
                            root -> root.join(InputProduct_.product, JoinType.LEFT).get(Product_.id)
                        )
                    );
            }
            if (criteria.getInputId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getInputId(), root -> root.join(InputProduct_.input, JoinType.LEFT).get(Input_.id))
                    );
            }
            if (criteria.getCurrencyId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCurrencyId(),
                            root -> root.join(InputProduct_.currency, JoinType.LEFT).get(Currency_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
