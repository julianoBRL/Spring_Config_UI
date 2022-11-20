package io.github.julianobrl.configui.services;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ResourceManager {
		
	@Getter
	private String resourceDir;
	
	@Getter
	private String resourceBkpDir;
	
	public ResourceManager(String resource, String resourceBkp) throws URISyntaxException, IOException {
		resourceDir = new File(resource).getCanonicalPath();
		resourceBkpDir = new File(resourceBkp).getCanonicalPath();
	}
	
	public File[] getFiles() {
		return (new File(resourceDir)).listFiles();
	}
	
	public File[] getBackupFiles() {
		return (new File(resourceBkpDir)).listFiles();
	}
	
	public URI getResourceURI(String resource) throws URISyntaxException {
		return getClass().getResource(resource).toURI();
	}
	
	public File getFile(URI resourceURI) {
		return new File(resourceURI);
	}
	
	
	/*######################
	 * 
	 *  File manipulation
	 *  
	 *######################
	 */
	
	public String readFile(String fileName) throws IOException {
		return new String(Files.readAllBytes(Paths.get(resourceDir+"/"+fileName)));
	}
	
	public void writeFile(String fileName, String content) throws IOException {
		backupFile(fileName);
		try {
			PrintWriter writer = new PrintWriter(resourceDir+"/"+fileName);
			writer.print(content);
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void deleteFile(String fileName,boolean backup) throws IOException {
		File file = null;
		
		if(!backup) {
			file = new File(resourceDir+"/"+fileName); 
			backupFile(fileName);
		}else {
			file = new File(resourceBkpDir+"/"+fileName); 
		}
		
	    if (file.delete()) { 
	      log.info("Deleted the file: " + file.getName());
	    } else {
	      log.error("Failed to delete the file: " + file.getName());
	    } 
	}
	
	private void backupFile(String fileName) throws IOException {
		LocalDateTime dateTime = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy_HH-mm");
		
		Path copied = Paths.get(resourceBkpDir+"/"+fileName+"_"+dateTime.format(formatter)+".bkp");
	    Path originalPath = Paths.get(resourceDir+"/"+fileName);
	    Files.copy(originalPath, copied, StandardCopyOption.REPLACE_EXISTING);
	    log.info("Backup of file "+fileName+" done!!");
	}

	public void restoreFile(String fileName) throws IOException {
		
		Path copied = Paths.get(resourceDir+"/"+fileName.split("_")[0]);
	    Path originalPath = Paths.get(resourceBkpDir+"/"+fileName);
	    Files.copy(originalPath, copied, StandardCopyOption.REPLACE_EXISTING);
	    
	    File file = new File(resourceBkpDir+"/"+fileName);
	    if (file.delete()) { 
	      log.info("Deleted the file: " + file.getName());
	    } else {
	      log.error("Failed to delete the file: " + file.getName());
	    } 
	    
	}
	
	public void createFile(String fileName) throws IOException {
		
		Path copied = Paths.get(resourceDir+"/"+fileName);
	    Files.createFile(copied);
	    log.info("Created file: "+fileName);
	    
	}
	
	public void createFile(MultipartFile file) throws IOException {
		
        Path fileNameAndPath = Paths.get(resourceDir+"/"+file.getName());
        Files.write(fileNameAndPath, file.getBytes());
        log.info("Saved file uploaded: "+file.getName());
        
	}
	
	
}
