package sample;

import java.time.LocalDate;

public class Kupovina {
    private Korisnik korisnik;
    private int korisnikID;
    private int proizvodID;
    private Proizvod proizvod;
    private LocalDate datumKupovine;
    private double placeno;

    public double getPlaceno() {
        return placeno;
    }

    public void setPlaceno(double placeno) {
        this.placeno = placeno;
    }

    public int getKorisnikID() {
        return korisnikID;
    }

    public void setKorisnikID(int korisnikID) {
        this.korisnikID = korisnikID;
    }

    public int getProizvodID() {
        return proizvodID;
    }

    public void setProizvodID(int proizvodID) {
        this.proizvodID = proizvodID;
    }



    public Kupovina() {
    }

    public Kupovina(int korisnikID, int proizvodID, LocalDate datumKupovine) {
        this.korisnikID = korisnikID;
        this.proizvodID = proizvodID;
        this.datumKupovine = datumKupovine;
    }

    public Kupovina(Korisnik korisnik, Proizvod proizvod, LocalDate datumKupovine, double placeno) {
        this.korisnik = korisnik;
        this.proizvod = proizvod;
        this.datumKupovine = datumKupovine;
        this.placeno = placeno;
    }

    public Korisnik getKorisnik() {
        return korisnik;
    }


    public void setKorisnik(Korisnik korisnik) {
        this.korisnik = korisnik;
    }

    public Proizvod getProizvod() {
        return proizvod;
    }

    public void setProizvod(Proizvod proizvod) {
        this.proizvod = proizvod;
        placeno = proizvod.getCijena();
    }

    public LocalDate getDatumKupovine() {
        return datumKupovine;
    }

    public void setDatumKupovine(LocalDate datumKupovine) {
        this.datumKupovine = datumKupovine;
    }


}
