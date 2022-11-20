package io.github.julianobrl.configui.controller;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import io.github.julianobrl.configui.dto.FileContent;
import io.github.julianobrl.configui.dto.Signin;
import io.github.julianobrl.configui.services.ResourceManager;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/ui")
public class UiAPiController {
	
	@Autowired
	private ResourceManager resourceManager;
    
    @PostMapping
    public String signin(@ModelAttribute Signin signin, Model model) {
    	System.out.println(signin.getUsername());
        return "redirect:/ui/dashboard";
    }
    
    
    
    @PostMapping("/saveFile")
    public String saveFile(@ModelAttribute FileContent save, Model model, final RedirectAttributes redirectAttributes){
    	log.info("saveFile - {}",save.getFilename());
    	try {
			resourceManager.writeFile(save.getFilename(), save.getContent());
			redirectAttributes.addFlashAttribute("success", "File saved!");
		} catch (IOException e) {
			redirectAttributes.addFlashAttribute("error", "Error while saving file!");
		}
        return "redirect:/ui/dashboard/"+save.getFilename();
    }
    
    @GetMapping("/restore/file/{filename}")
    public String restoreFile(@PathVariable("filename") String fileName, Model model, final RedirectAttributes redirectAttributes){
    	try {
			resourceManager.restoreFile(fileName);
			redirectAttributes.addFlashAttribute("success", "File restored!");
		} catch (IOException e) {
			redirectAttributes.addFlashAttribute("error", "Error while restoring file!");
		}
        return "redirect:/ui/dashboard/backups";
    }
    
    @GetMapping("/delete/file/{filename}")
    public String deleteBkpFile(@PathVariable("filename") String fileName, Model model, final RedirectAttributes redirectAttributes) {
    	try {
			resourceManager.deleteFile(fileName,true);
			redirectAttributes.addFlashAttribute("success", "Backup file deleted");
		} catch (IOException e) {
			redirectAttributes.addFlashAttribute("error", "Error while deleting backup file!");
		}
        return "redirect:/ui/dashboard/backups";
    }
    
    @PostMapping("/deleteFile")
    public String deleteFile(@ModelAttribute FileContent save, Model model, final RedirectAttributes redirectAttributes){
    	try {
			resourceManager.deleteFile(save.getFilename(),false);
			redirectAttributes.addFlashAttribute("success", "File deleted");
		} catch (IOException e) {
			redirectAttributes.addFlashAttribute("error", "Error while deleting file!");
		}
    	
        return "redirect:/ui/dashboard";
    }
    
	@PostMapping("/uploadFile")
    public String uploadFile(@RequestParam(required = false, name = "filename") String filename, 
    		@RequestParam(required = false, name = "image") MultipartFile file, Model model,
    		final RedirectAttributes redirectAttributes) throws IOException {
    	
    	try {
	    	if(filename != null && !filename.isBlank() && !filename.isEmpty() ) {
	    		resourceManager.createFile(filename);
	    		redirectAttributes.addFlashAttribute("success", "File created!!");
	    		return "redirect:/ui/dashboard/"+filename;
	    	}
	    	
	    	if(file != null && !file.isEmpty()) {
	    		resourceManager.createFile(file);
	    		redirectAttributes.addFlashAttribute("success", "File uploaded!!");
	    		return "redirect:/ui/dashboard/"+file.getName();
	    	}
    	}catch (FileAlreadyExistsException e) {	
    		redirectAttributes.addFlashAttribute("error", "Error: File already exists");
    		return "redirect:/ui/dashboard";
		}
    	
    	return "redirect:/ui/dashboard";
        
    }
}