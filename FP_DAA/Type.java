package FP_DAA;

// Enumerasi yang merepresentasikan jenis-jenis node pada grid
public enum Type {
    START,       // Node awal
    FINISH,      // Node akhir
    CURRENT,     // Node saat ini selama pencarian
    VISITED,     // Node yang telah dikunjungi selama pencarian
    FRONTIER,    // Node yang menjadi bagian dari frontier selama pencarian
    PATH,        // Node yang terletak pada solusi/path
    DEFAULT,     // Jenis default node
    WALL         // Node yang merupakan tembok/hambatan
}
