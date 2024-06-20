package sparta.code3line.security.oauth2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SuccessController {

    @GetMapping("/success")
    public String success(@RequestParam String email, @RequestParam String nickname, @RequestParam String username, Model model) {
        model.addAttribute("email", email);
        model.addAttribute("nickname", nickname);
        model.addAttribute("username", username);
        return "redirect:/success.html";
    }
}
