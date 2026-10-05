package ar.edu.um.isa.oncall.web.rest;

import ar.edu.um.isa.oncall.repository.EjecucionDeRunbookRepository;
import ar.edu.um.isa.oncall.service.EjecucionDeRunbookService;
import ar.edu.um.isa.oncall.service.dto.EjecucionDeRunbookDTO;
import ar.edu.um.isa.oncall.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link ar.edu.um.isa.oncall.domain.EjecucionDeRunbook}.
 */
@RestController
@RequestMapping("/api/ejecucion-de-runbooks")
public class EjecucionDeRunbookResource {

    private static final Logger LOG = LoggerFactory.getLogger(EjecucionDeRunbookResource.class);

    private static final String ENTITY_NAME = "ejecucionDeRunbook";

    @Value("${jhipster.clientApp.name:oncall}")
    private String applicationName;

    private final EjecucionDeRunbookService ejecucionDeRunbookService;

    private final EjecucionDeRunbookRepository ejecucionDeRunbookRepository;

    public EjecucionDeRunbookResource(
        EjecucionDeRunbookService ejecucionDeRunbookService,
        EjecucionDeRunbookRepository ejecucionDeRunbookRepository
    ) {
        this.ejecucionDeRunbookService = ejecucionDeRunbookService;
        this.ejecucionDeRunbookRepository = ejecucionDeRunbookRepository;
    }

    /**
     * {@code POST  /ejecucion-de-runbooks} : Create a new ejecucionDeRunbook.
     *
     * @param ejecucionDeRunbookDTO the ejecucionDeRunbookDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ejecucionDeRunbookDTO, or with status {@code 400 (Bad Request)} if the ejecucionDeRunbook has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<EjecucionDeRunbookDTO> createEjecucionDeRunbook(@Valid @RequestBody EjecucionDeRunbookDTO ejecucionDeRunbookDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save EjecucionDeRunbook : {}", ejecucionDeRunbookDTO);
        if (ejecucionDeRunbookDTO.getId() != null) {
            throw new BadRequestAlertException("A new ejecucionDeRunbook cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ejecucionDeRunbookDTO = ejecucionDeRunbookService.save(ejecucionDeRunbookDTO);
        return ResponseEntity.created(new URI("/api/ejecucion-de-runbooks/" + ejecucionDeRunbookDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, ejecucionDeRunbookDTO.getId().toString()))
            .body(ejecucionDeRunbookDTO);
    }

    /**
     * {@code PUT  /ejecucion-de-runbooks/:id} : Updates an existing ejecucionDeRunbook.
     *
     * @param id the id of the ejecucionDeRunbookDTO to save.
     * @param ejecucionDeRunbookDTO the ejecucionDeRunbookDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ejecucionDeRunbookDTO,
     * or with status {@code 400 (Bad Request)} if the ejecucionDeRunbookDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ejecucionDeRunbookDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EjecucionDeRunbookDTO> updateEjecucionDeRunbook(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EjecucionDeRunbookDTO ejecucionDeRunbookDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update EjecucionDeRunbook : {}, {}", id, ejecucionDeRunbookDTO);
        if (ejecucionDeRunbookDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ejecucionDeRunbookDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ejecucionDeRunbookRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ejecucionDeRunbookDTO = ejecucionDeRunbookService.update(ejecucionDeRunbookDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ejecucionDeRunbookDTO.getId().toString()))
            .body(ejecucionDeRunbookDTO);
    }

    /**
     * {@code PATCH  /ejecucion-de-runbooks/:id} : Partial updates given fields of an existing ejecucionDeRunbook, field will ignore if it is null
     *
     * @param id the id of the ejecucionDeRunbookDTO to save.
     * @param ejecucionDeRunbookDTO the ejecucionDeRunbookDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ejecucionDeRunbookDTO,
     * or with status {@code 400 (Bad Request)} if the ejecucionDeRunbookDTO is not valid,
     * or with status {@code 404 (Not Found)} if the ejecucionDeRunbookDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the ejecucionDeRunbookDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EjecucionDeRunbookDTO> partialUpdateEjecucionDeRunbook(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody EjecucionDeRunbookDTO ejecucionDeRunbookDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update EjecucionDeRunbook partially : {}, {}", id, ejecucionDeRunbookDTO);
        if (ejecucionDeRunbookDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ejecucionDeRunbookDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ejecucionDeRunbookRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EjecucionDeRunbookDTO> result = ejecucionDeRunbookService.partialUpdate(ejecucionDeRunbookDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ejecucionDeRunbookDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /ejecucion-de-runbooks} : get all the Ejecucion De Runbooks.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Ejecucion De Runbooks in body.
     */
    @GetMapping("")
    public List<EjecucionDeRunbookDTO> getAllEjecucionDeRunbooks(
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get all EjecucionDeRunbooks");
        return ejecucionDeRunbookService.findAll();
    }

    /**
     * {@code GET  /ejecucion-de-runbooks/:id} : get the "id" ejecucionDeRunbook.
     *
     * @param id the id of the ejecucionDeRunbookDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ejecucionDeRunbookDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EjecucionDeRunbookDTO> getEjecucionDeRunbook(@PathVariable("id") Long id) {
        LOG.debug("REST request to get EjecucionDeRunbook : {}", id);
        Optional<EjecucionDeRunbookDTO> ejecucionDeRunbookDTO = ejecucionDeRunbookService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ejecucionDeRunbookDTO);
    }

    /**
     * {@code DELETE  /ejecucion-de-runbooks/:id} : delete the "id" ejecucionDeRunbook.
     *
     * @param id the id of the ejecucionDeRunbookDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEjecucionDeRunbook(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete EjecucionDeRunbook : {}", id);
        ejecucionDeRunbookService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
