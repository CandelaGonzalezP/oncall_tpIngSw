package ar.edu.um.isa.oncall.service;

import ar.edu.um.isa.oncall.domain.PasoDeRunbook;
import ar.edu.um.isa.oncall.repository.PasoDeRunbookRepository;
import ar.edu.um.isa.oncall.service.dto.PasoDeRunbookDTO;
import ar.edu.um.isa.oncall.service.mapper.PasoDeRunbookMapper;
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
 * Service Implementation for managing {@link ar.edu.um.isa.oncall.domain.PasoDeRunbook}.
 */
@Service
@Transactional
public class PasoDeRunbookService {

    private static final Logger LOG = LoggerFactory.getLogger(PasoDeRunbookService.class);

    private final PasoDeRunbookRepository pasoDeRunbookRepository;

    private final PasoDeRunbookMapper pasoDeRunbookMapper;

    public PasoDeRunbookService(PasoDeRunbookRepository pasoDeRunbookRepository, PasoDeRunbookMapper pasoDeRunbookMapper) {
        this.pasoDeRunbookRepository = pasoDeRunbookRepository;
        this.pasoDeRunbookMapper = pasoDeRunbookMapper;
    }

    /**
     * Save a pasoDeRunbook.
     *
     * @param pasoDeRunbookDTO the entity to save.
     * @return the persisted entity.
     */
    public PasoDeRunbookDTO save(PasoDeRunbookDTO pasoDeRunbookDTO) {
        LOG.debug("Request to save PasoDeRunbook : {}", pasoDeRunbookDTO);
        PasoDeRunbook pasoDeRunbook = pasoDeRunbookMapper.toEntity(pasoDeRunbookDTO);
        pasoDeRunbook = pasoDeRunbookRepository.save(pasoDeRunbook);
        return pasoDeRunbookMapper.toDto(pasoDeRunbook);
    }

    /**
     * Update a pasoDeRunbook.
     *
     * @param pasoDeRunbookDTO the entity to save.
     * @return the persisted entity.
     */
    public PasoDeRunbookDTO update(PasoDeRunbookDTO pasoDeRunbookDTO) {
        LOG.debug("Request to update PasoDeRunbook : {}", pasoDeRunbookDTO);
        PasoDeRunbook pasoDeRunbook = pasoDeRunbookMapper.toEntity(pasoDeRunbookDTO);
        pasoDeRunbook = pasoDeRunbookRepository.save(pasoDeRunbook);
        return pasoDeRunbookMapper.toDto(pasoDeRunbook);
    }

    /**
     * Partially update a pasoDeRunbook.
     *
     * @param pasoDeRunbookDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PasoDeRunbookDTO> partialUpdate(PasoDeRunbookDTO pasoDeRunbookDTO) {
        LOG.debug("Request to partially update PasoDeRunbook : {}", pasoDeRunbookDTO);

        return pasoDeRunbookRepository
            .findById(pasoDeRunbookDTO.getId())
            .map(existingPasoDeRunbook -> {
                pasoDeRunbookMapper.partialUpdate(existingPasoDeRunbook, pasoDeRunbookDTO);

                return existingPasoDeRunbook;
            })
            .map(pasoDeRunbookRepository::save)
            .map(pasoDeRunbookMapper::toDto);
    }

    /**
     * Get all the pasoDeRunbooks.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<PasoDeRunbookDTO> findAll() {
        LOG.debug("Request to get all PasoDeRunbooks");
        return pasoDeRunbookRepository.findAll().stream().map(pasoDeRunbookMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get all the pasoDeRunbooks with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<PasoDeRunbookDTO> findAllWithEagerRelationships(Pageable pageable) {
        return pasoDeRunbookRepository.findAllWithEagerRelationships(pageable).map(pasoDeRunbookMapper::toDto);
    }

    /**
     * Get one pasoDeRunbook by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PasoDeRunbookDTO> findOne(Long id) {
        LOG.debug("Request to get PasoDeRunbook : {}", id);
        return pasoDeRunbookRepository.findOneWithEagerRelationships(id).map(pasoDeRunbookMapper::toDto);
    }

    /**
     * Delete the pasoDeRunbook by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete PasoDeRunbook : {}", id);
        pasoDeRunbookRepository.deleteById(id);
    }
}
