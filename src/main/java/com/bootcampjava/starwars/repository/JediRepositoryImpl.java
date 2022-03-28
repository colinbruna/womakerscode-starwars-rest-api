package com.bootcampjava.starwars.repository;

import com.bootcampjava.starwars.model.Jedi;
import com.bootcampjava.starwars.service.JediService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class JediRepositoryImpl implements JediRepository{

    private static final Logger logger = LogManager.getLogger(JediService.class);

    private final JdbcTemplate jdbcTemplate; //para trazer as queries e não precisar fazer tudo a mão
    private final SimpleJdbcInsert simpleJdbcInsert; //usado para fazer algumas queries mais simples

    public JediRepositoryImpl(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource) //objeto dataSource: faz parte do sql javax e serve para fazer queries sql dentro dele(quero que ele construa insserts do jdbc dentro dele usando as queries previstas no dataSource)
                .withTableName("jedis") //nome da tabela
                .usingGeneratedKeyColumns("id"); //qual será o campo id da minha coluna
    }

    @Override
    public Optional<Jedi> findById(Integer id) {
        try {
            Jedi jedi = jdbcTemplate.queryForObject("SELECT * FROM jedis WHERE id = ?",
                    new Object[]{id},
                    //mapear objeto, fazer um RowMapper que é uma função lambda e desse RowMapper vamos setar os objetos
                    (rs, rowNum) -> {
                        Jedi p = new Jedi();
                        p.setId(rs.getInt("id"));
                        p.setName(rs.getString("name"));
                        p.setStrength(rs.getInt("strength"));
                        p.setVersion(rs.getInt("version"));
                        return p;
                    });
            return Optional.of(jedi);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
    //queryForObject()é um método fornecido pelo JdbcTemplate, que é usado para obter um único registro do banco de dados e leva 3 parâmetros, sql_query, row_mapper e query.

    @Override
    public List<Jedi> findAll() {
        return jdbcTemplate.query("SELECT * FROM jedis",
                (rs, rowNumber) -> {
                    Jedi jedi = new Jedi();
                    jedi.setId(rs.getInt("id"));
                    jedi.setName(rs.getString("name"));
                    jedi.setStrength(rs.getInt("strength"));
                    jedi.setVersion(rs.getInt("version"));
                    return jedi;
                });
    }

    @Override
    public boolean update(Jedi jedi) {
        return jdbcTemplate.update("UPDATE jedis SET name = ?, strenght = ?, version = ? WHERE id = ?",
                jedi.getName(),
                jedi.getStrength(),
                jedi.getVersion(),
                jedi.getId()) == 1; //para o id começar em 1
    }
    //sql: atualizando tabela jedis

    @Override
    public Jedi save(Jedi jedi) {
        Map<String, Object> parameters = new HashMap<>(1);
        parameters.put("name", jedi.getName());
        parameters.put("strength", jedi.getStrength());
        parameters.put("version", jedi.getVersion()); //como queremos salvar os atributos, eles devem ser inseridos um a um
        //definir o id
        Number newId = simpleJdbcInsert.executeAndReturnKey(parameters);

        logger.info("Insertin Jedi intro database, generated key is: {}", newId);
        //agora precisa setar o id
        jedi.setId((Integer) newId);
        return jedi;
    }

    @Override
    public boolean delete(Integer id) {
        return jdbcTemplate.update("DELETE FROM jedis WHERE  id = ?", id) ==1;
    }
}