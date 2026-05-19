package fiap.com.br.petguardian.config;

import fiap.com.br.petguardian.atendimento.Atendimento;
import fiap.com.br.petguardian.atendimento.AtendimentoRepository;
import fiap.com.br.petguardian.endereco.Endereco;
import fiap.com.br.petguardian.endereco.EnderecoRepository;
import fiap.com.br.petguardian.endereco.bairro.Bairro;
import fiap.com.br.petguardian.endereco.bairro.BairroRepository;
import fiap.com.br.petguardian.endereco.cidade.Cidade;
import fiap.com.br.petguardian.endereco.cidade.CidadeRepository;
import fiap.com.br.petguardian.endereco.estado.Estado;
import fiap.com.br.petguardian.endereco.estado.EstadoRepository;
import fiap.com.br.petguardian.familia.Familia;
import fiap.com.br.petguardian.familia.FamiliaRepository;
import fiap.com.br.petguardian.pet.Pet;
import fiap.com.br.petguardian.pet.PetPorte;
import fiap.com.br.petguardian.pet.PetRepository;
import fiap.com.br.petguardian.pet.raca.Raca;
import fiap.com.br.petguardian.pet.raca.RacaRepository;
import fiap.com.br.petguardian.status.EnumStatus;
import fiap.com.br.petguardian.status.Status;
import fiap.com.br.petguardian.status.StatusRepository;
import fiap.com.br.petguardian.tarefa.Tarefa;
import fiap.com.br.petguardian.tarefa.TarefaRepository;
import fiap.com.br.petguardian.telefone.Telefone;
import fiap.com.br.petguardian.telefone.TelefoneRepository;
import fiap.com.br.petguardian.tipoatendimento.EnumTipoAtendimento;
import fiap.com.br.petguardian.tipoatendimento.TipoAtendimento;
import fiap.com.br.petguardian.tipoatendimento.TipoAtendimentoRepository;
import fiap.com.br.petguardian.usuario.Usuario;
import fiap.com.br.petguardian.usuario.UsuarioRepository;
import fiap.com.br.petguardian.usuariopet.UsuarioPet;
import fiap.com.br.petguardian.usuariopet.UsuarioPetRepository;
import fiap.com.br.petguardian.veterinaria.Veterinaria;
import fiap.com.br.petguardian.veterinaria.VeterinariaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.HashSet;

@Configuration
public class DataMockConfig {

