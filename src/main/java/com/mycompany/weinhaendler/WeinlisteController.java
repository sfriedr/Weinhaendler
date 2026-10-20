/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.weinhaendler;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author sfrie
 */
public class WeinlisteController {
    
    final String MELDUNG_KEINE_DATEN_ZUM_LÖSCHEN = "Es liegen keine internen Weindaten vor,\ndie gelöscht werden könnten.";
    final String MELDUNG_KEINE_MARKIERTEN_DATEN_ZUM_LÖSCHEN = "Sie müssen erst Weine markieren,\num sie löschen zu können.";
    final String MELDUNG_WEIN_LÖSCHEN_ANFRAGE = "Sind Sie sicher, dass Sie den/die Wein(e) löschen möchten?";
    final String MELDUNG_ALLE_WEINE_LÖSCHEN_ANFRAGE = "Sind Sie sicher, dass Sie alle Weine löschen möchten?";
    final String MELDUNG_WEINDATEN_GELÖSCHT = "Interne Weindaten erfolgreich gelöscht.";

    @FXML
    private VBox rootVBox;
    @FXML
    private MenuItem menuItemWeinLöschen, menuItemAllesLöschen;
    @FXML
    private TableView<Wine> wineTable;
    @FXML
    private TableColumn<Wine, String> tableColumnBestellNr, tableColumnJahrgang,
            tableColumnName, tableColumnFarbe, tableColumnBruttoflaschenpreis;
    
    public static Set<Wine> markierteWeineListe = new HashSet<>();
    
    public static ObservableList<Wine> weinListe = FXCollections.observableArrayList(); // "Observable" damit die Elemente dieser Liste direkt an die Zeilen der Tabelle gekoppelt sind.
    
