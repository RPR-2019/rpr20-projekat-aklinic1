package sample;

import org.json.JSONArray;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class CrmDAO {
    private static CrmDAO instance;
    private Connection connection;

    private PreparedStatement dajKorisnikeUpit, dodajKorisnikaUpit, dajIdZadnjegKorisnikaUpit, izbrisiKorisnikaUpit,
        dajProizvodeUpit, dodajProizvodUpit, izbrisiProizvodUpit, dajIDZadnjegProizvodaUpit, izmijeniKorisnikaUpit,
        izmijeniProizvodUpit, dajKupovineUpit, dodajKupovinuUpit;
    private PreparedStatement dajTagoveUpit, dodajTagUpit;

    public static CrmDAO getInstance(){
        if(instance == null) instance = new CrmDAO();
        return instance;
    }

    private CrmDAO() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:crm.db");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try{
            dajKorisnikeUpit = connection.prepareStatement("SELECT * FROM korisnici");
            dodajKorisnikaUpit = connection.prepareStatement("INSERT INTO korisnici VALUES(?, ?, ?, ?, ?, ?)");
            dajIdZadnjegKorisnikaUpit = connection.prepareStatement("SELECT MAX(ID) FROM korisnici");
            izbrisiKorisnikaUpit = connection.prepareStatement("DELETE FROM korisnici WHERE id=?");
            dajProizvodeUpit = connection.prepareStatement("SELECT * FROM proizvodi");
            dodajProizvodUpit = connection.prepareStatement("INSERT INTO proizvodi VALUES(?, ?, ?, ?, ?, ?, ?, ?)");
            dajIDZadnjegProizvodaUpit = connection.prepareStatement("SELECT MAX(ID) FROM proizvodi");
            izbrisiProizvodUpit = connection.prepareStatement("DELETE FROM korisnici WHERE id=?");
            dajTagoveUpit = connection.prepareStatement("SELECT * FROM tagovi");
            dodajTagUpit = connection.prepareStatement("INSERT INTO tagovi VALUES(?)");
            izbrisiProizvodUpit = connection.prepareStatement("DELETE FROM proizvodi WHERE id=?");
            izmijeniKorisnikaUpit = connection.prepareStatement("UPDATE korisnici SET Ime=?, Prezime=?, Email=?, " +
                    "Datum=?, Spol =? WHERE ID=?");
            izmijeniProizvodUpit = connection.prepareStatement("UPDATE proizvodi SET Naziv=?, Brend=?, Cijena=?, " +
                    "Kolicina=?, Popust=?, Informacije=?, Tagovi=? WHERE ID=?");
            dajKupovineUpit = connection.prepareStatement("SELECT * FROM kupovine");
            dodajKupovinuUpit = connection.prepareStatement("INSERT INTO kupovine VALUES(?,?,?)");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void dodajKupovinu(Kupovina kupovina){
        try {
            dodajKupovinuUpit.setInt(1, kupovina.getKorisnik().getId());
            dodajKupovinuUpit.setInt(2, kupovina.getProizvod().getId());
            dodajKupovinuUpit.setString(3, kupovina.getDatumKupovine().toString());
            dodajKupovinuUpit.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    public Kupovina dajKupovinu(ResultSet rs){
        Kupovina kupovina = null;
        try {
            kupovina =  new Kupovina(rs.getInt(1), rs.getInt(2),
                    LocalDate.parse(rs.getString(3)));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return kupovina;
    }
    public ArrayList<Kupovina> dajKupovine(){
        ArrayList<Kupovina> list = new ArrayList<>();
        try {
            ResultSet rs = dajKupovineUpit.executeQuery();
            while(rs.next()){
                list.add(dajKupovinu(rs));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return list;
    }


    public void izmijeniProizvod(Proizvod proizvod){
        try{
            izmijeniProizvodUpit.setString(1, proizvod.getNaziv());
            izmijeniProizvodUpit.setString(2, proizvod.getBrend());
            izmijeniProizvodUpit.setDouble(3, proizvod.getCijena());
            izmijeniProizvodUpit.setInt(4, proizvod.getKolicina());
            izmijeniProizvodUpit.setInt(5, proizvod.getPopust());
            izmijeniProizvodUpit.setString(6, proizvod.getDetaljneInformacije());
            izmijeniProizvodUpit.setString(7, proizvod.getTagoviJSON().toString());
            izmijeniProizvodUpit.setInt(8, proizvod.getId());
            izmijeniProizvodUpit.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    public void izmijeniKorisnika(Korisnik korisnik){
        try{
            izmijeniKorisnikaUpit.setString(1, korisnik.getIme());
            izmijeniKorisnikaUpit.setString(2, korisnik.getPrezime());
            izmijeniKorisnikaUpit.setString(3, korisnik.getEmail());
            izmijeniKorisnikaUpit.setString(4, korisnik.getDatumIspis());
            izmijeniKorisnikaUpit.setString(5, korisnik.getSpol());
            izmijeniKorisnikaUpit.setInt(6, korisnik.getId());
            izmijeniKorisnikaUpit.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private Korisnik dajKorisnika(ResultSet rs) {
        Korisnik korisnik = null;
        try {
            int id = rs.getInt(1);
            String ime, prezime, email, datumRodjenja, spol;
            ime = rs.getString(2);
            prezime = rs.getString(3);
            email = rs.getString(4);
            spol = rs.getString(6);

            datumRodjenja = rs.getString(5);
            LocalDate datum = null;
            if(datumRodjenja != null) {
                String[] rez = datumRodjenja.split("\\.");
                 datum = LocalDate.of(Integer.parseInt(rez[2]), Integer.parseInt(rez[1]), Integer.parseInt(rez[0]));
            }
            korisnik = new Korisnik(id, ime, prezime, email, datum, spol);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return korisnik;

    }

    public Proizvod dajProizvod(ResultSet rs){
        Proizvod proizvod = new Proizvod();
        try {
            proizvod.setId(rs.getInt(1));
            proizvod.setNaziv(rs.getString(2));
            proizvod.setBrend(rs.getString(3));
            proizvod.setCijena(rs.getDouble(4));
            proizvod.setKolicina(rs.getInt(5));
            proizvod.setPopust(rs.getInt(6));
            proizvod.setDetaljneInformacije(rs.getString(7));
            if(rs.getString(8) != null) {
                if(!rs.getString(8).isEmpty()) {
                    JSONArray tagoviJSON = new JSONArray(rs.getString(8));

                    Set<Tag> tagovi = new HashSet<>();
                    int i = 0;
                    for (i = 0; i < tagoviJSON.length(); i++) {
                        tagovi.add(new Tag(tagoviJSON.getString(i)));
                    }
                    proizvod.setTagovi(tagovi);
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return proizvod;
    }
    public ArrayList<Korisnik> dajKorisnike(){
        ArrayList<Korisnik> korisnici = new ArrayList<>();
        try {
            ResultSet rs = dajKorisnikeUpit.executeQuery();

            while(rs.next())
                korisnici.add(dajKorisnika(rs));

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return korisnici;
    }
    public ArrayList<Proizvod> dajProizvode(){
        ArrayList<Proizvod> proizvodi = new ArrayList<>();
        try {
            ResultSet rs = dajProizvodeUpit.executeQuery();

            while(rs.next()){
                proizvodi.add(dajProizvod(rs));
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return proizvodi;
    }
    public void dodajKorisnika(Korisnik korisnik) throws SQLException {

        dodajKorisnikaUpit.setInt(1,korisnik.getId());
        dodajKorisnikaUpit.setString(2, korisnik.getIme());
        dodajKorisnikaUpit.setString(3, korisnik.getPrezime());
        dodajKorisnikaUpit.setString(4, korisnik.getEmail());
        dodajKorisnikaUpit.setString(5, korisnik.getDatumIspis());
        dodajKorisnikaUpit.setString(6, korisnik.getSpol());
        dodajKorisnikaUpit.executeUpdate();
    }

    public  void dodajProizvod(Proizvod proizvod) throws SQLException {
        dodajProizvodUpit.setInt(1, proizvod.getId());
        dodajProizvodUpit.setString(2, proizvod.getNaziv());
        dodajProizvodUpit.setString(3, proizvod.getBrend());
        dodajProizvodUpit.setDouble(4, proizvod.getCijena());
        dodajProizvodUpit.setInt(5, proizvod.getKolicina());
        dodajProizvodUpit.setInt(6, proizvod.getPopust());
        dodajProizvodUpit.setString(7, proizvod.getDetaljneInformacije());
        dodajProizvodUpit.setString(8,  proizvod.getTagoviJSON().toString());

        dodajProizvodUpit.executeUpdate();
    }

    public int dajIdZadnjegKorisnika() throws SQLException {
        ResultSet rs = dajIdZadnjegKorisnikaUpit.executeQuery();
        rs.next();
        return rs.getInt(1);
    }

    public int dajIdZadnjegProizvoda() throws SQLException {
        ResultSet rs = dajIDZadnjegProizvodaUpit.executeQuery();
        rs.next();
        return rs.getInt(1);
    }

    public void izbrisiKorisnika(int id){
        try {
            izbrisiKorisnikaUpit.setInt(1, id);
            izbrisiKorisnikaUpit.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    public Set<Tag> dajTagove() {
        Set<Tag> tagovi = new HashSet<>();
        try {
            ResultSet rs = dajTagoveUpit.executeQuery();
            while (rs.next()) {
                Tag tag = new Tag(rs.getString(1));
                tagovi.add(tag);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return tagovi;
    }

    public void izbrisiProizvod(int id) {
        try{
            izbrisiProizvodUpit.setInt(1, id);
            izbrisiProizvodUpit.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    public void dodajTag(Tag tag){
        try {
            dodajTagUpit.setString(1, tag.getNaziv());
            dodajTagUpit.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
