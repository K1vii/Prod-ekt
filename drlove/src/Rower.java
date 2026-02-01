import java.util.ArrayList;
import java.util.List;

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

    public String getImagePath() { return imagePath; }
    public int getId() { return id; }
    public String getModel() { return model; }
    public String getOpis() { return opis; }
    public boolean isDostepny() { return czyDostepny; }
    public void setDostepny(boolean status) { this.czyDostepny = status; }

    @Override
    public String toString() { return "Rower " + model + " [ID: " + id + "]"; }
}

// Do zarządzania grupą rowerów na stacji
class Wypozyczalnia {
    private String nazwa;
    private List<Rower> flotaRowerow = new ArrayList<>();

    public Wypozyczalnia(String nazwa) { this.nazwa = nazwa; }
    public List<Rower> getFlota() { return flotaRowerow; }
    public String getNazwa() { return nazwa; }
    public void dodajRower(Rower r) { flotaRowerow.add(r); }
}