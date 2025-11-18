package com.example.demo.controller;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.model.Student;
import com.example.demo.repository.StudentRepository;

@Controller
public class StudentController {

     @Autowired
    private StudentRepository studentRepository;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("students", studentRepository.findAll());
        return "index";
    }

    @GetMapping("/estudiante/agregar")
    public String formStudent(Model model) {
        Student student = new Student();
        model.addAttribute("student", student);
        return "agregar";
    }

    @PostMapping("/estudiante")
    public String guardarStudent(@ModelAttribute("student") Student student) {
        studentRepository.save(student);
        return "redirect:/";
    }

    @GetMapping("/estudiante/editar/{id}")
    public String mostrarStudent(@PathVariable Long id, Model model) {
        Optional<Student> studentOptional = studentRepository.findById(id);
        if (studentOptional.isPresent()) {
            model.addAttribute("student", studentOptional.get());
            return "agregar"; // Reutilizamos la misma plantilla de formulario
        } else {
            // Opcional: Manejar el caso de ID no encontrado
            return "redirect:/?error=notFound"; // Redirige a la lista con un parámetro de error
        }
    }

    @GetMapping("/estudiante/eliminar/{id}")
    public String deleteStudent(@PathVariable Long id) {
        if (studentRepository.existsById(id)) {
            studentRepository.deleteById(id);
            return "redirect:/"; 
        } else {
             
            return "redirect:/?error=deleteFailed"; 
        }
    }
}