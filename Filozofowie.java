import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class Filozofowie{
	
	int liczbaFilozofow = 5;
	int liczbaWidelcow = liczbaFilozofow;
	
	public static void main(String[] args){
		
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
	int liczbaTurJedzenia;
	public Filozof(int id) {
		this.idFilozofa=id;
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
						liczbaTurJedzenia++;
						Thread.sleep((int)(1000*Math.random()));
						prawy.odloz(this);
					}
				lewy.odloz(this);
				}
			}catch(InterruptedException e) {}
		}
	}
}

