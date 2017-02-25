# jblockchain
> Einfache Blockchain-Implementierung für Demonstrationszwecke

[![license](https://img.shields.io/badge/license-Apache-blue.svg)](https://github.com/neozo-software/jblockchain/blob/master/LICENSE)

jblockchain ist ein minimalistisches System um die Prinzipien einer verteilten Blockchain im Detail zu erläutern. Die Implementierung ist keinewegs auf optimale Performanz ausgelegt und verfolgt auch nur einen naiven Ansatz eines verteilten Systems. Im Vordergrund steht ein übersichtlicher Code und nicht alle Randfälle und Raceconditions abzudecken.

## Ausprobieren
Da es sich um eine _Spring Boot_-Applikation handelt, genügt es das Projekt zu klonen und mit _maven_ zu bauen.

```shell
cd jblockchain
./mvnw package
```
Anschließend werden drei Module gebaut:
* __common__: Von den folgenden Modulen gemeinsam genutzter Code
* __node__: Teil des Blockchain-Netzes, der Transaktion und Blöcke verwaltet
* __client__: Ein Kommandozeilen-Client zur einfachen Kommunikation mit Nodes

Der Node wird einfach durch Ausführung der jar-Datei gestartet und kontaktiert anschließend den konfigurierten Master-Node um alle erforderlichrn Daten herunterzuladen.
```shell
java -jar node/target/node-0.0.1-SNAPSHOT.jar
```
