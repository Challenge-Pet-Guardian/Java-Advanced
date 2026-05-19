package fiap.com.br.petguardian.pet;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface PetRepository extends JpaRepository<Pet, Long> {
    @Query("select distinct p from Pet p join p.usuarioPets up where up.usuario.familia.id = :familiaId")
    List<Pet> findAllByFamiliaId(@Param("familiaId") Long familiaId);

    @Query("select distinct p.id from Pet p join p.usuarioPets up where up.usuario.familia.id = :familiaId")
    Set<Long> findIdsByFamiliaId(@Param("familiaId") Long familiaId);

    Page<Pet> findByNomeContainingIgnoreCase(String nome, Pageable pageable);
}
