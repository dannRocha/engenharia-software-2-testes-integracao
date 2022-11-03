package org.integracao.entity;

import java.time.LocalDate;
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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "locacao")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class Locacao implements BaseEntity<Integer> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_locacao")
    private Integer id;

    @Column(name = "valor_aluguel")
    @JsonProperty(value = "valor_aluguel")
    private Double valorAluguel;

    @Column(name = "pertecentual_multa")
    @JsonProperty(value = "pertecentual_multa")
    private Double pertecentualMulta;

    @Column(name = "dia_vencimento")
    @JsonProperty(value = "dia_vencimento")
    private Integer diaVencimento;

    @Column(name = "data_inicio")
    @JsonProperty(value = "data_inicio")
    private String dataInicio;

    @Column(name = "data_fim")
    @JsonProperty(value = "data_fim")
    private String dataFim;

    @Column(name = "ativo")
    private Boolean ativo;

    @Column(name = "obs")
    private String observacao;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(
        joinColumns = @JoinColumn(name = "id_locacao"), 
        inverseJoinColumns = @JoinColumn(name = "id_aluguel"))
    private List<Aluguel> alugueis;

    @ManyToOne
    @JoinColumn(name = "id_cliente", referencedColumnName = "id_cliente")
    private Cliente cliente;

    @ManyToOne(cascade = CascadeType.REFRESH, targetEntity = Imovel.class)
    // @JoinColumn(name = "id_imovel", nullable = false, insertable = false, updatable = false)
    private Imovel imovel;
}
