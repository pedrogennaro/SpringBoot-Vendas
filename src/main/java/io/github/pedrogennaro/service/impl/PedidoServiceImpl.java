package io.github.pedrogennaro.service.impl;

import io.github.pedrogennaro.api.dto.ItemPedidoDTO;
import io.github.pedrogennaro.api.dto.PedidoDTO;
import io.github.pedrogennaro.domain.entity.Cliente;
import io.github.pedrogennaro.domain.entity.ItemPedido;
import io.github.pedrogennaro.domain.entity.Pedido;
import io.github.pedrogennaro.domain.entity.Produto;
import io.github.pedrogennaro.domain.enums.StatusPedido;
import io.github.pedrogennaro.domain.repository.Clientes;
import io.github.pedrogennaro.domain.repository.ItensPedido;
import io.github.pedrogennaro.domain.repository.Pedidos;
import io.github.pedrogennaro.domain.repository.Produtos;
import io.github.pedrogennaro.exception.PedidoNaoEncontradoException;
import io.github.pedrogennaro.exception.RegraNegocioException;
import io.github.pedrogennaro.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PedidoServiceImpl implements PedidoService {

    private final Pedidos pedidosRepository;
    private final Clientes clientesRepository;
    private final Produtos produtosRepository;
    private final ItensPedido itensPedidoRepository;

    @Override
    @Transactional
    public Pedido salvar(PedidoDTO pedidoDTO) {

        Integer idCliente = pedidoDTO.getCliente();
        Cliente cliente = clientesRepository
                .findById(idCliente)
                .orElseThrow(() -> new RegraNegocioException("Código de cliente inválido"));

        Pedido pedido = new Pedido();
        pedido.setTotal(pedidoDTO.getTotal());
        pedido.setDataPedido(LocalDate.now());
        pedido.setCliente(cliente);
        pedido.setStatus(StatusPedido.REALIZADO);

        List<ItemPedido> itemPedidos = converterItems(pedido, pedidoDTO.getItems());

        pedidosRepository.save(pedido);
        itensPedidoRepository.saveAll(itemPedidos);

        pedido.setItensPedido(itemPedidos);

        return pedido;
    }

    @Override
    public Optional<Pedido> getPedidoById(Integer id) {
        return pedidosRepository.findByIdFetchItens(id);
    }

    @Override
    @Transactional
    public void updateStatusPedido(Integer id, StatusPedido statusPedido) {
        pedidosRepository.findById(id).map(pedido -> {
            pedido.setStatus(statusPedido);
            return pedidosRepository.save(pedido);
        }).orElseThrow(() -> new PedidoNaoEncontradoException());
    }

    private List<ItemPedido> converterItems(Pedido pedido, List<ItemPedidoDTO> items){
        if(items.isEmpty()){
            throw new RegraNegocioException("Não é possível realizar um pedido sem items");
        }
        return items.stream().map(dto -> {

            Integer idProduto = dto.getProduto();
            Produto produto = produtosRepository
                    .findById(idProduto)
                    .orElseThrow(() -> new RegraNegocioException("Código de produto inválido: " + idProduto));

            ItemPedido itemPedido = new ItemPedido();
            itemPedido.setQuantidade(dto.getQuantidade());
            itemPedido.setPedido(pedido);
            itemPedido.setProduto(produto);
            return itemPedido;
        }).collect(Collectors.toList());

    }
}
