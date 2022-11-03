package org.integracao.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "clientes")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class Cliente implements BaseEntity<Integer> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_cliente")
    private Integer id;

    @Column(name = "nome_cliente")
    private String nome;

    @Column(name = "cpf")
    private String cpf;

    @Column(name = "telefone1")
    @JsonProperty(value = "telefone1")
    private String telefoneOne;

    @Column(name = "telefone2")
    @JsonProperty(value = "telefone2")
    private String telefoneTwo;

    @Column(name = "email")
    private String email;

    @Column(name = "dt_nascimento")
    @JsonProperty(value = "dt_nascimento")
    private String dataNascimento;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable (
        joinColumns = @JoinColumn ( name =" id_cliente"),
        inverseJoinColumns = @JoinColumn ( name =" id_locacao "))
    private List<Locacao> locacoes;

}
