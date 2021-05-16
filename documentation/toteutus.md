### Toteutusdokumentti

#### Ohjelman rakenne
Ohjelman logiikka rakentuu seuraavista luokista:
- Tile
  - Edustaa yksittäistä ruutua kartalla. Omaa jonkin tietyn tyypin Type.
- Type
  - Edustaa maastoa, eli toistaiseksi joko merta, maata tai järveä.
- Map
  - Karttaolio, jonka kaksiulotteinen taulukko säilyttää kartan ruudut.
- TileQueue
  - Jono, joka auttaa ruutujen käsittelyssä. Generaattori käyttää tätä selvittäessään yhtenäiset maastoalueet kartalla.
- Generator
  - Sisältää kaiken logiikan kartan muodostamiseen ja kartan itsensäkin.
  - Muodostaa maa-alueet aloittamalla niiden rakentamisen satunnaisesti valituista pisteistä, joiden lukumäärä on satunnaistettu. Maa-alueen osuus määritellään käyttöliittymässä.
  - Tekee tyhjiksi jääneistä ruuduista vesistöruutuja.
  - Yhtenäistää alueita mukauttamalla liian pieniä alueita vallitseviin ympäröiviin alueisiin. Tämä suoritetaan useasti eri rajoituksilla, jotta lopputulos olisi siisti.
  - Tunnistaa yhtenäiset alueet (järvet, saaret) ja poistaa osan alueista, jotka ovat hyvin pieniä. Todennäköisyys poistaa pienet alueet määritellään käyttöliittymässä.
  - Piirtää järville ja maa-alueille rajat.
  - Luo metsäalueita maa-alueiden sisälle todennäköisyydellä, joka määritellään käyttöliittymässä.
  - Siivoaa metsäalueet mukauttamalla liian yksinäiset ruudut vallitsevaan ympäristöön.

Luokka GUI vastaa graafisen käyttöliittymän muodostamisesta ja kartan piirtämisestä näytölle. Käyttäjä voi määrittää piirrettävän kartan mittasuhteet ja joitakin esiasetuksia, joita Generator käyttää generoidessaan karttaa. Käyttäjä voi myös satunnaistaa kartan.  

Kun n on ruutujen määrä, sekä aika- että tilavaativuudet ovat O(n log n).

#### Puutteet ja parannusideat

Metsän generoiminen maa-alueille ei osaa "lävistää" maa-alueiden rajaruutuja, jotka erittäin kapeilla (kaksi ruutua leveillä) kannaksilla saattavat estää pääsyn kannaksen toiselle puolelle, ja metsä loppuu kuin seinään. Metsä näyttäisi luonnollisemmalta, jos se jatkuisi kannaksen toiselle puolelle rajaruuduista huolimatta. 

Lisäksi metsä näyttäisi realistisemmalta, jos se esiintyisi todennäköisemmin siellä, missä on järviä ja muuta metsää, eikä olisi riippumaton aikaisemmista metsistä. 

#### Lähteet
- [Polygonal Map Generation](http://www-cs-students.stanford.edu/~amitp/game-programming/polygon-map-generation/)
- Tirakirja (jono, syvyyshaku)
