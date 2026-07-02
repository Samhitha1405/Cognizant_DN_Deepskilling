package com.cognizant.springlearn;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

@SpringBootApplication
public class SpringLearnApplication {

	private static final Logger LOGGER =
			LoggerFactory.getLogger(SpringLearnApplication.class);

	public static void main(String[] args) throws ParseException {

		SpringApplication.run(SpringLearnApplication.class, args);

		SpringLearnApplication app = new SpringLearnApplication();
		app.displayDate();
		app.displayCountry();
		app.displayCountries();
	}

	public void displayDate() throws ParseException {

		LOGGER.info("START");

		ApplicationContext context =
				new ClassPathXmlApplicationContext("date-format.xml");

		SimpleDateFormat format =
				context.getBean("dateFormat", SimpleDateFormat.class);

		Date date = format.parse("31/12/2018");

		LOGGER.debug(date.toString());

		LOGGER.info("END");
	}

	public void displayCountry() {

		LOGGER.info("START");

		ApplicationContext context =
				new ClassPathXmlApplicationContext("country.xml");

		Country country = context.getBean("in", Country.class);

		LOGGER.debug(country.toString());

		LOGGER.info("END");
	}

	public void displayCountries() {

		LOGGER.info("START");

		ApplicationContext context =
				new ClassPathXmlApplicationContext("country.xml");

		@SuppressWarnings("unchecked")
		List<Country> countryList = (List<Country>) context.getBean("countryList");

		for (Country country : countryList) {
			LOGGER.debug(country.toString());
		}

		LOGGER.info("END");
	}
}