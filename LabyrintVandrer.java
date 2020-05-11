class LabyrintVandrer implements Runnable {

	Rute nesteRute;
	Rute gammelRute;
	String hvorDenKomFra;

	public LabyrintVandrer(Rute nesteRute, Rute gammelRute, String hvorDenKomFra) {
		this.nesteRute = nesteRute;
		this.gammelRute = gammelRute;
		this.hvorDenKomFra = hvorDenKomFra;

		if (Oblig7.detaljert == true) { // hvis detaljert
			System.out.println("Ny LabyrintVandrer går til: "+ nesteRute.toString());
		}
	}

	@Override
	public void run(){
		try {
			nesteRute.gaa(gammelRute, hvorDenKomFra);
		} catch (InterruptedException e) {}
	}
}
