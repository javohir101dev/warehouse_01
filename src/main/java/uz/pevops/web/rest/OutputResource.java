package uz.pevops.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;
import uz.pevops.repository.OutputRepository;
import uz.pevops.service.OutputQueryService;
import uz.pevops.service.OutputService;
import uz.pevops.service.criteria.OutputCriteria;
import uz.pevops.service.dto.OutputDTO;
import uz.pevops.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link uz.pevops.domain.Output}.
 */
@RestController
@RequestMapping("/api")
public class OutputResource {

    private final Logger log = LoggerFactory.getLogger(OutputResource.class);

    private static final String ENTITY_NAME = "warehouse01Output";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OutputService outputService;

    private final OutputRepository outputRepository;

    private final OutputQueryService outputQueryService;

    public OutputResource(OutputService outputService, OutputRepository outputRepository, OutputQueryService outputQueryService) {
        this.outputService = outputService;
        this.outputRepository = outputRepository;
        this.outputQueryService = outputQueryService;
    }

    /**
     * {@code POST  /outputs} : Create a new output.
     *
     * @param outputDTO the outputDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new outputDTO, or with status {@code 400 (Bad Request)} if the output has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/outputs")
    public ResponseEntity<OutputDTO> createOutput(@Valid @RequestBody OutputDTO outputDTO) throws URISyntaxException {
        log.debug("REST request to save Output : {}", outputDTO);
        if (outputDTO.getId() != null) {
            throw new BadRequestAlertException("A new output cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OutputDTO result = outputService.save(outputDTO);
        return ResponseEntity
            .created(new URI("/api/outputs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /outputs/:id} : Updates an existing output.
     *
     * @param id the id of the outputDTO to save.
     * @param outputDTO the outputDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated outputDTO,
     * or with status {@code 400 (Bad Request)} if the outputDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the outputDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/outputs/{id}")
    public ResponseEntity<OutputDTO> updateOutput(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody OutputDTO outputDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Output : {}, {}", id, outputDTO);
        if (outputDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, outputDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!outputRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        OutputDTO result = outputService.update(outputDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, outputDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /outputs/:id} : Partial updates given fields of an existing output, field will ignore if it is null
     *
     * @param id the id of the outputDTO to save.
     * @param outputDTO the outputDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated outputDTO,
     * or with status {@code 400 (Bad Request)} if the outputDTO is not valid,
     * or with status {@code 404 (Not Found)} if the outputDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the outputDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/outputs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<OutputDTO> partialUpdateOutput(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody OutputDTO outputDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Output partially : {}, {}", id, outputDTO);
        if (outputDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, outputDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!outputRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OutputDTO> result = outputService.partialUpdate(outputDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, outputDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /outputs} : get all the outputs.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of outputs in body.
     */
    @GetMapping("/outputs")
    public ResponseEntity<List<OutputDTO>> getAllOutputs(
        OutputCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Outputs by criteria: {}", criteria.toString().replaceAll("[\n\r\t]", "_"));
        Page<OutputDTO> page = outputQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /outputs/count} : count all the outputs.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/outputs/count")
    public ResponseEntity<Long> countOutputs(OutputCriteria criteria) {
        log.debug("REST request to count Outputs by criteria: {}", criteria.toString().replaceAll("[\n\r\t]", "_"));
        return ResponseEntity.ok().body(outputQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /outputs/:id} : get the "id" output.
     *
     * @param id the id of the outputDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the outputDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/outputs/{id}")
    public ResponseEntity<OutputDTO> getOutput(@PathVariable Long id) {
        log.debug("REST request to get Output : {}", id);
        Optional<OutputDTO> outputDTO = outputService.findOne(id);
        return ResponseUtil.wrapOrNotFound(outputDTO);
    }

    /**
     * {@code DELETE  /outputs/:id} : delete the "id" output.
     *
     * @param id the id of the outputDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/outputs/{id}")
    public ResponseEntity<Void> deleteOutput(@PathVariable Long id) {
        log.debug("REST request to delete Output : {}", id);
        outputService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
