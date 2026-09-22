public class ArvoreAVL {

    static class No {
        int chave, altura;
        No esq, dir;

        No(int chave) {
            this.chave = chave;
        }
    }

    private No raiz;

    private int altura(No n) {
        return n == null ? -1 : n.altura;
    }

    private int fb(No n) {
        return n == null ? 0 : altura(n.esq) - altura(n.dir);
    }

    private void atualizar(No n) {
        n.altura = 1 + Math.max(altura(n.esq), altura(n.dir));
    }

    private No rotacaoDireita(No y) {
        No x = y.esq;
        y.esq = x.dir;
        x.dir = y;
        atualizar(y);
        atualizar(x);
        return x;
    }

    private No rotacaoEsquerda(No x) {
        No y = x.dir;
        x.dir = y.esq;
        y.esq = x;
        atualizar(x);
        atualizar(y);
        return y;
    }

    private No balancear(No n) {
        atualizar(n);
        if (fb(n) > 1) {
            if (fb(n.esq) < 0) n.esq = rotacaoEsquerda(n.esq);
            return rotacaoDireita(n);
        }
        if (fb(n) < -1) {
            if (fb(n.dir) > 0) n.dir = rotacaoDireita(n.dir);
            return rotacaoEsquerda(n);
        }
        return n;
    }

    public void inserir(int chave) {
        raiz = inserir(raiz, chave);
    }

    private No inserir(No n, int chave) {
        if (n == null) return new No(chave);
        if (chave < n.chave) n.esq = inserir(n.esq, chave);
        else if (chave > n.chave) n.dir = inserir(n.dir, chave);
        else return n;
        return balancear(n);
    }

    public void remover(int chave) {
        raiz = remover(raiz, chave);
    }

    private No remover(No n, int chave) {
        if (n == null) return null;
        if (chave < n.chave) n.esq = remover(n.esq, chave);
        else if (chave > n.chave) n.dir = remover(n.dir, chave);
        else if (n.esq == null || n.dir == null) n = n.esq != null ? n.esq : n.dir;
        else {
            No sucessor = n.dir;
            while (sucessor.esq != null) sucessor = sucessor.esq;
            n.chave = sucessor.chave;
            n.dir = remover(n.dir, sucessor.chave);
        }
        return n == null ? null : balancear(n);
    }

    public void mostrar() {
        mostrar(raiz, 0);
        System.out.println();
    }

    private void mostrar(No n, int nivel) {
        if (n == null) return;
        mostrar(n.dir, nivel + 1);
        System.out.println(" ".repeat(nivel * 4) + n.chave + " (FB=" + fb(n) + ")");
        mostrar(n.esq, nivel + 1);
    }

    public static void main(String[] args) {
        ArvoreAVL arvore = new ArvoreAVL();
        for (int chave : new int[] {55, 26, 29, 13, 12, 11, 16, 1, 5, 29, -15, 4, 16, 8, 4, 5, 3, 1312, 100, 88}) {
            arvore.inserir(chave);
            arvore.mostrar();
        }
        for (int chave : new int[] {4, 29, 100, 5, 15, 16, 55}) {
            arvore.remover(chave);
            arvore.mostrar();
        }
    }
}
