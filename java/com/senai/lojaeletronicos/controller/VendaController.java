package com.senai.lojaeletronicos.controller;

import com.senai.lojaeletronicos.model.Cliente;
import com.senai.lojaeletronicos.model.Produto;
import com.senai.lojaeletronicos.model.Venda;
import com.senai.lojaeletronicos.repository.ClienteRepository;
import com.senai.lojaeletronicos.repository.ProdutoRepository;
import com.senai.lojaeletronicos.repository.VendaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/vendas")
@CrossOrigin("*")
public class VendaController {

    private final VendaRepository vendaRepository;
    private final ClienteRepository clienteRepository;
    private final ProdutoRepository produtoRepository;

    public VendaController(VendaRepository vendaRepository,
                           ClienteRepository clienteRepository,
                           ProdutoRepository produtoRepository) {
        this.vendaRepository = vendaRepository;
        this.clienteRepository = clienteRepository;
        this.produtoRepository = produtoRepository;
    }

    @GetMapping
    public List<Venda> listarTodas() {
        return vendaRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        Venda venda = vendaRepository.findById(id).orElse(null);

        if (venda == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("erro", "Venda não encontrada."));
        }

        return ResponseEntity.ok(venda);
    }

    @PostMapping
    public ResponseEntity<?> cadastrar(@RequestBody Venda venda) {
        if (venda == null) {
            return ResponseEntity.badRequest().body(Map.of("erro", "Dados da venda não enviados."));
        }

        if (venda.getCliente() == null || venda.getCliente().getId() == null) {
            return ResponseEntity.badRequest().body(Map.of("erro", "Cliente da venda é obrigatório."));
        }

        if (venda.getProduto() == null || venda.getProduto().getId() == null) {
            return ResponseEntity.badRequest().body(Map.of("erro", "Produto da venda é obrigatório."));
        }

        if (venda.getQuantidade() == null || venda.getQuantidade() <= 0) {
            return ResponseEntity.badRequest().body(Map.of("erro", "Quantidade deve ser maior que zero."));
        }

        if (venda.getDataVenda() == null) {
            return ResponseEntity.badRequest().body(Map.of("erro", "Data da venda é obrigatória."));
        }

        Cliente cliente = clienteRepository.findById(venda.getCliente().getId()).orElse(null);
        Produto produto = produtoRepository.findById(venda.getProduto().getId()).orElse(null);

        if (cliente == null) {
            return ResponseEntity.badRequest().body(Map.of("erro", "Cliente não encontrado."));
        }

        if (produto == null) {
            return ResponseEntity.badRequest().body(Map.of("erro", "Produto não encontrado."));
        }

        if (produto.getEstoque() < venda.getQuantidade()) {
            return ResponseEntity.badRequest().body(Map.of("erro", "Estoque insuficiente para realizar a venda."));
        }

        venda.setCliente(cliente);
        venda.setProduto(produto);
        venda.setValorTotal(produto.getPreco().multiply(BigDecimal.valueOf(venda.getQuantidade())));

        produto.setEstoque(produto.getEstoque() - venda.getQuantidade());
        produtoRepository.save(produto);

        Venda salva = vendaRepository.save(venda);
        return ResponseEntity.status(HttpStatus.CREATED).body(salva);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody Venda venda) {
        Venda existente = vendaRepository.findById(id).orElse(null);

        if (existente == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("erro", "Venda não encontrada para atualização."));
        }

        if (venda == null) {
            return ResponseEntity.badRequest().body(Map.of("erro", "Dados da venda não enviados."));
        }

        if (venda.getCliente() == null || venda.getCliente().getId() == null) {
            return ResponseEntity.badRequest().body(Map.of("erro", "Cliente da venda é obrigatório."));
        }

        if (venda.getProduto() == null || venda.getProduto().getId() == null) {
            return ResponseEntity.badRequest().body(Map.of("erro", "Produto da venda é obrigatório."));
        }

        if (venda.getQuantidade() == null || venda.getQuantidade() <= 0) {
            return ResponseEntity.badRequest().body(Map.of("erro", "Quantidade deve ser maior que zero."));
        }

        if (venda.getDataVenda() == null) {
            return ResponseEntity.badRequest().body(Map.of("erro", "Data da venda é obrigatória."));
        }

        Cliente cliente = clienteRepository.findById(venda.getCliente().getId()).orElse(null);
        Produto produto = produtoRepository.findById(venda.getProduto().getId()).orElse(null);

        if (cliente == null) {
            return ResponseEntity.badRequest().body(Map.of("erro", "Cliente não encontrado."));
        }

        if (produto == null) {
            return ResponseEntity.badRequest().body(Map.of("erro", "Produto não encontrado."));
        }

        existente.setDataVenda(venda.getDataVenda());
        existente.setQuantidade(venda.getQuantidade());
        existente.setCliente(cliente);
        existente.setProduto(produto);
        existente.setValorTotal(produto.getPreco().multiply(BigDecimal.valueOf(venda.getQuantidade())));

        Venda atualizada = vendaRepository.save(existente);
        return ResponseEntity.ok(atualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        Venda venda = vendaRepository.findById(id).orElse(null);

        if (venda == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("erro", "Venda não encontrada para exclusão."));
        }

        vendaRepository.deleteById(id);
        return ResponseEntity.ok(Map.of("mensagem", "Venda deletada com sucesso."));
    }
}