import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class SwingBinaryTreeGUI extends JFrame {

    private BinaryTree tree = new BinaryTree();
    private JTextArea resultTextArea = new JTextArea(10, 30);
    private JTextField insertField = new JTextField(10);
    private static Map<Node, Point> nodePositions;
    private static final int LEVEL_HEIGHT = 70;
    private Dimension treePanelSize = new Dimension(800, 600);

    private JPanel treePanel = new JPanel() {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            treePanel.setPreferredSize(treePanelSize);
            drawTree(g, tree.root);
        }
    };

    public SwingBinaryTreeGUI() {
        super("Binary Tree GUI");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setPreferredSize(screenSize);
        // Define a janela como tela cheia
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        JButton insertButton = new JButton("Inserir Elemento");
        JButton showButton = new JButton("BST Infos");
        JButton drawTreeButton = new JButton("Desenhar Árvore"); // Novo botão

        insertButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String input = insertField.getText();
                try {
                    tree.insert(input);
                    insertField.setText("");
                    resultTextArea.setText("Elemento " + input.trim() + " inserido.");
                    nodePositions = new HashMap<>();
                } catch (stringvazia ex) {
                    resultTextArea.setText("Espaços em branco não permitidos");
                }
            }
        });

        showButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // ... (o código de exibição é o mesmo que antes)
            }
        });

        drawTreeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calculateNodePositions(tree.root, 90, 90, 22);
                updateTreePanelSize();
                treePanel.repaint(); // Aciona o redesenho da árvore
            }
        });
        showButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    String result = "Em ordem:\n" + tree.EmOrdem() + "\n" +
                            "Pré-ordem:\n" + tree.Preordem() + "\n" +
                            "Pós-ordem:\n" + tree.Posordem() + "\n" +
                            "Level order:\n" + tree.Levordem() + "\n" +
                            "Elemento min: " + tree.findMin() + "\n" +
                            "Elemento máx: " + tree.findMax() + "\n" +
                            "Altura da árvore: " + (tree.height()-1) + "\n" +
                            "Tamanho da árvore: " + tree.size() + "\n" +
                            "Comprimento interno da árvore: " + tree.internalPathLength() + "\n" +
                            (tree.isBalanced(tree.root) ? "A árvore está balanceada." : "A árvore não está balanceada.");
                    resultTextArea.setText(result);
                }catch (IllegalArgumentException n){
                    resultTextArea.setText("Árvore vazia");
                }
            }
        });

        JPanel insertPanel = new JPanel();
        insertPanel.add(new JLabel("Digite um elemento a ser inserido: "));
        insertPanel.add(insertField);
        insertPanel.add(insertButton);
        insertPanel.add(showButton);
        insertPanel.add(drawTreeButton); // Adicionando o botão "Desenhar Árvore"

        JPanel panel = new JPanel();
        panel.add(showButton);

        resultTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultTextArea);

        setLayout(new BorderLayout());
        add(insertPanel, BorderLayout.WEST);
        add(treePanel, BorderLayout.NORTH);
        add(panel, BorderLayout.EAST);
        add(scrollPane, BorderLayout.CENTER);

        pack();

        setLocationRelativeTo(null);
    }
    private void updateTreePanelSize() {
        int maxX = 0;
        int maxY = 0;

        for (Point position : nodePositions.values()) {
            maxX = Math.max(maxX, position.x);
            maxY = Math.max(maxY, position.y);
        }

        treePanelSize = new Dimension(maxX + 40, maxY + 40); // Adiciona uma margem
        treePanel.setPreferredSize(treePanelSize);
        revalidate(); // Atualiza o layout
    }
    private static void calculateNodePositions(Node node, int x, int y, int xOffset) {
        if (node == null) {
            return;
        }

        int leftWidth = getWidth(node.left);
        Point currentPosition = new Point(x + leftWidth + xOffset, y);

        nodePositions.put(node, currentPosition);

        calculateNodePositions(node.left, x, y + LEVEL_HEIGHT, xOffset);
        calculateNodePositions(node.right, x + leftWidth + xOffset, y + LEVEL_HEIGHT, xOffset);
    }
    private static int getWidth(Node node) {
        if (node == null) {
            return 0;
        }

        int leftWidth = getWidth(node.left);
        int rightWidth = getWidth(node.right);

        return Math.max(60, leftWidth + 60 + rightWidth);
    }
    private void drawTree(Graphics g, Node node) {
        if (node == null) {
            return;
        }

        Point currentPosition = nodePositions.get(node);

        int circleRadius = 20;
        int circleCenterX = currentPosition.x;
        int circleCenterY = currentPosition.y;

        g.setColor(Color.WHITE);
        g.fillOval(circleCenterX - circleRadius, circleCenterY - circleRadius, 2 * circleRadius, 2 * circleRadius);
        g.setColor(Color.BLACK);
        g.drawOval(circleCenterX - circleRadius, circleCenterY - circleRadius, 2 * circleRadius, 2 * circleRadius);

        g.drawString(node.key, circleCenterX - 5, circleCenterY + 5);

        Node leftNode = node.left;
        Node rightNode = node.right;

        if (leftNode != null) {
            Point leftPosition = nodePositions.get(leftNode);
            drawLineBetweenCircles(g, circleCenterX, circleCenterY, leftPosition.x, leftPosition.y);
        }

        if (rightNode != null) {
            Point rightPosition = nodePositions.get(rightNode);
            drawLineBetweenCircles(g, circleCenterX, circleCenterY, rightPosition.x, rightPosition.y);
        }

        drawTree(g, leftNode);
        drawTree(g, rightNode);
    }
    private static void drawLineBetweenCircles(Graphics g, int x1, int y1, int x2, int y2) {
        int circleRadius = 20;
        double angle = Math.atan2(y2 - y1, x2 - x1);

        int x1OnCircle = (int) (x1 + circleRadius * Math.cos(angle));
        int y1OnCircle = (int) (y1 + circleRadius * Math.sin(angle));

        int x2OnCircle = (int) (x2 - circleRadius * Math.cos(angle));
        int y2OnCircle = (int) (y2 - circleRadius * Math.sin(angle));

        g.drawLine(x1OnCircle, y1OnCircle, x2OnCircle, y2OnCircle);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                SwingBinaryTreeGUI j = new SwingBinaryTreeGUI();
                j.setVisible(true);
            }
        });
    }
}
