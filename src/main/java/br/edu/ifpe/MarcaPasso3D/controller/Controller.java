package br.edu.ifpe.MarcaPasso3D.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class Controller {
    @GetMapping
    public String hellouword(){
        return "Ola";
    }
}
