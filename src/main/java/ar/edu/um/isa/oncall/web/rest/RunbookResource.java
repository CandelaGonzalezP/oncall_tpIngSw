package ar.edu.um.isa.oncall.web.rest;

import ar.edu.um.isa.oncall.repository.RunbookRepository;
import ar.edu.um.isa.oncall.service.RunbookService;
import ar.edu.um.isa.oncall.service.dto.RunbookDTO;
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
 * REST controller for managing {@link ar.edu.um.isa.oncall.domain.Runbook}.
 */
@RestController
@RequestMapping("/api/runbooks")
public class RunbookResource {

    private static final Logger LOG = LoggerFactory.getLogger(RunbookResource.class);

    private static final String ENTITY_NAME = "runbook";

    @Value("${jhipster.clientApp.name:oncall}")
    private String applicationName;

    private final RunbookService runbookService;

    private final RunbookRepository runbookRepository;

    public RunbookResource(RunbookService runbookService, RunbookRepository runbookRepository) {
        this.runbookService = runbookService;
        this.runbookRepository = runbookRepository;
    }

    /**
     * {@code POST  /runbooks} : Create a new runbook.
     *
     * @param runbookDTO the runbookDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new runbookDTO, or with status {@code 400 (Bad Request)} if the runbook has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<RunbookDTO> createRunbook(@Valid @RequestBody RunbookDTO runbookDTO) throws URISyntaxException {
        LOG.debug("REST request to save Runbook : {}", runbookDTO);
        if (runbookDTO.getId() != null) {
            throw new BadRequestAlertException("A new runbook cannot already have an ID", ENTITY_NAME, "idexists");
        }
        runbookDTO = runbookService.save(runbookDTO);
        return ResponseEntity.created(new URI("/api/runbooks/" + runbookDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, runbookDTO.getId().toString()))
            .body(runbookDTO);
    }

    /**
     * {@code PUT  /runbooks/:id} : Updates an existing runbook.
     *
     * @param id the id of the runbookDTO to save.
     * @param runbookDTO the runbookDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated runbookDTO,
     * or with status {@code 400 (Bad Request)} if the runbookDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the runbookDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<RunbookDTO> updateRunbook(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody RunbookDTO runbookDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Runbook : {}, {}", id, runbookDTO);
        if (runbookDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, runbookDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!runbookRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        runbookDTO = runbookService.update(runbookDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, runbookDTO.getId().toString()))
            .body(runbookDTO);
    }

    /**
     * {@code PATCH  /runbooks/:id} : Partial updates given fields of an existing runbook, field will ignore if it is null
     *
     * @param id the id of the runbookDTO to save.
     * @param runbookDTO the runbookDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated runbookDTO,
     * or with status {@code 400 (Bad Request)} if the runbookDTO is not valid,
     * or with status {@code 404 (Not Found)} if the runbookDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the runbookDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RunbookDTO> partialUpdateRunbook(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody RunbookDTO runbookDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Runbook partially : {}, {}", id, runbookDTO);
        if (runbookDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, runbookDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!runbookRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RunbookDTO> result = runbookService.partialUpdate(runbookDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, runbookDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /runbooks} : get all the Runbooks.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Runbooks in body.
     */
    @GetMapping("")
    public List<RunbookDTO> getAllRunbooks(@RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload) {
        LOG.debug("REST request to get all Runbooks");
        return runbookService.findAll();
    }

    /**
     * {@code GET  /runbooks/:id} : get the "id" runbook.
     *
     * @param id the id of the runbookDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the runbookDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<RunbookDTO> getRunbook(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Runbook : {}", id);
        Optional<RunbookDTO> runbookDTO = runbookService.findOne(id);
        return ResponseUtil.wrapOrNotFound(runbookDTO);
    }

    /**
     * {@code DELETE  /runbooks/:id} : delete the "id" runbook.
     *
     * @param id the id of the runbookDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRunbook(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Runbook : {}", id);
        runbookService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
