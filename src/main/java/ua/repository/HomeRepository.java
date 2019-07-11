package ua.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ua.model.Home;
import ua.model.LegalEntity;

import java.util.List;

public interface HomeRepository extends JpaRepository<Home, Integer>  {

    @Query("from Home where entity_id=:id")
    List<Home> findByLegalEntityId(int id);

    @Query("from Home where id=:id and entity_id=:legalEntityId")
    Home findByIdAndLegalEntityId(@Param("id") int id, @Param("legalEntityId") int legalEntityId);


}
