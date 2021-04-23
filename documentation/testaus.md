### Testausdokumentti

Ohjelman logiikassa on automaattinen yksikkötestaus jokaiselle luokalle Generator-luokkaa lukuunottamatta, mutta sillekin tullaan lisäämään enemmän testejä.

Suurin osa ohjelman testaamisesta on tehty manuaalisesti antamalla vaihtelevasti korkeita syötteitä (esim. kartan leveydeksi ja korkeudeksi asetetaan 1000) ja keskikokoisia syötteitä (500 * 500), ja tarkistamalla, että kartan ulkonäkö on odotusten mukainen. Kartta piirtää tyhjiksi jätetyt ruudut punaisella, joten niiden huomaaminen on helppoa paljain silminkin. 