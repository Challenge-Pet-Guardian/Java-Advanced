package fiap.com.br.petguardian.clinica;

import fiap.com.br.petguardian.exception.ResourceNotFoundException;
import fiap.com.br.petguardian.endereco.Endereco;
import fiap.com.br.petguardian.endereco.EnderecoService;
import fiap.com.br.petguardian.telefone.Telefone;
import fiap.com.br.petguardian.telefone.TelefoneRepository;
import fiap.com.br.petguardian.clinica.dto.ClinicaRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClinicaService {
    private final ClinicaRepository clinicaRepository;
    private final EnderecoService enderecoService;
    private final TelefoneRepository telefoneRepository;

    public List<Clinica> findAll() {
        return clinicaRepository.findAll();
    }

    public Clinica findById(Long id) {
        return findClinicaById(id);
    }

    public Clinica create(ClinicaRequest clinicaRequest) {
        Endereco endereco = enderecoService.findOrCreateByCepAndNumero(clinicaRequest.endereco());
        Telefone telefone = telefoneRepository.save(
                Telefone.builder().ddd(clinicaRequest.ddd()).numero(clinicaRequest.numeroTelefone()).build());
        Clinica clinica = clinicaRequest.toEntity(telefone, endereco);

        return clinicaRepository.save(clinica);
    }

    public Clinica update(Long id, ClinicaRequest clinicaRequest) {
        findClinicaById(id);
        Endereco endereco = enderecoService.findOrCreateByCepAndNumero(clinicaRequest.endereco());
        Telefone telefone = telefoneRepository.save(
                Telefone.builder().ddd(clinicaRequest.ddd()).numero(clinicaRequest.numeroTelefone()).build());
        Clinica clinica = clinicaRequest.toEntity(telefone, endereco);
        clinica.setId(id);
        return clinicaRepository.save(clinica);
    }

    public void delete(Long id) {
        findClinicaById(id);
        clinicaRepository.deleteById(id);
    }

    public Clinica findClinicaById(Long id) {
        return clinicaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Clinica com id " + id + " nao encontrada."));
    }
}
