package performance;

import online.umbcraft.hash.HashHelper;
import online.umbcraft.web.GreedyHashParser;
import online.umbcraft.web.HashParser;
import online.umbcraft.web.StreamedHashParser;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class PerformanceMeasures {

    public static void main(String[] args) throws ExecutionException, InterruptedException, IOException {

        HashParser v3scraper = new GreedyHashParser();
        HashParser v4scraper = new StreamedHashParser();


        String[] sizes = {"1k", "10k", "100k", "500k", "1000k"};
        int[] trialsAmount = {200, 200, 50, 20, 20};

        // very last token
        //int[] tokenVal = {999, 9999, 99999, 499999, 999999};

        // middle token
        //int[] tokenVal = {499, 4999, 49999, 249999, 499999};

        // first token
        int[] tokenVal = {0, 0, 0, 0, 0};

        String baseUrl = "https://raw.githubusercontent.com/Ivan8or/ServerlessDRM/master/demos/demo-";
        //String baseUrl = "https://www.umbcraft.online/download/demo-";

        //warmup
        System.out.println("warming up");
        trials(v3scraper, baseUrl + "100k-sorted.txt", 5000, 3);


        for(int size = 0; size < sizes.length; size++) {
            System.out.println("\n\n");
            int trials = trialsAmount[size];
            String sortedURL = baseUrl + sizes[size] + "-sorted.txt";
            String unsortedURL = baseUrl + sizes[size] + ".txt";

            long[] v3times = trials(v3scraper, unsortedURL, tokenVal[size], trials);
            long[] v4times = trials(v4scraper, unsortedURL, tokenVal[size], trials);

            System.out.println("Size: "+sizes[size]);
            stats(v3times, "greedy");
            stats(v4times, "streamed");
        }


        HashParser.getExecutorService().shutdown();
    }

    private static void stats(long[] trials, String label) {
        long average1 = Arrays.stream(trials).sum() / trials.length;
        //System.out.println(label+": " + Arrays.toString(trials));
        System.out.println(label+" average: " + average1+"ms");
    }

    private static long[] trials(HashParser scraper, String url, int index, int num)
            throws MalformedURLException, ExecutionException, InterruptedException {

        String keyhash = HashHelper.getHash(index+"");

        long[] times = new long[num];

        for (int i = 0; i < num; i++) {
            long milis = System.currentTimeMillis();
            Future<Boolean> exists = scraper.containsKey(url, keyhash);
            exists.get();
            long milisf = System.currentTimeMillis();
            System.out.print(".");
            times[i] = (milisf - milis);
        }
        System.out.println();
        return times;
    }

}
