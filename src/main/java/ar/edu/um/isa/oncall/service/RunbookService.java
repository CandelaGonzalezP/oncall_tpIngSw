package ar.edu.um.isa.oncall.service;

import ar.edu.um.isa.oncall.domain.Runbook;
import ar.edu.um.isa.oncall.repository.RunbookRepository;
import ar.edu.um.isa.oncall.service.dto.RunbookDTO;
import ar.edu.um.isa.oncall.service.mapper.RunbookMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ar.edu.um.isa.oncall.domain.Runbook}.
 */
@Service
@Transactional
public class RunbookService {

    private static final Logger LOG = LoggerFactory.getLogger(RunbookService.class);

    private final RunbookRepository runbookRepository;

    private final RunbookMapper runbookMapper;

    public RunbookService(RunbookRepository runbookRepository, RunbookMapper runbookMapper) {
        this.runbookRepository = runbookRepository;
        this.runbookMapper = runbookMapper;
    }

    /**
     * Save a runbook.
     *
     * @param runbookDTO the entity to save.
     * @return the persisted entity.
     */
    public RunbookDTO save(RunbookDTO runbookDTO) {
        LOG.debug("Request to save Runbook : {}", runbookDTO);
        Runbook runbook = runbookMapper.toEntity(runbookDTO);
        runbook = runbookRepository.save(runbook);
        return runbookMapper.toDto(runbook);
    }

    /**
     * Update a runbook.
     *
     * @param runbookDTO the entity to save.
     * @return the persisted entity.
     */
    public RunbookDTO update(RunbookDTO runbookDTO) {
        LOG.debug("Request to update Runbook : {}", runbookDTO);
        Runbook runbook = runbookMapper.toEntity(runbookDTO);
        runbook = runbookRepository.save(runbook);
        return runbookMapper.toDto(runbook);
    }

    /**
     * Partially update a runbook.
     *
     * @param runbookDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<RunbookDTO> partialUpdate(RunbookDTO runbookDTO) {
        LOG.debug("Request to partially update Runbook : {}", runbookDTO);

        return runbookRepository
            .findById(runbookDTO.getId())
            .map(existingRunbook -> {
                runbookMapper.partialUpdate(existingRunbook, runbookDTO);

                return existingRunbook;
            })
            .map(runbookRepository::save)
            .map(runbookMapper::toDto);
    }

    /**
     * Get all the runbooks.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<RunbookDTO> findAll() {
        LOG.debug("Request to get all Runbooks");
        return runbookRepository.findAll().stream().map(runbookMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get all the runbooks with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<RunbookDTO> findAllWithEagerRelationships(Pageable pageable) {
        return runbookRepository.findAllWithEagerRelationships(pageable).map(runbookMapper::toDto);
    }

    /**
     * Get one runbook by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<RunbookDTO> findOne(Long id) {
        LOG.debug("Request to get Runbook : {}", id);
        return runbookRepository.findOneWithEagerRelationships(id).map(runbookMapper::toDto);
    }

    /**
     * Delete the runbook by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Runbook : {}", id);
        runbookRepository.deleteById(id);
    }
}
