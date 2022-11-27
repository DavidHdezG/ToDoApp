package dba.ToDoApp.controllers;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;


public class TodoFormData {
    @NotBlank
    @Getter
    @Setter
    private String title;

    public TodoFormData(String title) {
        this.title = title;
    }

    public TodoFormData() {
    }
}