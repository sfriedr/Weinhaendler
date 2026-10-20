/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.weinhaendler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Optional;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import javafx.scene.control.ButtonBar;
import static javafx.scene.control.ButtonBar.ButtonData.CANCEL_CLOSE;
import static javafx.scene.control.ButtonBar.ButtonData.NO;
import static javafx.scene.control.ButtonBar.ButtonData.YES;
import javafx.scene.image.Image;

/**
 *
 * @author sfrie
 */
public class HauptfensterController {
    public static File zuletztGeöffneteDatei = null;
    
    final String MELDUNG_DATEI_GEPSPEICHERT = "Datei erfolgreich gespeichert.";
    final String MELDUNG_DATEI_LEER = "Sie können keine leere Datei laden.";
    final String MELDUNG_IMPORT_FEHLER = "Datei konnte nicht importiert werden.";
    final String MELDUNG_ERFOLG_ÜBERSCHREIBUNG_INTERN = "Interne Weindaten erfolgreich\nüberschrieben.";
    final String MELDUNG_ERFOLG_ERGÄNZEN_INTERN = "Interne Weindaten erfolgreich ergänzt.";
    final String MELDUNG_DATEI_LADEN_ABFRAGE = "Sollen die externen Weindaten\nden internen Weindaten\nhinzugefügt oder\nüberschrieben werden?";
    final String MELDUNG_KEINE_DATEN_ZUM_LÖSCHEN = "Es liegen keine internen Weindaten vor,\ndie gelöscht werden könnten.";
    final String MELDUNG_WEIN_LÖSCHEN_ANFRAGE = "Sind Sie sicher, dass Sie alle internen Weindaten\nlöschen möchten?";
    final String MELDUNG_WEINDATEN_GELÖSCHT = "Alle internen Weindaten erfolgreich gelöscht.";
    final String MELDUNG_INFO_TEXT = "Entwickler: Sebastian Friedrich\nJava Version: %s\nJavaFX Version: %s";
    final String ICON = "Firmenlogo.png";
    
    @FXML
    private MenuItem menuItemKopieren, menuItemEinfuegen, meniItemAusschneiden,
            menuItemSpeichern, menuItemBeenden, menuItemLoeschen;
    
    public static ArrayList<String> zeilenMitKorrektemFormatListe = new ArrayList<>();
    
