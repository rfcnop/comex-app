package br.com.alura.comex.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import br.com.alura.comex.dto.RequestCategoriaDto;
import br.com.alura.comex.service.CategoriaService;


@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @PostMapping
    private ResponseEntity<Object> cadastrar(@RequestBody @Valid RequestCategoriaDto request, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            var fieldError = bindingResult.getFieldError("nome");
            String mensagem = "";
            if (fieldError != null)
                mensagem = fieldError.getDefaultMessage();
            return new ResponseEntity<>(mensagem, HttpStatus.BAD_REQUEST);
        }

        var categoria = request.toCategoria();
        categoriaService.cadastrar(categoria);

        return new ResponseEntity<>(categoria, HttpStatus.OK);
    }
}
