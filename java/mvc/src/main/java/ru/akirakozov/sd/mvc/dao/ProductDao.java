package ru.akirakozov.sd.mvc.dao;

import ru.akirakozov.sd.mvc.model.TaskList;

import java.util.List;
import java.util.Optional;

/**
 * @author akirakozov
 */
public interface ProductDao {
    int addTaskList(TaskList.Data taskList);
    int addTask(TaskList.TaskData taskData);

    List<TaskList> getTaskLists();

    void toggleDone(TaskList.DoData doData);
}
