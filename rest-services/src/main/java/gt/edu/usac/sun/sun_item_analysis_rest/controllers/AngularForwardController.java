/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gt.edu.usac.sun.sun_item_analysis_rest.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 *
 * @author emayu
 */
@Controller
public class AngularForwardController {
    
    @GetMapping("{path:^(?!api|public)[^\\.]*}/**")
    public String handleForward() {
        return "forward:/";
    }
    
}
