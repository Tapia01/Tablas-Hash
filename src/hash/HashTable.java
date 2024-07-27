package hash;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class HashTable {
    private LinkedList<String>[] table;
    private int capacity;
    private Hash1 Function1;
    private Hash2 Function2;

    public HashTable(int capacity) {
        this.capacity = capacity;
        table = new LinkedList[capacity];
        for (int i = 0; i < capacity; i++) {
            table[i] = new LinkedList<>();
        }
        Function1 = new Hash1();
        Function2 = new Hash2();
    }

    public int getCapacity() {
        return capacity;
    }

    public void put(String key, String value, int functionNumber) {
        int index = hashFunction(key, functionNumber);
        String entry = key + ":" + value;
        table[index].add(entry);
    }

    public List<String> getDataAtIndex(int index) {
        List<String> data = new ArrayList<>();
        if (index < 0 || index >= capacity) {
            return data;
        }
        for (String entry : table[index]) {
            int separatorIndex = entry.indexOf(':');
            if (separatorIndex != -1) {
                String key = entry.substring(0, separatorIndex);
                String value = entry.substring(separatorIndex + 1);
                data.add("Key: " + key + ", Value: " + value);
            }
        }
        return data;
    }

    public String searchValueByKey(String key, int functionNumber) {
        String searchKey = key + ":";
        for (int i = 0; i < capacity; i++) {
            for (String entry : table[i]) {
                if (entry.startsWith(searchKey)) {
                    return "Índice: " + i + ", Valor: " + entry.substring(searchKey.length());
                }
            }
        }
        return null;
    }

    private int hashFunction(String key, int functionNumber) {
        if (functionNumber == 1) {
            return Function1.hash(key) % capacity;
        } else {
            return Function2.hash(key, capacity);
        }
    }

    public long measurePutTime(String key, String value, int functionNumber) {
        long startTime = System.nanoTime();
        put(key, value, functionNumber);
        long endTime = System.nanoTime();
        return endTime - startTime;
    }
}
