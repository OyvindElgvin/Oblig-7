import java.util.Iterator;
class Lenkeliste<T> implements Liste<T> {

	/*
	En lenkeliste med start til venstre og slutt til høyre
	*/

	protected Node start;

	public Lenkeliste() {
		start = null;
	}

	// indre klasse Node med variabel data
	class Node {
		T data;
		Node(T x){
			data = x;
		}
		Node neste = null;
	}

	// Iterator
	public Iterator<T> iterator(){
		return new LenkelisteIterator<T>(this);
	}

	// indre klasse LenkelisteIterator med hasNext() og next()
	class LenkelisteIterator<T> implements Iterator<T>{
		private Lenkeliste<T> enListe;
		private int pos;

		public LenkelisteIterator(Lenkeliste<T> liste){
			enListe = liste;
			pos = 0;
		}

		public T next(){				// returnerer neste element i lista
			return enListe.hent(pos++);
		}

		public boolean hasNext(){ 		// sjekker om det er et neste element
			return(pos < enListe.stoerrelse());
		}
	}

	// legger til ved posisjon pos
	public void leggTil(int pos, T x) {
		if (stoerrelse() == 0) { 	// hvis lista er tom og posisjonen er 0, legg til
			if (!(pos == 0)) {
				throw new UgyldigListeIndeks(pos);
			}
			start = new Node(x);
		} else if (pos < 0 || pos > stoerrelse()){ 	// hvis prøver å legge til utenfor endepunktene
			throw new UgyldigListeIndeks(pos);
		} else if (pos == 0) { 		// hvis lista ikke er tom, legg til foran
			Node nyNode = new Node(x);
			Node p = start;
			nyNode.neste = p;
			start = nyNode;
		} else { 					// legg til vilkårlig sted i lista
			Node p = start;
			for (int i = 0; i < pos-1; i++) { // posisjon foran pos
				p = p.neste;
			}
			Node nyNode = new Node(x);
			nyNode.neste = p.neste;
			p.neste = nyNode;
		}
	}

	// legger til helt sist i lista, helt til høyre
	public void leggTil(T x){
		if (start == null) {		// hvis lista er tom, legg til
			start = new Node(x);
		} else {					// hvis lista ikke er tom, legg til helt til høyre
			Node p = start;
			while (p.neste != null) {
				p = p.neste;
			}
			p.neste = new Node(x);
		}
	}

	// returnerer lengden av lista
	public int stoerrelse() {
		Node p = start;
		int n = 0;
		while (p != null) {
			p = p.neste;
			n++;
		}
		return n;
	}

	// Setter et element til x
	public void sett(int pos, T x) {
		if (pos < 0 || pos >= stoerrelse()){	// hvis pos er utenfor endepunktene
			throw new UgyldigListeIndeks(pos);
		}
		if (pos == 0) {					// hvis pos = 0
			if (start == null) { 		// men lista er tom
				throw new UgyldigListeIndeks(pos);
			}							// hvis lista ikke er tom
			start.data = x;				// sett første element til x
		} else {						// hvis pos != 0
			Node p = start;
			for (int i = 0; i < pos; i++) {
				p = p.neste;
			}
			p.data = x;					// setter elementet i pos til x
		}
	}

	// henter et element i posisjon pos uten å fjerne
	public T hent(int pos) {
		if (pos < 0 || pos >= stoerrelse()){	// hvis pos er utenfor endepunktene
			throw new UgyldigListeIndeks(pos);
		}
		if (start == null) {			// hvis lista er tom
			throw new UgyldigListeIndeks(pos);
		}
		if (pos == 0) {					// hent første element
			return start.data;
		} else {						// hvis pos != 0
			Node p = start;
			for (int i = 0; i < pos; i++) {
				p = p.neste;
			}
			return p.data;				// returnerer data til element i pos
		}
	}

	// fjerner elementet i posisjon pos
	public T fjern(int pos) {
		if (start == null) {
			throw new UgyldigListeIndeks(0);
		}
		if (pos < 0 || pos >= stoerrelse()){	// hvis pos er utenfor endepunktene
			throw new UgyldigListeIndeks(pos);
		}
		if (pos == 0) {					// fjerner første element
			T returneres = start.data;
			start = start.neste;
			return returneres;
		} else {						// fjerner element i posisjon pos
			Node p = start;
			for (int i = 0; i < pos-1; i++) {	// setter pointer før elementet som skal fjernes
				p = p.neste;
			}
			Node n = p.neste;
			p.neste = n.neste;
			return n.data;
		}
	}

	// fjerner første element, lengst til venstre
	public T fjern() {
		if (start == null) {			// hvis lista er tom
			throw new UgyldigListeIndeks(0);
		}
		T returneres = null;
		if (start != null) {			// fjerner elementet helt til venstre, første element
			returneres = start.data;
			start = start.neste;
		}
		return returneres;
	}
}
