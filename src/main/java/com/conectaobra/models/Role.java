package com.conectaobra.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Table(name = "tb_role")
@Data
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "role_id")
    @NotNull(message = "Campo obrigatório!")
    @Min(value = 1, message = "Deve possuir um valor igual ou superior a 1!")
    private Long id;

    @Column(unique = true,name = "nome")
    private String nome;
}