package io.github.pedrogennaro.domain.repository;

import io.github.pedrogennaro.domain.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Produtos extends JpaRepository<Produto, Integer> {
}
