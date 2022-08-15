package uz.pevops.web.rest;

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
import org.springframework.util.Base64Utils;
import uz.pevops.IntegrationTest;
import uz.pevops.domain.Attachment;
import uz.pevops.domain.AttachmentContent;
import uz.pevops.repository.AttachmentContentRepository;
import uz.pevops.service.criteria.AttachmentContentCriteria;
import uz.pevops.service.dto.AttachmentContentDTO;
import uz.pevops.service.mapper.AttachmentContentMapper;

/**
 * Integration tests for the {@link AttachmentContentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AttachmentContentResourceIT {

    private static final byte[] DEFAULT_BYTES = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_BYTES = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_BYTES_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_BYTES_CONTENT_TYPE = "image/png";

    private static final String ENTITY_API_URL = "/api/attachment-contents";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AttachmentContentRepository attachmentContentRepository;

    @Autowired
    private AttachmentContentMapper attachmentContentMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAttachmentContentMockMvc;

    private AttachmentContent attachmentContent;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AttachmentContent createEntity(EntityManager em) {
        AttachmentContent attachmentContent = new AttachmentContent().bytes(DEFAULT_BYTES).bytesContentType(DEFAULT_BYTES_CONTENT_TYPE);
        return attachmentContent;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AttachmentContent createUpdatedEntity(EntityManager em) {
        AttachmentContent attachmentContent = new AttachmentContent().bytes(UPDATED_BYTES).bytesContentType(UPDATED_BYTES_CONTENT_TYPE);
        return attachmentContent;
    }

    @BeforeEach
    public void initTest() {
        attachmentContent = createEntity(em);
    }

    @Test
    @Transactional
    void createAttachmentContent() throws Exception {
        int databaseSizeBeforeCreate = attachmentContentRepository.findAll().size();
        // Create the AttachmentContent
        AttachmentContentDTO attachmentContentDTO = attachmentContentMapper.toDto(attachmentContent);
        restAttachmentContentMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(attachmentContentDTO))
            )
            .andExpect(status().isCreated());

        // Validate the AttachmentContent in the database
        List<AttachmentContent> attachmentContentList = attachmentContentRepository.findAll();
        assertThat(attachmentContentList).hasSize(databaseSizeBeforeCreate + 1);
        AttachmentContent testAttachmentContent = attachmentContentList.get(attachmentContentList.size() - 1);
        assertThat(testAttachmentContent.getBytes()).isEqualTo(DEFAULT_BYTES);
        assertThat(testAttachmentContent.getBytesContentType()).isEqualTo(DEFAULT_BYTES_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void createAttachmentContentWithExistingId() throws Exception {
        // Create the AttachmentContent with an existing ID
        attachmentContent.setId(1L);
        AttachmentContentDTO attachmentContentDTO = attachmentContentMapper.toDto(attachmentContent);

        int databaseSizeBeforeCreate = attachmentContentRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAttachmentContentMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(attachmentContentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AttachmentContent in the database
        List<AttachmentContent> attachmentContentList = attachmentContentRepository.findAll();
        assertThat(attachmentContentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAttachmentContents() throws Exception {
        // Initialize the database
        attachmentContentRepository.saveAndFlush(attachmentContent);

        // Get all the attachmentContentList
        restAttachmentContentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(attachmentContent.getId().intValue())))
            .andExpect(jsonPath("$.[*].bytesContentType").value(hasItem(DEFAULT_BYTES_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].bytes").value(hasItem(Base64Utils.encodeToString(DEFAULT_BYTES))));
    }

    @Test
    @Transactional
    void getAttachmentContent() throws Exception {
        // Initialize the database
        attachmentContentRepository.saveAndFlush(attachmentContent);

        // Get the attachmentContent
        restAttachmentContentMockMvc
            .perform(get(ENTITY_API_URL_ID, attachmentContent.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(attachmentContent.getId().intValue()))
            .andExpect(jsonPath("$.bytesContentType").value(DEFAULT_BYTES_CONTENT_TYPE))
            .andExpect(jsonPath("$.bytes").value(Base64Utils.encodeToString(DEFAULT_BYTES)));
    }

    @Test
    @Transactional
    void getAttachmentContentsByIdFiltering() throws Exception {
        // Initialize the database
        attachmentContentRepository.saveAndFlush(attachmentContent);

        Long id = attachmentContent.getId();

        defaultAttachmentContentShouldBeFound("id.equals=" + id);
        defaultAttachmentContentShouldNotBeFound("id.notEquals=" + id);

        defaultAttachmentContentShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAttachmentContentShouldNotBeFound("id.greaterThan=" + id);

        defaultAttachmentContentShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAttachmentContentShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAttachmentContentsByAttachmentIsEqualToSomething() throws Exception {
        Attachment attachment;
        if (TestUtil.findAll(em, Attachment.class).isEmpty()) {
            attachmentContentRepository.saveAndFlush(attachmentContent);
            attachment = AttachmentResourceIT.createEntity(em);
        } else {
            attachment = TestUtil.findAll(em, Attachment.class).get(0);
        }
        em.persist(attachment);
        em.flush();
        attachmentContent.setAttachment(attachment);
        attachmentContentRepository.saveAndFlush(attachmentContent);
        Long attachmentId = attachment.getId();

        // Get all the attachmentContentList where attachment equals to attachmentId
        defaultAttachmentContentShouldBeFound("attachmentId.equals=" + attachmentId);

        // Get all the attachmentContentList where attachment equals to (attachmentId + 1)
        defaultAttachmentContentShouldNotBeFound("attachmentId.equals=" + (attachmentId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAttachmentContentShouldBeFound(String filter) throws Exception {
        restAttachmentContentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(attachmentContent.getId().intValue())))
            .andExpect(jsonPath("$.[*].bytesContentType").value(hasItem(DEFAULT_BYTES_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].bytes").value(hasItem(Base64Utils.encodeToString(DEFAULT_BYTES))));

        // Check, that the count call also returns 1
        restAttachmentContentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAttachmentContentShouldNotBeFound(String filter) throws Exception {
        restAttachmentContentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAttachmentContentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAttachmentContent() throws Exception {
        // Get the attachmentContent
        restAttachmentContentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAttachmentContent() throws Exception {
        // Initialize the database
        attachmentContentRepository.saveAndFlush(attachmentContent);

        int databaseSizeBeforeUpdate = attachmentContentRepository.findAll().size();

        // Update the attachmentContent
        AttachmentContent updatedAttachmentContent = attachmentContentRepository.findById(attachmentContent.getId()).get();
        // Disconnect from session so that the updates on updatedAttachmentContent are not directly saved in db
        em.detach(updatedAttachmentContent);
        updatedAttachmentContent.bytes(UPDATED_BYTES).bytesContentType(UPDATED_BYTES_CONTENT_TYPE);
        AttachmentContentDTO attachmentContentDTO = attachmentContentMapper.toDto(updatedAttachmentContent);

        restAttachmentContentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, attachmentContentDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(attachmentContentDTO))
            )
            .andExpect(status().isOk());

        // Validate the AttachmentContent in the database
        List<AttachmentContent> attachmentContentList = attachmentContentRepository.findAll();
        assertThat(attachmentContentList).hasSize(databaseSizeBeforeUpdate);
        AttachmentContent testAttachmentContent = attachmentContentList.get(attachmentContentList.size() - 1);
        assertThat(testAttachmentContent.getBytes()).isEqualTo(UPDATED_BYTES);
        assertThat(testAttachmentContent.getBytesContentType()).isEqualTo(UPDATED_BYTES_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingAttachmentContent() throws Exception {
        int databaseSizeBeforeUpdate = attachmentContentRepository.findAll().size();
        attachmentContent.setId(count.incrementAndGet());

        // Create the AttachmentContent
        AttachmentContentDTO attachmentContentDTO = attachmentContentMapper.toDto(attachmentContent);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAttachmentContentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, attachmentContentDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(attachmentContentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AttachmentContent in the database
        List<AttachmentContent> attachmentContentList = attachmentContentRepository.findAll();
        assertThat(attachmentContentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAttachmentContent() throws Exception {
        int databaseSizeBeforeUpdate = attachmentContentRepository.findAll().size();
        attachmentContent.setId(count.incrementAndGet());

        // Create the AttachmentContent
        AttachmentContentDTO attachmentContentDTO = attachmentContentMapper.toDto(attachmentContent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAttachmentContentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(attachmentContentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AttachmentContent in the database
        List<AttachmentContent> attachmentContentList = attachmentContentRepository.findAll();
        assertThat(attachmentContentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAttachmentContent() throws Exception {
        int databaseSizeBeforeUpdate = attachmentContentRepository.findAll().size();
        attachmentContent.setId(count.incrementAndGet());

        // Create the AttachmentContent
        AttachmentContentDTO attachmentContentDTO = attachmentContentMapper.toDto(attachmentContent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAttachmentContentMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(attachmentContentDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AttachmentContent in the database
        List<AttachmentContent> attachmentContentList = attachmentContentRepository.findAll();
        assertThat(attachmentContentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAttachmentContentWithPatch() throws Exception {
        // Initialize the database
        attachmentContentRepository.saveAndFlush(attachmentContent);

        int databaseSizeBeforeUpdate = attachmentContentRepository.findAll().size();

        // Update the attachmentContent using partial update
        AttachmentContent partialUpdatedAttachmentContent = new AttachmentContent();
        partialUpdatedAttachmentContent.setId(attachmentContent.getId());

        restAttachmentContentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAttachmentContent.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAttachmentContent))
            )
            .andExpect(status().isOk());

        // Validate the AttachmentContent in the database
        List<AttachmentContent> attachmentContentList = attachmentContentRepository.findAll();
        assertThat(attachmentContentList).hasSize(databaseSizeBeforeUpdate);
        AttachmentContent testAttachmentContent = attachmentContentList.get(attachmentContentList.size() - 1);
        assertThat(testAttachmentContent.getBytes()).isEqualTo(DEFAULT_BYTES);
        assertThat(testAttachmentContent.getBytesContentType()).isEqualTo(DEFAULT_BYTES_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateAttachmentContentWithPatch() throws Exception {
        // Initialize the database
        attachmentContentRepository.saveAndFlush(attachmentContent);

        int databaseSizeBeforeUpdate = attachmentContentRepository.findAll().size();

        // Update the attachmentContent using partial update
        AttachmentContent partialUpdatedAttachmentContent = new AttachmentContent();
        partialUpdatedAttachmentContent.setId(attachmentContent.getId());

        partialUpdatedAttachmentContent.bytes(UPDATED_BYTES).bytesContentType(UPDATED_BYTES_CONTENT_TYPE);

        restAttachmentContentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAttachmentContent.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAttachmentContent))
            )
            .andExpect(status().isOk());

        // Validate the AttachmentContent in the database
        List<AttachmentContent> attachmentContentList = attachmentContentRepository.findAll();
        assertThat(attachmentContentList).hasSize(databaseSizeBeforeUpdate);
        AttachmentContent testAttachmentContent = attachmentContentList.get(attachmentContentList.size() - 1);
        assertThat(testAttachmentContent.getBytes()).isEqualTo(UPDATED_BYTES);
        assertThat(testAttachmentContent.getBytesContentType()).isEqualTo(UPDATED_BYTES_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingAttachmentContent() throws Exception {
        int databaseSizeBeforeUpdate = attachmentContentRepository.findAll().size();
        attachmentContent.setId(count.incrementAndGet());

        // Create the AttachmentContent
        AttachmentContentDTO attachmentContentDTO = attachmentContentMapper.toDto(attachmentContent);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAttachmentContentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, attachmentContentDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(attachmentContentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AttachmentContent in the database
        List<AttachmentContent> attachmentContentList = attachmentContentRepository.findAll();
        assertThat(attachmentContentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAttachmentContent() throws Exception {
        int databaseSizeBeforeUpdate = attachmentContentRepository.findAll().size();
        attachmentContent.setId(count.incrementAndGet());

        // Create the AttachmentContent
        AttachmentContentDTO attachmentContentDTO = attachmentContentMapper.toDto(attachmentContent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAttachmentContentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(attachmentContentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AttachmentContent in the database
        List<AttachmentContent> attachmentContentList = attachmentContentRepository.findAll();
        assertThat(attachmentContentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAttachmentContent() throws Exception {
        int databaseSizeBeforeUpdate = attachmentContentRepository.findAll().size();
        attachmentContent.setId(count.incrementAndGet());

        // Create the AttachmentContent
        AttachmentContentDTO attachmentContentDTO = attachmentContentMapper.toDto(attachmentContent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAttachmentContentMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(attachmentContentDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AttachmentContent in the database
        List<AttachmentContent> attachmentContentList = attachmentContentRepository.findAll();
        assertThat(attachmentContentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAttachmentContent() throws Exception {
        // Initialize the database
        attachmentContentRepository.saveAndFlush(attachmentContent);

        int databaseSizeBeforeDelete = attachmentContentRepository.findAll().size();

        // Delete the attachmentContent
        restAttachmentContentMockMvc
            .perform(delete(ENTITY_API_URL_ID, attachmentContent.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AttachmentContent> attachmentContentList = attachmentContentRepository.findAll();
        assertThat(attachmentContentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
