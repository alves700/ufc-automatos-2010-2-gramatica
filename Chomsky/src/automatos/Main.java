package automatos;


import java.util.ArrayList;
public class Main {
	public static void main(String[] args) {
		
		Gramatica g = new Gramatica();
		
		ArrayList<Simbolo<?>> w = new ArrayList<Simbolo<?>>();
		Simbolo<String> t1 = new Simbolo<String>("(", true);
		Simbolo<String> t2 = new Simbolo<String>(")", true);
		w.add(t1);
		w.add(t1);
		w.add(t2);
		w.add(t2);
		w.add(t1);
		w.add(t2);
		//w.add(t2);
		//w.add(t1);
		
		g.addSimboloAlfabeto("(");
		g.addSimboloAlfabeto(")");

		//Cria o primeiro simbolo não terminal, 1:
		Simbolo<Integer> naoTerminal1 = g.gerarProximoSimboloNaoTerminal();
		g.simbInicial = naoTerminal1;
		g.addSimbNaoTerminal(naoTerminal1);

		//Regra: 1 -> 11
		Regra regra1 = new Regra(naoTerminal1);
		regra1.direita.add(naoTerminal1);
		regra1.direita.add(naoTerminal1);
		
		//Regra: 1 -> (1)
		Regra regra2 = new Regra(naoTerminal1);
		regra2.addSimboloTerminalDireita("(");
		regra2.addSimboloDireita(naoTerminal1); //Não terminal 1
		regra2.addSimboloTerminalDireita(")");
		
		//Regra: 1 -> e
		Regra regra3 = new Regra(naoTerminal1);
		regra3.addSimboloTerminalDireita("e");
		
		g.addRegra(regra1, regra2, regra3);
		
		System.out.println("Gramática original:");
		System.out.println(g.toString());
		System.out.println();
		System.out.println("Gramática na forma normal de Chomsky:");
		Chomsky.construirGramaticaChomsky(g);
		System.out.println(g.toString());
		
		System.out.println("Palavra pertence a gramatica? " + Chomsky.derivaPalavra(g, w));
		
		
		
	}
	
}
