package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
import java.awt.*;

public class UnosKorisnikaController {

    public ObservableList<String> polovi = FXCollections.observableArrayList();
    public Korisnik korisnik = null;
    public TextField txtIme;
    public TextField txtPrezime;
    public TextField txtEmail;
    public DatePicker datePicker;
    public ChoiceBox choiceSpol;

    @FXML
    public void initialize(){
        polovi.add("Muško");
        polovi.add("Žensko");
        choiceSpol.setItems(polovi);
    }

    public void actionOdustani(ActionEvent actionEvent){
        korisnik = null;
        Stage stage = (Stage) txtIme.getScene().getWindow();
        stage.close();
    }

    public Korisnik getKorisnik() {
        return korisnik;
    }

    public void actionDodaj(ActionEvent actionEvent){
        if(txtIme.getText().trim().isEmpty() || txtPrezime.getText().trim().isEmpty() || choiceSpol.getSelectionModel().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Upozorenje");
            alert.setHeaderText("Neispravan unos korisnika");
            if(txtIme.getText().trim().isEmpty() || txtPrezime.getText().trim().isEmpty())
                alert.setContentText("Prije potvrde unesite ime i prezime korisnika!");
            else alert.setContentText("Odaberite spol korisnika!");
                alert.showAndWait();
            if(txtIme.getText().trim().isEmpty())
                txtIme.requestFocus();
            else if(txtPrezime.getText().trim().isEmpty())
                txtPrezime.requestFocus();
            else choiceSpol.requestFocus();
        }
        else{
            korisnik = new Korisnik();
            korisnik.setIme(txtIme.getText());
            korisnik.setPrezime(txtPrezime.getText());
            korisnik.setEmail(txtEmail.getText());
            korisnik.setDatumRodjenja(datePicker.getValue());
            korisnik.setSpol((String) choiceSpol.getSelectionModel().getSelectedItem());
            Stage stage = (Stage) txtIme.getScene().getWindow();
            stage.close();
        }

    }


}
