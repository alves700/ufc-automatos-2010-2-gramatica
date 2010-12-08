package automatos;


import java.util.ArrayList;
public class Main {
	public static void main(String[] args) {
		
		Gramatica g = new Gramatica();
		g.addSimboloAlfabeto("+");
		g.addSimboloAlfabeto("*");
		g.addSimboloAlfabeto("(");
		g.addSimboloAlfabeto(")");
		g.addSimboloAlfabeto("id");
		Simbolo<Integer> E = g.gerarProximoSimboloNaoTerminal();
		g.addSimbNaoTerminal(E);
		Simbolo<Integer> T = g.gerarProximoSimboloNaoTerminal();
		g.addSimbNaoTerminal(T);
		Simbolo<Integer> F = g.gerarProximoSimboloNaoTerminal();
		g.addSimbNaoTerminal(F);
		
		g.simbInicial = E;
		
		Regra r1 = new Regra(E);
		r1.addSimboloDireita(E);
		r1.addSimboloTerminalDireita("+");
		r1.addSimboloDireita(T);
		
		Regra r2 = new Regra(E);
		r2.addSimboloDireita(T);
		
		Regra r3 = new Regra(T);
		r3.addSimboloDireita(T);
		r3.addSimboloTerminalDireita("*");
		r3.addSimboloDireita(F);
		
		Regra r4 = new Regra(T);
		r4.addSimboloDireita(F);
		
		Regra r5 = new Regra(F);
		r5.addSimboloTerminalDireita("(");
		r5.addSimboloDireita(E);
		r5.addSimboloTerminalDireita(")");
		
		Regra r6 = new Regra(F);
		r6.addSimboloTerminalDireita("id");
		
		Regra r7 = new Regra(F);
		r7.addSimboloTerminalDireita("id");
		r7.addSimboloTerminalDireita("(");
		r7.addSimboloDireita(E);
		r7.addSimboloTerminalDireita(")");
		
		g.addRegra(r1,r2,r3,r4,r5,r6,r7);
		
		Automato a = new Automato(g);
		System.out.println("ANTES DE CHAMAR O METODO:");
		System.out.println(g.toString());
		
		a.Heuristica1();
		
		System.out.println("DEPOIS DE CHAMADO:");
		System.out.println(a.g.toString());
		
		
		
		/*
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
		*/
		
		
	}
	
}
