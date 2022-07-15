import java.util.NoSuchElementException;

public class Pilha<Item> {
    /*
    Classe que abstrai o conceito de pilha. Implementada pelo método dos "resizing arrays".
    */

    private Item[] pilha;
    private int top; // guarda o índice (sendo que o primeiro é zero) do último elemento da pilha
    private int size; // guarda o tamanho do vetor dinâmico

    public Pilha() {
        pilha = (Item[]) new Object[1];
        top = -1;
        size = 1;
    }

    public void push(Item item) {
        /*
        Insere, na pilha, o elemento dado como argumento.
        */
        if (size <= top + 1) {
            resize(2*size);
        }
        pilha[++top] = item;
    }

    public Item pop() {
        /*
        Retira e retorna o elemento no topo da pilha.
        */
        if (top >= 0)
            return pilha[top--];
        else
            throw new NoSuchElementException("Pilha underflow");
    }

    public Item top() {
        /*
        Retorna o elemento no topo da pilha.
        */
        if (top >= 0)
            return pilha[top];
        else
            throw new NoSuchElementException("Pilha underflow");
    }

    public boolean isEmpty() {
        /*
        Retorna true quando a pilha está vazia. Caso contrário, retorna false.
        */
        return (top < 0);
    }

    public int size() {
        /*
        Retorna a quantidade de elementos na pilha.
        */
        return (top + 1);
    }

    private void resize(int new_size) {
        /*
        Função auxiliar que realiza o processo de expansão do tamanho da pilha (pela criação de um novo array
        de novo tamanho e cópia dos elementos do antigo), sobretudo quando o número de elementos excede o ta-
        manho atual do array que representa a pilha.
        */
        size = new_size;
        Item[] new_pilha = (Item[]) new Object[new_size];
        for (int i = 0; i <= top; i++)
            new_pilha[i] = pilha[i];
        pilha = new_pilha;
    }
}