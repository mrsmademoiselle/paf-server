# Logtagebuch

## Sprint: ?

Datum: ? <br> Von: ?

--- 

## Sprint: 1

Datum: 11.12.-23.12. <br> Von: Franzi

Ich hab versucht es kurzzuhalten, aber ich wollte die wichtigen Sachen dokumentieren :(

### Package Struktur

Unsere Packagestruktur war generell sehr ungleichmäßig. Zum Einen hatten wir das Problem, dass bei uns alles
"Controller" hieß, und somit das Wort mehrere Bedeutungen hatte. Das habe ich durch Renaming gelöst ("Manager", oder
"Service" - über die Benennung kann man nochmal reden. Es ging mir nur darum, die Mehrdeutigkeit aufzulösen.) Alle
unsere "ComponentController" heißen nun weiterhin "Controller". Alle ComponentController die es benötigen, werden nach
diesem Modell einen ComponentService bekommen (z.B. UserService). Alle Weiteren "Service"-Klassen habe ich in /helper/
gesteckt, z.B. den TokenManager und den HttpConnector. Das sind im Prinzip Basis-Komponenten unseres Systems, die keine
direkten Services bei uns darstellen, aber eine ähnliche Aufgabe übernehmen.

Des Weiteren haben unsere Controller alle möglichen Aufgaben übernommen, die so anfallen - Datenbeschaffung,
Datenverarbeitung, Redirects, Datenausgabe, Styling. Um das Ganze etwas zu abstrahieren und auseinanderzuziehen, habe
ich eine neue Schicht eingeführt, wie wir sie auf dem Server haben - die Services. Diese sind nun größtenteils als
Vermittler zwischen "Repository" (Server) und Controller zuständig und übernehmen somit einen Großteil der
Informationsverarbeitungsaufgaben.

Es ist möglich, dass dadurch das MVC Modell nicht mehr 1:1 bei uns so umgesetzt wird, weil eine höhere Abstraktion bei
uns stattfindet. Andererseits kann man jedoch auch argumentieren, dass das MVC nach wie vor vorhanden sind, weil alle 3
Komponenten noch vorhanden sind und die Aufgaben übernehmen, die ihnen zustehen.

### UserAuthDto

Damit wir nicht zwei UserDtos parallel haben (eins mit Passwort/Username, eins mit Passwort/Username/Profilbild), habe
ich die beiden gemerged.

### general.css

Es gibt eine neue Datei "general.css", in denen alles an Styling rein kann, das auf mehreren Seiten wiederverwendet wird
oder für "Corporal identity" zuständig ist - z.B. Styling von buttons, textfeldern und fonts. Ich glaube, in der letzten
Iteration wurde derselbe Ansatz durch "main.css" verfolgt. Dort habe ich aber nur Code gefunden, der nur in einer
Komponente verwendet wurde (login.fxml) und nicht Code, der für mehrere Komponenten gilt. Vielleicht war das ein Feature
in Arbeit oder so. In jedem Fall ist das jetzt die general.css.

### PapaController

Layout Management ist ein Pain in JavaFX. Dadurch ist es aktuell auch ein Pain, Elemente anzuordnen, weil man alles
Pixelgenau machen muss und in den meisten Fällen dennoch ein Margin/Padding irgendwo hat wo man sich denkt: "Wo kommt
das her?!?!?!"

Das macht es aktuell krebsig, Seiten zu erstellen. Um das etwas leichter zu machen, habe ich mich davon abgewandt,
einfach überall statische Zahlen für das Layout zu verwenden (z.B. prefWidth="1700.00"). Ein Ansatz, das ganze
statischer ohne viel Aufwand statischer zu gestalten ist, dafür Variablen zu entwerfen, die die visualBounds des
Fensters darstellen und im FXML verwendet werden können. Diese Variablen sind im sogenannten "PapaController" (Name to
be fixed) über getter festgelegt. Stand heute extended alle unsere ComponentController diesen PapaController, um die
Variablen verwenden zu können.

Dadurch kann man statt prefWidth="1700.00" einfach prefWidth="${controller.width}" verwenden. Außerdem lassen sich die
Rows (HBoxes) jetzt in gleichmäßige Columns (VBoxes) aufteilen, indem man einfach die Width der "Column Container" auf
${controller.width / anzahlColumns} setzt.

Wir haben also dadurch die Möglichkeit, auch mit JavaFX die Seiten halbwegs responsive darzustellen. Somit bleiben
beispielsweise die Elemente auch beim Resizing da wo sie sind und müssen nicht händisch um x Pixel verschoben werden.

### FileManager

FileManager übernimmt datei-relevante Aufgaben, z.B. das Managen von FileChoosers und das heraussuchen von Bildern als
Ressourcen. Diese Aufgaben wurden in mehreren ComponentControllern verwendet und 1:1 stumpf kopiert, weswegen ich ein
Auslagern sinnvoll fand.


# Sprint: 2

Datum: 27.12.-31.12. <br> Von: Chris

### Websocket

Bisher verwendet der Client eine simple SocketConnection, welche nicht das Websocket-Protokoll implementiert. Daher wäre 
so auch keine Verbindung zum WebSocketServer möglich. Dafür wurde die "org.java-websocket" - Dependency hinzugefügt (also einmal mvn clean install ausführen).
Die Klasse SocketConnector heißt nun WebSocketConnector um das Protokoll zu verdeutlichen.



