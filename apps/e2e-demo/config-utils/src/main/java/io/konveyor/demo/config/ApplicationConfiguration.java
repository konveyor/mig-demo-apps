package io.konveyor.demo.config;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class ApplicationConfiguration {

	private Properties config;

	public ApplicationConfiguration() {
		super();
		this.config = loadProperties();

	}

	private Properties loadProperties() {
		Properties properties = new Properties();

		try (InputStream inputStream = new FileInputStream("/opt/config/persistence.properties")) {

			properties.load(inputStream);

		} catch (Exception e) {
			System.out.println("Exception: " + e);
		}

		return properties;
	}

	public String getProperty (String name) {
		return config.getProperty(name);
	}



}
