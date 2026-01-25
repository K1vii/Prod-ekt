import java.util.Date;
import java.util.ArrayList;
import java.util.List;

// Klasa reprezentująca wpis w historii (DTO - Data Transfer Object)
class Wypozyczenie {
    private Rower rower;
    private Wypozyczalnia stacja;

    public Wypozyczenie(Rower rower, Wypozyczalnia stacja) {
        this.rower = rower;
        this.stacja = stacja;
    }

    public Rower getRower() { // TEGO BRAKOWAŁO - Naprawia błąd getRower
        return rower;
    }

    public Wypozyczalnia getWypozyczalnia() {
        return stacja;
    }
}

// Klasa Klient
class Klient {
    private String imie;
    private String nazwisko;
    private List<Wypozyczenie> mojeWypozyczenia = new ArrayList<>();

    public Klient(String imie, String nazwisko) {
        this.imie = imie;
        this.nazwisko = nazwisko;
    }

    // TEJ METODY BRAKOWAŁO (Błąd 157, 160):
    public List<Wypozyczenie> getMojeWypozyczenia() {
        return mojeWypozyczenia;
    }

    public void dodajDoHistorii(Wypozyczenie w) {
        mojeWypozyczenia.add(w);
    }

    public void usunWypozyczenie(Wypozyczenie w) {
        mojeWypozyczenia.remove(w);
    }

    // Sprawdzenie czy rower o danym ID jest już u tego klienta
    public boolean czyPosiadaRower(int id) {
        for (Wypozyczenie w : mojeWypozyczenia) {
            if (w.getRower().getId() == id) return true;
        }
        return false;
    }
}
