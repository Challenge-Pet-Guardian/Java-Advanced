package fiap.com.br.petguardian.veterinaria;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VeterinariaRepository extends JpaRepository<Veterinaria, Long> {
    Page<Veterinaria> findByNomeContainingIgnoreCase(String nome, Pageable pageable);
}
