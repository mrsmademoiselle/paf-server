# Grundsätzliches
- Bis zum Spielende wird nichts in der Datenbank gespeichert.
- Die Daten werden über Socketkommunikation zwischen Client und Server hin- und hergeschickt und somit überall aktualisiert, außer in der Datenbank.

Im folgenden wird der ungefähre Ablauf der Kommunikation zwischen den Clients und dem Spieler ingame chronologisch erläutert.

<hr>

<h2 align="center">Server</h1>

1. Matcht 2 Spieler zueinander.
2. erstellt Match-Objekt mit den Spielern. 
    - Karten-Objekte müssen erstellt werden
    - Karten-Objekte müssen mit istAufgedeckt = false initialisiert werden
    - Karten-Objekte müssen dupliziert werden
    - Karten-Objekte benötigen eine Position:
      - Da unsere Spielfeldgröße stattfindet, können wir die Karten einfach von 0-? durchnummerieren. Dafür müssen die Karten aber einmal in der Liste durchmischt werden, weil sie durchs duplizieren vermutlich nicht randomisiert sind (z.B. einmal kurz ins Hashset packen und wieder zurück)
    - (Die Karten bekommen eine Paar-ID)
    - Punktestand wird mit 0:0 initialisiert
    - Der User der am Zug ist ist random, z.B. immer der Spieler an Index 0 (?)
3. sendet Match-Objekt** mit beiden Spielern an beide Clients. (z.B. durch Events). 
    - Die Clients sollten hier bereits eine Websocketverbindung offen haben. Sollte eine Websocketverbindung nicht mehr bestehen, wird das Spiel direkt beendet: Der Gewinner ist der Spieler, der nicht abgebrochen hat.***
4. Das Match-Objekt ist wie folgt aufgebaut:
5. Der Server hält beide Socket-Verbindungen offen und wartet auf Nachrichten von den Clients. 
    - Jede Nachricht die vom Client nun reinkommt wird erst einmal als Kartenaufdeckung behandelt. Mehr dazu im weiteren Ablauf.


<br><br>
<small>** Hier verschicken wir das **ganze** Match-Objekt deswegen, weil wir dann alle relevanten Informationen gebündelt haben. z.B. brauchen wir bei späterer Kartenaufdeckung nicht nur die Karte die aufgedeckt wird, sondern auch andere Informationen zu anderen Karten, und welcher Spieler gerade dran ist. Wenn wir nur die veränderte Information vom Client an den Server, verschicken, wie z.B. die Karten-ID, müssten wir uns für jedes Update das Match neu aus der Datenbank laden. Das kostet sehr viel Rechenzeit.

*Problem bei diesem Ansatz:* Es besteht kein Sicherheitsmechanismus. Der Client könnte das Objekt beliebig manipulieren und mehrere Karten aufdecken, wenn er weiß, wie, weil die Objekte nicht abgeglichen werden können. Um das zu verhindern, müsste es eine teure Datenbankoperation geben, oder aber wir müssten uns einen Zwischenspeicher einrichten, indem das jeweils zuletzt gesendete Objekt gespeichert wird. In jedem Fall ist der Abgleich der Objekte aber teuer, weil wir alle Informationen überprüfen müssen: Nicht nur die Usert, sondern auch die einzelnen Karten mit ihren Feldern (istAufgedeckt, Bildpfad) und wer gerade am Zug ist.

*** das gilt für das gesamte Spiel. Bei jeder Kommunikation zwischen Server & Client muss das geprüft werden. </small>

<hr>

<h2 align="center">Client</h1>

1. Client empfängt das Match-Objekt.
2. Der Client stellt das Spiel mit seinen Informationen grafisch dar. 
    - Für den User, der gerade *nicht* am Zug ist, sind die Spielkarten ausgegraut und haben keinen onclick-Listener (?). 
    - Nur die Karten, die nicht aufgedeckt sind, haben einen onclick-Listener. 
    - Das ganze soll möglichst "bidirektional" * stattfinden, sodass ein Update eines Feldes im Match-Objekt auch ein Update auf der UI bedeutet.
    - da jeder Karte eine Position zugeordnet wurde, können die Karten grafisch sortiert werden (1-?)
3. Der Client wartet auf den Input vom User.
4. Sobald der ziehende User eine Karte anklickt, wird die Information vom Client an den Server weitergeleitet **.
    - Dabei wird lediglich "istAufgedeckt" am Card-Objekt im Match geupdated.
    - Anschließend wird das gesamte, aktualisierte Match-Objekt zurück an den Server gesendet. 

<br><br>

<small>* mir fällt das Wort nicht ein, in diesem Fall ist es nicht wirklich bi, aber ihr versteht, was ich meine :D 

** Hier findet noch keine grafische Darstellung auf der UI statt! Das hat den Hintergrund, dass wir hier die grafische Darstellung abhängig von der Server-Response machen und sie so zeitgleich auf beiden Clients darstellen können. </small>

<hr>

<h2 align="center">Server</h1>

