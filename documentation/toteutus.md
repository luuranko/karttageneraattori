### Toteutusdokumentti

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
  - Satunnaistaa maan osuuden suhteessa veteen ja muodostaa maa-alueet aloittamalla niiden rakentamisen satunnaisesti valituista muutamasta pisteestä.
  - Selvittää yhtenäiset alueet (järvet, saaret) ja poistaa osan alueista, jotka ovat hyvin pieniä.

Luokka GUI vastaa graafisen käyttöliittymän muodostamisesta ja kartan piirtämisestä näytölle. Jokainen kartan ruutu on yksi pikseli, ja eri maastotyypit piirretään eri väreillä. Käyttäjä voi määrittää piirrettävän kartan mittasuhteet. Generator-luokkaa kutsutaan karttaa luodessa.

Kun n on ruutujen määrä, sekä aika- että tilavaativuudet ovat O(n log n).

Lähteet:
- [Polygonal Map Generation](http://www-cs-students.stanford.edu/~amitp/game-programming/polygon-map-generation/)
- Tirakirja (jono, syvyyshaku)
