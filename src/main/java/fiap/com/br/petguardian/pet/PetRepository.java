package fiap.com.br.petguardian.pet;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface PetRepository extends JpaRepository<Pet, Long> {
    List<Pet> findAllByFamiliaId(Long familiaId);

    @Query("select p.id from Pet p where p.familia.id = :familiaId")
    Set<Long> findIdsByFamiliaId(@Param("familiaId") Long familiaId);
}
