package br.com.evelyn.calorias.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HelloWorldController {    // ENDPOINT

    @GetMapping("/hello")
    public String getHelloWorld(){
        return "<h1>Hello World!<h1>";
    }

    @GetMapping("/ola")
    public String getOlaMundo(){
        return "<h1>Olá Mundo!<h1>";
    }
}
