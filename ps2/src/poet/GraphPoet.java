/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package poet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

import graph.Graph;

public class GraphPoet {
    private final Graph<String> affinityGraph;
    private final List<String> corpusWords;
    
    public GraphPoet(File corpus) throws IOException {
        corpusWords = extractWordsFromFile(corpus);
        affinityGraph = generateAffinityGraph(corpusWords);
        checkRep();
    }
    private void checkRep() {
        assert affinityGraph != null;
    }
    /** Returns a list of words in lowercase, */
    private List<String> extractWordsFromFile(File corpus) throws IOException {
        List<String> words = new ArrayList<>();
        try (Scanner s = new Scanner(new BufferedReader(new FileReader(corpus)))) {
            while (s.hasNext()) {
                words.add(s.next().toLowerCase());
            }
        }
        assert words != Collections.EMPTY_LIST;
        return words;
    }
    /**
     
     * @return a graph whose vertices are lowercase words
     *         and edges are the counts of adjacencies
     */
    private Graph<String> generateAffinityGraph(List<String> words){
        Graph<String> graph = Graph.empty();
        
        for (int i = 0; i < words.size(); i++) {
            String source = words.get(i);
            graph.add(source);
            if ((i + 1) >= words.size()) {
                break;
            }
            String target = words.get(i + 1);
            int prev = graph.set(source, target, 1);
            graph.set(source, target, prev + 1);
        }
        return graph;
    }
    /** 
     * Returns the words in the affinity graph generated from the corpus
     * 
     * Words are defined as non-empty case-insensitive strings of non-space 
     * non-newline characters. They are delimited in the corpus by spaces, newlines, 
     * or the ends of the file.
     * 
     * @return list of words in the order they appear in the corpus. All words are
     *         in lowercase
     */
    public List<String> getCorpusWords() {
        return Collections.unmodifiableList(corpusWords);
    }
    /**
     * Generate a poem.
     * 
     * @param input string from which to create the poem
     * @return poem (as described above)
     */
    public String poem(String input) {
        String[] inputWords = input.split("\\s");
        StringBuilder poem = new StringBuilder(input);
        int fromIndex = 0;
        
        for (int i = 0; i < inputWords.length; i++) {
            if (i + 1 >= inputWords.length) {
                break;
            }
            Map<String, Integer> word1Targets = 
                    affinityGraph.targets(inputWords[i].toLowerCase());
            Map<String, Integer> word2Sources =
                    affinityGraph.sources(inputWords[i+1].toLowerCase());
            Set<String> probableBridges = word1Targets.keySet();
            
            List<String> allBridges = probableBridges.stream()
                    .filter(possibleBridge -> word2Sources.containsKey(possibleBridge))
                    .collect(Collectors.toList());
            
            if (!allBridges.isEmpty()) {
                Random rand = new Random();
                int  n = rand.nextInt(allBridges.size());
                String bridge = allBridges.get(n);
                // get the index of word 2 from the poem
                int insertAt = poem.indexOf(inputWords[i+1], fromIndex);
                // insert the bridge word before that word
                poem.insert(insertAt, bridge + " ");
            }
        }
        checkRep();
        return poem.toString();
    }
    
    @Override public String toString() {
        return affinityGraph.toString();
    }
}
