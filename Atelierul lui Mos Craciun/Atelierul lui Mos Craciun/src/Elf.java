import java.awt.*;
public class Elf extends Thread {
	Point pozitie;
	Fabrica fabrica;
	int nrElf;
	Cadou cadou;

	public Elf(Fabrica fabrica, Point poz, int nr) {
		this.fabrica = fabrica;
		this.pozitie = poz;
		nrElf = nr;
	}

	public void run() {
		while (true) {
			cadou = new Cadou();
			fabrica.creazaCadou(this, cadou);
			fabrica.raporteaza(this, cadou);
			fabrica.raporteazaToti();
		}
	}
}
