package com.vilelapinheiro.springgftstarterleonardo.controller;

import com.vilelapinheiro.springgftstarterleonardo.model.Cliente;
import com.vilelapinheiro.springgftstarterleonardo.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("cliente")
public class ClienteController {
    @Autowired
    private ClienteService clienteService;

    @GetMapping
    public ResponseEntity<Iterable<Cliente>> buscarTodos() {
        return ResponseEntity.ok(clienteService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok().body(clienteService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Cliente> novoCliente(@RequestBody Cliente cliente) {
        var newEntity = clienteService.inserir(cliente);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(newEntity.getId()).toUri();
        return ResponseEntity.created(uri).body(newEntity);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cliente> atualizaCliente(@PathVariable Integer id, @RequestBody Cliente cliente) {
        clienteService.atualizar(id, cliente);

        return ResponseEntity.accepted().body(cliente);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        clienteService.excluir(id);
        return ResponseEntity.ok().build();
    }
}
