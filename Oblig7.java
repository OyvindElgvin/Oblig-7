import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

class Oblig7 {

    static public boolean detaljert = false;

    public static void main(String[] args) throws InterruptedException {
        String filnavn = null;


        if (args.length > 0) {
            filnavn = args[0];
        } else {
            System.out.println("FEIL! Riktig bruk av programmet: "
                               +"java Oblig7 <labyrintfil>");
            return;
        }

        // Skriver ut detaljer
        if (args.length > 1) {
            if (args[1].equals("detaljert")) {
                detaljert = true;
            }
        }

        File fil = new File(filnavn);
        Labyrint l = null;
        try {
            l = Labyrint.lesFraFil(fil);
        } catch (FileNotFoundException e) {
            System.out.printf("FEIL: Kunne ikke lese fra '%s'\n", filnavn);
            System.exit(1);
        }

        // les start-koordinater fra standard input
        Scanner inn = new Scanner(System.in);
        System.out.println();

        if (!(Oblig7.detaljert == true)) {
			System.out.println("Bruk ordet <detaljert> som andre-argument for å se detaljer om labyrinten");
		}

        System.out.println("Skriv inn koordinater <kolonne> <rad> ('a' for aa avslutte)");

        String[] ord = inn.nextLine().split(" ");
        while (!ord[0].equals("a")) {

            try {
                int startKol = Integer.parseInt(ord[0]);
                int startRad = Integer.parseInt(ord[1]);

                Liste<String> utveier = l.finnUtveiFra(startKol, startRad);
                if (utveier.stoerrelse() != 0) {
                    for (String s : utveier) {
                        System.out.println();
                        System.out.println(s);
                    }
                } else {
                    System.out.println("Ingen utveier.");
                }
                System.out.println();
            } catch (NumberFormatException e) {
                System.out.println("Ugyldig input!");
            }

            System.out.println("Skriv inn nye koordinater ('a' for aa avslutte)");
            ord = inn.nextLine().split(" ");
        }
    }
}
