import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;


public class Atelier {
	public static void main(String[] args) {

		Random rand = new Random();
		int nrFabrici = 3;
		int nrReni = 3;

		Ren[] reni = new Ren[nrReni];
		int n;
		Raport raportarefabrica;
		Fabrica[] fab = new Fabrica[nrFabrici];
		for (int i = 0; i < nrFabrici; i++) {
			n = 6;

			fab[i] = new Fabrica(n, i);
			raportarefabrica = new Raport(fab[i]);
			raportarefabrica.start();
		}
		for (int i = 0; i < nrReni; i++) {
			reni[i] = new Ren(fab, i, nrFabrici);
			reni[i].start();
		}

		try {
			int serverPort = 6789;
			ServerSocket listenSocket = new ServerSocket(serverPort);

			while (true) {
				Socket clientSocket = listenSocket.accept();
				new MosCraciun(clientSocket);
			}
		} catch (IOException e) {
			System.out.println("Listen socket:" + e.getMessage());
		}
	}
}
