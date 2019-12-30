package cl.hccr.service.magnetoprocessor.domain;


public class MutantRequest {
    private String [] dna;
    private boolean isMutant = false;


    public MutantRequest() {
    }

    public MutantRequest(String[] dna) {
        this.dna = dna;
    }

    public String[] getDna() {
        return dna;
    }

    public void setDna(String[] dna) {
        this.dna = dna;
    }

    public boolean isMutant() {
        return isMutant;
    }

    public void setMutant(boolean mutant) {
        isMutant = mutant;
    }

    public String getDnaString(){
        StringBuilder sb = new StringBuilder();
        for (String dnaString: dna) {
            sb.append(dnaString);
        }
        return sb.toString();
    }
}

