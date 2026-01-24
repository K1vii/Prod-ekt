import java.util.Date;
import java.util.ArrayList;
import java.util.List;

// Klasa reprezentująca wpis w historii (DTO - Data Transfer Object)
class Wypozyczenie {
    private Rower rower;
    private Wypozyczalnia zWypozyczalni;
    private Date data;

    public Wypozyczenie(Rower rower, Wypozyczalnia zWypozyczalni) {
        this.rower = rower;
        this.zWypozyczalni = zWypozyczalni;
        this.data = new Date(); // Aktualna data
    }

    @Override
    public String toString() {
        return "Rower: " + rower.getModel() + ", z: " + zWypozyczalni.getNazwa() + ", dnia: " + data;
    }

    public Rower getRower() { return rower; }
    public Wypozyczalnia getWypozyczalnia() { return zWypozyczalni; }
}

// Klasa Klient
class Klient {
    private String imie;
    private String nazwisko;
    private List<Wypozyczenie> mojeWypozyczenia; // To jest "Zakładka Moje Wypożyczenia"

    public Klient(String imie, String nazwisko) {
        this.imie = imie;
        this.nazwisko = nazwisko;
        this.mojeWypozyczenia = new ArrayList<>();
    }

    public void dodajDoHistorii(Wypozyczenie w) {
        mojeWypozyczenia.add(w);
    }

    public String getImieNazwisko() {
        return imie + " " + nazwisko;
    }

    // Wyświetlanie "Zakładki"
    public void pokazMojeWypozyczenia() {
        System.out.println("\n--- MOJE WYPOŻYCZENIA (" + getImieNazwisko() + ") ---");
        if (mojeWypozyczenia.isEmpty()) {
            System.out.println("Brak aktywnych wypożyczeń.");
        } else {
            for (Wypozyczenie w : mojeWypozyczenia) {
                System.out.println(" > " + w);
            }
        }
    }
}
