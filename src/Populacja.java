import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

import static java.util.Collections.min;

public class Populacja {
    ArrayList<Osobnik> populacja;
    String sciezka_do_pliku;

    public Populacja(int rozmiar_populacji, String sciezka_do_pliku) {
        this.sciezka_do_pliku = sciezka_do_pliku;
        this.populacja=new ArrayList<>();
        for(int i=0;i<rozmiar_populacji;i++){
            this.populacja.add(wczytaj_osobnika(sciezka_do_pliku));
        }
    }
    public Populacja() {
        this.populacja=new ArrayList<>();
    }



    public Osobnik selekcja_turniejowa_osobnika(int rozmiar_turnieju){
        Random random = new Random();
        ArrayList<Osobnik> lista_osobnikow_selekcji = new ArrayList<>();
        int i=0;

        ArrayList<Integer> oceny_osobnikow_selekcji = new ArrayList();
        while(i<rozmiar_turnieju){
            int losowy_indeks = ThreadLocalRandom.current().nextInt(0, this.populacja.size());
            if(!lista_osobnikow_selekcji.contains(this.populacja.get(losowy_indeks))){
                lista_osobnikow_selekcji.add(this.populacja.get(losowy_indeks));
                oceny_osobnikow_selekcji.add(this.populacja.get(losowy_indeks).funkcja_oceny());
                i++;
            }
        }
        int indeks_najlepszego = oceny_osobnikow_selekcji.indexOf(min(oceny_osobnikow_selekcji));
        return lista_osobnikow_selekcji.get(indeks_najlepszego);

    }
    public Populacja selekcja_turniejowa_populacji(int rozmiar_turnieju){
        Populacja nowa_populacja = new Populacja();
        for (int i=0; i<this.populacja.size();i++){
            nowa_populacja.populacja.add(this.selekcja_turniejowa_osobnika(rozmiar_turnieju));
        }
        return nowa_populacja;
    }

    public Osobnik selekcja_ruletkowa_osobnika(){
        double suma_ocen_osobnikow=0;
        for (int i=0; i<this.populacja.size();i++){
            suma_ocen_osobnikow+=(1.0/this.populacja.get(i).funkcja_oceny());
        }
        double losowane_prawdopodobienstwo = ThreadLocalRandom.current().nextDouble(0, 1);
        //System.out.println("losowane prawdopodobienstwo: "+losowane_prawdopodobienstwo);
        int i=0;
        double aktualne_prawdopodobienstwo=0.0;
        while(aktualne_prawdopodobienstwo<1.0){
            double aktualna_ocena_osobnika=(1.0/this.populacja.get(i).funkcja_oceny());

            double aktualna_waga_osobnika = aktualna_ocena_osobnika/suma_ocen_osobnikow;
            //System.out.println("awo"+aktualna_waga_osobnika);

            aktualne_prawdopodobienstwo+=aktualna_waga_osobnika;
            //System.out.println("aktualne prawdopodb: "+aktualne_prawdopodobienstwo);
            if(aktualne_prawdopodobienstwo>=losowane_prawdopodobienstwo){
                return this.populacja.get(i);
            }
            i++;
        }
        return this.populacja.get(this.populacja.size()-1);
    }

