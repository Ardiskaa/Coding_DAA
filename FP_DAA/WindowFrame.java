package FP_DAA;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;

// Kelas yang merepresentasikan frame utama aplikasi pathfinding visualizer
public class WindowFrame extends JFrame {

    private JPanel container;

    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;

    // Menghitung jumlah baris dan kolom berdasarkan ukuran node
    public static int rows = (int) Math.floor(HEIGHT / Node.size);
    public static int cols = (int) Math.floor(HEIGHT / Node.size);

    // Konstruktor untuk inisialisasi frame
    public WindowFrame() {

        // Inisialisasi objek grid
        Grid grid = new Grid(rows, cols);

        // Membuat panel utama
        container = new JPanel();

        // Membuat panel grid
        GridPanel gridPanel = new GridPanel(WIDTH - 200, HEIGHT, grid);

        // Membuat panel kontrol
        ControlPanel controls = new ControlPanel(200, HEIGHT, gridPanel);

        // Mengatur layout panel utama
        container.setLayout(new BorderLayout());

        // Menambahkan panel kontrol di sebelah kiri dan panel grid di tengah
        container.add(BorderLayout.WEST, controls);
        container.add(BorderLayout.CENTER, gridPanel);

        // Mengatur properti frame
        this.setContentPane(container);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Path Finding Visualizer");
        this.getContentPane().setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.pack();
        this.setVisible(true);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
    }
}
