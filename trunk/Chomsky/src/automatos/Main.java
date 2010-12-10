package automatos;

import java.util.ArrayList;

public class Main {

   public static void main(String[] args) {

      Gramatica g = new Gramatica();
      g.addSimboloAlfabeto("C");
      g.addSimboloAlfabeto("C'");
      g.addSimboloAlfabeto("C''");
      g.addSimboloAlfabeto("Q");
      g.addSimboloAlfabeto("E");
      g.addSimboloAlfabeto("O");
      g.addSimboloAlfabeto("V");
      g.addSimboloAlfabeto("N");
      g.addSimboloAlfabeto("S");
      g.addSimboloAlfabeto("ELSE");
      g.addSimboloAlfabeto("A");
      g.addSimboloAlfabeto("A'");
      g.addSimboloAlfabeto("A''");
      g.addSimboloAlfabeto("(");
      g.addSimboloAlfabeto(")");
      g.addSimboloAlfabeto("{");
      g.addSimboloAlfabeto("}");
      g.addSimboloAlfabeto("[");
      g.addSimboloAlfabeto("]");
      g.addSimboloAlfabeto("while");
      g.addSimboloAlfabeto("if");
      //g.addSimboloAlfabeto("else");
      g.addSimboloAlfabeto("x");
      g.addSimboloAlfabeto("y");
      g.addSimboloAlfabeto("0");
      g.addSimboloAlfabeto("1");
      g.addSimboloAlfabeto("2");

      Simbolo<Integer> C = g.gerarProximoSimboloNaoTerminal();
      g.addSimbNaoTerminal(C);

      Simbolo<Integer> C1 = g.gerarProximoSimboloNaoTerminal();
      g.addSimbNaoTerminal(C1);

      Simbolo<Integer> C2 = g.gerarProximoSimboloNaoTerminal();
      g.addSimbNaoTerminal(C2);

      Simbolo<Integer> Q = g.gerarProximoSimboloNaoTerminal();
      g.addSimbNaoTerminal(Q);

      Simbolo<Integer> E = g.gerarProximoSimboloNaoTerminal();
      g.addSimbNaoTerminal(E);

      Simbolo<Integer> O = g.gerarProximoSimboloNaoTerminal();
      g.addSimbNaoTerminal(O);

      Simbolo<Integer> V = g.gerarProximoSimboloNaoTerminal();
      g.addSimbNaoTerminal(V);

      Simbolo<Integer> N = g.gerarProximoSimboloNaoTerminal();
      g.addSimbNaoTerminal(N);

      Simbolo<Integer> S = g.gerarProximoSimboloNaoTerminal();
      g.addSimbNaoTerminal(S);

      Simbolo<Integer> ELSE = g.gerarProximoSimboloNaoTerminal();
      g.addSimbNaoTerminal(ELSE);

      Simbolo<Integer> A = g.gerarProximoSimboloNaoTerminal();
      g.addSimbNaoTerminal(A);

      Simbolo<Integer> A1 = g.gerarProximoSimboloNaoTerminal();
      g.addSimbNaoTerminal(A1);

      Simbolo<Integer> A2 = g.gerarProximoSimboloNaoTerminal();
      g.addSimbNaoTerminal(A2);

      // C-> (C'
      Regra r1 = new Regra(C);
      r1.addSimboloTerminalDireita("(");
      r1.addSimboloDireita(C1);

      // C'-> E Q E)
      Regra r2 = new Regra(C1);
      r2.addSimboloDireita(E);
      r2.addSimboloDireita(Q);
      r2.addSimboloDireita(E);
      r2.addSimboloTerminalDireita(")");

      //C'-> C C''
      Regra r3 = new Regra(C1);
      r3.addSimboloDireita(C);
      r3.addSimboloDireita(C2);

      //C''-> && C)
      Regra r4 = new Regra(C2);
      r4.addSimboloTerminalDireita("&&");
      r4.addSimboloDireita(C);

      //C''-> || C)
      Regra r5 = new Regra(C2);
      r5.addSimboloTerminalDireita("||");
      r5.addSimboloDireita(C);

      //Q-> ==
      Regra r6 = new Regra(Q);
      r6.addSimboloTerminalDireita("==");

      //Q-> !=
      Regra r7 = new Regra(Q);
      r7.addSimboloTerminalDireita("!=");

      //Q-> >
      Regra r8= new Regra(Q);
      r8.addSimboloTerminalDireita(">");

      //Q-> <
      Regra r9= new Regra(Q);
      r9.addSimboloTerminalDireita("<");





      /*

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
