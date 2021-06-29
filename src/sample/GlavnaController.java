package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;

public class GlavnaController {
    private CrmDAO dao;
    private ObservableList<Korisnik> listaKorisnika;
    private ObservableList<Proizvod> listaProizvoda;
    private ObservableList<Kupovina> listaKupovina;
    private ObservableList<String> listaOpcija;

    public TableView tableView;
    public TableColumn columnIme, columnPrezime, columnEmail, columnDatumRodjenja;
    public ListView listOpcije;

    public GlavnaController(){
        dao = CrmDAO.getInstance();
        listaKorisnika = FXCollections.observableArrayList(dao.dajKorisnike());
        listaProizvoda = FXCollections.observableArrayList(dao.dajProizvode());
        listaKupovina = FXCollections.observableArrayList(dao.dajKupovine());
        for(Kupovina kupovina: listaKupovina){
            for(Korisnik korisnik: listaKorisnika){
                if(kupovina.getKorisnikID() == korisnik.getId())
                    kupovina.setKorisnik(korisnik);
            }
            for(Proizvod proizvod: listaProizvoda){
                if(kupovina.getProizvodID() == proizvod.getId())
                    kupovina.setProizvod(proizvod);
            }
        }
        listaOpcija = FXCollections.observableArrayList();
        listaOpcija.add("Proizvodi");
        listaOpcija.add("Korisnici");
        listaOpcija.add("Kupovine");
    }

    private void dodajVrijednostiKolonamaTabele(String c1, String c2, String c3, String c4){
        columnIme.setCellValueFactory(new PropertyValueFactory(c1));
        columnPrezime.setCellValueFactory(new PropertyValueFactory(c2));
        columnEmail.setCellValueFactory(new PropertyValueFactory(c3));
        columnDatumRodjenja.setCellValueFactory(new PropertyValueFactory(c4));
    }
    private void postaviImenaKolonaTabele(String c1, String c2, String c3, String c4){
        columnIme.setText(c1);
        columnPrezime.setText(c2);
        columnEmail.setText(c3);
        columnDatumRodjenja.setText(c4);
    }

    @FXML
    public void initialize(){

        tableView.setItems(listaKorisnika);
        dodajVrijednostiKolonamaTabele("ime", "prezime", "email", "datumIspis");

        listOpcije.setItems(listaOpcija);
        listOpcije.setOnMouseClicked(click -> {
            if(click.getClickCount() == 2){

                if(listOpcije.getSelectionModel().getSelectedItem().equals("Proizvodi")) {
                    if(!columnIme.getText().equals("Naziv")) {
                        postaviImenaKolonaTabele("Naziv", "Brend", "Cijena(KM)", "Količina");
                        tableView.setItems(listaProizvoda);
                        dodajVrijednostiKolonamaTabele("naziv", "brend", "cijena", "kolicina");
                    }
                }
                else if(listOpcije.getSelectionModel().getSelectedItem().equals("Korisnici")){
                    if(!columnIme.getText().equals("Ime")){
                        postaviImenaKolonaTabele("Ime", "Prezime", "E-mail", "Datum rođenja");
                        tableView.setItems(listaKorisnika);
                        dodajVrijednostiKolonamaTabele("ime", "prezime", "email", "datumIspis");
                    }
                }

                else if(listOpcije.getSelectionModel().getSelectedItem().equals("Kupovine")){
                    if(!columnIme.getText().equals("Kupac")){
                        postaviImenaKolonaTabele("Kupac", "Proizvod", "Datum", "Cijena");
                        tableView.setItems(listaKupovina);
                        dodajVrijednostiKolonamaTabele("korisnik", "proizvod", "datumKupovine", "placeno");
                    }
                }
            }
        });
    }

    public Stage korisnikLoader(String title, UnosKorisnikaController controller){
        Stage stage = new Stage();
        Parent root;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/unosKorisnika.fxml"));
            loader.setController(controller);
            root = loader.load();
            stage.setTitle(title);
            stage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stage;
    }

    public Stage proizvodLoader(String title, UnosProizvodaController controller){
        Stage stage = new Stage();
        Parent root;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/unosProizvoda.fxml"));
            loader.setController(controller);
            root = loader.load();
            stage.setTitle(title);
            stage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stage;
    }
    public Stage kupovinaLoader(String title, UnosKupovineController controller){
        Stage stage = new Stage();
        Parent root;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/unosKupovine.fxml"));
            loader.setController(controller);
            root = loader.load();
            stage.setTitle(title);
            stage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stage;
    }


