package utility;

import java.util.*;
import java.util.stream.Collectors;

public class CosineSimilarity
{
    public static double cosineSimilarity(String left, String right)
    {
        if(right.length() == 1)
            return right.charAt(0) == left.charAt(0) ? -1 : 0;
        String[] leftCount = ngrams(2, left);
        String[] rightCount = ngrams(2, right);
        HashSet<String> set = new HashSet<>();

        set.addAll(Arrays.asList(leftCount));
        set.addAll(Arrays.asList(rightCount));

        Map<String, Integer> leftFreq = count(leftCount);
        Map<String, Integer> rightFreq = count(rightCount);

        List<Integer> leftList = set.stream().map(x -> leftFreq.getOrDefault(x,0)).collect(Collectors.toList());
        List<Integer> rightList = set.stream().map(x -> rightFreq.getOrDefault(x,0)).collect(Collectors.toList());

        return -dot(leftList, rightList)/(mag(leftList)*mag(rightList));
    }
    private static int dot(List<Integer> a, List<Integer> b)
    {
        int rez = 0;
        for(int i=0;i<a.size();i++)
            rez += a.get(i) * b.get(i);

        return rez;
    }
    private static double mag(List<Integer> a)
    {
        return Math.sqrt(a.stream().map(x -> x * x).reduce(0, Integer::sum));
    }
    private static Map<String, Integer> count(String[] text)
    {
        Map<String, Integer> map = new HashMap<>();
        for(var letter : text)
        {
            if(!map.containsKey(letter))
                map.put(letter,0);
            map.put(letter, map.get(letter) + 1);
        }
        return map;
    }
    public static String[] ngrams(int n, String str) {
        String[] ngrams = new String[str.length() - 1];
        for (int i=0;i<str.length()-n+1;i++)
            ngrams[i] = str.substring(i, i + n);
        return ngrams;
    }

}
