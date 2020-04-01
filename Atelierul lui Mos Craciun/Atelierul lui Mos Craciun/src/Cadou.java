import java.util.Random;

public class Cadou {
	String nume;
	String tipCadou[] = {"papusa", "robot", "figurina", "gadget" };
	Random random = new Random();

	public Cadou() {
		nume = tipCadou[random.nextInt(4)];
	}
}
