package FP_DAA;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ControlPanel extends JPanel implements ActionListener {

    private JButton search;
    private JButton reset;
    private JButton resetPath;
    private JButton generate;
    private JComboBox<String> algorithms;

    private JSlider size;
    private JSlider jeda;
    private JLabel sizeLabel;
    private JLabel jedaLabel;
    private JLabel copyright;
    private String[] namaAlgoritma = { "Breadth first search", "Depth first search" };
    private JLabel start;
    private JLabel target;
    private JLabel current;
    private JLabel visited;
    private JLabel frontier;
    private JLabel path;
    private JLabel wall;
    private GridPanel gridPanel;
    SearchAlgorithms algo = null;

    // Konstruktor untuk ControlsPanel
    public ControlPanel(int width, int height, GridPanel gridPanel) {
        this.gridPanel = gridPanel;

        // Pengaturan tampilan panel
        this.setPreferredSize(new Dimension(width, height));
        this.setFocusable(true);
        this.setLayout(null);
        this.requestFocus();
        this.addFocusListener(new FocusListener() {
            @Override
            public void focusLost(FocusEvent e) {
                ControlPanel p = (ControlPanel) e.getSource();
                p.requestFocus();
            }

            @Override
            public void focusGained(FocusEvent e) {

            }
        });
        this.addKeyListener(new KeyAdapter() {
            @SuppressWarnings("removal")
            @Override
            public void keyPressed(KeyEvent e) {
                // Menghentikan atau melanjutkan algoritma saat tombol spasi ditekan
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    if (algo == null || !MyUtils.solving) {
                        return;
                    }

                    if (MyUtils.stopped) {
                        MyUtils.stopped = false;
                        algo.resume();
                        gridPanel.revalidate();
                        gridPanel.repaint();
                        return;
                    }

                    MyUtils.stopped = true;
                    algo.suspend();

                    gridPanel.revalidate();
                    gridPanel.repaint();
                }
            }
        });

        // Membuat tombol-tombol dan komponen UI lainnya
        search = new JButton("Mulai");
        search.setBounds(25, 10, 150, 30);
        search.addActionListener(this);
        search.setFocusable(false);

        generate = new JButton("Generate Rintangan");
        generate.setBounds(25, 50, 150, 30);
        generate.setFocusable(false);
        generate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Menghasilkan rintangan secara acak di grid
                if (MyUtils.solving) {
                    return;
                }
                gridPanel.clearWalls();

                Grid grid = gridPanel.getGrid();
                Random rand = new Random(System.currentTimeMillis());

                for (int i = 0; i < grid.getRows(); i++) {
                    for (int j = 0; j < grid.getCols(); j++) {
                        Node node = grid.getNode(i, j);
                        if (!node.isStart() && !node.isFinish() && rand.nextFloat() > 0.7) {
                            node.setWall(true);
                        }
                    }
                }

            }
        });

        reset = new JButton("Reset grid");
        reset.setBounds(25, 90, 150, 30);
        reset.setFocusable(false);
        reset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Me-reset grid ke kondisi awal
                MyUtils.solving = false;

                gridPanel.resetGrid();
                gridPanel.revalidate();
                gridPanel.repaint();
            }
        });

        resetPath = new JButton("Reset path");
        resetPath.setBounds(25, 130, 150, 30);
        resetPath.setFocusable(false);
        resetPath.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Me-reset jalur yang telah dicari
                if (MyUtils.solving) {
                    return;
                }
                gridPanel.resetPath();
                gridPanel.repaint();
            }
        });

        algorithms = new JComboBox<>(namaAlgoritma);
        algorithms.setSelectedIndex(0);
        algorithms.setBounds(25, 175, 150, 30);
        algorithms.setFocusable(false);
        algorithms.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Mengubah algoritma pencarian saat dipilih dari JComboBox
                if (MyUtils.solving) {
                    algorithms.setSelectedIndex(MyUtils.algorithm);
                    return;
                }
                MyUtils.algorithm = algorithms.getSelectedIndex();
            }
        });

        // Label dan slider untuk mengatur ukuran sel di grid
        sizeLabel = new JLabel("Ukuran: 20x20");
        sizeLabel.setBounds(24, 245, 150, 20);
        sizeLabel.setFocusable(false);

        size = new JSlider(20, 60, Node.size);
        size.setMajorTickSpacing(10);
        size.setMinorTickSpacing(10);
        size.setBounds(18, 270, 150, 20);
        size.setFocusable(false);
        size.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                // Mengubah ukuran sel di grid
                if (MyUtils.solving) {
                    return;
                }

                int cellSize = size.getValue() / 10 * 10;

                if (cellSize != Node.size) {
                    gridPanel.updateGrid(cellSize);
                    sizeLabel.setText("Ukuran: " + gridPanel.getGrid().getRows() + "x" + gridPanel.getGrid().getCols());
                }
            }
        });

        // Label dan slider untuk mengatur jeda dalam algoritma
        jedaLabel = new JLabel("Jeda: 30ms");
        jedaLabel.setBounds(24, 295, 150, 20);
        jedaLabel.setFocusable(false);

        jeda = new JSlider(0, 100, MyUtils.jeda);
        jeda.setMajorTickSpacing(10);
        jeda.setMinorTickSpacing(10);
        jeda.setBounds(18, 320, 150, 20);
        jeda.setFocusable(false);

        jeda.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                // Mengubah nilai jeda dalam algoritma
                MyUtils.jeda = jeda.getValue();
                jedaLabel.setText("jeda: " + MyUtils.jeda + "ms");
            }
        });

        // Label hak cipta
        copyright = new JLabel("Ardi 028, Bayu 085");
        copyright.setBounds(10, (int) (this.getPreferredSize().getHeight() - 30), 200, 20);
        copyright.setFocusable(false);

        // Label dan warna untuk menunjukkan jenis node di grid
        start = new JLabel("Starting node");
        start.setBounds(50, 350, 100, 15);
        start.setFocusable(false);

        target = new JLabel("Target node");
        target.setBounds(50, 375, 100, 15);
        target.setFocusable(false);

        current = new JLabel("Current node");
        current.setBounds(50, 400, 100, 15);
        current.setFocusable(false);

        visited = new JLabel("Visited node");
        visited.setBounds(50, 425, 100, 15);
        visited.setFocusable(false);

        frontier = new JLabel("Frontier node");
        frontier.setBounds(50, 450, 100, 15);
        frontier.setFocusable(false);

        path = new JLabel("Path node");
        path.setBounds(50, 475, 100, 15);
        path.setFocusable(false);

        wall = new JLabel("Wall node");
        wall.setBounds(50, 500, 100, 15);
        wall.setFocusable(false);

        this.add(search);
        this.add(reset);
        this.add(generate);
        this.add(algorithms);
        this.add(sizeLabel);
        this.add(size);
        this.add(jedaLabel);
        this.add(jeda);
        this.add(resetPath);
        this.add(copyright);
        this.add(start);
        this.add(target);
        this.add(current);
        this.add(visited);
        this.add(frontier);
        this.add(path);
        this.add(wall);
    }

    // Metode untuk menggambar warna-warna yang menunjukkan jenis node di grid
    @Override
    public void paintComponent(Graphics graphics) {
        Graphics2D g = (Graphics2D) graphics;
        super.paintComponent(g);
        g.setColor(Color.blue);
        g.fillRect(25, 350, 15, 15);

        g.setColor(Color.red);
        g.fillRect(25, 375, 15, 15);

        g.setColor(Color.magenta);
        g.fillRect(25, 400, 15, 15);

        g.setColor(Color.cyan);
        g.fillRect(25, 425, 15, 15);

        g.setColor(Color.green);
        g.fillRect(25, 450, 15, 15);

        g.setColor(Color.yellow);
        g.fillRect(25, 475, 15, 15);

        g.setColor(Color.black);
        g.fillRect(25, 500, 15, 15);
    }

    // Metode yang dipanggil saat tombol "Mulai" ditekan
    @Override
    public void actionPerformed(ActionEvent e) {
        this.requestFocus();

        // Memulai algoritma pencarian
        if (MyUtils.solving) {
            return;
        }
        gridPanel.resetPath();

        MyUtils.solving = true;
        algo = new SearchAlgorithms(gridPanel.getGrid(), gridPanel);
        algo.start();
    }
}
