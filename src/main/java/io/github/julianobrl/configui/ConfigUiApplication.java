package io.github.julianobrl.configui;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.context.annotation.Bean;

import io.github.julianobrl.configui.services.ResourceManager;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableConfigServer
@SpringBootApplication
public class ConfigUiApplication {
	
	@Value("${config.folder:./config}")
	private String configFolder;
	
	@Value("${config.bkp.folder:./config_bkp}")
	private String configBkpFolder;

	public static void main(String[] args) {
		SpringApplication.run(ConfigUiApplication.class, args);
	}
	
	@Bean
	ResourceManager loadResourceReader() throws URISyntaxException, IOException {
		log.info("Loading ResourceReader Bean");
		Files.createDirectories(Paths.get(configFolder));
		Files.createDirectories(Paths.get(configBkpFolder));
		return new ResourceManager(configFolder,configBkpFolder);
	}

}
