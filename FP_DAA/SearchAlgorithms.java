package FP_DAA;

import java.awt.Cursor;
import java.util.LinkedList;
import java.util.Queue;

import javax.swing.JPanel;

// Kelas yang menangani implementasi algoritma pencarian (BFS dan DFS)
public class SearchAlgorithms extends Thread {

    private Grid grid; // Objek grid yang digunakan untuk pencarian
    private JPanel panel; // Panel tempat grid digambar
    private boolean solutionFound = false; // Menandakan apakah solusi ditemukan selama pencarian

    // Konstruktor untuk SearchAlgorithms
    public SearchAlgorithms(Grid grid, JPanel panel) {
        this.grid = grid;
        this.panel = panel;
    }

    // Metode run yang dijalankan saat thread dimulai
    @Override
    public void run() {
        if (MyUtils.solving) {
            MyUtils.breakAlgo = false;
            solutionFound = false;
            switch (MyUtils.algorithm) {
                case 0:
                    bfs(grid.getStart());
                    break;
                case 1:
                    dfs(grid.getStart());
                    break;
            }
        }
        MyUtils.solving = false;
        if (MyUtils.breakAlgo) {
            grid.inisialisasiGrid();
        }
        panel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        panel.revalidate();
        panel.repaint();
    }

    // Metode untuk menjalankan algoritma DFS
    private void dfs(Node start) {
        dfsUntill(start);
    }

    // Metode rekursif untuk menjalankan DFS hingga solusi ditemukan
private void dfsUntill(Node node) {
    // Memeriksa apakah proses pencarian sedang berjalan atau solusi sudah ditemukan
    if (!MyUtils.solving || solutionFound) {
        return;
    }

    // Menandai node saat ini sebagai node yang sedang dieksplorasi
    node.setType(Type.CURRENT);
    node.setAlreadyVisited(true);

    // Memperbarui tampilan panel
    panel.revalidate();
    panel.repaint();

    // Menghentikan proses untuk waktu tertentu (jeda)
    jeda(MyUtils.jeda);

    // Jika node saat ini adalah node finish, ekstrak solusi
    if (node.equals(grid.getFinish())) {
        extractSolution(node);
        MyUtils.solving = false;
        solutionFound = true;
        return;
    } else {
        // Jika bukan node finish, tandai node saat ini sebagai node yang telah dikunjungi
        node.setType(Type.VISITED);

        // Dapatkan tetangga-tetangga dari node saat ini
        LinkedList<Node> children = node.getNeighbors(grid);

        for (Node child : children) {
            // Tandai tetangga-tetangga sebagai node frontier (jika belum dikunjungi)
            if (!solutionFound) {
                for (Node temp : children) {
                    if (temp.equals(child)) {
                        continue;
                    }
                    if (!temp.getType().equals(Type.VISITED)) {
                        temp.setType(Type.FRONTIER);
                    }
                }
            }

            // Lanjutkan pencarian rekursif pada tetangga yang belum dikunjungi
            dfsUntill(child);
        }
    }
}


    // Metode untuk menjalankan algoritma BFS
    private void bfs(Node startingNode) {
        // Membuat antrian (queue) frontier untuk menyimpan node-node yang akan dieksplorasi
        Queue<Node> frontier = new LinkedList<Node>();
    
        // Node saat ini yang sedang dieksplorasi
        Node currentNode = null;
    
        // Menambahkan startingNode ke dalam antrian frontier
        frontier.add(startingNode);
    
        // Melakukan iterasi selama proses pencarian berlangsung, antrian tidak kosong, dan solusi belum ditemukan
        while (MyUtils.solving && !frontier.isEmpty() && !solutionFound) {
            // Mengambil dan menghapus node pertama dari antrian frontier
            currentNode = frontier.poll();
    
            // Menandai node saat ini sebagai node yang sedang dieksplorasi
            currentNode.setType(Type.CURRENT);
    
            // Memperbarui tampilan panel
            panel.revalidate();
            panel.repaint();
    
            // Menghentikan proses untuk sementara waktu (jeda)
            jeda(MyUtils.jeda);
    
            // Pengecekan apakah node saat ini adalah node finish
            if (currentNode.equals(grid.getFinish())) {
                // Jika ya, ekstrak solusi, berhenti pencarian, dan set solutionFound menjadi true
                extractSolution(currentNode);
                MyUtils.solving = false;
                solutionFound = true;
                continue;
            } else {
                // Jika bukan node finish, tandai node saat ini sebagai node yang telah dikunjungi
                currentNode.setType(Type.VISITED);
    
                // Untuk setiap tetangga dari node saat ini
                for (Node neighbor : currentNode.getNeighbors(grid)) {
                    // Tambahkan tetangga ke dalam antrian frontier
                    frontier.add(neighbor);
    
                    // Tandai tetangga sebagai node frontier dan set sudah dikunjungi
                    neighbor.setType(Type.FRONTIER);
                    neighbor.setAlreadyVisited(true);
                }
            }
        }
    }
    

    // Metode untuk mengekstrak solusi dari node akhir ke node awal
    public void extractSolution(Node node) {
        if (!MyUtils.solving) {
            return;
        }

        Node parent = node.getParent();

        while (!grid.getStart().equals(parent)) {
            parent.setType(Type.PATH);
            panel.revalidate();
            panel.repaint();
            jeda(10);
            parent = parent.getParent();
        }
        panel.revalidate();
        panel.repaint();
    }

    // Metode untuk memberikan jeda dalam pembaruan GUI
    public void jeda(int jeda) {
        try {
            Thread.sleep(jeda);
            panel.repaint();
        } catch (InterruptedException e) {
        }
    }
}
