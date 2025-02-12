package FP_DAA;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class GridPanel extends JPanel implements MouseListener, MouseMotionListener {
    // Atribut untuk menyimpan objek Grid
    private Grid grid;

    // Tinggi dan lebar panel
    private int height;
    private int width;

    // Node saat ini yang sedang diinteraksi
    Node current = null;

    // Konstruktor untuk membuat objek GridPanel
    public GridPanel(int height, int width, Grid grid) {
        this.grid = grid;
        this.height = height;
        this.width = width;

        this.setSize(height, width);
        this.setFocusable(true);
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
    }

    // Metode untuk mengupdate ukuran sel dalam grid
    public void updateGrid(int cellSize) {
        if (!MyUtils.solving) {
            Node.size = cellSize;
            int rows = (int) Math.floor(height / Node.size);
            int cols = (int) Math.floor(width / Node.size);

            grid = new Grid(rows, cols);
            this.revalidate();
            this.repaint();
        }
    }

    // Metode untuk mengupdate posisi awal node
    public void updateStart(int x, int y) {
        if (!MyUtils.solving) {
            grid.setStart(grid.getNode(x, y));
            this.revalidate();
            this.repaint();
        }
    }

    // Metode untuk mengupdate posisi akhir node
    public void updateFinish(int x, int y) {
        if (!MyUtils.solving) {
            grid.setFinish(grid.getNode(x, y));
            this.revalidate();
            this.repaint();
        }
    }

    // Metode untuk mereset grid
    public void resetGrid() {
        MyUtils.solving = false;
        MyUtils.breakAlgo = true;
        MyUtils.stopped = false;
        grid.inisialisasiGrid();
        this.revalidate();
        this.repaint();
    }

    // Metode untuk menghapus rintangan dalam grid
    public void clearWalls() {
        grid.clearWalls();
        this.revalidate();
        this.repaint();
    }

    // Getter dan setter untuk objek Grid
    public Grid getGrid() {
        return grid;
    }

    public void setGrid(Grid grid) {
        this.grid = grid;
    }

    // Metode untuk menggambar komponen panel
    @Override
    public void paintComponent(Graphics graphics) {
        Graphics2D g = (Graphics2D) graphics;
        super.paintComponent(g);
        g.setStroke(new BasicStroke(1.5f));
        g.setColor(Color.black);
        grid.drawGrid(g, this);

        // Menampilkan pesan "Paused" jika algoritma sedang dijeda
        if (MyUtils.stopped) {
            g.setFont(new Font("Poppins", Font.CENTER_BASELINE, 60));
            g.setColor(new Color(255, 0, 142));
            g.drawString("Paused", width / 2 - 110, height / 4);
            g.setFont(new Font("Poppins", Font.CENTER_BASELINE, 30));
            g.drawString("Press space to resume", width / 2 - 160, height / 3);
        }
    }

    // Metode untuk menangani klik mouse
    @Override
    public void mouseClicked(MouseEvent e) {
        // Tidak digunakan
    }

    // Metode untuk mereset jalur yang telah dicari
    public void resetPath() {
        MyUtils.stopped = false;
        grid.resetPath();
        this.revalidate();
        this.repaint();
    }

    // Metode untuk menangani penekanan tombol mouse
    @Override
    public void mousePressed(MouseEvent e) {
        if (MyUtils.solving) {
            return;
        }
        int x = e.getX() / Node.size;
        int y = e.getY() / Node.size;
        if (x < 0 || y >= grid.getCols() || y < 0 || x >= grid.getRows()) {
            return;
        }
        if (SwingUtilities.isLeftMouseButton(e)) {
            if (grid.getNode(x, y) != null && !grid.getNode(x, y).isFinish() && !grid.getNode(x, y).isStart()) {
                grid.getNode(x, y).setWall(true);
            }
        }
        if (SwingUtilities.isRightMouseButton(e)) {
            if (grid.getNode(x, y) != null && !grid.getNode(x, y).isFinish() && !grid.getNode(x, y).isStart()) {
                grid.getNode(x, y).setWall(false);
            }
        }
        current = grid.getNode(x, y);
        this.revalidate();
        this.repaint();
    }

    // Metode untuk menangani pelepasan tombol mouse
    @Override
    public void mouseReleased(MouseEvent e) {
        if (MyUtils.solving) {
            return;
        }
        int x = e.getX() / Node.size;
        int y = e.getY() / Node.size;

        if (x < 0 || y >= grid.getCols() || y < 0 || x >= grid.getRows()) {
            return;
        }

        if (SwingUtilities.isLeftMouseButton(e)) {
            if (grid.getNode(x, y) != null && !grid.getNode(x, y).isFinish() && !grid.getNode(x, y).isStart()) {
                grid.getNode(x, y).setWall(true);
            }
        }
        if (SwingUtilities.isRightMouseButton(e)) {
            if (grid.getNode(x, y) != null && !grid.getNode(x, y).isFinish() && !grid.getNode(x, y).isStart()) {
                grid.getNode(x, y).setWall(false);
            }
        }

        current = null;
        this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        this.revalidate();
        this.repaint();
    }

    // Metode untuk menangani masuk mouse ke area panel
    @Override
    public void mouseEntered(MouseEvent e) {
        // Tidak digunakan
    }

    // Metode untuk menangani keluar mouse dari area panel
    @Override
    public void mouseExited(MouseEvent e) {
        // Tidak digunakan
    }

    // Metode untuk menangani pergerakan mouse
    @Override
    public void mouseDragged(MouseEvent e) {
        if (MyUtils.solving) {
            return;
        }
        int x = e.getX() / Node.size;
        int y = e.getY() / Node.size;

        if (x < 0 || y >= grid.getCols() || y < 0 || x >= grid.getRows()) {
            return;
        }

        if (SwingUtilities.isLeftMouseButton(e)) {
            if (current != null && current.isStart()) {
                current = grid.getNode(x, y);
                current.setWall(false);
                grid.setStart(current);
            }
        }
        if (SwingUtilities.isLeftMouseButton(e)) {
            if (current != null && current.isFinish()) {
                current = grid.getNode(x, y);
                current.setWall(false);
                grid.setFinish(current);
            }
        }

        if (SwingUtilities.isLeftMouseButton(e)) {
            if (current != null && !current.isFinish() && !current.isStart()) {
                current = grid.getNode(x, y);
                current.setWall(true);
            }
        }
        if (SwingUtilities.isRightMouseButton(e)) {
            if (current != null && !current.isFinish() && !current.isStart()) {
                current = grid.getNode(x, y);
                current.setWall(false);
            }
        }
        this.revalidate();
        this.repaint();
    }

    // Metode untuk menangani pergerakan mouse tanpa penekanan tombol
    @Override
    public void mouseMoved(MouseEvent e) {
        int x = e.getX() / Node.size;
        int y = e.getY() / Node.size;

        if (x < 0 || y >= grid.getCols() || y < 0 || x >= grid.getRows()) {
            return;
        }

        Node node = grid.getNode(x, y);

        if (MyUtils.solving) {
            this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        } else if (node != null && (node.isStart() || node.isFinish())) {
            this.setCursor(new Cursor(Cursor.MOVE_CURSOR));
        } else {
            this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
        this.revalidate();
        this.repaint();
    }
}
