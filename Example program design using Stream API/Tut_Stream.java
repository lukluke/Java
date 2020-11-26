
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import static java.util.Comparator.comparing;
import static java.util.Comparator.comparingInt;
import static java.util.Comparator.reverseOrder;
import java.util.Map;
import java.util.Scanner;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;


public class Tut_Stream 
{
    public static void main(String[] args) throws IOException
    {
        String fname = "videoData.txt";        
        
        System.out.println("Top 10 most popular videos (Video ID, view count):");
        
        Function<String, VideoRec> mapper = line -> {
                String[] token = line.split(",");
                return new VideoRec(Long.parseLong(token[0]), token[1], token[2]);
            };

        /* --------------------------------------------------------------

        BiConsumer<ArrayList<Pair<String, Integer>>, VideoRec> acc1 =
            (result, v) -> {
                if (result.isEmpty())
                    result.add(new Pair(v.getVid(), 1));
                else 
                {
                    Pair<String, Integer> item = result.get(result.size()-1);                   
                    if (item.getFirst().equals(v.getVid()))
                        item.setSecond(item.getSecond()+1);
                    else
                        result.add(new Pair(v.getVid(), 1));
                }
            };
        
        BiConsumer<ArrayList<Pair<String, Integer>>, Pair<String, Integer>> acc2 =
            (result, p) -> {
                if (result.size() < 10 || result.get(9).getSecond() <= p.getSecond())
                    result.add(p);
            }; 

        Path filepath = Paths.get(filename);
        try (Stream<String> lines = Files.lines(filepath))
        {
            lines.map(mapper)
                 .sorted(comparing(VideoRec::getVid))
                 .collect(ArrayList::new, acc1, ArrayList::addAll)
                 .stream()
                 .sorted((a, b) -> b.getSecond().compareTo(a.getSecond()))
                 .collect(ArrayList::new, acc2, ArrayList::addAll)
                 .forEach(System.out::println);
        }

        -------------------------------------------------------------------- */
        
        Path filepath = Paths.get(filename);
        try (Stream<String> lines = Files.lines(filepath))
        {
            lines.map(mapper)
                 .collect(Collectors.groupingBy(VideoRec::getVid,
                                                Collectors.counting()))
                 .entrySet()
                 .stream()
                 .sorted((Map.Entry<String, Long> e1, Map.Entry<String, Long> e2) 
                              -> e2.getValue().compareTo(e1.getValue()))
                 .limit(10)
                 .forEach(System.out::println);
        }
    }        
}
