package com.algaworks.ecommerce.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@DiscriminatorValue("cartao")
@Entity
//@Table(name = "pagamento_cartao")
public class PagamentoCartao extends Pagamento {

    @Column(name = "numero_cartao", length = 50)
    private String numeroCartao;
}
