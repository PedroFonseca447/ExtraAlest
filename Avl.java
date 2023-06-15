import java.io.FileWriter;
import java.io.IOException;

public class Avl {

    private class Nodo {
        Integer elemento;
        Nodo father;
        Nodo right;
        Nodo left;
        public int altura;
        public Nodo(Integer e) {
            this.elemento = e;
            father = null;
            right = null;
            left = null;
            altura=0;
        }
    }

   public Nodo root;
   public int count;


    public Avl(){
        root=null;
        count=0;
    }
    

    public boolean addRoot(Integer e){
        Nodo aux = new Nodo(e);

        if(root != null){
            return false;
        }
        
        root=aux;
        count++;
        return true;
    }

     public void add(Integer element) {
        root = add(root, element, null);
        count++;
    }

    private Nodo add(Nodo n, Integer element, Nodo father) {
        if (n == null) { // insere
            Nodo aux = new Nodo(element);
            aux.father = father;
            return aux;
        }

        if (n.elemento.compareTo(element) < 0) {
            n.right = add(n.right, element, n); // dir
        } else {
            n.left = add(n.left, element, n); // esq
        }

        // Atualiza a altura do nó
        n.altura = Math.max(height(n.left), height(n.right)) + 1;

        // Verifica o balanceamento após a inserção
        //de forma recursiva em todos os nodos da
        //árvore
        int balance = CoeficienteDeBalanceamento(n);
        if (balance > 1) {
            if (element.compareTo(n.left.elemento) < 0) {
                // Rotação simples à direita
                n = rotateRight(n);
            } else {
                // Rotação dupla, esquerda e depois  direita
                n = rotateLeftRight(n);
            }
        } else if (balance < -1) {
            if (element.compareTo(n.right.elemento) > 0) {
                // Rotação simples à esquerda
                n = rotateLeft(n);
            } else {
                // Rotação dupla,  direita e depois esquerda
                n = rotateRightLeft(n);
            }
        }

        return n;
    }

    //recebe as alturas tanto da esquerda como da direita
    //calcula o coeficiente de balanceamento
    //e retorna se esta balanceado(0,1 ou -1) ou retorna que esta
    //desbalanceado (se o inteiro retornar -2 ou 2 pra mais indica isso)

    private int CoeficienteDeBalanceamento(Nodo n) {
        int leftHeight = height(n.left);
        int rightHeight = height(n.right);
        return leftHeight - rightHeight;
    }

    //recebe o nodo e calcula sua altura na arvore

    private int height(Nodo n) {
        if (n == null) {
            return -1;
        }
        return n.altura;
    }

    //recebe o nodo e sua altura
    //calcula ela depois de ser solicitado 
    //normalmente a cada inserção e remoção
    //retorna nova altura do nodo
    private void updateHeight(Nodo n) {
        n.altura = Math.max(height(n.left), height(n.right)) + 1;
    }

    private Nodo rotateRight(Nodo n) {
        
        Nodo newRoot = n.left;
        //ai faz ele rodar para a direita, em relação ao seu filho
        n.left = newRoot.right;
        if (newRoot.right != null) {
            newRoot.right.father = n;
        }

        newRoot.right = n;
        newRoot.father = n.father;
        n.father = newRoot;

        updateHeight(n);
        updateHeight(newRoot);

        return newRoot;
    }

    private Nodo rotateLeft(Nodo n) {
        Nodo newRoot = n.right;
        n.right = newRoot.left;
        if (newRoot.left != null) {
            newRoot.left.father = n;
        }
        newRoot.left = n;
        newRoot.father = n.father;
        n.father = newRoot;

        updateHeight(n);
        updateHeight(newRoot);

        return newRoot;
    }

    private Nodo rotateLeftRight(Nodo n) {
        n.left = rotateLeft(n.left);
        return rotateRight(n);
    }

    private Nodo rotateRightLeft(Nodo n) {
        n.right = rotateRight(n.right);
        return rotateLeft(n);
    }



    
    public void generateDot(String filename) {
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write("digraph AVL {\n");
            writer.write("  node [shape=circle, fontname=\"Arial\"];\n");
            generateDot(root, writer);
            writer.write("}\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void generateDot(Nodo n, FileWriter writer) throws IOException {
        if (n == null) {
            return;
        }

        writer.write("  " + n.elemento + " [label=\"" + n.elemento + "\"];\n");

        if (n.left != null) {
            writer.write("  " + n.elemento + " -> " + n.left.elemento + " [label=\"left\"];\n");
        }

        if (n.right != null) {
            writer.write("  " + n.elemento + " -> " + n.right.elemento + " [label=\"right\"];\n");
        }

        generateDot(n.left, writer);
        generateDot(n.right, writer);
    }




}