package poet;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import org.junit.Test;

/**
 * Tests for GraphPoet.
 */
public class GraphPoetTest {
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; 
    }
    
    private static final GraphPoet instantiateGraph(String source) {
        try {
            // Use absolute paths for the test files
            final File corpus = new File("C:\\Users\\user\\OneDrive\\Desktop\\5thSemester\\Lab 9\\Lab 9\\ps2\\test\\resources\\" + source);
            GraphPoet graphPoet = new GraphPoet(corpus);
            return graphPoet;
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    // Instantiate GraphPoet objects using absolute paths
    final GraphPoet graphOneWord = instantiateGraph("TestOneWord.txt");
    final GraphPoet graphOneLine = instantiateGraph("TestOneLine.txt");
    final GraphPoet graphMultipleLines = instantiateGraph("TestMultipleLines.txt");
    
    // Tests for GraphPoet()
    @Test
    // covers corpus contains one word
    public void testGraphPoet_OneWord() {        
        List<String> corpusWords = graphOneWord.getCorpusWords();
        
        assertEquals("Expected corpus to contain one word",
                1, corpusWords.size());
        assertTrue("Expected word in lowercase",
                corpusWords.contains("here!"));
    }

    @Test
    // covers corpus contains one line
    public void testGraphPoet_OneLine() {
        List<String> corpusWords = graphOneLine.getCorpusWords();
        
        assertEquals("Expected all words in corpus",
                13, corpusWords.size());
        assertTrue("Expected words in lowercase",
                corpusWords.contains("to"));
    }

    @Test
    // covers corpus contains multiple line
    public void testGraphPoet_MultipleLine() {
        List<String> corpusWords = graphMultipleLines.getCorpusWords();
        
        assertNotEquals("Expected non-empty list", Collections.EMPTY_LIST, corpusWords);
        assertTrue("Expected words in lowercase",
                corpusWords.contains("don't"));
        assertTrue("Expected words in lowercase",
                corpusWords.contains("angelou"));
    }
    
    // Tests for poem()
    @Test
    // covers one word
    public void testPoem_OneWord() {
        String input = "Like";
        String output = graphMultipleLines.poem(input);
        
        assertEquals("Expected unchanged input", input, output);
    }

    @Test
    // covers multiple words
    public void testPoem_MultipleWords() {
        String input = "Seek to explore new and exciting synergies!";
        String output = graphOneLine.poem(input);
        String expected = "Seek to explore strange new life and exciting synergies!";
        
        assertEquals("Expected poetic output with words in input unchanged",
                expected, output);
    }

    @Test
    // covers word pairs with adjacency count > 1   
    public void testPoem_MultipleAdjacencies() {
        String input = "you MAY me";
        String output = graphMultipleLines.poem(input);
        
        assertNotEquals("Expected a bridge word inserted",
                input, output);
        assertTrue("Expected input words unchanged",
                output.contains("you") && output.contains("MAY"));
        assertTrue("Expected correct bridge word", 
                output.contains("write")
                || output.contains("trod")
                || output.contains("kill")
                || output.contains("cut")
                || output.contains("shoot"));
    }
}
