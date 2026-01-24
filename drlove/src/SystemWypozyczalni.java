class SystemWypozyczalni {

    // Główna metoda logiki biznesowej
    public boolean wypozyczRower(Klient klient, Wypozyczalnia wypozyczalnia, int rowerId) {
        // 1. Sprawdź dostępność w bazie
        Rower rower = wypozyczalnia.znajdzDostepnyRower(rowerId);

        if (rower != null) {
            // 2. Zmień stan w bazie (blokada roweru)
            rower.setDostepny(false);

            // 3. Stwórz obiekt wypożyczenia
            Wypozyczenie noweWypozyczenie = new Wypozyczenie(rower, wypozyczalnia);

            // 4. Dodaj do zakładki klienta
            klient.dodajDoHistorii(noweWypozyczenie);

            // 5. Powiadom Admina (zgodnie z wymaganiem)
            powiadomAdmina(klient, noweWypozyczenie);

            return true;
        } else {
            System.out.println("Błąd: Rower niedostępny lub nie istnieje w tej lokalizacji.");
            return false;
        }
    }

    // Prywatna metoda tylko dla systemu (symulacja panelu admina)
    private void powiadomAdmina(Klient k, Wypozyczenie w) {
        System.out.println("\n[ADMIN ALERT] Nowa transakcja!");
        System.out.println("   Kto: " + k.getImieNazwisko());
        System.out.println("   Co:  " + w.getRower().getModel() + " (ID: " + w.getRower().getId() + ")");
        System.out.println("   Skąd: " + w.getWypozyczalnia().getNazwa());
        System.out.println("------------------------------------------------");
    }
}
