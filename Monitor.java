import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Lock;

class Monitor {

	Lock laas = new ReentrantLock();

	Liste<String> utveiLista;

	public Monitor() {
		utveiLista = new Lenkeliste<String>();
	}

	public void leggILista(String noe) {
		laas.lock();
		try{
			utveiLista.leggTil(noe);
		}
		finally {
			laas.unlock();
		}
	}

	public Liste<String> hentUtveiLista() {
		laas.lock();
		try{
			return utveiLista;
		}
		finally {
			laas.unlock();
		}
	}
}
