package ar.edu.um.isa.oncall.web.rest;

import static ar.edu.um.isa.oncall.domain.RunbookAsserts.*;
import static ar.edu.um.isa.oncall.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ar.edu.um.isa.oncall.IntegrationTest;
import ar.edu.um.isa.oncall.domain.Runbook;
import ar.edu.um.isa.oncall.domain.enumeration.OrigenAlerta;
import ar.edu.um.isa.oncall.domain.enumeration.Severidad;
import ar.edu.um.isa.oncall.repository.RunbookRepository;
import ar.edu.um.isa.oncall.service.RunbookService;
import ar.edu.um.isa.oncall.service.dto.RunbookDTO;
import ar.edu.um.isa.oncall.service.mapper.RunbookMapper;
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
 * Integration tests for the {@link RunbookResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class RunbookResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final OrigenAlerta DEFAULT_ORIGEN_ALERTA = OrigenAlerta.PROMETHEUS;
    private static final OrigenAlerta UPDATED_ORIGEN_ALERTA = OrigenAlerta.DATADOG;

    private static final Severidad DEFAULT_SEVERIDAD_MINIMA = Severidad.SEV1;
    private static final Severidad UPDATED_SEVERIDAD_MINIMA = Severidad.SEV2;

    private static final String DEFAULT_PATRON_FINGERPRINT = "AAAAAAAAAA";
    private static final String UPDATED_PATRON_FINGERPRINT = "BBBBBBBBBB";

    private static final Integer DEFAULT_TIEMPO_MAX_MINUTOS = 1;
    private static final Integer UPDATED_TIEMPO_MAX_MINUTOS = 2;

    private static final Boolean DEFAULT_ACTIVO = false;
    private static final Boolean UPDATED_ACTIVO = true;

    private static final String ENTITY_API_URL = "/api/runbooks";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private RunbookRepository runbookRepository;

    @Mock
    private RunbookRepository runbookRepositoryMock;

    @Autowired
    private RunbookMapper runbookMapper;

    @Mock
    private RunbookService runbookServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRunbookMockMvc;

    private Runbook runbook;

    private Runbook insertedRunbook;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Runbook createEntity() {
        return new Runbook()
            .nombre(DEFAULT_NOMBRE)
            .descripcion(DEFAULT_DESCRIPCION)
            .origenAlerta(DEFAULT_ORIGEN_ALERTA)
            .severidadMinima(DEFAULT_SEVERIDAD_MINIMA)
            .patronFingerprint(DEFAULT_PATRON_FINGERPRINT)
            .tiempoMaxMinutos(DEFAULT_TIEMPO_MAX_MINUTOS)
            .activo(DEFAULT_ACTIVO);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Runbook createUpdatedEntity() {
        return new Runbook()
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .origenAlerta(UPDATED_ORIGEN_ALERTA)
            .severidadMinima(UPDATED_SEVERIDAD_MINIMA)
            .patronFingerprint(UPDATED_PATRON_FINGERPRINT)
            .tiempoMaxMinutos(UPDATED_TIEMPO_MAX_MINUTOS)
            .activo(UPDATED_ACTIVO);
    }

    @BeforeEach
    void initTest() {
        runbook = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedRunbook != null) {
            runbookRepository.delete(insertedRunbook);
            insertedRunbook = null;
        }
    }

    @Test
    @Transactional
    void createRunbook() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Runbook
        RunbookDTO runbookDTO = runbookMapper.toDto(runbook);
        var returnedRunbookDTO = om.readValue(
            restRunbookMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(runbookDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            RunbookDTO.class
        );

        // Validate the Runbook in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedRunbook = runbookMapper.toEntity(returnedRunbookDTO);
        assertRunbookUpdatableFieldsEquals(returnedRunbook, getPersistedRunbook(returnedRunbook));

        insertedRunbook = returnedRunbook;
    }

    @Test
    @Transactional
    void createRunbookWithExistingId() throws Exception {
        // Create the Runbook with an existing ID
        runbook.setId(1L);
        RunbookDTO runbookDTO = runbookMapper.toDto(runbook);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRunbookMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(runbookDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Runbook in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNombreIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        runbook.setNombre(null);

        // Create the Runbook, which fails.
        RunbookDTO runbookDTO = runbookMapper.toDto(runbook);

        restRunbookMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(runbookDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActivoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        runbook.setActivo(null);

        // Create the Runbook, which fails.
        RunbookDTO runbookDTO = runbookMapper.toDto(runbook);

        restRunbookMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(runbookDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRunbooks() throws Exception {
        // Initialize the database
        insertedRunbook = runbookRepository.saveAndFlush(runbook);

        // Get all the runbookList
        restRunbookMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(runbook.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].origenAlerta").value(hasItem(DEFAULT_ORIGEN_ALERTA.toString())))
            .andExpect(jsonPath("$.[*].severidadMinima").value(hasItem(DEFAULT_SEVERIDAD_MINIMA.toString())))
            .andExpect(jsonPath("$.[*].patronFingerprint").value(hasItem(DEFAULT_PATRON_FINGERPRINT)))
            .andExpect(jsonPath("$.[*].tiempoMaxMinutos").value(hasItem(DEFAULT_TIEMPO_MAX_MINUTOS)))
            .andExpect(jsonPath("$.[*].activo").value(hasItem(DEFAULT_ACTIVO)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllRunbooksWithEagerRelationshipsIsEnabled() throws Exception {
        when(runbookServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restRunbookMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(runbookServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllRunbooksWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(runbookServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restRunbookMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(runbookRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getRunbook() throws Exception {
        // Initialize the database
        insertedRunbook = runbookRepository.saveAndFlush(runbook);

        // Get the runbook
        restRunbookMockMvc
            .perform(get(ENTITY_API_URL_ID, runbook.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(runbook.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION))
            .andExpect(jsonPath("$.origenAlerta").value(DEFAULT_ORIGEN_ALERTA.toString()))
            .andExpect(jsonPath("$.severidadMinima").value(DEFAULT_SEVERIDAD_MINIMA.toString()))
            .andExpect(jsonPath("$.patronFingerprint").value(DEFAULT_PATRON_FINGERPRINT))
            .andExpect(jsonPath("$.tiempoMaxMinutos").value(DEFAULT_TIEMPO_MAX_MINUTOS))
            .andExpect(jsonPath("$.activo").value(DEFAULT_ACTIVO));
    }

    @Test
    @Transactional
    void getNonExistingRunbook() throws Exception {
        // Get the runbook
        restRunbookMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingRunbook() throws Exception {
        // Initialize the database
        insertedRunbook = runbookRepository.saveAndFlush(runbook);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the runbook
        Runbook updatedRunbook = runbookRepository.findById(runbook.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedRunbook are not directly saved in db
        em.detach(updatedRunbook);
        updatedRunbook
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .origenAlerta(UPDATED_ORIGEN_ALERTA)
            .severidadMinima(UPDATED_SEVERIDAD_MINIMA)
            .patronFingerprint(UPDATED_PATRON_FINGERPRINT)
            .tiempoMaxMinutos(UPDATED_TIEMPO_MAX_MINUTOS)
            .activo(UPDATED_ACTIVO);
        RunbookDTO runbookDTO = runbookMapper.toDto(updatedRunbook);

        restRunbookMockMvc
            .perform(
                put(ENTITY_API_URL_ID, runbookDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(runbookDTO))
            )
            .andExpect(status().isOk());

        // Validate the Runbook in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedRunbookToMatchAllProperties(updatedRunbook);
    }

    @Test
    @Transactional
    void putNonExistingRunbook() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        runbook.setId(longCount.incrementAndGet());

        // Create the Runbook
        RunbookDTO runbookDTO = runbookMapper.toDto(runbook);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRunbookMockMvc
            .perform(
                put(ENTITY_API_URL_ID, runbookDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(runbookDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Runbook in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRunbook() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        runbook.setId(longCount.incrementAndGet());

        // Create the Runbook
        RunbookDTO runbookDTO = runbookMapper.toDto(runbook);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRunbookMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(runbookDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Runbook in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRunbook() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        runbook.setId(longCount.incrementAndGet());

        // Create the Runbook
        RunbookDTO runbookDTO = runbookMapper.toDto(runbook);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRunbookMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(runbookDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Runbook in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRunbookWithPatch() throws Exception {
        // Initialize the database
        insertedRunbook = runbookRepository.saveAndFlush(runbook);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the runbook using partial update
        Runbook partialUpdatedRunbook = new Runbook();
        partialUpdatedRunbook.setId(runbook.getId());

        partialUpdatedRunbook.origenAlerta(UPDATED_ORIGEN_ALERTA).patronFingerprint(UPDATED_PATRON_FINGERPRINT);

        restRunbookMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRunbook.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRunbook))
            )
            .andExpect(status().isOk());

        // Validate the Runbook in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRunbookUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedRunbook, runbook), getPersistedRunbook(runbook));
    }

    @Test
    @Transactional
    void fullUpdateRunbookWithPatch() throws Exception {
        // Initialize the database
        insertedRunbook = runbookRepository.saveAndFlush(runbook);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the runbook using partial update
        Runbook partialUpdatedRunbook = new Runbook();
        partialUpdatedRunbook.setId(runbook.getId());

        partialUpdatedRunbook
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .origenAlerta(UPDATED_ORIGEN_ALERTA)
            .severidadMinima(UPDATED_SEVERIDAD_MINIMA)
            .patronFingerprint(UPDATED_PATRON_FINGERPRINT)
            .tiempoMaxMinutos(UPDATED_TIEMPO_MAX_MINUTOS)
            .activo(UPDATED_ACTIVO);

        restRunbookMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRunbook.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRunbook))
            )
            .andExpect(status().isOk());

        // Validate the Runbook in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRunbookUpdatableFieldsEquals(partialUpdatedRunbook, getPersistedRunbook(partialUpdatedRunbook));
    }

    @Test
    @Transactional
    void patchNonExistingRunbook() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        runbook.setId(longCount.incrementAndGet());

        // Create the Runbook
        RunbookDTO runbookDTO = runbookMapper.toDto(runbook);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRunbookMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, runbookDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(runbookDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Runbook in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRunbook() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        runbook.setId(longCount.incrementAndGet());

        // Create the Runbook
        RunbookDTO runbookDTO = runbookMapper.toDto(runbook);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRunbookMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(runbookDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Runbook in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRunbook() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        runbook.setId(longCount.incrementAndGet());

        // Create the Runbook
        RunbookDTO runbookDTO = runbookMapper.toDto(runbook);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRunbookMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(runbookDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Runbook in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRunbook() throws Exception {
        // Initialize the database
        insertedRunbook = runbookRepository.saveAndFlush(runbook);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the runbook
        restRunbookMockMvc
            .perform(delete(ENTITY_API_URL_ID, runbook.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return runbookRepository.count();
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

    protected Runbook getPersistedRunbook(Runbook runbook) {
        return runbookRepository.findById(runbook.getId()).orElseThrow();
    }

    protected void assertPersistedRunbookToMatchAllProperties(Runbook expectedRunbook) {
        assertRunbookAllPropertiesEquals(expectedRunbook, getPersistedRunbook(expectedRunbook));
    }

    protected void assertPersistedRunbookToMatchUpdatableProperties(Runbook expectedRunbook) {
        assertRunbookAllUpdatablePropertiesEquals(expectedRunbook, getPersistedRunbook(expectedRunbook));
    }
}