    public void initialize(){
        //menuItem.setMnemonicParsing(true);
        
        //Short Cuts
        menuItemKopieren.setAccelerator(new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_DOWN)); //Kopieren
        menuItemEinfuegen.setAccelerator(new KeyCodeCombination(KeyCode.V, KeyCombination.CONTROL_DOWN)); //Einfügen
        meniItemAusschneiden.setAccelerator(new KeyCodeCombination(KeyCode.X, KeyCombination.CONTROL_DOWN)); //Ausschneiden
        menuItemSpeichern.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN)); //Speichern
        menuItemLoeschen.setAccelerator(new KeyCodeCombination(KeyCode.DELETE, KeyCombination.CONTROL_DOWN)); //Löschen
        menuItemBeenden.setAccelerator(new KeyCodeCombination(KeyCode.W, KeyCombination.CONTROL_DOWN)); //Beenden (ALT + F4 oder STRG + W)
    }
    
    
    /*
    Erzeugt ein Meldefenster vor dem Schließvorgang des Hauptfensters,
    welcher durch die Schließen-Schaltfäche ausgelöst wurde.
    */
    public void hauptfensterSchliessenMeldungErzeugen(Stage stage) {
        //Hinzufügen des Schließen-Handlers für das Fenster
        stage.setOnCloseRequest(event -> {
            // Standardverhalten des events beim Fensterschließ-Vorgang unterdrücken
            event.consume();

            // Meldefenster anzeigen
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Bestätigung");
            alert.setHeaderText("Möchten Sie die Anwendung wirklich beenden?");
            alert.setContentText("Bitte bestätigen Sie Ihre Auswahl.");
            
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Fenster schließen
                stage.close();
            }
        });
    }
    
    
    /*
    Erzeugt ein Meldefenster vor dem Schließvorgang des Hauptfensters,
    welcher durch MenuItem "menuItemBeenden" ausgelöst wurde.
    */
    @FXML
    public void hauptfensterSchliessenMeldungErzeugen(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Bestätigung");
            alert.setHeaderText("Möchten Sie die Anwendung wirklich beenden?");
            alert.setContentText("Bitte bestätigen Sie Ihre Auswahl.");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Fenster schließen
                Platform.exit();
            }
    }
    
    //Über das MenuItem "Aufnehmen" wird das Fenster zur Weineingabe geöffnet.
    @FXML
    private void weineingabeOeffnen(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("WeineingabeView.fxml"));
        Parent root = loader.load();
        WeineingabeController controller = loader.getController();
        
        Stage stage = new Stage();
        controller.zweitfensterSchliessenMeldungErzeugen(stage); // Übergibt das Stage-Objekt an den Controller
        
        stage.getIcons().add(new Image(getClass().getResourceAsStream(ICON))); //Fenster-Icon anlegen.
        stage.setScene(new Scene(root));
        stage.setTitle("Weineingabe");
        stage.show();
    }
    
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    
    //Über das MenuItem "speichern" wird die Methode speichern(ActionEvent event) ausgelöst.
    /*
    Es wird als erstes geprüft, ob die Datei, die man speichern möchte die zuletzt gespeicherte Datei ist,
    anhand der Klassenvariable "zuletztGeöffneteDatei". Also, ob die Datei schon mal gespeichert wurde.
    Wenn die Datei bereits gespeichert wurde, wird sie überschrieben, ohne dass der FileChooser 
    nochmal aufgerufen werden muss.
    Wenn die Datei das erste Mal gespeichert wird (also "zuletztGeöffneteDatei" = null),
    dann wird der FileChooser-Diaglog geöffnet, bei welchem man einen Speicherort und Dateinamen übergibt.
    */
    @FXML
    private void speichern(ActionEvent event) throws IOException{
        if(zuletztGeöffneteDatei != null){ // Wenn vorher schob mal gespeichert wurde ...
            //System.out.println("File1: " + Files.readAllLines(Path.of(zuletztGeöffneteDatei.getPath())));
            dateiÜberschreiben(zuletztGeöffneteDatei); //, dann wird die Datei überschrieben.
        }
        else {
            //Erzeugung von FileChooser
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Wein speichern unter ...");

            //ExtensionFilter für verschiedene Dateitypen:
            FileChooser.ExtensionFilter textTyp = new FileChooser.ExtensionFilter("CSV-Dateien", "*.csv");

            //Dateityp dem FileChooser hinzufügen.
            fileChooser.getExtensionFilters().add(textTyp);

            File file = fileChooser.showSaveDialog(new Stage()); //Zeigt den Speicherdialog an und erzeugt leere Datei zum Speichern des Weinobjektes.
            dateiSpeichern(file);
        }
    }
    
    
   
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    
    
    /*
    Über das MenuItem "speichern unter" wird die Methode speichernUnter(ActionEvent event) ausgelöst und 
    ein FileChooser-Dialog geöffnet.
    Über diesen Dialog wählt man Speicherort und Dateiname aus, um darin die internen Weindaten zu speichern.
    */
    @FXML
    private void speichernUnter(ActionEvent event) throws IOException {
        //Erzeugung von FileChooser
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Wein speichern unter ...");
        
        //ExtensionFilter für verschiedene Dateitypen:
        FileChooser.ExtensionFilter textTyp = new FileChooser.ExtensionFilter("CSV-Dateien", "*.csv");
        
        //Dateityp dem FileChooser hinzufügen.
        fileChooser.getExtensionFilters().add(textTyp);
        
        /*
        "showSaveDialog(...)" öffnet einen Speichern-Dialog, in dem der Nutzer einen Dateinamen
        eingeben oder eine bestehende Datei auswählen kann.
        "fileChooser" gibt ein File-Objekt zurück.
        "file" enthählt (falls der Nutzer eine neue oder bestehende Datei auswählt und auf speichern klickt)
        ein File-Objekt, welches den Pfad zur Datei repräsentiert.
        Wenn auf abbrechen geklickt wurde, enthählt "file" null.
        Den Pfad von "file" kann mit "file.getAbsolutePath()" angzeigt werden.
        */
        
        File file = fileChooser.showSaveDialog(new Stage()); //Zeigt den Speicherdialog an und erzeugt leere Datei zum Speichern des Weinobjektes.
        dateiSpeichern(file);
    }
    
    
    
    public void zeigeSpeichernErfolgreichMeldefenster(){
        Alert alert = new Alert(AlertType.CONFIRMATION, MELDUNG_DATEI_GEPSPEICHERT, ButtonType.OK);
        alert.setTitle("Bestätigung");
        alert.setHeaderText("Gespeichert");
        Optional<ButtonType> antwort = alert.showAndWait();
    }
            
    
    /*
    Überschreibt eine bereits existierende Datei.
    Da FileWriter eine Datei automatisch überschreibt,
    kann hier einfach die Logik von "dateiSpeichern" aufgerufen werden.
    */
    public void dateiÜberschreiben(File file){
        dateiSpeichern(file);
    }
    
    
    
    
    public void dateiSpeichern(File file) {
    // BufferedWriter für performantes Schreiben in die CSV-Datei
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
        
        // CSV-Header schreiben (Spalten = Attribute nach dem Tidy-Data-Prinzip)
        writer.write("Bestell-Nr;Weinname;Erzeuger;Jahrgang;Farbe;Herkunftsland;Anbaugebiet;Alkoholgehalt;Sulfite;Vegan;Geschmack;Parker-Punkte;Lagerauer;Flaschengroesse;Preis pro Flasche (Brutto);Preis pro Flasche (Netto);Preis pro Liter (Brutto);Preis pro Liter (Netto)");
        writer.newLine();

        // Attribute konkateniert mit ";" eines Weinobjektes pro Zeile
        for (Wine wein : Wine.weine) {
            StringBuilder line = new StringBuilder();
            
            line.append(wein.getBestellNr()).append(";");
            line.append(wein.getWeinname()).append(";");
            line.append(wein.getErzeuger()).append(";");
            line.append(wein.getJahrgang()).append(";");
            line.append(wein.getFarbe()).append(";");
            line.append(wein.getHerkunftsland()).append(";");
            line.append(wein.getAnbaugebiet()).append(";");
            line.append(escapePointDecimal(wein.getAlkoholgehalt())).append(";");
            line.append(wein.getSulfite()).append(";");
            line.append(wein.getVegan()).append(";");
            line.append(wein.getGeschmack()).append(";");
            line.append(wein.getParkerPkt()).append(";");
            line.append(wein.getLagerdauer()).append(";");
            line.append(wein.getFlaschengroesse()).append(";");
            line.append(wein.getFlaschenpreisBrutto()).append(";");
            line.append(wein.getFlaschenpreisNetto()).append(";");
            line.append(wein.getLiterpreisBrutto()).append(";");
            line.append(wein.getLiterpreisNetto());
            
            writer.write(line.toString());
            writer.newLine();
        }
        // Nach erfolgreichem Speichern als zuletzt geöffnet setzen
        zuletztGeöffneteDatei = file; // z.B. C:\Users\sfrie\Desktop\Wein_3.csv
        zeigeSpeichernErfolgreichMeldefenster();
    }
    catch(IOException e){
        e.printStackTrace();
        zeigeÜberschreibungsstatusFehlerhaft("Fehler beim Speichern", "Die Datei konnte nicht gespeichert werden.");
    }
}


