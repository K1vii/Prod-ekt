import java.util.Date;
import java.util.ArrayList;
import java.util.List;

// Wypozyczenia
class Wypozyczenie {
    private Rower rower;
    private Wypozyczalnia wypozyczalnia;
    private Klient klient;

    public Wypozyczenie(Rower rower, Wypozyczalnia wypozyczalnia, Klient klient) {
        this.rower = rower;
        this.wypozyczalnia = wypozyczalnia;
        this.klient = klient;
    }

    // Gettery do obsługi logiki Admina i plików
    public Klient getKlient() { return klient; }
    public Rower getRower() { return rower; }
    public Wypozyczalnia getWypozyczalnia() { return wypozyczalnia; }
    public Wypozyczalnia getStacja() { return wypozyczalnia; }
}

// Klient
class Klient {
    private String imie;
    private String nazwisko;
    private List<Wypozyczenie> mojeWypozyczenia = new ArrayList<>();

    public Klient(String imie, String nazwisko) {
        this.imie = imie;
        this.nazwisko = nazwisko;
    }

    public String getImie() { return imie; }
    public String getNazwisko() { return nazwisko; }

    // Zarządzanie listą rowerów klienta
    public List<Wypozyczenie> getMojeWypozyczenia() {
        return mojeWypozyczenia;
    }

    public void dodajDoHistorii(Wypozyczenie w) {
        mojeWypozyczenia.add(w);
    }

    public void usunWypozyczenie(Wypozyczenie w) {
        mojeWypozyczenia.remove(w);
    }

    // check if douplicates
    public boolean czyPosiadaRower(int id) {
        for (Wypozyczenie w : mojeWypozyczenia) {
            if (w.getRower().getId() == id) return true;
        }
        return false;
    }
}