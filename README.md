# jblockchain
> Einfache Blockchain-Implementierung für Demonstrationszwecke

[![license](https://img.shields.io/badge/license-ASF2-blue.svg)](https://github.com/neozo-software/jblockchain/blob/master/LICENSE)

jblockchain ist ein minimalistisches System um die Prinzipien einer verteilten Blockchain im Detail zu erläutern. Die Implementierung ist keinewegs auf optimale Performanz ausgelegt und verfolgt auch nur einen naiven Ansatz eines verteilten Systems. Im Vordergrund steht ein übersichtlicher Code und nicht alle Randfälle, Raceconditions und Sicherheit abzudecken.

## Starten
Da es sich um eine _Spring Boot_-Applikation handelt, genügt es das Projekt zu klonen und mit _maven_ zu bauen.

```shell
cd jblockchain
./mvnw package
```
Anschließend werden drei Module gebaut:
* __common__: Von den folgenden Modulen gemeinsam genutzter Code
* __node__: Teil des Blockchain-Netzes, der Transaktionen und Blöcke verwaltet
* __client__: Ein Kommandozeilen-Client zur einfachen Kommunikation mit Nodes

Der Node wird einfach durch Ausführung der jar-Datei gestartet und kontaktiert anschließend den konfigurierten Master-Node um alle erforderlichrn Daten herunterzuladen.
```shell
java -jar node/target/node-0.0.1-SNAPSHOT.jar
```

## Interagieren
Nun können einfach die entsprechenden _REST_-Aufrufe getätigt werden um Aktionen auszulösen. Damit die _JSON_-Struktur nicht von Hand aufgebaut werden muss, kann sich mit dem Kommandozeilen-Client beholfen werden.

Zu Beginn kann eine eigene Adresse angelegt werden. Dafür wird ein Private-Public-Keypair benötigt.

```shell
cd client/target
java -jar client-0.0.1-SNAPSHOT.jar --keypair
```
Daraufhin werden die Dateien `key.priv` und `key.pub` erstellt.
Mit dem öffentlichen Schlüssel kann nun die Adresse für Max Mustermann werden. Der kontaktierte Node auf _localhost_ wird die neue Adresse an alle bekannten Nodes übermitteln.

```shell
java -jar client-0.0.1-SNAPSHOT.jar --address --node "http://localhost:8080" --name "Max Mustermann" --publickey key.pub
```

Unter der Resource <http://localhost:8080/address> kann kontrolliert werden, dass die Adresse im System angekommen ist. Für den nächsten Schritt wird dort der Hash der erzeugten Adresse entnommen. Denn um eine Transaktion zu erstellen, muss die Sender-Adresse über den Hash identifiziert werden.

```shell
java -jar client-0.0.1-SNAPSHOT.jar --transaction --node "http://localhost:8080" --sender "Tdz0bKDfca3QjFAe5Ccuj9Noy6ah8n+R8DnZznvjic4=" --message "Hallo Welt" --privatekey key.priv 
```

Die Resource <http://localhost:8080/transaction> listet nun die neue Transaktion mit der Nachricht "Hallo Welt", welche mit dem privaten Schlüssel signiert wurde, im Transaktions-Pool.

## Mining
Damit die Transaktionen im Pool ein Teil der Blockchain werden können, müssen diese in einem Block eingebunden werden. Diese Aufgaube wird kontinuierlich durch die Miner verfolgt.

Um das Mining auf dem lokalen Node anzustoßen, genügt ein Aufruf von <http://localhost:8080/block/start-miner>. Sobald ein valider Block gefunden wird, werden die im Block eingebetteten Transaktionen aus dem Pool entfernt und der Block an die Blockchain angehängt: <http://localhost:8080/block>.

