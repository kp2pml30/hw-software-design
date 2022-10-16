package ru.akirakozov.sd.mvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ru.akirakozov.sd.mvc.dao.ProductDao;
import ru.akirakozov.sd.mvc.model.TaskList;

import java.util.List;
import java.util.Optional;

/**
 * @author akirakozov
 */
@Controller
public class ProductController {
    private final ProductDao productDao;

    public ProductController(ProductDao productDao) {
        this.productDao = productDao;
    }

    @RequestMapping(value = "/toggle-done", method = RequestMethod.POST)
    public String toggleDone(@ModelAttribute("doData") TaskList.DoData doData) {
        productDao.toggleDone(doData);
        return "redirect:/get-products";
    }

    @RequestMapping(value = "/add-task-list", method = RequestMethod.POST)
    public String addQuestion(@ModelAttribute("listData") TaskList.Data listData) {
        assert listData.name != null;
        productDao.addTaskList(listData);
        return "redirect:/get-products";
    }

    @RequestMapping(value = "/add-task", method = RequestMethod.POST)
    public String addQuestion(@ModelAttribute("taskData") TaskList.TaskData taskData) {
        productDao.addTask(taskData);
        return "redirect:/get-products";
    }

    @RequestMapping(value = "/get-products", method = RequestMethod.GET)
    public String getProducts(ModelMap map) {
        prepareModelMap(map, productDao.getTaskLists());
        return "index";
    }

    private void prepareModelMap(ModelMap map, List<TaskList> taskLists) {
        map.addAttribute("taskLists", taskLists);
        map.addAttribute("listData", new TaskList.Data());
        map.addAttribute("taskData", new TaskList.TaskData());
        map.addAttribute("doData", new TaskList.DoData());
    }
}
