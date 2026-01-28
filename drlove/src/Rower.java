import java.util.ArrayList;
import java.util.List;
import java.util.Date;

// Klasa Rower - nasza podstawowa jednostka w "bazie danych"
class Rower {
    private int id;
    private String model;
    private boolean czyDostepny;
    private String imagePath;
    private String opis;


    public Rower(int id, String model, String imagePath, String opis) {
        this.id = id;
        this.model = model;
        this.imagePath = imagePath;
        this.opis = opis;
        this.czyDostepny = true;

    }

    // Gettery i Settery pozwalają kontrolować dostęp do danych
    public String getImagePath() { return imagePath; }
    public int getId() { return id; }
    public String getModel() { return model; }
    public String getOpis() { return opis; }
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
    private List<Rower> flotaRowerow = new ArrayList<>();

    public Wypozyczalnia(String nazwa) {
        this.nazwa = nazwa;
    }

    public List<Rower> getFlota() { // Naprawia błąd 236
        return flotaRowerow;
    }

    public String getNazwa() { return nazwa; }

    public void dodajRower(Rower r) {
        flotaRowerow.add(r);
    }
}