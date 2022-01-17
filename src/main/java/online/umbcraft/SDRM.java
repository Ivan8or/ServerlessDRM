package online.umbcraft;

import online.umbcraft.hash.HashHelper;
import online.umbcraft.web.KeyScraper;
import online.umbcraft.web.SortedKeyScraper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class SDRM {

    public static void main(String[] args) throws ExecutionException, InterruptedException, IOException {

        File unsorted = new File("C:\\Users\\ivant\\IdeaProjects\\ServerlessDRM\\src\\main\\resources\\demo-keys.txt");
        FileWriter unsortedw = new FileWriter(unsorted);

        File sorted = new File("C:\\Users\\ivant\\IdeaProjects\\ServerlessDRM\\src\\main\\resources\\demo-keys-sorted.txt");
        FileWriter sortedw = new FileWriter(sorted);
        unsortedw.write("----------BEGIN KEYS----------");
        sortedw.write("----------BEGIN KEYS----------");

        List<String> hashes = new ArrayList<>(1000);
        for(int i = 0; i < 1000; i++) {
            String toDigest = i+"";
            String hash = HashHelper.getHash(toDigest);
            hashes.add(hash);
            unsortedw.write("\n"+hash);
        }
        Collections.sort(hashes);
        for(String s: hashes) {
            sortedw.write("\n"+s);
        }
        sortedw.close();
        unsortedw.close();

        System.exit(0);

        KeyScraper pks1 = new SortedKeyScraper();
        String keyhash = HashHelper.getHash("449999");

        String url = "https://raw.githubusercontent.com/Ivan8or/ServerlessDRM/master/src/main/resources/demo-keys-sorted.txt";

        int trials = 50;

        long[] v1times = new long[trials];

        for(int i = 0; i < trials; i++) {
            long milis = System.currentTimeMillis();
            Future<Boolean> exists = pks1.containsKey(url, keyhash);
            exists.get();
            long milisf = System.currentTimeMillis();
            System.out.println("v1 milis: " + (milisf - milis));
            v1times[i] = (milisf - milis);
        }
        System.out.println("\n\n");

        long average1 = Arrays.stream(v1times).sum() / trials;
        System.out.println("v1: "+Arrays.toString(v1times));
        System.out.println("v1 average: "+average1);

        KeyScraper.getExecutorService().shutdown();
    }


}
