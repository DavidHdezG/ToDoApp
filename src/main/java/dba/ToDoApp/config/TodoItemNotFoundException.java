package dba.ToDoApp.config;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class TodoItemNotFoundException extends RuntimeException {
    public TodoItemNotFoundException(String id) {
        super("Todo item with id " + id + " not found");
    }
}

