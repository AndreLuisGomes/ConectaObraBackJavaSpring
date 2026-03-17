package com.conectaobra.controllers;

import com.conectaobra.models.Role;
import com.conectaobra.services.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
public class RoleController {

    // Dependências

    private final RoleService roleService;

    //

    @GetMapping("/listar")
    List<Role> listarRoles(){
        return roleService.obterTodas();
    }

}
