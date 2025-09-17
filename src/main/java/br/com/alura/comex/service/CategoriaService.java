package br.com.alura.comex.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.alura.comex.model.Categoria;
import br.com.alura.comex.repository.CategoriaRepository;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository repository;

    public void cadastrar(Categoria categoria) {
        if (categoria == null)
            return;
        if (repository.existsByNome(categoria.getNome()))
            return;

        repository.save(categoria);
    }

    public Categoria buscaPorId(Long idDaCategoria) {
        var optional = repository.findById(idDaCategoria);
        if (!optional.isPresent())
            return null;
        
        return optional.get();
    }
  
}
