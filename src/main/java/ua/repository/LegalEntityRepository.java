package ua.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ua.model.Home;
import ua.model.LegalEntity;

public interface LegalEntityRepository extends JpaRepository<LegalEntity, Integer> {

    @Query("from LegalEntity where subdomain =:subDomain")
    LegalEntity findBySubdomain(@Param("subDomain") String subDomain);
}
