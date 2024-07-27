package hash;

public class Hash1 {
    public int hash(String key) {
        int hash = 0;
        for (int i = 0; i < key.length(); i++) {
            hash += key.charAt(i); // Suma
            hash *= 31; // Multiplicación
        }

        if (hash < 0) {
            hash = -hash;
        }

        return hash;
    }
}
