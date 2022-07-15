import java.util.*;
import java.io.*;

public class Grafo {
    /*
    Classe que implementa um grafo, com os seguintes atributos:
        V = número de vértices
        E = número de arestas
        arestas = array com V listas ligadas, cada uma contendo os vértices adjacentes àquele contido no vetor
        palavras = array de Strings que apenas é diferente de null quando o grafo for composto por Strings
    */
    public int V;
    private int E;
    private NodeGrafo[] arestas;

    private class NodeGrafo {
        /*
        Classe auxiliar que implementa um nó utilizado como unidade básica para representar um grafo.
        */
        private int valor; // valor do vértice
        private NodeGrafo proximo;
        private int n_adjacentes; // guarda o número de vértices adjacentes (é diferente de -1 apenas se for um nó
                                  // diretamente contido no array de nós)

        private NodeGrafo(int valor) {
            this.valor = valor;
            proximo = null;
            n_adjacentes = -1;
        }

        private void add(int v) {
            NodeGrafo aux = this;
            for (int i = 0; i < this.n_adjacentes; i++)
                aux = aux.proximo;
            aux.proximo = new NodeGrafo(v);
            this.n_adjacentes++;
        }
    }

    public Grafo(int V) {
        this.V = V;
        E = 0;
        arestas = new NodeGrafo[V];
        for (int i = 0; i < V; i++) {
            arestas[i] = new NodeGrafo(i);
            arestas[i].n_adjacentes = 0;
        }
    }

    public void incluiArco(int i, int j) {
        arestas[i].add(j);
        E = E + 1;
    }

    public void dfsR(int v, boolean[] marked) {
        /*
        Método auxiliar que faz uma chamada recursiva da busca em profundidade em um grafo. Portanto, dado o
        índice de um vértice, percorre todos os seus adjacentes e chama a busca para cada um deles.
        */
        marked[v] = true;
        NodeGrafo aux = arestas[v];
        if (aux != null) {
            for (int i = 0; i < arestas[v].n_adjacentes; i++) {
                aux = aux.proximo;
                int w = aux.valor;
                if (!marked[w])
                    dfsR(w, marked);
            }
        }
    }
}