

Verheiraten von User und Spieler:
- Dazu habe ich die onMessage umgebaut 
  - Es gibt eine Flagliste, als Attribut vom WebSocketServer, wo die Nachrichtenflags rein kommen
  - Damit haben wir ein Standardisiertes Format nach dem wir die Nachrichten
  handeln und versenden, dass allen bekannt ist
  - Damit koennen wir die message tabelle eventuell auch besser handeln
- jede Nachricht wird nun nach Flags durchsucht
  - Aktuell wird nach 'token gesucht', wenn der String gefunden wird
  Wird der Token gefiltert, der user des tokens wird gesucht und an das Playerobjekt uebergeben
  anschliessend kommt er in die Queue
  - Der Default ist, dass die Nachricht an alle Subscriber weitergeleitet wird

Refactor:
- Code formatieren
- Doku wo moeglich
- verdeutschifizieren

Bilderupload:
- Das Dynamische laden ueber den imageController geht nun auch, dazu muss im header die ressource angegeben werden, allerdinggs ist die Frage
  ob wir den Imagecontroller ueberhaupt brauchen wenn spring das direkt selber machen kann (das hatte uns das laden des Bildes auch uebrigens stark vereinfacht)
  und ob wir die Bilder nicht direkt in den Clients verankern
- ... Das war alles nicht notwendig, weil es bereits internetfacing endpunkte gibt.........
- der Endpunkt steht und liefert erstmal nur das default bild aus, getestet mit insomnia unter unix
  - Das muss noch umgebaut werden
- Macht es wirklich sinn die Bilder von den Kartenset auf dem Server vorzuhalten und von 
  den Clients laden zu lassen?
  - Wir haben die Bilder fuer das Logo etc. ebenfalls auf den clients und nicht auf dem Server
  - Es ist eher eine Art "experiment" um zu testen ob wir die Bilder ueber das jsx/fxml direkt reinbekommen, wenn nicht wuerde
  ich die Bilder des Sets auf dem Client vorhalten, damit sparen wir uns requests, zumal der Server das Spielfeld ja propagiert
    
- Anlegen eines weiteren Conontrollers fuer die Endpunkte
  - Auch wenn es aktuell nur einer ist