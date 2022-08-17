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
import uz.devops.domain.Users;
import uz.devops.domain.Warehouse;
import uz.devops.domain.enumeration.Status;
import uz.devops.repository.WarehouseRepository;
import uz.devops.service.criteria.WarehouseCriteria;
import uz.devops.service.dto.WarehouseDTO;
import uz.devops.service.mapper.WarehouseMapper;

/**
 * Integration tests for the {@link WarehouseResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class WarehouseResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Status DEFAULT_STATUS = Status.ACTIVE;
    private static final Status UPDATED_STATUS = Status.DELETED;

    private static final String ENTITY_API_URL = "/api/warehouses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Autowired
    private WarehouseMapper warehouseMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWarehouseMockMvc;

    private Warehouse warehouse;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Warehouse createEntity(EntityManager em) {
        Warehouse warehouse = new Warehouse().name(DEFAULT_NAME).status(DEFAULT_STATUS);
        return warehouse;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Warehouse createUpdatedEntity(EntityManager em) {
        Warehouse warehouse = new Warehouse().name(UPDATED_NAME).status(UPDATED_STATUS);
        return warehouse;
    }

    @BeforeEach
    public void initTest() {
        warehouse = createEntity(em);
    }

    @Test
    @Transactional
    void createWarehouse() throws Exception {
        int databaseSizeBeforeCreate = warehouseRepository.findAll().size();
        // Create the Warehouse
        WarehouseDTO warehouseDTO = warehouseMapper.toDto(warehouse);
        restWarehouseMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(warehouseDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Warehouse in the database
        List<Warehouse> warehouseList = warehouseRepository.findAll();
        assertThat(warehouseList).hasSize(databaseSizeBeforeCreate + 1);
        Warehouse testWarehouse = warehouseList.get(warehouseList.size() - 1);
        assertThat(testWarehouse.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testWarehouse.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void createWarehouseWithExistingId() throws Exception {
        // Create the Warehouse with an existing ID
        warehouse.setId(1L);
        WarehouseDTO warehouseDTO = warehouseMapper.toDto(warehouse);

        int databaseSizeBeforeCreate = warehouseRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restWarehouseMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(warehouseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Warehouse in the database
        List<Warehouse> warehouseList = warehouseRepository.findAll();
        assertThat(warehouseList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = warehouseRepository.findAll().size();
        // set the field null
        warehouse.setName(null);

        // Create the Warehouse, which fails.
        WarehouseDTO warehouseDTO = warehouseMapper.toDto(warehouse);

        restWarehouseMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(warehouseDTO))
            )
            .andExpect(status().isBadRequest());

        List<Warehouse> warehouseList = warehouseRepository.findAll();
        assertThat(warehouseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = warehouseRepository.findAll().size();
        // set the field null
        warehouse.setStatus(null);

        // Create the Warehouse, which fails.
        WarehouseDTO warehouseDTO = warehouseMapper.toDto(warehouse);

        restWarehouseMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(warehouseDTO))
            )
            .andExpect(status().isBadRequest());

        List<Warehouse> warehouseList = warehouseRepository.findAll();
        assertThat(warehouseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllWarehouses() throws Exception {
        // Initialize the database
        warehouseRepository.saveAndFlush(warehouse);

        // Get all the warehouseList
        restWarehouseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(warehouse.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    void getWarehouse() throws Exception {
        // Initialize the database
        warehouseRepository.saveAndFlush(warehouse);

        // Get the warehouse
        restWarehouseMockMvc
            .perform(get(ENTITY_API_URL_ID, warehouse.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(warehouse.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getWarehousesByIdFiltering() throws Exception {
        // Initialize the database
        warehouseRepository.saveAndFlush(warehouse);

        Long id = warehouse.getId();

        defaultWarehouseShouldBeFound("id.equals=" + id);
        defaultWarehouseShouldNotBeFound("id.notEquals=" + id);

        defaultWarehouseShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultWarehouseShouldNotBeFound("id.greaterThan=" + id);

        defaultWarehouseShouldBeFound("id.lessThanOrEqual=" + id);
        defaultWarehouseShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllWarehousesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        warehouseRepository.saveAndFlush(warehouse);

        // Get all the warehouseList where name equals to DEFAULT_NAME
        defaultWarehouseShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the warehouseList where name equals to UPDATED_NAME
        defaultWarehouseShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllWarehousesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        warehouseRepository.saveAndFlush(warehouse);

        // Get all the warehouseList where name in DEFAULT_NAME or UPDATED_NAME
        defaultWarehouseShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the warehouseList where name equals to UPDATED_NAME
        defaultWarehouseShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllWarehousesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        warehouseRepository.saveAndFlush(warehouse);

        // Get all the warehouseList where name is not null
        defaultWarehouseShouldBeFound("name.specified=true");

        // Get all the warehouseList where name is null
        defaultWarehouseShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllWarehousesByNameContainsSomething() throws Exception {
        // Initialize the database
        warehouseRepository.saveAndFlush(warehouse);

        // Get all the warehouseList where name contains DEFAULT_NAME
        defaultWarehouseShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the warehouseList where name contains UPDATED_NAME
        defaultWarehouseShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllWarehousesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        warehouseRepository.saveAndFlush(warehouse);

        // Get all the warehouseList where name does not contain DEFAULT_NAME
        defaultWarehouseShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the warehouseList where name does not contain UPDATED_NAME
        defaultWarehouseShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllWarehousesByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        warehouseRepository.saveAndFlush(warehouse);

        // Get all the warehouseList where status equals to DEFAULT_STATUS
        defaultWarehouseShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the warehouseList where status equals to UPDATED_STATUS
        defaultWarehouseShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllWarehousesByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        warehouseRepository.saveAndFlush(warehouse);

        // Get all the warehouseList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultWarehouseShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the warehouseList where status equals to UPDATED_STATUS
        defaultWarehouseShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllWarehousesByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        warehouseRepository.saveAndFlush(warehouse);

        // Get all the warehouseList where status is not null
        defaultWarehouseShouldBeFound("status.specified=true");

        // Get all the warehouseList where status is null
        defaultWarehouseShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    void getAllWarehousesByUsersIsEqualToSomething() throws Exception {
        Users users;
        if (TestUtil.findAll(em, Users.class).isEmpty()) {
            warehouseRepository.saveAndFlush(warehouse);
            users = UsersResourceIT.createEntity(em);
        } else {
            users = TestUtil.findAll(em, Users.class).get(0);
        }
        em.persist(users);
        em.flush();
        warehouse.addUsers(users);
        warehouseRepository.saveAndFlush(warehouse);
        Long usersId = users.getId();

        // Get all the warehouseList where users equals to usersId
        defaultWarehouseShouldBeFound("usersId.equals=" + usersId);

        // Get all the warehouseList where users equals to (usersId + 1)
        defaultWarehouseShouldNotBeFound("usersId.equals=" + (usersId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultWarehouseShouldBeFound(String filter) throws Exception {
        restWarehouseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(warehouse.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));

        // Check, that the count call also returns 1
        restWarehouseMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultWarehouseShouldNotBeFound(String filter) throws Exception {
        restWarehouseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restWarehouseMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingWarehouse() throws Exception {
        // Get the warehouse
        restWarehouseMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewWarehouse() throws Exception {
        // Initialize the database
        warehouseRepository.saveAndFlush(warehouse);

        int databaseSizeBeforeUpdate = warehouseRepository.findAll().size();

        // Update the warehouse
        Warehouse updatedWarehouse = warehouseRepository.findById(warehouse.getId()).get();
        // Disconnect from session so that the updates on updatedWarehouse are not directly saved in db
        em.detach(updatedWarehouse);
        updatedWarehouse.name(UPDATED_NAME).status(UPDATED_STATUS);
        WarehouseDTO warehouseDTO = warehouseMapper.toDto(updatedWarehouse);

        restWarehouseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, warehouseDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(warehouseDTO))
            )
            .andExpect(status().isOk());

        // Validate the Warehouse in the database
        List<Warehouse> warehouseList = warehouseRepository.findAll();
        assertThat(warehouseList).hasSize(databaseSizeBeforeUpdate);
        Warehouse testWarehouse = warehouseList.get(warehouseList.size() - 1);
        assertThat(testWarehouse.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testWarehouse.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void putNonExistingWarehouse() throws Exception {
        int databaseSizeBeforeUpdate = warehouseRepository.findAll().size();
        warehouse.setId(count.incrementAndGet());

        // Create the Warehouse
        WarehouseDTO warehouseDTO = warehouseMapper.toDto(warehouse);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWarehouseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, warehouseDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(warehouseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Warehouse in the database
        List<Warehouse> warehouseList = warehouseRepository.findAll();
        assertThat(warehouseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchWarehouse() throws Exception {
        int databaseSizeBeforeUpdate = warehouseRepository.findAll().size();
        warehouse.setId(count.incrementAndGet());

        // Create the Warehouse
        WarehouseDTO warehouseDTO = warehouseMapper.toDto(warehouse);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWarehouseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(warehouseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Warehouse in the database
        List<Warehouse> warehouseList = warehouseRepository.findAll();
        assertThat(warehouseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWarehouse() throws Exception {
        int databaseSizeBeforeUpdate = warehouseRepository.findAll().size();
        warehouse.setId(count.incrementAndGet());

        // Create the Warehouse
        WarehouseDTO warehouseDTO = warehouseMapper.toDto(warehouse);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWarehouseMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(warehouseDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Warehouse in the database
        List<Warehouse> warehouseList = warehouseRepository.findAll();
        assertThat(warehouseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateWarehouseWithPatch() throws Exception {
        // Initialize the database
        warehouseRepository.saveAndFlush(warehouse);

        int databaseSizeBeforeUpdate = warehouseRepository.findAll().size();

        // Update the warehouse using partial update
        Warehouse partialUpdatedWarehouse = new Warehouse();
        partialUpdatedWarehouse.setId(warehouse.getId());

        partialUpdatedWarehouse.status(UPDATED_STATUS);

        restWarehouseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWarehouse.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWarehouse))
            )
            .andExpect(status().isOk());

        // Validate the Warehouse in the database
        List<Warehouse> warehouseList = warehouseRepository.findAll();
        assertThat(warehouseList).hasSize(databaseSizeBeforeUpdate);
        Warehouse testWarehouse = warehouseList.get(warehouseList.size() - 1);
        assertThat(testWarehouse.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testWarehouse.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void fullUpdateWarehouseWithPatch() throws Exception {
        // Initialize the database
        warehouseRepository.saveAndFlush(warehouse);

        int databaseSizeBeforeUpdate = warehouseRepository.findAll().size();

        // Update the warehouse using partial update
        Warehouse partialUpdatedWarehouse = new Warehouse();
        partialUpdatedWarehouse.setId(warehouse.getId());

        partialUpdatedWarehouse.name(UPDATED_NAME).status(UPDATED_STATUS);

        restWarehouseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWarehouse.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWarehouse))
            )
            .andExpect(status().isOk());

        // Validate the Warehouse in the database
        List<Warehouse> warehouseList = warehouseRepository.findAll();
        assertThat(warehouseList).hasSize(databaseSizeBeforeUpdate);
        Warehouse testWarehouse = warehouseList.get(warehouseList.size() - 1);
        assertThat(testWarehouse.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testWarehouse.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingWarehouse() throws Exception {
        int databaseSizeBeforeUpdate = warehouseRepository.findAll().size();
        warehouse.setId(count.incrementAndGet());

        // Create the Warehouse
        WarehouseDTO warehouseDTO = warehouseMapper.toDto(warehouse);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWarehouseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, warehouseDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(warehouseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Warehouse in the database
        List<Warehouse> warehouseList = warehouseRepository.findAll();
        assertThat(warehouseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWarehouse() throws Exception {
        int databaseSizeBeforeUpdate = warehouseRepository.findAll().size();
        warehouse.setId(count.incrementAndGet());

        // Create the Warehouse
        WarehouseDTO warehouseDTO = warehouseMapper.toDto(warehouse);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWarehouseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(warehouseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Warehouse in the database
        List<Warehouse> warehouseList = warehouseRepository.findAll();
        assertThat(warehouseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWarehouse() throws Exception {
        int databaseSizeBeforeUpdate = warehouseRepository.findAll().size();
        warehouse.setId(count.incrementAndGet());

        // Create the Warehouse
        WarehouseDTO warehouseDTO = warehouseMapper.toDto(warehouse);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWarehouseMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(warehouseDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Warehouse in the database
        List<Warehouse> warehouseList = warehouseRepository.findAll();
        assertThat(warehouseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteWarehouse() throws Exception {
        // Initialize the database
        warehouseRepository.saveAndFlush(warehouse);

        int databaseSizeBeforeDelete = warehouseRepository.findAll().size();

        // Delete the warehouse
        restWarehouseMockMvc
            .perform(delete(ENTITY_API_URL_ID, warehouse.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Warehouse> warehouseList = warehouseRepository.findAll();
        assertThat(warehouseList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
