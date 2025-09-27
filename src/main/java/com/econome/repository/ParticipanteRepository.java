package com.econome.repository;

import com.econome.domain.Participante;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repositório Spring Data JPA para a entidade Participante.
 * Centraliza operações de persistência e favorece testabilidade da camada de serviço.
 */
public interface ParticipanteRepository extends JpaRepository<Participante, Long> {

    Optional<Participante> findByCodigo(String codigo);

    Optional<Participante> findByCpfCnpj(String cpfCnpj);

    boolean existsByCodigo(String codigo);

    boolean existsByCpfCnpj(String cpfCnpj);
}