private String escapePointDecimal(String value) {
    if (value == null) {
        return "";
    }
    if (value.contains(".")) {
        return value.replace(".", ",");
    }
    return value;
}
    
    
    
    private void zeigeÜberschreibungsstatusFehlerhaft(String title, String content) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    
    @FXML
    private void öffnen(ActionEvent event){
        //Erzeigung von FileChooser
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Wein öffnen");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Dateien", "*.csv"));
        File file = fileChooser.showOpenDialog(new Stage()); //Zeigt den Speicherdialog an und erhält durch Anklicken die Datei, die geöffnet werden soll.
        // Wenn der User im FileChooser auf Abbrechen klickt, ist file null. Wenn file null ist, soll Methode öffnen abbrechen.
        if(file == null){
            return;
        }
        
        boolean dateiLeer = istDateiLeer(file);
        
        if(dateiLeer){ //Ist Datei leer
            zeigeDateiLeerMeldefenser();
        }
        else {
            if(!Wine.weine.isEmpty()){
                ButtonType rückgabe = zeigeAnfrageDateiLadenMeldefenser();
                switch(rückgabe.getButtonData()){
                    case YES:
                        // Wenn "Überschreiben" geklickt wurde.
                        interneWeindatenÜberschreiben(file);
                        zuletztGeöffneteDatei = file;
                        break;
                    case NO:
                        // Wenn "Hinzufügen" geklickt wurde.
                        interneWeindatenErgänzen(file);
                        zuletztGeöffneteDatei = file;
                        break;
                    case CANCEL_CLOSE:
                        // Wenn "Abbrechen" geklickt wurde.
                        // kein "return" nötig, weil void-Methode. Methode bricht ab.
                }
                
            }
            else {
                interneWeindatenErgänzen(file);
                zuletztGeöffneteDatei = file;
            }
        }
    }
    

    /*
    Eine Datei ist leer, wenn sie keine Daten oder nur einen Header enthält.
    */
    public boolean istDateiLeer(File file){
        if(file.length() == 0){
            return true;
        }
        try(BufferedReader reader = new BufferedReader(new FileReader(file))){
            String line;
            int anzahlZeilen = 0;
            while((line = reader.readLine()) != null){
                // Leere Zeilen ignorieren
                if(line.isEmpty()){
                    continue;
                }
                anzahlZeilen++;
                // Wenn mehr als eine Zeile, dann ist Datei nicht leer, denn die erste gilt als Header.
                if(anzahlZeilen > 1){
                    return false;
                }
            }
        }
        catch(IOException e){
            e.printStackTrace();
            return true;
        }
        // Wenn Schleife durchläuft und maximal eine Zeile (Header) oder null Zeilen gefunden wurden:
        return true;
    }
    
    
    // Importiert externe Weindaten aus CSV-Datei und überschreibt bereits existierende, interne Weindaten.
    public void interneWeindatenÜberschreiben(File file){
        Wine.weine.clear();
        try(BufferedReader reader = new BufferedReader(new FileReader(file))){
            String line;
            boolean isHeader = true;
            
            while((line = reader.readLine()) != null){ // Solange Zeilen vorhanden sind ...
                if(line.isEmpty()){ // Leere Zeilen überspringen
                    continue;
                }
                if(isHeader){ // Erste Zeile (Header) überspringen.
                    isHeader = false;
                    continue;
                }
                //Zeile nach ";" in Spalten aufteilen.
                // -1 sorgt dafür, dass auch Spalten ohne Wert aufgenommen werden und "" zugeordnet bekommen.
                String[] spalten = line.split(";", -1);
                // CSV-Datei hat genau 18 Spalten. Darum wird geprüft, ob pro Zeile genau 18 Spalten vorhanden sind.
                if(spalten.length == 18){ // 
                    // Jede Zeile als Objekt speichern. Konstruktor speichert Objekt automatisch in Collection Wine.weine
                    new Wine(
                        spalten[0],
                        spalten[1],
                        spalten[2],
                        spalten[3],
                        spalten[4],
                        spalten[5],
                        spalten[6],
                        spalten[7],
                        spalten[8],
                        spalten[9],
                        spalten[10],
                        spalten[11],
                        spalten[12],
                        spalten[13],
                        spalten[14],
                        spalten[15],
                        spalten[16],
                        spalten[17]
                    );
                }
            }
            zeigeInterneWeindatenÜberschriebenErfolg();
        } catch (IOException ex) {
            ex.printStackTrace();
            zeigeImportfehlerMeldefenser();
        }
    }
    
    
    // Importiert externe Weindaten aus CSV-Datei und ergänzt die bereits existerende, interne Weindaten mit ihnen.
    public void interneWeindatenErgänzen(File file){
        try(BufferedReader reader = new BufferedReader(new FileReader(file))){
            String line;
            boolean isHeader = true;
            
            while((line = reader.readLine()) != null){
                if(line.isEmpty()){
                    continue;
                }
                if(isHeader){
                    isHeader = false;
                    continue;
                }
                String[] spalten = line.split(";", -1);
                if(spalten.length == 18){
                    new Wine(
                        spalten[0],
                        spalten[1],
                        spalten[2],
                        spalten[3],
                        spalten[4],
                        spalten[5],
                        spalten[6],
                        spalten[7],
                        spalten[8],
                        spalten[9],
                        spalten[10],
                        spalten[11],
                        spalten[12],
                        spalten[13],
                        spalten[14],
                        spalten[15],
                        spalten[16],
                        spalten[17]
                    );
                }
            }
            zeigeInterneWeindatenErgänzenErfolg();
        } catch (IOException ex) {
            ex.printStackTrace();
            zeigeImportfehlerMeldefenser();
        }
    }
    
    
    public void zeigeDateiLeerMeldefenser(){
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Fehler beim Laden");
        alert.setHeaderText(null);
        alert.setContentText(MELDUNG_DATEI_LEER);
        alert.showAndWait();
    }
    
    
    public ButtonType zeigeAnfrageDateiLadenMeldefenser(){
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Bestätigung");
        alert.setHeaderText("Interne Weindaten bereits vorhanden");
        alert.setContentText(MELDUNG_DATEI_LADEN_ABFRAGE);
        
        ButtonType btnÜberschreiben = new ButtonType("Überschreiben", ButtonBar.ButtonData.YES);
        ButtonType btnHinzufügen = new ButtonType("Hinzufügen", ButtonBar.ButtonData.NO);
        ButtonType btnAbbrechen = new ButtonType("Abbrechen", ButtonBar.ButtonData.CANCEL_CLOSE);
        
        alert.getButtonTypes().setAll(btnÜberschreiben, btnHinzufügen, btnAbbrechen);

        Optional<ButtonType> antwort = alert.showAndWait();
        if(antwort.isPresent()){
            return antwort.get();
        }
        return null;
    }
    
    public void zeigeImportfehlerMeldefenser(){
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Fehler beim Laden");
        alert.setHeaderText(null);
        alert.setContentText(MELDUNG_IMPORT_FEHLER);
        alert.showAndWait();
    }
    
    public void zeigeInterneWeindatenÜberschriebenErfolg(){
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Import abgeschlossen");
        alert.setHeaderText(null);
        alert.setContentText(MELDUNG_ERFOLG_ÜBERSCHREIBUNG_INTERN);
        alert.showAndWait();
    }
    
    public void zeigeInterneWeindatenErgänzenErfolg(){
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Import abgeschlossen");
        alert.setHeaderText(null);
        alert.setContentText(MELDUNG_ERFOLG_ERGÄNZEN_INTERN);
        alert.showAndWait();
    }
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////
    

    @FXML
    private void listeÖffnen(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("WeinlisteView.fxml"));
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.getIcons().add(new Image(getClass().getResourceAsStream(ICON))); //Fenster-Icon anlegen.
        stage.setScene(scene);
        stage.setTitle("Weinliste");
        stage.show();
    }
    
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    
    //Über das MenuItem "Löschen" wird die Methode löschen(ActionEvent event) ausgelöst.
    /*
    Alle Weine, die in der Weinliste markiert bzw. angeklickt wurden,
    werden gelöscht.
    */
    @FXML
    private void löschen(ActionEvent event) {
        if(Wine.weine.isEmpty()){
            zeigeKeineDatenZumLöschenMeldefenster();
        }
        else {
            if(zeigeNachfrageLöschenMeldefenster().equals(ButtonType.YES)){
                WeinlisteController.markierteWeineListe.clear();
                WeinlisteController.weinListe.clear();
                zeilenMitKorrektemFormatListe.clear();
                Wine.weine.clear();
                zuletztGeöffneteDatei = null;
                zeigeBestätigungLöschenMeldefenster();
            }
        }
    }
    
    public void zeigeKeineDatenZumLöschenMeldefenster(){
        Alert alert = new Alert(AlertType.ERROR, MELDUNG_KEINE_DATEN_ZUM_LÖSCHEN, ButtonType.OK);
        alert.setTitle("Fehler");
        alert.setHeaderText("Fehler beim Löschvorgang");
        Optional<ButtonType> antwort = alert.showAndWait();
    }
    
    public ButtonType zeigeNachfrageLöschenMeldefenster(){
        Alert alert = new Alert(AlertType.CONFIRMATION, MELDUNG_WEIN_LÖSCHEN_ANFRAGE, ButtonType.YES, ButtonType.NO);
        alert.setTitle("Bestätigung");
        alert.setHeaderText("Weindaten löschen");

        Optional<ButtonType> antwort = alert.showAndWait();
        if(antwort.isPresent()){
            return antwort.get();
        }
        return null;
    }
    
    public void zeigeBestätigungLöschenMeldefenster(){
        Alert alert = new Alert(AlertType.CONFIRMATION, MELDUNG_WEINDATEN_GELÖSCHT, ButtonType.OK);
        alert.setTitle("Bestätigung");
        alert.setHeaderText("Weindaten gelöscht");
        Optional<ButtonType> antwort = alert.showAndWait();
    }
    
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    
    @FXML
    private void zeigeInfoDialog(ActionEvent event) {
        String javaVersio = System.getProperty("java.version"); //22.0.2
        String javaFXVersio = System.getProperty("javafx.runtime.version"); //22+30
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setHeaderText("Über diese Anwendung");
        alert.setContentText(String.format(MELDUNG_INFO_TEXT, javaVersio, javaFXVersio));
        alert.showAndWait();
    }
    
}