    public Osobnik krzyzowanie(Osobnik rodzic1, Osobnik rodzic2,double prawdopodobienstwo_krzyzowania){
        double min_prog_krzyzowania = ThreadLocalRandom.current().nextDouble(0, 1);
        Osobnik nowyOsobnik = new Osobnik(rodzic1.szerokosc_plytki,rodzic1.wysokosc_plytki);

        if(min_prog_krzyzowania < prawdopodobienstwo_krzyzowania){
            for(int i=0; i<rodzic1.lista_polaczen.size();i++){
                //przypisywanie referencji
                double losowane_prawdopodobienstwo = ThreadLocalRandom.current().nextDouble(0, 1);
                if(losowane_prawdopodobienstwo>0.5){
                    nowyOsobnik.lista_polaczen.add(rodzic1.lista_polaczen.get(i));
                }
                else{
                    nowyOsobnik.lista_polaczen.add(rodzic2.lista_polaczen.get(i));
                }

            }
        }
        else{
            nowyOsobnik.lista_polaczen.addAll(rodzic1.lista_polaczen);
        }

        return nowyOsobnik;
    }
/*
    public Osobnik mutacja(Osobnik osobnik, double prawdopodobienstwo_mutacji){
        //mamy nowy obiekt osobnika z listą połączeń wskazujący na połączenia swoich rodziców
        //musimy utworzyć nowe połączenia a dla nich nowe segmenty aby nie zmieniać referencji rodziców
        int ilosc_polaczen = osobnik.lista_polaczen.size();
        System.out.println("ILOSC Polaczen osobnika: "+ilosc_polaczen);
        for(int i=0;i<ilosc_polaczen;i++){
            Polaczenie kopia_polaczenia = new Polaczenie(osobnik.lista_polaczen.get(i).punkt_poczatkowy,osobnik.lista_polaczen.get(i).punkt_koncowy);
            ArrayList<Segment> lista_segmentow_dla_kopii = new ArrayList<>();
            //int nr_segmentu=0;
            int ilosc_segmentow = osobnik.lista_polaczen.get(i).lista_segmentow.size();
            System.out.println("ILOSC Segmentow polaczenia"+(i+1)+": "+ilosc_segmentow);
            for(int j=0;j<ilosc_segmentow-1;j++){
                System.out.println("PETLA");
                double min_prog_mutacji = ThreadLocalRandom.current().nextDouble(0, 1);
                if(min_prog_mutacji<prawdopodobienstwo_mutacji){
                    //zachodzi mutacja
                    Punkt punkt_poczatkowy = osobnik.lista_polaczen.get(i).lista_segmentow.get(j).punkt_poczatkowy_segmentu;
                    Punkt punkt_koncowy = osobnik.lista_polaczen.get(i).lista_segmentow.get(j+1).punkt_koncowy_segmentu;

                    int maksymalna_wartosc_x = osobnik.szerokosc_plytki;
                    int minimalna_wartosc_x = 0;
                    int maksymalna_wartosc_y = osobnik.wysokosc_plytki;
                    int minimalna_wartosc_y = 0;
                    int licznik_bledow = 0;
                    Random random = new Random();
                   // int kierunek = random.nextInt() % 2; //0 - poziomo, y - pionowo

                    String ostatni_kierunek = "";
                    String aktualny_kierunek = osobnik.lista_polaczen.get(i).lista_segmentow.get(j).kierunek;
                    Punkt ostatni_punkt = punkt_poczatkowy;
                    /*
                    if (kierunek == 0){
                        aktualny_kierunek = "poziomy";
                    }
                    else{
                        aktualny_kierunek="pionowy";
                    }
                    */
/*
                    while (!ostatni_punkt.equals(punkt_koncowy)) {
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
                            int wartosc = ThreadLocalRandom.current().nextInt(1, osobnik.szerokosc_plytki + 1);
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
                            int wartosc = ThreadLocalRandom.current().nextInt(1, osobnik.wysokosc_plytki + 1);
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
                            Segment nowy_segment = new Segment(ostatni_punkt, nowy_punkt);
                            //System.out.println(nowy_segment);
                            //sprawdzenie czy nowy segment nie wychodzi poz aplytke i się nie przecina
                            boolean nie_przecina=true;

                            ArrayList<Punkt> punkty_segmentu = nowy_segment.zwroc_punkty_segmentu();
                            punkty_segmentu.remove(new Punkt(nowy_segment.punkt_poczatkowy_segmentu.X(),nowy_segment.punkt_poczatkowy_segmentu.Y()));
                            punkty_segmentu.remove(new Punkt(nowy_segment.punkt_koncowy_segmentu.X(),nowy_segment.punkt_koncowy_segmentu.Y()));

                            ArrayList<Punkt> aktualne_punkty_polaczenia = kopia_polaczenia.zwroc_punkty_polaczenia();
                            for(int m=0;m<lista_segmentow_dla_kopii.size();m++){
                                aktualne_punkty_polaczenia.addAll(lista_segmentow_dla_kopii.get(m).zwroc_punkty_segmentu());
                            }
                            //punkty z dalszej częsci
                            ArrayList<Punkt> pozostale_punkty_do_sprawdzenia = new ArrayList<>();
                            for(int l=j+1;l<(osobnik.lista_polaczen.get(i).lista_segmentow.size()-1);l++){
                                pozostale_punkty_do_sprawdzenia.addAll(osobnik.lista_polaczen.get(i).lista_segmentow.get(l).zwroc_punkty_segmentu());
                            }
                            //sprawdzenie
                            for(int k=0;k<punkty_segmentu.size();k++){
                                if(aktualne_punkty_polaczenia.contains(punkty_segmentu.get(k)) || pozostale_punkty_do_sprawdzenia.contains(punkty_segmentu.get(k))){
                                    nie_przecina=false;
                                    break;
                                }
                            }
                            if(nie_przecina) {
                                lista_segmentow_dla_kopii.add(nowy_segment);
                                //nr_segmentu++;
                                ostatni_punkt = nowy_punkt;
                                ostatni_kierunek = aktualny_kierunek;
                            }
                            else{
                                licznik_bledow++;
                                if(licznik_bledow==10){
                                    ostatni_punkt = punkt_poczatkowy;
                                    lista_segmentow_dla_kopii.clear();
                                    licznik_bledow=0;
                                }
                            }
                        }
                    }
                    j++;

                    //koniec mutacji
                }
                else{
                    Segment kopia_segmentu = new Segment(osobnik.lista_polaczen.get(i).lista_segmentow.get(j).punkt_poczatkowy_segmentu,osobnik.lista_polaczen.get(i).lista_segmentow.get(j).punkt_koncowy_segmentu);
                    //osobnik.lista_polaczen.get(i).lista_segmentow.set(nr_segmentu, kopia_segmentu);
                    lista_segmentow_dla_kopii.add(kopia_segmentu);
                }
                //nr_segmentu++;
            }

            if (ilosc_segmentow == 1) {
                Punkt nowy_p_pocz = new Punkt(osobnik.lista_polaczen.get(i).lista_segmentow.get(0).punkt_poczatkowy_segmentu.X(),osobnik.lista_polaczen.get(i).lista_segmentow.get(0).punkt_poczatkowy_segmentu.Y());
                Punkt nowy_p_konc = new Punkt(osobnik.lista_polaczen.get(i).lista_segmentow.get(0).punkt_koncowy_segmentu.X(),osobnik.lista_polaczen.get(i).lista_segmentow.get(0).punkt_koncowy_segmentu.Y());
                Segment kopia_segmentu = new Segment(nowy_p_pocz,nowy_p_konc);
                lista_segmentow_dla_kopii.add(kopia_segmentu);

            }
            else{
                Punkt nowy_p_pocz = new Punkt(lista_segmentow_dla_kopii.get((lista_segmentow_dla_kopii.size()-1)).punkt_koncowy_segmentu.X(),lista_segmentow_dla_kopii.get((lista_segmentow_dla_kopii.size()-1)).punkt_koncowy_segmentu.Y());
                Punkt nowy_p_konc = new Punkt(osobnik.lista_polaczen.get(i).lista_segmentow.get(osobnik.lista_polaczen.get(i).lista_segmentow.size()-1).punkt_koncowy_segmentu.X(),osobnik.lista_polaczen.get(i).lista_segmentow.get(osobnik.lista_polaczen.get(i).lista_segmentow.size()-1).punkt_koncowy_segmentu.Y());
                Segment kopia_segmentu = new Segment(nowy_p_pocz,nowy_p_konc);
                lista_segmentow_dla_kopii.add(kopia_segmentu);
            }

            System.out.println("LISTA SEGMENTOW DLA KOPI: "+lista_segmentow_dla_kopii);
            kopia_polaczenia.lista_segmentow=lista_segmentow_dla_kopii;
            osobnik.lista_polaczen.set(i, kopia_polaczenia);

        }

        return osobnik;
    }
*/
    public Populacja selekcja_ruletkowa_populacji(){
        Populacja nowa_populacja = new Populacja();
        for (int i=0; i<this.populacja.size();i++){
            nowa_populacja.populacja.add(this.selekcja_ruletkowa_osobnika());
        }
        return nowa_populacja;
    }

