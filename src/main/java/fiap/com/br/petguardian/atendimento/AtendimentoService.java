package fiap.com.br.petguardian.atendimento;

import fiap.com.br.petguardian.familia.Familia;
import fiap.com.br.petguardian.status.Status;
import fiap.com.br.petguardian.status.StatusService;
import fiap.com.br.petguardian.tipoatendimento.TipoAtendimento;
import fiap.com.br.petguardian.tipoatendimento.TipoAtendimentoService;

import fiap.com.br.petguardian.atendimento.dto.AtendimentoRequest;
import fiap.com.br.petguardian.familia.FamiliaRepository;
import fiap.com.br.petguardian.pet.Pet;
import fiap.com.br.petguardian.pet.PetRepository;
import fiap.com.br.petguardian.veterinaria.Veterinaria;
import fiap.com.br.petguardian.veterinaria.VeterinariaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AtendimentoService {
    private final AtendimentoRepository atendimentoRepository;
    private final PetRepository petRepository;
    private final VeterinariaRepository veterinariaRepository;
    private final FamiliaRepository familiaRepository;
    private final StatusService statusService;
    private final TipoAtendimentoService tipoAtendimentoService;

    public List<Atendimento> findAll(Long familiaId) {
        expirarAtendimentosPendentesAtrasados();
        findFamiliaById(familiaId);
        return atendimentoRepository.findAllByFamiliaId(familiaId);
    }

    public Atendimento findById(Long id) {
        expirarAtendimentosPendentesAtrasados();
        return findAtendimentoById(id);
    }

    public Atendimento create(AtendimentoRequest atendimentoRequest) {
        Pet pet = findPetById(atendimentoRequest.petId());
        Veterinaria veterinaria = findVeterinariaById(atendimentoRequest.veterinariaId());

        TipoAtendimento tipoObj = tipoAtendimentoService.findTipoAtendimentoByNome(atendimentoRequest.tipo());
        Status statusObj = statusService.findStatusByNome("PENDENTE");
        Atendimento atendimento = atendimentoRequest.toEntity(tipoObj, pet, veterinaria, statusObj);
        return atendimentoRepository.save(atendimento);
    }

    public Atendimento update(Long id, AtendimentoRequest atendimentoRequest) {
        findAtendimentoById(id);
        expirarAtendimentosPendentesAtrasados();

        Pet pet = findPetById(atendimentoRequest.petId());
        Veterinaria veterinaria = findVeterinariaById(atendimentoRequest.veterinariaId());

        String statusStr = atendimentoRequest.status();

        if ("EXPIRADO".equalsIgnoreCase(statusStr) && atendimentoRequest.data().isAfter(LocalDateTime.now())) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Não é permitido marcar como EXPIRADO antes da data do atendimento.");

        TipoAtendimento tipoObj = tipoAtendimentoService.findTipoAtendimentoByNome(atendimentoRequest.tipo());
        Status statusObj = statusService.findStatusByNome(statusStr);
        Atendimento atendimento = atendimentoRequest.toEntity(tipoObj, pet, veterinaria, statusObj);
        atendimento.setId(id);
        return atendimentoRepository.save(atendimento);
    }

    public void delete(Long id) {
        findAtendimentoById(id);
        atendimentoRepository.deleteById(id);
    }

    private Atendimento findAtendimentoById(Long id) {
        return atendimentoRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Atendimento com id " + id + " não encontrado."));
    }

    private Pet findPetById(Long id) {
        return petRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pet com id " + id + " não encontrado."));
    }

    private Veterinaria findVeterinariaById(Long id) {
        return veterinariaRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Veterinária com id " + id + " não encontrada."));
    }

    private Familia findFamiliaById(Long id) {
        return familiaRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Família com id " + id + " não encontrada."));
    }

    private void expirarAtendimentosPendentesAtrasados() {
        Status pendente = statusService.findStatusByNome("PENDENTE");
        Status expirado = statusService.findStatusByNome("EXPIRADO");
        atendimentoRepository.expirarAtendimentosPendentesAtrasados(LocalDateTime.now(), pendente, expirado);
    }
}
