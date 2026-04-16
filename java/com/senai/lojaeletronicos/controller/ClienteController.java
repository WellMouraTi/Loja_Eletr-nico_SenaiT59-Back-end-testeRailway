package com.senai.lojaeletronicos.controller;

import com.senai.lojaeletronicos.model.Cliente;
import com.senai.lojaeletronicos.repository.ClienteRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes")
@CrossOrigin("*")
public class ClienteController {

    private final ClienteRepository repository;

    public ClienteController(ClienteRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Cliente> listarTodos() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Cliente buscarPorId(@PathVariable Long id) {
        return repository.findById(id).orElse(null);
    }

    @GetMapping("/buscar/{nome}")
    public List<Cliente> buscarPorNome(@PathVariable String nome) {
        return repository.findByNomeContainingIgnoreCase(nome);
    }

    @PostMapping
    public Cliente cadastrar(@RequestBody Cliente cliente) {
        return repository.save(cliente);
    }

    @PutMapping("/{id}")
    public Cliente atualizar(@PathVariable Long id, @RequestBody Cliente cliente) {
        Cliente existente = repository.findById(id).orElse(null);

        if (existente != null) {
            existente.setNome(cliente.getNome());
            existente.setCpf(cliente.getCpf());
            existente.setTelefone(cliente.getTelefone());
            existente.setEmail(cliente.getEmail());
            existente.setCidade(cliente.getCidade());
            return repository.save(existente);
        }

        return null;
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        repository.deleteById(id);
    }
}