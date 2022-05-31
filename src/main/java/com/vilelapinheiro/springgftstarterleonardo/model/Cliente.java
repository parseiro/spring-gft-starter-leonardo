package com.vilelapinheiro.springgftstarterleonardo.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Integer id;

    @Getter @Setter
    private String nome;

    @ManyToOne
    @Getter @Setter
    private Endereco endereco;
}
