public class Punkt {
    private int x;
    private int y;

    public Punkt(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "("+x+", "+y+")";
    }

    public int X(){
        return this.x;
    }
    public int Y(){
        return this.y;
    }

    public void X(int x) {
        this.x = x;
    }

    public void Y(int y) {
        this.y = y;
    }
/*
    public boolean equals(Punkt pkt) {
        return (this.x==pkt.X() && this.y==pkt.Y());
    }
*/
    @Override
    public boolean equals(Object obj) {
        return (this.x==(((Punkt)obj).X()) && this.y==(((Punkt)obj).Y()));
    }

    public boolean czy_punkt_poza_plytka(int szerokosc_plytki, int wysokosc_plytki){
        if(this.x<0 || this.x>szerokosc_plytki || this.y<0 || this.y>wysokosc_plytki)return true;
        else return false;
    }
}
