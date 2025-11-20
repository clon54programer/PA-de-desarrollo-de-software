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
import java.util.List;

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

    @GetMapping("/add_active_fisic")
    public String ShowAddActivePanel(Model model) {

        model.addAttribute("activoFisico", new ActiveFisic());
        model.addAttribute("estados", ActiveFisic.EstadoActivo.values());

        return "activo_fisic_form";
    }

    @PostMapping("/add_active_fisic")
    public String AddActiveFisic(@ModelAttribute ActiveFisic activoFisico, Model model) {

        activoFisicoRepo.save(activoFisico);
        model.addAttribute("success", "Activo físico agregado correctamente");

        return "activo_success";
    }

    @GetMapping("/add_active_digital")
    public String ShowAddActiveDgitalPanel(Model model) {

        model.addAttribute("activoDigital", new ActivoDigital());

        return "activo_digital_form";
    }

    @PostMapping("/add_active_digital")
    public String AddActiveDigital(

            @ModelAttribute ActivoDigital activoDigital,
            Model model) {

        activoDigitalRepo.save(activoDigital);
        model.addAttribute("success", "Activo digital agregado correctamente");

        return "activo_success";
    }

    @GetMapping("/show_fisic_active")
    public String GetFisicActive(Model model) {
        List<ActiveFisic> activos_fisicos = activoFisicoRepo.findAll();
        model.addAttribute("activos_fisicos", activos_fisicos);
        return "show_fisic_activos";
    }

    @GetMapping("/show_digital_active")
    public String GetDigitalcActive(Model model) {
        List<ActivoDigital> activos_digitals = activoDigitalRepo.findAll();
        model.addAttribute("activos_digitals", activos_digitals);
        return "show_digital_activos";
    }

    @GetMapping("/update_fisic_active")
    public String ShowUpdateFisicUpdate(Model model) {
        model.addAttribute("fisic_results", null);
        return "update_fisic_update";
    }

    @PostMapping("/update_fisic_active")
    public String GetFisicActiveForName(@RequestParam("name") String name, Model model) {
        List<ActiveFisic> fisic_results = activoFisicoRepo.findByNombreContainingIgnoreCase(name);
        model.addAttribute("fisic_results", fisic_results);
        model.addAttribute("name_find", name);
        return "update_fisic_update";
    }

    @PostMapping("/update_fisic_active")
    public String GetFisicActiveForID(@RequestParam("id") Long id, Model model) {
        ActiveFisic fisic = activoFisicoRepo.findById(id).orElse(null);
        model.addAttribute("fisic", fisic);
        return "update_fisic_update";
    }

    @PostMapping("/update_fisic_active")
    public String UpdateFisicActive(@ModelAttribute ActiveFisic active, Model model) {
        activoFisicoRepo.save(active);
        String message = "Se actualizo el activo " + active.getName();
        model.addAttribute("message", message);
        model.addAttribute("name_active", active.getName());
        return "success_update";
    }

    @GetMapping("/update_digital_active")
    public String ShowUpdateDigitalUpdate(Model model) {
        return "update_digital_update";
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