    public void actionDodaj(ActionEvent actionEvent) {
        Stage stage;

            if(columnIme.getText().equals("Ime")) {
                UnosKorisnikaController controller = new UnosKorisnikaController();
                stage = korisnikLoader("Unos korisnika", controller);
                stage.show();
                stage.setMinHeight(stage.getHeight());
                stage.setMinWidth(stage.getWidth());
                stage.setOnHiding(event -> {
                    Korisnik korisnik = controller.getKorisnik();

                    if (korisnik != null) {
                        try {
                            korisnik.setId(dao.dajIdZadnjegKorisnika() + 1);
                            dao.dodajKorisnika(korisnik);
                            listaKorisnika.setAll(dao.dajKorisnike());
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
                    }
                });
            }
            else if(columnIme.getText().equals("Naziv")) {
                UnosProizvodaController controller = new UnosProizvodaController(dao.dajTagove(), dao, null);
                stage = proizvodLoader("Unos proizvoda", controller);
                stage.show();
                stage.setMinWidth(stage.getWidth());
                stage.setMinHeight(stage.getHeight());
                stage.setOnHiding(event -> {
                    Proizvod proizvod = controller.getProizvod();

                    if(proizvod != null){
                        try{
                            proizvod.setId(dao.dajIdZadnjegProizvoda() + 1);
                            dao.dodajProizvod(proizvod);
                            listaProizvoda.setAll(dao.dajProizvode());
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
                    }
                });

            }
            else{
                UnosKupovineController controller = new UnosKupovineController(listaKorisnika, listaProizvoda);
                stage = kupovinaLoader("Unesi kupovinu", controller);
                stage.show();
                stage.setMinWidth(stage.getWidth());
                stage.setMinHeight(stage.getHeight());
                stage.setOnHiding(event -> {
                    ArrayList<Kupovina> kupovine = controller.getKupovina();

                    if(kupovine != null){
                            for(Kupovina kupovina:  kupovine){
                                dao.dodajKupovinu(kupovina);
                                listaKupovina.add(kupovina);
                            }
                    }
                });
            }

    }

    public void actionIzbrisi(ActionEvent actionEvent) {
        if(columnIme.getText().equals("Ime")){
            Korisnik korisnik = (Korisnik) tableView.getSelectionModel().getSelectedItem();
            if (korisnik != null) {
                int id = korisnik.getId();
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Potvrda brisanja");
                String imePrezime = korisnik.getIme() + " " + korisnik.getPrezime();
                alert.setHeaderText("Brisanje korisnika " + imePrezime);
                alert.setContentText("Da li ste sigurni da želite obrisati korisnika " + imePrezime + "?");
                alert.setResizable(true);

                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    dao.izbrisiKorisnika(id);
                    listaKorisnika.setAll(dao.dajKorisnike());
                }

            }
        }
        else{
            Proizvod proizvod = (Proizvod) tableView.getSelectionModel().getSelectedItem();
            if(proizvod!=null){
                int id = proizvod.getId();
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Potvrda brisanja");
                String naziv = proizvod.getNaziv();
                alert.setHeaderText("Brisanje proizvoda " + naziv);
                alert.setContentText("Da li ste sigurni da želite obrisati proizvod " + naziv + "?");
                alert.setResizable(true);

                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    dao.izbrisiProizvod(id);
                    listaProizvoda.setAll(dao.dajProizvode());
                }
            }
        }
    }

    public void actionDetaljno(ActionEvent actionEvent) {
        Stage stage;
        if(columnIme.getText().equals("Ime")){
            Korisnik korisnik = (Korisnik) tableView.getSelectionModel().getSelectedItem();
            if(korisnik != null) {
                UnosKorisnikaController controller = new UnosKorisnikaController(korisnik);
                stage = korisnikLoader("Detaljno o korisniku", controller);
                stage.show();
                stage.setMinWidth(stage.getWidth());
                stage.setMinHeight(stage.getHeight());

                stage.setOnHiding(event -> {
                    Korisnik korisnik1 = controller.getKorisnik();

                    if (korisnik1 != null) {
                        dao.izmijeniKorisnika(korisnik1);
                        listaKorisnika.setAll(dao.dajKorisnike());
                    }
                });
            }
        }
        else{
            Proizvod proizvod = (Proizvod) tableView.getSelectionModel().getSelectedItem();
            if(proizvod != null){
                UnosProizvodaController controller = new UnosProizvodaController(dao.dajTagove(), dao, proizvod);
                stage = proizvodLoader("Detaljno o proizvodu", controller);
                stage.show();
                stage.setMinWidth(stage.getWidth());
                stage.setMinHeight(stage.getHeight());

                stage.setOnHiding(event -> {
                    Proizvod proizvod1 = controller.getProizvod();

                    if (proizvod1 != null) {
                        dao.izmijeniProizvod(proizvod1);
                        listaProizvoda.setAll(dao.dajProizvode());
                    }
                });
            }
        }
    }
}
