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
import uz.devops.domain.Measurement;
import uz.devops.repository.MeasurementRepository;
import uz.devops.service.criteria.MeasurementCriteria;
import uz.devops.service.dto.MeasurementDTO;
import uz.devops.service.mapper.MeasurementMapper;

/**
 * Service for executing complex queries for {@link Measurement} entities in the database.
 * The main input is a {@link MeasurementCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link MeasurementDTO} or a {@link Page} of {@link MeasurementDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MeasurementQueryService extends QueryService<Measurement> {

    private final Logger log = LoggerFactory.getLogger(MeasurementQueryService.class);

    private final MeasurementRepository measurementRepository;

    private final MeasurementMapper measurementMapper;

    public MeasurementQueryService(MeasurementRepository measurementRepository, MeasurementMapper measurementMapper) {
        this.measurementRepository = measurementRepository;
        this.measurementMapper = measurementMapper;
    }

    /**
     * Return a {@link List} of {@link MeasurementDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<MeasurementDTO> findByCriteria(MeasurementCriteria criteria) {
        log.debug("find by criteria : {}", criteria.toString().replaceAll("[\n\r\t]", "_"));
        final Specification<Measurement> specification = createSpecification(criteria);
        return measurementMapper.toDto(measurementRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link MeasurementDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MeasurementDTO> findByCriteria(MeasurementCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria.toString().replaceAll("[\n\r\t]", "_"), page);
        final Specification<Measurement> specification = createSpecification(criteria);
        return measurementRepository.findAll(specification, page).map(measurementMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MeasurementCriteria criteria) {
        log.debug("count by criteria : {}", criteria.toString().replaceAll("[\n\r\t]", "_"));
        final Specification<Measurement> specification = createSpecification(criteria);
        return measurementRepository.count(specification);
    }

    /**
     * Function to convert {@link MeasurementCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Measurement> createSpecification(MeasurementCriteria criteria) {
        Specification<Measurement> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Measurement_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Measurement_.name));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), Measurement_.status));
            }
        }
        return specification;
    }
}
