package com.algaworks.ecommerce.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NamedStoredProcedureQuery(name = "compraram_acima_media", procedureName = "compraram_acima_media",
        parameters = @StoredProcedureParameter(name = "ano", type = Integer.class, mode = ParameterMode.IN),
        resultClasses = Cliente.class
)
@SecondaryTable(name = "cliente_detalhe",
        pkJoinColumns = @PrimaryKeyJoinColumn(name = "cliente_id"),
        foreignKey = @ForeignKey(name = "fk_cliente_detalhe_cliente")
)
@Entity
@Table(name = "cliente",
        uniqueConstraints = { @UniqueConstraint(name = "unq_cliente_cpf", columnNames = { "cpf" }) },
        indexes = { @Index(name = "idx_cliente_nome", columnList = "nome") })
public class Cliente extends EntidadeBaseInteger{

    @NotBlank
    @Column(length = 100, nullable = false)
    private String nome;

    @NotNull
    @Pattern(regexp = "(^\\d{3}\\x2E\\d{3}\\x2E\\d{3}\\x2D\\d{2}$)")
    @Column(length = 14, nullable = false)
    private String cpf;

    @Transient
    private String primeiroNome;

    @NotEmpty
    @ElementCollection
    @CollectionTable(name = "cliente_contato",
        joinColumns = @JoinColumn(name = "cliente_id", nullable = false,
                foreignKey = @ForeignKey(name = "fk_cliente_contato_cliente")))
    @MapKeyColumn(name = "tipo")
    @Column(name = "descricao")
    private Map<String, String> contatos;

    @NotNull
    @Column(table = "cliente_detalhe", length = 30, nullable = false)
    @Enumerated(EnumType.STRING)
    private SexoCliente sexo;

    @Past
    @Column(name = "data_nascimento", table = "cliente_detalhe")
    private LocalDate dataNascimento;

    @OneToMany(mappedBy = "cliente")
    private List<Pedido> pedidos;

    @PostLoad
    public void depoisDeCarregarEntidade() {
        if(this.nome != null && !this.nome.isBlank()) {
            int index = nome.indexOf(" ");
            this.primeiroNome = index > -1 ? nome.substring(0, index) : nome;
        }
    }
}
