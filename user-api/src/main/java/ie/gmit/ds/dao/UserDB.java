package ie.gmit.ds.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ie.gmit.ds.User;

public class UserDB {
	 
	public static HashMap<Integer, User> users = new HashMap<>();
   
	static{
    	users.put(1, new User(1, "a", "a@a.com", "123456", "hH0UCayHs0fdeBU76VMCUoRFyQTQnfzVzRbtIXkRzfc=".getBytes(), "fKt+KANV54fISpVGBXOzv5Uv/jxGfPloWOrkyTC2jeA=".getBytes()));
    	users.put(2, new User(2, "b", "b@b.com", "123456", "hH0UCayHs0fdeBU76VMCUoRFyQTQnfzVzRbtIXkRzfc=".getBytes(), "fKt+KANV54fISpVGBXOzv5Uv/jxGfPloWOrkyTC2jeA=".getBytes()));
    	users.put(3, new User(3, "c", "c@c.com", "123456", "hH0UCayHs0fdeBU76VMCUoRFyQTQnfzVzRbtIXkRzfc=".getBytes(), "fKt+KANV54fISpVGBXOzv5Uv/jxGfPloWOrkyTC2jeA=".getBytes()));
    }
	
	public static List<User> getUsers(){
		return new ArrayList<User>(users.values());
	}
	
	public static void addUser(User u) {
		users.put(u.getUserID(), u);
	}
	
	public static User getUser(Integer id) {
		return users.get(id);
	}
	
	public static void updateUser(Integer id, User user){
		users.put(id, user);
    }
     
    public static void removeUser(Integer id){
    	users.remove(id);
    }
    
    public static boolean userExists(Integer id) {
    	return users.containsKey(id);
    }
}
