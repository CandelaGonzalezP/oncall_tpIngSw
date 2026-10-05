package ar.edu.um.isa.oncall.web.rest;

import static ar.edu.um.isa.oncall.domain.EjecucionDeRunbookAsserts.*;
import static ar.edu.um.isa.oncall.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ar.edu.um.isa.oncall.IntegrationTest;
import ar.edu.um.isa.oncall.domain.EjecucionDeRunbook;
import ar.edu.um.isa.oncall.domain.Incidente;
import ar.edu.um.isa.oncall.domain.Runbook;
import ar.edu.um.isa.oncall.domain.enumeration.EstadoEjecucion;
import ar.edu.um.isa.oncall.repository.EjecucionDeRunbookRepository;
import ar.edu.um.isa.oncall.repository.UserRepository;
import ar.edu.um.isa.oncall.service.EjecucionDeRunbookService;
import ar.edu.um.isa.oncall.service.dto.EjecucionDeRunbookDTO;
import ar.edu.um.isa.oncall.service.mapper.EjecucionDeRunbookMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
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
 * Integration tests for the {@link EjecucionDeRunbookResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class EjecucionDeRunbookResourceIT {

    private static final Instant DEFAULT_INICIADA_EN = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_INICIADA_EN = Instant.ofEpochMilli(1701729143509L);

    private static final Instant DEFAULT_FINALIZADA_EN = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FINALIZADA_EN = Instant.ofEpochMilli(1701729143509L);

    private static final EstadoEjecucion DEFAULT_ESTADO = EstadoEjecucion.EN_CURSO;
    private static final EstadoEjecucion UPDATED_ESTADO = EstadoEjecucion.COMPLETADA;

    private static final Integer DEFAULT_PASO_ACTUAL = 0;
    private static final Integer UPDATED_PASO_ACTUAL = 1;

    private static final String DEFAULT_MOTIVO_FALLA = "AAAAAAAAAA";
    private static final String UPDATED_MOTIVO_FALLA = "BBBBBBBBBB";

    private static final String DEFAULT_NOTAS = "AAAAAAAAAA";
    private static final String UPDATED_NOTAS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/ejecucion-de-runbooks";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private EjecucionDeRunbookRepository ejecucionDeRunbookRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private EjecucionDeRunbookRepository ejecucionDeRunbookRepositoryMock;

    @Autowired
    private EjecucionDeRunbookMapper ejecucionDeRunbookMapper;

    @Mock
    private EjecucionDeRunbookService ejecucionDeRunbookServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEjecucionDeRunbookMockMvc;

    private EjecucionDeRunbook ejecucionDeRunbook;

    private EjecucionDeRunbook insertedEjecucionDeRunbook;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EjecucionDeRunbook createEntity(EntityManager em) {
        EjecucionDeRunbook ejecucionDeRunbook = new EjecucionDeRunbook()
            .iniciadaEn(DEFAULT_INICIADA_EN)
            .finalizadaEn(DEFAULT_FINALIZADA_EN)
            .estado(DEFAULT_ESTADO)
            .pasoActual(DEFAULT_PASO_ACTUAL)
            .motivoFalla(DEFAULT_MOTIVO_FALLA)
            .notas(DEFAULT_NOTAS);
        // Add required entity
        Runbook runbook;
        if (TestUtil.findAll(em, Runbook.class).isEmpty()) {
            runbook = RunbookResourceIT.createEntity();
            em.persist(runbook);
            em.flush();
        } else {
            runbook = TestUtil.findAll(em, Runbook.class).get(0);
        }
        ejecucionDeRunbook.setRunbook(runbook);
        // Add required entity
        Incidente incidente;
        if (TestUtil.findAll(em, Incidente.class).isEmpty()) {
            incidente = IncidenteResourceIT.createEntity();
            em.persist(incidente);
            em.flush();
        } else {
            incidente = TestUtil.findAll(em, Incidente.class).get(0);
        }
        ejecucionDeRunbook.setIncidente(incidente);
        return ejecucionDeRunbook;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EjecucionDeRunbook createUpdatedEntity(EntityManager em) {
        EjecucionDeRunbook updatedEjecucionDeRunbook = new EjecucionDeRunbook()
            .iniciadaEn(UPDATED_INICIADA_EN)
            .finalizadaEn(UPDATED_FINALIZADA_EN)
            .estado(UPDATED_ESTADO)
            .pasoActual(UPDATED_PASO_ACTUAL)
            .motivoFalla(UPDATED_MOTIVO_FALLA)
            .notas(UPDATED_NOTAS);
        // Add required entity
        Runbook runbook;
        if (TestUtil.findAll(em, Runbook.class).isEmpty()) {
            runbook = RunbookResourceIT.createUpdatedEntity();
            em.persist(runbook);
            em.flush();
        } else {
            runbook = TestUtil.findAll(em, Runbook.class).get(0);
        }
        updatedEjecucionDeRunbook.setRunbook(runbook);
        // Add required entity
        Incidente incidente;
        if (TestUtil.findAll(em, Incidente.class).isEmpty()) {
            incidente = IncidenteResourceIT.createUpdatedEntity();
            em.persist(incidente);
            em.flush();
        } else {
            incidente = TestUtil.findAll(em, Incidente.class).get(0);
        }
        updatedEjecucionDeRunbook.setIncidente(incidente);
        return updatedEjecucionDeRunbook;
    }

    @BeforeEach
    void initTest() {
        ejecucionDeRunbook = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedEjecucionDeRunbook != null) {
            ejecucionDeRunbookRepository.delete(insertedEjecucionDeRunbook);
            insertedEjecucionDeRunbook = null;
        }
    }

    @Test
    @Transactional
    void createEjecucionDeRunbook() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the EjecucionDeRunbook
        EjecucionDeRunbookDTO ejecucionDeRunbookDTO = ejecucionDeRunbookMapper.toDto(ejecucionDeRunbook);
        var returnedEjecucionDeRunbookDTO = om.readValue(
            restEjecucionDeRunbookMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ejecucionDeRunbookDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            EjecucionDeRunbookDTO.class
        );

        // Validate the EjecucionDeRunbook in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedEjecucionDeRunbook = ejecucionDeRunbookMapper.toEntity(returnedEjecucionDeRunbookDTO);
        assertEjecucionDeRunbookUpdatableFieldsEquals(
            returnedEjecucionDeRunbook,
            getPersistedEjecucionDeRunbook(returnedEjecucionDeRunbook)
        );

        insertedEjecucionDeRunbook = returnedEjecucionDeRunbook;
    }

    @Test
    @Transactional
    void createEjecucionDeRunbookWithExistingId() throws Exception {
        // Create the EjecucionDeRunbook with an existing ID
        ejecucionDeRunbook.setId(1L);
        EjecucionDeRunbookDTO ejecucionDeRunbookDTO = ejecucionDeRunbookMapper.toDto(ejecucionDeRunbook);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEjecucionDeRunbookMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ejecucionDeRunbookDTO)))
            .andExpect(status().isBadRequest());

        // Validate the EjecucionDeRunbook in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkIniciadaEnIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        ejecucionDeRunbook.setIniciadaEn(null);

        // Create the EjecucionDeRunbook, which fails.
        EjecucionDeRunbookDTO ejecucionDeRunbookDTO = ejecucionDeRunbookMapper.toDto(ejecucionDeRunbook);

        restEjecucionDeRunbookMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ejecucionDeRunbookDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEstadoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        ejecucionDeRunbook.setEstado(null);

        // Create the EjecucionDeRunbook, which fails.
        EjecucionDeRunbookDTO ejecucionDeRunbookDTO = ejecucionDeRunbookMapper.toDto(ejecucionDeRunbook);

        restEjecucionDeRunbookMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ejecucionDeRunbookDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEjecucionDeRunbooks() throws Exception {
        // Initialize the database
        insertedEjecucionDeRunbook = ejecucionDeRunbookRepository.saveAndFlush(ejecucionDeRunbook);

        // Get all the ejecucionDeRunbookList
        restEjecucionDeRunbookMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ejecucionDeRunbook.getId().intValue())))
            .andExpect(jsonPath("$.[*].iniciadaEn").value(hasItem(DEFAULT_INICIADA_EN.toString())))
            .andExpect(jsonPath("$.[*].finalizadaEn").value(hasItem(DEFAULT_FINALIZADA_EN.toString())))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO.toString())))
            .andExpect(jsonPath("$.[*].pasoActual").value(hasItem(DEFAULT_PASO_ACTUAL)))
            .andExpect(jsonPath("$.[*].motivoFalla").value(hasItem(DEFAULT_MOTIVO_FALLA)))
            .andExpect(jsonPath("$.[*].notas").value(hasItem(DEFAULT_NOTAS)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllEjecucionDeRunbooksWithEagerRelationshipsIsEnabled() throws Exception {
        when(ejecucionDeRunbookServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restEjecucionDeRunbookMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(ejecucionDeRunbookServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllEjecucionDeRunbooksWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(ejecucionDeRunbookServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restEjecucionDeRunbookMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(ejecucionDeRunbookRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getEjecucionDeRunbook() throws Exception {
        // Initialize the database
        insertedEjecucionDeRunbook = ejecucionDeRunbookRepository.saveAndFlush(ejecucionDeRunbook);

        // Get the ejecucionDeRunbook
        restEjecucionDeRunbookMockMvc
            .perform(get(ENTITY_API_URL_ID, ejecucionDeRunbook.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ejecucionDeRunbook.getId().intValue()))
            .andExpect(jsonPath("$.iniciadaEn").value(DEFAULT_INICIADA_EN.toString()))
            .andExpect(jsonPath("$.finalizadaEn").value(DEFAULT_FINALIZADA_EN.toString()))
            .andExpect(jsonPath("$.estado").value(DEFAULT_ESTADO.toString()))
            .andExpect(jsonPath("$.pasoActual").value(DEFAULT_PASO_ACTUAL))
            .andExpect(jsonPath("$.motivoFalla").value(DEFAULT_MOTIVO_FALLA))
            .andExpect(jsonPath("$.notas").value(DEFAULT_NOTAS));
    }

    @Test
    @Transactional
    void getNonExistingEjecucionDeRunbook() throws Exception {
        // Get the ejecucionDeRunbook
        restEjecucionDeRunbookMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEjecucionDeRunbook() throws Exception {
        // Initialize the database
        insertedEjecucionDeRunbook = ejecucionDeRunbookRepository.saveAndFlush(ejecucionDeRunbook);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the ejecucionDeRunbook
        EjecucionDeRunbook updatedEjecucionDeRunbook = ejecucionDeRunbookRepository.findById(ejecucionDeRunbook.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEjecucionDeRunbook are not directly saved in db
        em.detach(updatedEjecucionDeRunbook);
        updatedEjecucionDeRunbook
            .iniciadaEn(UPDATED_INICIADA_EN)
            .finalizadaEn(UPDATED_FINALIZADA_EN)
            .estado(UPDATED_ESTADO)
            .pasoActual(UPDATED_PASO_ACTUAL)
            .motivoFalla(UPDATED_MOTIVO_FALLA)
            .notas(UPDATED_NOTAS);
        EjecucionDeRunbookDTO ejecucionDeRunbookDTO = ejecucionDeRunbookMapper.toDto(updatedEjecucionDeRunbook);

        restEjecucionDeRunbookMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ejecucionDeRunbookDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(ejecucionDeRunbookDTO))
            )
            .andExpect(status().isOk());

        // Validate the EjecucionDeRunbook in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedEjecucionDeRunbookToMatchAllProperties(updatedEjecucionDeRunbook);
    }

    @Test
    @Transactional
    void putNonExistingEjecucionDeRunbook() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ejecucionDeRunbook.setId(longCount.incrementAndGet());

        // Create the EjecucionDeRunbook
        EjecucionDeRunbookDTO ejecucionDeRunbookDTO = ejecucionDeRunbookMapper.toDto(ejecucionDeRunbook);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEjecucionDeRunbookMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ejecucionDeRunbookDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(ejecucionDeRunbookDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EjecucionDeRunbook in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEjecucionDeRunbook() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ejecucionDeRunbook.setId(longCount.incrementAndGet());

        // Create the EjecucionDeRunbook
        EjecucionDeRunbookDTO ejecucionDeRunbookDTO = ejecucionDeRunbookMapper.toDto(ejecucionDeRunbook);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEjecucionDeRunbookMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(ejecucionDeRunbookDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EjecucionDeRunbook in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEjecucionDeRunbook() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ejecucionDeRunbook.setId(longCount.incrementAndGet());

        // Create the EjecucionDeRunbook
        EjecucionDeRunbookDTO ejecucionDeRunbookDTO = ejecucionDeRunbookMapper.toDto(ejecucionDeRunbook);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEjecucionDeRunbookMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ejecucionDeRunbookDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the EjecucionDeRunbook in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEjecucionDeRunbookWithPatch() throws Exception {
        // Initialize the database
        insertedEjecucionDeRunbook = ejecucionDeRunbookRepository.saveAndFlush(ejecucionDeRunbook);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the ejecucionDeRunbook using partial update
        EjecucionDeRunbook partialUpdatedEjecucionDeRunbook = new EjecucionDeRunbook();
        partialUpdatedEjecucionDeRunbook.setId(ejecucionDeRunbook.getId());

        partialUpdatedEjecucionDeRunbook
            .iniciadaEn(UPDATED_INICIADA_EN)
            .finalizadaEn(UPDATED_FINALIZADA_EN)
            .estado(UPDATED_ESTADO)
            .notas(UPDATED_NOTAS);

        restEjecucionDeRunbookMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEjecucionDeRunbook.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEjecucionDeRunbook))
            )
            .andExpect(status().isOk());

        // Validate the EjecucionDeRunbook in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEjecucionDeRunbookUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedEjecucionDeRunbook, ejecucionDeRunbook),
            getPersistedEjecucionDeRunbook(ejecucionDeRunbook)
        );
    }

    @Test
    @Transactional
    void fullUpdateEjecucionDeRunbookWithPatch() throws Exception {
        // Initialize the database
        insertedEjecucionDeRunbook = ejecucionDeRunbookRepository.saveAndFlush(ejecucionDeRunbook);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the ejecucionDeRunbook using partial update
        EjecucionDeRunbook partialUpdatedEjecucionDeRunbook = new EjecucionDeRunbook();
        partialUpdatedEjecucionDeRunbook.setId(ejecucionDeRunbook.getId());

        partialUpdatedEjecucionDeRunbook
            .iniciadaEn(UPDATED_INICIADA_EN)
            .finalizadaEn(UPDATED_FINALIZADA_EN)
            .estado(UPDATED_ESTADO)
            .pasoActual(UPDATED_PASO_ACTUAL)
            .motivoFalla(UPDATED_MOTIVO_FALLA)
            .notas(UPDATED_NOTAS);

        restEjecucionDeRunbookMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEjecucionDeRunbook.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEjecucionDeRunbook))
            )
            .andExpect(status().isOk());

        // Validate the EjecucionDeRunbook in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEjecucionDeRunbookUpdatableFieldsEquals(
            partialUpdatedEjecucionDeRunbook,
            getPersistedEjecucionDeRunbook(partialUpdatedEjecucionDeRunbook)
        );
    }

    @Test
    @Transactional
    void patchNonExistingEjecucionDeRunbook() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ejecucionDeRunbook.setId(longCount.incrementAndGet());

        // Create the EjecucionDeRunbook
        EjecucionDeRunbookDTO ejecucionDeRunbookDTO = ejecucionDeRunbookMapper.toDto(ejecucionDeRunbook);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEjecucionDeRunbookMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ejecucionDeRunbookDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(ejecucionDeRunbookDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EjecucionDeRunbook in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEjecucionDeRunbook() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ejecucionDeRunbook.setId(longCount.incrementAndGet());

        // Create the EjecucionDeRunbook
        EjecucionDeRunbookDTO ejecucionDeRunbookDTO = ejecucionDeRunbookMapper.toDto(ejecucionDeRunbook);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEjecucionDeRunbookMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(ejecucionDeRunbookDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EjecucionDeRunbook in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEjecucionDeRunbook() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ejecucionDeRunbook.setId(longCount.incrementAndGet());

        // Create the EjecucionDeRunbook
        EjecucionDeRunbookDTO ejecucionDeRunbookDTO = ejecucionDeRunbookMapper.toDto(ejecucionDeRunbook);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEjecucionDeRunbookMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(ejecucionDeRunbookDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the EjecucionDeRunbook in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEjecucionDeRunbook() throws Exception {
        // Initialize the database
        insertedEjecucionDeRunbook = ejecucionDeRunbookRepository.saveAndFlush(ejecucionDeRunbook);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the ejecucionDeRunbook
        restEjecucionDeRunbookMockMvc
            .perform(delete(ENTITY_API_URL_ID, ejecucionDeRunbook.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return ejecucionDeRunbookRepository.count();
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

    protected EjecucionDeRunbook getPersistedEjecucionDeRunbook(EjecucionDeRunbook ejecucionDeRunbook) {
        return ejecucionDeRunbookRepository.findById(ejecucionDeRunbook.getId()).orElseThrow();
    }

    protected void assertPersistedEjecucionDeRunbookToMatchAllProperties(EjecucionDeRunbook expectedEjecucionDeRunbook) {
        assertEjecucionDeRunbookAllPropertiesEquals(expectedEjecucionDeRunbook, getPersistedEjecucionDeRunbook(expectedEjecucionDeRunbook));
    }

    protected void assertPersistedEjecucionDeRunbookToMatchUpdatableProperties(EjecucionDeRunbook expectedEjecucionDeRunbook) {
        assertEjecucionDeRunbookAllUpdatablePropertiesEquals(
            expectedEjecucionDeRunbook,
            getPersistedEjecucionDeRunbook(expectedEjecucionDeRunbook)
        );
    }
}
