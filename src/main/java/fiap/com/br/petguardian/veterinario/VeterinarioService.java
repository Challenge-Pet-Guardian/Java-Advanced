package fiap.com.br.petguardian.veterinario;

import fiap.com.br.petguardian.clinica.Clinica;
import fiap.com.br.petguardian.clinica.ClinicaService;
import fiap.com.br.petguardian.exception.ResourceNotFoundException;
import fiap.com.br.petguardian.telefone.Telefone;
import fiap.com.br.petguardian.telefone.TelefoneRepository;
import fiap.com.br.petguardian.veterinario.dto.VeterinarioRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VeterinarioService {
    private final VeterinarioRepository veterinarioRepository;
    private final ClinicaService clinicaService;
    private final TelefoneRepository telefoneRepository;

    public List<Veterinario> findAll() {
        return veterinarioRepository.findAll();
    }

    public Veterinario findById(Long id) {
        return findVeterinarioById(id);
    }

    public Veterinario create(VeterinarioRequest request) {
        Clinica clinica = request.clinicaId() == null ? null : clinicaService.findClinicaById(request.clinicaId());
        Telefone telefone = telefoneRepository.save(
                Telefone.builder().ddd(request.ddd()).numero(request.numeroTelefone()).build());
        Veterinario veterinario = request.toEntity(telefone, clinica);
        return veterinarioRepository.save(veterinario);
    }

    public Veterinario update(Long id, VeterinarioRequest request) {
        findVeterinarioById(id);
        Clinica clinica = request.clinicaId() == null ? null : clinicaService.findClinicaById(request.clinicaId());
        Telefone telefone = telefoneRepository.save(
                Telefone.builder().ddd(request.ddd()).numero(request.numeroTelefone()).build());
        Veterinario veterinario = request.toEntity(telefone, clinica);
        veterinario.setId(id);
        return veterinarioRepository.save(veterinario);
    }

    public void delete(Long id) {
        findVeterinarioById(id);
        veterinarioRepository.deleteById(id);
    }

    public Veterinario findVeterinarioById(Long id) {
        return veterinarioRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Veterinario com id " + id + " nao encontrado."));
    }
}
