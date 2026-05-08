package br.com.evelyn.calorias.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tbl_usuarios")
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Usuario {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "SEQ_USUARIOS"
    )
    @SequenceGenerator(
            name = "SEQ_USUARIOS",
            sequenceName = "SEQ_USUARIOS",
            allocationSize = 1
    )
    @Column(name = "usuario_id")
    @JsonAlias("usuario_id")
    private Long usuarioId;

    private String nome;
    private String email;
    private String senha;
}