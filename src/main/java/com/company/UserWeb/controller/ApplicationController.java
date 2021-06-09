package com.company.UserWeb.controller;

import com.company.UserWeb.domain.User;
import com.company.UserWeb.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
public class ApplicationController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/applicationPage")
    public String application(Model model){
        model.addAttribute("users", userRepository.findAll());
        return "applicationPage";
    }

    @PostMapping("/edit")
    public String edit(@RequestParam("box") List<String> ids, @RequestParam("action") String action){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(auth.getName());
        if(action.equals("delete"))
        for(String id:ids){
            userRepository.delete(userRepository.getById(Integer.parseInt(id)));
        } else {
            if (action.equals("block")) {
                for (String id : ids) {
                    User userFromDB = userRepository.getById(Integer.parseInt(id));
                    userFromDB.setActive(false);
                    userFromDB.setStatus();
                    userRepository.save(userFromDB);
                    if (user.getId() == userFromDB.getId()){
                        return "redirect:/logout";
                    }
                }
            } else {
                for (String id : ids) {
                    User userFromDB = userRepository.getById(Integer.parseInt(id));
                    userFromDB.setActive(true);
                    userFromDB.setStatus();
                    userRepository.save(userFromDB);
                }
            }

        }
        return "redirect:/applicationPage";
    }

}