    private void pokaz_wagi(){
        double suma_ocen_osobnikow=0;
        for (int i=0; i<this.populacja.size();i++){
            suma_ocen_osobnikow+=(1.0/this.populacja.get(i).funkcja_oceny());
        }
        System.out.println("Suma ocen osobnikow: "+suma_ocen_osobnikow);
        for (int i=0; i<this.populacja.size();i++){
            double aktualna_ocena_osobnika=(1.0/this.populacja.get(i).funkcja_oceny());
            double aktualna_waga_osobnika = aktualna_ocena_osobnika/suma_ocen_osobnikow;
            System.out.println("Waga "+i+": "+aktualna_waga_osobnika);
        }

    }


    public void losuj_polaczenia_osobnikow(){
        for(int i = 0; i <this.populacja.size();i++){
            this.populacja.get(i).losuj_polaczenia();
        }
    }
    public void pokaz_ocene_osobnikow(){
        for(int i=0;i<this.populacja.size();i++){
            System.out.println("Osobnik "+(i+1)+": "+ this.populacja.get(i).funkcja_oceny());
        }
    }

    private Osobnik wczytaj_osobnika(String sciezka_do_pliku){
        try {
            File plik_do_odczytu = new File(sciezka_do_pliku);
            Scanner skan = new Scanner(plik_do_odczytu);
            if (skan.hasNextLine()) {
                String data = skan.nextLine();
                String[] wymiary = data.split(";");
                int x= Integer.parseInt(wymiary[0]);
                int y= Integer.parseInt(wymiary[1]);
                Osobnik nowyOsobnik = new Osobnik(x,y);

                while (skan.hasNextLine()) {
                    data = skan.nextLine();
                    String[] punkty = data.split(";");
                    int x1= Integer.parseInt(punkty[0]);
                    int y1= Integer.parseInt(punkty[1]);
                    int x2= Integer.parseInt(punkty[2]);
                    int y2= Integer.parseInt(punkty[3]);
                    nowyOsobnik.dodaj_polaczenie(x1,y1,x2,y2);
                }
                skan.close();
                return nowyOsobnik;
            }
            skan.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public String toString() {
        String osobniki="";
        for(int i=0;i<this.populacja.size();i++){
            osobniki+="\n\t________________________________________\n\tOsobnik "+(i+1)+": "+populacja.get(i);
        }
        return "POPULACJA O ROZMIARZE: "+this.populacja.size()+ osobniki;
    }

    public Osobnik mutacja(Osobnik osobnik, double prawdopodobienstwo_mutacji){
        int maks_liczba_bledow_mutacji=1000;

        int ilosc_polaczen = osobnik.lista_polaczen.size();
        for(int i=0;i<ilosc_polaczen;i++){
            Punkt pkt_pocz_kopii = new Punkt(osobnik.lista_polaczen.get(i).punkt_poczatkowy.X(),osobnik.lista_polaczen.get(i).punkt_poczatkowy.Y());
            Punkt pkt_konc_kopii = new Punkt(osobnik.lista_polaczen.get(i).punkt_koncowy.X(),osobnik.lista_polaczen.get(i).punkt_koncowy.Y());
            Polaczenie kopia_polaczenia = new Polaczenie(pkt_pocz_kopii,pkt_konc_kopii);
            ArrayList<Segment> lista_segmentow_dla_kopii = new ArrayList<>();

            int ilosc_segmentow = osobnik.lista_polaczen.get(i).lista_segmentow.size();
            //System.out.println(ilosc_segmentow);
            //System.out.println(osobnik.lista_polaczen.get(i).lista_segmentow);
            //przypadek, gdy polaczenia ma tylko jeden segment
            if (ilosc_segmentow == 1) {
                //System.out.println("<>Przypadek, gdy polaczenia ma tylko jeden segment - wtedy bez mutacji - przepisujemy");
                Punkt nowy_p_pocz = new Punkt(osobnik.lista_polaczen.get(i).punkt_poczatkowy.X(),osobnik.lista_polaczen.get(i).punkt_poczatkowy.Y());
                Punkt nowy_p_konc = new Punkt(osobnik.lista_polaczen.get(i).lista_segmentow.get(0).punkt_koncowy_segmentu.X(),osobnik.lista_polaczen.get(i).lista_segmentow.get(0).punkt_koncowy_segmentu.Y());
                //Punkt nowy_p_konc = new Punkt(osobnik.lista_polaczen.get(i).punkt_koncowy.X(),osobnik.lista_polaczen.get(i).punkt_koncowy.Y());
                Segment tymczasowa_kopia_segmentu = new Segment(nowy_p_pocz,nowy_p_konc);
                //System.out.println("<>Dodalismy nowy segment dla sytuacji ilosc_segmentow==1: "+tymczasowa_kopia_segmentu);

                //lista_segmentow_dla_kopii.add(tymczasowa_kopia_segmentu);

                //System.out.println("PP: "+nowy_p_pocz);
                //System.out.println("PK:"+nowy_p_konc);
                double min_prog_mutacji = ThreadLocalRandom.current().nextDouble(0, 1);
                //zachodzi mutacja dla segmentu
                if(min_prog_mutacji<prawdopodobienstwo_mutacji) {
                    int licznik_bledow_mutacji = 0;


                    int maksymalna_wartosc_x = osobnik.szerokosc_plytki;
                    int minimalna_wartosc_x = 0;
                    int maksymalna_wartosc_y = osobnik.wysokosc_plytki;
                    int minimalna_wartosc_y = 0;
                    int licznik_bledow = 0;
                    Random random = new Random();

                    String ostatni_kierunek = "";
                    String aktualny_kierunek = tymczasowa_kopia_segmentu.kierunek;
                    Punkt ostatni_punkt = new Punkt(nowy_p_pocz.X(), nowy_p_pocz.Y());

                    while (!ostatni_punkt.equals(nowy_p_konc)) {
                        if (ostatni_kierunek.equals("poziomy")) {
                            aktualny_kierunek = "pionowy";
                        } else if (ostatni_kierunek.equals("pionowy")) {
                            aktualny_kierunek = "poziomy";
                        } else {
                        }

                        int znak_operacji = random.nextInt() % 2; //0 - +, 1 - -
                        Punkt nowy_punkt;
                        if (aktualny_kierunek.equals("poziomy")) {
                            int wartosc = ThreadLocalRandom.current().nextInt(1, osobnik.szerokosc_plytki + 1);
                            if (znak_operacji == 0) {
                                nowy_punkt = new Punkt((ostatni_punkt.X() + wartosc), ostatni_punkt.Y());
                            } else {
                                nowy_punkt = new Punkt((ostatni_punkt.X() - wartosc), ostatni_punkt.Y());
                            }
                            if (nowy_punkt.X() > maksymalna_wartosc_x) {
                                nowy_punkt.X(maksymalna_wartosc_x);
                            }
                            if (nowy_punkt.X() < minimalna_wartosc_x) {
                                nowy_punkt.X(minimalna_wartosc_x);
                            }

                        } else {
                            int wartosc = ThreadLocalRandom.current().nextInt(1, osobnik.wysokosc_plytki + 1);
                            if (znak_operacji == 0) {
                                nowy_punkt = new Punkt(ostatni_punkt.X(), (ostatni_punkt.Y() + wartosc));
                            } else {
                                nowy_punkt = new Punkt(ostatni_punkt.X(), (ostatni_punkt.Y() - wartosc));
                            }
                            if (nowy_punkt.Y() > maksymalna_wartosc_y) {
                                nowy_punkt.Y(maksymalna_wartosc_y);
                            }
                            if (nowy_punkt.Y() < minimalna_wartosc_y) {
                                nowy_punkt.Y(minimalna_wartosc_y);
                            }
                        }
                        if (!ostatni_punkt.equals(nowy_punkt)) {
                            Segment nowy_segment = new Segment(new Punkt(ostatni_punkt.X(), ostatni_punkt.Y()), nowy_punkt);
                            //System.out.println(nowy_segment);

                            //sprawdzenie czy nowy segment nie wychodzi poz aplytke i się nie przecina
                            boolean nie_przecina = true;

                            //punkty z dodawanego segmentu
                            ArrayList<Punkt> punkty_segmentu = nowy_segment.zwroc_punkty_segmentu();
                            punkty_segmentu.remove(new Punkt(nowy_segment.punkt_poczatkowy_segmentu.X(), nowy_segment.punkt_poczatkowy_segmentu.Y()));
                            if (nowy_punkt.equals(nowy_p_konc))
                                punkty_segmentu.remove(new Punkt(nowy_segment.punkt_koncowy_segmentu.X(), nowy_segment.punkt_koncowy_segmentu.Y()));

                            //punkty z dotychczasowych segmentow
                            ArrayList<Punkt> aktualne_punkty_polaczenia = new ArrayList<>();
                            for (int m = 0; m < lista_segmentow_dla_kopii.size(); m++) {
                                aktualne_punkty_polaczenia.addAll(lista_segmentow_dla_kopii.get(m).zwroc_punkty_segmentu());
                            }

                            //sprawdzenie
                            for (int k = 0; k < punkty_segmentu.size(); k++) {
                                if (aktualne_punkty_polaczenia.contains(punkty_segmentu.get(k))) {
                                    nie_przecina = false;
                                    break;
                                }
                            }
                            if (nie_przecina) {
                                //System.out.println("<>Dodalismy nowey segment dla polaczenia" + i + ": " + nowy_segment);
                                lista_segmentow_dla_kopii.add(nowy_segment);
                                ostatni_punkt = nowy_punkt;
                                //ostatni_punkt = new Punkt(nowy_punkt.X(),nowy_punkt.Y());
                                ostatni_kierunek = aktualny_kierunek;
                            } else {
                                licznik_bledow++;
                                licznik_bledow_mutacji++;
                                if (licznik_bledow == 10) {
                                    //ostatni_punkt = new Punkt(punkt_poczatkowy.X(),punkt_poczatkowy.Y());
                                    ostatni_punkt = nowy_p_pocz;
                                    lista_segmentow_dla_kopii = new ArrayList<>();
                                    licznik_bledow = 0;
                                    //System.out.println("<>Nie udalo sie z tym wiec od nowa");
                                }
                                if (licznik_bledow_mutacji == maks_liczba_bledow_mutacji) {
                                    lista_segmentow_dla_kopii = new ArrayList<>();
                                    lista_segmentow_dla_kopii.add(tymczasowa_kopia_segmentu);
                                    ostatni_punkt = nowy_p_konc;
                                }
                            }
                        }

                    }
                }
                //mutacja dla jedynego segmentu polaczenia nie zaszłą
                else{
                    lista_segmentow_dla_kopii = new ArrayList<>();
                    lista_segmentow_dla_kopii.add(tymczasowa_kopia_segmentu);
                }
            //    */

            }
            ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            //mamy wiecej niz jeden segment w polaczenie - petla zadziala
            else{
                //System.out.println("<>Mamy wiecej niz jeden segment w polaczenie - petla zadziala");
                boolean czy_przedostatni_jest_mutowany=false;
                for(int j=0;j<ilosc_segmentow-1;j++){
                    double min_prog_mutacji = ThreadLocalRandom.current().nextDouble(0, 1);
                    //zachodzi mutacja dla segmentu
                    if(min_prog_mutacji<prawdopodobienstwo_mutacji){
                        //zachodzi mutacja
                        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                        if(j==ilosc_segmentow-2) czy_przedostatni_jest_mutowany=true;

                        //Punkt punkt_poczatkowy = new Punkt(osobnik.lista_polaczen.get(i).lista_segmentow.get(j).punkt_poczatkowy_segmentu.X(),osobnik.lista_polaczen.get(i).lista_segmentow.get(j).punkt_poczatkowy_segmentu.Y());
                        Punkt punkt_poczatkowy = new Punkt(osobnik.lista_polaczen.get(i).punkt_poczatkowy.X(),osobnik.lista_polaczen.get(i).punkt_poczatkowy.Y());
                        //Punkt punkt_koncowy = new Punkt(osobnik.lista_polaczen.get(i).lista_segmentow.get(j+1).punkt_koncowy_segmentu.X(),osobnik.lista_polaczen.get(i).lista_segmentow.get(j+1).punkt_koncowy_segmentu.Y());
                        Punkt punkt_koncowy = new Punkt(osobnik.lista_polaczen.get(i).lista_segmentow.get(j+1).punkt_koncowy_segmentu.X(),osobnik.lista_polaczen.get(i).lista_segmentow.get(j+1).punkt_koncowy_segmentu.Y());

                        int maksymalna_wartosc_x = osobnik.szerokosc_plytki;
                        int minimalna_wartosc_x = 0;
                        int maksymalna_wartosc_y = osobnik.wysokosc_plytki;
                        int minimalna_wartosc_y = 0;
                        int licznik_bledow = 0;
                        Random random = new Random();
                        // int kierunek = random.nextInt() % 2; //0 - poziomo, y - pionowo

                        String ostatni_kierunek = "";
                        String aktualny_kierunek = osobnik.lista_polaczen.get(i).lista_segmentow.get(j).kierunek;
                        Punkt ostatni_punkt = new Punkt(punkt_poczatkowy.X(),punkt_poczatkowy.Y());
                    /*
                    if (kierunek == 0){
                        aktualny_kierunek = "poziomy";
                    }
                    else{
                        aktualny_kierunek="pionowy";
                    }
                    */
                        while (!ostatni_punkt.equals(punkt_koncowy)) {
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
                                int wartosc = ThreadLocalRandom.current().nextInt(1, osobnik.szerokosc_plytki + 1);
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
                                int wartosc = ThreadLocalRandom.current().nextInt(1, osobnik.wysokosc_plytki + 1);
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

                                //sprawdzenie czy nowy segment nie wychodzi poz aplytke i siÄ™ nie przecina
                                boolean nie_przecina=true;

                                //punkty z dodawanego segmentu
                                ArrayList<Punkt> punkty_segmentu = nowy_segment.zwroc_punkty_segmentu();
                                punkty_segmentu.remove(new Punkt(nowy_segment.punkt_poczatkowy_segmentu.X(),nowy_segment.punkt_poczatkowy_segmentu.Y()));
                                if(nowy_punkt.equals(punkt_koncowy)) punkty_segmentu.remove(new Punkt(nowy_segment.punkt_koncowy_segmentu.X(),nowy_segment.punkt_koncowy_segmentu.Y()));

                                //punkty z dotychczasowych segmentow
                                ArrayList<Punkt> aktualne_punkty_polaczenia = new ArrayList<>();
                                for(int m=0;m<lista_segmentow_dla_kopii.size();m++){
                                    aktualne_punkty_polaczenia.addAll(lista_segmentow_dla_kopii.get(m).zwroc_punkty_segmentu());
                                }
                                //punkty z dalszej czÄ™sci polaczenia
                                ArrayList<Punkt> pozostale_punkty_do_sprawdzenia = new ArrayList<>();
                                for(int l=j+2;l<(osobnik.lista_polaczen.get(i).lista_segmentow.size());l++){
                                    pozostale_punkty_do_sprawdzenia.addAll(osobnik.lista_polaczen.get(i).lista_segmentow.get(l).zwroc_punkty_segmentu());
                                }
                                //sprawdzenie
                                for(int k=0;k<punkty_segmentu.size();k++){
                                    if(aktualne_punkty_polaczenia.contains(punkty_segmentu.get(k)) || pozostale_punkty_do_sprawdzenia.contains(punkty_segmentu.get(k))){
                                        nie_przecina=false;
                                        break;
                                    }
                                }
                                if(nie_przecina) {
                                    //System.out.println("<>Dodalismy nowey segment dla polaczenia"+i+": "+nowy_segment);
                                    lista_segmentow_dla_kopii.add(nowy_segment);
                                    ostatni_punkt = nowy_punkt;
                                    //ostatni_punkt = new Punkt(nowy_punkt.X(),nowy_punkt.Y());
                                    ostatni_kierunek = aktualny_kierunek;
                                }
                                else{
                                    licznik_bledow++;
                                    if(licznik_bledow==10){
                                        //ostatni_punkt = new Punkt(punkt_poczatkowy.X(),punkt_poczatkowy.Y());
                                        ostatni_punkt = punkt_poczatkowy;
                                        lista_segmentow_dla_kopii = new ArrayList<>();
                                        licznik_bledow=0;
                                        //System.out.println("<>Nie udalo sie z tym wiec od nowa");
                                    }
                                }
                            }
                        }
                        j++;

                        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                        //koniec mutacji
                    }
                    //nie zachodzi mutacja dla segmentu - dodajemy nowy segment bez zmian
                    else{
                        Punkt nowy_p_pocz_w_petli = new Punkt(osobnik.lista_polaczen.get(i).lista_segmentow.get(j).punkt_poczatkowy_segmentu.X(),osobnik.lista_polaczen.get(i).lista_segmentow.get(j).punkt_poczatkowy_segmentu.Y());
                        Punkt nowy_p_konc_w_petli = new Punkt(osobnik.lista_polaczen.get(i).lista_segmentow.get(j).punkt_koncowy_segmentu.X(),osobnik.lista_polaczen.get(i).lista_segmentow.get(j).punkt_koncowy_segmentu.Y());

                        Segment kopia_segmentu = new Segment(nowy_p_pocz_w_petli,nowy_p_konc_w_petli);
                        //System.out.println("<>Nie wypadlo na mutacje i dodajemy segment bez zmain: "+kopia_segmentu);
                        lista_segmentow_dla_kopii.add(kopia_segmentu);
                    }


                }
                //po wyjsciu z petli ,jesli nie mutowalismy przedostatniego segmentu, musimy dodac ostatni segment polaczenia, ktory nigdy nie ulega mutacji w petli
                if(!czy_przedostatni_jest_mutowany){
                    Punkt nowy_p_pocz = new Punkt(lista_segmentow_dla_kopii.get((lista_segmentow_dla_kopii.size()-1)).punkt_koncowy_segmentu.X(),lista_segmentow_dla_kopii.get((lista_segmentow_dla_kopii.size()-1)).punkt_koncowy_segmentu.Y());
                    Punkt nowy_p_konc = new Punkt(osobnik.lista_polaczen.get(i).lista_segmentow.get(osobnik.lista_polaczen.get(i).lista_segmentow.size()-1).punkt_koncowy_segmentu.X(),osobnik.lista_polaczen.get(i).lista_segmentow.get(osobnik.lista_polaczen.get(i).lista_segmentow.size()-1).punkt_koncowy_segmentu.Y());
                    Segment kopia_segmentu = new Segment(nowy_p_pocz,nowy_p_konc);
                    //System.out.println("<>Przeszlismy przez petle, i nie mutowalismy przedosttaniego, wiec dodajemy ostatni segmet: "+kopia_segmentu);
                    lista_segmentow_dla_kopii.add(kopia_segmentu);
                }

            }
            ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


            kopia_polaczenia.lista_segmentow=lista_segmentow_dla_kopii;
            osobnik.lista_polaczen.set(i, kopia_polaczenia);

        }

        return osobnik;
    }


    public Osobnik mutacja_poprawiona(Osobnik osobnik, double prawdopodobienstwo_mutacji){
        int maks_liczba_bledow_mutacji=1000;

        int ilosc_polaczen = osobnik.lista_polaczen.size();
        for(int i=0;i<ilosc_polaczen;i++){
            Punkt pkt_pocz_kopii = new Punkt(osobnik.lista_polaczen.get(i).punkt_poczatkowy.X(),osobnik.lista_polaczen.get(i).punkt_poczatkowy.Y());
            Punkt pkt_konc_kopii = new Punkt(osobnik.lista_polaczen.get(i).punkt_koncowy.X(),osobnik.lista_polaczen.get(i).punkt_koncowy.Y());
            Polaczenie kopia_polaczenia = new Polaczenie(pkt_pocz_kopii,pkt_konc_kopii);
            ArrayList<Segment> lista_segmentow_dla_kopii = new ArrayList<>();

            int ilosc_segmentow = osobnik.lista_polaczen.get(i).lista_segmentow.size();
            //System.out.println(ilosc_segmentow);
            //System.out.println(osobnik.lista_polaczen.get(i).lista_segmentow);
            //przypadek, gdy polaczenia ma tylko jeden segment
            if (ilosc_segmentow == 1) {
                //System.out.println("<>Przypadek, gdy polaczenia ma tylko jeden segment - wtedy bez mutacji - przepisujemy");
                Punkt nowy_p_pocz = new Punkt(osobnik.lista_polaczen.get(i).punkt_poczatkowy.X(),osobnik.lista_polaczen.get(i).punkt_poczatkowy.Y());
                Punkt nowy_p_konc = new Punkt(osobnik.lista_polaczen.get(i).lista_segmentow.get(0).punkt_koncowy_segmentu.X(),osobnik.lista_polaczen.get(i).lista_segmentow.get(0).punkt_koncowy_segmentu.Y());
                //Punkt nowy_p_konc = new Punkt(osobnik.lista_polaczen.get(i).punkt_koncowy.X(),osobnik.lista_polaczen.get(i).punkt_koncowy.Y());
                Segment tymczasowa_kopia_segmentu = new Segment(nowy_p_pocz,nowy_p_konc);
                //System.out.println("<>Dodalismy nowy segment dla sytuacji ilosc_segmentow==1: "+tymczasowa_kopia_segmentu);

                //lista_segmentow_dla_kopii.add(tymczasowa_kopia_segmentu);

                //System.out.println("PP: "+nowy_p_pocz);
                //System.out.println("PK:"+nowy_p_konc);
                double min_prog_mutacji = ThreadLocalRandom.current().nextDouble(0, 1);
                //zachodzi mutacja dla segmentu
                if(min_prog_mutacji<prawdopodobienstwo_mutacji) {
                    int licznik_bledow_mutacji = 0;


                    int maksymalna_wartosc_x = osobnik.szerokosc_plytki;
                    int minimalna_wartosc_x = 0;
                    int maksymalna_wartosc_y = osobnik.wysokosc_plytki;
                    int minimalna_wartosc_y = 0;
                    int licznik_bledow = 0;
                    Random random = new Random();

                    String ostatni_kierunek = "";
                    String aktualny_kierunek = tymczasowa_kopia_segmentu.kierunek;
                    Punkt ostatni_punkt = new Punkt(nowy_p_pocz.X(), nowy_p_pocz.Y());

                    while (!ostatni_punkt.equals(nowy_p_konc)) {
                        if (ostatni_kierunek.equals("poziomy")) {
                            aktualny_kierunek = "pionowy";
                        } else if (ostatni_kierunek.equals("pionowy")) {
                            aktualny_kierunek = "poziomy";
                        } else {
                        }

                        int znak_operacji = random.nextInt() % 2; //0 - +, 1 - -
                        Punkt nowy_punkt;
                        if (aktualny_kierunek.equals("poziomy")) {
                            int wartosc = ThreadLocalRandom.current().nextInt(1, osobnik.szerokosc_plytki + 1);
                            if (znak_operacji == 0) {
                                nowy_punkt = new Punkt((ostatni_punkt.X() + wartosc), ostatni_punkt.Y());
                            } else {
                                nowy_punkt = new Punkt((ostatni_punkt.X() - wartosc), ostatni_punkt.Y());
                            }
                            if (nowy_punkt.X() > maksymalna_wartosc_x) {
                                nowy_punkt.X(maksymalna_wartosc_x);
                            }
                            if (nowy_punkt.X() < minimalna_wartosc_x) {
                                nowy_punkt.X(minimalna_wartosc_x);
                            }

                        } else {
                            int wartosc = ThreadLocalRandom.current().nextInt(1, osobnik.wysokosc_plytki + 1);
                            if (znak_operacji == 0) {
                                nowy_punkt = new Punkt(ostatni_punkt.X(), (ostatni_punkt.Y() + wartosc));
                            } else {
                                nowy_punkt = new Punkt(ostatni_punkt.X(), (ostatni_punkt.Y() - wartosc));
                            }
                            if (nowy_punkt.Y() > maksymalna_wartosc_y) {
                                nowy_punkt.Y(maksymalna_wartosc_y);
                            }
                            if (nowy_punkt.Y() < minimalna_wartosc_y) {
                                nowy_punkt.Y(minimalna_wartosc_y);
                            }
                        }
                        if (!ostatni_punkt.equals(nowy_punkt)) {
                            Segment nowy_segment = new Segment(new Punkt(ostatni_punkt.X(), ostatni_punkt.Y()), nowy_punkt);
                            //System.out.println(nowy_segment);

                            //sprawdzenie czy nowy segment nie wychodzi poz aplytke i się nie przecina
                            boolean nie_przecina = true;

                            //punkty z dodawanego segmentu
                            ArrayList<Punkt> punkty_segmentu = nowy_segment.zwroc_punkty_segmentu();
                            punkty_segmentu.remove(new Punkt(nowy_segment.punkt_poczatkowy_segmentu.X(), nowy_segment.punkt_poczatkowy_segmentu.Y()));
                            if (nowy_punkt.equals(nowy_p_konc))
                                punkty_segmentu.remove(new Punkt(nowy_segment.punkt_koncowy_segmentu.X(), nowy_segment.punkt_koncowy_segmentu.Y()));

                            //punkty z dotychczasowych segmentow
                            ArrayList<Punkt> aktualne_punkty_polaczenia = new ArrayList<>();
                            for (int m = 0; m < lista_segmentow_dla_kopii.size(); m++) {
                                aktualne_punkty_polaczenia.addAll(lista_segmentow_dla_kopii.get(m).zwroc_punkty_segmentu());
                            }

                            //sprawdzenie
                            for (int k = 0; k < punkty_segmentu.size(); k++) {
                                if (aktualne_punkty_polaczenia.contains(punkty_segmentu.get(k))) {
                                    nie_przecina = false;
                                    break;
                                }
                            }
                            if (nie_przecina) {
                                //System.out.println("<>Dodalismy nowey segment dla polaczenia" + i + ": " + nowy_segment);
                                lista_segmentow_dla_kopii.add(nowy_segment);
                                ostatni_punkt = nowy_punkt;
                                //ostatni_punkt = new Punkt(nowy_punkt.X(),nowy_punkt.Y());
                                ostatni_kierunek = aktualny_kierunek;
                            } else {
                                licznik_bledow++;
                                licznik_bledow_mutacji++;
                                if (licznik_bledow == 10) {
                                    //ostatni_punkt = new Punkt(punkt_poczatkowy.X(),punkt_poczatkowy.Y());
                                    ostatni_punkt = nowy_p_pocz;
                                    lista_segmentow_dla_kopii = new ArrayList<>();
                                    licznik_bledow = 0;
                                    //System.out.println("<>Nie udalo sie z tym wiec od nowa");
                                }
                                if (licznik_bledow_mutacji == maks_liczba_bledow_mutacji) {
                                    lista_segmentow_dla_kopii = new ArrayList<>();
                                    lista_segmentow_dla_kopii.add(tymczasowa_kopia_segmentu);
                                    ostatni_punkt = nowy_p_konc;
                                }
                            }
                        }

                    }
                }
                //mutacja dla jedynego segmentu polaczenia nie zaszłą
                else{
                    lista_segmentow_dla_kopii = new ArrayList<>();
                    lista_segmentow_dla_kopii.add(tymczasowa_kopia_segmentu);
                }
                //    */

            }
            ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            //mamy wiecej niz jeden segment w polaczenie - petla zadziala
            else{
                //System.out.println("<>Mamy wiecej niz jeden segment w polaczenie - petla zadziala");
                boolean czy_przedostatni_jest_mutowany=false;
                for(int j=0;j<ilosc_segmentow-1;j++){
                    double min_prog_mutacji = ThreadLocalRandom.current().nextDouble(0, 1);
                    //zachodzi mutacja dla segmentu
                    if(min_prog_mutacji<prawdopodobienstwo_mutacji){
                        //zachodzi mutacja
                        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                        if(j==ilosc_segmentow-2) czy_przedostatni_jest_mutowany=true;

                        ArrayList<Segment> lista_nowo_losowanych_segmentow = new ArrayList<>();
                        boolean czy_anulowac_mutacje = false;

                        Punkt punkt_poczatkowy = new Punkt(osobnik.lista_polaczen.get(i).lista_segmentow.get(j).punkt_poczatkowy_segmentu.X(),osobnik.lista_polaczen.get(i).lista_segmentow.get(j).punkt_poczatkowy_segmentu.Y());
                        Punkt punkt_koncowy = new Punkt(osobnik.lista_polaczen.get(i).lista_segmentow.get(j+1).punkt_koncowy_segmentu.X(),osobnik.lista_polaczen.get(i).lista_segmentow.get(j+1).punkt_koncowy_segmentu.Y());

                        int maksymalna_wartosc_x = osobnik.szerokosc_plytki;
                        int minimalna_wartosc_x = 0;
                        int maksymalna_wartosc_y = osobnik.wysokosc_plytki;
                        int minimalna_wartosc_y = 0;
                        int licznik_bledow = 0;
                        int liczba_wszystkich_bledow=0;
                        Random random = new Random();

                        String ostatni_kierunek = "";
                        String aktualny_kierunek = osobnik.lista_polaczen.get(i).lista_segmentow.get(j).kierunek;
                        Punkt ostatni_punkt = new Punkt(punkt_poczatkowy.X(),punkt_poczatkowy.Y());

                        while (!ostatni_punkt.equals(punkt_koncowy) && !czy_anulowac_mutacje) {
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
                                int wartosc = ThreadLocalRandom.current().nextInt(1, osobnik.szerokosc_plytki + 1);
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
                                int wartosc = ThreadLocalRandom.current().nextInt(1, osobnik.wysokosc_plytki + 1);
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

                                //sprawdzenie czy nowy segment nie wychodzi poz aplytke i siÄ™ nie przecina
                                boolean nie_przecina=true;

                                //punkty z dodawanego segmentu
                                ArrayList<Punkt> punkty_segmentu = nowy_segment.zwroc_punkty_segmentu();
                                punkty_segmentu.remove(new Punkt(nowy_segment.punkt_poczatkowy_segmentu.X(),nowy_segment.punkt_poczatkowy_segmentu.Y()));
                                if(nowy_punkt.equals(punkt_koncowy)) punkty_segmentu.remove(new Punkt(nowy_segment.punkt_koncowy_segmentu.X(),nowy_segment.punkt_koncowy_segmentu.Y()));

                                //punkty z dotychczasowych segmentow
                                ArrayList<Punkt> aktualne_punkty_polaczenia = new ArrayList<>();
                                for(int m=0;m<lista_segmentow_dla_kopii.size();m++){
                                    aktualne_punkty_polaczenia.addAll(lista_segmentow_dla_kopii.get(m).zwroc_punkty_segmentu());
                                }
                                for(int n=0;n<lista_nowo_losowanych_segmentow.size();n++){
                                    aktualne_punkty_polaczenia.addAll(lista_nowo_losowanych_segmentow.get(n).zwroc_punkty_segmentu());
                                }
                                //punkty z dalszej czesci polaczenia
                                ArrayList<Punkt> pozostale_punkty_do_sprawdzenia = new ArrayList<>();
                                for(int l=j+2;l<(osobnik.lista_polaczen.get(i).lista_segmentow.size());l++){
                                    pozostale_punkty_do_sprawdzenia.addAll(osobnik.lista_polaczen.get(i).lista_segmentow.get(l).zwroc_punkty_segmentu());
                                }
                                //sprawdzenie
                                for(int k=0;k<punkty_segmentu.size();k++){
                                    if(aktualne_punkty_polaczenia.contains(punkty_segmentu.get(k)) || pozostale_punkty_do_sprawdzenia.contains(punkty_segmentu.get(k))){
                                        nie_przecina=false;
                                        break;
                                    }
                                }
                                if(nie_przecina) {
                                    //lista_segmentow_dla_kopii.add(nowy_segment);
                                    lista_nowo_losowanych_segmentow.add(nowy_segment);
                                    //ostatni_punkt = nowy_punkt;
                                    ostatni_punkt = new Punkt(nowy_punkt.X(),nowy_punkt.Y());
                                    ostatni_kierunek = aktualny_kierunek;
                                }
                                else{
                                    licznik_bledow++;
                                    liczba_wszystkich_bledow++;
                                    if(licznik_bledow==10){
                                        ostatni_punkt = new Punkt(punkt_poczatkowy.X(),punkt_poczatkowy.Y());
                                        //ostatni_punkt = punkt_poczatkowy;
                                        //lista_segmentow_dla_kopii = new ArrayList<>();
                                        lista_nowo_losowanych_segmentow = new ArrayList<>();
                                        licznik_bledow=0;
                                        //System.out.println("<>Nie udalo sie z tym wiec od nowa");
                                    }
                                    if(liczba_wszystkich_bledow==1000){
                                        czy_anulowac_mutacje=true;
                                        Punkt nowy_p_pocz_w_petli = new Punkt(osobnik.lista_polaczen.get(i).lista_segmentow.get(j).punkt_poczatkowy_segmentu.X(),osobnik.lista_polaczen.get(i).lista_segmentow.get(j).punkt_poczatkowy_segmentu.Y());
                                        Punkt nowy_p_konc_w_petli = new Punkt(osobnik.lista_polaczen.get(i).lista_segmentow.get(j).punkt_koncowy_segmentu.X(),osobnik.lista_polaczen.get(i).lista_segmentow.get(j).punkt_koncowy_segmentu.Y());

                                        Segment kopia_segmentu = new Segment(nowy_p_pocz_w_petli,nowy_p_konc_w_petli);
                                        lista_nowo_losowanych_segmentow = new ArrayList<>();
                                        lista_nowo_losowanych_segmentow.add(kopia_segmentu);
                                        if(j==ilosc_segmentow-2) czy_przedostatni_jest_mutowany=false;
                                        j=j-1;
                                    }
                                }
                            }
                        }
                        lista_segmentow_dla_kopii.addAll(lista_nowo_losowanych_segmentow);

                        j=j+1;

                        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                        //koniec mutacji
                    }
                    //nie zachodzi mutacja dla segmentu - dodajemy nowy segment bez zmian
                    else{
                        Punkt nowy_p_pocz_w_petli = new Punkt(osobnik.lista_polaczen.get(i).lista_segmentow.get(j).punkt_poczatkowy_segmentu.X(),osobnik.lista_polaczen.get(i).lista_segmentow.get(j).punkt_poczatkowy_segmentu.Y());
                        Punkt nowy_p_konc_w_petli = new Punkt(osobnik.lista_polaczen.get(i).lista_segmentow.get(j).punkt_koncowy_segmentu.X(),osobnik.lista_polaczen.get(i).lista_segmentow.get(j).punkt_koncowy_segmentu.Y());

                        Segment kopia_segmentu = new Segment(nowy_p_pocz_w_petli,nowy_p_konc_w_petli);
                        //System.out.println("<>Nie wypadlo na mutacje i dodajemy segment bez zmain: "+kopia_segmentu);
                        lista_segmentow_dla_kopii.add(kopia_segmentu);
                    }


                }
                //po wyjsciu z petli ,jesli nie mutowalismy przedostatniego segmentu, musimy dodac ostatni segment polaczenia, ktory nigdy nie ulega mutacji w petli
                if(!czy_przedostatni_jest_mutowany){
                    Punkt nowy_p_pocz = new Punkt(lista_segmentow_dla_kopii.get((lista_segmentow_dla_kopii.size()-1)).punkt_koncowy_segmentu.X(),lista_segmentow_dla_kopii.get((lista_segmentow_dla_kopii.size()-1)).punkt_koncowy_segmentu.Y());
                    Punkt nowy_p_konc = new Punkt(osobnik.lista_polaczen.get(i).lista_segmentow.get(osobnik.lista_polaczen.get(i).lista_segmentow.size()-1).punkt_koncowy_segmentu.X(),osobnik.lista_polaczen.get(i).lista_segmentow.get(osobnik.lista_polaczen.get(i).lista_segmentow.size()-1).punkt_koncowy_segmentu.Y());
                    Segment kopia_segmentu = new Segment(nowy_p_pocz,nowy_p_konc);
                    //System.out.println("<>Przeszlismy przez petle, i nie mutowalismy przedosttaniego, wiec dodajemy ostatni segmet: "+kopia_segmentu);
                    lista_segmentow_dla_kopii.add(kopia_segmentu);
                }

            }
            ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


            kopia_polaczenia.lista_segmentow=lista_segmentow_dla_kopii;
            osobnik.lista_polaczen.set(i, kopia_polaczenia);

        }

        return osobnik;
    }


}
