package com.example.todolist;

public class Task {
    private String dateAdded;
    private String dateLastEdited;
    private String taskTitle;
    private String taskText;

    public Task(String dateAdded, String dateLastEdited, String taskTitle, String taskText) {
        this.dateAdded = dateAdded;
        this.dateLastEdited = dateLastEdited;
        this.taskTitle = taskTitle;
        this.taskText = taskText;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }

    public String getDateLastEdited() {
        return dateLastEdited;
    }

    public void setDateLastEdited(String dateLastEdited) {
        this.dateLastEdited = dateLastEdited;
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    public String getTaskText() {
        return taskText;
    }

    public void setTaskText(String taskText) {
        this.taskText = taskText;
    }

    @Override
    public String toString() {
        return dateAdded + "`" + dateLastEdited + "`" + taskTitle + "`" + taskText ;
    }
}
