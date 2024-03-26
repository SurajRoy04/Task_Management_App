import java.sql.*;
import java.util.Scanner;
class Task {
private String description;
private boolean completed;
public Task(String description) {
this.description = description;
this.completed = false;
}
public String getDescription() {
return description;
}
public boolean isCompleted() {
return completed;
}
public void setCompleted(boolean completed) {
this.completed = completed;
}
}
class TaskManager {
private Connection connection;
public TaskManager(Connection connection) {
this.connection = connection;
}
public void addTask(String description) {
String insertQuery = "INSERT INTO tasks (description, completed) VALUES (?, ?)";
try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {
preparedStatement.setString(1, description);
preparedStatement.setBoolean(2, false);
int rowsAffected = preparedStatement.executeUpdate();
if (rowsAffected > 0) {
System.out.println("Task added: " + description);
} else {
System.out.println("Failed to add task");
}
} catch (SQLException e) {
e.printStackTrace();
}
}
public void editTask(int id, String newDescription) {
String updateQuery = "UPDATE tasks SET description = ? WHERE id = ?";
try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
preparedStatement.setString(1, newDescription);
preparedStatement.setInt(2, id);
int rowsAffected = preparedStatement.executeUpdate();
if (rowsAffected > 0) {
System.out.println("Task edited: " + newDescription);
} else {
System.out.println("Failed to edit task");
}
} catch (SQLException e) {
e.printStackTrace();
}
}
public void completeTask(int id) {
String updateQuery = "UPDATE tasks SET completed = true WHERE id = ?";
try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
preparedStatement.setInt(1, id);
int rowsAffected = preparedStatement.executeUpdate();
if (rowsAffected > 0) {
System.out.println("Task completed");
} else {
System.out.println("Failed to complete task");
}
} catch (SQLException e) {
e.printStackTrace();
}
}
public void listTasks() {
String selectQuery = "SELECT * FROM tasks";
try (Statement statement = connection.createStatement();
ResultSet resultSet = statement.executeQuery(selectQuery)) {
if (!resultSet.isBeforeFirst()) {
System.out.println("No tasks available");
} else {
System.out.println("Tasks:");
while (resultSet.next()) {
int id = resultSet.getInt("id");
String description = resultSet.getString("description");
boolean completed = resultSet.getBoolean("completed");
System.out.println(id + ". " + description + " - Completed: " + completed);
}
}
} catch (SQLException e) {
e.printStackTrace();
}
}
}
class TaskManagementApp{
private static final String JDBC_URL = "jdbc:mysql://localhost:3306/task_manager_db";
private static final String USERNAME = "root";
private static final String PASSWORD = "Wb@758004nak";
public static void main(String[] args) {
try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
TaskManager taskManager = new TaskManager(connection);
Scanner scanner = new Scanner(System.in);
while (true) {
System.out.println("\nCommand Options:");
System.out.println("1. Add Task");
System.out.println("2. Edit Task");
System.out.println("3. Complete Task");
System.out.println("4. List Tasks");
System.out.println("5. Exit");
System.out.print("Enter your choice (1-5): ");
int choice = scanner.nextInt();
scanner.nextLine(); // Consume newline
switch (choice) {
case 1:
System.out.print("Enter task description: ");
String description = scanner.nextLine();
taskManager.addTask(description);
break;
case 2:
System.out.print("Enter task id to edit: ");
int editId = scanner.nextInt();
scanner.nextLine(); // Consume newline
System.out.print("Enter new task description: ");
String newDescription = scanner.nextLine();
taskManager.editTask(editId, newDescription);
break;
case 3:
System.out.print("Enter task id to complete: ");
int completeId = scanner.nextInt();
taskManager.completeTask(completeId);
break;
case 4:
taskManager.listTasks();
break;
case 5:
System.out.println("Exiting Task Manager. Goodbye!");
scanner.close();
System.exit(0);
break;
default:
System.out.println("Invalid choice. Please enter a number between 1 and 5.");
}
}
} catch (SQLException e) {
e.printStackTrace();
}
}
}
