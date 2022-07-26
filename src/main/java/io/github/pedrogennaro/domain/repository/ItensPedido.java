package io.github.pedrogennaro.domain.repository;

import io.github.pedrogennaro.domain.entity.ItemPedido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItensPedido extends JpaRepository<ItemPedido, Integer> {
}
