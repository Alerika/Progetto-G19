package it.unipv.gui.common;

public class Movie {
    private String codice;
    private String locandinaPath;
    private String titolo;
    private String regia;
    private String cast;
    private String durata;
    private String anno;
    private String trama;

    public String getCodice() { return codice; }

    public void setCodice(String codice) { this.codice = codice; }

    public String getLocandinaPath() {
        return locandinaPath;
    }

    public void setLocandinaPath(String locan) {
        this.locandinaPath = locan;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public String getRegia() { return regia; }

    public void setRegia(String regia) { this.regia = regia; }

    public String getCast() {
        return cast;
    }

    public void setCast(String cast) {
        this.cast = cast;
    }

    public String getDurata() {
        return durata;
    }

    public void setDurata(String durata) {
        this.durata = durata;
    }

    public String getAnno() {
        return anno;
    }

    public void setAnno(String anno) {
        this.anno = anno;
    }

    public String getTrama() { return trama; }

    public void setTrama(String trama) {
        this.trama = trama;
    }
}
