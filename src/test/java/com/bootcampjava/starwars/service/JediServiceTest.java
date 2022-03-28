package com.bootcampjava.starwars.service;

import com.bootcampjava.starwars.model.Jedi;
import com.bootcampjava.starwars.repository.JediRepositoryImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class) //sempre precisa colocar essa anotação na classe de teste
@SpringBootTest //anotação da classe de teste do próprio spring
public class JediServiceTest {

    @Autowired //sobrescrevendo a minha classe JediService
    private JediService jediService;

    @MockBean //mock de uma classe
    private JediRepositoryImpl jediRepository;

    //teste para caso seja encontrado com sucesso
    @Test
    @DisplayName("Should return Jedi with success")
    public void testFindBySuccess() {

        // cenario
        Jedi mockJedi = new Jedi(1, "Jedi Name", 10, 1); //mock de um objeto
        Mockito.doReturn(Optional.of(mockJedi)).when(jediRepository).findById(1); //Retornar um optional de um mockJedi quando o jediRepository for id 1

        // execucao
        Optional<Jedi> returnedJedi = jediService.findById(1); //Optional da classe Jedi

        // assert
        Assertions.assertTrue(returnedJedi.isPresent(), "Jedi was not found"); //garantir que o Jedi retornado está presente, com esta mensagem caso não seja achado
        Assertions.assertSame(returnedJedi.get(), mockJedi, "Jedis must be the same"); //meu jedi retornado tem que ser o mesmo do mock, senão retorna uma mensagem
    }

    // TODO: Criar teste de erro NOT FOUND
    @Test
    @DisplayName("Jedi not found")
    public void testIdNotFound() {
        // cenario
        Mockito.doReturn(Optional.empty()).when(jediRepository).findById(1);

        // execucao
        Optional<Jedi> returnedJedi = jediService.findById(1);

        // assert
        Assertions.assertFalse(returnedJedi.isPresent(), "Jedi was not found");
    }

    // TODO: Criar um teste pro findAll();
    @Test
    @DisplayName("Should find all Jedis")
    public void testFindAll() {

        //cenario
        Jedi mockJedi = new Jedi(1, "Jedi Name", 10, 1);
        Jedi mockJedi2 = new Jedi(2, "Jedi Name 2", 10, 1);
        List<Jedi> mockJediList = new ArrayList<>();
        mockJediList.add(mockJedi);
        mockJediList.add(mockJedi2);
        Mockito.doReturn(mockJediList).when(jediRepository).findAll();

        // execucao
        List<Jedi> returnedJedisList = jediService.findAll();

        // assert
        Assertions.assertFalse(returnedJedisList.isEmpty(),"The list cannot be empty");
        //outro teste
        //Assertions.assertSame(returnedJedisList.size(), 2, "The list is not complete");
    }
}