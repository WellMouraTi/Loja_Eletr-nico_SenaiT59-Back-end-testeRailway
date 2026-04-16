package com.senai.lojaeletronicos.controller;

import com.senai.lojaeletronicos.model.Produto;
import com.senai.lojaeletronicos.repository.ProdutoRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/produtos")
@CrossOrigin("*")
public class ProdutoController {

    private final ProdutoRepository repository;

    public ProdutoController(ProdutoRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Produto> listarTodos() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Produto buscarPorId(@PathVariable Long id) {
        return repository.findById(id).orElse(null);
    }

    @GetMapping("/buscar/{nome}")
    public List<Produto> buscarPorNome(@PathVariable String nome) {
        return repository.findByNomeContainingIgnoreCase(nome);
    }

    @PostMapping
    public Produto cadastrar(@RequestBody Produto produto) {
        return repository.save(produto);
    }

    @PutMapping("/{id}")
    public Produto atualizar(@PathVariable Long id, @RequestBody Produto produto) {
        Produto existente = repository.findById(id).orElse(null);

        if (existente != null) {
            existente.setNome(produto.getNome());
            existente.setMarca(produto.getMarca());
            existente.setCategoria(produto.getCategoria());
            existente.setPreco(produto.getPreco());
            existente.setEstoque(produto.getEstoque());
            return repository.save(existente);
        }

        return null;
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        repository.deleteById(id);
    }
}