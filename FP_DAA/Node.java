package FP_DAA;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.LinkedList;
import java.util.Objects;

import javax.swing.JPanel;

// Kelas Node merepresentasikan simpul dalam grid
public class Node {
    private int x; // Koordinat x
    private int y; // Koordinat y
    private int g; // Nilai g (cost) untuk algoritma pencarian

    private Type type = Type.DEFAULT; // Tipe node untuk menentukan warna
    private boolean isWall = false; // Menandakan apakah node ini merupakan tembok
    private boolean isStart = false; // Menandakan apakah node ini merupakan titik awal
    private boolean isFinish = false; // Menandakan apakah node ini merupakan titik akhir
    private boolean alreadyVisited; // Menandakan apakah node ini sudah dikunjungi

    public static int size = 30; // Ukuran default untuk node

    private Node parent; // Parent dari node ini dalam pencarian

    // Konstruktor untuk membuat node dengan koordinat (x, y)
    public Node(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // Mengembalikan daftar tetangga dari node ini dalam grid
    public LinkedList<Node> getNeighbors(Grid grid) {
        LinkedList<Node> neighbors = new LinkedList<Node>();

        // Tetangga sebelah kanan
        if (this.x + 1 < grid.getRows() && x >= 0) {
            Node node = grid.getNode(this.x + 1, this.y);
            if (!node.alreadyVisited && !node.isWall) {
                createNeighbor(node, grid);
                neighbors.add(node);
            }
        }

        // Tetangga sebelah bawah
        if (this.y + 1 < grid.getCols() && this.y >= 0) {
            Node node = grid.getNode(this.x, this.y + 1);
            if (!node.alreadyVisited && !node.isWall) {
                createNeighbor(node, grid);
                neighbors.add(node);
            }
        }

        // Tetangga sebelah atas
        if (this.y - 1 >= 0 && this.y < grid.getCols()) {
            Node node = grid.getNode(this.x, this.y - 1);
            if (!node.alreadyVisited && !node.isWall) {
                createNeighbor(node, grid);
                neighbors.add(node);
            }
        }

        // Tetangga sebelah kiri
        if (this.x - 1 >= 0 && this.x < grid.getRows()) {
            Node node = grid.getNode(this.x - 1, this.y);
            if (!node.alreadyVisited && !node.isWall) {
                createNeighbor(node, grid);
                neighbors.add(node);
            }
        }

        return neighbors;
    }

    // Membuat tetangga dengan menetapkan parent dan nilai g
    public void createNeighbor(Node node, Grid grid) {
        node.setParent(this);
        node.setG(this.g + 1);
    }

    // Menggambar node pada panel dengan warna yang sesuai
    public void draw(Graphics2D g, JPanel panel) {
        g.setColor(Color.black);

        // Jika merupakan tembok, node diisi dengan warna hitam
        if (isWall) {
            g.fillRect(x * size, y * size, size, size);
        }

        // Menetapkan warna sesuai dengan tipe node
        switch (type) {
            case START:
                g.setColor(Color.blue);
                break;
            case VISITED:
                g.setColor(Color.cyan);
                break;
            case CURRENT:
                g.setColor(Color.magenta);
                break;
            case FINISH:
                g.setColor(Color.red);
                break;
            case FRONTIER:
                g.setColor(Color.GREEN);
                break;
            case WALL:
                return; // Node tembok tidak diubah warnanya
            case PATH:
                g.setColor(Color.yellow);
                break;
            default:
                return; // Default tidak diubah warnanya
        }

        // Menggambar node dengan warna yang sudah ditetapkan
        g.setStroke(new BasicStroke(1.5f));
        g.fillRect(x * size, y * size, size, size);
        panel.revalidate();
        panel.repaint();
    }

    // Metode clone yang mengembalikan salinan dari node ini
    @Override
    protected Object clone() throws CloneNotSupportedException {
        Node newNode = new Node(this.x, this.y);
        return newNode;
    }

    // Metode hashCode yang menghasilkan nilai hash berdasarkan koordinat (x, y)
    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    // Metode equals untuk memeriksa apakah dua node sama berdasarkan koordinat (x, y)
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Node other = (Node) obj;
        return x == other.x && y == other.y;
    }

    // Metode toString yang mengembalikan representasi string dari node ini
    @Override
    public String toString() {
        return "x: " + this.x + " y: " + this.y;
    }

    // Getter dan setter untuk berbagai atribut node

    public boolean isStart() {
        return isStart;
    }

    public void setStart(boolean isStart) {
        if (isStart) {
            this.type = Type.START;
        } else {
            this.type = Type.DEFAULT;
        }
        this.isStart = isStart;
    }

    public boolean isFinish() {
        return isFinish;
    }

    public void setFinish(boolean isFinish) {
        if (isFinish) {
            this.type = Type.FINISH;
        } else {
            this.type = Type.DEFAULT;
        }
        this.isFinish = isFinish;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getG() {
        return g;
    }

    public void setG(int g) {
        this.g = g;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        if (isStart || isFinish) {
            return;
        }
        this.type = type;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public boolean isWall() {
        return isWall;
    }

    public void setWall(boolean isWall) {
        if (isWall) {
            this.setType(Type.WALL);
        } else {
            this.setType(Type.DEFAULT);
        }
        this.isWall = isWall;
    }

    // Metode untuk mengganti status tembok (menjadi tembok jika bukan tembok, dan sebaliknya)
    public void toggleWall() {
        this.isWall = !this.isWall;
    }

    // Getter dan setter untuk ukuran node
    public static int getSize() {
        return size;
    }

    public static void setSize(int size) {
        Node.size = size;
    }

    public boolean isAlreadyVisited() {
        return alreadyVisited;
    }

    public void setAlreadyVisited(boolean alreadyVisited) {
        this.alreadyVisited = alreadyVisited;
    }
}
