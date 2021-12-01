**Server für Patterns und Frameworks**

**Starten**

**Besonderheiten**

**Endpunkte**
Aktuell verwenden wir folgende REST-Endpunkte:
|Endpunkt|Beschreibung|
|-|-|
|/user/login|POST, kein jwt-check, returned jwt-Token für den Client wenn erfolgreich, sonst 401|
|/user/register|POST, kein jwt-check, returned 200 wenn registrierung erfolgreich, 400 wenn Problem|
|||