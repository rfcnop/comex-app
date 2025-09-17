package br.com.alura.comex.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.alura.comex.model.Produto;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

}
