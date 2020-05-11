
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.*;


class Labyrint {

	List<List<Rute>> ruteListe;
	static int antallKolonner;
	static int antallRader;
	Liste<String> utveiLista;
	Monitor monitor;
	public static Lock lock = new ReentrantLock();
	final static Condition erTom = lock.newCondition();


	private Labyrint(List<List<Rute>> lab, int kolonner, int rader) {
		this.ruteListe = lab;
		antallKolonner = kolonner;
		antallRader = rader;
	}

	// Override av toString() returnerer et kart over labyrinten
	@Override
	public String toString() {
		System.out.println();
		System.out.println("Fra toString()");
		System.out.println(antallRader+" "+antallKolonner);
		String hele = "";
		for (int y = 0; y < antallRader; y++) {
			String enRad = "";
			for (int x = 0; x < antallKolonner; x++) {
				enRad += String.valueOf(ruteListe.get(y).get(x).tilTegn());
				// 									rad  kolonne       okeyy?? rart.
			}
			if (y == antallRader-1) {
				hele += String.format("%s",enRad);
			} else if (!(y == antallRader)) {
				hele += String.format("%s\n",enRad);
			}
		}
		return hele;
	}

	// Leser inn koordinatene, tegntypen, og om rutene er en åpning på labyrinten.
	// Setter disse Ruteobjektene inn i en 2D ArrayList med samme koordinater som i originalfilen.
	// For hver Rute lages en referanse til hver av naboene og en referanse til labyrinten.
	// Metoden returnerer den ferdige labyrinten.
	static Labyrint lesFraFil(File fil) throws FileNotFoundException {
		Scanner scanned = new Scanner(fil);
		antallRader = scanned.nextInt();
		antallKolonner = scanned.nextInt();
		if (Oblig7.detaljert == true) {
			System.out.println("Antall kolonner: "+antallKolonner+"\nAntall rader: "+antallRader);
		}
		String kastLinjen = scanned.nextLine(); // hopper over første linja
		// lager et todimmensjonalt-array
		List<List<Rute>> heleKartet = new ArrayList<List<Rute>>();
		int raden = 0;
		while (scanned.hasNextLine()) {
			List<Rute> radArray = new ArrayList<>();
			String linje = "";
			linje = scanned.nextLine();
			for (int kol = 0; kol < antallKolonner; kol++) {
				// henter tegnet for hver kolonne i hver rad
				char tegnet = linje.charAt(kol);
				Rute nesteRute = null;
				// hvis åpning
				if (tegnet == '.' && (kol == 0 || kol == antallKolonner-1 || raden == 0 || raden == antallRader-1 )) {
					nesteRute = new Aapning(kol, raden);
					if (Oblig7.detaljert == true) {
						System.out.println("Åpning ved "+nesteRute);
					}
				// hvis hvit rute
				} else if (tegnet == '.') {
					nesteRute = new HvitRute(kol, raden);
				// hvis sort rute
				} else if (tegnet == '#') {
					nesteRute = new SortRute(kol, raden);
				}
				// legger ruta inn i raden
				radArray.add(nesteRute);
			}
			// legger inn raden
			heleKartet.add(radArray);
			raden ++;
		}
		// legger til naboene til hver Rute
		for (int rad = 0; rad < antallRader; rad++) {
			// for hver rad,
			for (int kol = 0; kol < antallKolonner; kol++) {
				// for hver kolonne
				if (kol < antallKolonner-1) {
					// hvis ikke helt til høyre
					heleKartet.get(rad).get(kol).settNaboOst(heleKartet.get(rad).get(kol+1));
					heleKartet.get(rad).get(kol+1).settNaboVest(heleKartet.get(rad).get(kol));
				}
				if (rad < antallRader-1) {
					// hvis ikke på siste rad
					heleKartet.get(rad).get(kol).settNaboSor(heleKartet.get(rad+1).get(kol));
					heleKartet.get(rad+1).get(kol).settNaboNord(heleKartet.get(rad).get(kol));
				}
			}
		}
		Labyrint midlertidigLabyrint = new Labyrint(heleKartet, antallKolonner, antallRader);

		// setter labyrintreferansen til hver rute
		for (int rad = 0; rad < antallRader; rad++) {
			for (int kol = 0; kol < antallKolonner; kol++) {
				heleKartet.get(rad).get(kol).settLabRef(midlertidigLabyrint);
			}
		}
		// Skriver ut detaljer
		if (Oblig7.detaljert == true) {
			System.out.println();
			System.out.println("Koordinatene av hele kartet");
			for (int rader = 0; rader < antallRader; rader++) {
				for (int kolonner = 0; kolonner < antallKolonner; kolonner++) {
					System.out.print(heleKartet.get(rader).get(kolonner).toString());
				}
				System.out.println();
			}
		}
		// Skriver ut detaljer
		if (Oblig7.detaljert == true) {
			System.out.println();
			System.out.println("Kartet");
			for (int rader = 0; rader < antallRader; rader++) {
				for (int koller = 0; koller < antallKolonner; koller++) {
					System.out.print(heleKartet.get(rader).get(koller).tilTegn());
				}
				System.out.println();
			}
		}
		// Tester toString() og sjekker naboene til tilfeldig valgt rute
		if (Oblig7.detaljert == true) {
			System.out.println(midlertidigLabyrint.toString());
			// Sjekker naboene til en tilfeldig valgt rute
			Random rand = new Random();
			int testKolonne = rand.nextInt(antallKolonner);
			int testRad = rand.nextInt(antallRader);
			System.out.println("Her er de hvite naboene til for eksempel "+ "("+testKolonne+", "+testRad+")");
			heleKartet.get(testRad).get(testKolonne).finnHviteNaboer();
			System.out.println("Sjekk disse manuelt i kartet over");
		}

		return midlertidigLabyrint;
	}// lesFraFil() ferdig


	// kaller på finnUtvei() og returnerer Stringen med utveier
	public Liste<String> finnUtveiFra(int k, int r) throws InterruptedException {
		monitor = new Monitor();
		ruteListe.get(r).get(k).finnUtvei();
		return monitor.hentUtveiLista();
	}

}
