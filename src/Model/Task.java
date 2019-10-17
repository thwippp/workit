package Model;

import java.util.Date;

/**
 *
 * @author Schaffeld, B. (000790777);
 */
public class Task {
    private int id;
    private String name;
    private String description;
    private Date dueDate;
    private double severity;
    private double scope;
    private double priority;
    private int userId;

    public Task(int id, String name, String description, Date dueDate, double severity, double scope, double priority, int userId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.dueDate = dueDate;
        this.severity = severity;
        this.scope = scope;
        this.priority = priority;
        this.userId = userId;
    }

    public Task(String name, String description, Date dueDate, double severity, double scope) {
        this.name = name;
        this.description = description;
        this.dueDate = dueDate;
        this.severity = severity;
        this.scope = scope;
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

    public double getSeverity() {
        return severity;
    }

    public void setSeverity(double severity) {
        this.severity = severity;
    }

    public double getScope() {
        return scope;
    }

    public void setScope(double scope) {
        this.scope = scope;
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

    @Override
    public String toString() {
        return "Task{" + "id=" + id + ", name=" + name + ", description=" + description + ", dueDate=" + dueDate + ", severity=" + severity + ", scope=" + scope + ", priority=" + priority + ", userId=" + userId + '}';
    }
    
}
