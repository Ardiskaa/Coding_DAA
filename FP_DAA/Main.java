package FP_DAA;
import javax.swing.SwingUtilities;

public class Main {
    // Metode utama untuk menjalankan program
    public static void main(String[] args) {
        // Memastikan operasi antarmuka pengguna (UI) dijalankan pada thread yang benar
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Membuat objek WindowFrame untuk memulai aplikasi
                new WindowFrame();
            }
        });
    }
}
