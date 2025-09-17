package br.com.alura.comex.dto;

import br.com.alura.comex.model.Categoria;
import br.com.alura.comex.model.Produto;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record RequestProdutoDto(
    @NotNull(message = "O nome do produto não pode ser nulo.")
    @Size(min = 2, max = 50, message = "O nome do produto precisa ter no mínimo 2 caracteres e no máximo 50.")
    String nome,

    @NotNull(message = "O preço é de preenchimento obrigatório.")
    @Positive(message = "O preço deve ser um número positivo.") Double preco,

    @Size(max = 100, message = "A descrição deve possuir no máximo 100 caractetes.")
    String descricao,

    @NotNull(message = "A quantidade em estoque é de preenchimento obrigatório.")
    Integer quantidadeEmEstoque,

    @NotNull(message = "O ID da categoria é de preenchimento obrigatório.")
    Long idDaCategoria) {

    public Produto toProduto(Categoria categoria) {
        return new Produto(nome, descricao, preco, quantidadeEmEstoque, categoria);
    }
    
}
