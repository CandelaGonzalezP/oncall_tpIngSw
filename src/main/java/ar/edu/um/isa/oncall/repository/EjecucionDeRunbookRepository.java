package ar.edu.um.isa.oncall.repository;

import ar.edu.um.isa.oncall.domain.EjecucionDeRunbook;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the EjecucionDeRunbook entity.
 */
@Repository
public interface EjecucionDeRunbookRepository extends JpaRepository<EjecucionDeRunbook, Long> {
    @Query(
        "select ejecucionDeRunbook from EjecucionDeRunbook ejecucionDeRunbook where ejecucionDeRunbook.ejecutor.login = ?#{authentication.name}"
    )
    List<EjecucionDeRunbook> findByEjecutorIsCurrentUser();

    default Optional<EjecucionDeRunbook> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<EjecucionDeRunbook> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<EjecucionDeRunbook> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select ejecucionDeRunbook from EjecucionDeRunbook ejecucionDeRunbook left join fetch ejecucionDeRunbook.runbook left join fetch ejecucionDeRunbook.incidente left join fetch ejecucionDeRunbook.ejecutor",
        countQuery = "select count(ejecucionDeRunbook) from EjecucionDeRunbook ejecucionDeRunbook"
    )
    Page<EjecucionDeRunbook> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select ejecucionDeRunbook from EjecucionDeRunbook ejecucionDeRunbook left join fetch ejecucionDeRunbook.runbook left join fetch ejecucionDeRunbook.incidente left join fetch ejecucionDeRunbook.ejecutor"
    )
    List<EjecucionDeRunbook> findAllWithToOneRelationships();

    @Query(
        "select ejecucionDeRunbook from EjecucionDeRunbook ejecucionDeRunbook left join fetch ejecucionDeRunbook.runbook left join fetch ejecucionDeRunbook.incidente left join fetch ejecucionDeRunbook.ejecutor where ejecucionDeRunbook.id =:id"
    )
    Optional<EjecucionDeRunbook> findOneWithToOneRelationships(@Param("id") Long id);
}
