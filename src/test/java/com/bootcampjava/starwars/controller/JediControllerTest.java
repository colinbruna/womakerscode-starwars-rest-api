package com.bootcampjava.starwars.controller;

import com.bootcampjava.starwars.model.Jedi;
import com.bootcampjava.starwars.repository.JediRepository;
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

import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class JediControllerTest {

    @MockBean
    private JediService jediService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("GET /jedi/1 - SUCCESS")
    public void testGetJediByIdWithSuccess() throws Exception {

        // cenario
        Mockito.doReturn(Optional.of(buildJedi())).when(jediService).findById(1);

        // execucao
        mockMvc.perform(get("/jedi/{id}", 1))

                // assert
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                .andExpect(header().string(HttpHeaders.ETAG, "\"1\""))
                .andExpect(header().string(HttpHeaders.LOCATION, "/jedi/1"))

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

        // execucao
        mockMvc.perform(get("/jedi/{1}", 1))

                // assert
                .andExpect(status().isNotFound());
    }

    // TODO: Teste do POST com sucesso
    @Test
    @DisplayName("POST /jedi/ - Success")
    public void testPostWithSuccess() throws Exception {

        // cenario
        Jedi jedi = buildJedi();
        String json = asJsonString(jedi);
        Mockito.doReturn(jedi).when(jediService).save(jedi);

        // execucao
        mockMvc.perform(
                    post("/jedi/")
                   .contentType("application/json;charset=UTF-8")
                   .content(json))

                // assert
                .andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.ETAG, "\"1\""))
                .andExpect(content().string(json));
    }

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

    private Jedi buildJedi() {
        return new Jedi(1, "HanSolo", 10, 1);
    }
}