    public void initialize(){
        weinListe.clear();
        weinListe.setAll(Wine.weine); // Jedes Mal, wenn Fenster geöffnet/initialisiert wird, bekommt die ObservableList die internen Weindaten aus Wine.weine
        
        tableColumnBestellNr.setCellValueFactory(new PropertyValueFactory<>("bestellNr"));
        tableColumnJahrgang.setCellValueFactory(new PropertyValueFactory<>("jahrgang"));
        tableColumnName.setCellValueFactory(new PropertyValueFactory<>("weinname"));
        tableColumnFarbe.setCellValueFactory(new PropertyValueFactory<>("farbe"));
        tableColumnBruttoflaschenpreis.setCellValueFactory(new PropertyValueFactory<>("flaschenpreisBrutto"));
        
        wineTable.setItems(weinListe);
        
        //Mehrere Zeilen können gleichzeitig markiert sein.
        wineTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        
        /*
        Alle markierten Zeilen werden automatisch in "selectedItems" gespeichert und
        gelöscht, wenn die Markierung entfernt wird.
        */
        wineTable.getSelectionModel().getSelectedItems().addListener((ListChangeListener<Wine>) change -> {
            markierteWeineListe.clear();
            markierteWeineListe.addAll(wineTable.getSelectionModel().getSelectedItems());
            //System.out.println("Aktuell ausgewählt: " + markierteWeineListe);
        });
        
        // Überwachen, wann das Fenster geladen ist, um das Schließen abzufangen
        // Sobald Fenster geschlossen wird, wird die Collection mit den markierten Zeilen geleert.
        rootVBox.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.windowProperty().addListener((obs, oldWindow, newWindow) -> {
                    if (newWindow != null) {
                        Stage stage = (Stage) newWindow;
                        
                        // Wenn das Fenster geschlossen wird (egal ob über 'X' oder Code)
                        stage.setOnCloseRequest(event -> {
                            markierteWeineListe.clear(); // Leert die Liste beim Schließen!
                        });
                        
                        // Alternativ (falls Fenster auch über einen Schließen-Button im UI zugehen):
                        stage.setOnHiding(event -> {
                            markierteWeineListe.clear();
                        });
                    }
                });
            }
        });
        System.out.println("Weine intern: " + Wine.weine);
    }
    
    
    /*
    Alle Weine, die in der Weinliste markiert bzw. angeklickt wurden,
    werden gelöscht.
    */
    @FXML
    private void auswahlLöschen(ActionEvent event) {
        if(Wine.weine.isEmpty()){
            zeigeKeineDatenZumLöschenMeldefenster();
        }
        else if(markierteWeineListe.isEmpty()){
            zeigeKeineMarkiertenDatenZumLöschenMeldefenster();
        }
        else {
            if(zeigeNachfrageLöschenMeldefenster().equals(ButtonType.YES)){
                /*System.out.println("markierteWeineListe: " + markierteWeineListe);
                System.out.println();
                System.out.println("weinListe: " + weinListe);
                System.out.println();
                System.out.println("weine: " + Wine.weine);
                System.out.println("------------------------------");*/
                
                
                Wine.weine.removeAll(markierteWeineListe);
                weinListe.removeAll(markierteWeineListe);
                markierteWeineListe.clear();
                
                //HauptfensterController.zeilenMitKorrektemFormatListe.clear();
                zeigeBestätigungLöschenMeldefenster();
                
                
                /*System.out.println("markierteWeineListe: " + markierteWeineListe);
                System.out.println();
                System.out.println("weinListe: " + weinListe);
                System.out.println();
                System.out.println("weine: " + Wine.weine);*/
            }
        }
    }
    
    
    /*
    Alle Weine, in der Weinliste werden gelöscht,
    unabhängig ob sie markiert sind oder nicht.
    */
    @FXML
    private void allesLöschen(ActionEvent event) {
        if(weinListe.isEmpty()){
            zeigeKeineDatenZumLöschenMeldefenster();
        }
        else {
            if(zeigeNachfrageAllesLöschenMeldefenster().equals(ButtonType.YES)){
                markierteWeineListe.clear();
                Wine.weine.clear();
                weinListe.clear();
                HauptfensterController.zeilenMitKorrektemFormatListe.clear();
                HauptfensterController.zeilenMitKorrektemFormatListe.clear();
                HauptfensterController.zuletztGeöffneteDatei = null;
            }
        }
        
    }
    
    
    public void zeigeKeineDatenZumLöschenMeldefenster(){
        Alert alert = new Alert(Alert.AlertType.ERROR, MELDUNG_KEINE_DATEN_ZUM_LÖSCHEN, ButtonType.OK);
        alert.setTitle("Fehler");
        alert.setHeaderText("Fehler beim Löschvorgang");
        Optional<ButtonType> antwort = alert.showAndWait();
    }
    
    public void zeigeKeineMarkiertenDatenZumLöschenMeldefenster(){
        Alert alert = new Alert(Alert.AlertType.ERROR, MELDUNG_KEINE_MARKIERTEN_DATEN_ZUM_LÖSCHEN, ButtonType.OK);
        alert.setTitle("Fehler");
        alert.setHeaderText("Fehler beim Löschvorgang");
        Optional<ButtonType> antwort = alert.showAndWait();
    }
    
    public ButtonType zeigeNachfrageLöschenMeldefenster(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, MELDUNG_WEIN_LÖSCHEN_ANFRAGE, ButtonType.YES, ButtonType.NO);
        alert.setTitle("Bestätigung");
        alert.setHeaderText("Weindaten löschen");

        Optional<ButtonType> antwort = alert.showAndWait();
        if(antwort.isPresent()){
            return antwort.get();
        }
        return null;
    }
    
    public ButtonType zeigeNachfrageAllesLöschenMeldefenster(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, MELDUNG_ALLE_WEINE_LÖSCHEN_ANFRAGE, ButtonType.YES, ButtonType.NO);
        alert.setTitle("Bestätigung");
        alert.setHeaderText("Weindaten löschen");

        Optional<ButtonType> antwort = alert.showAndWait();
        if(antwort.isPresent()){
            return antwort.get();
        }
        return null;
    }
    
    public void zeigeBestätigungLöschenMeldefenster(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, MELDUNG_WEINDATEN_GELÖSCHT, ButtonType.OK);
        alert.setTitle("Bestätigung");
        alert.setHeaderText("Weindaten gelöscht");
        Optional<ButtonType> antwort = alert.showAndWait();
    }
    
}
