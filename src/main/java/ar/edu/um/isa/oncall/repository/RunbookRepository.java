package ar.edu.um.isa.oncall.repository;

import ar.edu.um.isa.oncall.domain.Runbook;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Runbook entity.
 */
@Repository
public interface RunbookRepository extends JpaRepository<Runbook, Long> {
    default Optional<Runbook> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Runbook> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Runbook> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select runbook from Runbook runbook left join fetch runbook.servicio",
        countQuery = "select count(runbook) from Runbook runbook"
    )
    Page<Runbook> findAllWithToOneRelationships(Pageable pageable);

    @Query("select runbook from Runbook runbook left join fetch runbook.servicio")
    List<Runbook> findAllWithToOneRelationships();

    @Query("select runbook from Runbook runbook left join fetch runbook.servicio where runbook.id =:id")
    Optional<Runbook> findOneWithToOneRelationships(@Param("id") Long id);
}
