import java.awt.*;
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

public class Fabrica {
	LinkedList<Point> fabrica;
	int N;
	int nrFabrica;
	int nrElfi;
	Elf[] elfi;
	ConcurrentLinkedQueue<String> cadouri;
	ReentrantLock zavor;
	Semaphore semReni = new Semaphore(1);
	Random rand = new Random();

	public Fabrica(int N, int nr) {
		this.N = N;
		this.nrFabrica = nr;
		this.zavor = new ReentrantLock();
		int nrElfi = 3;
		// int nrElfi = rand.nextInt(N / 2);
		this.nrElfi = nrElfi;
		elfi = new Elf[nrElfi];
		fabrica = new LinkedList<Point>();
		cadouri = new ConcurrentLinkedQueue<String>();

		for (int k = 0; k < nrElfi; k++) {
			boolean creat = false;
			Point poz = new Point();
			while (!creat) {
				poz.x = rand.nextInt(N);
				poz.y = rand.nextInt(N);
				boolean pozValida = true;
				for (int l = 0; l < k; l++) {
					if ((poz.y == elfi[l].pozitie.y && poz.x == elfi[l].pozitie.x)) {
						pozValida = false;
						break;
					}
				}
				if (pozValida) {
					try {
						elfi[k] = new Elf(this, poz, k);
						fabrica.add(poz);
					} catch (NullPointerException e) {
						e.printStackTrace();
					} finally {
						creat = true;
						System.out.println("A fost creat elful " + k + " in fabrica " + this.nrFabrica + " pe linia "
								+ poz.y + " coloana " + poz.x);
						elfi[k].start();
					}
					try {
						Thread.sleep(rand.nextInt(1) + 0);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}

		}
	}

	public synchronized void raporteaza(Elf elf, Cadou cadou) {
		zavor.lock();
		System.out.println(
				"Elful " + elf.nrElf + " din fabrica " + elf.fabrica.nrFabrica + " a creat cadoul " + cadou.nume);
		zavor.unlock();
	}

	public synchronized void raporteazaToti() {
		zavor.lock();
		for (int i = 0; i < fabrica.size(); i++) {
			System.out.println("In fabrica " + this.nrFabrica + " elful " + elfi[i].nrElf + " este pe linia "
					+ elfi[i].pozitie.y + ", coloana " + elfi[i].pozitie.x);
		}
		zavor.unlock();
	}

	boolean mutaSus(Elf elf, Cadou cadou) {
		int i;
		Point poz = new Point();
		poz.x = elf.pozitie.x;
		poz.y = elf.pozitie.y - 1;
		if (elf.pozitie.y < 1) {
			return false;
		}
		if (fabrica.indexOf(poz) != -1) {
			return false;
		}
		return true;
	}

	boolean mutaJos(Elf elf, Cadou cadou) {
		int i;
		Point poz = new Point();
		poz.x = elf.pozitie.x;
		poz.y = elf.pozitie.y + 1;
		if (elf.pozitie.y > N - 2) {
			return false;
		}
		if (fabrica.indexOf(poz) != -1) {
			return false;
		}
		return true;
	}

	boolean mutaStanga(Elf elf, Cadou cadou) {
		int i;
		Point poz = new Point();
		poz.x = elf.pozitie.x - 1;
		poz.y = elf.pozitie.y;
		if (elf.pozitie.x < 1) {
			return false;
		}
		if (fabrica.indexOf(poz) != -1) {
			return false;
		}
		return true;
	}

	boolean mutaDreapta(Elf elf, Cadou cadou) throws NullPointerException {
		int i;
		Point poz = new Point();
		poz.x = elf.pozitie.x + 1;
		poz.y = elf.pozitie.y;
		if (elf.pozitie.x > N - 2) {
			return false;
		}
		if (fabrica.indexOf(poz) != -1) {
			return false;
		}
		return true;
	}

	public synchronized void creazaCadou(Elf elf, Cadou cadou) {
		zavor.lock();
		if (!(mutaDreapta(elf, cadou) || mutaJos(elf, cadou) || mutaStanga(elf, cadou) || mutaSus(elf, cadou))) {
			try {
				Thread.sleep(rand.nextInt(41) + 10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} else {
			boolean mutareValida = false;
			while (!mutareValida) {
				int r = rand.nextInt(4);
				if (r == 0) {
					mutareValida = mutaSus(elf, cadou);
					if (mutareValida) {
						fabrica.remove(elf.pozitie);
						cadouri.add(cadou.nume);
						elf.pozitie.y = elf.pozitie.y - 1;
						fabrica.add(elf.pozitie);
					}
				} else if (r == 1) {
					mutareValida = mutaDreapta(elf, cadou);
					if (mutareValida) {
						fabrica.remove(elf.pozitie);
						cadouri.add(cadou.nume);
						elf.pozitie.x = elf.pozitie.x + 1;
						fabrica.add(elf.pozitie);
					}
				} else if (r == 2) {
					mutareValida = mutaJos(elf, cadou);
					if (mutareValida) {
						fabrica.remove(elf.pozitie);
						cadouri.add(cadou.nume);
						elf.pozitie.y = elf.pozitie.y + 1;
						fabrica.add(elf.pozitie);
					}
				} else {
					mutareValida = mutaStanga(elf, cadou);
					if (mutareValida) {
						fabrica.remove(elf.pozitie);
						cadouri.add(cadou.nume);
						elf.pozitie.x = elf.pozitie.x - 1;
						fabrica.add(elf.pozitie);
					}
				}
			}
			zavor.unlock();
			try {
				Thread.sleep(30);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public String impacheteazaCadou(Ren ren) {
		String cadou = new String();
		semReni.tryAcquire();
		cadou = cadouri.poll();
		semReni.release();
		 System.out.println("Renul "+ren.nrRen+" a impachetat cadoul "+cadou+" din fabrica "+this.nrFabrica);
		return cadou;
	}
}
