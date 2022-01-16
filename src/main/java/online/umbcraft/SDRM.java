package online.umbcraft;

import online.umbcraft.hash.HashHelper;
import online.umbcraft.web.PlaintextKeyScraper;

import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class SDRM {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        
        PlaintextKeyScraper pks = new PlaintextKeyScraper();
        Future<Boolean> exists = pks.containsKey("https://raw.githubusercontent.com/Ivan8or/GoldenDupes/master/settings.gradle", "rootProject.name = 'GoldenDupes'");
        System.out.println("does key exist? "+exists.get());
    }
}
