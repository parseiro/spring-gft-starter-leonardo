package com.vilelapinheiro.springgftstarterleonardo.service;

import com.vilelapinheiro.springgftstarterleonardo.model.Cliente;

public interface ClienteService {
    Iterable<Cliente> findAll();

    Cliente buscarPorId(Integer id);

    Cliente inserir(Cliente cliente);

    Cliente atualizar(Integer id, Cliente cliente);

    void excluir(Integer id);
}
