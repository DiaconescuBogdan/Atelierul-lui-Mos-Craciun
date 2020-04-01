import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;


public class Ren extends Thread {
	Fabrica fabrica[];
	int nrRen;
	int nrFabrici;
	static int fab = -1;
	Socket s;

	public Ren(Fabrica[] fab, int nr, int nrFabrici) {
		nrRen = nr;
		s = null;
		this.nrFabrici = nrFabrici;
		fabrica = new Fabrica[nrFabrici];
		for (int i = 0; i < nrFabrici; i++) {
			this.fabrica[i] = fab[i];
		}
	}

	public void run() {
		String cadou;
		Random random = new Random();
		while (true) {
			int i = random.nextInt(nrFabrici);
			if (fab == i) {
				try {
					Thread.sleep(20);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			fab = i;
			if (fabrica[i].cadouri.size() > 0) {
				cadou = fabrica[i].impacheteazaCadou(this);
				System.out.println(
						"Renul " + nrRen + " a impachetat cadoul " + cadou + " din fabrica " + fabrica[i].nrFabrica);

				try {
					int serverPort = 6789;
					s = new Socket(InetAddress.getLocalHost(), serverPort);
					DataOutputStream out = new DataOutputStream(s.getOutputStream());
					out.writeUTF(cadou);
				} catch (UnknownHostException e) {
					System.out.println("Socket:" + e.getMessage());
				} catch (EOFException e) {
					System.out.println("EOF:" + e.getMessage());
				} catch (IOException e) {
					System.out.println("readline:" + e.getMessage());
				} finally {
					if (s != null)
						try {
							s.close();
						} catch (IOException e) {
							System.out.println("close:" + e.getMessage());
						}
				}
			}
		}
	}
}
