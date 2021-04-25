import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class Osobnik {
    int szerokosc_plytki;
    int wysokosc_plytki;
    ArrayList<Polaczenie> lista_polaczen;
    int[][] plytka;

    public Osobnik(int szerokosc_plytki, int wysokosc_plytki) {
        this.szerokosc_plytki = szerokosc_plytki;
        this.wysokosc_plytki = wysokosc_plytki;
        this.lista_polaczen = new ArrayList<Polaczenie>();
        this.plytka=new int [wysokosc_plytki][szerokosc_plytki];
    }

    public void dodaj_polaczenie(int x_pocz, int y_pocz, int x_konc, int y_konc){
        Punkt pkt_pocz = new Punkt(x_pocz,y_pocz);
        Punkt pkt_konc = new Punkt(x_konc,y_konc);
        Polaczenie nowe_polaczenie = new Polaczenie(pkt_pocz,pkt_konc);
        this.lista_polaczen.add(nowe_polaczenie);
    }
    public void losuj_polaczenia(){
        for(int i = 0; i <this.lista_polaczen.size();i++){
            this.lista_polaczen.get(i).losuj_liste_segmentow_poprawnych(szerokosc_plytki,wysokosc_plytki);
            //this.lista_polaczen.get(i).losuj_liste_segmentow(szerokosc_plytki,wysokosc_plytki);
        }
    }
    public int liczba_przeciec(){
        ArrayList<Punkt> lista_punktow = new ArrayList<>();
        int liczba_przeciec=0;
        for(int i = 0; i <this.lista_polaczen.size();i++){
            if(this.lista_polaczen.get(i)!=null){
                ArrayList<Punkt> aktualne_punkty = this.lista_polaczen.get(i).zwroc_punkty_polaczenia();
                if (aktualne_punkty != null) {
                    for(int j=0;j<aktualne_punkty.size();j++){
                        if(lista_punktow.contains(aktualne_punkty.get(j))){
                            liczba_przeciec++;
                        }
                        lista_punktow.add(aktualne_punkty.get(j));
                    }
                }
            }
        }
        return liczba_przeciec;
    }

    public int funkcja_oceny(){
        int waga_sumy_liczby_przeciec = 10000;
        int waga_sumy_dlugosci_sciezek=10;
        int waga_sumy_liczby_segmentow=10;
        int waga_liczby_sciezek_poza_plytka=80;
        int waga_sumy_dlugosci_czesci_sciezek_poza_plytka=80;

        int ocena=0;
        for(int i = 0; i <this.lista_polaczen.size();i++){
            ocena+=this.liczba_przeciec()*waga_sumy_liczby_przeciec;
            ocena+=this.lista_polaczen.get(i).sumaryczna_dlugosc_sciezek()*waga_sumy_dlugosci_sciezek;
            ocena+=this.lista_polaczen.get(i).sumaryczna_liczba_segmentow()*waga_sumy_liczby_segmentow;
            ocena+=this.lista_polaczen.get(i).liczba_sciezek_poza_plytka(szerokosc_plytki,wysokosc_plytki)*waga_liczby_sciezek_poza_plytka;
            ocena+=this.lista_polaczen.get(i).sumaryczna_dlugosc_sciezek_poza_plytka(szerokosc_plytki,wysokosc_plytki)*waga_sumy_dlugosci_czesci_sciezek_poza_plytka;
        }
        return ocena;
    }
    /*
    public void wypelnij_plytke(){
        for(int i=0;i<this.lista_polaczen.size();i++){
            ArrayList<Punkt> lista_punktow_polaczenia = this.lista_polaczen.get(i).zwroc_punkty_polaczenia();
            for(int j=0;j<lista_punktow_polaczenia.size();j++){
                this.plytka[lista_punktow_polaczenia.get(j).Y()][lista_punktow_polaczenia.get(j).X()] = i+1;
            }
        }
    }
*/

    public void pokaz_plytke(){
        System.out.println();
        for(int i=0;i<wysokosc_plytki;i++){
            for(int j=0;j<szerokosc_plytki;j++){
                System.out.print(this.plytka[i][j]);
            }
            System.out.println();
        }
    }
/*
    @Override
    public boolean equals(Object obj) {
        for(int i=0;i<this.lista_polaczen.size();i++){
            if(!this.lista_polaczen.get(i).equals( ((Osobnik)obj).lista_polaczen.get(i))) return false;
        }
        return true;
    }
*/
    @Override
    public String toString() {
        String polaczenia="";
        for(int i=0;i<this.lista_polaczen.size();i++){
            polaczenia+="\n\t\tPolaczenie "+(i+1)+": "+lista_polaczen.get(i);
        }
        return "Plytka o rozmiarach: "+szerokosc_plytki+" x "+wysokosc_plytki+polaczenia;
    }

    public void zapisz_do_pliku(String nazwa){
        try (PrintWriter writer = new PrintWriter(new File(nazwa))) {

            StringBuilder sb = new StringBuilder();
            sb.append("Plytka o rozmiarach: ");
            sb.append(szerokosc_plytki);
            sb.append(" x ");
            sb.append(wysokosc_plytki);
            sb.append("\n");

            for(int i=0;i<this.lista_polaczen.size();i++){
                sb.append("Segment ");
                sb.append(i+1);
                sb.append(",\n");
                sb.append("X,Y\n");
                ArrayList<Punkt> punkty_polaczenia = this.lista_polaczen.get(i).zwroc_punkty_polaczenia();
                for(int j=0;j<punkty_polaczenia.size();j++){
                    sb.append(punkty_polaczenia.get(j).X());
                    sb.append(",");
                    sb.append(punkty_polaczenia.get(j).Y());
                    sb.append("\n");
                }

            }
            writer.write(sb.toString());

            System.out.println("Zapisano rozwiązanie!");

        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}
