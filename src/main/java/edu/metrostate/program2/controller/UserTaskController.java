package edu.metrostate.program2.controller;

import edu.metrostate.program2.model.AppUser;
import edu.metrostate.program2.model.Task;
import edu.metrostate.program2.service.UserTaskService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;

@Controller
public class UserTaskController {

    @Autowired
    UserTaskService userTaskService;
    @Autowired
    PasswordEncoder passwordEncoder;


    @GetMapping(value={"/", "", "/home"})
    public String home(Model model, Principal principal) {
        AppUser appUser = null;
        if (principal != null && principal.getName() != null) {
            appUser = userTaskService.getUser(principal.getName());
            if (appUser != null) {
                if(appUser.getRoles().contains("USER")) {
                    return "redirect:/user/home";
                }
            }
        }
        model.addAttribute("user", appUser);
        return "home";
    }

    @GetMapping("/register")
    public String create(Model model) {
        model.addAttribute("user", new AppUser());
        return "registration";
    }

    @PostMapping("/register/process")
    public String process(Model model, @Valid @ModelAttribute("user") AppUser user, Errors errors, HttpSession session) {
        if (errors.hasErrors()) {
            return "registration";
        }
        if (userTaskService.existsByUsername(user.getUsername())) {
            model.addAttribute("usernameError", "Username is already taken");
            return "registration";
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles("USER");
        userTaskService.createUser(user);
        session.setAttribute("user", user);
        return "redirect:/login";
    }

    @GetMapping(value={"/user/home", "/user"})
    public String userHome(Model model, @ModelAttribute AppUser user, Principal principal) {
        AppUser currentUser = userTaskService.getUser(principal.getName());
        model.addAttribute("user", currentUser);
        model.addAttribute("tasks", userTaskService.getTasksByUser(currentUser.getId()));
        return "user/user-home";
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("user", new AppUser());
        return "custom-login";
    }

    @GetMapping("/user/tasks")
    public String tasks(Model model, Principal principal) {
        AppUser appUser = null;
        if (principal != null && principal.getName() != null) {
            appUser = userTaskService.getUser(principal.getName());
            if (appUser != null) {
                model.addAttribute("tasks", userTaskService.getTasksByUser(appUser.getId()));
            } else {
                //If you somehow get to this step but your account cannot be found, I'm logging the user out.
                //This is most likely impossible.
                return "redirect:/logout";
            }
        }else{
            return "redirect:/login";
        }
        Task task = new Task();
        task.setUserId(appUser.getId());
        model.addAttribute("task", task);
        model.addAttribute("user", appUser);
        return "user/task-list";
    }
    @GetMapping("/user/tasks/completed")
    public String tasksCompleted(Model model, Principal principal) {
        AppUser appUser = null;
        if (principal != null && principal.getName() != null) {
            appUser = userTaskService.getUser(principal.getName());
            if (appUser != null) {
                model.addAttribute("tasks", userTaskService.getCompletedTasks(appUser.getId()));
            } else {
                //If you somehow get to this step but your account cannot be found, I'm logging the user out.
                //This is most likely impossible.
                return "redirect:/logout";
            }
        }else{
            return "redirect:/login";
        }
        Task task = new Task();
        task.setUserId(appUser.getId());
        model.addAttribute("task", task);
        model.addAttribute("user", appUser);
        return "user/task-list";
    }

    @GetMapping("/user/tasks/not-completed")
    public String tasksNotCompleted(Model model, Principal principal) {
        AppUser appUser = null;
        if (principal != null && principal.getName() != null) {
            appUser = userTaskService.getUser(principal.getName());
            if (appUser != null) {
                model.addAttribute("tasks", userTaskService.getUncompletedTasks((appUser.getId())));
            } else {
                //If you somehow get to this step but your account cannot be found, I'm logging the user out.
                //This is most likely impossible.
                return "redirect:/logout";
            }
        }else{
            return "redirect:/login";
        }
        Task task = new Task();
        task.setUserId(appUser.getId());
        task.setCategory("None");
        model.addAttribute("task", task);
        model.addAttribute("user", appUser);
        return "user/task-list";
    }

    @PostMapping("/user/tasks/add")
    public String addTask(Model model, @Valid @ModelAttribute("task") Task task, Errors errors, Principal principal) {
        AppUser appUser = null;
        if (principal != null && principal.getName() != null) {
            appUser = userTaskService.getUser(principal.getName());
        }else{
            return "redirect:/login";
        }
        if (errors.hasErrors()) {
            model.addAttribute("user", appUser);
            model.addAttribute("hasErrors", true);
            return "user/task-list";
        }
        task.setUserId(appUser.getId());
        userTaskService.addTask(task);
        return "redirect:/user/tasks";
    }

    @GetMapping("/user/tasks/{id}/update")
    public String updateTask(Model model, @PathVariable int id, Principal principal) {
        AppUser appUser = null;
        if (principal != null && principal.getName() != null) {
            appUser = userTaskService.getUser(principal.getName());
        } else {
            return "redirect:/login";
        }
        model.addAttribute("task", userTaskService.getTask(id));
        model.addAttribute("user", appUser);
        return "user/task-update";
    }

    @PostMapping("/user/tasks/update")
    public String update(Model model, @ModelAttribute("task") @Valid Task task, Errors errors, Principal principal) {
        AppUser appUser = null;
        if (principal != null && principal.getName() != null) {
            appUser = userTaskService.getUser(principal.getName());
        } else {
            return "redirect:/login";
        }
        if (errors.hasErrors()) {
            model.addAttribute("hasErrors", true);
            return "user/task-update";
        }
        model.addAttribute("user", appUser);
        task.setUserId(appUser.getId());
        userTaskService.addTask(task);
        return "redirect:/user/tasks";
    }


    @PostMapping("/user/tasks/{id}/delete")
    public String delete(@PathVariable int id) {
        userTaskService.deleteTask(id);
        return "redirect:/user/tasks";
    }
}
