package com.vilelapinheiro.springgftstarterleonardo.repository;

import com.vilelapinheiro.springgftstarterleonardo.model.Cliente;
import org.springframework.data.repository.CrudRepository;

public interface ClientRepository extends CrudRepository<Cliente, Integer> {
}
