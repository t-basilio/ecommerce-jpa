package com.algaworks.ecommerce.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@DiscriminatorValue("cartao")
@Entity
//@Table(name = "pagamento_cartao")
public class PagamentoCartao extends Pagamento {

    @NotBlank
    @Column(name = "numero_cartao", length = 50)
    private String numeroCartao;
}
