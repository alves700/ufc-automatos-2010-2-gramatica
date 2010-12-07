package automatos.apd;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
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
		Pattern transitionPattern = Pattern.compile("\\(([^,]*),([^,]*),([^,]*)\\),\\(([^,]*),([^,]*)\\)");
		
		try {
			reader = new BufferedReader(new FileReader(caminho));
			EstadoLeitura estadoLeitura = EstadoLeitura.ESTADO_INICIAL;
			String linha = null;
			while ((linha = reader.readLine()) != null) {
				EstadoLeitura proxEstadoLeitura = EstadoLeitura.getByIdentificador(linha);
				if (proxEstadoLeitura != null) {
					estadoLeitura = proxEstadoLeitura;
				} else {
					switch (estadoLeitura) {
					case ESTADO_INICIAL:
						Estado estadoInicial = new Estado(linha.trim(), false);
						automato.estadoInicial = estadoInicial;
						automato.estados.add(estadoInicial);
						break;
					case ESTADOS_FINAIS:
						Estado estadoFinal = obterOuAdicionarEstado(automato, linha);
						estadoFinal.isFinal = true;
						break;
					case TRANSICOES:
						Matcher linhaMatcher = transitionPattern.matcher(linha.replaceAll("  ", " "));
						if (!linhaMatcher.matches()) {
							throw new Exception();
						}
						Estado estadoAnterior = obterOuAdicionarEstado(automato, linhaMatcher.group(1));
						String simbolo = linhaMatcher.group(2);
						String[] topoPilhaEsperado = linhaMatcher.group(3).split(" ");
						Estado estadoDestino = obterOuAdicionarEstado(automato, linhaMatcher.group(4));
						String[] adicionarTopoPilha = linhaMatcher.group(5).split(" ");
						estadoAnterior.addTransicao(new Transicao(simbolo, estadoDestino, topoPilhaEsperado, adicionarTopoPilha));
						break;
					}
				}
			}
			automato = new AutomatoPilha();
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
		String strEstadoInicial = "Estado inicial: " + estadoInicial.toString() + "\n";
		String strEstadosFinais = "Estados finais: ";
		
		Collections.sort(estados);
		Iterator<Estado> estadosIt = estados.iterator();
		while (estadosIt.hasNext()) {
			Estado estado = (Estado) estadosIt.next();
			if (estado.isFinal) {
				strEstadosFinais += estado + " ";
			} else {
				strEstados += estado + " ";
			}
		}
		strEstados += "\n";
		strEstadosFinais += "\n";
		
		String strTransicoes = "Transições: ";
		estadosIt = estados.iterator();
		while (estadosIt.hasNext()) {
			Estado estado = (Estado) estadosIt.next();
			for (Transicao transicao : estado.transicoes) {
				strTransicoes += transicao + "\n";
			}
		}
		return strEstados + strEstadoInicial + strEstadosFinais + strTransicoes;
	}

	private static enum EstadoLeitura {
		ESTADO_INICIAL("#estado_inicial"),
		ESTADOS_FINAIS("#estados_finais"),
		TRANSICOES("#transicoes");
		
		public final String identificador;
		EstadoLeitura(String identificador) {
			this.identificador = identificador;
		}
		
		public static EstadoLeitura getByIdentificador(String identificador) {
			for (EstadoLeitura estado : values()) {
				if (identificador.equals(estado.identificador)) {
					return estado;
				}
			}
			return null;
		}
	}
}
