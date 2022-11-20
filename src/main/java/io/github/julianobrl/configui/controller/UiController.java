package io.github.julianobrl.configui.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import io.github.julianobrl.configui.dto.FileContent;
import io.github.julianobrl.configui.dto.Signin;
import io.github.julianobrl.configui.services.ResourceManager;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/ui")
public class UiController {
	
	@Autowired
	private ResourceManager resourceReader;
	
    @ModelAttribute("files")
    public List<String> allFiles(){
    	List<String> fileNames = new ArrayList<>();
    	File[] configs = resourceReader.getFiles();
    	
    	for (File file : configs) {
    		fileNames.add(file.getName());
		}
    	
    	return fileNames;
    }
    
    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard";
    }
    
    @GetMapping("/dashboard/backups")
    public ModelAndView dashboardBackups() {
    	ModelAndView mav = new ModelAndView("dashboard_backups");
    	
    	//BackupFiles
    	List<String> BKPfileNames = new ArrayList<>();
    	File[] BKPconfigs = resourceReader.getBackupFiles();
    	
    	for (File file : BKPconfigs) {
    		BKPfileNames.add(file.getName());
		}

    	mav.addObject("BKPfiles", BKPfileNames);
        return mav;
    }
    
    @GetMapping("/dashboard/{filename}")
    public ModelAndView editFile(@PathVariable("filename") String filename) {
    	ModelAndView mav = new ModelAndView("dashboard");
    	
    	FileContent file = new FileContent();
    	try {
			file.setContent(resourceReader.readFile(filename));
			file.setFilename(filename);
	    	
	    	mav.addObject("fileFormHolder", file);
	    	mav.addObject("contents", file.getContent());
	    	mav.addObject("filename", file.getFilename());
		} catch (IOException e) {
			log.error(e.getMessage());
			mav = new ModelAndView("redirect:/ui/dashboard");
		}
    	
        return mav;
    }
    
    @GetMapping
    public ModelAndView index() {
    	ModelAndView mav = new ModelAndView("signin");
    	mav.addObject("signin", new Signin());
        return mav;
    }
    
}