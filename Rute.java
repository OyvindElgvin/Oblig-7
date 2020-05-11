import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Lock;


abstract class Rute extends Thread {

	protected Labyrint labyrintRef;
	protected int kol;
	protected int rad;
	protected Rute nord;
	protected Rute sor;
	protected Rute ost;
	protected Rute vest;
	protected List<Rute> hviteNaboer;

	Lock laas = new ReentrantLock();

	protected Rute(int k, int r) {
		kol = k;
		rad = r;
	}

	public void settLabRef(Labyrint lab) {
		labyrintRef = lab;
	}

	abstract boolean erAapning();
	abstract char tilTegn();

	protected void settNaboNord(Rute nabo){
		nord = nabo;}
	protected void settNaboSor(Rute nabo){
		sor = nabo;}
	protected void settNaboOst(Rute nabo){
		ost = nabo;}
	protected void settNaboVest(Rute nabo){
		vest = nabo;}

	public String toString() {
		return "("+kol+", "+rad+")";
	}

	// test for å sjekke om naboene er satt riktig
	protected void finnHviteNaboer() {
		hviteNaboer = new ArrayList<Rute>();
		if (kol > -1 && kol < labyrintRef.antallKolonner-1) {
			if ((ost.tilTegn() == '.'))
				hviteNaboer.add(ost);}
		if (kol < labyrintRef.antallKolonner && kol > 0) {
			if ((vest.tilTegn() == '.'))
				hviteNaboer.add(vest);}
		if (rad > -1 && rad < labyrintRef.antallRader-1) {
			if ((sor.tilTegn() == '.'))
				hviteNaboer.add(sor);}
		if (rad < labyrintRef.antallRader && rad > 0) {
			if ((nord.tilTegn() == '.'))
				hviteNaboer.add(nord);}
	}

	abstract void gaa(Rute forrige, String gamleKoordinater) throws InterruptedException ;

	protected void finnUtvei() {
		try {
			Runnable vandreren = new LabyrintVandrer(this, this, "");
			Thread nyThread = new Thread(vandreren);
			nyThread.start();
			nyThread.join();
		} catch (InterruptedException e) {}
		return;
	}
}
