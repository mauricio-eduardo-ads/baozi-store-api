
package com.baozi.store.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.baozi.store.model.Pedido;
import com.baozi.store.model.Cliente;
import com.baozi.store.model.Produto;

import com.baozi.store.repository.PedidoRepository;
import com.baozi.store.repository.ClienteRepository;
import com.baozi.store.repository.ProdutoRepository;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    private final PedidoRepository pedidoRepository;
    private final ClienteRepository clienteRepository;
    private final ProdutoRepository produtoRepository;

    public PedidoController(
            PedidoRepository pedidoRepository,
            ClienteRepository clienteRepository,
            ProdutoRepository produtoRepository) {

        this.pedidoRepository = pedidoRepository;
        this.clienteRepository = clienteRepository;
        this.produtoRepository = produtoRepository;
    }

    // Registrar pedido
    @PostMapping
    public ResponseEntity<Pedido> criar(@RequestBody Pedido pedido) {

        if (pedido.getCliente() == null ||
            pedido.getProduto() == null ||
            pedido.getCliente().getId() == null ||
            pedido.getProduto().getId() == null ||
            pedido.getQuantidade() == null ||
            pedido.getQuantidade() <= 0) {

            return ResponseEntity.badRequest().build();
        }

        Optional<Cliente> cliente = clienteRepository.findById(
            pedido.getCliente().getId()
        );

        Optional<Produto> produto = produtoRepository.findById(
            pedido.getProduto().getId()
        );

        if (cliente.isEmpty() || produto.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        pedido.setCliente(cliente.get());
        pedido.setProduto(produto.get());

        Pedido pedidoSalvo = pedidoRepository.save(pedido);

        return ResponseEntity.ok(pedidoSalvo);
    }

    // Listar todos os pedidos
    @GetMapping
    public List<Pedido> listarTodos() {
        return pedidoRepository.findAll();
    }

    // Consultar pedido por ID
    @GetMapping("/{id}")
    public ResponseEntity<Pedido> buscarPorId(@PathVariable Long id) {

        Optional<Pedido> pedido = pedidoRepository.findById(id);

        if (pedido.isPresent()) {
            return ResponseEntity.ok(pedido.get());
        }

        return ResponseEntity.notFound().build();
    }

    // Excluir pedido
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {

        if (!pedidoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        pedidoRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
