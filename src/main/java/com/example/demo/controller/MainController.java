package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.repository.UserRepository;
import com.example.demo.model.User;

@Controller
public class MainController {

    @GetMapping("/")
    public String ShowIndex(Model model) {
        return "index";
    }

    // begin of crud of actives

    // end of crud of actives

    // begin login
    @Autowired
    private UserRepository userRepo;

    @GetMapping("/login")
    public String ShowLoginForm(Model model) {
        model.addAttribute("user", new User());
        return "login";
    }

    /// Recibe la informacion para iniciar seccion
    @PostMapping("/login")
    public String SendLogin(@ModelAttribute User user, Model model) {
        User usuarioBD = userRepo.findByName(user.getName());

        if (usuarioBD == null || !usuarioBD.getPassword().equals(user.getPassword())) {
            model.addAttribute("error", "Usuario o contraseña incorrectos");
            return "login";
        }

        model.addAttribute("usuario", usuarioBD);
        return "index";
    }

    // end login

    // begin sign up

    // end sign up

    // begin asignacion de vulnerabiliades y recomendaciones

    // end asignacion de vulnerabiliades y recomendaciones

}
