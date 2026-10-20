/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.weinhaendler;

import java.util.ArrayList;
import java.util.Optional;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 *
 * @author sfrie
 */
public class WeineingabeController {
    
    final String PFLICHTFELD_ROT_MARKIERT = "-fx-border-color: red; -fx-border-radius: 5";
    final String PFLICHTFELD_NICHT_MARKIERT = "-fx-border-color: transparent; -fx-border-radius: 5";
    final String MELDUNG_PFLICHTFELDER_LEER = "Bitte vergewissern Sie sich, dass alle Pflichtfelder\nvollständig und korrekt ausgefüllt sind.";
    final String MELDUNG_EINGABEN_VERWERFEN = "Möchten Sie die Eingabe wirklich abbrechen?\nAlle bisherigen Eingaben gehen damit verloren.";
    final String MELDUNG_EINGABEN_SPEICHERN = "Ihre Eingaben wurden zwischengespeichert.";
    final String MELDUNG_ANWENDUNG_SCHLIESSEN = "Möchten Sie die Weineingabe wirklich schließen?";
    final double MEHRWERTSTEUER = 0.19;
    
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private Label labelBestellnummer, labelFarbe, labelHerkunftsland, labelGeschmack;
    @FXML
    private Label labelAlkoholgehalt, labelParker, labelPreisProFlasche;
    @FXML
    private CheckBox disableAlkoholCheckBox, checkboxVegan, checkboxKeineSulfite,
            disableParkerCheckBox, disableLagerdauerCheckBox;
    @FXML
    private TextField textfieldBestellnummer, textfieldWeinname, textfieldErzeuger,  textfieldJahrgang,
            textfieldFlaschenpreisButto, textfieldFlaschenpreisNetto, textfieldLiterpreisNetto,
            textfieldLiterpreisButto;
    @FXML
    private ToggleGroup toggleGroupFarbe, toggleGoupGeschmack;
    @FXML
    private ComboBox<String> comboBoxHerkunftsland, comboBoxAnbaugebiet, comboBoxFlaschengroeße;
    @FXML
    private Spinner<Double> spinnerAlkoholgehalt;
    @FXML
    private Spinner<Integer> spinnerParker, spinnerLagerdauer;
    @FXML
    private Button buttonSpeichern, buttonAbbrechen;
    
    ObservableList<String> herkunftslandList = FXCollections.observableArrayList("Italien", "Frankreich", "Spanien", "Deutschland", "Amerika");
       
    ObservableList<String> angaubgebietItalienList = FXCollections.observableArrayList("Abruzzen", "Basilikata", "Ligurin", "Piemont");
    ObservableList<String> angaubgebietFrankreichList = FXCollections.observableArrayList("Elsass Pinot Noir", "Bordeaux Côtes de Francs", "Bourgogne Aligoté", "Vin de Bugey");
    ObservableList<String> angaubgebietSpanienList = FXCollections.observableArrayList("El Bierzo", "Toro", "Empordâ-Costa Brava", "Montilla-Moriles");
    ObservableList<String> angaubgebietDeutschlandList = FXCollections.observableArrayList("Ahr", "Mittelrhein", "Rheingau", "Saale-Unstrut");
         
    ObservableList<String> flaschengroeßeList = FXCollections.observableArrayList("0,25", "0,375", "0,5", "0,62", "0,75", "1,0", "1,5", "3,0", "3,5", "6,0");
    
    ObservableList<Node> pflichtfelderListe;
    
