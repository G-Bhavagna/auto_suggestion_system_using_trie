package com.searchSuggestion.Suggestion;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Controller
public class SuggestionController {

    // Create a single Trie instance
    private Trie trie = new Trie();
    SuggestionController(){
        trie.insert("jimmy");
        trie.insert("jam");
        trie.insert("jyothi");
        trie.insert("bhava");
        trie.insert("bhutan");
    }
    @GetMapping("/")
    public String home() {
        // Initialize the Trie with words
        trie.intialWords();
        return "index";
    }

    @PostMapping("/search")
    public String search() {
        // Handle form submission
        return "result";
    }

    @GetMapping("/getSuggestions")
    @ResponseBody
    public List<String> getSuggestions(@RequestParam String prefix) {
        // Use the existing Trie instance initialized in home() method
        return trie.advancedSearch(prefix);
    }
}

class Node {
    Node links[] = new Node[26];
    boolean isEnd = false;

    boolean contains(char ch) {
        return links[ch - 'a'] != null;
    }

    void set() {
        isEnd = true;
    }

    boolean isEnd() {
        return isEnd;
    }

    Node get(char ch) {
        return links[ch - 'a'];
    }

    void put(char ch, Node node) {
        links[ch - 'a'] = node;
    }
}

class Trie {
    Node root;

    Trie() {
        root = new Node();
    }

    void insert(String str) {
        Node node = root;
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            if (!node.contains(ch)) {
                node.put(ch, new Node());
            }
            node = node.get(ch);
        }
        node.set();
    }

    List<String> advancedSearch(String str) {
        List<String> arr = new ArrayList<>();
        Node node = root;
        for (int i = 0; i < str.length(); i++) {
            if (node.contains(str.charAt(i))) {
                node = node.get(str.charAt(i));
                if(node==null) break;
            }
        }
        searchWords(node, arr, str);
        System.out.println(arr);
        return arr;
    }

    void searchWords(Node node, List<String> auto, String word) {
        if (node == null) return;

        if (node.isEnd) auto.add(word);

        Node[] nodes = node.links;

        for (int i = 0; i < nodes.length; i++) {
            if (nodes[i] != null) {
                char ch = (char) (97 + i);
                searchWords(nodes[i], auto, word + String.valueOf(ch));
            }
        }
    }

    public void intialWords() {
        for (int i = 0; i < 100; i++) {
            String randomWord = generateRandomWord(3, 8);
            insert(randomWord);
        }
    }

    static String generateRandomWord(int minLength, int maxLength) {
        Random random = new Random();
        int length = random.nextInt(maxLength - minLength + 1) + minLength;

        StringBuilder word = new StringBuilder();
        for (int i = 0; i < length; i++) {
            char randomChar = (char) (random.nextInt(26) + 'a');
            word.append(randomChar);
        }

        return word.toString();
    }
}
