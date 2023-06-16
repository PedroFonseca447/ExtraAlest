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
            altura = 0;
        }
    }

    public Nodo root;
    public int count;

    public Avl() {
        root = null;
        count = 0;
    }
    /**
         * @param e é o elemento que será adicionado ao root da árvore
         * @return true se a árvore ainda não tem uma raíz.
         * @return false se a árvore já tem uma raíz
         */
    public boolean addRoot(Integer e) {
        Nodo aux = new Nodo(e);

        if (root != null) {
            return false;
        }

        root = aux;
        count++;
        return true;
    }

        /**
         * Esse método de forma recursiva, percorre a árvore a cada inserção de elemento
         * partindo do root, após a inserção usando do elemento presente no nodo(altura) 
         * ele análisa se essa inserção causou algum diferencial no coeficiente de balance-
         * amento de algum dos nodos presentes na árvore, e a organiza caso seja necessário
         * @param element é o elemento que será adicionado ao novo nodo da árvore    
         */


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
        int balance = CoeficienteDeBalanceamento(n);
        if (balance > 1) {
            if (element.compareTo(n.left.elemento) < 0) {
                // Rotação simples à direita
                n = rotateRight(n);
            } else {
                // Rotação dupla, esquerda e depois direita
                n = rotateLeftRight(n);
            }
        } else if (balance < -1) {
            if (element.compareTo(n.right.elemento) > 0) {
                // Rotação simples à esquerda
                n = rotateLeft(n);
            } else {
                // Rotação dupla, direita e depois esquerda
                n = rotateRightLeft(n);
            }
        }

        return n;
    }
    /**
     * @param element recebe o elemento que terá seu nodo removido da árvore
     * Nesse método assim como no add, deve ser feito a remoção tomando cuidado
     * para manter a avl balanceada, em caso de alterações de coeficiente de bala
     * ceamento de cada nodo.
     */

    public void remove(Integer element) {
        root = remove(root, element);
        count--;
    }

    private Nodo remove(Nodo n, Integer element) {
        if (n == null) {
            return null;
        }

        if (n.elemento.compareTo(element) < 0) {
            n.right = remove(n.right, element);
        } else if (n.elemento.compareTo(element) > 0) {
            n.left = remove(n.left, element);
        } else {
            if (n.left == null || n.right == null) {
                // Caso 1: Nó com 0 ou 1 filho
                Nodo aux = (n.left != null) ? n.left : n.right;
                if (aux == null) {
                    // Nó folha, apenas o remove
                    aux = n;
                    n = null;
                } else {
                    // Nó com 1 filho, copia os valores do filho para o nó atual
                    n.elemento = aux.elemento;
                    n.left = aux.left;
                    n.right = aux.right;
                }
                aux = null; // Libera o nó removido da memória
            } else {
                // Caso 2: Nó com 2 filhos
                Nodo minRight = findMin(n.right); // Encontra o nó mínimo na subárvore direita
                n.elemento = minRight.elemento; // Substitui o elemento do nó atual pelo mínimo encontrado
                n.right = remove(n.right, minRight.elemento); // Remove o nó mínimo encontrado
            }
        }

        if (n != null) {
            // Atualiza a altura do nó
            n.altura = Math.max(height(n.left), height(n.right)) + 1;

            // Verifica o balanceamento após a remoção
            int balance = CoeficienteDeBalanceamento(n);
            if (balance > 1) {
                if (CoeficienteDeBalanceamento(n.left) >= 0) {
                    // Rotação simples à direita
                    n = rotateRight(n);
                } else {
                    // Rotação dupla, esquerda e depois direita
                    n = rotateLeftRight(n);
                }
            } else if (balance < -1) {
                if (CoeficienteDeBalanceamento(n.right) <= 0) {
                    // Rotação simples à esquerda
                    n = rotateLeft(n);
                } else {
                    // Rotação dupla, direita e depois esquerda
                    n = rotateRightLeft(n);
                }
            }
        }

        return n;
    }
    /**
     * @param aux recebe um nodo
     * @return o valor minimo na subárvore solicitada pelo nodo seja na esquerda ou direita
     */

    private Nodo findMin(Nodo aux) {
        if (aux.left == null) {
            return aux;
        }
        return findMin(aux.left);
    }

    /**
     * Esse método realiza a operação de calculo para saber se o nodo aux possui um desbalanceamento
     * caso ele devolva um número superior a 1 ou inferior a -1 indica que está desbalanceado.
     * @param aux ao receber o nodo, calcula a altura de suas subTree.
     * @return a diferença entre a altura de uma subtree da esquerda do aux menos a altura da subtree a direita de aux
     */

    private int CoeficienteDeBalanceamento(Nodo aux) {
        int leftHeight = height(aux.left);
        int rightHeight = height(aux.right);
        return leftHeight - rightHeight;
    }

     /**
     * @param recebe o nodo consultado sobre a altura
     * @return devolve a altura do nodo.
     */


    private int height(Nodo aux) {
        if (aux == null) {
            return -1;
        }
        return aux.altura;
    }

     /**
      * Esse método funciona para a cada inserção/remoção ele realizar a atualização das alturas
      e assim ajudar se ocorreu algum desbalanceamento na avl. Após a operação ele realiza a alteração
      usando o Math.max para pegar o maior valor em uma operação de 2 inteiros
     * @param n um nodo para recalcular sua altura
     **/

    private void updateHeight(Nodo n) {
        n.altura = Math.max(height(n.left), height(n.right)) + 1;
    }


     /**
      * Nesse método recebe o nodo com  a diferença das alturas de sua subtree 
    sendo de <-1, assim demonstrando que precisa de todos os nodos em contados com
      o n em específico girem para a direita.
     * @param n nodo que possui em seu coeficiente um desbalanceamento
     */
    private Nodo rotateRight(Nodo n) {
        Nodo newRoot = n.left;
        n.left = newRoot.right;
        //nesse caso procura se o nodo
        //a direita do nodo que está sendo
        //realocado possui filhos e torna esse filho
        //filho do antigo root reposicionando a árvpre
        if (newRoot.right != null) {
            newRoot.right.father = n;
        }
        newRoot.right = n;
        newRoot.father = n.father;
        n.father = newRoot;

        //atualiza as alturas 
        updateHeight(n);
      
       //atualiza as alturas 
        updateHeight(newRoot);

        return newRoot;
    }
     /**
      * Nesse método recebe o nodo com essa diferença  das alturas de sua subtree
     sendo de >1, assim demonstrando que precisa de todos os nodos em contados com
      o n em específico girem para a esquerda.
     * @param n nodo que possui em seu coeficiente um desbalanceamento
     */

    private Nodo rotateLeft(Nodo n) {
        Nodo newRoot = n.right;
        n.right = newRoot.left;
         //nesse caso procura se o nodo
        //a esquerda do nodo que está sendo
        //realocado possui filhos e torna esse filho
        //filho do antigo root realocado na árvore
        if (newRoot.left != null) {
            newRoot.left.father = n;
        }
        newRoot.left = n;
        newRoot.father = n.father;
        n.father = newRoot;
        //atualiza as alturas 
        updateHeight(n);
        //atualiza as alturas 
        updateHeight(newRoot);

        return newRoot;
    }
     /**
     * Tem casos que apenas um único giro, n é suficiente e mantém a árvore desequilibrada,
     * nisso esse método ativa primeiro o giro do filho do nodo n para a esquerda, e depois gira
     * o n para a direita junto de seus filhos
     * @param n nodo que possui em seu coeficiente um desbalanceamento
     */

    private Nodo rotateLeftRight(Nodo n) {
        n.left = rotateLeft(n.left);
        return rotateRight(n);
    }


       /**
     * Tem casos que apenas um único giro, n é suficiente e mantém a árvore desequilibrada,
     * nisso esse método ativa primeiro o giro do filho do nodo n para a direita, e depois gira
     * o n para a esquerda junto de seus filhos
     * @param n nodo que possui em seu coeficiente um desbalanceamento
     */

    private Nodo rotateRightLeft(Nodo n) {
        n.right = rotateRight(n.right);
        return rotateLeft(n);
    }


    public boolean contains(Integer procura){
        Nodo n = searchNodo(procura, root);
        return (n!=null);
    }

    private Nodo searchNodo(Integer eInteger,Nodo aux){
        if(aux==null|| eInteger==null){
            return null;
        }
        if(eInteger==aux.elemento){
            return aux;
        }
        if (eInteger < aux.elemento)
         return searchNodo(eInteger, aux.left);
       else
          return searchNodo(eInteger, aux.right);

    
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