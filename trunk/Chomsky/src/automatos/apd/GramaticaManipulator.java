package automatos.apd;

import java.io.BufferedReader;
import java.io.FileReader;

import automatos.Gramatica;
import automatos.Regra;
import automatos.Simbolo;

public class GramaticaManipulator {
	// forma do txt
	// 1 linha V={. . . . .} onde os . são os elementos n terminas
	// 2 linha A={. .} simbolos terminais
	// 3 linha G=X onde X é a regra inicial
	// 4..n linha R->A a conjunto de regras um Y antes representa que o estado é
	// um número com mais de dois algorismo ex:99 cada item é separado por " "
	public static Gramatica getFromFile(final String caminho) {
		BufferedReader reader = null;
		Gramatica gramatica = null;
		try {
			int lineCount = 1;
			gramatica = new Gramatica();
			reader = new BufferedReader(new FileReader(caminho));
			String linha = null;
			String objects[] = null;
			while ((linha = reader.readLine()) != null) {
				switch (lineCount) {
				case 1:
					linha = linha.substring(3, linha.length() - 1);
					objects = linha.split(" ");
					for (String string : objects) {
						gramatica.addSimbNaoTerminal(Integer.parseInt(string));
					}
					break;
				case 2:
					linha = linha.substring(3, linha.length() - 1);
					objects = linha.split(" ");
					for (String string : objects) {
						gramatica.addSimboloAlfabeto(string);
					}
					break;
				case 3:
					gramatica.simbInicial = new Simbolo<Integer>(Integer
							.parseInt(linha.charAt(2) + ""), false);
					break;
				default:
					objects = linha.split("->");
					Regra regra = new Regra(new Simbolo<Integer>(Integer
							.parseInt(objects[0]), false));
					boolean alreadyFind = false;
					String objects2[] = objects[1].split(" ");
					
					for (String string : objects2) {
						alreadyFind = false;
						for (Simbolo<String> alfabeto : gramatica.alfabeto) {
							if (alfabeto.valor.equals(string)) {
								alreadyFind = true;
								break;
							}
						}
						if (alreadyFind) {
							regra.addSimboloTerminalDireita(string);
						} else {
							regra.addSimboloNaoTerminalDireita(Integer
									.parseInt(string));
						}
					}
					gramatica.addRegra(regra);
					break;
				}
				lineCount++;
			}
		} catch (Exception e) {
		}
		return gramatica;
	}
}
