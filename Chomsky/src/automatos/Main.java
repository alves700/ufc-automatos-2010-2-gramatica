package automatos;

public class Main {
	public static void main(String[] args) {
		Gramatica g = new Gramatica();
		
		g.addSimboloAlfabeto("(");
		g.addSimboloAlfabeto(")");

		//Cria o primeiro simbolo, 1, não terminal:
		Simbolo<Integer> naoTerminal1 = g.gerarProximoSimboloNaoTerminal();
		g.simbInicial = naoTerminal1;
		g.addSimbNaoTerminal(naoTerminal1);

		//1 -> 11
		Regra regra1 = new Regra(naoTerminal1);
		regra1.direita.add(naoTerminal1);
		regra1.direita.add(naoTerminal1);
		
		//1 -> (1)
		Regra regra2 = new Regra(naoTerminal1);
		regra2.addSimboloTerminalDireita("(");
		regra2.addSimboloDireita(naoTerminal1); //Não terminal 1
		regra2.addSimboloTerminalDireita(")");
		
		//1 -> e
		
		Regra regra3 = new Regra(naoTerminal1);
		regra3.addSimboloTerminalDireita("e");
		
		g.addRegra(regra1, regra2, regra3);
		
		System.out.println("Gramática original:");
		System.out.println(g.toString());
		System.out.println();
		System.out.println("Gramática na forma normal d Chomsky:");
		Chomsky.construirGramaticaChomsky(g);
		System.out.println(g.toString());
	}
}
