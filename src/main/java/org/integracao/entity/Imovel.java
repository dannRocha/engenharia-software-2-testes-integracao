package org.integracao.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "imoveis")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class Imovel implements BaseEntity<Integer> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_imovel")
    private Integer id;

    private String endereco;
    private String bairro;
    private String cep;
    private Double metragem;
    private Short banheiros;
    private Short dormitorios;
    private Short suites;

    @Column(name = "tipo_imovel")
    @JsonProperty(value = "tipo")
    private String tipo;

    @Column(name = "vagas_garagem")
    @JsonProperty(value = "vagas_garagem")
    private Short vagasGaragem;

    @Column(name = "valor_aluguel_sugerido")
    @JsonProperty(value = "valor_aluguel_sugerido")
    private Double valorAluguel;

    @Column(name = "observacao")
    private String observacao;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinTable (
        joinColumns = @JoinColumn ( name =" id_imovel"),
        inverseJoinColumns = @JoinColumn ( name =" id_locacao "))
    private List<Locacao> locacoes;
}
