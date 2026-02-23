package com.conectaobra.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Table(name = "tb_role")
@Data
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    @NotNull(message = "Não pode ser nulo")
    @Min(value = 1, message = "Tem que possui um valor igual ou superior a 1")
    private Long id;

    @Column(unique = true,name = "nome")
    @NotBlank(message = "Não pode ser vazio")
    private String nome;
}