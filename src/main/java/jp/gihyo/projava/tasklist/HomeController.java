package jp.gihyo.projava.tasklist;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.config.Task;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
public class HomeController {
    record TaskItem(String id, String task, String deadline, boolean done){}

    private final TaskListDao dao;

    @Autowired //auto wired between Controller and TaskListdao in Constractor
    HomeController(TaskListDao dao){
        this.dao = dao;
    }
    @RequestMapping("/hello") //respond when called '/hello' by http
    String hello(Model model){
        model.addAttribute("time", LocalDateTime.now());
        return "hello"; //return .html file
    }

    @GetMapping("/list") //respond when called '/list'
    String listItems(Model model){
        List<TaskItem> taskItems = dao.findAll(); //generate instance by reference dao and findAll method
        model.addAttribute("taskList", taskItems); //insert taskItems into html file
        return "home"; //return html
    }

    @GetMapping("/add") //respond when called '/add' by html file
    String addItem(@RequestParam("task") String task, //receive some value from html input
                   @RequestParam("deadline") String deadline){
        String id = UUID.randomUUID().toString().substring(0, 8); //generate random value and use it on id
        TaskItem item = new TaskItem(id, task, deadline, false); //generate instance
        dao.add(item); //using add method in dao and operating database by dao
        return "redirect:/list"; //calling '/list' to update display 
    }

    @GetMapping("/delete")
    String deleteItem(@RequestParam("id") String id){
        dao.delete(id); //using delete method in dao and operating sql in dao file
        return "redirect:/list";
    }

    @GetMapping("/update")
    String updateItem(@RequestParam("id") String id, //receive some variable from html 
                      @RequestParam("task") String task,
                      @RequestParam("deadline") String deadline,
                      @RequestParam("done") boolean done){
        TaskItem taskItem = new TaskItem(id, task, deadline, done);
        dao.update(taskItem);
        return "redirect:/list";
    }
}
