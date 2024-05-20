package FP_DAA;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class Grid {

    // Atribut untuk menyimpan jumlah baris dan kolom dalam grid
    private int rows, cols;
    
    // Matriks untuk menyimpan node-node dalam grid
    private Node[][] grid;

    // Node yang menandai titik awal dan akhir dalam grid
    private Node start;
    private Node finish;

    // Konstruktor untuk membuat objek Grid dengan jumlah baris dan kolom tertentu
    public Grid(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        grid = new Node[rows][cols];
        inisialisasiGrid();
    }
    
    // Metode untuk menghasilkan salinan objek Grid (belum diimplementasikan)
    public Grid cloneGrid() {
        // Implementasikan logika untuk menghasilkan salinan objek Grid
        return null;
    }

    // Metode untuk menginisialisasi grid dengan node-node default
    public void inisialisasiGrid() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                grid[i][j] = new Node(i, j);
            }
        }
        start = grid[0][0];
        start.setStart(true);
        finish = grid[rows - 1][cols - 1];
        finish.setFinish(true);
    }

    // Metode untuk menghapus rintangan di dalam grid
    public void clearWalls() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                grid[i][j].setWall(false);
            }
        }
    }

    // Metode untuk mereset jalur yang telah dicari
    public void resetPath() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Node node = grid[i][j];
                node.setParent(null);
                node.setAlreadyVisited(false);
                node.setType(Type.DEFAULT);
                node.setG(0);
            }
        }
    }

    // Metode untuk mengupdate ukuran grid
    public void updateGrid(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        grid = new Node[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                grid[i][j] = new Node(i, j);
            }
        }
    }

    // Metode untuk menggambar grid pada panel
    public void drawGrid(Graphics2D g, JPanel panel) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                grid[i][j].draw(g, panel);
                g.setColor(Color.black);
                g.setStroke(new BasicStroke(1.5f));
                g.drawRect(i * Node.getSize(), j * Node.getSize(), Node.getSize(), Node.getSize());
            }
        }
        start.draw(g, panel);
        finish.draw(g, panel);
        panel.revalidate();
        panel.repaint();
    }

    // Metode untuk mencetak informasi grid ke konsol (tidak digunakan)
    public void printGrid() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < rows; j++) {
                System.out.println("i: " + i + " j: " + j);
            }
        }
    }

    // Getter dan setter untuk atribut-atribut Grid
    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getCols() {
        return cols;
    }

    public void setCols(int cols) {
        this.cols = cols;
    }

    // Metode untuk mendapatkan node pada posisi tertentu dalam grid
    public Node getNode(int x, int y) {
        return grid[x][y];
    }

    public Node[][] getGrid() {
        return grid;
    }

    public void setGrid(Node[][] grid) {
        this.grid = grid;
    }

    // Getter dan setter untuk node start dalam grid
    public Node getStart() {
        return start;
    }

    public void setStart(Node start) {
        if (start.equals(finish)) {
            return;
        }
        this.start.setStart(false);
        this.start = start;
        this.start.setStart(true);
    }

    // Getter dan setter untuk node finish dalam grid
    public Node getFinish() {
        return finish;
    }

    public void setFinish(Node finish) {
        if (start.equals(finish)) {
            return;
        }
        this.finish.setFinish(false);
        this.finish = finish;
        this.finish.setFinish(true);
    }
}
