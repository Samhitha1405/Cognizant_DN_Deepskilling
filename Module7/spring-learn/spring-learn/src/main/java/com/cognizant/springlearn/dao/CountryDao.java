package com.cognizant.springlearn.dao;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Repository;

import com.cognizant.springlearn.Country;

@Repository
public class CountryDao {
    private static List<Country> COUNTRY_LIST;

    @SuppressWarnings("unchecked")
    public CountryDao() {
        ApplicationContext context =
                new ClassPathXmlApplicationContext("country.xml");
        COUNTRY_LIST = (List<Country>) context.getBean("countryList");
    }

    public List<Country> getAllCountries() {
        return COUNTRY_LIST;
    }
}