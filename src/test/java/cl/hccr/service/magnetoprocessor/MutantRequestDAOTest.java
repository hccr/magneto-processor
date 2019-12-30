package cl.hccr.service.magnetoprocessor;


import cl.hccr.service.magnetoprocessor.domain.MutantRequest;
import cl.hccr.service.magnetoprocessor.repository.MutantRequestDAO;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class MutantRequestDAOTest {
    @Autowired
    private MutantRequestDAO mutantRequestDAO;

    @Autowired
    private JdbcTemplate template;


    @BeforeEach
    void cleanUp(){
        template.execute("TRUNCATE TABLE dna");
        template.execute("DELETE FROM hash_table WHERE TRUE");
    }



    @Test
    void isHashIndex(){
        String[] dna = {"AAAAAA","BBBBBB","CCCCCC","DDDDDD","EEEEEE","FFFFFF"};
        MutantRequest request = new MutantRequest(dna);

        Assertions.assertThat(mutantRequestDAO.isHashIndex(request.getDnaString().hashCode())).isEqualTo(false);
        Assertions.assertThat(mutantRequestDAO.insert(request)).isEqualTo(true);
        Assertions.assertThat(mutantRequestDAO.isHashIndex(request.getDnaString().hashCode())).isEqualTo(true);

    }

    @Test
    void insertDuplicate(){
        String[] dna = {"AAAAAA","BBBBBB","CCCCCC","DDDDDD","EEEEEE","FFFFFF"};
        MutantRequest request = new MutantRequest(dna);

        Assertions.assertThat(mutantRequestDAO.insert(request)).isEqualTo(true);
        Assertions.assertThat(mutantRequestDAO.isHashIndex(request.getDnaString().hashCode())).isEqualTo(true);
        Assertions.assertThat(mutantRequestDAO.insert(request)).isEqualTo(true);

    }


}
