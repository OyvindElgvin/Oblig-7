import java.util.ArrayList;

public class HvitRute extends Rute {

	protected String vei = "";
	ArrayList<Thread> traadArray;

	public HvitRute(int k, int r) {
		super(k, r);
	}

	public char tilTegn(){
		return '.';
	}

	@Override
	public boolean erAapning() {
		return false;
	}

	// Rekursiv metode som lager en string med alle koordinatene og kaller
	// på seg selv hvis naborutene er hvite og ikke er der den kom fra.
	protected void gaa(Rute forrige, String gamleKoordinater) throws InterruptedException {
		vei = gamleKoordinater + this.toString();
		finnHviteNaboer();
		hviteNaboer.remove(forrige);
		traadArray = new ArrayList<>();

		// hvis blindvei
		if (hviteNaboer.size() == 0) {
			if (Oblig7.detaljert == true) { // hvis detaljert
				System.out.println("blindvei: "+toString());
			}
			return;
		} else {
			// hvis ikke blindvei
			vei = vei + " --> ";
			// hvis > 1 nabo startes nye tråder for alle untatt siste nabo,
			// dette er fordi den gamle tråden må være igjen for å starte de andre trådene
			if (hviteNaboer.size() > 1) {
				for (int i = 0; i < hviteNaboer.size()-1; i++) {
					Runnable vandreren = new LabyrintVandrer(hviteNaboer.get(i), this, vei);
					Thread nyThread = new Thread(vandreren);
					nyThread.start();
					traadArray.add(nyThread);
				}
			}
			// Den gamle tråden sendes videre til den siste hvite naboen rekursivt ved nytt kall på gaa()
			hviteNaboer.get(hviteNaboer.size()-1).gaa(this, vei);
			if (Oblig7.detaljert == true) { // hvis detaljert
				System.out.println(this+" --> "+hviteNaboer.get(hviteNaboer.size()-1).toString());
			}

			// joiner trådene
			try{
				for (Thread traad : traadArray) {
					traad.join();
				}
			} catch (InterruptedException e) {}
		}
	}
}
