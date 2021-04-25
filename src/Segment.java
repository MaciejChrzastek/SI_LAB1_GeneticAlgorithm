import java.util.ArrayList;

public class Segment {
    Punkt punkt_poczatkowy_segmentu;
    Punkt punkt_koncowy_segmentu;
    String kierunek;
    int wartosc;

    public Segment(Punkt punkt_poczatkowy_segmentu, Punkt punkt_koncowy_segmentu) {
        this.punkt_poczatkowy_segmentu = punkt_poczatkowy_segmentu;
        this.punkt_koncowy_segmentu = punkt_koncowy_segmentu;
        this.sprawdz_kierunek();
        this.oblicz_wartosc();
    }

    public boolean czy_segment_poza_plytka(int szerokosc_plytki, int wysokosc_plytki){
        if(this.punkt_poczatkowy_segmentu.czy_punkt_poza_plytka(szerokosc_plytki,wysokosc_plytki) || this.punkt_koncowy_segmentu.czy_punkt_poza_plytka(szerokosc_plytki,wysokosc_plytki)) return true;
        else return false;
    }

    @Override
    public String toString() {
        return "{"+this.punkt_poczatkowy_segmentu+"; "+this.punkt_koncowy_segmentu+"}"+ "Kierunek: "+this.kierunek +", Wartosc: "+this.wartosc;
    }

    private void sprawdz_kierunek(){
        int x_pocz = punkt_poczatkowy_segmentu.X();
        int y_pocz = punkt_poczatkowy_segmentu.Y();
        int x_konc = punkt_koncowy_segmentu.X();
        int y_konc = punkt_koncowy_segmentu.Y();

        if(x_pocz==x_konc){
            this.kierunek="pionowy";
        }
        else if (y_pocz==y_konc){
            this.kierunek="poziomy";
        }
    }
    private void oblicz_wartosc(){
        int x_pocz = punkt_poczatkowy_segmentu.X();
        int y_pocz = punkt_poczatkowy_segmentu.Y();
        int x_konc = punkt_koncowy_segmentu.X();
        int y_konc = punkt_koncowy_segmentu.Y();

        if(x_pocz==x_konc){
            this.wartosc= Math.abs(y_konc-y_pocz);
        }
        else if (y_pocz==y_konc){
            this.wartosc= Math.abs(x_konc-x_pocz);
        }
    }

    public ArrayList<Punkt> zwroc_punkty_segmentu(){
        ArrayList<Punkt> lista_punktow = new ArrayList<>();
            String obecny_kierunek = this.kierunek;
            if(obecny_kierunek.equals("pionowy")){
                Punkt aktualny_punkt = new Punkt(this.punkt_poczatkowy_segmentu.X(),this.punkt_poczatkowy_segmentu.Y());
                Punkt koncowy_punkt = new Punkt(this.punkt_koncowy_segmentu.X(),this.punkt_koncowy_segmentu.Y());

                while(!aktualny_punkt.equals(koncowy_punkt)){
                    lista_punktow.add(aktualny_punkt);

                    if(Math.abs(koncowy_punkt.Y()-(aktualny_punkt.Y()+1)) < Math.abs(koncowy_punkt.Y()-(aktualny_punkt.Y()-1))){
                        aktualny_punkt = new Punkt(aktualny_punkt.X(),aktualny_punkt.Y()+1);
                    }
                    else{
                        aktualny_punkt = new Punkt(aktualny_punkt.X(),aktualny_punkt.Y()-1);
                    }
                }
                lista_punktow.add(new Punkt(koncowy_punkt.X(),koncowy_punkt.Y()));
            }
            else{
                Punkt aktualny_punkt = new Punkt(this.punkt_poczatkowy_segmentu.X(),this.punkt_poczatkowy_segmentu.Y());
                Punkt koncowy_punkt = new Punkt(this.punkt_koncowy_segmentu.X(),this.punkt_koncowy_segmentu.Y());

                while(!aktualny_punkt.equals(koncowy_punkt)){
                    lista_punktow.add(aktualny_punkt);

                    if(Math.abs(koncowy_punkt.X()-(aktualny_punkt.X()+1)) < Math.abs(koncowy_punkt.X()-(aktualny_punkt.X()-1))){
                        aktualny_punkt = new Punkt(aktualny_punkt.X()+1,aktualny_punkt.Y());
                    }
                    else{
                        aktualny_punkt = new Punkt(aktualny_punkt.X()-1,aktualny_punkt.Y());
                    }
                }
                lista_punktow.add(new Punkt(koncowy_punkt.X(),koncowy_punkt.Y()));

            }

        return lista_punktow;
    }


}
