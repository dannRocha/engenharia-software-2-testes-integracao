package org.integracao.entity;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "alugueis")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class Aluguel implements BaseEntity<Integer> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_aluguel")
    private Integer id;

    @Column(name = "data_vencimento")
    @JsonProperty(value = "data_vencimento")
    private String dataVencimento;

    @Column(name = "valor_pago")
    @JsonProperty(value = "valor_pago")
    private Double valorPago;

    @Column(name = "data_pagamento")
    @JsonProperty(value = "data_pagamento")
    private String dataPagamento;

    @Column(name = "obs")
    private String obervacao;

    @ManyToOne
    @JoinColumn(name = "id_locacao", nullable = false)
    private Locacao locacao;
}
