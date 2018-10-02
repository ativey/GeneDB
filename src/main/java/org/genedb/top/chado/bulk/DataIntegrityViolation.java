package org.genedb.top.chado.bulk;

public class DataIntegrityViolation extends Exception {

    public DataIntegrityViolation() {
        super();
    }

    public DataIntegrityViolation(String format, Object... params) {
        super(String.format(format, params));
    }
}
