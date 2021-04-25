import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Polaczenie {
    Punkt punkt_poczatkowy;
    Punkt punkt_koncowy;

    ArrayList<Segment> lista_segmentow;

    public Polaczenie(Punkt punkt_poczatkowy, Punkt punkt_koncowy) {
        this.punkt_poczatkowy = punkt_poczatkowy;
        this.punkt_koncowy = punkt_koncowy;
        this.lista_segmentow = new ArrayList<>();
    }
    //jesli punkt już wylosowany to losuj jescze raz
    public void losuj_liste_segmentow(int szerokosc_plytki, int wysokosc_plytki){
        //int maksymalna_wartosc_x = szerokosc_plytki + 20;
        //int minimalna_wartosc_x = 0 - 20;
        //int maksymalna_wartosc_y = wysokosc_plytki + 20;
        //int minimalna_wartosc_y = 0 - 20;
        int maksymalna_wartosc_x = szerokosc_plytki;
        int minimalna_wartosc_x = 0;
        int maksymalna_wartosc_y = wysokosc_plytki;
        int minimalna_wartosc_y = 0;
        Random random = new Random();
        int kierunek = random.nextInt() % 2; //0 - poziomo, y - pionowo
        String ostatni_kierunek = "";
        String aktualny_kierunek = "pionowy";
        Punkt ostatni_punkt = new Punkt(this.punkt_poczatkowy.X(),this.punkt_poczatkowy.Y());
        if (kierunek == 0){
            aktualny_kierunek = "poziomy";
        }
        while (!ostatni_punkt.equals(this.punkt_koncowy)) {
            if (ostatni_kierunek.equals("poziomy")){
                aktualny_kierunek = "pionowy";
            }
            else if (ostatni_kierunek.equals("pionowy")){
                aktualny_kierunek = "poziomy";
            }
            else{
            }

            int znak_operacji = random.nextInt() % 2; //0 - +, 1 - -
            Punkt nowy_punkt;
            if (aktualny_kierunek.equals("poziomy")){
                int wartosc = ThreadLocalRandom.current().nextInt(1, szerokosc_plytki + 1);
                if (znak_operacji == 0){
                    nowy_punkt = new Punkt((ostatni_punkt.X() + wartosc), ostatni_punkt.Y());
                }
                else{
                    nowy_punkt = new Punkt((ostatni_punkt.X() - wartosc), ostatni_punkt.Y());
                }
                if (nowy_punkt.X() > maksymalna_wartosc_x){
                    nowy_punkt.X(maksymalna_wartosc_x);
                }
                if (nowy_punkt.X() < minimalna_wartosc_x){
                    nowy_punkt.X(minimalna_wartosc_x);
                }

            }
            else{
                int wartosc = ThreadLocalRandom.current().nextInt(1, wysokosc_plytki + 1);
                if (znak_operacji == 0){
                    nowy_punkt = new Punkt(ostatni_punkt.X(), (ostatni_punkt.Y() + wartosc));
                }
                else{
                    nowy_punkt = new Punkt(ostatni_punkt.X(), (ostatni_punkt.Y() - wartosc));
                }
                if (nowy_punkt.Y() > maksymalna_wartosc_y){
                    nowy_punkt.Y(maksymalna_wartosc_y);
                }
                if (nowy_punkt.Y() < minimalna_wartosc_y){
                    nowy_punkt.Y(minimalna_wartosc_y);
                }
            }
            if(!ostatni_punkt.equals(nowy_punkt)){
                Segment nowy_segment = new Segment(new Punkt(ostatni_punkt.X(),ostatni_punkt.Y()), new Punkt(nowy_punkt.X(),nowy_punkt.Y()));
                //System.out.println(nowy_segment);
                this.lista_segmentow.add(nowy_segment);
                ostatni_punkt = new Punkt(nowy_punkt.X(),nowy_punkt.Y());
                ostatni_kierunek = aktualny_kierunek;
            }
        }
    }

    public void losuj_liste_segmentow_poprawnych(int szerokosc_plytki, int wysokosc_plytki){
        int maksymalna_wartosc_x = szerokosc_plytki;
        int minimalna_wartosc_x = 0;
        int maksymalna_wartosc_y = wysokosc_plytki;
        int minimalna_wartosc_y = 0;
        int licznik_bledow = 0;
        Random random = new Random();
        int kierunek = random.nextInt() % 2; //0 - poziomo, y - pionowo
        String ostatni_kierunek = "";
        String aktualny_kierunek = "pionowy";
        Punkt ostatni_punkt = new Punkt(this.punkt_poczatkowy.X(),this.punkt_poczatkowy.Y());
        if (kierunek == 0){
            aktualny_kierunek = "poziomy";
        }
        while (!ostatni_punkt.equals(this.punkt_koncowy)) {
            if (ostatni_kierunek.equals("poziomy")){
                aktualny_kierunek = "pionowy";
            }
            else if (ostatni_kierunek.equals("pionowy")){
                aktualny_kierunek = "poziomy";
            }
            else{
            }

            int znak_operacji = random.nextInt() % 2; //0 - +, 1 - -
            Punkt nowy_punkt;
            if (aktualny_kierunek.equals("poziomy")){
                int wartosc = ThreadLocalRandom.current().nextInt(1, szerokosc_plytki + 1);
                if (znak_operacji == 0){
                    nowy_punkt = new Punkt((ostatni_punkt.X() + wartosc), ostatni_punkt.Y());
                }
                else{
                    nowy_punkt = new Punkt((ostatni_punkt.X() - wartosc), ostatni_punkt.Y());
                }
                if (nowy_punkt.X() > maksymalna_wartosc_x){
                    nowy_punkt.X(maksymalna_wartosc_x);
                }
                if (nowy_punkt.X() < minimalna_wartosc_x){
                    nowy_punkt.X(minimalna_wartosc_x);
                }

            }
            else{
                int wartosc = ThreadLocalRandom.current().nextInt(1, wysokosc_plytki + 1);
                if (znak_operacji == 0){
                    nowy_punkt = new Punkt(ostatni_punkt.X(), (ostatni_punkt.Y() + wartosc));
                }
                else{
                    nowy_punkt = new Punkt(ostatni_punkt.X(), (ostatni_punkt.Y() - wartosc));
                }
                if (nowy_punkt.Y() > maksymalna_wartosc_y){
                    nowy_punkt.Y(maksymalna_wartosc_y);
                }
                if (nowy_punkt.Y() < minimalna_wartosc_y){
                    nowy_punkt.Y(minimalna_wartosc_y);
                }
            }
            if(!ostatni_punkt.equals(nowy_punkt)){
                Segment nowy_segment = new Segment(new Punkt(ostatni_punkt.X(),ostatni_punkt.Y()), nowy_punkt);
                //System.out.println(nowy_segment);
                //sprawdzenie czy nowy segment nie wychodzi poz aplytke i się nie przecina
                boolean nie_przecina=true;
                ArrayList<Punkt> aktualne_punkty_polaczenia = this.zwroc_punkty_polaczenia();
                ArrayList<Punkt> punkty_segmentu = nowy_segment.zwroc_punkty_segmentu();
                punkty_segmentu.remove(0);
                for(int i=0;i<punkty_segmentu.size();i++){
                    if(aktualne_punkty_polaczenia.contains(punkty_segmentu.get(i)) ){
                        nie_przecina=false;
                        //System.out.println("AJJJJ\n");
                        break;
                    }
                }
                if(nie_przecina) {
                    //System.out.println("OOO\n");

                    this.lista_segmentow.add(nowy_segment);
                    ostatni_punkt = new Punkt(nowy_punkt.X(),nowy_punkt.Y());
                    ostatni_kierunek = aktualny_kierunek;
                }
                else{
                    licznik_bledow++;
                    if(licznik_bledow==10){
                        ostatni_punkt = new Punkt(this.punkt_poczatkowy.X(),this.punkt_poczatkowy.Y());
                        this.lista_segmentow.clear();
                        licznik_bledow=0;
                    }
                }
            }
        }
    }

    public int sumaryczna_dlugosc_sciezek(){
        int suma=0;
        for(int i=0;i<this.lista_segmentow.size();i++){
            suma+=lista_segmentow.get(i).wartosc;
        }
        return suma;
    }
    public int sumaryczna_liczba_segmentow(){
        return this.lista_segmentow.size();
    }
    public int liczba_sciezek_poza_plytka(int szerokosc_plytki, int wysokosc_plytki){
        int suma=0;
        for(int i=0;i<this.lista_segmentow.size();i++){
            if(this.lista_segmentow.get(i).czy_segment_poza_plytka(szerokosc_plytki,wysokosc_plytki))
            suma+=1;
        }
        return suma;
    }
    public int sumaryczna_dlugosc_sciezek_poza_plytka(int szerokosc_plytki, int wysokosc_plytki){
        int suma=0;
        for(int i=0;i<this.lista_segmentow.size();i++){
            if(this.lista_segmentow.get(i).czy_segment_poza_plytka(szerokosc_plytki,wysokosc_plytki)) {
                String obecny_kierunek = this.lista_segmentow.get(i).kierunek;
                if(obecny_kierunek.equals("pionowy")){
                    Punkt aktualny_punkt = this.lista_segmentow.get(i).punkt_poczatkowy_segmentu;
                    Punkt koncowy_punkt = this.lista_segmentow.get(i).punkt_koncowy_segmentu;

                    while(!aktualny_punkt.equals(koncowy_punkt)){
                        if(aktualny_punkt.czy_punkt_poza_plytka(szerokosc_plytki,wysokosc_plytki)) suma+=1;

                        if(Math.abs(koncowy_punkt.Y()-(aktualny_punkt.Y()+1)) < Math.abs(koncowy_punkt.Y()-(aktualny_punkt.Y()-1))){
                            aktualny_punkt = new Punkt(aktualny_punkt.X(),aktualny_punkt.Y()+1);
                        }
                        else{
                            aktualny_punkt = new Punkt(aktualny_punkt.X(),aktualny_punkt.Y()-1);
                        }
                    }
                }
                else{
                    Punkt aktualny_punkt = this.lista_segmentow.get(i).punkt_poczatkowy_segmentu;
                    Punkt koncowy_punkt = this.lista_segmentow.get(i).punkt_koncowy_segmentu;

                    while(!aktualny_punkt.equals(koncowy_punkt)){
                        if(aktualny_punkt.czy_punkt_poza_plytka(szerokosc_plytki,wysokosc_plytki)) suma+=1;

                        if(Math.abs(koncowy_punkt.X()-(aktualny_punkt.X()+1)) < Math.abs(koncowy_punkt.X()-(aktualny_punkt.X()-1))){
                            aktualny_punkt = new Punkt(aktualny_punkt.X()+1,aktualny_punkt.Y());
                        }
                        else{
                            aktualny_punkt = new Punkt(aktualny_punkt.X()-1,aktualny_punkt.Y());
                        }
                    }

                }
            }
        }
        return suma;
    }

    //w osobniku kazzda polaczenie i punkty z niej - zaczynamy od pkt startowego, idziemy segmentem i zapisujac powornujemy z tym co mamy
    public ArrayList<Punkt> zwroc_punkty_polaczenia(){
        ArrayList<Punkt> lista_punktow = new ArrayList<>();

        for(int i=0;i<this.lista_segmentow.size();i++){
            String obecny_kierunek = this.lista_segmentow.get(i).kierunek;

            if(obecny_kierunek.equals("pionowy")){
                Punkt aktualny_punkt = new Punkt(this.lista_segmentow.get(i).punkt_poczatkowy_segmentu.X(),this.lista_segmentow.get(i).punkt_poczatkowy_segmentu.Y());
                Punkt koncowy_punkt = new Punkt(this.lista_segmentow.get(i).punkt_koncowy_segmentu.X(),this.lista_segmentow.get(i).punkt_koncowy_segmentu.Y());

                while(!aktualny_punkt.equals(koncowy_punkt)){
                    lista_punktow.add(aktualny_punkt);

                    if(Math.abs(koncowy_punkt.Y()-(aktualny_punkt.Y()+1)) < Math.abs(koncowy_punkt.Y()-(aktualny_punkt.Y()-1))){
                        aktualny_punkt = new Punkt(aktualny_punkt.X(),aktualny_punkt.Y()+1);
                    }
                    else{
                        aktualny_punkt = new Punkt(aktualny_punkt.X(),aktualny_punkt.Y()-1);
                    }
                }
            }
            else{
                Punkt aktualny_punkt = new Punkt(this.lista_segmentow.get(i).punkt_poczatkowy_segmentu.X(),this.lista_segmentow.get(i).punkt_poczatkowy_segmentu.Y());
                Punkt koncowy_punkt = new Punkt(this.lista_segmentow.get(i).punkt_koncowy_segmentu.X(),this.lista_segmentow.get(i).punkt_koncowy_segmentu.Y());

                while(!aktualny_punkt.equals(koncowy_punkt)){
                    lista_punktow.add(aktualny_punkt);

                    if(Math.abs(koncowy_punkt.X()-(aktualny_punkt.X()+1)) < Math.abs(koncowy_punkt.X()-(aktualny_punkt.X()-1))){
                        aktualny_punkt = new Punkt(aktualny_punkt.X()+1,aktualny_punkt.Y());
                    }
                    else{
                        aktualny_punkt = new Punkt(aktualny_punkt.X()-1,aktualny_punkt.Y());
                    }
                }

            }
            if(this.lista_segmentow.size()==1)lista_punktow.add(new Punkt(this.lista_segmentow.get(this.lista_segmentow.size()-1).punkt_koncowy_segmentu.X(), this.lista_segmentow.get(this.lista_segmentow.size()-1).punkt_koncowy_segmentu.Y()));
        }
        if(this.lista_segmentow.size()>1)lista_punktow.add(new Punkt(this.lista_segmentow.get(this.lista_segmentow.size()-1).punkt_koncowy_segmentu.X(), this.lista_segmentow.get(this.lista_segmentow.size()-1).punkt_koncowy_segmentu.Y()));


        return lista_punktow;
    }

    @Override
    public boolean equals(Object obj) {
        ArrayList<Punkt> pkt1=this.zwroc_punkty_polaczenia();
        ArrayList<Punkt> pkt2=((Polaczenie)obj).zwroc_punkty_polaczenia();
        for(int i=0;i<pkt1.size();i++){
            if(!pkt1.get(i).equals( pkt2.get(i))) return false;
        }
        return true;
    }

    @Override
    public String toString() {
        String segmenty="";
        for(int i=0;i<this.lista_segmentow.size();i++){
            segmenty+="\n\t\t\tSegment "+(i+1)+": "+lista_segmentow.get(i);
        }
        return "Punkt Poczatkowy: "+punkt_poczatkowy+"; Punkt Koncowy: "+punkt_koncowy+": "+segmenty;
    }
}
