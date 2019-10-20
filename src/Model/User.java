package Model;

/**
 *
 * @author Schaffeld, B. (000790777);
 */
public class User {

    private int id;
    private String firstName;
    private String lastName;
    private String username;
    private String email;

//    public User(ArrayList<String> arrayList) {
//        this.id = Integer.parseInt(arrayList.get(0));
//        this.firstName = arrayList.get(1);
//        this.lastName = arrayList.get(2);
//        this.username = arrayList.get(3);
//        this.email = arrayList.get(4);
//    }
    public User(int id, String firstName, String lastName, String username, String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
    }

    public User(String firstName, String lastName, String username, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "User{" + "id=" + id + ", username=" + username + ", firstName=" + firstName + ", lastName=" + lastName + ", email=" + email + '}';
    }

}
