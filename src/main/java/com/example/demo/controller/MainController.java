package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.repository.ActivoDigitalRepository;
import com.example.demo.repository.ActivoFisicoRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.VulnRepository;
import com.example.demo.repository.RecomendacionRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpSession;

import com.example.demo.model.*;
import com.example.demo.model.Vuln.EstadoVulnerabilidad;

import org.springframework.web.bind.annotation.RequestBody;

@Controller
@SessionAttributes("user_login")
public class MainController {

    @GetMapping("/")
    public String ShowIndex(Model model) {
        return "index";
    }

    @GetMapping("/path")
    public String RedirectIndex(Model model) {
        return "forward:/";
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

    @GetMapping("/delete_fisic_active")
    public String ShowDeleteFisicActive(Model model) {
        model.addAttribute("message", "");
        return "delete_fisic_active";
    }

    @PostMapping("/delete_fisic_active")
    public String DeleteFisicActive(@RequestParam("id") Long id, Model model) {
        activoFisicoRepo.deleteById(id);

        model.addAttribute("message", "Se elimino el activo");

        return "delete_fisic_active";
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

        return "update_fisic_active";
    }

    @PostMapping(value = "/update_fisic_active", params = "name")
    public String GetFisicActiveForName(@RequestParam("name") String name, Model model) {
        List<ActiveFisic> fisic_results = activoFisicoRepo.findByNameContainingIgnoreCase(name);
        model.addAttribute("fisic_results", fisic_results);
        model.addAttribute("name_find", name);
        return "update_fisic_active";
    }

    @PostMapping(value = "/update_fisic_active", params = "id")
    public String GetFisicActiveForID(@RequestParam("id") Long id, Model model) {
        ActiveFisic fisic = activoFisicoRepo.findById(id).orElse(null);
        model.addAttribute("fisic", fisic);
        model.addAttribute("active", fisic);
        model.addAttribute("estados", ActiveFisic.EstadoActivo.values());
        return "update_fisic_active";
    }

    @PostMapping(value = "/update_fisic_active/save")
    public String UpdateFisicActive(@ModelAttribute ActiveFisic active, Model model) {
        activoFisicoRepo.save(active);
        String message = "Se actualizo el activo " + active.getName();
        model.addAttribute("message", message);
        model.addAttribute("name_active", active.getName());
        return "success_update";
    }

    @GetMapping("/update_digital_active")
    public String ShowUpdateDigitalUpdate(Model model) {
        model.addAttribute("digital_results", null);
        return "update_digital_active";
    }

    @PostMapping(value = "/update_digital_active", params = "name")
    public String GetDigitalcActiveForName(@RequestParam("name") String name, Model model) {
        List<ActivoDigital> digital_results = activoDigitalRepo.findByNameContainingIgnoreCase(name);
        model.addAttribute("digital_results", digital_results);
        model.addAttribute("name_find", name);
        return "update_digital_active";
    }

    @PostMapping(value = "/update_digital_active", params = "id")
    public String GetFisicDigitalForID(@RequestParam("id") Long id, Model model) {
        ActivoDigital digital = activoDigitalRepo.findById(id).orElse(null);
        model.addAttribute("digital", digital);
        model.addAttribute("active", digital);
        return "update_digital_active";
    }

    @PostMapping(value = "/update_digital_active/save")
    public String UpdateDigitalActive(@ModelAttribute ActivoDigital active, Model model) {
        activoDigitalRepo.save(active);
        String message = "Se actualizo el activo " + active.getName();
        model.addAttribute("message", message);
        model.addAttribute("name_active", active.getName());
        return "success_update";
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

    @Autowired
    VulnRepository vulnRepository;

    @Autowired
    RecomendacionRepository recomendacionRepository;

    public enum VulnActions {
        AGREGAR,
        ELIMINAR,
        LEER,
    }

    public class ActionRequest {
        private VulnActions action;

        public VulnActions getAction() {
            return this.action;
        }

        public void setAction(VulnActions action) {
            this.action = action;
        }

    }

    @GetMapping("/add_vuln_at_fisic_active")
    public String ShowAddVulnAtFisicActive(Model model) {
        return "add_vuln_fisic_active";
    }

    @GetMapping("/add_vuln_at_digital_active")
    public String ShowAddVulnAtDigitalActive(Model model) {
        return "add_vuln_digital_active";
    }

    @PostMapping(value = "/add_vuln_at_fisic_active", params = "name")
    public String SearchFisicActiveForName(@RequestParam("name") String name, Model model) {
        List<ActiveFisic> actives = activoFisicoRepo.findByNameContainingIgnoreCase(name);

        System.out.println("actives: " + actives.isEmpty());
        for (ActiveFisic activeFisic : actives) {
            System.out.println("name: " + activeFisic.getName());
        }

        model.addAttribute("actives", actives);
        return "add_vuln_fisic_active";
    }

    @PostMapping(value = "/add_vuln_at_digital_active", params = "name")
    public String SearchDigitalActiveForName(@RequestParam("name") String name, Model model) {
        List<ActivoDigital> actives = activoDigitalRepo.findByNameContainingIgnoreCase(name);

        System.out.println("actives: " + actives.isEmpty());
        for (ActivoDigital active : actives) {
            System.out.println("name: " + active.getName());
        }

        model.addAttribute("actives", actives);
        return "add_vuln_digital_active";
    }

    @PostMapping(value = "/add_vuln_at_fisic_active", params = "id")
    public String GetFisicActiveForIDVuln(@RequestParam("id") Long id, Model model) {
        ActiveFisic active = activoFisicoRepo.findById(id).orElse(null);
        String debug_message = "";
        if (active == null) {
            debug_message = "Es nulo";
        } else {
            debug_message = "name: " + active.getName();
        }
        model.addAttribute("debug_message", debug_message);
        model.addAttribute("active", active);
        model.addAttribute("actions", VulnActions.values());
        model.addAttribute("active_name", active.getName());
        model.addAttribute("action", VulnActions.LEER.toString());
        model.addAttribute("action_request", new ActionRequest());
        return "add_vuln_fisic_active";
    }

    @PostMapping(value = "/add_vuln_at_digital_active", params = "id")
    public String GetDigitalActiveForIDVuln(@RequestParam("id") Long id, Model model) {
        ActivoDigital active = activoDigitalRepo.findById(id).orElse(null);
        String debug_message = "";
        if (active == null) {
            debug_message = "Es nulo";
        } else {
            debug_message = "name: " + active.getName();
        }
        model.addAttribute("debug_message", debug_message);
        model.addAttribute("active", active);
        model.addAttribute("actions", VulnActions.values());
        model.addAttribute("active_name", active.getName());
        model.addAttribute("action", VulnActions.LEER.toString());
        model.addAttribute("action_request", new ActionRequest());
        return "add_vuln_digital_active";
    }

    @PostMapping("/add_vuln_at_fisic_active/action")
    public String GetAction(@ModelAttribute("action_request") ActionRequest action, Model model) {

        if (action.getAction() == VulnActions.AGREGAR) {
            model.addAttribute("action_select", action.getAction().toString());
            model.addAttribute("vuln", new VulnDTO());
            model.addAttribute("estados", Vuln.EstadoVulnerabilidad.values());
            System.out.println("action " + action.getAction().toString());
        } else if (action.getAction() == VulnActions.LEER) {
            model.addAttribute("action_select", action.getAction().toString());
            System.out.println("action " + action.getAction().toString());
            model.addAttribute("vulns", null);
        } else if (action.getAction() == VulnActions.ELIMINAR) {
            model.addAttribute("action_select", action.getAction().toString());
            System.out.println("action " + action.getAction().toString());
        }

        System.out.println("de");

        return "add_vuln_fisic_active";
    }

    @PostMapping("/add_vuln_at_digital_active/action")
    public String GetActionDigital(@ModelAttribute("action_request") ActionRequest action, Model model) {

        if (action.getAction() == VulnActions.AGREGAR) {
            model.addAttribute("action_select", action.getAction().toString());
            model.addAttribute("vuln", new VulnDTO());
            model.addAttribute("estados", Vuln.EstadoVulnerabilidad.values());
            System.out.println("action " + action.getAction().toString());
        } else if (action.getAction() == VulnActions.LEER) {
            model.addAttribute("action_select", action.getAction().toString());
            System.out.println("action " + action.getAction().toString());
            model.addAttribute("vulns", null);
        } else if (action.getAction() == VulnActions.ELIMINAR) {
            model.addAttribute("action_select", action.getAction().toString());
            System.out.println("action " + action.getAction().toString());
        }

        System.out.println("de");

        return "add_vuln_digital_active";
    }

    @PostMapping("/add_vuln_at_fisic_active/get")
    public String GetVulnForIDAatFisicActive(@RequestParam("id") long id, Model model) {
        model.addAttribute("vulns", vulnRepository.findByActivoAfectadoId(id));
        return "add_vuln_fisic_active";
    }

    @PostMapping("/add_vuln_at_digital_active/get")
    public String GetVulnForIDAatDigitalActive(@RequestParam("id") long id, Model model) {
        model.addAttribute("vulns", vulnRepository.findByActivoAfectadoId(id));
        return "add_vuln_digital_active";
    }

    @PostMapping("/add_vuln_at_fisic_active/delete")
    public String DeleteForIDAatFisicActive(@RequestParam("id") long id, Model model) {

        vulnRepository.deleteById(id);
        model.addAttribute("delete_message", "Se elimino la vulnerabiliad");

        return "add_vuln_fisic_active";
    }

    @PostMapping("/add_vuln_at_digital_active/delete")
    public String DeleteForIDAatdigitalActive(@RequestParam("id") long id, Model model) {

        vulnRepository.deleteById(id);
        model.addAttribute("delete_message", "Se elimino la vulnerabiliad");

        return "add_vuln_digital_active";
    }

    public class VulnDTO {
        public Long activoAfectadoId;

        public Long getActivoAfectadoId() {
            return activoAfectadoId;
        }

        public void setActivoAfectadoId(Long activoAfectadoId) {
            this.activoAfectadoId = activoAfectadoId;
        }

        public String descripcion;

        public String getDescripcion() {
            return descripcion;
        }

        public void setDescripcion(String descripcion) {
            this.descripcion = descripcion;
        }

        public String cve;

        public String getCve() {
            return cve;
        }

        public void setCve(String cve) {
            this.cve = cve;
        }

        public LocalDate fechaDeDescubrimiento;

        public LocalDate getFechaDeDescubrimiento() {
            return fechaDeDescubrimiento;
        }

        public void setFechaDeDescubrimiento(LocalDate fechaDeDescubrimiento) {
            this.fechaDeDescubrimiento = fechaDeDescubrimiento;
        }

        public EstadoVulnerabilidad estado;

        public EstadoVulnerabilidad getEstado() {
            return estado;
        }

        public void setEstado(EstadoVulnerabilidad estado) {
            this.estado = estado;
        }
    }

    @PostMapping("/add_vuln_at_fisic_active/add")
    public String AddVulnAtFisicActive(@ModelAttribute VulnDTO vulnDTO, Model model) {
        System.out.println("[INFO] Se agrego la vuln" + vulnDTO);

        Optional<ActiveFisic> active = activoFisicoRepo.findById(vulnDTO.activoAfectadoId);

        if (active.isEmpty()) {
            return "redirect:/err_vuln";
        }
        Vuln vuln = new Vuln();
        vuln.setActivoAfectado(active.get());
        vuln.setDescripcion(vulnDTO.descripcion);
        vuln.setCve(vulnDTO.cve);
        vuln.setFechaDeDescubrimiento(vulnDTO.fechaDeDescubrimiento);
        vuln.setEstado(vulnDTO.estado);

        vulnRepository.save(vuln);
        String message = "Se agrego la vulnerabilidad de forma exitosa";

        model.addAttribute("message", message);
        model.addAttribute("name_active", active.get().getName());

        return "success_vuln";
    }

    @PostMapping("/add_vuln_at_digital_active/add")
    public String AddVulnAtDigitalActive(@ModelAttribute VulnDTO vulnDTO, Model model) {
        System.out.println("[INFO] Se agrego la vuln" + vulnDTO);

        Optional<ActivoDigital> active = activoDigitalRepo.findById(vulnDTO.activoAfectadoId);

        if (active.isEmpty()) {
            return "redirect:/err_vuln";
        }
        Vuln vuln = new Vuln();
        vuln.setActivoAfectado(active.get());
        vuln.setDescripcion(vulnDTO.descripcion);
        vuln.setCve(vulnDTO.cve);
        vuln.setFechaDeDescubrimiento(vulnDTO.fechaDeDescubrimiento);
        vuln.setEstado(vulnDTO.estado);

        vulnRepository.save(vuln);
        String message = "Se agrego la vulnerabilidad de forma exitosa";

        model.addAttribute("message", message);
        model.addAttribute("name_active", active.get().getName());

        return "success_vuln";
    }

    // end asignacion de vulnerabiliades y recomendaciones

}
