package dba.ToDoApp.models;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

//import javax.validation.constraints.NotBlank;


@Data
@Document(collection = "todo")
public class ToDoItem {
    @Id
    private String id;
    @NotBlank
    private String title;
    private boolean completed;

    public ToDoItem(String title, boolean completed) {
        this.title = title;
        this.completed = completed;
    }
}
