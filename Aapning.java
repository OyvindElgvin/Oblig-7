class Aapning extends HvitRute {

	public Aapning(int k, int r) {
		super(k, r);
	}

	public char tilTegn(){
		return '.';
	}

	@Override
	public boolean erAapning() {
		return true;
	}

	protected void gaa(Rute forrige, String gamleKoordinater) throws InterruptedException {
		if (this.erAapning() == true) {
			vei = gamleKoordinater + toString();
			labyrintRef.monitor.leggILista(vei);
			if (Oblig7.detaljert == true) { // hvis detaljert
				System.out.println("\nÅPNING på ruta "+toString());
			}
		}
		return;
	}
}
