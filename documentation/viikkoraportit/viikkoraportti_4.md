## Viikkoraportti 4

**Työtunteja tällä viikolla:** 15

#### Viikon aikana tehtyä

Aloitin muuttamalla kartan visuaalista ilmettä hexagonaalisesta siihen, että jokainen ruutu on pikselin kokoinen. Sen jälkeen lisäsin algoritmiin yhtenäisten alueiden tunnistamisen. Siistin turhaksi jäänyttä koodia pois.

#### Ohjelman edistyminen

Algoritmi tunnistaa nyt yhtenäiset alueet ja voi siten tehdä maan ympäröimistä vesialueista järviä. 

#### Uutta opittua

Opin JavaFX:n visuaalisten työkalujen käyttöä; enimmäkseen PixelWriteria, mutta hieman myös Canvaksen käyttöä. Vaikka alueita löytävä algoritmi on vielä epätäydellinen, koin kehittyneeni sitä tehdessä.

#### Ongelmia ja epäselvyyksiä

Algoritmi, joka käy läpi yhtenäiset alueet kartalla, toimii toistaiseksi rekursiolla. Aion korjata asian pikimmiten, sillä jo 150 * 150 -koon kartalla ohjelma saattaa kaatua pinon täyttymisen vuoksi. Algoritmin tekeminen oli niin haastavaa, että jäi vähän aikaa millekään muulle. Jouduin myös poistamaan kartan rajoja siistivät algoritmit, koska ne eivät enää sopineet uuteen tyyliin. Alueita tunnistavan algoritmin kanssa oli paljon vaikeuksia, ja arvostaisin ehdotuksia tietorakenteisiin sitä varten.

#### Seuraavaksi luvassa

Teen tietorakenteen, joka poistaa rekursion tarpeen alueita tunnistavasta algoritmista. Pohdin tällä hetkellä jonoa, johon läpikäytävät ruudut laitetaan. Kun algoritmia on parannettu, aion käyttää sitä karsimaan ylimääräisiä saaria pois, sillä suurilla kartoilla pientä saaristoa on liikaa. Sitä voisi myös hyödyntää myöhemmin esimerkiksi metsien tai jokien asettamiseen.