    @Bean
    CommandLineRunner seedData(
            EnderecoRepository enderecoRepository,
            BairroRepository bairroRepository,
            CidadeRepository cidadeRepository,
            EstadoRepository estadoRepository,
            FamiliaRepository familiaRepository,
            UsuarioRepository usuarioRepository,
            PetRepository petRepository,
            UsuarioPetRepository usuarioPetRepository,
            VeterinariaRepository veterinariaRepository,
            TarefaRepository tarefaRepository,
            AtendimentoRepository atendimentoRepository,
            StatusRepository statusRepository,
            TipoAtendimentoRepository tipoAtendimentoRepository,
            RacaRepository racaRepository,
            TelefoneRepository telefoneRepository
    ) {
        return args -> {
            if (familiaRepository.count() > 0) {
                return;
            }

            // 1. Endereços: Estado → Cidade → Bairro → Endereco
            Estado estado = estadoRepository.save(
                    Estado.builder().nome("São Paulo").build()
            );

            Cidade cidade = cidadeRepository.save(
                    Cidade.builder().nome("São Paulo").estado(estado).build()
            );

            Bairro bairro = bairroRepository.save(
                    Bairro.builder().nome("Pinheiros").cidade(cidade).build()
            );

            Endereco enderecoCarlos = enderecoRepository.save(
                    Endereco.builder().cep("05422010").numero("500").rua("Rua dos Pinheiros").bairro(bairro).build()
            );

            Endereco enderecoMariana = enderecoRepository.save(
                    Endereco.builder().cep("04533020").numero("12").rua("Avenida Faria Lima").bairro(bairro).build()
            );

            Endereco enderecoVet = enderecoRepository.save(
                    Endereco.builder().cep("01311200").numero("1000").rua("Avenida Paulista").bairro(bairro).build()
            );

            // 2. Telefones
            Telefone telCarlos = telefoneRepository.save(
                    Telefone.builder().ddd("11").numero("911112222").build()
            );

            Telefone telAna = telefoneRepository.save(
                    Telefone.builder().ddd("11").numero("933334444").build()
            );

            Telefone telMariana = telefoneRepository.save(
                    Telefone.builder().ddd("11").numero("955556666").build()
            );

            Telefone telVet = telefoneRepository.save(
                    Telefone.builder().ddd("11").numero("988888888").build()
            );

            // 3. Veterinária
            Veterinaria vet = veterinariaRepository.save(
                    Veterinaria.builder()
                            .nome("Clínica Pet Feliz")
                            .telefone(telVet)
                            .endereco(enderecoVet)
                            .build()
            );

            // 4. Raças
            Raca racaGolden = racaRepository.save(Raca.builder().nome("Golden Retriever").build());
            Raca racaSiames = racaRepository.save(Raca.builder().nome("Siamês").build());
            Raca racaViraLata = racaRepository.save(Raca.builder().nome("Vira-lata").build());

            // 5. Status e Tipos de Atendimento
            Status statusPendente = statusRepository.findByNomeStatus(EnumStatus.PENDENTE)
                    .orElseGet(() -> statusRepository.save(Status.builder().nome_status(EnumStatus.PENDENTE).build()));

            Status statusConcluido = statusRepository.findByNomeStatus(EnumStatus.CONCLUIDO)
                    .orElseGet(() -> statusRepository.save(Status.builder().nome_status(EnumStatus.CONCLUIDO).build()));

            Status statusExpirado = statusRepository.findByNomeStatus(EnumStatus.EXPIRADO)
                    .orElseGet(() -> statusRepository.save(Status.builder().nome_status(EnumStatus.EXPIRADO).build()));

            TipoAtendimento tipoConsulta = tipoAtendimentoRepository.findByTipoAtendimento(EnumTipoAtendimento.CONSULTA)
                    .orElseGet(() -> tipoAtendimentoRepository.save(TipoAtendimento.builder().tipoAtendimento(EnumTipoAtendimento.CONSULTA).build()));

            TipoAtendimento tipoVacinacao = tipoAtendimentoRepository.findByTipoAtendimento(EnumTipoAtendimento.VACINACAO)
                    .orElseGet(() -> tipoAtendimentoRepository.save(TipoAtendimento.builder().tipoAtendimento(EnumTipoAtendimento.VACINACAO).build()));

            TipoAtendimento tipoCirurgia = tipoAtendimentoRepository.findByTipoAtendimento(EnumTipoAtendimento.CIRURGIA)
                    .orElseGet(() -> tipoAtendimentoRepository.save(TipoAtendimento.builder().tipoAtendimento(EnumTipoAtendimento.CIRURGIA).build()));

            // 6. Família Silva
            Familia familiaSilva = familiaRepository.save(
                    Familia.builder().nome("Família Silva").usuarios(new HashSet<>()).build()
            );

            Usuario carlos = usuarioRepository.save(
                    Usuario.builder()
                            .nome("Carlos Silva")
                            .email("carlos.silva@petguardian.com")
                            .senha("123456")
                            .telefone(telCarlos)
                            .endereco(enderecoCarlos)
                            .familia(familiaSilva)
                            .build()
            );

            Usuario ana = usuarioRepository.save(
                    Usuario.builder()
                            .nome("Ana Silva")
                            .email("ana.silva@petguardian.com")
                            .senha("123456")
                            .telefone(telAna)
                            .endereco(enderecoCarlos)
                            .familia(familiaSilva)
                            .build()
            );

            Pet thor = petRepository.save(
                    Pet.builder()
                            .nome("Thor")
                            .idade(4)
                            .raca(racaGolden)
                            .porte(PetPorte.GRANDE)
                            .sexo('M')
                            .castrado(Boolean.TRUE)
                            .build()
            );

            Pet mel = petRepository.save(
                    Pet.builder()
                            .nome("Mel")
                            .idade(2)
                            .raca(racaSiames)
                            .porte(PetPorte.PEQUENO)
                            .sexo('F')
                            .castrado(Boolean.FALSE)
                            .build()
            );

            usuarioPetRepository.save(UsuarioPet.principal(carlos, thor));
            usuarioPetRepository.save(UsuarioPet.of(ana, thor, false));
            usuarioPetRepository.save(UsuarioPet.principal(ana, mel));

            // Tarefas da Família Silva
            tarefaRepository.save(
                    Tarefa.builder()
                            .titulo("Alimentar o Thor")
                            .descricao("Dar 300g de ração seca e trocar água.")
                            .pontosTarefa(10)
                            .criacao(LocalDateTime.now())
                            .prazo(LocalDateTime.now().plusHours(2))
                            .status(statusPendente)
                            .criador(carlos)
                            .pet(thor)
                            .build()
            );

            tarefaRepository.save(
                    Tarefa.builder()
                            .titulo("Dar banho no Thor")
                            .descricao("Levar ao pet shop ou dar banho no quintal.")
                            .pontosTarefa(50)
                            .criacao(LocalDateTime.now().minusDays(1))
                            .prazo(LocalDateTime.now().plusDays(1))
                            .conclusao(LocalDateTime.now())
                            .status(statusConcluido)
                            .criador(carlos)
                            .concluinte(carlos)
                            .pet(thor)
                            .build()
            );

            tarefaRepository.save(
                    Tarefa.builder()
                            .titulo("Escovar a Mel")
                            .descricao("Usar escova especial para pelos mortos.")
                            .pontosTarefa(15)
                            .criacao(LocalDateTime.now().minusDays(3))
                            .prazo(LocalDateTime.now().minusDays(1))
                            .status(statusExpirado)
                            .criador(ana)
                            .pet(mel)
                            .build()
            );

            // Atendimentos da Família Silva
            atendimentoRepository.save(
                    Atendimento.builder()
                            .tipo(tipoVacinacao)
                            .data(LocalDateTime.now().plusDays(2))
                            .anotacoes("Vacina V10 anual do Thor.")
                            .valor(150.0)
                            .status(statusPendente)
                            .pet(thor)
                            .veterinaria(vet)
                            .build()
            );

            atendimentoRepository.save(
                    Atendimento.builder()
                            .tipo(tipoConsulta)
                            .data(LocalDateTime.now().minusDays(2))
                            .anotacoes("Check-up geral da Mel.")
                            .valor(120.0)
                            .status(statusConcluido)
                            .pet(mel)
                            .veterinaria(vet)
                            .build()
            );

            // 7. Família Santos
            Familia familiaSantos = familiaRepository.save(
                    Familia.builder().nome("Família Santos").usuarios(new HashSet<>()).build()
            );

            Usuario mariana = usuarioRepository.save(
                    Usuario.builder()
                            .nome("Mariana Santos")
                            .email("mariana.santos@petguardian.com")
                            .senha("123456")
                            .telefone(telMariana)
                            .endereco(enderecoMariana)
                            .familia(familiaSantos)
                            .build()
            );

            Pet rex = petRepository.save(
                    Pet.builder()
                            .nome("Rex")
                            .idade(5)
                            .raca(racaViraLata)
                            .porte(PetPorte.MEDIO)
                            .sexo('M')
                            .castrado(Boolean.TRUE)
                            .build()
            );

            usuarioPetRepository.save(UsuarioPet.principal(mariana, rex));

            tarefaRepository.save(
                    Tarefa.builder()
                            .titulo("Passear com o Rex")
                            .descricao("Passeio de 30 minutos no parque.")
                            .pontosTarefa(20)
                            .criacao(LocalDateTime.now())
                            .prazo(LocalDateTime.now().plusHours(3))
                            .status(statusPendente)
                            .criador(mariana)
                            .pet(rex)
                            .build()
            );

            atendimentoRepository.save(
                    Atendimento.builder()
                            .tipo(tipoCirurgia)
                            .data(LocalDateTime.now().minusDays(5))
                            .anotacoes("Castração e pós-operatório do Rex.")
                            .valor(800.0)
                            .status(statusConcluido)
                            .pet(rex)
                            .veterinaria(vet)
                            .build()
            );
        };
    }
}
