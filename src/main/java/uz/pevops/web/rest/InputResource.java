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
import uz.pevops.repository.InputRepository;
import uz.pevops.service.InputQueryService;
import uz.pevops.service.InputService;
import uz.pevops.service.criteria.InputCriteria;
import uz.pevops.service.dto.InputDTO;
import uz.pevops.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link uz.pevops.domain.Input}.
 */
@RestController
@RequestMapping("/api")
public class InputResource {

    private final Logger log = LoggerFactory.getLogger(InputResource.class);

    private static final String ENTITY_NAME = "warehouse01Input";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InputService inputService;

    private final InputRepository inputRepository;

    private final InputQueryService inputQueryService;

    public InputResource(InputService inputService, InputRepository inputRepository, InputQueryService inputQueryService) {
        this.inputService = inputService;
        this.inputRepository = inputRepository;
        this.inputQueryService = inputQueryService;
    }

    /**
     * {@code POST  /inputs} : Create a new input.
     *
     * @param inputDTO the inputDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new inputDTO, or with status {@code 400 (Bad Request)} if the input has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/inputs")
    public ResponseEntity<InputDTO> createInput(@Valid @RequestBody InputDTO inputDTO) throws URISyntaxException {
        log.debug("REST request to save Input : {}", inputDTO);
        if (inputDTO.getId() != null) {
            throw new BadRequestAlertException("A new input cannot already have an ID", ENTITY_NAME, "idexists");
        }
        InputDTO result = inputService.save(inputDTO);
        return ResponseEntity
            .created(new URI("/api/inputs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /inputs/:id} : Updates an existing input.
     *
     * @param id the id of the inputDTO to save.
     * @param inputDTO the inputDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated inputDTO,
     * or with status {@code 400 (Bad Request)} if the inputDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the inputDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/inputs/{id}")
    public ResponseEntity<InputDTO> updateInput(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody InputDTO inputDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Input : {}, {}", id, inputDTO);
        if (inputDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, inputDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!inputRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        InputDTO result = inputService.update(inputDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, inputDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /inputs/:id} : Partial updates given fields of an existing input, field will ignore if it is null
     *
     * @param id the id of the inputDTO to save.
     * @param inputDTO the inputDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated inputDTO,
     * or with status {@code 400 (Bad Request)} if the inputDTO is not valid,
     * or with status {@code 404 (Not Found)} if the inputDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the inputDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/inputs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<InputDTO> partialUpdateInput(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody InputDTO inputDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Input partially : {}, {}", id, inputDTO);
        if (inputDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, inputDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!inputRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<InputDTO> result = inputService.partialUpdate(inputDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, inputDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /inputs} : get all the inputs.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of inputs in body.
     */
    @GetMapping("/inputs")
    public ResponseEntity<List<InputDTO>> getAllInputs(
        InputCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Inputs by criteria: {}", criteria.toString().replaceAll("[\n\r\t]", "_"));
        Page<InputDTO> page = inputQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /inputs/count} : count all the inputs.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/inputs/count")
    public ResponseEntity<Long> countInputs(InputCriteria criteria) {
        log.debug("REST request to count Inputs by criteria: {}", criteria.toString().replaceAll("[\n\r\t]", "_"));
        return ResponseEntity.ok().body(inputQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /inputs/:id} : get the "id" input.
     *
     * @param id the id of the inputDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the inputDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/inputs/{id}")
    public ResponseEntity<InputDTO> getInput(@PathVariable Long id) {
        log.debug("REST request to get Input : {}", id);
        Optional<InputDTO> inputDTO = inputService.findOne(id);
        return ResponseUtil.wrapOrNotFound(inputDTO);
    }

    /**
     * {@code DELETE  /inputs/:id} : delete the "id" input.
     *
     * @param id the id of the inputDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/inputs/{id}")
    public ResponseEntity<Void> deleteInput(@PathVariable Long id) {
        log.debug("REST request to delete Input : {}", id);
        inputService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
