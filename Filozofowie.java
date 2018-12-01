import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class Filozofowie{
	
	static int liczbaFilozofow = 5;
	
	public static void main(String[] args){
		Filozof[] filozofowie = new Filozof[liczbaFilozofow];
		Widelec[] widelce = new Widelec[liczbaFilozofow];
		Thread[] f = new Thread[liczbaFilozofow];
		for (int i = 0; i<liczbaFilozofow; i++) {
			widelce[i] = new Widelec(i);
		}
		for (int i = 0; i<liczbaFilozofow; i++) {
			filozofowie[i] = new Filozof(i, widelce[i], widelce[(i+1)%liczbaFilozofow]);
			f[i] = new Thread(filozofowie[i]);
			f[i].start();
		}
	}

}

class Widelec{
	ReentrantLock blokada = new ReentrantLock(); //pozwala na dostep tylko jednemu filozofowi naraz
	int idWidelca;
	public Widelec (int id) {
		this.idWidelca=id;
	}
	public boolean podnies(Filozof filozof) throws InterruptedException{
		if(blokada.tryLock(1, TimeUnit.SECONDS)) {
			System.out.println("Filozof " + filozof.idFilozofa + " podniosl widelec " + idWidelca);
			return true;
		}
		return false;
	}
	public void odloz (Filozof filozof){
		blokada.unlock();
		System.out.println("Filozof " + filozof.idFilozofa + " odlozyl widelec " + idWidelca);
	}
}

class Filozof implements Runnable{
	int idFilozofa;
	Widelec lewy;
	Widelec prawy;
	public Filozof(int id, Widelec lewy, Widelec prawy) {
		this.idFilozofa=id;
		this.lewy=lewy;
		this.prawy=prawy;
	}
	public void run() {
		while(true) {
			try {
				System.out.println("Filozof " + idFilozofa + " mysli.");
				Thread.sleep((int)(2000*Math.random()));
			}catch (InterruptedException e){}
			try {
				if (lewy.podnies(this)) {
					if (prawy.podnies(this)) {
						System.out.println("Filozof " + idFilozofa + " je.");
						Thread.sleep((int)(1000*Math.random()));
						prawy.odloz(this);
					}
				lewy.odloz(this);
				}
			}catch(InterruptedException e) {}
		}
	}
}

