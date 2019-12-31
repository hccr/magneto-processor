package cl.hccr.service.magnetoprocessor.repository;

import cl.hccr.service.magnetoprocessor.domain.MutantRequest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;


@Repository
public class SimpleMutantRequestDAO implements MutantRequestDAO {

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public SimpleMutantRequestDAO(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }


    @Override
    public boolean insert(final MutantRequest request) {
        String dna = request.getDnaString();
        int dnaHashCode = dna.hashCode();
        int affectedDnaRows = 0;
        String insertDnaRecord = "INSERT INTO dna (hash_index, dna, mutant ) VALUES ( :dnaHashCode , :dna , :mutant ) ;";

        if(isHashIndex(dnaHashCode)){
            
            //Ya hay una cadena con el mismo hashcode
            //Verifico si ya esta esta cadena buscandola, si no, la inserto.

            String selectSql = "SELECT COUNT(*) FROM dna where hash_index = :dnaHashCode and dna = :dna ;";

            int count = namedParameterJdbcTemplate.queryForObject(selectSql, new MapSqlParameterSource("dnaHashCode", dnaHashCode).addValue("dna",dna), Integer.class);
            if(count==1) {
                return true;
            }else {
                try{
                    affectedDnaRows = namedParameterJdbcTemplate.update(insertDnaRecord, new MapSqlParameterSource("dnaHashCode", dnaHashCode)
                            .addValue("dna",dna)
                            .addValue("mutant",request.isMutant()));
                    return affectedDnaRows>0;
                }catch (Exception e){e.printStackTrace();}
                return false;
            }
        }else{

            //No hay una cadena con el mismo hashcode, se debe insertar el hashcode

            String insertHashIndexSql = "INSERT INTO hash_table (hash_index) VALUES ( :dnaHashCode ) ;";
            try{
                int affectedIndexRows = namedParameterJdbcTemplate.update(insertHashIndexSql, new MapSqlParameterSource("dnaHashCode", dnaHashCode));
                if(affectedIndexRows>0){
                    affectedDnaRows = namedParameterJdbcTemplate.update(insertDnaRecord, new MapSqlParameterSource("dnaHashCode", dnaHashCode)
                            .addValue("dna",dna)
                            .addValue("mutant",request.isMutant()));;
                }
                return affectedIndexRows>0 && affectedDnaRows>0;
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean isHashIndex(int dnaHashCode){
        String sql = "select hash_index from hash_table where hash_index = :dnaHashCode ";
        try{
            Integer result = namedParameterJdbcTemplate.queryForObject(sql,new MapSqlParameterSource("dnaHashCode",dnaHashCode), Integer.class);
            return result!=null && result>0;
        }catch (EmptyResultDataAccessException e){}
        return false;
    }

}
