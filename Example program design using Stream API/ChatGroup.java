import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

public class ChatGroup 
{
    private final String groupName;
    private final int groupID;
    private final ArrayList<User> members;
    
    public ChatGroup(String gName, int gid)
    {
        groupName = gName;
        groupID = gid;
        members = new ArrayList();  
    }

    public String getGroupName()
    {
        return groupName;
    }

    public int getGroupID()
    {
        return groupID;
    }
   
    public User[] getMembers()
    {
        return members.toArray(new User[members.size()]);
    }

    @Override
    public String toString()
    {
        StringBuilder buffer = new StringBuilder();
        buffer.append("Group name : " + groupName + "\n");
        buffer.append("Group ID : " + groupID + "\n");
        buffer.append("Members : \n");
        for (User u : members)
             buffer.append(u + "\n");
              
        return buffer.toString();
    }
   
    public Optional<User> getUser(String tel)
    {      
        User key = new User("", tel);
        int k = Collections.binarySearch(members, key);
        if (k >= 0)
            return Optional.of(members.get(k));
        else
            return Optional.empty();
    }

    public void addUser(User u)
    {        
        int k = Collections.binarySearch(members, u);
        if (k < 0)
            members.add(-(k+1), u);       
    }
        
    public void removeUser(User u)
    {       
        int k = Collections.binarySearch(members, u);
        if (k >= 0)
            members.remove(k);
    }
}
    
class User implements Comparable<User>
{
    private String name;
    private String tel;
    
    public User(String n, String t)
    {
        name = n;
        tel = t;
    }

    public String getName()
    {
        return name;
    }
    
    public String getTel()
    {
        return tel;
    }
    
    @Override
    public String toString()
    {
        return name + ", " + tel;
    }

    @Override
    public int compareTo(User other) 
    {
        return tel.compareTo(other.tel);
    }
    
    @Override
    public boolean equals(Object other)
    {
        if (other instanceof User)
        {
            User u = (User)other;
            return name.equalsIgnoreCase(u.name) && tel.equals(u.tel);
        }
        else
            return false;
    }
        
    @Override
    public int hashCode()
    {
        return name.toUpperCase().concat(tel).hashCode();
    }
}
