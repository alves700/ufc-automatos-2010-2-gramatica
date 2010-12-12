package automatos.apd;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AutomatoPilha {
	public List<Estado> estados = new ArrayList<Estado>();
	public Estado estadoInicial;
	
	public void addEstado(Estado... estado) {
		for (Estado e : estado) {
			estados.add(e);
		}
	}
	
	public static AutomatoPilha carregarDeArquivo(String caminho) throws Exception {
		AutomatoPilha automato = new AutomatoPilha();
		BufferedReader reader = null;
		Pattern transitionPattern = Pattern.compile("\\(([^?]+)\\?([^?]*)\\?([^?]*)\\)\\(([^?]+)\\?([^?]*)\\)");
		
		try {
			reader = new BufferedReader(new FileReader(caminho));
			SecaoLeitura secaoLeitura = SecaoLeitura.SECAO_ESTADO_INICIAL;
			String linha = null;
			while ((linha = reader.readLine()) != null) {
				linha = linha.replaceAll(" ", "");
				SecaoLeitura proxSecaoLeitura = SecaoLeitura.getByIdentificador(linha);
				if (proxSecaoLeitura != null) {
					secaoLeitura = proxSecaoLeitura;
				} else {
					switch (secaoLeitura) {
					case SECAO_ESTADO_INICIAL:
						Estado estadoInicial = new Estado(linha.trim(), false);
						automato.estadoInicial = estadoInicial;
						automato.estados.add(estadoInicial);
						break;
					case SECAO_ESTADOS_FINAIS:
						Estado estadoFinal = obterOuAdicionarEstado(automato, linha);
						estadoFinal.isFinal = true;
						break;
					case SECAO_TRANSICOES:
						Matcher linhaMatcher = transitionPattern.matcher(linha);
						if (!linhaMatcher.matches()) {
							throw new Exception();
						}
						Estado estadoAnterior = obterOuAdicionarEstado(automato, linhaMatcher.group(1));
						String simbolo = linhaMatcher.group(2);
						simbolo = simbolo.isEmpty() ? null : simbolo;
						String[] topoPilhaEsperado = linhaMatcher.group(3).split(" ");
						if (topoPilhaEsperado[0].isEmpty()) {
							topoPilhaEsperado = new String[0];
						}
						Estado estadoDestino = obterOuAdicionarEstado(automato, linhaMatcher.group(4));
						String[] adicionarTopoPilha = linhaMatcher.group(5).split(" ");
						if (adicionarTopoPilha[0].isEmpty()) {
							adicionarTopoPilha = new String[0];
						}
						estadoAnterior.addTransicao(new Transicao(simbolo, estadoDestino, topoPilhaEsperado, adicionarTopoPilha));
						break;
					}
				}
			}
		} finally {
			reader.close();
		}
		return automato;
	}
	
	private static Estado obterOuAdicionarEstado(AutomatoPilha automato, String label) {
		int indiceEstadoExistente = automato.estados.indexOf(new Estado(label, false));
		if (indiceEstadoExistente == -1) {
			Estado estado = new Estado(label, false);
			automato.addEstado(estado);
			return estado;
		} else {
			return automato.estados.get(indiceEstadoExistente);
		}
	}
	
	public boolean derivaString(String string) {
		Stack<String> pilha = new Stack<String>();
		List<String> topoPilha = new ArrayList<String>();
		Estado estadoAtual = estadoInicial;
		int posicaoStr = 0;
		do {
			String simboloAtual = null;
			if (posicaoStr < string.length()) {
				simboloAtual = Character.toString(string.charAt(posicaoStr));
			}
			if (simboloAtual == null && estadoAtual.isFinal) {
				return true;
			}
			//Realiza desempilhamentos até encontrar uma transição:
			Transicao transicao = null;
			do {
				transicao = procurarTransicao(estadoAtual.transicoes, simboloAtual, topoPilha);
				if (transicao == null) {
					if (pilha.isEmpty()) {
						return false;
					} else {
						topoPilha.add(pilha.pop());
					}
				}
			} while (transicao == null);
			topoPilha.clear();
			
			for (int i = transicao.adicionarTopoPilha.size() - 1; i >=0; i--) {
				pilha.push(transicao.adicionarTopoPilha.get(i));
			}			
			estadoAtual = transicao.estadoDestino;
			if (transicao.simbolo != null) {
				posicaoStr++;
			}
		} while (true);
	}
	
	public Transicao procurarTransicao(List<Transicao> transicoes, String simbolo, List<String> topoPilha) {
		//TODO RETIRAR VERIFICACAO DE UMBIGUIDADE!!!
		Transicao transicaoEncontrada = null;
		for (Transicao transicao : transicoes) {
			if (transicao.simbolo != null && !transicao.simbolo.equals(simbolo)) {
				continue;
			}
			if (transicao.topoPilhaEsperado.isEmpty() || transicao.topoPilhaEsperado.equals(topoPilha)) {
				if (transicaoEncontrada != null) {
					throw new RuntimeException("Ambiguidade!");
				}
				transicaoEncontrada = transicao;
			}
		}
		return transicaoEncontrada;
	}

	@Override
	public String toString() {
		String strEstados = "Estados: ";
		String strEstadoInicial = "Estado inicial: " + estadoInicial.label + "\n";
		String strEstadosFinais = "Estados finais: ";
		
		Collections.sort(estados);
		Iterator<Estado> estadosIt = estados.iterator();
		while (estadosIt.hasNext()) {
			Estado estado = (Estado) estadosIt.next();
			if (estado.isFinal) {
				strEstadosFinais += estado.label + " ";
			} else {
				strEstados += estado.label + " ";
			}
		}
		strEstados += "\n";
		strEstadosFinais += "\n";
		
		String strTransicoes = "Transições:\n";
		estadosIt = estados.iterator();
		while (estadosIt.hasNext()) {
			Estado estado = (Estado) estadosIt.next();
			for (Transicao transicao : estado.transicoes) {
				strTransicoes += "\t" + transicao.toString(estado) + "\n";
			}
		}
		return strEstados + strEstadoInicial + strEstadosFinais + strTransicoes;
	}

	private static enum SecaoLeitura {
		SECAO_ESTADO_INICIAL("#estado_inicial"),
		SECAO_ESTADOS_FINAIS("#estados_finais"),
		SECAO_TRANSICOES("#transicoes");
		
		public final String identificador;
		SecaoLeitura(String identificador) {
			this.identificador = identificador;
		}
		
		public static SecaoLeitura getByIdentificador(String identificador) {
			for (SecaoLeitura estado : values()) {
				if (identificador.equals(estado.identificador)) {
					return estado;
				}
			}
			return null;
		}
	}
}
