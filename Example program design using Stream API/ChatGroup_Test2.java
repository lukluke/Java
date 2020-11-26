import java.util.ArrayList;
import java.util.Arrays;
import static java.util.Comparator.comparing;
import java.util.List;
import java.util.Set;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ChatGroup_Test2 
{    
    public static List<User> getFriends_FunctionUtil(List<ChatGroup> groups, User u)
    {
        // Implementation using FunctionUtil.
        
        // Users in the same chat group are considered to be friends.
        // Return a list of users (without duplicate) that are friends 
        // of u derived from the list of chat groups.
        // Result list does not contain u himself/herself.
       
        List<ChatGroup> tempGroups =         
        FunctionUtil.filter(groups, 
                            g -> g.getUser(u.getTel()).isPresent());
        
        List<User> tempUsers =         
        FunctionUtil.transform(tempGroups, 
                               (r, g) -> r.addAll(Arrays.asList(g.getMembers())));
        
        /*  ------------- The above 2 steps may be combined -----------------
        List<User> tempUsers =         
        FunctionUtil.transform(groups,
                               (r, g) -> {
                                   if (g.getUser(u.getTel()).isPresent())
                                       r.addAll(Arrays.asList(g.getMembers()));
                               });
        ---------------------------------------------------------------------- */
        
        tempUsers.sort(comparing(User::getTel));
        
        return 
        FunctionUtil.transform(tempUsers, 
                               (r, t) -> {
                                   if (!t.getTel().equals(u.getTel()))
                                       if (r.isEmpty() ||
                                           !t.getTel().equals(r.get(r.size()-1).getTel()))
                                          r.add(t);
                               });
    }
    
    public static List<User> getFriends_Stream(List<ChatGroup> groups, User u)
    {
        // Implementation using Stream API.
        // Try different approaches to remove duplicates in the result list.
        // 1. Design your own accumulator.
        // 2. Use Collectors.toMap().
        // 3. Use Collectors.toSet().
        
        Function<ChatGroup, Stream<User>> mapper = g -> Arrays.stream(g.getMembers());
        
        return groups.stream()
                     .filter(g -> g.getUser(u.getTel()).isPresent())
                     .flatMap(mapper) // stream of User
                     .sorted()
                     .filter(t -> !t.getTel().equals(u.getTel()))
                     .distinct()  // if User.equals(User) is defined, computational expensive operator !!
                     .collect(Collectors.toList());
  
       /* -----------------  If User.equals(User) is not defined ------------------
                     .collect(ArrayList::new,
                              (ArrayList<User> result, User t) -> {
                                  if (result.isEmpty() || 
                                      !t.getTel().equals(result.get(result.size()-1).getTel()))
                                      result.add(t); 
                              },
                              ArrayList::addAll); 
        -------------------------------------------------------------------------- */
        
        /* ----------------- Design using Collectors.toMap() ---------------------

        // Function<User, String> keyMapper = User::getTel; 
        // Function<User, User> valueMapper = t -> t;
        // BinaryOperator<User> mergeFunction = (r1, r2) -> r2;
             
        return groups.stream()
                     .filter(g -> g.getUser(u.getTel()).isPresent())
                     .flatMap(mapper)
                     .filter(t -> !t.getTel().equals(u.getTel()))
                     .collect(Collectors.toMap(User::getTel,     // keyMapper,
                                               t -> t,           // valueMapper
                                               (r1, r2) -> r2 )) // mergeFunction
                     .values()  // Collection of User
                     .stream()
                     .collect(Collectors.toList()); 
        -------------------------------------------------------------------------- */
        
        /* ------------------- Design using Collectors.toSet() -------------------

        // Need to define User.equals(User) and User.hashCode()
        Set<User> result = groups.stream()
                                 .filter(g -> g.getUser(u.getTel()).isPresent())
                                 .flatMap(mapper)
                                 .filter(t -> !t.getTel().equals(u.getTel()))
                                 .collect(Collectors.toSet());
        
        return Arrays.asList(result.toArray(new User[result.size()]));
        -------------------------------------------------------------------------- */
        
    }
}
