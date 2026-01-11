
public class Rower {

    private String id;
    private String typ;
    private boolean dostepny;

    public Rower(String id, String typ) {
        this.id = id;
        this.typ = typ;
        this.dostepny = true;
    }

    public String getId() {
        return id;
    }

    public String getTyp() {
        return typ;
    }

    public boolean isDostepny() {
        return dostepny;
    }

    public void wypozycz() {
        this.dostepny = false;
    }

    public void zwroc() {
        this.dostepny = true;
    }

    @Override
    public String toString() {
        return id + " | " + typ + " | " + (dostepny ? "DOSTĘPNY" : "WYPOŻYCZONY");
    }
}
