import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class BinaryTree {
    Node root;

    BinaryTree() {
        root = null;
    }

    void insert(String key) throws stringvazia {
        if(!(key.trim().isEmpty())) {
            key = key.trim();
            root = insertRec(root, key);
        }else {
            throw new stringvazia();
        }
    }
// criar outro método insert para a esquerda e direita da raiz, bem como retornar o valor
    // e fazer este se tornar de qual lado foi inserido o elemento, acompanhando a formação da árvore
    Node insertRec(Node root, String key) {
        if (root == null) {
            root = new Node(key);
            // função vem aki para informar se foi esquerda ou direita, e o ramo anterior
            // a string montada vai sair dessa nova função
            return root;
        }

        int compare = key.compareTo(root.key);
        if (compare < 0)
            root.left = insertRec(root.left, key);// parametro vem aqui pra pegar as infos
        else if (compare > 0)
            root.right = insertRec(root.right, key);

        return root;
    }

    ArrayList<String> s = new ArrayList<>();
    Queue<Node> queue = new LinkedList<>();
    public String EmOrdem(){
        s.clear();
        inorderTraversal(root);
        String a = String.join(", ",s);
        return a;
    }
    public String Preordem(){
        s.clear();
        preorderTraversal(root);
        String a = String.join(", ",s);
        return a;
    }
    public String Posordem(){
        s.clear();
        postorderTraversal(root);
        String a = String.join(", ",s);
        return a;
    }
    public String Levordem(){
        s.clear();
        levelOrderTraversal(root);
        String a = String.join(", ",s);
        return a;
    }
    private void inorderTraversal(Node root) {//array
        if (root != null) {
            inorderTraversal(root.left);
            s.add(root.key);
            inorderTraversal(root.right);
        }
    }
    private void preorderTraversal(Node root) {
        if (root != null) {
            s.add(root.key);
            preorderTraversal(root.left);
            preorderTraversal(root.right);
        }
    }
    private void postorderTraversal(Node root) {
        if (root != null) {
            postorderTraversal(root.left);
            postorderTraversal(root.right);
            s.add(root.key);
        }
    }
    public static void limparFila(Queue<?> fila) {
        fila.clear();  // Limpa todos os elementos da fila
    }
    void levelOrderTraversal(Node root) {
        limparFila(queue);
        if (root == null)
            return;
        queue.add(root);
        s.add(root.key);
        if(root.left!=null)
            s.add(root.left.key);
        if(root.right!=null)
            s.add(root.right.key);


        while (!queue.isEmpty()) {

            Node current = queue.poll();

            if (current.left != null) {
                queue.add(current.left);
                if(queue.peek() != null) {
                    if(queue.peek().left!=null) {
                        s.add(queue.peek().left.key);
                    }

                }
            }
            if (current.right != null) {
                queue.add(current.right);
                if(queue.peek() != null) {
                    if(queue.peek().right!=null) {
                        s.add(queue.peek().right.key);
                    }
                }
            }

        }


    }
    int height(Node node) {
        if (node == null)
            return 0;
        return 1 + Math.max(height(node.left), height(node.right));
    }
    public int height() {
        return height(root);
    }
    boolean isBalanced(Node node) {
        if (node == null)
            return true;

        int leftHeight = height(node.left);
        int rightHeight = height(node.right);

        return Math.abs(leftHeight - rightHeight) <= 1 && isBalanced(node.left) && isBalanced(node.right);
    }
    int internalPathLength(Node node, int depth) {
        if (node == null)
            return 0;
        return depth + internalPathLength(node.left, depth + 1) + internalPathLength(node.right, depth + 1);
    }

    public int internalPathLength() {
        return internalPathLength(root, 0);
    }
    int size(Node node) {
        if (node == null)
            return 0;
        return 1 + size(node.left) + size(node.right);
    }

    public int size() {
        return size(root);
    }
    String findMin(Node node) {
        if (node == null)
            throw new IllegalArgumentException("A árvore está vazia");

        while (node.left != null)
            node = node.left;

        return node.key;
    }

    String findMax(Node node) {
        if (node == null)
            throw new IllegalArgumentException("A árvore está vazia");

        while (node.right != null)
            node = node.right;

        return node.key;
    }

    public String findMin() {
        return findMin(root);
    }

    public String findMax() {
        return findMax(root);
    }
}