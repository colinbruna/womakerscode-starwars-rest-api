package com.bootcampjava.starwars.controller;

import com.bootcampjava.starwars.model.Jedi;
import com.bootcampjava.starwars.service.JediService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc //essa biblioteca serve para mockar rotas e mockar o retorno delas
public class JediControllerTest {

    @MockBean //anotação para a classe ter os mesmos comportamentos, porém com retornos simulados
    private JediService jediService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("GET /jedi/1 - SUCCESS")
    // como esse teste está na camada de controller, queremos que estoure uma Exception
    public void testGetJediByIdWithSuccess() throws Exception {

        // cenario
        Jedi mockJedi = new Jedi(1, "HanSolo", 10, 1); //aqui estou mockando um jedi
        Mockito.doReturn(Optional.of(mockJedi)).when(jediService).findById(1); //retornar um opcional mockjedi quando o jediservice procurar por id

        // execucao
        mockMvc.perform(get("/jedi/{id}", 1)) //método perform vai construir a minha URI e vai pegar os status que eu quero que essa URI retorne

                // asserts
                .andExpect(status().isOk()) //espero que retorne um status - ok
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)) //espero que o retorno seja um json

                .andExpect(header().string(HttpHeaders.ETAG, "\"1\"")) //espero que o retorno seja em forma de string
                .andExpect(header().string(HttpHeaders.LOCATION, "/jedi/1")) //e que também tenha a localização

                //o que queremos que tenha no retorno
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("HanSolo")))
                .andExpect(jsonPath("$.strength", is(10)))
                .andExpect(jsonPath("$.version", is(1)));
    }

    @Test
    @DisplayName("GET /jedi/1 - Not Found")
    public void testGetJediByIdNotFound() throws Exception {

        // cenario
        Mockito.doReturn(Optional.empty()).when(jediService).findById(1);

        //execucao
        mockMvc.perform(get("/jedi/{1}", 1))

                //asserts
                .andExpect(status().isNotFound());
    }

    // TODO: Teste do POST com sucesso

    // TODO: Teste do PUT com sucesso

    // TODO: Teste do PUT com uma versao igual da ja existente - deve retornar um conflito

    // TODO: Teste do PUT com erro - not found

    // TODO: Teste do delete com sucesso

    // TODO: Teste do delete com erro - deletar um id ja deletado

    // TODO: Teste do delete com erro  - internal server error

    //método auxiliar para ler o Json com String
    static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}