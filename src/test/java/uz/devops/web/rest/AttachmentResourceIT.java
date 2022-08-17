package uz.devops.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import uz.devops.IntegrationTest;
import uz.devops.domain.Attachment;
import uz.devops.repository.AttachmentRepository;
import uz.devops.service.criteria.AttachmentCriteria;
import uz.devops.service.dto.AttachmentDTO;
import uz.devops.service.mapper.AttachmentMapper;

/**
 * Integration tests for the {@link AttachmentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AttachmentResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Long DEFAULT_SIZE_ATTACHMENT = 1L;
    private static final Long UPDATED_SIZE_ATTACHMENT = 2L;
    private static final Long SMALLER_SIZE_ATTACHMENT = 1L - 1L;

    private static final String DEFAULT_CONTENT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT_TYPE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/attachments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AttachmentRepository attachmentRepository;

    @Autowired
    private AttachmentMapper attachmentMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAttachmentMockMvc;

    private Attachment attachment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Attachment createEntity(EntityManager em) {
        Attachment attachment = new Attachment()
            .name(DEFAULT_NAME)
            .sizeAttachment(DEFAULT_SIZE_ATTACHMENT)
            .contentType(DEFAULT_CONTENT_TYPE);
        return attachment;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Attachment createUpdatedEntity(EntityManager em) {
        Attachment attachment = new Attachment()
            .name(UPDATED_NAME)
            .sizeAttachment(UPDATED_SIZE_ATTACHMENT)
            .contentType(UPDATED_CONTENT_TYPE);
        return attachment;
    }

    @BeforeEach
    public void initTest() {
        attachment = createEntity(em);
    }

    @Test
    @Transactional
    void createAttachment() throws Exception {
        int databaseSizeBeforeCreate = attachmentRepository.findAll().size();
        // Create the Attachment
        AttachmentDTO attachmentDTO = attachmentMapper.toDto(attachment);
        restAttachmentMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(attachmentDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Attachment in the database
        List<Attachment> attachmentList = attachmentRepository.findAll();
        assertThat(attachmentList).hasSize(databaseSizeBeforeCreate + 1);
        Attachment testAttachment = attachmentList.get(attachmentList.size() - 1);
        assertThat(testAttachment.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testAttachment.getSizeAttachment()).isEqualTo(DEFAULT_SIZE_ATTACHMENT);
        assertThat(testAttachment.getContentType()).isEqualTo(DEFAULT_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void createAttachmentWithExistingId() throws Exception {
        // Create the Attachment with an existing ID
        attachment.setId(1L);
        AttachmentDTO attachmentDTO = attachmentMapper.toDto(attachment);

        int databaseSizeBeforeCreate = attachmentRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAttachmentMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(attachmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Attachment in the database
        List<Attachment> attachmentList = attachmentRepository.findAll();
        assertThat(attachmentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = attachmentRepository.findAll().size();
        // set the field null
        attachment.setName(null);

        // Create the Attachment, which fails.
        AttachmentDTO attachmentDTO = attachmentMapper.toDto(attachment);

        restAttachmentMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(attachmentDTO))
            )
            .andExpect(status().isBadRequest());

        List<Attachment> attachmentList = attachmentRepository.findAll();
        assertThat(attachmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSizeAttachmentIsRequired() throws Exception {
        int databaseSizeBeforeTest = attachmentRepository.findAll().size();
        // set the field null
        attachment.setSizeAttachment(null);

        // Create the Attachment, which fails.
        AttachmentDTO attachmentDTO = attachmentMapper.toDto(attachment);

        restAttachmentMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(attachmentDTO))
            )
            .andExpect(status().isBadRequest());

        List<Attachment> attachmentList = attachmentRepository.findAll();
        assertThat(attachmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkContentTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = attachmentRepository.findAll().size();
        // set the field null
        attachment.setContentType(null);

        // Create the Attachment, which fails.
        AttachmentDTO attachmentDTO = attachmentMapper.toDto(attachment);

        restAttachmentMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(attachmentDTO))
            )
            .andExpect(status().isBadRequest());

        List<Attachment> attachmentList = attachmentRepository.findAll();
        assertThat(attachmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAttachments() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList
        restAttachmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(attachment.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].sizeAttachment").value(hasItem(DEFAULT_SIZE_ATTACHMENT.intValue())))
            .andExpect(jsonPath("$.[*].contentType").value(hasItem(DEFAULT_CONTENT_TYPE)));
    }

    @Test
    @Transactional
    void getAttachment() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        // Get the attachment
        restAttachmentMockMvc
            .perform(get(ENTITY_API_URL_ID, attachment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(attachment.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.sizeAttachment").value(DEFAULT_SIZE_ATTACHMENT.intValue()))
            .andExpect(jsonPath("$.contentType").value(DEFAULT_CONTENT_TYPE));
    }

    @Test
    @Transactional
    void getAttachmentsByIdFiltering() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        Long id = attachment.getId();

        defaultAttachmentShouldBeFound("id.equals=" + id);
        defaultAttachmentShouldNotBeFound("id.notEquals=" + id);

        defaultAttachmentShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAttachmentShouldNotBeFound("id.greaterThan=" + id);

        defaultAttachmentShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAttachmentShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAttachmentsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where name equals to DEFAULT_NAME
        defaultAttachmentShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the attachmentList where name equals to UPDATED_NAME
        defaultAttachmentShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAttachmentsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where name in DEFAULT_NAME or UPDATED_NAME
        defaultAttachmentShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the attachmentList where name equals to UPDATED_NAME
        defaultAttachmentShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAttachmentsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where name is not null
        defaultAttachmentShouldBeFound("name.specified=true");

        // Get all the attachmentList where name is null
        defaultAttachmentShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllAttachmentsByNameContainsSomething() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where name contains DEFAULT_NAME
        defaultAttachmentShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the attachmentList where name contains UPDATED_NAME
        defaultAttachmentShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAttachmentsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where name does not contain DEFAULT_NAME
        defaultAttachmentShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the attachmentList where name does not contain UPDATED_NAME
        defaultAttachmentShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAttachmentsBySizeAttachmentIsEqualToSomething() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where sizeAttachment equals to DEFAULT_SIZE_ATTACHMENT
        defaultAttachmentShouldBeFound("sizeAttachment.equals=" + DEFAULT_SIZE_ATTACHMENT);

        // Get all the attachmentList where sizeAttachment equals to UPDATED_SIZE_ATTACHMENT
        defaultAttachmentShouldNotBeFound("sizeAttachment.equals=" + UPDATED_SIZE_ATTACHMENT);
    }

    @Test
    @Transactional
    void getAllAttachmentsBySizeAttachmentIsInShouldWork() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where sizeAttachment in DEFAULT_SIZE_ATTACHMENT or UPDATED_SIZE_ATTACHMENT
        defaultAttachmentShouldBeFound("sizeAttachment.in=" + DEFAULT_SIZE_ATTACHMENT + "," + UPDATED_SIZE_ATTACHMENT);

        // Get all the attachmentList where sizeAttachment equals to UPDATED_SIZE_ATTACHMENT
        defaultAttachmentShouldNotBeFound("sizeAttachment.in=" + UPDATED_SIZE_ATTACHMENT);
    }

    @Test
    @Transactional
    void getAllAttachmentsBySizeAttachmentIsNullOrNotNull() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where sizeAttachment is not null
        defaultAttachmentShouldBeFound("sizeAttachment.specified=true");

        // Get all the attachmentList where sizeAttachment is null
        defaultAttachmentShouldNotBeFound("sizeAttachment.specified=false");
    }

    @Test
    @Transactional
    void getAllAttachmentsBySizeAttachmentIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where sizeAttachment is greater than or equal to DEFAULT_SIZE_ATTACHMENT
        defaultAttachmentShouldBeFound("sizeAttachment.greaterThanOrEqual=" + DEFAULT_SIZE_ATTACHMENT);

        // Get all the attachmentList where sizeAttachment is greater than or equal to UPDATED_SIZE_ATTACHMENT
        defaultAttachmentShouldNotBeFound("sizeAttachment.greaterThanOrEqual=" + UPDATED_SIZE_ATTACHMENT);
    }

    @Test
    @Transactional
    void getAllAttachmentsBySizeAttachmentIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where sizeAttachment is less than or equal to DEFAULT_SIZE_ATTACHMENT
        defaultAttachmentShouldBeFound("sizeAttachment.lessThanOrEqual=" + DEFAULT_SIZE_ATTACHMENT);

        // Get all the attachmentList where sizeAttachment is less than or equal to SMALLER_SIZE_ATTACHMENT
        defaultAttachmentShouldNotBeFound("sizeAttachment.lessThanOrEqual=" + SMALLER_SIZE_ATTACHMENT);
    }

    @Test
    @Transactional
    void getAllAttachmentsBySizeAttachmentIsLessThanSomething() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where sizeAttachment is less than DEFAULT_SIZE_ATTACHMENT
        defaultAttachmentShouldNotBeFound("sizeAttachment.lessThan=" + DEFAULT_SIZE_ATTACHMENT);

        // Get all the attachmentList where sizeAttachment is less than UPDATED_SIZE_ATTACHMENT
        defaultAttachmentShouldBeFound("sizeAttachment.lessThan=" + UPDATED_SIZE_ATTACHMENT);
    }

    @Test
    @Transactional
    void getAllAttachmentsBySizeAttachmentIsGreaterThanSomething() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where sizeAttachment is greater than DEFAULT_SIZE_ATTACHMENT
        defaultAttachmentShouldNotBeFound("sizeAttachment.greaterThan=" + DEFAULT_SIZE_ATTACHMENT);

        // Get all the attachmentList where sizeAttachment is greater than SMALLER_SIZE_ATTACHMENT
        defaultAttachmentShouldBeFound("sizeAttachment.greaterThan=" + SMALLER_SIZE_ATTACHMENT);
    }

    @Test
    @Transactional
    void getAllAttachmentsByContentTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where contentType equals to DEFAULT_CONTENT_TYPE
        defaultAttachmentShouldBeFound("contentType.equals=" + DEFAULT_CONTENT_TYPE);

        // Get all the attachmentList where contentType equals to UPDATED_CONTENT_TYPE
        defaultAttachmentShouldNotBeFound("contentType.equals=" + UPDATED_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void getAllAttachmentsByContentTypeIsInShouldWork() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where contentType in DEFAULT_CONTENT_TYPE or UPDATED_CONTENT_TYPE
        defaultAttachmentShouldBeFound("contentType.in=" + DEFAULT_CONTENT_TYPE + "," + UPDATED_CONTENT_TYPE);

        // Get all the attachmentList where contentType equals to UPDATED_CONTENT_TYPE
        defaultAttachmentShouldNotBeFound("contentType.in=" + UPDATED_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void getAllAttachmentsByContentTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where contentType is not null
        defaultAttachmentShouldBeFound("contentType.specified=true");

        // Get all the attachmentList where contentType is null
        defaultAttachmentShouldNotBeFound("contentType.specified=false");
    }

    @Test
    @Transactional
    void getAllAttachmentsByContentTypeContainsSomething() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where contentType contains DEFAULT_CONTENT_TYPE
        defaultAttachmentShouldBeFound("contentType.contains=" + DEFAULT_CONTENT_TYPE);

        // Get all the attachmentList where contentType contains UPDATED_CONTENT_TYPE
        defaultAttachmentShouldNotBeFound("contentType.contains=" + UPDATED_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void getAllAttachmentsByContentTypeNotContainsSomething() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where contentType does not contain DEFAULT_CONTENT_TYPE
        defaultAttachmentShouldNotBeFound("contentType.doesNotContain=" + DEFAULT_CONTENT_TYPE);

        // Get all the attachmentList where contentType does not contain UPDATED_CONTENT_TYPE
        defaultAttachmentShouldBeFound("contentType.doesNotContain=" + UPDATED_CONTENT_TYPE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAttachmentShouldBeFound(String filter) throws Exception {
        restAttachmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(attachment.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].sizeAttachment").value(hasItem(DEFAULT_SIZE_ATTACHMENT.intValue())))
            .andExpect(jsonPath("$.[*].contentType").value(hasItem(DEFAULT_CONTENT_TYPE)));

        // Check, that the count call also returns 1
        restAttachmentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAttachmentShouldNotBeFound(String filter) throws Exception {
        restAttachmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAttachmentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAttachment() throws Exception {
        // Get the attachment
        restAttachmentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAttachment() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        int databaseSizeBeforeUpdate = attachmentRepository.findAll().size();

        // Update the attachment
        Attachment updatedAttachment = attachmentRepository.findById(attachment.getId()).get();
        // Disconnect from session so that the updates on updatedAttachment are not directly saved in db
        em.detach(updatedAttachment);
        updatedAttachment.name(UPDATED_NAME).sizeAttachment(UPDATED_SIZE_ATTACHMENT).contentType(UPDATED_CONTENT_TYPE);
        AttachmentDTO attachmentDTO = attachmentMapper.toDto(updatedAttachment);

        restAttachmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, attachmentDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(attachmentDTO))
            )
            .andExpect(status().isOk());

        // Validate the Attachment in the database
        List<Attachment> attachmentList = attachmentRepository.findAll();
        assertThat(attachmentList).hasSize(databaseSizeBeforeUpdate);
        Attachment testAttachment = attachmentList.get(attachmentList.size() - 1);
        assertThat(testAttachment.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testAttachment.getSizeAttachment()).isEqualTo(UPDATED_SIZE_ATTACHMENT);
        assertThat(testAttachment.getContentType()).isEqualTo(UPDATED_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingAttachment() throws Exception {
        int databaseSizeBeforeUpdate = attachmentRepository.findAll().size();
        attachment.setId(count.incrementAndGet());

        // Create the Attachment
        AttachmentDTO attachmentDTO = attachmentMapper.toDto(attachment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAttachmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, attachmentDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(attachmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Attachment in the database
        List<Attachment> attachmentList = attachmentRepository.findAll();
        assertThat(attachmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAttachment() throws Exception {
        int databaseSizeBeforeUpdate = attachmentRepository.findAll().size();
        attachment.setId(count.incrementAndGet());

        // Create the Attachment
        AttachmentDTO attachmentDTO = attachmentMapper.toDto(attachment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAttachmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(attachmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Attachment in the database
        List<Attachment> attachmentList = attachmentRepository.findAll();
        assertThat(attachmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAttachment() throws Exception {
        int databaseSizeBeforeUpdate = attachmentRepository.findAll().size();
        attachment.setId(count.incrementAndGet());

        // Create the Attachment
        AttachmentDTO attachmentDTO = attachmentMapper.toDto(attachment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAttachmentMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(attachmentDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Attachment in the database
        List<Attachment> attachmentList = attachmentRepository.findAll();
        assertThat(attachmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAttachmentWithPatch() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        int databaseSizeBeforeUpdate = attachmentRepository.findAll().size();

        // Update the attachment using partial update
        Attachment partialUpdatedAttachment = new Attachment();
        partialUpdatedAttachment.setId(attachment.getId());

        partialUpdatedAttachment.sizeAttachment(UPDATED_SIZE_ATTACHMENT);

        restAttachmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAttachment.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAttachment))
            )
            .andExpect(status().isOk());

        // Validate the Attachment in the database
        List<Attachment> attachmentList = attachmentRepository.findAll();
        assertThat(attachmentList).hasSize(databaseSizeBeforeUpdate);
        Attachment testAttachment = attachmentList.get(attachmentList.size() - 1);
        assertThat(testAttachment.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testAttachment.getSizeAttachment()).isEqualTo(UPDATED_SIZE_ATTACHMENT);
        assertThat(testAttachment.getContentType()).isEqualTo(DEFAULT_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateAttachmentWithPatch() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        int databaseSizeBeforeUpdate = attachmentRepository.findAll().size();

        // Update the attachment using partial update
        Attachment partialUpdatedAttachment = new Attachment();
        partialUpdatedAttachment.setId(attachment.getId());

        partialUpdatedAttachment.name(UPDATED_NAME).sizeAttachment(UPDATED_SIZE_ATTACHMENT).contentType(UPDATED_CONTENT_TYPE);

        restAttachmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAttachment.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAttachment))
            )
            .andExpect(status().isOk());

        // Validate the Attachment in the database
        List<Attachment> attachmentList = attachmentRepository.findAll();
        assertThat(attachmentList).hasSize(databaseSizeBeforeUpdate);
        Attachment testAttachment = attachmentList.get(attachmentList.size() - 1);
        assertThat(testAttachment.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testAttachment.getSizeAttachment()).isEqualTo(UPDATED_SIZE_ATTACHMENT);
        assertThat(testAttachment.getContentType()).isEqualTo(UPDATED_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingAttachment() throws Exception {
        int databaseSizeBeforeUpdate = attachmentRepository.findAll().size();
        attachment.setId(count.incrementAndGet());

        // Create the Attachment
        AttachmentDTO attachmentDTO = attachmentMapper.toDto(attachment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAttachmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, attachmentDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(attachmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Attachment in the database
        List<Attachment> attachmentList = attachmentRepository.findAll();
        assertThat(attachmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAttachment() throws Exception {
        int databaseSizeBeforeUpdate = attachmentRepository.findAll().size();
        attachment.setId(count.incrementAndGet());

        // Create the Attachment
        AttachmentDTO attachmentDTO = attachmentMapper.toDto(attachment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAttachmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(attachmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Attachment in the database
        List<Attachment> attachmentList = attachmentRepository.findAll();
        assertThat(attachmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAttachment() throws Exception {
        int databaseSizeBeforeUpdate = attachmentRepository.findAll().size();
        attachment.setId(count.incrementAndGet());

        // Create the Attachment
        AttachmentDTO attachmentDTO = attachmentMapper.toDto(attachment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAttachmentMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(attachmentDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Attachment in the database
        List<Attachment> attachmentList = attachmentRepository.findAll();
        assertThat(attachmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAttachment() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        int databaseSizeBeforeDelete = attachmentRepository.findAll().size();

        // Delete the attachment
        restAttachmentMockMvc
            .perform(delete(ENTITY_API_URL_ID, attachment.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Attachment> attachmentList = attachmentRepository.findAll();
        assertThat(attachmentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
