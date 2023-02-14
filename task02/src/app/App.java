package app;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class App {

    public static Map<String, List<NextWord>> nextWordCount = new TreeMap<>();
    
    public static void main (String[] args) {
        
        String dirName = args[0];

        File dir = new File(dirName);

        File[] fileNames = dir.listFiles();
        for (File f : fileNames) {
            try {
                textCountToMap(f);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        for(String k : nextWordCount.keySet()) {
            System.out.println(k);
            for (NextWord nw : nextWordCount.get(k)){
                System.out.printf("    %s %.2f\n", nw.getWord(), wordProbability(nw, nextWordCount.get(k)));
            }
        }
    }

    public static void textCountToMap(File f) throws IOException {
        InputStream is = new FileInputStream(f);
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);

        // store text file as String fullText
        String line = "";
        String fullText = "";
        while ((line = br.readLine()) != null) {
            fullText += (line.toLowerCase() + " ");
        }

        // get rid of quote marks to keep contractions
        fullText = fullText.replaceAll("\s\'", " ");
        fullText = fullText.replaceAll("\'\s", " ");
        
        // split fullText into an array of individual words
        String[] words = fullText.split("[:\" .,!?(){}-]+");

        NextWord next;
        List<NextWord> nextWordList;
        List<String> nextWordListCheck;
        List<NextWord> temp;

        // iterate and stop at second last word
        for (int i = 0; i < words.length-1; i++) {
            // map does not contain the first word as key,
            // key = first word, value = list with NextWord
            // list should the first NextWord(next word, 1)
            if (!nextWordCount.containsKey(words[i])) {
                next = new NextWord(words[i+1]);
                nextWordList = new ArrayList<>();
                nextWordList.add(next);
                nextWordCount.put(words[i], nextWordList);
            } else {
                // map already has word as key
                // check if the next word already exists in values
                // if yes, get index
                nextWordListCheck = new ArrayList<>();
                int index; 
                for (NextWord n : nextWordCount.get(words[i])) {
                    nextWordListCheck.add(n.getWord());
                }
                temp = nextWordCount.get(words[i]);
                // next word is in list, increment count in temp list
                if (nextWordListCheck.contains(words[i+1])) {
                    index = nextWordListCheck.indexOf(words[i+1]);
                    temp.get(index).setCount(temp.get(index).getCount() + 1);
                } else { 
                    // next word is not in list, add new NextWord object to temp list
                    next = new NextWord(words[i+1]);
                    temp.add(next);
                }
                // replace map value list with temp list
                nextWordCount.replace(words[i], nextWordCount.get(words[i]), temp);
            }
        }
        br.close();
        isr.close();
        is.close();
    }

    // calculate sum of all word counts
    // divide word count by sum
    public static float wordProbability (NextWord word, List<NextWord> list) {
        float sumCount = 0f;
        for (NextWord n : list) {
            sumCount += n.getCount();
        }
        return word.getCount()/sumCount;
    }
}
