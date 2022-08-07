package io.github.pedrogennaro.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class InformacoesPedidoDTO {
    private Integer id;
    private String dataPedido;
    private String cpf;
    private String nomeCliente;
    private BigDecimal total;
    private String status;
    private List<InformacaoItemPedidoDTO> items;
}
