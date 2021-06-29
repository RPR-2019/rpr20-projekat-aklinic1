package sample;

import org.json.JSONArray;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Proizvod {
    private int id;
    private String naziv;
    private String brend;
    private Double cijena;
    private Integer popust = 0;
    private Integer kolicina;
    private String detaljneInformacije;
    //    slika
    private Set<Tag> tagovi = new HashSet<>();

    public Proizvod() {
    }

    public Double dajCijenuSaPopustom(){
        return cijena*(1-(double)popust/100);
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }


    public Double getCijena() {
        return cijena;
    }

    public void setCijena(Double cijena) {
        this.cijena = cijena;
    }

    public Integer getPopust() {
        return popust;
    }

    public void setPopust(Integer popust) {
        this.popust = popust;
    }

    public boolean imaPopust(){
        if(popust != 0) return true;
        return false;
    }

    public boolean imaNaStanju(){
        if(kolicina == 0) return false;
        return true;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBrend() {
        return brend;
    }

    public void setBrend(String brend) {
        this.brend = brend;
    }

    public Integer getKolicina() {
        return kolicina;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Proizvod proizvod = (Proizvod) o;
        return id == proizvod.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public void setKolicina(Integer kolicina) {
        this.kolicina = kolicina;
    }

    public String getDetaljneInformacije() {
        return detaljneInformacije;
    }

    public void setDetaljneInformacije(String detaljneInformacije) {
        this.detaljneInformacije = detaljneInformacije;
    }

    public Set<Tag> getTagovi() {
        return tagovi;
    }

    public void setTagovi(Set<Tag> tagovi) {
        this.tagovi = tagovi;
    }

    @Override
    public String toString() {
        return naziv + ", " + brend;
    }
    public JSONArray getTagoviJSON(){
        JSONArray array = new JSONArray();
        for(Tag tag: tagovi)
            array.put(tag.getNaziv());
        return array;
    }
}
