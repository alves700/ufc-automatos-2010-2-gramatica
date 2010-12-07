package automatos.apd;

public class Main {
	public static void main(String[] args) {
		Estado q1 = new Estado("q1", true);
		Estado q2 = new Estado("q2", false);
		Estado q3 = new Estado("q3", false);
		Estado q4 = new Estado("q4", true);
		
		Transicao t1 = new Transicao(null, q2,
				new String[] {}, new String[] {"$"});
		Transicao t2 = new Transicao("0", q2,
				new String[] {}, new String[] {"0"});
		Transicao t3 = new Transicao("1", q3,
				new String[] {"0"}, new String[] {});
		Transicao t4 = new Transicao("1", q3,
				new String[] {"0"}, new String[] {});
		Transicao t5 = new Transicao(null, null,
				new String[] {"$"}, new String[] {});
		
		q1.addTransicao(t1);
		q2.addTransicao(t2);
		q2.addTransicao(t3);
		q3.addTransicao(t4);
		q3.addTransicao(t5);
		
		AutomatoPilha apd = new AutomatoPilha();
		apd.estadoInicial = q1;
		apd.addEstado(q1, q2, q3, q4);
		
		System.out.println(apd.derivaString("000111"));
	}
}
