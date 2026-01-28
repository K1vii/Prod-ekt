import java.util.Date;
import java.util.ArrayList;
import java.util.List;

// Klasa reprezentująca wpis w historii (DTO - Data Transfer Object)
class Wypozyczenie {
    private Rower rower;
    private Wypozyczalnia wypozyczalnia;
    private Klient klient; // <--- DODAJ TO POLE

    public Wypozyczenie(Rower rower, Wypozyczalnia wypozyczalnia, Klient klient) {
        this.rower = rower;
        this.wypozyczalnia = wypozyczalnia;
        this.klient = klient; // <--- PRZYPISZ KLIENTA
    }

    // Dodaj gettera, żeby Admin mógł go odczytać
    public Klient getKlient() { return klient; }

    public Rower getRower() { return rower; }
    public Wypozyczalnia getWypozyczalnia() { return wypozyczalnia; }
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
    public String getImie() { return imie; }
    public String getNazwisko() { return nazwisko; }

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
