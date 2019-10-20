package Model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Schaffeld, B. (000790777);
 */
public class Task {

    private int id;
    private String name;
    private String description;
    private Date dueDate;
    private double scope;
    private double severity;
    private double priority;
    private int userId;
    private String username;

    public Task(int id, String name, String description, Date dueDate, double scope, double severity, double priority, int userId, String username) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.dueDate = dueDate;
        this.scope = scope;
        this.severity = severity;
        this.priority = priority;
        this.userId = userId;
        this.username = username;
    }

    public Task(String name, String description, Date dueDate, double scope, double severity) {
        this.name = name;
        this.description = description;
        this.dueDate = dueDate;
        this.scope = scope;
        this.severity = severity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public double getScope() {
        return scope;
    }

    public void setScope(double scope) {
        this.scope = scope;
    }

    public double getSeverity() {
        return severity;
    }

    public void setSeverity(double severity) {
        this.severity = severity;
    }

    public double getPriority() {
        return priority;
    }

    public void setPriority(double priority) {
        this.priority = priority;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "Task{" + "id=" + id + ", name=" + name + ", description=" + description + ", dueDate=" + dueDate + ", scope=" + scope + ", severity=" + severity + ", priority=" + priority + ", userId=" + userId + ", username=" + username + '}';
    }

}
