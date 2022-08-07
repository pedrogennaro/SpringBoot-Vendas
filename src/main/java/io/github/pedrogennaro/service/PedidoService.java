package io.github.pedrogennaro.service;

import io.github.pedrogennaro.api.dto.PedidoDTO;
import io.github.pedrogennaro.domain.entity.Pedido;
import io.github.pedrogennaro.domain.enums.StatusPedido;

import java.util.Optional;

public interface PedidoService {
    Pedido salvar(PedidoDTO pedidoDTO);
    Optional<Pedido> getPedidoById(Integer id);
    void updateStatusPedido(Integer id, StatusPedido statusPedido);
}
