package automatos.apd;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main {
	public static void main(String[] args) {
		try {
			GramaticaManipulator.getFromFile("gramaticaDeTeste.txt");
			// Estado q1 = new Estado("q1", true);
			// Estado q2 = new Estado("q2", false);
			// Estado q3 = new Estado("q3", false);
			// Estado q4 = new Estado("q4", true);
			//			
			// Transicao t1 = new Transicao(null, q2, new String[] {}, new
			// String[] {"$"});
			// Transicao t2 = new Transicao("0", q2, new String[] {}, new
			// String[] {"0"});
			// Transicao t3 = new Transicao("1", q3, new String[] {"0"}, new
			// String[] {});
			// Transicao t4 = new Transicao("1", q3, new String[] {"0"}, new
			// String[] {});
			// Transicao t5 = new Transicao(null, q4, new String[] {"$"}, new
			// String[] {});
			//			
			// q1.addTransicao(t1);
			// q2.addTransicao(t2);
			// q2.addTransicao(t3);
			// q3.addTransicao(t4);
			// q3.addTransicao(t5);
			//			
			// AutomatoPilha apd2 = new AutomatoPilha();
			// apd2.estadoInicial = q1;
			// apd2.addEstado(q1, q2, q3, q4);
			// System.out.println(apd2);

			AutomatoPilha apd = AutomatoPilha.carregarDeArquivo("apd.txt");
			System.out.println("APD:");
			System.out.println(apd);
			
			String string = lerStringTeste("string.txt");
			
			System.out.println("Teste com a string:\n" + string + "\n");
			System.out.print("O APD deriva a string? "
					+ apd.derivaString(string));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static String lerStringTeste(String caminho) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(caminho));
		StringBuilder strBuilder = new StringBuilder();
		String linha;
		while ((linha = reader.readLine()) != null) {
			strBuilder.append(linha);
		}
		reader.close();
		return strBuilder.toString();
	}
}
