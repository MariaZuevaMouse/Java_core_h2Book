package stream1.parallel;

import static java.util.stream.Collectors.*;

import java.io.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

public class ParallelStreams {
    public static void main(String[] args) throws IOException {
        String contents = new String(Files.readAllBytes(Paths.get("alice30.txt")), StandardCharsets.UTF_8);

        List<String> wordList = Arrays.asList(contents.split("\\PL+"));

        //bad code
        int[] shortWords = new int[10];
        wordList.parallelStream().forEach(s->{
            if(s.length() <10) shortWords[s.length()]++;
        });
        System.out.println(Arrays.toString(shortWords));

        //bad code 2
        Arrays.fill(shortWords, 0);
        wordList.parallelStream().forEach(s ->{
            if(s.length()<10)  shortWords[s.length()]++;
        });
        System.out.println(Arrays.toString(shortWords));

        // good solution
        Map<Integer, Long> shortWordsCount = wordList
                .parallelStream().filter(s->s.length()<10)
                .collect(groupingBy(String::length, counting()));

        System.out.println(shortWordsCount);

        //second solution
        Map<Integer, List<String>> result =
                wordList.parallelStream().collect(
                        Collectors.groupingByConcurrent(String::length));
        System.out.println(result.get(14));

        result = wordList.parallelStream().collect(
                Collectors.groupingByConcurrent(String::length));
        System.out.println(result.get(14));

        Map<Integer, Long> wordCounts = wordList.parallelStream()
                .collect(groupingByConcurrent(String::length, counting()));

        System.out.println(wordCounts);



    }
}