    ObservableList<TextField> textFieldList;
    
    
    public void initialize(){
        
        pflichtfelderListe = FXCollections.observableArrayList(
                textfieldBestellnummer, textfieldWeinname,
                comboBoxHerkunftsland,
                comboBoxFlaschengroeße,
                textfieldFlaschenpreisButto, textfieldFlaschenpreisNetto,
                textfieldLiterpreisButto, textfieldLiterpreisNetto
                );
    
        comboBoxHerkunftsland.setItems(herkunftslandList);
        comboBoxFlaschengroeße.setItems(flaschengroeßeList);
        comboBoxAnbaugebiet.disableProperty().bind(Bindings.isNull(comboBoxHerkunftsland.valueProperty()));
        
        
        //Short Cut "ESC" für buttonAbbrechen:
        buttonAbbrechen.setCancelButton(true);
        
        //Short Cut "Str + s" für buttonSpeichern:
            // Listener hinzufügen, um sicherzustellen, dass die Scene gesetzt wurde
        anchorPane.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                // Shortcut Strg+S hinzufügen, wenn die Scene gesetzt wurde
                newScene.getAccelerators().put(
                    new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN),
                    () -> speichern(new ActionEvent()) // Speichern-Methode aufrufen
                );
            }
        });
        
        
        //Spinner initialisieren und deklarieren:
        SpinnerValueFactory<Double> alkoholWerte = 
            new SpinnerValueFactory.DoubleSpinnerValueFactory(7.5, 25., 12, 0.5);
        //Startwert, Endwert, Initialwert, Schritte
        spinnerAlkoholgehalt.setValueFactory(alkoholWerte);
        spinnerAlkoholgehalt.disableProperty().bind(disableAlkoholCheckBox.selectedProperty());
        
        SpinnerValueFactory<Integer> parkerWerte = 
            new SpinnerValueFactory.IntegerSpinnerValueFactory(50, 100, 100, 1);
        spinnerParker.setValueFactory(parkerWerte);
        spinnerParker.disableProperty().bind(disableParkerCheckBox.selectedProperty());
        
        SpinnerValueFactory<Integer> lagerdauerWerte = 
            new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 12, 1, 1);
        spinnerLagerdauer.setValueFactory(lagerdauerWerte);
        spinnerLagerdauer.disableProperty().bind(disableLagerdauerCheckBox.selectedProperty());
        
        
        //Standardwert in ComboBox setzen:
        comboBoxFlaschengroeße.setValue(comboBoxFlaschengroeße.getItems().get(0));
        
        
        //Memorics:
        labelBestellnummer.setLabelFor(textfieldBestellnummer);
        labelFarbe.setLabelFor((Node) toggleGroupFarbe.getToggles().get(0));
        labelHerkunftsland.setLabelFor(comboBoxHerkunftsland);
        labelAlkoholgehalt.setLabelFor(spinnerAlkoholgehalt);
        labelGeschmack.setLabelFor((Node) toggleGoupGeschmack.getToggles().get(0));
        labelParker.setLabelFor(spinnerParker);
        labelPreisProFlasche.setLabelFor(textfieldFlaschenpreisButto);
        
        
        //Tooltips:
        Tooltip tooltipBestellNr = new Tooltip("Jahreszahl-Großbuchstaben (ohne Sonderz.)-Ziffern");
        Tooltip.install(textfieldBestellnummer, tooltipBestellNr);
        
        Tooltip tooltipParkerPunkte = new Tooltip("Die Parker-Punkte reichen von 50 bis 100");
        Tooltip.install(spinnerParker, tooltipParkerPunkte);
       
        
        //Kommata "," sollen in Textfeldern direkt in "." transformiert werden.
        textfieldFlaschenpreisButto.setTextFormatter(createTextFormatterKommaZuPunktUndNurZahlen());
        textfieldFlaschenpreisNetto.setTextFormatter(createTextFormatterKommaZuPunktUndNurZahlen());
        textfieldLiterpreisButto.setTextFormatter(createTextFormatterKommaZuPunktUndNurZahlen());
        textfieldLiterpreisNetto.setTextFormatter(createTextFormatterKommaZuPunktUndNurZahlen());
        textfieldJahrgang.setTextFormatter(createTextFormatterKommaZuPunktUndNurZahlen());
        
        
        //Code für Betragsfelder ->
        textFieldList = FXCollections.observableArrayList(textfieldFlaschenpreisButto, textfieldFlaschenpreisNetto,
                textfieldLiterpreisButto, textfieldLiterpreisNetto);
        //FocusListener für Betragsfelder aus "textFieldList
        for (TextField currentTextField : textFieldList) {
            // Ursprünglichen Wert speichern, wenn Fokus auf das Textfeld kommt
            currentTextField.focusedProperty().addListener((observable, focusLost, focusGained) -> {
                if (focusGained) {
                    currentTextField.setUserData(currentTextField.getText());
                } else {
                    // Beim Verlassen überprüfen, ob sich der Wert geändert hat
                    String originalValue = (String) currentTextField.getUserData();
                    if (!currentTextField.getText().equals(originalValue)) {
                        if (comboBoxFlaschengroeße.getValue() != null) {
                            flaschenUndLiterpreiseBerechnen(currentTextField);
                        }
                    }
                }
            });
        }

        comboBoxFlaschengroeße.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                aktualisiereTextFelderBeiFlaschengroesseWechsel();
            }
        });
    }
    
    
    // Methode zum Erstellen eines TextFormatters
    private TextFormatter<String> createTextFormatterKommaZuPunktUndNurZahlen() {
        return new TextFormatter<>(change -> {
            if (change.getText().contains(",") || change.getText().matches("[0-9]*")) { //Wenn Zahlen enthalten, dann übernehmen
                change.setText(change.getText().replace(".", ","));
                return change;
            }
            return null; //Alles, was keine Zahl ist nicht übernehmen
        });
    }
    
    
    /*
    Falls es fehlende Nutzereingaben in den Pflichtfeldern
    flaschenpreisBrutto, flaschenpreisNetto, literpreisBrutto, literpreisNetto gibt,
    werden diese automatisch vervollständigt solange mindestens eines davon beschrieben ist und
    eine Flaschengröße in der ComboBox ausgewählt ist.
    */
    
    private void aktualisiereTextFelderBeiFlaschengroesseWechsel() {
        for (TextField textField : textFieldList) {
            if (!textField.getText().isEmpty()) {
                flaschenUndLiterpreiseBerechnen(textField);
                break;
            }
        }
    }
    private void flaschenUndLiterpreiseBerechnen(TextField textfield) {
        String flaschenpreisBrutto = textfieldFlaschenpreisButto.getText();
        String flaschenpreisNetto = textfieldFlaschenpreisNetto.getText();
        String literpreisBrutto = textfieldLiterpreisButto.getText();
        String literpreisNetto = textfieldLiterpreisNetto.getText();
        String flaschengroeße = comboBoxFlaschengroeße.getValue();

        if (flaschengroeße == null || flaschengroeße.isEmpty()) {
            return; // Keine Berechnung ohne Flaschengröße
        }

        double groesse = parseDoubleSafe(flaschengroeße);

        if (textfield == textfieldFlaschenpreisButto && !flaschenpreisBrutto.isEmpty()) {
            double netto = round(parseDoubleSafe(flaschenpreisBrutto) / (1 + MEHRWERTSTEUER));
            textfieldFlaschenpreisNetto.setText(formatDouble(netto));
            double literBrutto = round(parseDoubleSafe(flaschenpreisBrutto) / groesse);
            textfieldLiterpreisButto.setText(formatDouble(literBrutto));
            double literNetto = round(netto / groesse);
            textfieldLiterpreisNetto.setText(formatDouble(literNetto));
        } else if (textfield == textfieldFlaschenpreisNetto && !flaschenpreisNetto.isEmpty()) {
            double brutto = round(parseDoubleSafe(flaschenpreisNetto) * (1 + MEHRWERTSTEUER));
            textfieldFlaschenpreisButto.setText(formatDouble(brutto));
            double literNetto = round(parseDoubleSafe(flaschenpreisNetto) / groesse);
            textfieldLiterpreisNetto.setText(formatDouble(literNetto));
            double literBrutto = round(brutto / groesse);
            textfieldLiterpreisButto.setText(formatDouble(literBrutto));
        } else if (textfield == textfieldLiterpreisButto && !literpreisBrutto.isEmpty()) {
            double netto = round(parseDoubleSafe(literpreisBrutto) / (1 + MEHRWERTSTEUER));
            textfieldLiterpreisNetto.setText(formatDouble(netto));
            double flaschenBrutto = round(parseDoubleSafe(literpreisBrutto) * groesse);
            textfieldFlaschenpreisButto.setText(formatDouble(flaschenBrutto));
            double flaschenNetto = round(netto * groesse);
            textfieldFlaschenpreisNetto.setText(formatDouble(flaschenNetto));
        } else if (textfield == textfieldLiterpreisNetto && !literpreisNetto.isEmpty()) {
            double brutto = round(parseDoubleSafe(literpreisNetto) * (1 + MEHRWERTSTEUER));
            textfieldLiterpreisButto.setText(formatDouble(brutto));
            double flaschenNetto = round(parseDoubleSafe(literpreisNetto) * groesse);
            textfieldFlaschenpreisNetto.setText(formatDouble(flaschenNetto));
            double flaschenBrutto = round(brutto * groesse);
            textfieldFlaschenpreisButto.setText(formatDouble(flaschenBrutto));
        }
    }

    private double parseDoubleSafe(String value) {
        try {
            return Double.parseDouble(value.replace(",", "."));
        } catch (NumberFormatException e) {
            return 0.0; // Standardwert bei Fehler
        }
    }

    private String formatDouble(double value) {
        return String.format("%.2f", value).replace(".", ",");
    }

    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
    
    //Code für Betragsfelder <-
    
    
    
    
    //Verbindet ComboBox_Herkunftsland mit ComboBox_Anbaugebiet:
    @FXML
    private void gebietZuLand(ActionEvent event) {
        if(event.getSource() == comboBoxHerkunftsland){
            String land = (String) comboBoxHerkunftsland.getSelectionModel().getSelectedItem();
            if(land == null){
                return;
            }
            if(land.equals("Italien")){
                comboBoxAnbaugebiet.setItems(angaubgebietItalienList);
            }
            if(land.equals("Frankreich")){
                comboBoxAnbaugebiet.setItems(angaubgebietFrankreichList);
            }
            if(land.equals("Spanien")){
                comboBoxAnbaugebiet.setItems(angaubgebietSpanienList);
            }
            if(land.equals("Deutschland")){
                comboBoxAnbaugebiet.setItems(angaubgebietDeutschlandList);
            }
        }
    }
    
    //Setzt die Auswahl in ComboBox_Herkunftsland mit ComboBox_Anbaugebiet wieder zurück:
    @FXML
    private void herkunftUndAnbauZurücksetzen(ActionEvent event) {
        comboBoxHerkunftsland.setValue(null);
        comboBoxAnbaugebiet.setValue(null);
    }

    
    ////////////////////////////////////////////////////////////////////////////////////
    
    
    /*
    Prüft zuerst, ob das Pflichtfeld "textfieldBestellnummer" ausgefüllt und sntaktisch korrekt ist.
    Dann werden alle anderen Pflichtfelder ab dem zweiten Index durchlaufen und
    geprüft, ob sie ausgefüllt sind.
    Wenn alle Pflichtfelder ausgefüllt sind und das erste sowohl ausgefüllt als auch
    syntaktisch korrekt ist, gibt die Methode true zurück.
    */
    public boolean pflichtfelderKorrektUndAusgefüllt(ObservableList<Node> pflichtfelderListe){
        ArrayList<Boolean> pflichtfelderAusgefülltListe = new ArrayList<>();
        boolean istKorrektUndAusgefülltBestellNr = ((TextField) pflichtfelderListe.get(0))
                .getText().matches("[0-9]{4}\\-[a-zA-Z]{4}\\-[0-9]{4}");
        //Syntax und Ausgefülltheit von Pflichtfeld "textfieldBestellnummer" prüfen.
        if(!istKorrektUndAusgefülltBestellNr){
            pflichtfelderListe.get(0).setStyle(PFLICHTFELD_ROT_MARKIERT);
        }
        else {
            pflichtfelderListe.get(0).setStyle(PFLICHTFELD_NICHT_MARKIERT);
        }
        
        /*
        Pflichtfelder ab dem zweiten Index durchlaufen,
        weil der erste oben zusammen mit der Syntax bereits geprüft wurde.
        */
        for(int i=1; i<pflichtfelderListe.size(); i++){
            Node node = pflichtfelderListe.get(i);
            //Prüfen, ob Pflicht-Textfelder ausgefüllt sind.
            if(pflichtfelderListe.get(i) instanceof TextField){
                if(((TextField) node).getText().equals("")){
                    pflichtfelderAusgefülltListe.add(false);
                    node.setStyle(PFLICHTFELD_ROT_MARKIERT);
                }
                else {
                    pflichtfelderAusgefülltListe.add(true);
                    node.setStyle(PFLICHTFELD_NICHT_MARKIERT);
                }
            }
            
            //Prüfen, ob Pflicht-ComboBoxen ausgefüllt sind.
            if(node instanceof ComboBox){
                if(((ComboBox) node).getValue() == null){
                    pflichtfelderAusgefülltListe.add(false);
                    node.setStyle(PFLICHTFELD_ROT_MARKIERT);
                }
                else {
                    pflichtfelderAusgefülltListe.add(true);
                    node.setStyle(PFLICHTFELD_NICHT_MARKIERT);
                }
            }
        }
        
        return istKorrektUndAusgefülltBestellNr && !pflichtfelderAusgefülltListe.contains(false);
    }
    
    /*
    Alle Pflicht-Weindaten werden in einem Objekt "Wein" gespeichert.
    Wichtig: Umlaute können nicht übernommen werden.
    */
    public Wine weindatenSpeichern(){
        String bestellNr = textfieldBestellnummer.getText();
        String weinname = textfieldWeinname.getText();
        String erzeuger = (textfieldErzeuger.getText().equals(""))
                ? "/"
                : textfieldErzeuger.getText();
        String jahrgang = (textfieldJahrgang.getText().equals(""))
                ? "/"
                : textfieldJahrgang.getText();
        String farbe = String.valueOf(((RadioButton) toggleGroupFarbe.getSelectedToggle()).getText());
        String herkunftsland = comboBoxHerkunftsland.getValue();
        String anbaugebiet = (comboBoxAnbaugebiet.getValue() == null)
                ? "/"
                : comboBoxAnbaugebiet.getValue();
        String alkoholgehalt = !(disableAlkoholCheckBox.isSelected())
                ? spinnerAlkoholgehalt.getValue().toString()
                : "0.";
        String sulfite = (checkboxKeineSulfite.isSelected())
                ? "nein"
                : "ja";
        String vegan = (checkboxVegan.isSelected())
                ? "ja"
                : "nein";
        String geschmack = (String.valueOf(((RadioButton) toggleGoupGeschmack.getSelectedToggle()).getText()).equals("keine Angabe"))
                ? "/"
                : String.valueOf(((RadioButton) toggleGoupGeschmack.getSelectedToggle()).getText());
        String parkerPunkte = disableParkerCheckBox.isSelected()
                ? "/"
                : spinnerParker.getValue().toString();
        String lagerdauer = disableLagerdauerCheckBox.isSelected()
                ? "/"
                : spinnerLagerdauer.getValue().toString().equals("1.0")
                    ? spinnerLagerdauer.getValue().toString() + " Jahr"
                    : spinnerLagerdauer.getValue().toString() + " Jahre";
        String flaschengröße = comboBoxFlaschengroeße.getValue();
        /*String flaschenpreis = "(" + textfieldFlaschenpreisButto.getText() + " EUR brutto und "
                + textfieldFlaschenpreisNetto.getText() + " EUR netto)";
        String literpreis = "(" + textfieldLiterpreisButto.getText() + " EUR brutto und "
                + textfieldLiterpreisNetto.getText() + " EUR netto)";*/
        String flaschenpreisBrutto = textfieldFlaschenpreisButto.getText();
        String flaschenpreisNetto = textfieldFlaschenpreisNetto.getText();
        String literpreisBrutto = textfieldLiterpreisButto.getText();
        String literpreisNetto = textfieldLiterpreisNetto.getText();
        
        Wine wein = new Wine(bestellNr, weinname, erzeuger, jahrgang, farbe, herkunftsland, anbaugebiet,
                alkoholgehalt, sulfite, vegan, geschmack, parkerPunkte, lagerdauer,
                flaschengröße, flaschenpreisBrutto, flaschenpreisNetto, literpreisBrutto, literpreisNetto);
        
        return wein;
    }
    
    //Setzt ALLE Eingaben zurück sowie farbliche Markierungen
    public void reset(){
        // Inputs zurücksetzen:
        for(Node node: anchorPane.getChildren()){
            //Textfelder zurücksetzen.
            if(node instanceof TextField){
                ((TextField) node).setText("");
            }
            //ComboBoxen zurücksetzen.
            if(node instanceof ComboBox<?>){
                    ((ComboBox<?>) node).setValue(null);
            }
            comboBoxFlaschengroeße.setValue("0,25");
            //CheckBoxen zurücksetzen.
            if(node instanceof CheckBox){
                ((CheckBox) node).setSelected(false);
            }
            
        }
        //Spinnder auf Initialwert zurücksetzen.
        spinnerAlkoholgehalt.getValueFactory().setValue(12.);
        spinnerParker.getValueFactory().setValue(100);
        spinnerLagerdauer.getValueFactory().setValue(1);
        
        //Zurücksetzen auf Standard-Radiobutton.
        toggleGroupFarbe.getToggles().get(0).setSelected(true);
        toggleGoupGeschmack.getToggles().get(4).setSelected(true);
        
        // Rote Markierungen der Pflichtfelder zurücksetzen
        for(Node node: pflichtfelderListe){
            node.setStyle(PFLICHTFELD_NICHT_MARKIERT);
        }
    }
    
    @FXML
    private void speichern(ActionEvent event) {
        //flaschenUndLiterpreiseBerechnen();
        boolean pflichtfelderKorrektUndAusgefüllt = pflichtfelderKorrektUndAusgefüllt(pflichtfelderListe);
        if(pflichtfelderKorrektUndAusgefüllt){
            if(meldefensterEingabenSpeichern().equals(ButtonType.OK)){
                weindatenSpeichern();
                reset();
            }
        }
        else {
            meldefensterFehlendeEingaben();
        }
    }
    
    @FXML
    private void abbrechen(ActionEvent event) {
        //Prüft, ob Eingaben getätigt wurden.
        if(eingabenGetaetigt()){
            //Wirft ein Meldefenster. Wenn "YES" geklickt wurde, dann wird Methode reset() ausgeführt.
            if(meldefensterEingabenVerwerfen().equals(ButtonType.YES)){
                reset();
            }
        }
        else {
            reset(); // Für den Fall, dass ohne Eingaben auf "Speichern" geklickt wurde, wird durch "reset" zumindest die roten Markierungen entfernt.
        }
    }
    
    
    //////////////////////////////////////////////////////////////////////////
    
    
    
     public boolean eingabenGetaetigt(){
        ArrayList<Boolean> eingabeGetaetigtListe = new ArrayList<>();
        
        for(Node node: anchorPane.getChildren()){
            if(node instanceof TextField){
                if(((TextField)node).getText().equals("")){
                    eingabeGetaetigtListe.add(false);
                }
                else {
                    eingabeGetaetigtListe.add(true);
                }
            }
                
            /*if(node instanceof ComboBox){
                if(((ComboBox)node).getValue() == null){
                    eingabeGetaetigtListe.add(false);
                }
                else {
                    eingabeGetaetigtListe.add(true);
                }
            }*/
            
            if(node instanceof CheckBox){
                if(!((CheckBox)node).isSelected()){
                    eingabeGetaetigtListe.add(false);
                }
                else {
                    eingabeGetaetigtListe.add(true);
                }
            }
            
        }
        
        //Hier soll nur eine ComboBox_Herkunftsland geprüft werden, weil ComboBox Flaschengröße per default immer einen Wert hat.
        if(comboBoxHerkunftsland.getValue() == null){
                    eingabeGetaetigtListe.add(false);
                }
                else {
                    eingabeGetaetigtListe.add(true);
                }
        
        return eingabeGetaetigtListe.contains(true);
    }
    
    
    //Meldefenster, welches mitteilt, dass nicht alle Pflichtfelder korrekt oder vollständig ausgefüllt sind.
    public ButtonType meldefensterFehlendeEingaben(){
        Alert alert = new Alert(Alert.AlertType.ERROR, MELDUNG_PFLICHTFELDER_LEER, ButtonType.OK);
        Optional<ButtonType> antwort = alert.showAndWait();
        if(antwort.isPresent()){
            return antwort.get();
        }
        else {
            return null;
        }
    }
    
    //Meldefenster, welches fragt, ob die Eingaben wirklich verworfen werden sollen.
    public ButtonType meldefensterEingabenVerwerfen(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, MELDUNG_EINGABEN_VERWERFEN, ButtonType.YES, ButtonType.NO);
        alert.setTitle("Bestätigung");
        alert.setHeaderText("Eingabe abbrechen");
        Optional<ButtonType> antwort = alert.showAndWait();
        if(antwort.isPresent()){
            return antwort.get();
        }
        else {
            return null;
        }
    }
    
    //Meldefenster, welches fragt, ob die Eingaben wirklich gespeichert werden sollen.
    public ButtonType meldefensterEingabenSpeichern(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION, MELDUNG_EINGABEN_SPEICHERN, ButtonType.OK);
        Optional<ButtonType> antwort = alert.showAndWait();
        if(antwort.isPresent()){
            return antwort.get();
        }
        else {
            return null;
        }
    }
    
    
    // Methode, die beim Schließen des Zweitfensters aufgerufen wird
    public void zweitfensterSchliessenMeldungErzeugen(Stage stage) {
        stage.setOnCloseRequest(event -> {
            // Standardverhalten des events beim Fensterschließ-Vorgang unterdrücken
            event.consume();

            //Meldefenster anzeigen
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Bestätigung");
            alert.setHeaderText("Anwendung schließen");
            alert.setContentText(MELDUNG_ANWENDUNG_SCHLIESSEN);
            
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                stage.close(); // Schließt die Anwendung
            }
        });
    };
    
}
