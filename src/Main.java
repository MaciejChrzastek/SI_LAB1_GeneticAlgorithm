import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class Main {

    public static Osobnik znajdz_najlepsze_rozwiazanie(int rozmiar_populacji, String sciezka_do_pliku, int liczba_pokolen, String rodzaj_selekcji,int rozmiar_turnieju, double prawdopodobienstwo_krzyzowania,double prawdopodobienstwo_mutacji){
        Populacja aktualna_populacja = new Populacja(rozmiar_populacji,sciezka_do_pliku);
        aktualna_populacja.losuj_polaczenia_osobnikow();
        System.out.println(aktualna_populacja);

        Osobnik najlepszy_osobnik=null;
        ///////////////////////////////////////
        //ArrayList<Integer> oceny_najlepszych_z_pokolen = new ArrayList<>();
        //////////////////////////////////////////
        for(int i=0;i<liczba_pokolen;i++){
            ///////////////////////////////////////////////
            //Osobnik najlepszy_osobnik_z_pokolenia=null;
            //////////////////////////////////////////////
            System.out.println("Populacja "+(i+1)+": \n");
            System.out.println("________________________________________");
            Populacja nowa_populacja = new Populacja(aktualna_populacja.populacja.size(),aktualna_populacja.sciezka_do_pliku);
            for(int j=0;j<aktualna_populacja.populacja.size();j++){
                if(rodzaj_selekcji.equals("turniejowa")){
                    System.out.println("Wybrano selekcję turniejową: \n");
                    Osobnik rodzic1 = aktualna_populacja.selekcja_turniejowa_osobnika(rozmiar_turnieju);
                    System.out.println("Wybrano rodzica 1: \n");
                    System.out.println(rodzic1);
                    Osobnik rodzic2 = aktualna_populacja.selekcja_turniejowa_osobnika(rozmiar_turnieju);
                    System.out.println("Wybrano rodzica 2: \n");
                    System.out.println(rodzic2);
                    Osobnik potomek = aktualna_populacja.krzyzowanie(rodzic1,rodzic2,prawdopodobienstwo_krzyzowania);
                    System.out.println("Dokonano krzyzowania potomka: \n");
                    System.out.println(potomek);

                    potomek=aktualna_populacja.mutacja_poprawiona(potomek,prawdopodobienstwo_mutacji);
                    System.out.println("Dokonano mutacji potomka: \n");
                    System.out.println(potomek);
                    nowa_populacja.populacja.set(j,potomek);
                    if(najlepszy_osobnik==null || najlepszy_osobnik.funkcja_oceny()>potomek.funkcja_oceny()){
                        najlepszy_osobnik=potomek;
                    }
                    ///////////////////////////////////////////////
                    //if(najlepszy_osobnik_z_pokolenia==null || najlepszy_osobnik_z_pokolenia.funkcja_oceny()>potomek.funkcja_oceny()){
                    //    najlepszy_osobnik=potomek;
                    //    najlepszy_osobnik_z_pokolenia=potomek;
                    //}
                    ///////////////////////////////////////////////

                }
                else {
                    System.out.println("Wybrano selekcję ruletkową: \n");
                    Osobnik rodzic1 = aktualna_populacja.selekcja_ruletkowa_osobnika();
                    System.out.println("Wybrano rodzica 1: \n");
                    System.out.println(rodzic1);
                    Osobnik rodzic2 = aktualna_populacja.selekcja_ruletkowa_osobnika();
                    System.out.println("Wybrano rodzica 2: \n");
                    System.out.println(rodzic2);
                    Osobnik potomek = aktualna_populacja.krzyzowanie(rodzic1,rodzic2,prawdopodobienstwo_krzyzowania);
                    System.out.println("Dokonano krzyzowania potomka: \n");
                    System.out.println(potomek);

                    potomek=aktualna_populacja.mutacja_poprawiona(potomek,prawdopodobienstwo_mutacji);
                    System.out.println("Dokonano mutacji potomka: \n");
                    System.out.println(potomek);
                    nowa_populacja.populacja.set(j,potomek);
                    if(najlepszy_osobnik==null || najlepszy_osobnik.funkcja_oceny()>potomek.funkcja_oceny()){
                        najlepszy_osobnik=potomek;
                    }
                    ///////////////////////////////////////////////
                    //if(najlepszy_osobnik_z_pokolenia==null || najlepszy_osobnik_z_pokolenia.funkcja_oceny()>potomek.funkcja_oceny()){
                    //    najlepszy_osobnik=potomek;
                    //    najlepszy_osobnik_z_pokolenia=potomek;
                    //}
                    ///////////////////////////////////////////////

                }
                System.out.println("________________________________________");

            }
            ///////////////////////////////////////////////
            //oceny_najlepszych_z_pokolen.add(najlepszy_osobnik_z_pokolenia.funkcja_oceny());
            ///////////////////////////////////////////////
            aktualna_populacja = nowa_populacja;
        }
        //////////////////////////////////
        //zapisz_wyniki_populacji_do_pliku("wyniki.csv", oceny_najlepszych_z_pokolen);
        //////////////////////////////////
        return najlepszy_osobnik;
    }

    public static void main(String [] args) {
        /*
        Osobnik rozwiazanie = znajdz_najlepsze_rozwiazanie(20,"C:\\Users\\Maciej Chrząstek\\Desktop\\lab1_problemy_testowe\\zad0.txt",10,"ruletkowa",3,0.7,0.5);
        System.out.println("NAJLEPSZE ROZWIAZANIE:");
        System.out.println("________________________________________");
        System.out.println(rozwiazanie);
        System.out.println("________________________________________");
        //rozwiazanie.zapisz_do_pliku("rozwiazanie.csv");
        */
        ///*
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //ZADANIE 0 - ZBADANIE WPŁYWU PARAMETRÓW:

        //------ROZMIAR POPULACJI
        String zad0="";
        String podstawowy_przyklad = zwroc_instancje(20,"C:\\Users\\Maciej Chrząstek\\Desktop\\lab1_problemy_testowe\\zad0.txt",10,"turniejowa",3,0.7,0.002);

        // ROZMIAR POPULACJI: 10, LICZBA POKOLEN: 10, RODZAJ SELEKCJI: TURNIEJOWA, ROZMIAR TURNIEJU:3, PRAWDOPODOBIENSTWO KRZYZOWANIA 0.7, PRAWDOPODOBIENSTWO MUTACJI 0.002
        zad0 += zwroc_instancje(10,"C:\\Users\\Maciej Chrząstek\\Desktop\\lab1_problemy_testowe\\zad0.txt",10,"turniejowa",3,0.7,0.002);
        // ROZMIAR POPULACJI: 20, LICZBA POKOLEN: 10, RODZAJ SELEKCJI: TURNIEJOWA, ROZMIAR TURNIEJU:3, PRAWDOPODOBIENSTWO KRZYZOWANIA 0.7, PRAWDOPODOBIENSTWO MUTACJI 0.002
        zad0 +="\n" + podstawowy_przyklad;
        // ROZMIAR POPULACJI: 40, LICZBA POKOLEN: 10, RODZAJ SELEKCJI: TURNIEJOWA, ROZMIAR TURNIEJU:3, PRAWDOPODOBIENSTWO KRZYZOWANIA 0.7, PRAWDOPODOBIENSTWO MUTACJI 0.002
        zad0 +="\n" + zwroc_instancje(40,"C:\\Users\\Maciej Chrząstek\\Desktop\\lab1_problemy_testowe\\zad0.txt",10,"turniejowa",3,0.7,0.002);
        zapisz_wyniki_populacji_do_pliku("zad0_rozmiar_populacji.csv", zad0);

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //------LICZBA POKOLEN
        // ROZMIAR POPULACJI: 20, LICZBA POKOLEN: 5, RODZAJ SELEKCJI: TURNIEJOWA, ROZMIAR TURNIEJU:3, PRAWDOPODOBIENSTWO KRZYZOWANIA 0.7, PRAWDOPODOBIENSTWO MUTACJI 0.002
        zad0 +="\n\n" + zwroc_instancje(20,"C:\\Users\\Maciej Chrząstek\\Desktop\\lab1_problemy_testowe\\zad0.txt",5,"turniejowa",3,0.7,0.002);
        // ROZMIAR POPULACJI: 20, LICZBA POKOLEN: 10, RODZAJ SELEKCJI: TURNIEJOWA, ROZMIAR TURNIEJU:3, PRAWDOPODOBIENSTWO KRZYZOWANIA 0.7, PRAWDOPODOBIENSTWO MUTACJI 0.002
        zad0 +="\n" + podstawowy_przyklad;
        // ROZMIAR POPULACJI: 20, LICZBA POKOLEN: 20, RODZAJ SELEKCJI: TURNIEJOWA, ROZMIAR TURNIEJU:3, PRAWDOPODOBIENSTWO KRZYZOWANIA 0.7, PRAWDOPODOBIENSTWO MUTACJI 0.002
        zad0 +="\n" + zwroc_instancje(20,"C:\\Users\\Maciej Chrząstek\\Desktop\\lab1_problemy_testowe\\zad0.txt",20,"turniejowa",3,0.7,0.002);
        zapisz_wyniki_populacji_do_pliku("zad0_liczba_pokolen.csv",zad0);

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //-----ROZMIAR TURNIEJU
        // ROZMIAR POPULACJI: 20, LICZBA POKOLEN: 10, RODZAJ SELEKCJI: TURNIEJOWA, ROZMIAR TURNIEJU:3, PRAWDOPODOBIENSTWO KRZYZOWANIA 0.7, PRAWDOPODOBIENSTWO MUTACJI 0.002
        zad0 +="\n\n" + podstawowy_przyklad;
        // ROZMIAR POPULACJI: 20, LICZBA POKOLEN: 10, RODZAJ SELEKCJI: TURNIEJOWA, ROZMIAR TURNIEJU:5, PRAWDOPODOBIENSTWO KRZYZOWANIA 0.7, PRAWDOPODOBIENSTWO MUTACJI 0.002
        zad0 +="\n" + zwroc_instancje(20,"C:\\Users\\Maciej Chrząstek\\Desktop\\lab1_problemy_testowe\\zad0.txt",10,"turniejowa",5,0.7,0.002);
        // ROZMIAR POPULACJI: 20, LICZBA POKOLEN: 10, RODZAJ SELEKCJI: TURNIEJOWA, ROZMIAR TURNIEJU:8, PRAWDOPODOBIENSTWO KRZYZOWANIA 0.7, PRAWDOPODOBIENSTWO MUTACJI 0.002
        zad0 +="\n" + zwroc_instancje(20,"C:\\Users\\Maciej Chrząstek\\Desktop\\lab1_problemy_testowe\\zad0.txt",10,"turniejowa",8,0.7,0.002);
        zapisz_wyniki_populacji_do_pliku("zad0_rozmiar_turnieju.csv",zad0);

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //-----RULETKA I TURNIEJ
        //TURNIEJ
        // ROZMIAR POPULACJI: 20, LICZBA POKOLEN: 10, RODZAJ SELEKCJI: TURNIEJOWA, ROZMIAR TURNIEJU:3, PRAWDOPODOBIENSTWO KRZYZOWANIA 0.7, PRAWDOPODOBIENSTWO MUTACJI 0.002
        zad0 += "\n\n" +zwroc_instancje(10,"C:\\Users\\Maciej Chrząstek\\Desktop\\lab1_problemy_testowe\\zad0.txt",10,"turniejowa",3,0.7,0.002);
        // ROZMIAR POPULACJI: 20, LICZBA POKOLEN: 10, RODZAJ SELEKCJI: TURNIEJOWA, ROZMIAR TURNIEJU:3, PRAWDOPODOBIENSTWO KRZYZOWANIA 0.7, PRAWDOPODOBIENSTWO MUTACJI 0.002
        zad0 +="\n" + podstawowy_przyklad;
        // ROZMIAR POPULACJI: 20, LICZBA POKOLEN: 10, RODZAJ SELEKCJI: TURNIEJOWA, ROZMIAR TURNIEJU:3, PRAWDOPODOBIENSTWO KRZYZOWANIA 0.7, PRAWDOPODOBIENSTWO MUTACJI 0.002
        zad0 += "\n" +zwroc_instancje(40,"C:\\Users\\Maciej Chrząstek\\Desktop\\lab1_problemy_testowe\\zad0.txt",10,"turniejowa",3,0.7,0.002);


        //RULETKA
        // ROZMIAR POPULACJI: 20, LICZBA POKOLEN: 10, RODZAJ SELEKCJI: RULETKOWA, ROZMIAR TURNIEJU:3, PRAWDOPODOBIENSTWO KRZYZOWANIA 0.7, PRAWDOPODOBIENSTWO MUTACJI 0.002
        zad0 += "\n" +zwroc_instancje(10,"C:\\Users\\Maciej Chrząstek\\Desktop\\lab1_problemy_testowe\\zad0.txt",10,"ruletkowa",3,0.7,0.002);
        // ROZMIAR POPULACJI: 20, LICZBA POKOLEN: 10, RODZAJ SELEKCJI: RULETKOWA, ROZMIAR TURNIEJU:3, PRAWDOPODOBIENSTWO KRZYZOWANIA 0.7, PRAWDOPODOBIENSTWO MUTACJI 0.002
        zad0 += "\n" +zwroc_instancje(20,"C:\\Users\\Maciej Chrząstek\\Desktop\\lab1_problemy_testowe\\zad0.txt",10,"ruletkowa",3,0.7,0.002);
        // ROZMIAR POPULACJI: 20, LICZBA POKOLEN: 10, RODZAJ SELEKCJI: RULETKOWA, ROZMIAR TURNIEJU:3, PRAWDOPODOBIENSTWO KRZYZOWANIA 0.7, PRAWDOPODOBIENSTWO MUTACJI 0.002
        zad0 += "\n" +zwroc_instancje(40,"C:\\Users\\Maciej Chrząstek\\Desktop\\lab1_problemy_testowe\\zad0.txt",10,"ruletkowa",3,0.7,0.002);

        zapisz_wyniki_populacji_do_pliku("zad0_mutacja_vs_turniej.csv",zad0);
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //-----ZBADANIE WPLYWU PARAMETROW PRAWDOPODOBIENSTWA
        //KRZYZOWANIE
        // ROZMIAR POPULACJI: 20, LICZBA POKOLEN: 10, RODZAJ SELEKCJI: TURNIEJOWA, ROZMIAR TURNIEJU:3, PRAWDOPODOBIENSTWO KRZYZOWANIA 0.7, PRAWDOPODOBIENSTWO MUTACJI 0.002
        zad0 += "\n\n" + podstawowy_przyklad;
        // ROZMIAR POPULACJI: 20, LICZBA POKOLEN: 10, RODZAJ SELEKCJI: TURNIEJOWA, ROZMIAR TURNIEJU:3, PRAWDOPODOBIENSTWO KRZYZOWANIA 0.5, PRAWDOPODOBIENSTWO MUTACJI 0.002
        zad0 += "\n" +zwroc_instancje(10,"C:\\Users\\Maciej Chrząstek\\Desktop\\lab1_problemy_testowe\\zad0.txt",10,"turniejowa",3,0.5,0.002);
        // ROZMIAR POPULACJI: 20, LICZBA POKOLEN: 10, RODZAJ SELEKCJI: TURNIEJOWA, ROZMIAR TURNIEJU:3, PRAWDOPODOBIENSTWO KRZYZOWANIA 0.3, PRAWDOPODOBIENSTWO MUTACJI 0.002
        zad0 += "\n" +zwroc_instancje(10,"C:\\Users\\Maciej Chrząstek\\Desktop\\lab1_problemy_testowe\\zad0.txt",10,"turniejowa",3,0.3,0.002);
        zapisz_wyniki_populacji_do_pliku("zad0_prawdopodobienstwo_krzyzowania.csv",zad0);

        //MUTACJA
        // ROZMIAR POPULACJI: 20, LICZBA POKOLEN: 10, RODZAJ SELEKCJI: TURNIEJOWA, ROZMIAR TURNIEJU:3, PRAWDOPODOBIENSTWO KRZYZOWANIA 0.7, PRAWDOPODOBIENSTWO MUTACJI 0.005
        zad0 += "\n\n" +zwroc_instancje(10,"C:\\Users\\Maciej Chrząstek\\Desktop\\lab1_problemy_testowe\\zad0.txt",10,"turniejowa",3,0.7,0.005);
        // ROZMIAR POPULACJI: 20, LICZBA POKOLEN: 10, RODZAJ SELEKCJI: TURNIEJOWA, ROZMIAR TURNIEJU:3, PRAWDOPODOBIENSTWO KRZYZOWANIA 0.7, PRAWDOPODOBIENSTWO MUTACJI 0.010
        zad0 += "\n" +zwroc_instancje(10,"C:\\Users\\Maciej Chrząstek\\Desktop\\lab1_problemy_testowe\\zad0.txt",10,"turniejowa",3,0.7,0.010);
        // ROZMIAR POPULACJI: 20, LICZBA POKOLEN: 10, RODZAJ SELEKCJI: TURNIEJOWA, ROZMIAR TURNIEJU:3, PRAWDOPODOBIENSTWO KRZYZOWANIA 0.7, PRAWDOPODOBIENSTWO MUTACJI 0.015
        zad0 += "\n" +zwroc_instancje(10,"C:\\Users\\Maciej Chrząstek\\Desktop\\lab1_problemy_testowe\\zad0.txt",10,"turniejowa",3,0.7,0.015);

        zapisz_wyniki_populacji_do_pliku("zad0_calosc.csv",zad0);



//*/

    }

    public static void zapisz_wyniki_populacji_do_pliku(String nazwa, String napis){
        try (PrintWriter writer = new PrintWriter(new File(nazwa))) {

            StringBuilder sb = new StringBuilder();
            writer.write(napis);
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void zapisz_wyniki_populacji_do_pliku(String nazwa, ArrayList<Integer> lista_ocen_wynikow){
        try (PrintWriter writer = new PrintWriter(new File(nazwa))) {

            StringBuilder sb = new StringBuilder();


            for(int i=0;i<lista_ocen_wynikow.size();i++){
                sb.append((i+1));
                sb.append(",");
                sb.append(lista_ocen_wynikow.get(i));
                sb.append("\n");
            }
            writer.write(sb.toString());

            System.out.println("Zapisano wyniki populacji!");

        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    public static String zwroc_instancje(int rozmiar_populacji, String sciezka_do_pliku, int liczba_pokolen, String rodzaj_selekcji,int rozmiar_turnieju, double prawdopodobienstwo_krzyzowania,double prawdopodobienstwo_mutacji){
        ArrayList<Integer> lista_najlpeszych = new ArrayList<>();
        ArrayList<Integer> lista_losowych = new ArrayList<>();
        for(int i=0;i<7;i++){
            ArrayList<Integer> wyniki = znajdz_wyniki_uruchomienia(rozmiar_populacji,sciezka_do_pliku,liczba_pokolen,rodzaj_selekcji,rozmiar_turnieju,prawdopodobienstwo_krzyzowania,prawdopodobienstwo_mutacji);
             lista_najlpeszych.add(wyniki.get(0));
             lista_losowych.add(wyniki.get(1));
        }
        int najlepszy_wynik1 = Collections.min(lista_najlpeszych);
        int najgorszy_wynik1 = Collections.max(lista_najlpeszych);
        double srednia1 = srednia(lista_najlpeszych);
        double odchylenie1 = sd(lista_najlpeszych,srednia1);
        String instancja1 = najlepszy_wynik1 +","+ najgorszy_wynik1+","+srednia1+","+odchylenie1;

        int najlepszy_wynik2 = Collections.min(lista_losowych);
        int najgorszy_wynik2 = Collections.max(lista_losowych);
        double srednia2 = srednia(lista_losowych);
        double odchylenie2 = sd(lista_losowych,srednia2);
        String instancja2 = najlepszy_wynik2 +","+ najgorszy_wynik2+","+srednia2+","+odchylenie2;

        return instancja1+","+instancja2;
    }

    public static ArrayList<Integer> znajdz_wyniki_uruchomienia(int rozmiar_populacji, String sciezka_do_pliku, int liczba_pokolen, String rodzaj_selekcji,int rozmiar_turnieju, double prawdopodobienstwo_krzyzowania,double prawdopodobienstwo_mutacji){
        ArrayList<Integer> wyniki_uruchomienia = new ArrayList<>();

        Populacja aktualna_populacja = new Populacja(rozmiar_populacji,sciezka_do_pliku);
        aktualna_populacja.losuj_polaczenia_osobnikow();
        System.out.println(aktualna_populacja);

        Osobnik najlepszy_losowy=null;
        for(int i=0;i<aktualna_populacja.populacja.size();i++){
            if(najlepszy_losowy==null || aktualna_populacja.populacja.get(i).funkcja_oceny()<najlepszy_losowy.funkcja_oceny()){
                najlepszy_losowy = aktualna_populacja.populacja.get(i);
            }
        }

        Osobnik najlepszy_osobnik=null;
        //Osobnik najgorszy_osobnik=null;

        ///////////////////////////////////////
        //ArrayList<Integer> oceny_najlepszych_z_pokolen = new ArrayList<>();
        //////////////////////////////////////////
        for(int i=0;i<liczba_pokolen;i++){
            ///////////////////////////////////////////////
            //Osobnik najlepszy_osobnik_z_pokolenia=null;
            //////////////////////////////////////////////
            System.out.println("Populacja "+(i+1)+": \n");
            System.out.println("________________________________________");
            Populacja nowa_populacja = new Populacja(aktualna_populacja.populacja.size(),aktualna_populacja.sciezka_do_pliku);
            for(int j=0;j<aktualna_populacja.populacja.size();j++){
                if(rodzaj_selekcji.equals("turniejowa")){
                    System.out.println("Wybrano selekcję turniejową: \n");
                    Osobnik rodzic1 = aktualna_populacja.selekcja_turniejowa_osobnika(rozmiar_turnieju);
                    System.out.println("Wybrano rodzica 1: \n");
                    System.out.println(rodzic1);
                    Osobnik rodzic2 = aktualna_populacja.selekcja_turniejowa_osobnika(rozmiar_turnieju);
                    System.out.println("Wybrano rodzica 2: \n");
                    System.out.println(rodzic2);
                    Osobnik potomek = aktualna_populacja.krzyzowanie(rodzic1,rodzic2,prawdopodobienstwo_krzyzowania);
                    System.out.println("Dokonano krzyzowania potomka: \n");
                    System.out.println(potomek);

                    potomek=aktualna_populacja.mutacja(potomek,prawdopodobienstwo_mutacji);
                    System.out.println("Dokonano mutacji potomka: \n");
                    System.out.println(potomek);
                    nowa_populacja.populacja.set(j,potomek);
                    if(najlepszy_osobnik==null || najlepszy_osobnik.funkcja_oceny()>potomek.funkcja_oceny()){
                        najlepszy_osobnik=potomek;
                    }
                    //if(najgorszy_osobnik==null || najgorszy_osobnik.funkcja_oceny()<potomek.funkcja_oceny()){
                    //    najgorszy_osobnik=potomek;
                    //}
                    ///////////////////////////////////////////////
                    //if(najlepszy_osobnik_z_pokolenia==null || najlepszy_osobnik_z_pokolenia.funkcja_oceny()>potomek.funkcja_oceny()){
                    //    najlepszy_osobnik=potomek;
                    //    najlepszy_osobnik_z_pokolenia=potomek;
                    //}
                    ///////////////////////////////////////////////

                }
                else {
                    System.out.println("Wybrano selekcję ruletkową: \n");
                    Osobnik rodzic1 = aktualna_populacja.selekcja_ruletkowa_osobnika();
                    System.out.println("Wybrano rodzica 1: \n");
                    System.out.println(rodzic1);
                    Osobnik rodzic2 = aktualna_populacja.selekcja_ruletkowa_osobnika();
                    System.out.println("Wybrano rodzica 2: \n");
                    System.out.println(rodzic2);
                    Osobnik potomek = aktualna_populacja.krzyzowanie(rodzic1,rodzic2,prawdopodobienstwo_krzyzowania);
                    System.out.println("Dokonano krzyzowania potomka: \n");
                    System.out.println(potomek);

                    potomek=aktualna_populacja.mutacja(potomek,prawdopodobienstwo_mutacji);
                    System.out.println("Dokonano mutacji potomka: \n");
                    System.out.println(potomek);
                    nowa_populacja.populacja.set(j,potomek);
                    if(najlepszy_osobnik==null || najlepszy_osobnik.funkcja_oceny()>potomek.funkcja_oceny()){
                        najlepszy_osobnik=potomek;
                    }
                    //if(najgorszy_osobnik==null || najgorszy_osobnik.funkcja_oceny()<potomek.funkcja_oceny()){
                    //    najgorszy_osobnik=potomek;
                    //}
                    ///////////////////////////////////////////////
                    //if(najlepszy_osobnik_z_pokolenia==null || najlepszy_osobnik_z_pokolenia.funkcja_oceny()>potomek.funkcja_oceny()){
                    //    najlepszy_osobnik=potomek;
                    //    najlepszy_osobnik_z_pokolenia=potomek;
                    //}
                    ///////////////////////////////////////////////

                }
                System.out.println("________________________________________");

            }
            ///////////////////////////////////////////////
            //oceny_najlepszych_z_pokolen.add(najlepszy_osobnik_z_pokolenia.funkcja_oceny());
            ///////////////////////////////////////////////
            aktualna_populacja = nowa_populacja;
        }
        //////////////////////////////////
        //zapisz_wyniki_populacji_do_pliku("wyniki.csv", oceny_najlepszych_z_pokolen);
        //////////////////////////////////
        //return najlepszy_osobnik;
        //wyniki_uruchomienia.add();
        wyniki_uruchomienia.add(najlepszy_osobnik.funkcja_oceny());
        wyniki_uruchomienia.add(najlepszy_losowy.funkcja_oceny());
        return wyniki_uruchomienia;
    }

    public static double srednia (ArrayList<Integer> wyniki)
    {
        double suma = 0.0;

        for ( int i= 0;i < wyniki.size(); i++)
        {
            suma+= (double)wyniki.get(i);
        }
        return suma/wyniki.size();
    }


    public static double sd (ArrayList<Integer> wyniki, double srednia)
    {
        // Step 1:
        double temp = 0.0;

        for (int i = 0; i < wyniki.size(); i++)
        {
            int aktualny_wynik = wyniki.get(i);

            // Step 2:
            double squrDiffToMean = Math.pow(aktualny_wynik - srednia, 2);

            // Step 3:
            temp += squrDiffToMean;
        }

        // Step 4:
        double meanOfDiffs = (double) temp / (double) (wyniki.size());

        // Step 5:
        return Math.sqrt(meanOfDiffs);
    }
}
