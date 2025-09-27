package com.econome.domain;

import com.econome.participantes.enums.TipoParticipante;
import com.econome.participantes.enums.TipoPessoa;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.ZonedDateTime;

@Entity
@Data
@Table(name = "participantes")
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Participante implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "codigo", nullable = false, unique = true)
    private String codigo;

    @Column(name = "cpf_cnpj", nullable = false, unique = true)
    private String cpfCnpj;

    @Column(name = "nome")
    private String nome;

    @Column(name = "tipo_pessoa")
    @Enumerated(EnumType.STRING)
    private TipoPessoa tipoPessoa;

    @Column(name = "tipo_participante")
    @Enumerated(EnumType.STRING)
    private TipoParticipante tipoParticipante;

    @Column(name = "data_hora_cadastro", nullable = false)
    private ZonedDateTime dataHoraCadastro;

}