6. Der Server empfängt das Match-Update.
7. Der Server evaluiert, ob es sich um die insgesamt zweite aufgedeckte Karte handelt, oder nicht. 
    - Wenn ja, dann wird außerdem evaluiert, ob die beiden Karten matchen oder nicht. *
8. Der Server sendet das Update des Match-Objektes an beide Clients zurück (Welche Karte aufgedeckt wurde + Info, ob es sich um die 2. Karte handelt oder nicht). **

<br><br>
<small> * Da wir oben festgelegt haben, dass wir das ganze Match-Objekt verschicken, haben wir hier nun alle Karten des Spiels vorliegen. Diese beiden Evaluationen können nun also einfach über eine Iteration aller Karten stattfinden und somit dynamisch inplace geschehen.

** Abhängig von dieser Response machen wir die grafische Darstellung in der UI, um die Clients zu synchronisieren. Siehe oben. </small>

<hr>

<h2 align="center">Client</h1>

5. Der Client empfängt das Update des Match-Objekts vom Server.
6. Der Client deckt die Karte grafisch auf und liest aus der Server-Resposne, ob es sich dabei um die 1. oder die 2. aufgedeckte Karte handelt. 
    - Bei der ersten aufgedeckten Karte wartet er auf weiteren User-Input und macht erstmal nichts. (für zweite Karte, siehe unten)
7. Der User deckt eine zweite Karte auf. 
    - Dabei wird die Information vom Client an den Server weitergeleitet (wie in Client-Schritt Nummer 4).

<hr>

<h2 align="center">Server</h1>

9. Der Server empfängt das Match-Update.
10. Der Server evaluiert wie in Server-Schritt Nummer 7, ob es sich um die zweite Karte handelt, oder nicht. 
    - Es wird außerdem evaluiert, ob die Karten matchen. 
      - MATCH JA? der Server updated außerdem den Punktestand im Match-Objekt. 
          - Der Server evaluiert dann außerdem, ob noch andere Karten zugedeckt sind, oder ob das Spiel jetzt beendet wurde.
          - Wenn das letzte Kartenpaar aufgedeckt wurde, verarbeitet der Server das (finales Update des Match-Objektes?) und schickt eine Nachricht mit einem entsprechenden End-Trigger an den Client. 
          - Der Trigger könnte der Einfachheit halber z.B. ein Feld im Match-Objekt sein "isFinished = true". Oder aber man erstellt ein "EndResultDto" mit den für den Endscreen relevanten Informationen (Punktestände, Spielernamen, ...?).
        - MATCH NEIN? im Match-Objekt wird lediglich geupdated, welcher Spieler gerade am Zug ist (jetzt nämlich der andere).
11. Der Server sendet das Match-Update an beide Clients zurück wie in Server-Schritt Nummer 8.
12. Falls es der letzte Zug war, wird die Websocketkommunikation zu beiden Clients geschlossen.

<hr>

<h2 align="center">Client</h1>

8. Der Client wertet aus, ob das Spiel beendet wurde, oder nicht.
    - Falls das Spiel beendet wurde: 
      - Der Client stellt den entsprechenden Endbildschirm mit Punkestand dar. Die Socket-Kommunikation wird geschlossen.
    - Falls das Spiel noch nicht beendet wurde: 
      - Der Client wertet das Match-Objekt generisch für beide Fälle (egal ob die Karten matchen oder nicht) aus. 
      - Das bedeutet, der Client muss die Felder des Objektes bidirektional (nicht das richtige Wort, siehe oben) binden und sein eigenes Objekt durch das Objekt der letzten Server-Response "ersetzen". 
      Abhängig von diesem Objekt wird dann in der UI dargestellt, welcher Benutzer gerade dran ist und wie der Punktestand steht (und diese Information wird automatisch nach jeder Server-Response erneuert).

<hr>

<h2 align="center">Offene Fragen</h1>

- Was passiert bei Beendigung des Spiels? 
  - z.B. bei Verbindungsabbruch: Wer ist der Gewinner? Gibt es überhaupt einen? Wird bei einem normalen Ende einfach das Match-Objekt in der Datenbank abgespeichert? Zu besprechen
- Woran erkennen wir, ob eine Karte einer anderen matcht? (Ursprünglich angedachte war Hash-Wert, neue Idee: wir vergeben eine "zweite" ID, die für beide Karten gleich sind. Dann hat jede Karte ihre eigene ID, aber noch eine zweite ID, die für jedes "Paar" unique ist und anhand derer man die Paare zueinander zuordnen können. Da wir unsere Karte ohnehin durch duplizieren erstellen, sollte das kein Problem sein)
- TODO: Sicherheitsmechanismus: siehe Kleingedrucktes oben
  - Sicherheitsmechanismus: Es ist kein Abgleich möglich, ob eine bereits aufgedeckte Karte versucht wird aufzudecken. Das hängt mit dem Punkt zusammen. ist aber theoretisch auch keine Anforderung.:-)