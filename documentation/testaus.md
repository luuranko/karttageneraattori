### Testausdokumentti

Ohjelman perustoiminnot on testattu automaattisella yksikkötestauksella, ja esimerkkikarttojen testaamisella.

Suurin osa ohjelman testaamisesta on tehty manuaalisesti antamalla vaihtelevasti korkeita syötteitä (esim. kartan leveydeksi ja korkeudeksi asetetaan 1000) ja keskikokoisia syötteitä (500 * 500), ja tarkistamalla, että kartan ulkonäkö on odotusten mukainen.

Projektin aikana havaittiin, että karttaa generoitaessa erittäin suureksi (yli 1000 * 1000) sen laatu kärsii: erittäin suurissa kartoissa hyvin moni alue on suhteessa kartan kokoon erittäin pieni, ja kartta näyttää tiheältä saaristolta. Algoritmiin kuuluu hyvin pienten alueiden poistaminen, mutta suurissa kartoissa niiden määrä on suurempi kuin pienissä, ja karsiminen auttaa enää hyvin vähän. Tämän vuoksi sovelluksessa rajoitetaan maksimikoko 1000 * 1000 ruutuun. Samoista syistä hyvin pienet kartat ovat liian pelkistettyjä ja tyhjiä, joten minimikoko rajoitettiin 100 * 100 ruutuun.