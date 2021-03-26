## Viikkoraportti 2

**Työtunteja tällä viikolla:** 16

#### Viikon aikana tehtyä

Aloitin ohjelman peruspalikoiden rakentamisen. Tutustuin JavaFX:n piirtämistoimintoihin, jotta saisin välittömästi visuaalista palautetta ohjelman etenemisestä. Tutustuin Mersenne Twisteriin alustavasti. Käytin paljon aikaa siihen, että yritin neliöiden onnistuttua saada ohjelman piirtämään ruudut kuusikulmioina.
Pohdin mahdollisia tapoja suorittaa algoritmi. En vieläkään tiedä nimeltä sellaisia algoritmeja, joita haluaisin käyttää, mutta inspiroiduin joistakin löytämistäni esimerkeistä.
Tein testejä, otin käyttöön checkstylen, ja kirjoitin ensimmäiset javadoc-kommentit.

#### Ohjelman edistyminen

Ohjelmassa on toiminnallisuuksia kartan ruutujen ja kartan luomiseen ja generoimiseen, sekä graafinen käyttöliittymä, jossa voi käyttäjän syötteen mukaan generoida eri kokoisia karttoja. Generointialgoritmi on toistaiseksi hyvin yksinkertainen ja myös hidas. Toistaiseksi 1000 * 1000 ruudun kartan generoiminen vie monta sekuntia. Graafinen käyttöliittymä on myös erityisen hidas piirtämään karttaa, ja esim. yli kolmensadan ruudun kartat saattavat kaataa ohjelman. Ohjelma käyttää Javan Random-luokkaa satunnaislukujen generoimiseen vielä toistaiseksi.

#### Uutta opittua

Opin uusia tapoja piirtää karttaa JavaFX:llä ja Polygonien käyttöä, ja tutustuin muutamaan uuteen layout-luokkaan. 

#### Ongelmia ja epäselvyyksiä

Pohdin sitä, tarvitseeko minun välttämättä implementoida Mersenne Twister, vai riittäisikö Javan Random-luokka. Mersenne Twister on toistaiseksi vaikea ymmärtää. Jos on Mersenne Twisteriä yksinkertaisempia satunnaislukugeneraattoreita, arvostaisin ehdotuksia. 
Lisäksi minusta tuntuisi rajoittavalta yrittää suorittaa nimenomaan jokin jonkun aiemmin keksimä algoritmi. En koe pystyväni määrittämään algoritmin aikavaatimusta vielä, mutta uskon sen kuitenkin olevan lopulta pienempi kuin O(n)^2.

#### Seuraavaksi luvassa

Algoritmin parantaminen on tärkeysjärjestyksenä ensimmäisenä. Teen algoritmin, joka määrittää rantaviivan tietoisesti. Implementoin jonkin satunnaislukugeneraattorin myöhemmin, jos välttämätöntä.