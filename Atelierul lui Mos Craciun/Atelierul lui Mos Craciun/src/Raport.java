
public class Raport extends Thread {
	Fabrica fabrica;

	Raport (Fabrica fabrica) {
        this.fabrica = fabrica;
    }

	public void run() {
		while (true) {
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			fabrica.raporteazaToti();
		}
	}

}
