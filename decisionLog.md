# Sprint 4

Von: Franzi

TODO:

- Brainstorming Architektur
- WebsocketServer, speichern des Spiels ausprobieren

## UserAuthService

- Anlegen für UserAuthService um mehrfach wiederholten Code zu minimieren
    - kümmert sich um alle Auth-relevanten Dinge, wie z.B. authenticate, getUserFromJwt, generateToken, etc

## UserController

- Auseinanderziehen des UserControllers in mehrere je nach Anwendungszweck
    - UserAuthenticationController für login/register
    - UserHistoryController für UserHistory
    - UserInformationController für das Updaten/Ausgeben der Userinfo
    - UserTestController für alles, was uns beim Testen hilft (kann gegen Ende des Projekts gelöscht werden)
- Idealerweise hat jeder Controller einen separaten Prefix (UserAuthController z.B. user/auth, UserInfoController
  user/info).
    - Da das aber mit den Clients abgesprochen werden muss, ist das noch nicht umgesetzt.

## Game.List<UserScore>

- Game.List<UserScores> sind nun eine eigene Entity (statt Embeddable), weil UserScore.User eine eigene Entity ist und
  Embeddables keine Entities behinhalten dürfen.
    - Lösungsansatz 1: Wir speichern die UserScores als eigene Entities in der DB und holen sie uns für die Berechnung
      der GameHistory, statt des gesamten Game-Objekts. Das würde funktionieren, weil jeder User pro Spiel ohnehin nur
      ein UserScore-Objekt hat. Wir hätten dann allerdings keinen Bezug mehr zum Spiel selbst, es sei denn, wir würden
      in UserScore das Feld "gameId" hinzufügen oder Heiratstabellen erstellen. Den Bezug brauchen wir aber zum heutigen
      Stand ohnehin nicht, womit das "Problem" theoretischer Natur ist.
    - Lösungsansatz 2: Wir speichern statt der User-Objekte nur die Userreferenz (ID oder Usernamen) im UserScore ab,
      damit darf UserScore dann wieder ein Embeddable sein, weil String ein primitiver Datentyp ist. Auch das wäre in
      unserem Fall genug, weil die Usernamen bei uns eindeutig sind und wir die User-Objekte darüber identifizieren und
      vergleichen können. Die Frage ist nur, ob dieser Ansatz logisch so sinnvoll ist, denn es ist eher ein hacky
      Workaround als eine Lösung des Problems.
    - Lösungsansatz 3: ???

# Sprint 3

Von: Franzi

## MessageKeys-Enum

- Achtung: Die Reihenfolge des JSONs (Client -> Server) muss immer wie folgt sein:
    - {actionFlag:$value/null, JWT: $jwt_value}
        - Das JWT wird immer als letztes erwartet, das ActionFlag als erstes

## Client-Ws-Connections auf dem Server speichern

- Der letzte Stand des Servers hat ein Problem. Wir speichern die Websocketconnection im Player-Objekt, um bei
  Änderungen eine Beanrichtigung an beide Clients vornehmen zu können. Das wird allerdings in Zukunft nicht mehr möglich
  sein, denn unser Client ist so aufgesetzt, dass er sich bei einem Connection-Loss selbst neu verbindet - und dann
  dafür den Port wechselt. Aus diesem Grund ist es bad practice, die Clientverbindung "statisch" auf dem Server zu
  speichern.
- Wir können im React Client unsere Websockets nicht auf einen spezifischen Port binden, weil der Browser das unter der
  Haube für uns macht: https://en.wikipedia.org/wiki/Ephemeral_port.
- Ein besserer Ansatz ist es daher, bei jeder neuen Nachricht das Websocket-Objekt der Clients neu zu setzen, wenn es
  sich ändert. Nach diesem Ansatz würde der Server ein Flag empfangen und zusätzlich immer ein JWT, um den Spieler zu
  identifizieren. Durch das Jwt holt er sich den (eindeutigen) Usernamen, sucht in den Matches nach dem Player mit
  diesem Usernamen und updated die Websocketconnection im Objekt, falls sie sich geändert hat.
