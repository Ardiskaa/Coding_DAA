package FP_DAA;

// Kelas utilitas MyUtils berisi variabel-variabel yang digunakan secara global dalam program
public class MyUtils {
    // Variabel jeda untuk mengatur waktu tunda dalam visualisasi
    public static int jeda = 30;

    // Variabel algorithm untuk menyimpan pilihan algoritma pencarian
    // 0: Breadth First Search (bfs)
    // 1: Depth First Search (dfs)
    public static int algorithm = 0;

    // Variabel solving untuk menentukan apakah proses pencarian sedang berlangsung
    public static boolean solving = false;

    // Variabel breakAlgo untuk memberhentikan eksekusi algoritma secara tiba-tiba
    public static boolean breakAlgo = false;

    // Variabel stopped untuk menandakan apakah proses pencarian telah dihentikan
    public static boolean stopped = false;
}
