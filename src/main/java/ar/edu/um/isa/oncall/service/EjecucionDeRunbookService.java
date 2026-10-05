package ar.edu.um.isa.oncall.service;

import ar.edu.um.isa.oncall.domain.EjecucionDeRunbook;
import ar.edu.um.isa.oncall.repository.EjecucionDeRunbookRepository;
import ar.edu.um.isa.oncall.service.dto.EjecucionDeRunbookDTO;
import ar.edu.um.isa.oncall.service.mapper.EjecucionDeRunbookMapper;
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
 * Service Implementation for managing {@link ar.edu.um.isa.oncall.domain.EjecucionDeRunbook}.
 */
@Service
@Transactional
public class EjecucionDeRunbookService {

    private static final Logger LOG = LoggerFactory.getLogger(EjecucionDeRunbookService.class);

    private final EjecucionDeRunbookRepository ejecucionDeRunbookRepository;

    private final EjecucionDeRunbookMapper ejecucionDeRunbookMapper;

    public EjecucionDeRunbookService(
        EjecucionDeRunbookRepository ejecucionDeRunbookRepository,
        EjecucionDeRunbookMapper ejecucionDeRunbookMapper
    ) {
        this.ejecucionDeRunbookRepository = ejecucionDeRunbookRepository;
        this.ejecucionDeRunbookMapper = ejecucionDeRunbookMapper;
    }

    /**
     * Save a ejecucionDeRunbook.
     *
     * @param ejecucionDeRunbookDTO the entity to save.
     * @return the persisted entity.
     */
    public EjecucionDeRunbookDTO save(EjecucionDeRunbookDTO ejecucionDeRunbookDTO) {
        LOG.debug("Request to save EjecucionDeRunbook : {}", ejecucionDeRunbookDTO);
        EjecucionDeRunbook ejecucionDeRunbook = ejecucionDeRunbookMapper.toEntity(ejecucionDeRunbookDTO);
        ejecucionDeRunbook = ejecucionDeRunbookRepository.save(ejecucionDeRunbook);
        return ejecucionDeRunbookMapper.toDto(ejecucionDeRunbook);
    }

    /**
     * Update a ejecucionDeRunbook.
     *
     * @param ejecucionDeRunbookDTO the entity to save.
     * @return the persisted entity.
     */
    public EjecucionDeRunbookDTO update(EjecucionDeRunbookDTO ejecucionDeRunbookDTO) {
        LOG.debug("Request to update EjecucionDeRunbook : {}", ejecucionDeRunbookDTO);
        EjecucionDeRunbook ejecucionDeRunbook = ejecucionDeRunbookMapper.toEntity(ejecucionDeRunbookDTO);
        ejecucionDeRunbook = ejecucionDeRunbookRepository.save(ejecucionDeRunbook);
        return ejecucionDeRunbookMapper.toDto(ejecucionDeRunbook);
    }

    /**
     * Partially update a ejecucionDeRunbook.
     *
     * @param ejecucionDeRunbookDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<EjecucionDeRunbookDTO> partialUpdate(EjecucionDeRunbookDTO ejecucionDeRunbookDTO) {
        LOG.debug("Request to partially update EjecucionDeRunbook : {}", ejecucionDeRunbookDTO);

        return ejecucionDeRunbookRepository
            .findById(ejecucionDeRunbookDTO.getId())
            .map(existingEjecucionDeRunbook -> {
                ejecucionDeRunbookMapper.partialUpdate(existingEjecucionDeRunbook, ejecucionDeRunbookDTO);

                return existingEjecucionDeRunbook;
            })
            .map(ejecucionDeRunbookRepository::save)
            .map(ejecucionDeRunbookMapper::toDto);
    }

    /**
     * Get all the ejecucionDeRunbooks.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<EjecucionDeRunbookDTO> findAll() {
        LOG.debug("Request to get all EjecucionDeRunbooks");
        return ejecucionDeRunbookRepository
            .findAll()
            .stream()
            .map(ejecucionDeRunbookMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get all the ejecucionDeRunbooks with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<EjecucionDeRunbookDTO> findAllWithEagerRelationships(Pageable pageable) {
        return ejecucionDeRunbookRepository.findAllWithEagerRelationships(pageable).map(ejecucionDeRunbookMapper::toDto);
    }

    /**
     * Get one ejecucionDeRunbook by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<EjecucionDeRunbookDTO> findOne(Long id) {
        LOG.debug("Request to get EjecucionDeRunbook : {}", id);
        return ejecucionDeRunbookRepository.findOneWithEagerRelationships(id).map(ejecucionDeRunbookMapper::toDto);
    }

    /**
     * Delete the ejecucionDeRunbook by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete EjecucionDeRunbook : {}", id);
        ejecucionDeRunbookRepository.deleteById(id);
    }
}
