package com.bootcampjava.starwars.repository;

import com.bootcampjava.starwars.model.Jedi;

import java.util.List;
import java.util.Optional;

public interface JediRepository {

    //Queries

    Optional<Jedi> findById(Integer id); //procura por id, tipo de retorno é opcional

    List<Jedi> findAll(); //procura tudo, retorna uma lista

    boolean update(Jedi jedi); //atualizar

    Jedi save(Jedi jedi); //salvar um Jedi

    boolean delete(Integer id); //apagar um id
}
