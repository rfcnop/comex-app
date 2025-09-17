package br.com.alura.comex.dto;

import br.com.alura.comex.model.Categoria;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RequestCategoriaDto(
    @NotNull(message = "O nome da categoria não pode ser nulo.")
    @Size(min = 2, max = 50, message = "O nome da categoria precisa ter no mínimo 2 caracteres e no máximo 50.")
    String nome)
{
    public Categoria toCategoria() {
        return new Categoria(nome, true);
    }
}
