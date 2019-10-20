package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Schaffeld, B. (000790777);
 */
public class Master {
    
    private static ObservableList<User> allUsers = FXCollections.observableArrayList();
    private static ObservableList<Task> allTasks = FXCollections.observableArrayList();
    
    public static ObservableList<User> getAllUsers(){
        return allUsers;
    }
    
    public static void setAllUsers(ObservableList<User> allUsers){
        Master.allUsers = allUsers;
    }
    
    public static void addUser(User user){
        allUsers.add(user);
    }
    
    public static void deleteUser(User user){
        allUsers.remove(user);
    }
    
    public static void deleteAllUsers(){
        allUsers.clear();
    }
    
    public static ObservableList<Task> getAllTasks(){
        return allTasks;
    }
    
    public static void setAllTasks(ObservableList<Task> allTasks){
        Master.allTasks = allTasks;
    }
    
    public static void addTask(Task task){
        allTasks.add(task);
    }
    
    public static void deleteTask(Task task){
        allTasks.remove(task);
    }
    
    public static void deleteAllTasks(){
        allTasks.clear();
    }
    
    
    
    
    
    
    
}
