package dba.ToDoApp.repositories;

import dba.ToDoApp.models.ToDoItem;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ToDoItemRepository extends MongoRepository<ToDoItem, String> {

    int countAllByCompleted(boolean completed);
    List<ToDoItem> findAllByCompleted(boolean completed);

}
