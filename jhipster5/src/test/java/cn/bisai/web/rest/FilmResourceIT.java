package cn.bisai.web.rest;

import cn.bisai.Bisaiapp5App;
import cn.bisai.domain.Film;
import cn.bisai.repository.FilmRepository;
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
 * Integration tests for the {@Link FilmResource} REST controller.
 */
@SpringBootTest(classes = Bisaiapp5App.class)
public class FilmResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    private static final Long DEFAULT_LENGTH = 1L;
    private static final Long UPDATED_LENGTH = 2L;

    private static final Boolean DEFAULT_PLAYING = false;
    private static final Boolean UPDATED_PLAYING = true;

    private static final Long DEFAULT_CURTIME = 1L;
    private static final Long UPDATED_CURTIME = 2L;

    @Autowired
    private FilmRepository filmRepository;

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

    private MockMvc restFilmMockMvc;

    private Film film;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final FilmResource filmResource = new FilmResource(filmRepository);
        this.restFilmMockMvc = MockMvcBuilders.standaloneSetup(filmResource)
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
    public static Film createEntity(EntityManager em) {
        Film film = new Film()
            .name(DEFAULT_NAME)
            .url(DEFAULT_URL)
            .length(DEFAULT_LENGTH)
            .playing(DEFAULT_PLAYING)
            .curtime(DEFAULT_CURTIME);
        return film;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Film createUpdatedEntity(EntityManager em) {
        Film film = new Film()
            .name(UPDATED_NAME)
            .url(UPDATED_URL)
            .length(UPDATED_LENGTH)
            .playing(UPDATED_PLAYING)
            .curtime(UPDATED_CURTIME);
        return film;
    }

    @BeforeEach
    public void initTest() {
        film = createEntity(em);
    }

    @Test
    @Transactional
    public void createFilm() throws Exception {
        int databaseSizeBeforeCreate = filmRepository.findAll().size();

        // Create the Film
        restFilmMockMvc.perform(post("/api/films")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(film)))
            .andExpect(status().isCreated());

        // Validate the Film in the database
        List<Film> filmList = filmRepository.findAll();
        assertThat(filmList).hasSize(databaseSizeBeforeCreate + 1);
        Film testFilm = filmList.get(filmList.size() - 1);
        assertThat(testFilm.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testFilm.getUrl()).isEqualTo(DEFAULT_URL);
        assertThat(testFilm.getLength()).isEqualTo(DEFAULT_LENGTH);
        assertThat(testFilm.isPlaying()).isEqualTo(DEFAULT_PLAYING);
        assertThat(testFilm.getCurtime()).isEqualTo(DEFAULT_CURTIME);
    }

    @Test
    @Transactional
    public void createFilmWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = filmRepository.findAll().size();

        // Create the Film with an existing ID
        film.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFilmMockMvc.perform(post("/api/films")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(film)))
            .andExpect(status().isBadRequest());

        // Validate the Film in the database
        List<Film> filmList = filmRepository.findAll();
        assertThat(filmList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllFilms() throws Exception {
        // Initialize the database
        filmRepository.saveAndFlush(film);

        // Get all the filmList
        restFilmMockMvc.perform(get("/api/films?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(film.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())))
            .andExpect(jsonPath("$.[*].length").value(hasItem(DEFAULT_LENGTH.intValue())))
            .andExpect(jsonPath("$.[*].playing").value(hasItem(DEFAULT_PLAYING.booleanValue())))
            .andExpect(jsonPath("$.[*].curtime").value(hasItem(DEFAULT_CURTIME.intValue())));
    }
    
    @Test
    @Transactional
    public void getFilm() throws Exception {
        // Initialize the database
        filmRepository.saveAndFlush(film);

        // Get the film
        restFilmMockMvc.perform(get("/api/films/{id}", film.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(film.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL.toString()))
            .andExpect(jsonPath("$.length").value(DEFAULT_LENGTH.intValue()))
            .andExpect(jsonPath("$.playing").value(DEFAULT_PLAYING.booleanValue()))
            .andExpect(jsonPath("$.curtime").value(DEFAULT_CURTIME.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingFilm() throws Exception {
        // Get the film
        restFilmMockMvc.perform(get("/api/films/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFilm() throws Exception {
        // Initialize the database
        filmRepository.saveAndFlush(film);

        int databaseSizeBeforeUpdate = filmRepository.findAll().size();

        // Update the film
        Film updatedFilm = filmRepository.findById(film.getId()).get();
        // Disconnect from session so that the updates on updatedFilm are not directly saved in db
        em.detach(updatedFilm);
        updatedFilm
            .name(UPDATED_NAME)
            .url(UPDATED_URL)
            .length(UPDATED_LENGTH)
            .playing(UPDATED_PLAYING)
            .curtime(UPDATED_CURTIME);

        restFilmMockMvc.perform(put("/api/films")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedFilm)))
            .andExpect(status().isOk());

        // Validate the Film in the database
        List<Film> filmList = filmRepository.findAll();
        assertThat(filmList).hasSize(databaseSizeBeforeUpdate);
        Film testFilm = filmList.get(filmList.size() - 1);
        assertThat(testFilm.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testFilm.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testFilm.getLength()).isEqualTo(UPDATED_LENGTH);
        assertThat(testFilm.isPlaying()).isEqualTo(UPDATED_PLAYING);
        assertThat(testFilm.getCurtime()).isEqualTo(UPDATED_CURTIME);
    }

    @Test
    @Transactional
    public void updateNonExistingFilm() throws Exception {
        int databaseSizeBeforeUpdate = filmRepository.findAll().size();

        // Create the Film

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFilmMockMvc.perform(put("/api/films")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(film)))
            .andExpect(status().isBadRequest());

        // Validate the Film in the database
        List<Film> filmList = filmRepository.findAll();
        assertThat(filmList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteFilm() throws Exception {
        // Initialize the database
        filmRepository.saveAndFlush(film);

        int databaseSizeBeforeDelete = filmRepository.findAll().size();

        // Delete the film
        restFilmMockMvc.perform(delete("/api/films/{id}", film.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database is empty
        List<Film> filmList = filmRepository.findAll();
        assertThat(filmList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Film.class);
        Film film1 = new Film();
        film1.setId(1L);
        Film film2 = new Film();
        film2.setId(film1.getId());
        assertThat(film1).isEqualTo(film2);
        film2.setId(2L);
        assertThat(film1).isNotEqualTo(film2);
        film1.setId(null);
        assertThat(film1).isNotEqualTo(film2);
    }
}
