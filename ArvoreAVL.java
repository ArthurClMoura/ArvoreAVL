/**
 * Árvore AVL: árvore binária de busca que se rebalanceia após cada inserção e remoção.
 *
 * Fator de balanceamento: FB(no) = altura(esquerda) - altura(direita).
 * Altura de árvore vazia = -1. Altura de folha = 0.
 * A árvore só é AVL se todo nó tem FB em {-1, 0, +1}.
 *
 * Chave igual não é inserida de novo (busca binária clássica, sem repetidos).
 */
public class ArvoreAVL {

    static class No {
        int chave;
        No esq;
        No dir;
        int altura;

        No(int chave) {
            this.chave = chave;
        }
    }

    private No raiz;

    private int altura(No no) {
        return no == null ? -1 : no.altura;
    }

    /** FB = h(esquerda) - h(direita). */
    private int fb(No no) {
        return no == null ? 0 : altura(no.esq) - altura(no.dir);
    }

    private void atualizarAltura(No no) {
        no.altura = 1 + Math.max(altura(no.esq), altura(no.dir));
    }

    /** Rotação simples à direita. Caso LL: o nó ficou pesado à esquerda. */
    private No rotacaoDireita(No y) {
        No x = y.esq;
        No t2 = x.dir;
        x.dir = y;
        y.esq = t2;
        atualizarAltura(y);
        atualizarAltura(x);
        return x;
    }

    /** Rotação simples à esquerda. Caso RR: o nó ficou pesado à direita. */
    private No rotacaoEsquerda(No x) {
        No y = x.dir;
        No t2 = y.esq;
        y.esq = x;
        x.dir = t2;
        atualizarAltura(x);
        atualizarAltura(y);
        return y;
    }

    /**
     * Chamado em cada ancestral, na volta da recursão.
     * Se |FB| passar de 1, o filho do lado pesado decide a rotação.
     * Na inserção uma rotação já restaura as alturas. Na remoção pode haver outra mais acima.
     */
    private No balancear(No no) {
        atualizarAltura(no);
        int fator = fb(no);

        if (fator > 1) {
            if (fb(no.esq) < 0) {
                System.out.println("    RDD (dupla à direita) no nó " + no.chave
                        + "  FB=" + fator + ", filho esquerdo FB=" + fb(no.esq));
                no.esq = rotacaoEsquerda(no.esq);
            } else {
                System.out.println("    RSD (simples à direita) no nó " + no.chave
                        + "  FB=" + fator + ", filho esquerdo FB=" + fb(no.esq));
            }
            return rotacaoDireita(no);
        }

        if (fator < -1) {
            if (fb(no.dir) > 0) {
                System.out.println("    RDE (dupla à esquerda) no nó " + no.chave
                        + "  FB=" + fator + ", filho direito FB=" + fb(no.dir));
                no.dir = rotacaoDireita(no.dir);
            } else {
                System.out.println("    RSE (simples à esquerda) no nó " + no.chave
                        + "  FB=" + fator + ", filho direito FB=" + fb(no.dir));
            }
            return rotacaoEsquerda(no);
        }

        return no;
    }

    public void inserir(int chave) {
        System.out.println("\nInserir " + chave);
        raiz = inserir(raiz, chave);
        mostrar();
    }

    private No inserir(No no, int chave) {
        if (no == null) {
            return new No(chave);
        }
        if (chave < no.chave) {
            no.esq = inserir(no.esq, chave);
        } else if (chave > no.chave) {
            no.dir = inserir(no.dir, chave);
        } else {
            System.out.println("    " + chave + " já existe: não insere");
            return no;
        }
        return balancear(no);
    }

    public void remover(int chave) {
        System.out.println("\nRemover " + chave);
        raiz = remover(raiz, chave);
        mostrar();
    }

    private No remover(No no, int chave) {
        if (no == null) {
            System.out.println("    " + chave + " não está na árvore");
            return null;
        }
        if (chave < no.chave) {
            no.esq = remover(no.esq, chave);
        } else if (chave > no.chave) {
            no.dir = remover(no.dir, chave);
        } else if (no.esq == null || no.dir == null) {
            no = (no.esq != null) ? no.esq : no.dir;
        } else {
            No sucessor = minimo(no.dir);
            System.out.println("    " + no.chave + " tem dois filhos; sucessor = " + sucessor.chave);
            no.chave = sucessor.chave;
            no.dir = remover(no.dir, sucessor.chave);
        }
        if (no == null) {
            return null;
        }
        return balancear(no);
    }

    private No minimo(No no) {
        while (no.esq != null) {
            no = no.esq;
        }
        return no;
    }

    /** Direita em cima, nó no meio, esquerda embaixo. O número entre parênteses é o FB. */
    public void mostrar() {
        mostrar(raiz, 0);
    }

    private void mostrar(No no, int nivel) {
        if (no == null) {
            return;
        }
        mostrar(no.dir, nivel + 1);
        System.out.println(" ".repeat(nivel * 4) + no.chave + " (FB=" + fb(no) + ")");
        mostrar(no.esq, nivel + 1);
    }

    public static void main(String[] args) {
        ArvoreAVL arvore = new ArvoreAVL();
        int[] inserir = {55, 26, 29, 13, 12, 11, 16, 1, 5, 29, -15, 4, 16, 8, 4, 5, 3, 1312, 100, 88};
        for (int chave : inserir) {
            arvore.inserir(chave);
        }
        int[] remover = {4, 29, 100, 5, 15, 16, 55};
        for (int chave : remover) {
            arvore.remover(chave);
        }
    }
}
