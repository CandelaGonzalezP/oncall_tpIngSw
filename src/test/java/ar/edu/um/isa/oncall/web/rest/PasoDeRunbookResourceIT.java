package ar.edu.um.isa.oncall.web.rest;

import static ar.edu.um.isa.oncall.domain.PasoDeRunbookAsserts.*;
import static ar.edu.um.isa.oncall.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ar.edu.um.isa.oncall.IntegrationTest;
import ar.edu.um.isa.oncall.domain.PasoDeRunbook;
import ar.edu.um.isa.oncall.domain.Runbook;
import ar.edu.um.isa.oncall.repository.PasoDeRunbookRepository;
import ar.edu.um.isa.oncall.service.PasoDeRunbookService;
import ar.edu.um.isa.oncall.service.dto.PasoDeRunbookDTO;
import ar.edu.um.isa.oncall.service.mapper.PasoDeRunbookMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link PasoDeRunbookResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PasoDeRunbookResourceIT {

    private static final Integer DEFAULT_ORDEN = 1;
    private static final Integer UPDATED_ORDEN = 2;

    private static final String DEFAULT_TITULO = "AAAAAAAAAA";
    private static final String UPDATED_TITULO = "BBBBBBBBBB";

    private static final String DEFAULT_INSTRUCCIONES = "AAAAAAAAAA";
    private static final String UPDATED_INSTRUCCIONES = "BBBBBBBBBB";

    private static final Boolean DEFAULT_OBLIGATORIO = false;
    private static final Boolean UPDATED_OBLIGATORIO = true;

    private static final String ENTITY_API_URL = "/api/paso-de-runbooks";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PasoDeRunbookRepository pasoDeRunbookRepository;

    @Mock
    private PasoDeRunbookRepository pasoDeRunbookRepositoryMock;

    @Autowired
    private PasoDeRunbookMapper pasoDeRunbookMapper;

    @Mock
    private PasoDeRunbookService pasoDeRunbookServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPasoDeRunbookMockMvc;

    private PasoDeRunbook pasoDeRunbook;

    private PasoDeRunbook insertedPasoDeRunbook;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PasoDeRunbook createEntity(EntityManager em) {
        PasoDeRunbook pasoDeRunbook = new PasoDeRunbook()
            .orden(DEFAULT_ORDEN)
            .titulo(DEFAULT_TITULO)
            .instrucciones(DEFAULT_INSTRUCCIONES)
            .obligatorio(DEFAULT_OBLIGATORIO);
        // Add required entity
        Runbook runbook;
        if (TestUtil.findAll(em, Runbook.class).isEmpty()) {
            runbook = RunbookResourceIT.createEntity();
            em.persist(runbook);
            em.flush();
        } else {
            runbook = TestUtil.findAll(em, Runbook.class).get(0);
        }
        pasoDeRunbook.setRunbook(runbook);
        return pasoDeRunbook;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PasoDeRunbook createUpdatedEntity(EntityManager em) {
        PasoDeRunbook updatedPasoDeRunbook = new PasoDeRunbook()
            .orden(UPDATED_ORDEN)
            .titulo(UPDATED_TITULO)
            .instrucciones(UPDATED_INSTRUCCIONES)
            .obligatorio(UPDATED_OBLIGATORIO);
        // Add required entity
        Runbook runbook;
        if (TestUtil.findAll(em, Runbook.class).isEmpty()) {
            runbook = RunbookResourceIT.createUpdatedEntity();
            em.persist(runbook);
            em.flush();
        } else {
            runbook = TestUtil.findAll(em, Runbook.class).get(0);
        }
        updatedPasoDeRunbook.setRunbook(runbook);
        return updatedPasoDeRunbook;
    }

    @BeforeEach
    void initTest() {
        pasoDeRunbook = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedPasoDeRunbook != null) {
            pasoDeRunbookRepository.delete(insertedPasoDeRunbook);
            insertedPasoDeRunbook = null;
        }
    }

    @Test
    @Transactional
    void createPasoDeRunbook() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the PasoDeRunbook
        PasoDeRunbookDTO pasoDeRunbookDTO = pasoDeRunbookMapper.toDto(pasoDeRunbook);
        var returnedPasoDeRunbookDTO = om.readValue(
            restPasoDeRunbookMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pasoDeRunbookDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PasoDeRunbookDTO.class
        );

        // Validate the PasoDeRunbook in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPasoDeRunbook = pasoDeRunbookMapper.toEntity(returnedPasoDeRunbookDTO);
        assertPasoDeRunbookUpdatableFieldsEquals(returnedPasoDeRunbook, getPersistedPasoDeRunbook(returnedPasoDeRunbook));

        insertedPasoDeRunbook = returnedPasoDeRunbook;
    }

    @Test
    @Transactional
    void createPasoDeRunbookWithExistingId() throws Exception {
        // Create the PasoDeRunbook with an existing ID
        pasoDeRunbook.setId(1L);
        PasoDeRunbookDTO pasoDeRunbookDTO = pasoDeRunbookMapper.toDto(pasoDeRunbook);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPasoDeRunbookMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pasoDeRunbookDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PasoDeRunbook in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkOrdenIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        pasoDeRunbook.setOrden(null);

        // Create the PasoDeRunbook, which fails.
        PasoDeRunbookDTO pasoDeRunbookDTO = pasoDeRunbookMapper.toDto(pasoDeRunbook);

        restPasoDeRunbookMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pasoDeRunbookDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTituloIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        pasoDeRunbook.setTitulo(null);

        // Create the PasoDeRunbook, which fails.
        PasoDeRunbookDTO pasoDeRunbookDTO = pasoDeRunbookMapper.toDto(pasoDeRunbook);

        restPasoDeRunbookMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pasoDeRunbookDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkInstruccionesIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        pasoDeRunbook.setInstrucciones(null);

        // Create the PasoDeRunbook, which fails.
        PasoDeRunbookDTO pasoDeRunbookDTO = pasoDeRunbookMapper.toDto(pasoDeRunbook);

        restPasoDeRunbookMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pasoDeRunbookDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkObligatorioIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        pasoDeRunbook.setObligatorio(null);

        // Create the PasoDeRunbook, which fails.
        PasoDeRunbookDTO pasoDeRunbookDTO = pasoDeRunbookMapper.toDto(pasoDeRunbook);

        restPasoDeRunbookMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pasoDeRunbookDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPasoDeRunbooks() throws Exception {
        // Initialize the database
        insertedPasoDeRunbook = pasoDeRunbookRepository.saveAndFlush(pasoDeRunbook);

        // Get all the pasoDeRunbookList
        restPasoDeRunbookMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pasoDeRunbook.getId().intValue())))
            .andExpect(jsonPath("$.[*].orden").value(hasItem(DEFAULT_ORDEN)))
            .andExpect(jsonPath("$.[*].titulo").value(hasItem(DEFAULT_TITULO)))
            .andExpect(jsonPath("$.[*].instrucciones").value(hasItem(DEFAULT_INSTRUCCIONES)))
            .andExpect(jsonPath("$.[*].obligatorio").value(hasItem(DEFAULT_OBLIGATORIO)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPasoDeRunbooksWithEagerRelationshipsIsEnabled() throws Exception {
        when(pasoDeRunbookServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPasoDeRunbookMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(pasoDeRunbookServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPasoDeRunbooksWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(pasoDeRunbookServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPasoDeRunbookMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(pasoDeRunbookRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getPasoDeRunbook() throws Exception {
        // Initialize the database
        insertedPasoDeRunbook = pasoDeRunbookRepository.saveAndFlush(pasoDeRunbook);

        // Get the pasoDeRunbook
        restPasoDeRunbookMockMvc
            .perform(get(ENTITY_API_URL_ID, pasoDeRunbook.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pasoDeRunbook.getId().intValue()))
            .andExpect(jsonPath("$.orden").value(DEFAULT_ORDEN))
            .andExpect(jsonPath("$.titulo").value(DEFAULT_TITULO))
            .andExpect(jsonPath("$.instrucciones").value(DEFAULT_INSTRUCCIONES))
            .andExpect(jsonPath("$.obligatorio").value(DEFAULT_OBLIGATORIO));
    }

    @Test
    @Transactional
    void getNonExistingPasoDeRunbook() throws Exception {
        // Get the pasoDeRunbook
        restPasoDeRunbookMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPasoDeRunbook() throws Exception {
        // Initialize the database
        insertedPasoDeRunbook = pasoDeRunbookRepository.saveAndFlush(pasoDeRunbook);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the pasoDeRunbook
        PasoDeRunbook updatedPasoDeRunbook = pasoDeRunbookRepository.findById(pasoDeRunbook.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPasoDeRunbook are not directly saved in db
        em.detach(updatedPasoDeRunbook);
        updatedPasoDeRunbook
            .orden(UPDATED_ORDEN)
            .titulo(UPDATED_TITULO)
            .instrucciones(UPDATED_INSTRUCCIONES)
            .obligatorio(UPDATED_OBLIGATORIO);
        PasoDeRunbookDTO pasoDeRunbookDTO = pasoDeRunbookMapper.toDto(updatedPasoDeRunbook);

        restPasoDeRunbookMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pasoDeRunbookDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(pasoDeRunbookDTO))
            )
            .andExpect(status().isOk());

        // Validate the PasoDeRunbook in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPasoDeRunbookToMatchAllProperties(updatedPasoDeRunbook);
    }

    @Test
    @Transactional
    void putNonExistingPasoDeRunbook() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pasoDeRunbook.setId(longCount.incrementAndGet());

        // Create the PasoDeRunbook
        PasoDeRunbookDTO pasoDeRunbookDTO = pasoDeRunbookMapper.toDto(pasoDeRunbook);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPasoDeRunbookMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pasoDeRunbookDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(pasoDeRunbookDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PasoDeRunbook in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPasoDeRunbook() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pasoDeRunbook.setId(longCount.incrementAndGet());

        // Create the PasoDeRunbook
        PasoDeRunbookDTO pasoDeRunbookDTO = pasoDeRunbookMapper.toDto(pasoDeRunbook);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPasoDeRunbookMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(pasoDeRunbookDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PasoDeRunbook in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPasoDeRunbook() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pasoDeRunbook.setId(longCount.incrementAndGet());

        // Create the PasoDeRunbook
        PasoDeRunbookDTO pasoDeRunbookDTO = pasoDeRunbookMapper.toDto(pasoDeRunbook);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPasoDeRunbookMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pasoDeRunbookDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PasoDeRunbook in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePasoDeRunbookWithPatch() throws Exception {
        // Initialize the database
        insertedPasoDeRunbook = pasoDeRunbookRepository.saveAndFlush(pasoDeRunbook);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the pasoDeRunbook using partial update
        PasoDeRunbook partialUpdatedPasoDeRunbook = new PasoDeRunbook();
        partialUpdatedPasoDeRunbook.setId(pasoDeRunbook.getId());

        partialUpdatedPasoDeRunbook.titulo(UPDATED_TITULO).instrucciones(UPDATED_INSTRUCCIONES);

        restPasoDeRunbookMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPasoDeRunbook.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPasoDeRunbook))
            )
            .andExpect(status().isOk());

        // Validate the PasoDeRunbook in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPasoDeRunbookUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPasoDeRunbook, pasoDeRunbook),
            getPersistedPasoDeRunbook(pasoDeRunbook)
        );
    }

    @Test
    @Transactional
    void fullUpdatePasoDeRunbookWithPatch() throws Exception {
        // Initialize the database
        insertedPasoDeRunbook = pasoDeRunbookRepository.saveAndFlush(pasoDeRunbook);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the pasoDeRunbook using partial update
        PasoDeRunbook partialUpdatedPasoDeRunbook = new PasoDeRunbook();
        partialUpdatedPasoDeRunbook.setId(pasoDeRunbook.getId());

        partialUpdatedPasoDeRunbook
            .orden(UPDATED_ORDEN)
            .titulo(UPDATED_TITULO)
            .instrucciones(UPDATED_INSTRUCCIONES)
            .obligatorio(UPDATED_OBLIGATORIO);

        restPasoDeRunbookMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPasoDeRunbook.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPasoDeRunbook))
            )
            .andExpect(status().isOk());

        // Validate the PasoDeRunbook in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPasoDeRunbookUpdatableFieldsEquals(partialUpdatedPasoDeRunbook, getPersistedPasoDeRunbook(partialUpdatedPasoDeRunbook));
    }

    @Test
    @Transactional
    void patchNonExistingPasoDeRunbook() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pasoDeRunbook.setId(longCount.incrementAndGet());

        // Create the PasoDeRunbook
        PasoDeRunbookDTO pasoDeRunbookDTO = pasoDeRunbookMapper.toDto(pasoDeRunbook);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPasoDeRunbookMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, pasoDeRunbookDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(pasoDeRunbookDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PasoDeRunbook in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPasoDeRunbook() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pasoDeRunbook.setId(longCount.incrementAndGet());

        // Create the PasoDeRunbook
        PasoDeRunbookDTO pasoDeRunbookDTO = pasoDeRunbookMapper.toDto(pasoDeRunbook);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPasoDeRunbookMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(pasoDeRunbookDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PasoDeRunbook in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPasoDeRunbook() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pasoDeRunbook.setId(longCount.incrementAndGet());

        // Create the PasoDeRunbook
        PasoDeRunbookDTO pasoDeRunbookDTO = pasoDeRunbookMapper.toDto(pasoDeRunbook);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPasoDeRunbookMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(pasoDeRunbookDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PasoDeRunbook in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePasoDeRunbook() throws Exception {
        // Initialize the database
        insertedPasoDeRunbook = pasoDeRunbookRepository.saveAndFlush(pasoDeRunbook);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the pasoDeRunbook
        restPasoDeRunbookMockMvc
            .perform(delete(ENTITY_API_URL_ID, pasoDeRunbook.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return pasoDeRunbookRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected PasoDeRunbook getPersistedPasoDeRunbook(PasoDeRunbook pasoDeRunbook) {
        return pasoDeRunbookRepository.findById(pasoDeRunbook.getId()).orElseThrow();
    }

    protected void assertPersistedPasoDeRunbookToMatchAllProperties(PasoDeRunbook expectedPasoDeRunbook) {
        assertPasoDeRunbookAllPropertiesEquals(expectedPasoDeRunbook, getPersistedPasoDeRunbook(expectedPasoDeRunbook));
    }

    protected void assertPersistedPasoDeRunbookToMatchUpdatableProperties(PasoDeRunbook expectedPasoDeRunbook) {
        assertPasoDeRunbookAllUpdatablePropertiesEquals(expectedPasoDeRunbook, getPersistedPasoDeRunbook(expectedPasoDeRunbook));
    }
}
