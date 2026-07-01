package com.cognizant.ormlearn;

import com.cognizant.ormlearn.model.Country;
import com.cognizant.ormlearn.service.CountryService;
import com.cognizant.ormlearn.service.exception.CountryNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.List;

@SpringBootApplication
public class OrmLearnApplication {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(OrmLearnApplication.class);

    private static CountryService countryService;

    public static void main(String[] args) throws CountryNotFoundException {
        ApplicationContext context =
                SpringApplication.run(OrmLearnApplication.class, args);
        countryService = context.getBean(CountryService.class);

        testGetAllCountries();
        testFindCountryByCode();
        testAddCountry();
        testUpdateCountry();
        testDeleteCountry();
        testSearchCountries();
    }

    private static void testGetAllCountries() {
        LOGGER.info("Start testGetAllCountries");
        List<Country> countries = countryService.getAllCountries();
        LOGGER.debug("Countries: {}", countries);
        LOGGER.info("End testGetAllCountries");
    }

    private static void testFindCountryByCode() throws CountryNotFoundException {
        LOGGER.info("Start testFindCountryByCode");
        Country country = countryService.findCountryByCode("IN");
        LOGGER.debug("Country: {}", country);
        LOGGER.info("End testFindCountryByCode");
    }

    private static void testAddCountry() throws CountryNotFoundException {
        LOGGER.info("Start testAddCountry");
        Country country = new Country();
        country.setCode("NZ");
        country.setName("New Zealand");
        countryService.addCountry(country);
        Country added = countryService.findCountryByCode("NZ");
        LOGGER.debug("Added Country: {}", added);
        LOGGER.info("End testAddCountry");
    }

    private static void testUpdateCountry() throws CountryNotFoundException {
        LOGGER.info("Start testUpdateCountry");
        countryService.updateCountry("NZ", "New Zealand (Updated)");
        Country updated = countryService.findCountryByCode("NZ");
        LOGGER.debug("Updated Country: {}", updated);
        LOGGER.info("End testUpdateCountry");
    }

    private static void testDeleteCountry() {
        LOGGER.info("Start testDeleteCountry");
        countryService.deleteCountry("NZ");
        LOGGER.debug("Deleted country NZ");
        LOGGER.info("End testDeleteCountry");
    }

    private static void testSearchCountries() {
        LOGGER.info("Start testSearchCountries");
        List<Country> containing = countryService.findCountriesContaining("ou");
        LOGGER.debug("Countries containing 'ou': {}", containing);

        List<Country> ordered = countryService.findCountriesContainingOrdered("ou");
        LOGGER.debug("Countries containing 'ou' ordered: {}", ordered);

        List<Country> startingZ = countryService.findCountriesStartingWith("Z");
        LOGGER.debug("Countries starting with Z: {}", startingZ);
        LOGGER.info("End testSearchCountries");
    }
}