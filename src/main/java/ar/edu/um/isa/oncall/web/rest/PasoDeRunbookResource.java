package ar.edu.um.isa.oncall.web.rest;

import ar.edu.um.isa.oncall.repository.PasoDeRunbookRepository;
import ar.edu.um.isa.oncall.service.PasoDeRunbookService;
import ar.edu.um.isa.oncall.service.dto.PasoDeRunbookDTO;
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
 * REST controller for managing {@link ar.edu.um.isa.oncall.domain.PasoDeRunbook}.
 */
@RestController
@RequestMapping("/api/paso-de-runbooks")
public class PasoDeRunbookResource {

    private static final Logger LOG = LoggerFactory.getLogger(PasoDeRunbookResource.class);

    private static final String ENTITY_NAME = "pasoDeRunbook";

    @Value("${jhipster.clientApp.name:oncall}")
    private String applicationName;

    private final PasoDeRunbookService pasoDeRunbookService;

    private final PasoDeRunbookRepository pasoDeRunbookRepository;

    public PasoDeRunbookResource(PasoDeRunbookService pasoDeRunbookService, PasoDeRunbookRepository pasoDeRunbookRepository) {
        this.pasoDeRunbookService = pasoDeRunbookService;
        this.pasoDeRunbookRepository = pasoDeRunbookRepository;
    }

    /**
     * {@code POST  /paso-de-runbooks} : Create a new pasoDeRunbook.
     *
     * @param pasoDeRunbookDTO the pasoDeRunbookDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new pasoDeRunbookDTO, or with status {@code 400 (Bad Request)} if the pasoDeRunbook has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PasoDeRunbookDTO> createPasoDeRunbook(@Valid @RequestBody PasoDeRunbookDTO pasoDeRunbookDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save PasoDeRunbook : {}", pasoDeRunbookDTO);
        if (pasoDeRunbookDTO.getId() != null) {
            throw new BadRequestAlertException("A new pasoDeRunbook cannot already have an ID", ENTITY_NAME, "idexists");
        }
        pasoDeRunbookDTO = pasoDeRunbookService.save(pasoDeRunbookDTO);
        return ResponseEntity.created(new URI("/api/paso-de-runbooks/" + pasoDeRunbookDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, pasoDeRunbookDTO.getId().toString()))
            .body(pasoDeRunbookDTO);
    }

    /**
     * {@code PUT  /paso-de-runbooks/:id} : Updates an existing pasoDeRunbook.
     *
     * @param id the id of the pasoDeRunbookDTO to save.
     * @param pasoDeRunbookDTO the pasoDeRunbookDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pasoDeRunbookDTO,
     * or with status {@code 400 (Bad Request)} if the pasoDeRunbookDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the pasoDeRunbookDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PasoDeRunbookDTO> updatePasoDeRunbook(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PasoDeRunbookDTO pasoDeRunbookDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update PasoDeRunbook : {}, {}", id, pasoDeRunbookDTO);
        if (pasoDeRunbookDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pasoDeRunbookDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pasoDeRunbookRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        pasoDeRunbookDTO = pasoDeRunbookService.update(pasoDeRunbookDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pasoDeRunbookDTO.getId().toString()))
            .body(pasoDeRunbookDTO);
    }

    /**
     * {@code PATCH  /paso-de-runbooks/:id} : Partial updates given fields of an existing pasoDeRunbook, field will ignore if it is null
     *
     * @param id the id of the pasoDeRunbookDTO to save.
     * @param pasoDeRunbookDTO the pasoDeRunbookDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pasoDeRunbookDTO,
     * or with status {@code 400 (Bad Request)} if the pasoDeRunbookDTO is not valid,
     * or with status {@code 404 (Not Found)} if the pasoDeRunbookDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the pasoDeRunbookDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PasoDeRunbookDTO> partialUpdatePasoDeRunbook(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PasoDeRunbookDTO pasoDeRunbookDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update PasoDeRunbook partially : {}, {}", id, pasoDeRunbookDTO);
        if (pasoDeRunbookDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pasoDeRunbookDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pasoDeRunbookRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PasoDeRunbookDTO> result = pasoDeRunbookService.partialUpdate(pasoDeRunbookDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pasoDeRunbookDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /paso-de-runbooks} : get all the Paso De Runbooks.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Paso De Runbooks in body.
     */
    @GetMapping("")
    public List<PasoDeRunbookDTO> getAllPasoDeRunbooks(
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get all PasoDeRunbooks");
        return pasoDeRunbookService.findAll();
    }

    /**
     * {@code GET  /paso-de-runbooks/:id} : get the "id" pasoDeRunbook.
     *
     * @param id the id of the pasoDeRunbookDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pasoDeRunbookDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PasoDeRunbookDTO> getPasoDeRunbook(@PathVariable("id") Long id) {
        LOG.debug("REST request to get PasoDeRunbook : {}", id);
        Optional<PasoDeRunbookDTO> pasoDeRunbookDTO = pasoDeRunbookService.findOne(id);
        return ResponseUtil.wrapOrNotFound(pasoDeRunbookDTO);
    }

    /**
     * {@code DELETE  /paso-de-runbooks/:id} : delete the "id" pasoDeRunbook.
     *
     * @param id the id of the pasoDeRunbookDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePasoDeRunbook(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete PasoDeRunbook : {}", id);
        pasoDeRunbookService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
