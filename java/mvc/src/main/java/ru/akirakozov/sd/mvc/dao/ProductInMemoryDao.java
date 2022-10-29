package ru.akirakozov.sd.mvc.dao;

import ru.akirakozov.sd.mvc.model.TaskList;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author akirakozov
 */
public class ProductInMemoryDao implements ProductDao {
    private final AtomicInteger lastId = new AtomicInteger(0);
    private final List<TaskList> taskLists = new CopyOnWriteArrayList<>();

    @Override
    public int addTaskList(final TaskList.Data taskList) {
        int id = lastId.getAndIncrement();
        taskLists.add(new TaskList(id, taskList.name));
        return id;
    }

    @Override
    public List<TaskList> getTaskLists() {
        return List.copyOf(taskLists);
    }

    @Override
    public int addTask(TaskList.TaskData taskData) {
        final TaskList.Task ret = taskLists.get(taskData.id).addTask(taskData.description);
        return ret.id;
    }

    @Override
    public void toggleDone(TaskList.DoData doData) {
        taskLists.get(doData.lstId).toggleDone(doData.itId, doData.done);
    }
}
