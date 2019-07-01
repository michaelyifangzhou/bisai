package cn.bisai.web.rest;

import cn.bisai.Bisaiapp5App;
import cn.bisai.domain.Vedio;
import cn.bisai.repository.VedioRepository;
import cn.bisai.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;

import static cn.bisai.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@Link VedioResource} REST controller.
 */
@SpringBootTest(classes = Bisaiapp5App.class)
public class VedioResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ISPLAYING = false;
    private static final Boolean UPDATED_ISPLAYING = true;

    private static final Long DEFAULT_LENGTH = 1L;
    private static final Long UPDATED_LENGTH = 2L;

    @Autowired
    private VedioRepository vedioRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restVedioMockMvc;

    private Vedio vedio;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final VedioResource vedioResource = new VedioResource(vedioRepository);
        this.restVedioMockMvc = MockMvcBuilders.standaloneSetup(vedioResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vedio createEntity(EntityManager em) {
        Vedio vedio = new Vedio()
            .name(DEFAULT_NAME)
            .url(DEFAULT_URL)
            .isplaying(DEFAULT_ISPLAYING)
            .length(DEFAULT_LENGTH);
        return vedio;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vedio createUpdatedEntity(EntityManager em) {
        Vedio vedio = new Vedio()
            .name(UPDATED_NAME)
            .url(UPDATED_URL)
            .isplaying(UPDATED_ISPLAYING)
            .length(UPDATED_LENGTH);
        return vedio;
    }

    @BeforeEach
    public void initTest() {
        vedio = createEntity(em);
    }

    @Test
    @Transactional
    public void createVedio() throws Exception {
        int databaseSizeBeforeCreate = vedioRepository.findAll().size();

        // Create the Vedio
        restVedioMockMvc.perform(post("/api/vedios")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(vedio)))
            .andExpect(status().isCreated());

        // Validate the Vedio in the database
        List<Vedio> vedioList = vedioRepository.findAll();
        assertThat(vedioList).hasSize(databaseSizeBeforeCreate + 1);
        Vedio testVedio = vedioList.get(vedioList.size() - 1);
        assertThat(testVedio.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testVedio.getUrl()).isEqualTo(DEFAULT_URL);
        assertThat(testVedio.isIsplaying()).isEqualTo(DEFAULT_ISPLAYING);
        assertThat(testVedio.getLength()).isEqualTo(DEFAULT_LENGTH);
    }

    @Test
    @Transactional
    public void createVedioWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = vedioRepository.findAll().size();

        // Create the Vedio with an existing ID
        vedio.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restVedioMockMvc.perform(post("/api/vedios")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(vedio)))
            .andExpect(status().isBadRequest());

        // Validate the Vedio in the database
        List<Vedio> vedioList = vedioRepository.findAll();
        assertThat(vedioList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllVedios() throws Exception {
        // Initialize the database
        vedioRepository.saveAndFlush(vedio);

        // Get all the vedioList
        restVedioMockMvc.perform(get("/api/vedios?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vedio.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())))
            .andExpect(jsonPath("$.[*].isplaying").value(hasItem(DEFAULT_ISPLAYING.booleanValue())))
            .andExpect(jsonPath("$.[*].length").value(hasItem(DEFAULT_LENGTH.intValue())));
    }
    
    @Test
    @Transactional
    public void getVedio() throws Exception {
        // Initialize the database
        vedioRepository.saveAndFlush(vedio);

        // Get the vedio
        restVedioMockMvc.perform(get("/api/vedios/{id}", vedio.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(vedio.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL.toString()))
            .andExpect(jsonPath("$.isplaying").value(DEFAULT_ISPLAYING.booleanValue()))
            .andExpect(jsonPath("$.length").value(DEFAULT_LENGTH.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingVedio() throws Exception {
        // Get the vedio
        restVedioMockMvc.perform(get("/api/vedios/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateVedio() throws Exception {
        // Initialize the database
        vedioRepository.saveAndFlush(vedio);

        int databaseSizeBeforeUpdate = vedioRepository.findAll().size();

        // Update the vedio
        Vedio updatedVedio = vedioRepository.findById(vedio.getId()).get();
        // Disconnect from session so that the updates on updatedVedio are not directly saved in db
        em.detach(updatedVedio);
        updatedVedio
            .name(UPDATED_NAME)
            .url(UPDATED_URL)
            .isplaying(UPDATED_ISPLAYING)
            .length(UPDATED_LENGTH);

        restVedioMockMvc.perform(put("/api/vedios")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedVedio)))
            .andExpect(status().isOk());

        // Validate the Vedio in the database
        List<Vedio> vedioList = vedioRepository.findAll();
        assertThat(vedioList).hasSize(databaseSizeBeforeUpdate);
        Vedio testVedio = vedioList.get(vedioList.size() - 1);
        assertThat(testVedio.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testVedio.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testVedio.isIsplaying()).isEqualTo(UPDATED_ISPLAYING);
        assertThat(testVedio.getLength()).isEqualTo(UPDATED_LENGTH);
    }

    @Test
    @Transactional
    public void updateNonExistingVedio() throws Exception {
        int databaseSizeBeforeUpdate = vedioRepository.findAll().size();

        // Create the Vedio

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVedioMockMvc.perform(put("/api/vedios")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(vedio)))
            .andExpect(status().isBadRequest());

        // Validate the Vedio in the database
        List<Vedio> vedioList = vedioRepository.findAll();
        assertThat(vedioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteVedio() throws Exception {
        // Initialize the database
        vedioRepository.saveAndFlush(vedio);

        int databaseSizeBeforeDelete = vedioRepository.findAll().size();

        // Delete the vedio
        restVedioMockMvc.perform(delete("/api/vedios/{id}", vedio.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database is empty
        List<Vedio> vedioList = vedioRepository.findAll();
        assertThat(vedioList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Vedio.class);
        Vedio vedio1 = new Vedio();
        vedio1.setId(1L);
        Vedio vedio2 = new Vedio();
        vedio2.setId(vedio1.getId());
        assertThat(vedio1).isEqualTo(vedio2);
        vedio2.setId(2L);
        assertThat(vedio1).isNotEqualTo(vedio2);
        vedio1.setId(null);
        assertThat(vedio1).isNotEqualTo(vedio2);
    }
}
