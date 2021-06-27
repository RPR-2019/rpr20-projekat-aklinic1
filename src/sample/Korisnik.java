package sample;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Korisnik {

    public enum Spol{

        MUSKO("musko"),
        ZENSKO("zensko");

        private String naziv;

        Spol(String naziv) {
            this.naziv = naziv;
        }

        public String getNaziv() {
            return naziv;
        }
    }
    public enum StarosnaKateogrija{
        BEBA,
        DIJETE,
        PUNOLJETAN,
        ZREO,
        SENIOR
    }

    private String ime;
    private String prezime;
    private String email;
    private LocalDate datumRodjenja;
    private ArrayList<Proizvod> kupljeniProizvodi = new ArrayList<>();
    private int id;
    private Spol spol;

    public Korisnik() {
    }

    public Korisnik(int id, String ime, String prezime, String email, LocalDate datumRodjenja, String spol) {
        this.ime = ime;
        this.prezime = prezime;
        this.email = email;
        this.datumRodjenja = datumRodjenja;
        this.id = id;
        if(spol.equals("Muško")) this.spol = Spol.MUSKO;
        else this.spol = Spol.ZENSKO;
    }

    public Korisnik(String ime, String prezime, String email, LocalDate datumRodjenja) {
        this.ime = ime;
        this.prezime = prezime;
        this.email = email;
        this.datumRodjenja = datumRodjenja;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getDatumRodjenja() {
        return datumRodjenja;
    }

    public void setDatumRodjenja(LocalDate datumRodjenja) {
        this.datumRodjenja = datumRodjenja;
    }

    public String getDatumIspis() {
        if(datumRodjenja == null) return null;
        DateTimeFormatter fomatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        return fomatter.format(datumRodjenja);
    }
    public int dajGodine(){
        return Period.between(datumRodjenja, LocalDate.now()).getYears();
    }

    public String getSpol() {
        return spol.getNaziv();
    }

    public void setSpol(String spol) {
        if(spol.equals("Muško")) this.spol = Spol.MUSKO;
        else this.spol = Spol.ZENSKO;
    }

    public StarosnaKateogrija dajStarosnuKateogriju(){
        int godine = dajGodine();

        if(godine >=0 && godine < 7)
            return StarosnaKateogrija.BEBA;
        else if(godine >=7 && godine < 18)
            return StarosnaKateogrija.DIJETE;
        else if(godine >= 18 && godine < 27)
            return StarosnaKateogrija.PUNOLJETAN;
        else if(godine>=27 && godine<50)
            return StarosnaKateogrija.ZREO;
        else
            return StarosnaKateogrija.SENIOR;

    }
}
