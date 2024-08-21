import java.util.function.Function;
class BloomFilter {
    Function<String, Integer>[] functions;
    boolean[] bloomFilter;
    int bloomSize;
    BloomFilter(int bloomSize, Function<String, Integer>[] functions) {
        this.functions = functions; // functions that will be used
        this.bloomSize = bloomSize; // size of the bloom filter
        bloomFilter = new boolean[bloomSize]; // defaults to false
    }
    void exampleFunctions() {
        functions = new Function[3];
        functions[0] = (String s) -> s.length() % bloomSize;
        functions[1] = (String s) -> s.charAt(0) % bloomSize;
        functions[2] = (String s) -> s.charAt(s.length() - 1) % bloomSize;
    }
    void addToBloom(String x) { // O(k), k = number of functions
        for (Function<String, Integer> function : functions) { // for each function
            bloomFilter[function.apply(x)] = true; // add to bloom
        }
    }
    boolean isInBloom(String x) { // O(k), k = number of functions
        for (Function<String, Integer> function : functions) { // for each function
            if (!bloomFilter[function.apply(x)]) // if not in bloom
                return false; // One function is not in bloom -> false
        }
        return true; // If all functions are present in bloom -> true
    }
}