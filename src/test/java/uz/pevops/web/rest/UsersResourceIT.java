package uz.pevops.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import uz.pevops.IntegrationTest;
import uz.pevops.domain.Users;
import uz.pevops.domain.Warehouse;
import uz.pevops.domain.enumeration.Status;
import uz.pevops.repository.UsersRepository;
import uz.pevops.service.UsersService;
import uz.pevops.service.criteria.UsersCriteria;
import uz.pevops.service.dto.UsersDTO;
import uz.pevops.service.mapper.UsersMapper;

/**
 * Integration tests for the {@link UsersResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class UsersResourceIT {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "+899152019859";
    private static final String UPDATED_PHONE_NUMBER = "+889260860795";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_PASSWORD = "BBBBBBBBBB";

    private static final Status DEFAULT_STATUS = Status.ACTIVE;
    private static final Status UPDATED_STATUS = Status.BLOCKED;

    private static final String ENTITY_API_URL = "/api/users";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UsersRepository usersRepository;

    @Mock
    private UsersRepository usersRepositoryMock;

    @Autowired
    private UsersMapper usersMapper;

    @Mock
    private UsersService usersServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUsersMockMvc;

    private Users users;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Users createEntity(EntityManager em) {
        Users users = new Users()
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .phoneNumber(DEFAULT_PHONE_NUMBER)
            .code(DEFAULT_CODE)
            .password(DEFAULT_PASSWORD)
            .status(DEFAULT_STATUS);
        return users;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Users createUpdatedEntity(EntityManager em) {
        Users users = new Users()
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .code(UPDATED_CODE)
            .password(UPDATED_PASSWORD)
            .status(UPDATED_STATUS);
        return users;
    }

    @BeforeEach
    public void initTest() {
        users = createEntity(em);
    }

    @Test
    @Transactional
    void createUsers() throws Exception {
        int databaseSizeBeforeCreate = usersRepository.findAll().size();
        // Create the Users
        UsersDTO usersDTO = usersMapper.toDto(users);
        restUsersMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(usersDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Users in the database
        List<Users> usersList = usersRepository.findAll();
        assertThat(usersList).hasSize(databaseSizeBeforeCreate + 1);
        Users testUsers = usersList.get(usersList.size() - 1);
        assertThat(testUsers.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testUsers.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testUsers.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testUsers.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testUsers.getPassword()).isEqualTo(DEFAULT_PASSWORD);
        assertThat(testUsers.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void createUsersWithExistingId() throws Exception {
        // Create the Users with an existing ID
        users.setId(1L);
        UsersDTO usersDTO = usersMapper.toDto(users);

        int databaseSizeBeforeCreate = usersRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUsersMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(usersDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Users in the database
        List<Users> usersList = usersRepository.findAll();
        assertThat(usersList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFirstNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = usersRepository.findAll().size();
        // set the field null
        users.setFirstName(null);

        // Create the Users, which fails.
        UsersDTO usersDTO = usersMapper.toDto(users);

        restUsersMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(usersDTO))
            )
            .andExpect(status().isBadRequest());

        List<Users> usersList = usersRepository.findAll();
        assertThat(usersList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = usersRepository.findAll().size();
        // set the field null
        users.setLastName(null);

        // Create the Users, which fails.
        UsersDTO usersDTO = usersMapper.toDto(users);

        restUsersMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(usersDTO))
            )
            .andExpect(status().isBadRequest());

        List<Users> usersList = usersRepository.findAll();
        assertThat(usersList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPhoneNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = usersRepository.findAll().size();
        // set the field null
        users.setPhoneNumber(null);

        // Create the Users, which fails.
        UsersDTO usersDTO = usersMapper.toDto(users);

        restUsersMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(usersDTO))
            )
            .andExpect(status().isBadRequest());

        List<Users> usersList = usersRepository.findAll();
        assertThat(usersList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = usersRepository.findAll().size();
        // set the field null
        users.setCode(null);

        // Create the Users, which fails.
        UsersDTO usersDTO = usersMapper.toDto(users);

        restUsersMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(usersDTO))
            )
            .andExpect(status().isBadRequest());

        List<Users> usersList = usersRepository.findAll();
        assertThat(usersList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPasswordIsRequired() throws Exception {
        int databaseSizeBeforeTest = usersRepository.findAll().size();
        // set the field null
        users.setPassword(null);

        // Create the Users, which fails.
        UsersDTO usersDTO = usersMapper.toDto(users);

        restUsersMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(usersDTO))
            )
            .andExpect(status().isBadRequest());

        List<Users> usersList = usersRepository.findAll();
        assertThat(usersList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllUsers() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList
        restUsersMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(users.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllUsersWithEagerRelationshipsIsEnabled() throws Exception {
        when(usersServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restUsersMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(usersServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllUsersWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(usersServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restUsersMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(usersRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getUsers() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get the users
        restUsersMockMvc
            .perform(get(ENTITY_API_URL_ID, users.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(users.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.password").value(DEFAULT_PASSWORD))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getUsersByIdFiltering() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        Long id = users.getId();

        defaultUsersShouldBeFound("id.equals=" + id);
        defaultUsersShouldNotBeFound("id.notEquals=" + id);

        defaultUsersShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultUsersShouldNotBeFound("id.greaterThan=" + id);

        defaultUsersShouldBeFound("id.lessThanOrEqual=" + id);
        defaultUsersShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllUsersByFirstNameIsEqualToSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where firstName equals to DEFAULT_FIRST_NAME
        defaultUsersShouldBeFound("firstName.equals=" + DEFAULT_FIRST_NAME);

        // Get all the usersList where firstName equals to UPDATED_FIRST_NAME
        defaultUsersShouldNotBeFound("firstName.equals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllUsersByFirstNameIsInShouldWork() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where firstName in DEFAULT_FIRST_NAME or UPDATED_FIRST_NAME
        defaultUsersShouldBeFound("firstName.in=" + DEFAULT_FIRST_NAME + "," + UPDATED_FIRST_NAME);

        // Get all the usersList where firstName equals to UPDATED_FIRST_NAME
        defaultUsersShouldNotBeFound("firstName.in=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllUsersByFirstNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where firstName is not null
        defaultUsersShouldBeFound("firstName.specified=true");

        // Get all the usersList where firstName is null
        defaultUsersShouldNotBeFound("firstName.specified=false");
    }

    @Test
    @Transactional
    void getAllUsersByFirstNameContainsSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where firstName contains DEFAULT_FIRST_NAME
        defaultUsersShouldBeFound("firstName.contains=" + DEFAULT_FIRST_NAME);

        // Get all the usersList where firstName contains UPDATED_FIRST_NAME
        defaultUsersShouldNotBeFound("firstName.contains=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllUsersByFirstNameNotContainsSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where firstName does not contain DEFAULT_FIRST_NAME
        defaultUsersShouldNotBeFound("firstName.doesNotContain=" + DEFAULT_FIRST_NAME);

        // Get all the usersList where firstName does not contain UPDATED_FIRST_NAME
        defaultUsersShouldBeFound("firstName.doesNotContain=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllUsersByLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where lastName equals to DEFAULT_LAST_NAME
        defaultUsersShouldBeFound("lastName.equals=" + DEFAULT_LAST_NAME);

        // Get all the usersList where lastName equals to UPDATED_LAST_NAME
        defaultUsersShouldNotBeFound("lastName.equals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllUsersByLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where lastName in DEFAULT_LAST_NAME or UPDATED_LAST_NAME
        defaultUsersShouldBeFound("lastName.in=" + DEFAULT_LAST_NAME + "," + UPDATED_LAST_NAME);

        // Get all the usersList where lastName equals to UPDATED_LAST_NAME
        defaultUsersShouldNotBeFound("lastName.in=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllUsersByLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where lastName is not null
        defaultUsersShouldBeFound("lastName.specified=true");

        // Get all the usersList where lastName is null
        defaultUsersShouldNotBeFound("lastName.specified=false");
    }

    @Test
    @Transactional
    void getAllUsersByLastNameContainsSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where lastName contains DEFAULT_LAST_NAME
        defaultUsersShouldBeFound("lastName.contains=" + DEFAULT_LAST_NAME);

        // Get all the usersList where lastName contains UPDATED_LAST_NAME
        defaultUsersShouldNotBeFound("lastName.contains=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllUsersByLastNameNotContainsSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where lastName does not contain DEFAULT_LAST_NAME
        defaultUsersShouldNotBeFound("lastName.doesNotContain=" + DEFAULT_LAST_NAME);

        // Get all the usersList where lastName does not contain UPDATED_LAST_NAME
        defaultUsersShouldBeFound("lastName.doesNotContain=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllUsersByPhoneNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where phoneNumber equals to DEFAULT_PHONE_NUMBER
        defaultUsersShouldBeFound("phoneNumber.equals=" + DEFAULT_PHONE_NUMBER);

        // Get all the usersList where phoneNumber equals to UPDATED_PHONE_NUMBER
        defaultUsersShouldNotBeFound("phoneNumber.equals=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllUsersByPhoneNumberIsInShouldWork() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where phoneNumber in DEFAULT_PHONE_NUMBER or UPDATED_PHONE_NUMBER
        defaultUsersShouldBeFound("phoneNumber.in=" + DEFAULT_PHONE_NUMBER + "," + UPDATED_PHONE_NUMBER);

        // Get all the usersList where phoneNumber equals to UPDATED_PHONE_NUMBER
        defaultUsersShouldNotBeFound("phoneNumber.in=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllUsersByPhoneNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where phoneNumber is not null
        defaultUsersShouldBeFound("phoneNumber.specified=true");

        // Get all the usersList where phoneNumber is null
        defaultUsersShouldNotBeFound("phoneNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllUsersByPhoneNumberContainsSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where phoneNumber contains DEFAULT_PHONE_NUMBER
        defaultUsersShouldBeFound("phoneNumber.contains=" + DEFAULT_PHONE_NUMBER);

        // Get all the usersList where phoneNumber contains UPDATED_PHONE_NUMBER
        defaultUsersShouldNotBeFound("phoneNumber.contains=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllUsersByPhoneNumberNotContainsSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where phoneNumber does not contain DEFAULT_PHONE_NUMBER
        defaultUsersShouldNotBeFound("phoneNumber.doesNotContain=" + DEFAULT_PHONE_NUMBER);

        // Get all the usersList where phoneNumber does not contain UPDATED_PHONE_NUMBER
        defaultUsersShouldBeFound("phoneNumber.doesNotContain=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllUsersByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where code equals to DEFAULT_CODE
        defaultUsersShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the usersList where code equals to UPDATED_CODE
        defaultUsersShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllUsersByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where code in DEFAULT_CODE or UPDATED_CODE
        defaultUsersShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the usersList where code equals to UPDATED_CODE
        defaultUsersShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllUsersByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where code is not null
        defaultUsersShouldBeFound("code.specified=true");

        // Get all the usersList where code is null
        defaultUsersShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    void getAllUsersByCodeContainsSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where code contains DEFAULT_CODE
        defaultUsersShouldBeFound("code.contains=" + DEFAULT_CODE);

        // Get all the usersList where code contains UPDATED_CODE
        defaultUsersShouldNotBeFound("code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllUsersByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where code does not contain DEFAULT_CODE
        defaultUsersShouldNotBeFound("code.doesNotContain=" + DEFAULT_CODE);

        // Get all the usersList where code does not contain UPDATED_CODE
        defaultUsersShouldBeFound("code.doesNotContain=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllUsersByPasswordIsEqualToSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where password equals to DEFAULT_PASSWORD
        defaultUsersShouldBeFound("password.equals=" + DEFAULT_PASSWORD);

        // Get all the usersList where password equals to UPDATED_PASSWORD
        defaultUsersShouldNotBeFound("password.equals=" + UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    void getAllUsersByPasswordIsInShouldWork() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where password in DEFAULT_PASSWORD or UPDATED_PASSWORD
        defaultUsersShouldBeFound("password.in=" + DEFAULT_PASSWORD + "," + UPDATED_PASSWORD);

        // Get all the usersList where password equals to UPDATED_PASSWORD
        defaultUsersShouldNotBeFound("password.in=" + UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    void getAllUsersByPasswordIsNullOrNotNull() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where password is not null
        defaultUsersShouldBeFound("password.specified=true");

        // Get all the usersList where password is null
        defaultUsersShouldNotBeFound("password.specified=false");
    }

    @Test
    @Transactional
    void getAllUsersByPasswordContainsSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where password contains DEFAULT_PASSWORD
        defaultUsersShouldBeFound("password.contains=" + DEFAULT_PASSWORD);

        // Get all the usersList where password contains UPDATED_PASSWORD
        defaultUsersShouldNotBeFound("password.contains=" + UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    void getAllUsersByPasswordNotContainsSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where password does not contain DEFAULT_PASSWORD
        defaultUsersShouldNotBeFound("password.doesNotContain=" + DEFAULT_PASSWORD);

        // Get all the usersList where password does not contain UPDATED_PASSWORD
        defaultUsersShouldBeFound("password.doesNotContain=" + UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    void getAllUsersByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where status equals to DEFAULT_STATUS
        defaultUsersShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the usersList where status equals to UPDATED_STATUS
        defaultUsersShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllUsersByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultUsersShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the usersList where status equals to UPDATED_STATUS
        defaultUsersShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllUsersByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where status is not null
        defaultUsersShouldBeFound("status.specified=true");

        // Get all the usersList where status is null
        defaultUsersShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    void getAllUsersByWarehouseIsEqualToSomething() throws Exception {
        Warehouse warehouse;
        if (TestUtil.findAll(em, Warehouse.class).isEmpty()) {
            usersRepository.saveAndFlush(users);
            warehouse = WarehouseResourceIT.createEntity(em);
        } else {
            warehouse = TestUtil.findAll(em, Warehouse.class).get(0);
        }
        em.persist(warehouse);
        em.flush();
        users.addWarehouse(warehouse);
        usersRepository.saveAndFlush(users);
        Long warehouseId = warehouse.getId();

        // Get all the usersList where warehouse equals to warehouseId
        defaultUsersShouldBeFound("warehouseId.equals=" + warehouseId);

        // Get all the usersList where warehouse equals to (warehouseId + 1)
        defaultUsersShouldNotBeFound("warehouseId.equals=" + (warehouseId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultUsersShouldBeFound(String filter) throws Exception {
        restUsersMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(users.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));

        // Check, that the count call also returns 1
        restUsersMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultUsersShouldNotBeFound(String filter) throws Exception {
        restUsersMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restUsersMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingUsers() throws Exception {
        // Get the users
        restUsersMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewUsers() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        int databaseSizeBeforeUpdate = usersRepository.findAll().size();

        // Update the users
        Users updatedUsers = usersRepository.findById(users.getId()).get();
        // Disconnect from session so that the updates on updatedUsers are not directly saved in db
        em.detach(updatedUsers);
        updatedUsers
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .code(UPDATED_CODE)
            .password(UPDATED_PASSWORD)
            .status(UPDATED_STATUS);
        UsersDTO usersDTO = usersMapper.toDto(updatedUsers);

        restUsersMockMvc
            .perform(
                put(ENTITY_API_URL_ID, usersDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(usersDTO))
            )
            .andExpect(status().isOk());

        // Validate the Users in the database
        List<Users> usersList = usersRepository.findAll();
        assertThat(usersList).hasSize(databaseSizeBeforeUpdate);
        Users testUsers = usersList.get(usersList.size() - 1);
        assertThat(testUsers.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testUsers.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testUsers.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testUsers.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testUsers.getPassword()).isEqualTo(UPDATED_PASSWORD);
        assertThat(testUsers.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void putNonExistingUsers() throws Exception {
        int databaseSizeBeforeUpdate = usersRepository.findAll().size();
        users.setId(count.incrementAndGet());

        // Create the Users
        UsersDTO usersDTO = usersMapper.toDto(users);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUsersMockMvc
            .perform(
                put(ENTITY_API_URL_ID, usersDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(usersDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Users in the database
        List<Users> usersList = usersRepository.findAll();
        assertThat(usersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUsers() throws Exception {
        int databaseSizeBeforeUpdate = usersRepository.findAll().size();
        users.setId(count.incrementAndGet());

        // Create the Users
        UsersDTO usersDTO = usersMapper.toDto(users);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUsersMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(usersDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Users in the database
        List<Users> usersList = usersRepository.findAll();
        assertThat(usersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUsers() throws Exception {
        int databaseSizeBeforeUpdate = usersRepository.findAll().size();
        users.setId(count.incrementAndGet());

        // Create the Users
        UsersDTO usersDTO = usersMapper.toDto(users);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUsersMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(usersDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Users in the database
        List<Users> usersList = usersRepository.findAll();
        assertThat(usersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUsersWithPatch() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        int databaseSizeBeforeUpdate = usersRepository.findAll().size();

        // Update the users using partial update
        Users partialUpdatedUsers = new Users();
        partialUpdatedUsers.setId(users.getId());

        partialUpdatedUsers.phoneNumber(UPDATED_PHONE_NUMBER).code(UPDATED_CODE).status(UPDATED_STATUS);

        restUsersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUsers.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUsers))
            )
            .andExpect(status().isOk());

        // Validate the Users in the database
        List<Users> usersList = usersRepository.findAll();
        assertThat(usersList).hasSize(databaseSizeBeforeUpdate);
        Users testUsers = usersList.get(usersList.size() - 1);
        assertThat(testUsers.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testUsers.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testUsers.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testUsers.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testUsers.getPassword()).isEqualTo(DEFAULT_PASSWORD);
        assertThat(testUsers.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void fullUpdateUsersWithPatch() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        int databaseSizeBeforeUpdate = usersRepository.findAll().size();

        // Update the users using partial update
        Users partialUpdatedUsers = new Users();
        partialUpdatedUsers.setId(users.getId());

        partialUpdatedUsers
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .code(UPDATED_CODE)
            .password(UPDATED_PASSWORD)
            .status(UPDATED_STATUS);

        restUsersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUsers.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUsers))
            )
            .andExpect(status().isOk());

        // Validate the Users in the database
        List<Users> usersList = usersRepository.findAll();
        assertThat(usersList).hasSize(databaseSizeBeforeUpdate);
        Users testUsers = usersList.get(usersList.size() - 1);
        assertThat(testUsers.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testUsers.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testUsers.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testUsers.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testUsers.getPassword()).isEqualTo(UPDATED_PASSWORD);
        assertThat(testUsers.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingUsers() throws Exception {
        int databaseSizeBeforeUpdate = usersRepository.findAll().size();
        users.setId(count.incrementAndGet());

        // Create the Users
        UsersDTO usersDTO = usersMapper.toDto(users);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUsersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, usersDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(usersDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Users in the database
        List<Users> usersList = usersRepository.findAll();
        assertThat(usersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUsers() throws Exception {
        int databaseSizeBeforeUpdate = usersRepository.findAll().size();
        users.setId(count.incrementAndGet());

        // Create the Users
        UsersDTO usersDTO = usersMapper.toDto(users);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUsersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(usersDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Users in the database
        List<Users> usersList = usersRepository.findAll();
        assertThat(usersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUsers() throws Exception {
        int databaseSizeBeforeUpdate = usersRepository.findAll().size();
        users.setId(count.incrementAndGet());

        // Create the Users
        UsersDTO usersDTO = usersMapper.toDto(users);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUsersMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(usersDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Users in the database
        List<Users> usersList = usersRepository.findAll();
        assertThat(usersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUsers() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        int databaseSizeBeforeDelete = usersRepository.findAll().size();

        // Delete the users
        restUsersMockMvc
            .perform(delete(ENTITY_API_URL_ID, users.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Users> usersList = usersRepository.findAll();
        assertThat(usersList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
