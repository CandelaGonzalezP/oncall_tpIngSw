package ar.edu.um.isa.oncall.repository;

import ar.edu.um.isa.oncall.domain.PasoDeRunbook;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PasoDeRunbook entity.
 */
@Repository
public interface PasoDeRunbookRepository extends JpaRepository<PasoDeRunbook, Long> {
    default Optional<PasoDeRunbook> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<PasoDeRunbook> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<PasoDeRunbook> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select pasoDeRunbook from PasoDeRunbook pasoDeRunbook left join fetch pasoDeRunbook.runbook",
        countQuery = "select count(pasoDeRunbook) from PasoDeRunbook pasoDeRunbook"
    )
    Page<PasoDeRunbook> findAllWithToOneRelationships(Pageable pageable);

    @Query("select pasoDeRunbook from PasoDeRunbook pasoDeRunbook left join fetch pasoDeRunbook.runbook")
    List<PasoDeRunbook> findAllWithToOneRelationships();

    @Query("select pasoDeRunbook from PasoDeRunbook pasoDeRunbook left join fetch pasoDeRunbook.runbook where pasoDeRunbook.id =:id")
    Optional<PasoDeRunbook> findOneWithToOneRelationships(@Param("id") Long id);
}
