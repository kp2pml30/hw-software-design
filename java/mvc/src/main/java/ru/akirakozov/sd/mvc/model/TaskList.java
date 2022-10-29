package ru.akirakozov.sd.mvc.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author akirakozov
 */
public class TaskList {
    private final int id;
    private final String name;
    private final List<Task> tasks = new CopyOnWriteArrayList<>();
    private final AtomicInteger lastId = new AtomicInteger(0);

    public static class Data {
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String name;
    }

    public static class TaskData {
        public int id;
        public String description;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }

    public class Task {
        public volatile boolean done = false;
        public final int id;
        public final String description;

        public Task(final int id, final String description) {
            this.id = id;
            this.description = description;
        }
    }

    public TaskList(final int id, final String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Task> getTasks() {
        return List.copyOf(tasks);
    }

    public Task addTask(final String description) {
        final Task ret = new Task(lastId.getAndIncrement(), description);
        tasks.add(ret);
        return ret;
    }

    public void toggleDone(final int taskId, final boolean done) {
        tasks.get(taskId).done = done;
    }

    public static class DoData {
        public int lstId;
        public int itId;
        public boolean done;

        public int getLstId() {
            return lstId;
        }

        public void setLstId(int lstId) {
            this.lstId = lstId;
        }

        public int getItId() {
            return itId;
        }

        public void setItId(int itId) {
            this.itId = itId;
        }

        public boolean isDone() {
            return done;
        }

        public void setDone(boolean done) {
            this.done = done;
        }
    }

}
