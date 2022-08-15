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
import uz.pevops.IntegrationTest;
import uz.pevops.domain.Measurement;
import uz.pevops.domain.enumeration.Status;
import uz.pevops.repository.MeasurementRepository;
import uz.pevops.service.criteria.MeasurementCriteria;
import uz.pevops.service.dto.MeasurementDTO;
import uz.pevops.service.mapper.MeasurementMapper;

/**
 * Integration tests for the {@link MeasurementResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MeasurementResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Status DEFAULT_STATUS = Status.ACTIVE;
    private static final Status UPDATED_STATUS = Status.BLOCKED;

    private static final String ENTITY_API_URL = "/api/measurements";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MeasurementRepository measurementRepository;

    @Autowired
    private MeasurementMapper measurementMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMeasurementMockMvc;

    private Measurement measurement;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Measurement createEntity(EntityManager em) {
        Measurement measurement = new Measurement().name(DEFAULT_NAME).status(DEFAULT_STATUS);
        return measurement;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Measurement createUpdatedEntity(EntityManager em) {
        Measurement measurement = new Measurement().name(UPDATED_NAME).status(UPDATED_STATUS);
        return measurement;
    }

    @BeforeEach
    public void initTest() {
        measurement = createEntity(em);
    }

    @Test
    @Transactional
    void createMeasurement() throws Exception {
        int databaseSizeBeforeCreate = measurementRepository.findAll().size();
        // Create the Measurement
        MeasurementDTO measurementDTO = measurementMapper.toDto(measurement);
        restMeasurementMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(measurementDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Measurement in the database
        List<Measurement> measurementList = measurementRepository.findAll();
        assertThat(measurementList).hasSize(databaseSizeBeforeCreate + 1);
        Measurement testMeasurement = measurementList.get(measurementList.size() - 1);
        assertThat(testMeasurement.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMeasurement.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void createMeasurementWithExistingId() throws Exception {
        // Create the Measurement with an existing ID
        measurement.setId(1L);
        MeasurementDTO measurementDTO = measurementMapper.toDto(measurement);

        int databaseSizeBeforeCreate = measurementRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMeasurementMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(measurementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Measurement in the database
        List<Measurement> measurementList = measurementRepository.findAll();
        assertThat(measurementList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = measurementRepository.findAll().size();
        // set the field null
        measurement.setName(null);

        // Create the Measurement, which fails.
        MeasurementDTO measurementDTO = measurementMapper.toDto(measurement);

        restMeasurementMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(measurementDTO))
            )
            .andExpect(status().isBadRequest());

        List<Measurement> measurementList = measurementRepository.findAll();
        assertThat(measurementList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = measurementRepository.findAll().size();
        // set the field null
        measurement.setStatus(null);

        // Create the Measurement, which fails.
        MeasurementDTO measurementDTO = measurementMapper.toDto(measurement);

        restMeasurementMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(measurementDTO))
            )
            .andExpect(status().isBadRequest());

        List<Measurement> measurementList = measurementRepository.findAll();
        assertThat(measurementList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMeasurements() throws Exception {
        // Initialize the database
        measurementRepository.saveAndFlush(measurement);

        // Get all the measurementList
        restMeasurementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(measurement.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    void getMeasurement() throws Exception {
        // Initialize the database
        measurementRepository.saveAndFlush(measurement);

        // Get the measurement
        restMeasurementMockMvc
            .perform(get(ENTITY_API_URL_ID, measurement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(measurement.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getMeasurementsByIdFiltering() throws Exception {
        // Initialize the database
        measurementRepository.saveAndFlush(measurement);

        Long id = measurement.getId();

        defaultMeasurementShouldBeFound("id.equals=" + id);
        defaultMeasurementShouldNotBeFound("id.notEquals=" + id);

        defaultMeasurementShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultMeasurementShouldNotBeFound("id.greaterThan=" + id);

        defaultMeasurementShouldBeFound("id.lessThanOrEqual=" + id);
        defaultMeasurementShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMeasurementsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        measurementRepository.saveAndFlush(measurement);

        // Get all the measurementList where name equals to DEFAULT_NAME
        defaultMeasurementShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the measurementList where name equals to UPDATED_NAME
        defaultMeasurementShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMeasurementsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        measurementRepository.saveAndFlush(measurement);

        // Get all the measurementList where name in DEFAULT_NAME or UPDATED_NAME
        defaultMeasurementShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the measurementList where name equals to UPDATED_NAME
        defaultMeasurementShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMeasurementsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        measurementRepository.saveAndFlush(measurement);

        // Get all the measurementList where name is not null
        defaultMeasurementShouldBeFound("name.specified=true");

        // Get all the measurementList where name is null
        defaultMeasurementShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllMeasurementsByNameContainsSomething() throws Exception {
        // Initialize the database
        measurementRepository.saveAndFlush(measurement);

        // Get all the measurementList where name contains DEFAULT_NAME
        defaultMeasurementShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the measurementList where name contains UPDATED_NAME
        defaultMeasurementShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMeasurementsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        measurementRepository.saveAndFlush(measurement);

        // Get all the measurementList where name does not contain DEFAULT_NAME
        defaultMeasurementShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the measurementList where name does not contain UPDATED_NAME
        defaultMeasurementShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMeasurementsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        measurementRepository.saveAndFlush(measurement);

        // Get all the measurementList where status equals to DEFAULT_STATUS
        defaultMeasurementShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the measurementList where status equals to UPDATED_STATUS
        defaultMeasurementShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllMeasurementsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        measurementRepository.saveAndFlush(measurement);

        // Get all the measurementList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultMeasurementShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the measurementList where status equals to UPDATED_STATUS
        defaultMeasurementShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllMeasurementsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        measurementRepository.saveAndFlush(measurement);

        // Get all the measurementList where status is not null
        defaultMeasurementShouldBeFound("status.specified=true");

        // Get all the measurementList where status is null
        defaultMeasurementShouldNotBeFound("status.specified=false");
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMeasurementShouldBeFound(String filter) throws Exception {
        restMeasurementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(measurement.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));

        // Check, that the count call also returns 1
        restMeasurementMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMeasurementShouldNotBeFound(String filter) throws Exception {
        restMeasurementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMeasurementMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMeasurement() throws Exception {
        // Get the measurement
        restMeasurementMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewMeasurement() throws Exception {
        // Initialize the database
        measurementRepository.saveAndFlush(measurement);

        int databaseSizeBeforeUpdate = measurementRepository.findAll().size();

        // Update the measurement
        Measurement updatedMeasurement = measurementRepository.findById(measurement.getId()).get();
        // Disconnect from session so that the updates on updatedMeasurement are not directly saved in db
        em.detach(updatedMeasurement);
        updatedMeasurement.name(UPDATED_NAME).status(UPDATED_STATUS);
        MeasurementDTO measurementDTO = measurementMapper.toDto(updatedMeasurement);

        restMeasurementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, measurementDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(measurementDTO))
            )
            .andExpect(status().isOk());

        // Validate the Measurement in the database
        List<Measurement> measurementList = measurementRepository.findAll();
        assertThat(measurementList).hasSize(databaseSizeBeforeUpdate);
        Measurement testMeasurement = measurementList.get(measurementList.size() - 1);
        assertThat(testMeasurement.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMeasurement.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void putNonExistingMeasurement() throws Exception {
        int databaseSizeBeforeUpdate = measurementRepository.findAll().size();
        measurement.setId(count.incrementAndGet());

        // Create the Measurement
        MeasurementDTO measurementDTO = measurementMapper.toDto(measurement);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMeasurementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, measurementDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(measurementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Measurement in the database
        List<Measurement> measurementList = measurementRepository.findAll();
        assertThat(measurementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMeasurement() throws Exception {
        int databaseSizeBeforeUpdate = measurementRepository.findAll().size();
        measurement.setId(count.incrementAndGet());

        // Create the Measurement
        MeasurementDTO measurementDTO = measurementMapper.toDto(measurement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMeasurementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(measurementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Measurement in the database
        List<Measurement> measurementList = measurementRepository.findAll();
        assertThat(measurementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMeasurement() throws Exception {
        int databaseSizeBeforeUpdate = measurementRepository.findAll().size();
        measurement.setId(count.incrementAndGet());

        // Create the Measurement
        MeasurementDTO measurementDTO = measurementMapper.toDto(measurement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMeasurementMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(measurementDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Measurement in the database
        List<Measurement> measurementList = measurementRepository.findAll();
        assertThat(measurementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMeasurementWithPatch() throws Exception {
        // Initialize the database
        measurementRepository.saveAndFlush(measurement);

        int databaseSizeBeforeUpdate = measurementRepository.findAll().size();

        // Update the measurement using partial update
        Measurement partialUpdatedMeasurement = new Measurement();
        partialUpdatedMeasurement.setId(measurement.getId());

        restMeasurementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMeasurement.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMeasurement))
            )
            .andExpect(status().isOk());

        // Validate the Measurement in the database
        List<Measurement> measurementList = measurementRepository.findAll();
        assertThat(measurementList).hasSize(databaseSizeBeforeUpdate);
        Measurement testMeasurement = measurementList.get(measurementList.size() - 1);
        assertThat(testMeasurement.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMeasurement.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void fullUpdateMeasurementWithPatch() throws Exception {
        // Initialize the database
        measurementRepository.saveAndFlush(measurement);

        int databaseSizeBeforeUpdate = measurementRepository.findAll().size();

        // Update the measurement using partial update
        Measurement partialUpdatedMeasurement = new Measurement();
        partialUpdatedMeasurement.setId(measurement.getId());

        partialUpdatedMeasurement.name(UPDATED_NAME).status(UPDATED_STATUS);

        restMeasurementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMeasurement.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMeasurement))
            )
            .andExpect(status().isOk());

        // Validate the Measurement in the database
        List<Measurement> measurementList = measurementRepository.findAll();
        assertThat(measurementList).hasSize(databaseSizeBeforeUpdate);
        Measurement testMeasurement = measurementList.get(measurementList.size() - 1);
        assertThat(testMeasurement.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMeasurement.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingMeasurement() throws Exception {
        int databaseSizeBeforeUpdate = measurementRepository.findAll().size();
        measurement.setId(count.incrementAndGet());

        // Create the Measurement
        MeasurementDTO measurementDTO = measurementMapper.toDto(measurement);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMeasurementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, measurementDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(measurementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Measurement in the database
        List<Measurement> measurementList = measurementRepository.findAll();
        assertThat(measurementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMeasurement() throws Exception {
        int databaseSizeBeforeUpdate = measurementRepository.findAll().size();
        measurement.setId(count.incrementAndGet());

        // Create the Measurement
        MeasurementDTO measurementDTO = measurementMapper.toDto(measurement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMeasurementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(measurementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Measurement in the database
        List<Measurement> measurementList = measurementRepository.findAll();
        assertThat(measurementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMeasurement() throws Exception {
        int databaseSizeBeforeUpdate = measurementRepository.findAll().size();
        measurement.setId(count.incrementAndGet());

        // Create the Measurement
        MeasurementDTO measurementDTO = measurementMapper.toDto(measurement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMeasurementMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(measurementDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Measurement in the database
        List<Measurement> measurementList = measurementRepository.findAll();
        assertThat(measurementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMeasurement() throws Exception {
        // Initialize the database
        measurementRepository.saveAndFlush(measurement);

        int databaseSizeBeforeDelete = measurementRepository.findAll().size();

        // Delete the measurement
        restMeasurementMockMvc
            .perform(delete(ENTITY_API_URL_ID, measurement.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Measurement> measurementList = measurementRepository.findAll();
        assertThat(measurementList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
