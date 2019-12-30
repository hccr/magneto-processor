package cl.hccr.service.magnetoprocessor;

import cl.hccr.service.magnetoprocessor.domain.MutantRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MutantRequestTest {

    @Test
    void getDnaString(){
        String[] dna = {"AAAAAA","BBBBBB","CCCCCC","DDDDDD","EEEEEE","FFFFFF"};
        String result = "AAAAAABBBBBBCCCCCCDDDDDDEEEEEEFFFFFF";
        MutantRequest request = new MutantRequest(dna);

        String dnaString = request.getDnaString();
        Assertions.assertThat(dnaString).isEqualTo(result);
    }

    @Test
    void getDna(){
        String[] dna = {"AAAAAA","BBBBBB","CCCCCC","DDDDDD","EEEEEE","FFFFFF"};

        MutantRequest request = new MutantRequest(dna);
        Assertions.assertThat(request.getDna()).isEqualTo(dna);
    }

    @Test
    void isMutant(){
        String[] dna = {"AAAAAA","BBBBBB","CCCCCC","DDDDDD","EEEEEE","FFFFFF"};

        MutantRequest request = new MutantRequest(dna);
        request.setMutant(true);
        Assertions.assertThat(request.isMutant()).isEqualTo(true);
    }

}
