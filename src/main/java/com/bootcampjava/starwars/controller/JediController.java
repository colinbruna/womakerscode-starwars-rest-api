package com.bootcampjava.starwars.controller;

import com.bootcampjava.starwars.model.Jedi;
import com.bootcampjava.starwars.service.JediService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
public class JediController {

    private static final Logger logger = LogManager.getLogger(JediService.class);

    private final JediService jediService;

    public JediController(JediService jediService) {
        this.jediService = jediService;
    }

    //criando método de GET         (@PathVariable para definir o nome da variável, nesse caso o id)
    @GetMapping("/jedi/{id}")//criando a minha rota
    public ResponseEntity<?> getJedi(@PathVariable int id) {

        return jediService.findById(id)
                .map(jedi -> { //ajuda a mapear as informções usando lambda function
                    try {
                        return ResponseEntity
                                .ok() // a resposta seja ok caso tenha dado certo
                                .eTag(Integer.toString(jedi.getVersion())) //eTag trazendo a versão desse jedi
                                .location(new URI("/jedi/" + jedi.getId()))//colocar e descrever a URI dessa requisição
                                .body(jedi);//o que eu quero que tenha no corpo da requisição
                    } catch (URISyntaxException e) {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); //vai retornar esse status caso dê algum erro
                    }
                })
                .orElse(ResponseEntity.notFound().build());
    }
    //criando o método POST
    @PostMapping("/jedi/")
    public ResponseEntity<Jedi> saveJedi(@RequestBody Jedi jedi) {

        Jedi newJedi = jediService.save(jedi);

        try {
            return ResponseEntity
                    .created(new URI("/jedi/" + newJedi.getId()))
                    .eTag(Integer.toString(newJedi.getVersion()))
                    .body(newJedi);
        } catch (URISyntaxException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}