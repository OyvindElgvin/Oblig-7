class SortRute extends Rute {

	public SortRute(int k, int r) {
		super(k, r);
	}

	public char tilTegn(){
		return '#';
	}

	@Override
	public boolean erAapning() {
		return false;
	}

	protected void gaa(Rute forrige, String gamleKoordinater) {
		return;
	}
}
