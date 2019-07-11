package com.tr.persistence.dao;

import com.tr.persistence.Entities.Officer;
import com.tr.persistence.Entities.Rank;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.*;

import javax.transaction.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static jdk.nashorn.internal.objects.NativeArray.forEach;
import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class JpaOfficerDAOTest {
    @Qualifier("jpaOfficerDAO")
    @Autowired
    private OfficerDAO dao;

    @Autowired
    private JdbcTemplate template;

    @Test
    public void testSave(){
             Officer officer = new Officer(Rank.LIEUTENANT, "Nyota", "Uhuru");
             officer = dao.save(officer);
             assertNotNull(officer.getId());
    }

    @Test
    public void findOneThatExists(){
        template.query("select id from officers", (rs, num) -> rs.getInt("id"))
        .forEach(id ->{
            Optional<Officer> officer = dao.findById(id);
            assertTrue(officer.isPresent());
            assertEquals(id, officer.get().getId());
        });
    }

    @Test
    public void findOneThatNotExist(){
        Optional<Officer> officer = dao.findById(999);
        assertFalse(officer.isPresent());
    }

    @Test
    public void findAll(){
        List<String> dbNames = dao.findAll().stream()
                .map(Officer::getLast)
                .collect(Collectors.toList());
        assertThat(dbNames, containsInAnyOrder("Kirk", "Picard","Sisko", "Janeway", "Archer"));
    }

    @Test
    public void count(){
        assertEquals(5, dao.count());
    }

    @Test
    public void delete(){
        template.query("select id from officers", (rs, num) ->rs.getInt("id"))
                .forEach(id -> {
                    Optional<Officer> officer = dao.findById(id);
                    assertTrue(officer.isPresent());
                    dao.delete(officer.get());
                });
    }

    @Test
    public void existsById(){
        template.query("Select id from officers",
                (rs, num) ->rs.getInt("id"))
        .forEach(id -> assertTrue(String .format("%d should exist", id),
                dao.existsById(id)));
    }

    @Test
    public void doesNotExist(){
        List<Integer> ids = template.query("select id from officers",
                (rs, num ) -> rs.getInt("id"));
        assertThat(ids, not(contains(999)));
        assertFalse(dao.existsById(999));
    }
}
