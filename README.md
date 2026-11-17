# Rush Hour NxM – Java 21 / Maven

Saját tanulási projekt a Programozási technológiák követelményei alapján. A projekt a tanári leírás szerinti kétjátékos, konzolos Rush Hour játékot valósítja meg: A jobbra, B balra távozik. A gép véletlen szabályos lépéseket választ.

## IntelliJ IDEA
1. Csomagold ki a ZIP-et. **File > Open**: válaszd a projekt gyökérmappáját, amelyben a `pom.xml` van.
2. Project SDK: **JDK 21**; a `src/main/java` legyen Sources Root (Maven-import esetén automatikus).
3. Maven: **Reload All Maven Projects**. A `src/main/java/hu/rushhour/app/Main.java` fájlban a `public static void main(String[] args)` mellett megjelenik a zöld futtató háromszög.
4. Terminálban: `mvn clean install`; futtatás: `java -jar target/rush-hour-sajat-1.0.0-jar-with-dependencies.jar`.
5. Ha a zöld nyíl hiányzik, ellenőrizd a JDK 21-et, és nyisd meg a `pom.xml` fájlt Maven-projektként.

## Parancsok
`move C 1`, `move A -1`, `exit A`, `scores`, `save tabla.txt`, `load tabla.txt`, `savexml mentes.xml`, `loadxml mentes.xml`, `help`, `quit`.

A járművek csak a saját tengelyük mentén csúszhatnak. A kijáratot a megfelelő szélen álló A/B jármű `exit` művelete használja. A program induláskor alapértelmezett pályát használ, vagy argumentumként megadható egy TXT-pálya. A játékállás XML-ben is menthető és folytatható. Az eredményeket H2 JDBC adatbázis tárolja.

## Csomagok
- `app`: indítás; `config`: állandók; `model`: tábla és járművek
- `game`: játékmenet és gépi ellenfél; `service`: lépésellenőrzés, megjelenítés
- `io`: TXT és XML; `db`: H2/JDBC; `ui`: konzolsegédek; `event`: Observer-események

## A projekt jelenlegi állapota és ellenőrzése

A Rush Hour projekt Java 21 és Maven használatával készült. A fejlesztés során több hibajavításon és tesztelésen esett át.

**Elvégzett ellenőrzések:**
- A Maven `verify` parancs sikeresen lefutott (`BUILD SUCCESS`).
- A JUnit-tesztek sikeresen teljesültek.
- A JaCoCo által jelzett tesztlefedettség 91%, amely meghaladja a 80%-os minimumkövetelményt.
- A Checkstyle-ellenőrzés sikeresen lefutott.
- A játék konzolos működését, a pályabetöltést, az eredmények tárolását és a mentési funkciókat is kipróbáltuk.

**Fontos tudnivalók:**

A játék alapértelmezett pályával indul. Az XML-alapú mentés kiegészítő funkcióként szerepel a projektben.

A sikeres tesztek és ellenőrzések alátámasztják a program működését, de nem helyettesítik a tanári követelmények teljes körű ellenőrzését és a szóbeli védést.
