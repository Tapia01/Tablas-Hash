package hash;

public class Hash2 {
    public int hash(String key, int tableSize) {
        int hash = 1;

        for (int i = 0; i < key.length(); i++) {
            hash = (hash * 31) + key.charAt(i);
        }

        hash %= tableSize;
        if (hash < 0) {
            hash += tableSize;
        }

        return hash;
    }
}
