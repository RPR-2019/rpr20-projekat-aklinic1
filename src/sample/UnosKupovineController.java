package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;

public class UnosKupovineController {
    private ObservableList<Proizvod> proizvodi;
    private ObservableList<Korisnik> korisnici;
    private ObservableList<Proizvod> kupljeniProizvodi;

    public ArrayList<Kupovina> kupovina = null;
    public ListView<Proizvod> listProizvodi, listKupljeniProizvodi;
    public ChoiceBox<Korisnik> choiceKorisnik;

    public UnosKupovineController(ObservableList<Korisnik> korisnici, ObservableList<Proizvod> proizvodi) {
        korisnici.sort(Comparator.comparing(Korisnik::getIme));
        this.proizvodi = proizvodi;
        this.korisnici = korisnici;
        kupljeniProizvodi = FXCollections.observableArrayList();
    }
    @FXML
    public void initialize(){
        listProizvodi.setItems(proizvodi);
        choiceKorisnik.setItems(korisnici);

        listProizvodi.setOnMouseClicked(click -> {
            if(click.getClickCount() == 2){
                Proizvod proizvod = listProizvodi.getSelectionModel().getSelectedItem();
                proizvod.setKolicina(proizvod.getKolicina() - 1);
                kupljeniProizvodi.add(proizvod);
                listKupljeniProizvodi.setItems(kupljeniProizvodi);
            }
        });
        listKupljeniProizvodi.setOnMouseClicked(click ->{
            if(click.getClickCount() == 2){
                Proizvod proizvod = listKupljeniProizvodi.getSelectionModel().getSelectedItem();
                proizvod.setKolicina(proizvod.getKolicina() + 1);
                kupljeniProizvodi.remove(proizvod);
                listKupljeniProizvodi.setItems(kupljeniProizvodi);
            }
        });
    }

    public void actionZavrsiKupovinu(ActionEvent actionEvent){
        kupovina = new ArrayList<>();
        for(Proizvod proizvod: kupljeniProizvodi){
            kupovina.add(new Kupovina(choiceKorisnik.getValue(), proizvod, LocalDate.now(), proizvod.getCijena()));

        }
        Stage stage = (Stage) listProizvodi.getScene().getWindow();
        stage.close();
    }
    public void actionOdustani(ActionEvent actionEvent){
        Stage stage = (Stage) listProizvodi.getScene().getWindow();
        stage.close();
    }

    public ArrayList<Kupovina> getKupovina() {
        return kupovina;
    }
}
