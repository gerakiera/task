package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;
import java.net.URL;

public class Main {
    public static void main(String[] args) throws IOException {

        URL url = new URL("https://github.com/PeacockTeam/new-job/releases/download/v1.0/lng-4.txt.gz");
        GZIPInputStream gzis = new GZIPInputStream(url.openStream());
        InputStreamReader reader = new InputStreamReader(gzis);
        BufferedReader in = new BufferedReader(reader);

        String readed;
        while ((readed = in.readLine()) != null) {
            System.out.println(readed);
        }
    }
}