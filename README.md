# JavaFX GUI für einen fiktiven Weinhändler objektorientiert und nach MVC-Konzept (Model-View-Controller)

## Technische Details
Der Code wurde in JavaFX geschrieben und die Views (fxml-Dateien) im SceneBuilder erstellt. Darüber hinaus gibt es Anbindungen von CSS.

## Funktionsumfang
Durch das Hauptfenster der GUI kann der User sämtliche Funktionalitäten über das Menü steuern. Diese umfassen:
* Importieren von CSV-Datei in internen Weindatenbestand (im Verzeichnis /data befinden sich beispielhafte CSV-Dateien zum Import)
* Exportieren des internen Weindatenbestandes in CSV-Datei
* Starten eines Weineingabe-Nebenfenster zur Erzeugung von Weindaten
* Tabellarische Aufbereitung der internen Weindaten sowie Möglichkeit der Sortierung und Entfernung
Anmerkung: Das Menü enthält noch weitere MenuItems ohne Funktionalitäten. Weitere Spielereien folgen.

![Startbildschirm bzw. Hauptfenster der Anwendung](/docs/Screenshots/Hauptfenster.PNG "Hauptfenster")

Die Weineingabe umfasst einen Dialog zur Eingabe von diversen Weinangaben (pflicht und optional). Dabei wird dem User eine möglichst große Ergonomie ermöglicht durch ...
* Autovervollständigung
* Eingabebeschränkung
* Prompttext
* Tooltip
* Mnemonic
* Visuelle Rückmeldungen bei fehlerhaften Eingaben
* Rückmeldungen in Form von Meldefenstern
Sämtliche Weine werden als Objekte gespeichert und der internen Datenstruktur übergeben.

![Weineingabe-Fenster](/docs/Screenshots/Weineingabe.PNG "Wein anlegen")

Des Weiteren bringt die tabellarische Zusammenfassung einen Überblick über die internen Weindaten. Sie können sortiert oder gelöscht werden.

![Weinliste-Fenster](/docs/Screenshots/Weinliste.PNG "Weine")