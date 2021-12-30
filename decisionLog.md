Bilderupload:
- ich hasse spring... Das war alles nicht notwendig, weil es bereits interfacing endpunkte gibt.........
- der Endpunkt steht und liefert erstmal nur das default bild aus, getestet mit insomnia unter unix
  - Das muss noch umgebaut werden
- Macht es wirklich sinn die Bilder von den Kartenset auf dem Server vorzuhalten und von 
  den Clients laden zu lassen?
  - Wir haben die Bilder fuer das Logo etc. ebenfalls auf den clients und nicht auf dem Server
  - Es ist eher eine Art "experiment" um zu testen ob wir die Bilder ueber das jsx/fxml direkt reinbekommen, wenn nicht wuerde
  ich die Bilder des Sets auf dem Client vorhalten, damit sparen wir uns requests, zumal der Server das Spielfeld ja propagiert
    
- Anlegen eines weiteren Conontrollers fuer die Endpunkte
  - Auch wenn es aktuell nur einer ist