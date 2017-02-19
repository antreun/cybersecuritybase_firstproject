package sec.project.controller;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import sec.project.domain.Signup;
import sec.project.repository.SignupRepository;

@Controller
public class SignupController {

    @Autowired
    private SignupRepository signupRepository;

    @PostConstruct
    public void init() {
        //insert default content into the signup database
        signupRepository.save(new Signup("Donald Trump", "White House", "john"));
        signupRepository.save(new Signup("Mike Pence", "Washington DC", "john"));
        signupRepository.save(new Signup("Hillary Clinton", "New York", "jack"));
        signupRepository.save(new Signup("Barack Obama", "Washington DC", "jack"));

    }

    @RequestMapping(value = "/form", method = RequestMethod.GET)
    public String loadForm(Model model) {
        //next line should be removed for the fix
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        //next line should be removed for the fix
        String name = auth.getName();
        //next line should be removed for the fix
        model.addAttribute("username", name);

        return "form";
    }

    @RequestMapping(value = "/form", method = RequestMethod.POST)
    public String submitForm(@RequestParam String name, @RequestParam String address, @RequestParam String username) {

        // Fix for the security issue which allows changing the username
        // using the form. Also remove the lines mentioned in above method and
        // the username in this method's parameters and the form template.
        //Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        //username = auth.getName(); //get logged in username
        signupRepository.save(new Signup(name, address, username));
        return "redirect:/signups";
    }

    @RequestMapping(value = "/remove", method = RequestMethod.GET)
    public String removeSignup(@RequestParam long id) {

        signupRepository.delete(id);

        //Fix for the security issue which allows any logged in user to delete
        //any signup. We need to check that the current user is actually
        //the creator of the signup.
        /*
         Signup signup = signupRepository.findOne(id);
         Authentication auth = SecurityContextHolder.getContext().getAuthentication();
         if (signup.getUsername().equals(auth.getName())) {
         signupRepository.delete(id);
         }
         */
        return "redirect:/signups";
    }

    @RequestMapping(value = "/signups", method = RequestMethod.GET)
    public String signups(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName(); //get logged in username
        List<Signup> signups = signupRepository.findAll();
        model.addAttribute("signups", signups);
        model.addAttribute("username", name);

        return "signups";
    }
}
