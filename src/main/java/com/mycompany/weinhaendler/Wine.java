/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.weinhaendler;

import java.util.Set;
import java.util.Objects;

/**
 *
 * @author sfrie
 */
public class Wine {
    
    public String bestellNr, weinname, erzeuger, jahrgang, farbe, herkunftsland, anbaugebiet,
            alkoholgehalt, sulfite, vegan, geschmack, parkerPunkte, lagerdauer,
                  flaschengroesse, flaschenpreisBrutto, flaschenpreisNetto, literpreisBrutto, literpreisNetto;
    
    public static Set<Wine> weine = new ReplacingSet<>();
    
    public Wine(String bestellNr, String weinname, String erzeuger, String jahrgang, String farbe,
            String herkunftsland, String anbaugebiet, String alkoholgehalt, String sulfite,
            String vegan, String geschmack, String parkerPunkte, String lagerdauer,
            String flaschengroesse, String flaschenpreisBrutto, String flaschenpreisNetto, 
            String literpreisBrutto, String literpreisNetto){
        this.bestellNr = bestellNr;
        this.weinname = weinname;
        this.erzeuger = erzeuger;
        this.jahrgang = jahrgang;
        this.farbe = farbe;
        this.herkunftsland = herkunftsland;
        this.anbaugebiet = anbaugebiet;
        this.alkoholgehalt = alkoholgehalt;
        this.sulfite = sulfite;
        this.vegan = vegan;
        this.geschmack = geschmack;
        this.parkerPunkte = parkerPunkte;
        this.lagerdauer = lagerdauer;
        this.flaschengroesse = flaschengroesse;
        this.flaschenpreisBrutto = flaschenpreisBrutto;
        this.flaschenpreisNetto = flaschenpreisNetto;
        this.literpreisBrutto = literpreisBrutto;
        this.literpreisNetto = literpreisNetto;
        weine.add(this);
    }
    
    @Override
    public int hashCode(){
        return Objects.hash(this.bestellNr);
    }
    
    @Override
    public boolean equals(Object obj){
        if(this == obj){
            return true;
        }
        if(obj instanceof Wine){
            Wine wein = (Wine) obj;
            return this.bestellNr.equals(wein.bestellNr);
        }
        return false;
    }

    @Override
    public String toString(){
        return "Bestellnummer: " + this.bestellNr + "; Weinname: " + this.weinname
                + "; Erzeuger: " + this.erzeuger + "; Jahrgang: " + this.jahrgang
                + "; Farbe: " + this.farbe + "; Herkunftsland: " + this.herkunftsland
                + "; Anbaugebiet: " + this.anbaugebiet + "; Alkoholgehalt: " + this.alkoholgehalt + "Vol%"
                + "; Sulfite: " + this.sulfite + "; Vegan: " + this.vegan
                + "; Geschmack: " + this.geschmack + "; Parker-Punkte: " + this.parkerPunkte
                + "; Lagerdauer: " + this.lagerdauer + "; Flaschengröße: " + this.flaschengroesse + "l"
                + "; Flaschenpreis: " + "(" + this.flaschenpreisBrutto + " EUR brutto und " + this.flaschenpreisNetto + " EUR netto)"
                + "; Flaschenpreis: " + "(" + this.literpreisBrutto + " EUR brutto und " + this.literpreisNetto + " EUR netto);";
    }
    
   public String getBestellNr(){
        return this.bestellNr;
    }
   
   public String getWeinname(){
        return this.weinname;
    }
   
   public String getErzeuger(){
        return this.erzeuger;
    }
    
    public String getJahrgang(){
        return this.jahrgang;
    }
    
    public String getFarbe(){
        return this.farbe;
    }
    
    public String getHerkunftsland(){
        return this.herkunftsland;
    }
    
    public String getAnbaugebiet(){
        return this.anbaugebiet;
    }
    
    public String getAlkoholgehalt(){
        return this.alkoholgehalt;
    }
    
    public String getSulfite(){
        return this.sulfite;
    }
    
    public String getVegan(){
        return this.vegan;
    }
    
    public String getGeschmack(){
        return this.geschmack;
    }
    
    public String getParkerPkt(){
        return this.parkerPunkte;
    }
    
    public String getLagerdauer(){
        return this.lagerdauer;
    }
    
    public String getFlaschengroesse(){
        return this.flaschengroesse;
    }
    
    /*
    Das Objektattribut "preisProFlasche" besteht selbst aus zwei Attributen
    "flaschenpreisBrutto" und "flaschenpreisNetto". Diese Methode extrahiert
    den reinen Betrag von "flaschenpreisBrutto".
    "Flaschenpreis: (59,50 EUR brutto und 50 EUR netto)" -> 59,50
    */
    /*public String getFlaschenpreisBrutto(){
        //return this.preisProFlasche.replaceAll(".*?(\\d+\\,?\\d+).*", "$1");
        return this.preisProFlasche.replaceAll(".*?(\\d+(\\,\\d{2})?).*", "$1");
    }*/
    
    public String getFlaschenpreisBrutto(){
        return this.flaschenpreisBrutto;
    }
    
    public String getFlaschenpreisNetto(){
        return this.flaschenpreisNetto;
    }
    
    public String getLiterpreisBrutto(){
        return this.literpreisBrutto;
    }
    
    public String getLiterpreisNetto(){
        return this.literpreisNetto;
    }
    
}
