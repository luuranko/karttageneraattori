## Käyttöohje

### Käynnistys

Releasen jar-tiedostosta: komentorivillä komento `java -jar karttageneraattori.jar`

Kloonattuna: juurihakemistossa komento `gradle run`, tai `gradle shadowJar` ja sen jälkeen `java -jar app/build/libs/app-all.jar`.

### Ohjelman käyttö

Ohjelmaikkunan vasemmassa laidassa ovat kontrollit, joiden avulla voidaan antaa asetuksia kartoille ja generoida niitä.

Ylimpänä on Zoom level -slider ja Refresh-nappi. Muuttamalla arvoa ja sitten painamalla nappia voi muuttaa kartan visuaalista kokoa: arvolla 1 jokainen kartan ruutu on yhden pikselin kokoinen, arvolla 5 ruudun koko on 5 * 5 pikseliä. Refresh-napin painaminen vain piirtää uudelleen jo olemassa olevan kartan, eikä generoi uutta.

Alempana olevat arvot ovat pakollisia uusien karttojen generoimiseen. Map Width viittaa luotavan kartan leveyteen ruuduissa, ja Map Height samoin korkeuden osalta. % of land -slider määrittää alustavan maa-alueen osuuden kartan koko pinta-alasta (järviä ei lasketa maa-alueeksi), mutta lopullisessa kartassa tämä on vain suuntaa-antava arvo. Forestation- ja Small islands and lakes -valikoista tulee valita toivottu valinta generoimista varten. Forestation-valikko määrittelee todennäköisyyden metsän generoimiselle, mutta ei takaa tiettyä metsämäärää kartalla. Small islands and lakes -valikko samoin määrittelee todennäköisyyden sille, että kartalla on hyvin pientä saaristoa ja pieniä järviä.

Generate-nappia painamalla kun kaikki valinnat on tehty voidaan luoda uusi kartta haluttujen asetusten pohjalta. 

Random-nappi luo uuden kartan satunnaistaen kenttien arvot.

Generate- tai Random-napin painaminen silloin, kun Zoom level -arvoa on muutettu, piirtää uuden kartan uudella arvolla.
