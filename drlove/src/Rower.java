import java.util.ArrayList;
import java.util.List;
import java.util.Date;

// Klasa Rower - nasza podstawowa jednostka w "bazie danych"
class Rower {
    private int id;
    private String model;
    private boolean czyDostepny;

    public Rower(int id, String model) {
        this.id = id;
        this.model = model;
        this.czyDostepny = true;
    }

    // Gettery i Settery pozwalają kontrolować dostęp do danych
    public int getId() { return id; }
    public String getModel() { return model; }
    public boolean isDostepny() { return czyDostepny; }

    public void setDostepny(boolean status) {
        this.czyDostepny = status;
    }

    @Override
    public String toString() {
        return "Rower " + model + " [ID: " + id + "]";
    }
}

// Klasa Wypozyczalnia - kontener na rowery
class Wypozyczalnia {
    private String nazwa;
    private List<Rower> flotaRowerow; // To jest nasza lokalna "baza" dla punktu

    public Wypozyczalnia(String nazwa) {
        this.nazwa = nazwa;
        this.flotaRowerow = new ArrayList<>();
    }

    public void dodajRower(Rower rower) {
        flotaRowerow.add(rower);
    }

    public String getNazwa() { return nazwa; }

    // Metoda szukająca roweru w tym konkretnym punkcie
    public Rower znajdzDostepnyRower(int id) {
        for (Rower r : flotaRowerow) {
            if (r.getId() == id && r.isDostepny()) {
                return r;
            }
        }
        return null; // Nie znaleziono lub zajęty
    }
}