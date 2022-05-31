package com.vilelapinheiro.springgftstarterleonardo.service;

import com.vilelapinheiro.springgftstarterleonardo.model.Cliente;
import com.vilelapinheiro.springgftstarterleonardo.model.Endereco;
import com.vilelapinheiro.springgftstarterleonardo.repository.ClientRepository;
import com.vilelapinheiro.springgftstarterleonardo.repository.EnderecoRepository;
import com.vilelapinheiro.springgftstarterleonardo.service.exceptions.ResourceNotFoundException;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/* Implementa ClienteService como uma Estratégia
 *
 ** Author: Leonardo Vilela Pinheiro
 */
@Service
public class ClienteServiceImpl implements ClienteService {

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    EnderecoRepository enderecoRepository;

    @Autowired
    CepService cepService;

    @Transactional(readOnly = true)
    @Override
    public Iterable<Cliente> findAll() {
        return clientRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Cliente buscarPorId(Integer id) {
        var obj = clientRepository.findById(id);
        Cliente entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
        return entity;
    }

    @Transactional(readOnly = false)
    @Override
    public Cliente inserir(Cliente cliente) {
        return salvarClienteComCep(cliente);
    }

    @Transactional(readOnly = false)
    @Override
    public Cliente atualizar(Integer id, Cliente cliente) {
        var clienteEncontrado = clientRepository.findById(id);
        if (clienteEncontrado.isPresent()) {
            return salvarClienteComCep(cliente);
        } else {
            throw new ResourceNotFoundException("Entity not found");
        }
    }

    @Transactional(readOnly = false)
    @Override
    public void excluir(Integer id) {
        if (clientRepository.existsById(id)) {
            clientRepository.deleteById(id);
        }
    }

    private Cliente salvarClienteComCep(Cliente cliente) {
        // Verificar se o Endereco do Cliente já existe (pelo CEP).
        String cep = cliente.getEndereco().getCep();
        Endereco endereco = enderecoRepository.findById(cep).orElseGet(() -> {
            // Caso não exista, integrar com o ViaCEP e persistir o retorno.
            Endereco novoEndereco;
            try {
                novoEndereco = cepService.consultaCep(cep);
            } catch (FeignException e) {
                throw new ResourceNotFoundException("CEP not found");
            }
            if (novoEndereco.getCep() == null) {
                throw new ResourceNotFoundException("CEP not found");
            }
            enderecoRepository.save(novoEndereco);
            return novoEndereco;
        });
        cliente.setEndereco(endereco);
        // Inserir Cliente, vinculando o Endereco (novo ou existente).
        return clientRepository.save(cliente);
    }
}
