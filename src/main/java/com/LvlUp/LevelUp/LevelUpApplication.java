package com.LvlUp.LevelUp;

import org.springframework.boot.SpringApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SpringBootApplication
public class LevelUpApplication {
	private static final Logger log = LoggerFactory.getLogger(LevelUpApplication.class);
	private static final List<String> LOG_MESSAGES = new ArrayList<>();
	public static ConfigurableApplicationContext context;

	public static void main(String[] args) {
		String propertiesFilePath = getPropertiesFilePath();
		setPropertiesFilePath(propertiesFilePath);
		// Generate timestamp string in the format: log-YYYY-MM-DD_HH-MM-SS
		String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
		String logFileName = "AppLog/log-" + timestamp + ".log";

		// Set logging.file.name system property with dynamic log file name
		System.setProperty("logging.file.name", logFileName);
		WriteLog("Logging file name set to: " + logFileName);
		SpringApplication app = new SpringApplication(LevelUpApplication.class);
		app.addListeners((ApplicationListener<ApplicationReadyEvent>) event -> {
			LevelUpApplication application = event.getApplicationContext()
					.getBean(LevelUpApplication.class);
			printLogMessages();
		});
		context = app.run(args);
	}

	/**
	 * Determines the location of the properties file based on its presence in
	 * either the `classes` folder or the classpath. It logs which file is being
	 * used.
	 *
	 * @return The path to the properties file.
	 */
	private static String getPropertiesFilePath() {
		String propertiesFilePath;
		if (Files.exists(Paths.get("classes/application.properties"))) {
			propertiesFilePath = "file:classes/application.properties";
			WriteLog("Custom properties file found in classes folder.");
		} else if (new ClassPathResource("application.properties").exists()) {
			propertiesFilePath = "classpath:application.properties";
			WriteLog("Custom properties file found in classpath.");
		} else {
			propertiesFilePath = "classpath:application.properties"; // or use default
			WriteLog("Custom properties file not found. Using default properties.");
		}
		return propertiesFilePath;
	}

	/**
	 * Sets the properties file path in the system properties for Spring to use.
	 *
	 * @param propertiesFilePath The path to the properties file.
	 */
	private static void setPropertiesFilePath(String propertiesFilePath) {
		try {
			System.setProperty("spring.config.location", propertiesFilePath);
			log.info("Spring config location set to: {}", propertiesFilePath);
		} catch (SecurityException e) {
			log.error("Security exception while setting spring.config.location: ", e);
		} catch (Exception e) {
			log.error("Unexpected error setting spring.config.location: ", e);
		}
	}

	/**
	 * Prints all the log messages accumulated in the `LOG_MESSAGES` list.
	 */
	private static void printLogMessages() {
		LOG_MESSAGES.forEach(log::info);
	}

	/**
	 * Adds a log message to the `LOG_MESSAGES` list, which will be printed when the
	 * application is ready.
	 *
	 * @param message The log message to add.
	 */
	public static void WriteLog(String message) {
		LOG_MESSAGES.add(message);
	}

	private static void deleteExistingFiles(String directoryPath) {
		try {
			Path path = Paths.get(directoryPath);
			if (Files.exists(path)) {
				Files.walk(path).filter(Files::isRegularFile).forEach(file -> {
					try {
						Files.delete(file);
						WriteLog("Deleted file: " + file);
					} catch (IOException e) {
						log.error("Failed to delete file: " + file, e);
					}
				});
			}
		} catch (IOException e) {
			log.error("Failed to access or delete files in directory: " + directoryPath, e);
		}
	}

}
