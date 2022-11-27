package dba.ToDoApp.controllers;

import dba.ToDoApp.config.TodoItemNotFoundException;
import dba.ToDoApp.models.ToDoItem;
import dba.ToDoApp.repositories.ToDoItemRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/")
public class TodoItemController {
    private final ToDoItemRepository toDoItemRepository;

    public TodoItemController(ToDoItemRepository toDoItemRepository) {
        this.toDoItemRepository = toDoItemRepository;
    }

    @GetMapping
    public String index(Model model) {
        addAttributesForIndex(model, ListFilter.ALL);
        return "index";
    }
    @GetMapping("/active")
    public String indexActive(Model model) {
        addAttributesForIndex(model, ListFilter.ACTIVE);
        return "index";
    }

    @GetMapping("/completed")
    public String indexCompleted(Model model) {
        addAttributesForIndex(model, ListFilter.COMPLETED);
        return "index";
    }

    private void addAttributesForIndex(Model model,
                                       ListFilter listFilter) {
        model.addAttribute("item", new TodoFormData());
        model.addAttribute("filter", listFilter);
        model.addAttribute("todos", getTodoItems(listFilter));
        model.addAttribute("totalNumberOfItems", toDoItemRepository.count());
        model.addAttribute("numberOfActiveItems", getNumberOfActiveItems());
        model.addAttribute("numberOfCompletedItems", getNumberOfCompletedItems());
    }

    @PostMapping
    public String addNewTodoItem(@Valid @ModelAttribute("item") TodoFormData formData) {
        toDoItemRepository.save(new ToDoItem(formData.getTitle(), false));
        return "redirect:/";
    }

    @PostMapping("/{id}/toggle")
    public String toggleSelection(@PathVariable("id") String id) {
        ToDoItem todoItem = toDoItemRepository.findById(id)
                .orElseThrow(() -> new TodoItemNotFoundException(id));

        todoItem.setCompleted(!todoItem.isCompleted());
        toDoItemRepository.save(todoItem);
        return "redirect:/";
    }

    @GetMapping("/edit/{id}")
    public String update(@PathVariable("id") String id, Model model) {
        ToDoItem todoItem = toDoItemRepository.findById(id)
                .orElseThrow(() -> new TodoItemNotFoundException(id));
        model.addAttribute("item", new TodoFormData(todoItem.getTitle()));
        model.addAttribute("id", id);
        return "update";
    }

    @PutMapping("/toggle-all")
    public String toggleAll() {
        List<ToDoItem> todoItems = toDoItemRepository.findAll();
        for (ToDoItem todoItem : todoItems) {
            todoItem.setCompleted(!todoItem.isCompleted());
            toDoItemRepository.save(todoItem);
        }
        return "redirect:/";
    }
    @GetMapping("/delete/{id}")
    public String deleteTodoItem(@PathVariable("id") String id) {
        toDoItemRepository.deleteById(id);
        return "redirect:/";
    }
    @GetMapping("/clear/completed")
    public String deleteCompletedItems() {
        List<ToDoItem> items = toDoItemRepository.findAllByCompleted(true);
        for (ToDoItem item : items) {
            toDoItemRepository.deleteById(item.getId());
        }
        return "redirect:/";
    }
    private List<TodoItemDto> getTodoItems(ListFilter filter) {
        return switch (filter) {
            case ALL -> convertToDto(toDoItemRepository.findAll());
            case ACTIVE -> convertToDto(toDoItemRepository.findAllByCompleted(false));
            case COMPLETED -> convertToDto(toDoItemRepository.findAllByCompleted(true));
        };
    }
    private List<TodoItemDto> convertToDto(List<ToDoItem> todoItems) {
        return todoItems
                .stream()
                .map(todoItem -> new TodoItemDto(todoItem.getId(),
                        todoItem.getTitle(),
                        todoItem.isCompleted()))
                .collect(Collectors.toList());
    }
    public static record TodoItemDto(String id, String title, boolean completed) {
    }
    private int getNumberOfActiveItems() {
        return toDoItemRepository.countAllByCompleted(false);
    }

    private int getNumberOfCompletedItems() {
        return toDoItemRepository.countAllByCompleted(true);
    }

    public enum ListFilter {
        ALL,
        ACTIVE,
        COMPLETED
    }
}