- In diesem Fall ist es aber best practice, mit heartbeats periodische "Identifikationsnachrichten" vom Client an den
  Server zu schicken, quasi im Ping Pong Prinzip. Falls sich dort die Websocketverbindung ändert, kann man sie gleich im
  Heartbeat im Server setzen.
    - Mit diesem Verfahren ist es aber fast notwendig, auch Datenverluste zu erwarten. Wenn der Server sein Objekt an
      eine Websocketconnection sendet, kann es sein, dass diese Connection zu dem Zeitpunkt (zB wegen
      Client-Internetproblemen) gar nicht mehr existiert. Dann müsste der Server die Nachricht nochmal schicken, sobald
      das nächste Ping vom Client reinkommt. Vielleicht ist es daher schlau, bei jeder Nachricht vom Server->Client
      ein "pong" vom Client zurückzusenden, so nach dem Motto "ich hab deine Nachricht bekommen". Kommt das pong nicht,
      dann weiß der Server "ok, die Nachricht ist nicht angekommen, ich muss das Paket gleich nochmal schicken, wenn der
      nächste Heartbeat vom Client reinkommt!".

## Card

- von boolean `isFlipped` zu FlipStatus-Enum.
    - Erklärung: Dadurch können wir 2 Karten in einem Zwischenstatus behalten bevor wir sie flippen. Wird die erste
      Karte geflippt, geht sie in den "waiting to be flipped"-Status. Beim Flippen der zweiten Karte wird geschaut, ob
      die beiden Karten matchen. Wenn ja, werden beide geflippt. Wenn nein, werden beide "unflipped".

## TODOs

- WebsocketServer und GameHandler auseinanderziehen

<hr>
- dereferenzieren von user aus player und player aus Match fuer Spielende

Verheiraten von User und Spieler:

- Dazu habe ich die onMessage umgebaut
    - Es gibt eine Flagliste, als Attribut vom WebSocketServer, wo die Nachrichtenflags rein kommen
    - Damit haben wir ein Standardisiertes Format nach dem wir die Nachrichten handeln und versenden, dass allen bekannt
      ist
    - Damit koennen wir die message tabelle eventuell auch besser handeln
- jede Nachricht wird nun nach Flags durchsucht
    - Aktuell wird nach 'token gesucht', wenn der String gefunden wird Wird der Token gefiltert, der user des tokens
      wird gesucht und an das Playerobjekt uebergeben anschliessend kommt er in die Queue
    - Der Default ist, dass die Nachricht an alle Subscriber weitergeleitet wird

Refactor:

- Code formatieren
- Doku wo moeglich
- verdeutschifizieren

Bilderupload:

- Das Dynamische laden ueber den imageController geht nun auch, dazu muss im header die ressource angegeben werden,
  allerdinggs ist die Frage ob wir den Imagecontroller ueberhaupt brauchen wenn spring das direkt selber machen kann (
  das hatte uns das laden des Bildes auch uebrigens stark vereinfacht)
  und ob wir die Bilder nicht direkt in den Clients verankern
- ... Das war alles nicht notwendig, weil es bereits internetfacing endpunkte gibt.........
- der Endpunkt steht und liefert erstmal nur das default bild aus, getestet mit insomnia unter unix
    - Das muss noch umgebaut werden
- Macht es wirklich sinn die Bilder von den Kartenset auf dem Server vorzuhalten und von den Clients laden zu lassen?
    - Wir haben die Bilder fuer das Logo etc. ebenfalls auf den clients und nicht auf dem Server
    - Es ist eher eine Art "experiment" um zu testen ob wir die Bilder ueber das jsx/fxml direkt reinbekommen, wenn
      nicht wuerde ich die Bilder des Sets auf dem Client vorhalten, damit sparen wir uns requests, zumal der Server das
      Spielfeld ja propagiert

- Anlegen eines weiteren Conontrollers fuer die Endpunkte
    - Auch wenn es aktuell nur einer ist