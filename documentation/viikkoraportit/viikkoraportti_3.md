## Viikkoraportti 3

**Työtunteja tällä viikolla:** 20

#### Viikon aikana tehtyä

Uusi versio algoritmista aloitettu ja kehitetty eteenpäin ja uusiin toiminnallisuuksiin tehty testejä. Kokeilin monia eri tapoja toteuttaa algoritmit ennen kuin päädyin nykyisiin versioihin. 

#### Ohjelman edistyminen

Nyt algoritmi valitsee tyhjältä kartalta muutaman satunnaisen pisteen, joista se lähtee rakentamaan saaria asettamalla maata pisteiden viereisille ruuduille. Kartan reunat ovat aina merta. Seuraavaksi saarien reunoja siistitään niin, että ne eivät litisty kartan reunoja vasten niin vahvasti. Sen jälkeen jokainen yksittäinen ruutu laitetaan mukautumaan ympäristöönsä, jos se on ainut laatuaan ympäröivistä ruuduista. Lopulta kaikki tyhjiksi jääneet ruudut muutetaan vesialueiksi.

#### Ongelmia ja epäselvyyksiä

Karttojen piirtäminen on JavaFX:lle vaikeaa ja hidasta, koska jokainen ruutu on yksittäinen Polygon-olio. Oletan Polygonien määrän johtavan hitauteen ja kaatumisiin. Tämä vaikeuttaa testaamista hieman, mutta ei tietenkään liity algoritmiin itseensä mitenkään. Etsin kuitenkin parempia tapoja toteuttaa kartta. Mietin sitäkin, ovatko algoritmin osat liian hitaita jo nyt.

#### Seuraavaksi luvassa

Algoritmi, joka on tietoinen maa-alueista erillisinä alueina ja osaa myös sanoa, onko jokin vesialue kiinni reunassa (merialue) vai maan ympäröimä (järvi). Aion myös parantaa kartan reunojen siistimistä niin, että maa-alueiden reunat lähellä kartan reunoja näyttävät luonnollisemmilta. 