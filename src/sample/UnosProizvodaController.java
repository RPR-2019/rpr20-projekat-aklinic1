package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class UnosProizvodaController {
    private ObservableList<Tag> tagovi;
    private CrmDAO dao;
    private Set<Tag> izabraniTagovi = new HashSet<>();
    private Set<Tag> tagoviZaPrikaz;

    public ListView<Tag> listTagovi;
    public TextField txtNaziv, txtBrend, txtCijena, txtTag, txtKolicina;
    public TextArea txtInformacije;
    public FlowPane flowPane;
    public Proizvod proizvod;
    public Button btnDodaj;

    public Proizvod getProizvod() {
        return proizvod;
    }

    public UnosProizvodaController(Set<Tag> tagovi, CrmDAO dao, Proizvod proizvod){
        this.proizvod = proizvod;
        this.dao = dao;
        tagoviZaPrikaz = tagovi;
        this.tagovi = FXCollections.observableArrayList();
        for(Tag tag: tagovi) {
            this.tagovi.add(tag);
        }
    }

    @FXML
    public void initialize(){
        if(proizvod != null) btnDodaj.setText("Izmijeni");
        listTagovi.setItems(this.tagovi);
        listTagovi.setOnMouseClicked( click -> {
            if(click.getClickCount() == 2){
                Tag izabraniTag = listTagovi.getSelectionModel().getSelectedItem();
                int duzina = izabraniTagovi.size();
                izabraniTagovi.add(izabraniTag);
                if(duzina != izabraniTagovi.size())   {    //Provjeravamo da li je ovaj tag vec izabran
                    flowPane.getChildren().add(dajTag(izabraniTag.getNaziv()));
                }

            }
        });
        if(proizvod != null){
            txtNaziv.setText(proizvod.getNaziv());
            txtCijena.setText(String.valueOf(proizvod.getCijena()));
            txtBrend.setText(proizvod.getBrend());
            txtInformacije.setText(proizvod.getDetaljneInformacije());
            txtKolicina.setText(String.valueOf(proizvod.getKolicina()));
            for(Tag t: proizvod.getTagovi()){
                flowPane.getChildren().add(dajTag(t.getNaziv()));
            }
        }
    }

    public Button dajTag(String naziv){
        Image image = new javafx.scene.image.Image("delete.png");
        ImageView imageView = new ImageView(image);
        Button tag = new Button(naziv);
        tag.setContentDisplay(ContentDisplay.RIGHT);
        imageView.setFitHeight(20);
        imageView.setFitWidth(20);
        tag.setGraphic(imageView);
        tag.setStyle("   -fx-border-width: 2;" +
                "        -fx-border-color: DeepSkyBlue;" +
                "        -fx-border-radius: 4;" +
                "        -fx-background-color: f1f1f1;" +
                "        -fx-border-insets: 5;");
        tag.setOnAction(actionEvent -> {
            flowPane.getChildren().remove(tag);
        });
        return tag;
    }

    public void actionDodaj(ActionEvent actionEvent){
        String cijena = txtCijena.getText().replaceFirst(",",".");
        boolean unosCijene = true;
        try {
            Double.parseDouble(cijena);
            Integer.parseInt(txtKolicina.getText());
        }catch (Exception e){
            unosCijene = false;
        }

        if(txtNaziv.getText().trim().isEmpty() || unosCijene == false) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Upozorenje");
            alert.setHeaderText("Neispravan unos proizvoda");
            if (txtNaziv.getText().trim().isEmpty())
                alert.setContentText("Prije potvrde unesite naziv proizvoda!");
            else alert.setContentText("Unesite validnu cijenu proizvoda!");
            alert.showAndWait();
            if (txtNaziv.getText().trim().isEmpty())
                txtNaziv.requestFocus();
            else {
                txtCijena.requestFocus();
            }
        }
        else {
            if(proizvod == null) proizvod = new Proizvod();
            proizvod.setNaziv(txtNaziv.getText());
            proizvod.setBrend(txtBrend.getText());
            proizvod.setCijena(Double.parseDouble(cijena));
            proizvod.setKolicina(Integer.parseInt(txtKolicina.getText()));
            proizvod.setDetaljneInformacije(txtInformacije.getText());
            proizvod.setTagovi(izabraniTagovi);
            Stage stage = (Stage) txtNaziv.getScene().getWindow();
            stage.close();
        }
    }

    public void actionOdustani(ActionEvent actionEvent){
        proizvod = null;
        Stage stage = (Stage) txtNaziv.getScene().getWindow();
        stage.close();
    }


    public void actionDodajTag(ActionEvent actionEvent){
        if(!txtTag.getText().trim().isEmpty()){
            Tag tag = new Tag(txtTag.getText());
            int vel = tagoviZaPrikaz.size();
            tagoviZaPrikaz.add(tag);
            if(vel != tagoviZaPrikaz.size()) {          //Provjeravamo da li ovaj tag vec postoji
                listTagovi.getItems().add(tag);
                dao.dodajTag(tag);
            }
            txtTag.setText("");
            listTagovi.getSelectionModel().select(listTagovi.getItems().size() - 1);
            listTagovi.scrollTo(listTagovi.getItems().size() - 1);
            listTagovi.requestFocus();
        }
        else {
            txtTag.setText("");
            txtTag.requestFocus();
        }
    }
}
