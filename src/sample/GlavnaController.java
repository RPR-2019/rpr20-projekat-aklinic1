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
import java.util.Optional;

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;

public class GlavnaController {
    private CrmDAO dao;
    private ObservableList<Korisnik> listaKorisnika;
    private ObservableList<Proizvod> listaProizvoda;
    private ObservableList<String> listaOpcija;

    public TableView tableView;
    public TableColumn columnIme, columnPrezime, columnEmail, columnDatumRodjenja;
    public ListView listOpcije;

    public GlavnaController(){
        dao = CrmDAO.getInstance();
        listaKorisnika = FXCollections.observableArrayList(dao.dajKorisnike());
        listaProizvoda = FXCollections.observableArrayList(dao.dajProizvode());
        listaOpcija = FXCollections.observableArrayList();
        listaOpcija.add("Proizvodi");
        listaOpcija.add("Korisnici");
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
                    if(columnIme.getText().equals("Ime")) {
                        postaviImenaKolonaTabele("Naziv", "Brend", "Cijena(KM)", "Količina");
                        tableView.setItems(listaProizvoda);
                        dodajVrijednostiKolonamaTabele("naziv", "brend", "cijena", "kolicina");
                    }
                }
                else if(listOpcije.getSelectionModel().getSelectedItem().equals("Korisnici")){
                    if(columnIme.getText().equals("Naziv")){
                        postaviImenaKolonaTabele("Ime", "Prezime", "E-mail", "Datum rođenja");
                        tableView.setItems(listaKorisnika);
                        dodajVrijednostiKolonamaTabele("ime", "prezime", "email", "datumIspis");
                    }
                }
            }
        });
    }


    public void actionDodaj(ActionEvent actionEvent) {
        Stage stage = new Stage();
        Parent root;
        try {
            if(columnIme.getText().equals("Ime")) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/unosKorisnika.fxml"));
                UnosKorisnikaController controller = new UnosKorisnikaController();
                loader.setController(controller);
                root = loader.load();
                stage.setTitle("Unos korisnika");
                stage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
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
            else {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/unosProizvoda.fxml"));
                UnosProizvodaController controller = new UnosProizvodaController(dao.dajTagove(), dao);
                loader.setController(controller);
                root = loader.load();
                stage.setTitle("Unos Proizvoda");
                stage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
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

        } catch (IOException e) {
            e.printStackTrace();
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
}
