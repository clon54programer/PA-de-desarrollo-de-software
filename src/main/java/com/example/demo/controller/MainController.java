package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.example.demo.repository.ActivoDigitalRepository;
import com.example.demo.repository.ActivoFisicoRepository;
import com.example.demo.repository.UserRepository;

import jakarta.servlet.http.HttpSession;

import com.example.demo.model.*;

@Controller
@SessionAttributes("user_login")
public class MainController {

    @GetMapping("/")
    public String ShowIndex(Model model) {
        return "index";
    }

    // begin of crud of actives

    @Autowired
    private ActivoFisicoRepository activoFisicoRepo;

    @Autowired
    private ActivoDigitalRepository activoDigitalRepo;

    @GetMapping("/add_active")
    public String ShowAddActivePanel(Model model) {

        model.addAttribute("activoFisico", new ActiveFisic());
        model.addAttribute("activoDigital", new ActivoDigital());
        model.addAttribute("estados", ActiveFisic.EstadoActivo.values());

        return "activo_form";
    }

    @PostMapping("/add_active")
    public String AddActivePanel(@RequestParam("tipo") String tipo,
            @ModelAttribute ActiveFisic activoFisico,
            @ModelAttribute ActivoDigital activoDigital,
            Model model) {
        if ("fisico".equalsIgnoreCase(tipo)) {
            activoFisicoRepo.save(activoFisico);
            model.addAttribute("success", "Activo físico agregado correctamente");
        } else if ("digital".equalsIgnoreCase(tipo)) {
            activoDigitalRepo.save(activoDigital);
            model.addAttribute("success", "Activo digital agregado correctamente");
        } else {
            model.addAttribute("error", "Tipo de activo no válido");
            return "activo_form";
        }

        return "activo_success";
    }

    // end of crud of actives

    // begin login
    @Autowired
    private UserRepository userRepo;
    private final String USER_LOGIN = "user_login";

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
        model.addAttribute(USER_LOGIN, usuarioBD);
        model.addAttribute("success", "Login exitoso");
        return "index";
    }

    // end login

    // begin sign up
    @GetMapping("/sign")
    public String ShowSignUpForm(Model model) {
        model.addAttribute("user", new User());
        return "sign";
    }

    @PostMapping("/sign")
    public String AddUser(@ModelAttribute User user) {
        userRepo.save(user);
        return "sign";
    }

    // end sign up

    // begin asignacion de vulnerabiliades y recomendaciones

    // end asignacion de vulnerabiliades y recomendaciones

}
