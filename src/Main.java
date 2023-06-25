import java.io.File;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.ForkJoinPool;

public class Main {
    private static final char[] sizeMultipliers = {
            'B', 'K', 'M', 'G', 'T'
    };
    private static final String[] sizeNames =
            {"b", "Kb", "Mb", "Gb", "Tb"};
    public static void main(String[] args) {
        String folderPath = "/Users/vcarstein/Desktop/projects";
        File file = new File(folderPath);

        long start = System.currentTimeMillis();

        FolderSizeCalculator calculator = new FolderSizeCalculator(file);
        ForkJoinPool pool = new ForkJoinPool();
        long size = pool.invoke(calculator);
        System.out.println(size);

        System.out.println(getHumanReadableSize(size));
        System.out.println(getSizeFromHumanReadable(getHumanReadableSize(size)));

        long duration = System.currentTimeMillis() - start;


        System.out.println(duration);

    }
    //24B, 243Kb, 36Mb, 34Gb, 42Tb
    public static String getHumanReadableSize(long size) {
        for (int i = 0; i <sizeMultipliers.length; i++) {
            double value = size / Math.pow(1024, i);
            if (value < 1024) {
                return Math.round(value) +
                        String.valueOf(sizeMultipliers[i]) +
                        (i > 0 ? "b" : "");
            }
        }
        return "bigger";
    }

    public static long getSizeFromHumanReadable(String size) {
        HashMap<Character, Integer> char2multiplier = getMultipliers();
        char sizeFactor = size
                .replaceAll("[0-9\\s+]+", "")
                .charAt(0);
        int multiplier = char2multiplier.get(sizeFactor);
        long length = multiplier * Long.parseLong(
                size.replaceAll("[^0-9]", ""));
        return length;
    }

    private static HashMap<Character, Integer> getMultipliers() {
        HashMap<Character, Integer> char2multiplier = new HashMap<>();
        for (int i = 0; i < sizeMultipliers.length; i++) {
            char2multiplier.put(
                    sizeMultipliers[i],
                    (int) Math.pow(1024, i));
        }
        return char2multiplier;
    }
}