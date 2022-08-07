package io.github.pedrogennaro.api.controller;

import io.github.pedrogennaro.api.dto.AtualizacaoStatusPedidoDTO;
import io.github.pedrogennaro.api.dto.InformacaoItemPedidoDTO;
import io.github.pedrogennaro.api.dto.InformacoesPedidoDTO;
import io.github.pedrogennaro.api.dto.PedidoDTO;
import io.github.pedrogennaro.domain.entity.ItemPedido;
import io.github.pedrogennaro.domain.entity.Pedido;
import io.github.pedrogennaro.domain.enums.StatusPedido;
import io.github.pedrogennaro.service.PedidoService;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Integer save(@RequestBody PedidoDTO pedidoDTO){
        Pedido pedido = pedidoService.salvar(pedidoDTO);
        return pedido.getId();
    }

    @GetMapping("/{id}")
    public InformacoesPedidoDTO getPedidoById(@PathVariable Integer id){
        return pedidoService.getPedidoById(id)
                .map(p -> converterPedido(p))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido não encontrado"));
    }

    private InformacoesPedidoDTO converterPedido(Pedido pedido){
        return InformacoesPedidoDTO.builder()
                .id(pedido.getId())
                .dataPedido(pedido.getDataPedido().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                .cpf(pedido.getCliente().getCpf())
                .nomeCliente(pedido.getCliente().getNome())
                .total(pedido.getTotal())
                .status(pedido.getStatus().name())
                .items(converterItemsPedido(pedido.getItensPedido()))
                .build();
    }

    private List<InformacaoItemPedidoDTO> converterItemsPedido(List<ItemPedido> itens){
        if(CollectionUtils.isEmpty(itens)){
            return Collections.emptyList();
        }
        return itens.stream()
                .map(item -> InformacaoItemPedidoDTO.builder()
                        .descricaoProduto(item.getProduto().getDescricao())
                        .precoUnitario(item.getProduto().getPreco())
                        .quantidade(item.getQuantidade())
                        .build()
                ).collect(Collectors.toList());
    }

    @PatchMapping("/{id}")
    public void updateStatus(@PathVariable Integer id, @RequestBody AtualizacaoStatusPedidoDTO statusDto){
        String novoStatus = statusDto.getNovoStatus();
        pedidoService.updateStatusPedido(id, StatusPedido.valueOf(novoStatus));
    }

}
