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
import uz.pevops.repository.AttachmentContentRepository;
import uz.pevops.service.AttachmentContentQueryService;
import uz.pevops.service.AttachmentContentService;
import uz.pevops.service.criteria.AttachmentContentCriteria;
import uz.pevops.service.dto.AttachmentContentDTO;
import uz.pevops.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link uz.pevops.domain.AttachmentContent}.
 */
@RestController
@RequestMapping("/api")
public class AttachmentContentResource {

    private final Logger log = LoggerFactory.getLogger(AttachmentContentResource.class);

    private static final String ENTITY_NAME = "warehouse01AttachmentContent";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AttachmentContentService attachmentContentService;

    private final AttachmentContentRepository attachmentContentRepository;

    private final AttachmentContentQueryService attachmentContentQueryService;

    public AttachmentContentResource(
        AttachmentContentService attachmentContentService,
        AttachmentContentRepository attachmentContentRepository,
        AttachmentContentQueryService attachmentContentQueryService
    ) {
        this.attachmentContentService = attachmentContentService;
        this.attachmentContentRepository = attachmentContentRepository;
        this.attachmentContentQueryService = attachmentContentQueryService;
    }

    /**
     * {@code POST  /attachment-contents} : Create a new attachmentContent.
     *
     * @param attachmentContentDTO the attachmentContentDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new attachmentContentDTO, or with status {@code 400 (Bad Request)} if the attachmentContent has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/attachment-contents")
    public ResponseEntity<AttachmentContentDTO> createAttachmentContent(@Valid @RequestBody AttachmentContentDTO attachmentContentDTO)
        throws URISyntaxException {
        log.debug("REST request to save AttachmentContent : {}", attachmentContentDTO);
        if (attachmentContentDTO.getId() != null) {
            throw new BadRequestAlertException("A new attachmentContent cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AttachmentContentDTO result = attachmentContentService.save(attachmentContentDTO);
        return ResponseEntity
            .created(new URI("/api/attachment-contents/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /attachment-contents/:id} : Updates an existing attachmentContent.
     *
     * @param id the id of the attachmentContentDTO to save.
     * @param attachmentContentDTO the attachmentContentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated attachmentContentDTO,
     * or with status {@code 400 (Bad Request)} if the attachmentContentDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the attachmentContentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/attachment-contents/{id}")
    public ResponseEntity<AttachmentContentDTO> updateAttachmentContent(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AttachmentContentDTO attachmentContentDTO
    ) throws URISyntaxException {
        log.debug("REST request to update AttachmentContent : {}, {}", id, attachmentContentDTO);
        if (attachmentContentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, attachmentContentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!attachmentContentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AttachmentContentDTO result = attachmentContentService.update(attachmentContentDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, attachmentContentDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /attachment-contents/:id} : Partial updates given fields of an existing attachmentContent, field will ignore if it is null
     *
     * @param id the id of the attachmentContentDTO to save.
     * @param attachmentContentDTO the attachmentContentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated attachmentContentDTO,
     * or with status {@code 400 (Bad Request)} if the attachmentContentDTO is not valid,
     * or with status {@code 404 (Not Found)} if the attachmentContentDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the attachmentContentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/attachment-contents/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AttachmentContentDTO> partialUpdateAttachmentContent(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AttachmentContentDTO attachmentContentDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update AttachmentContent partially : {}, {}", id, attachmentContentDTO);
        if (attachmentContentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, attachmentContentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!attachmentContentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AttachmentContentDTO> result = attachmentContentService.partialUpdate(attachmentContentDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, attachmentContentDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /attachment-contents} : get all the attachmentContents.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of attachmentContents in body.
     */
    @GetMapping("/attachment-contents")
    public ResponseEntity<List<AttachmentContentDTO>> getAllAttachmentContents(
        AttachmentContentCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get AttachmentContents by criteria: {}", criteria.toString().replaceAll("[\n\r\t]", "_"));
        Page<AttachmentContentDTO> page = attachmentContentQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /attachment-contents/count} : count all the attachmentContents.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/attachment-contents/count")
    public ResponseEntity<Long> countAttachmentContents(AttachmentContentCriteria criteria) {
        log.debug("REST request to count AttachmentContents by criteria: {}", criteria.toString().replaceAll("[\n\r\t]", "_"));
        return ResponseEntity.ok().body(attachmentContentQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /attachment-contents/:id} : get the "id" attachmentContent.
     *
     * @param id the id of the attachmentContentDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the attachmentContentDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/attachment-contents/{id}")
    public ResponseEntity<AttachmentContentDTO> getAttachmentContent(@PathVariable Long id) {
        log.debug("REST request to get AttachmentContent : {}", id);
        Optional<AttachmentContentDTO> attachmentContentDTO = attachmentContentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(attachmentContentDTO);
    }

    /**
     * {@code DELETE  /attachment-contents/:id} : delete the "id" attachmentContent.
     *
     * @param id the id of the attachmentContentDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/attachment-contents/{id}")
    public ResponseEntity<Void> deleteAttachmentContent(@PathVariable Long id) {
        log.debug("REST request to delete AttachmentContent : {}", id);
        attachmentContentService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
