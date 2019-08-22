package com.example.demosoapserver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.baeldung.springsoap.gen.Country;
import com.baeldung.springsoap.gen.Currency;

@Component
public class CountryRepository {

	private static final Map<String, Country> countries = new HashMap<>();

	@PostConstruct
	public void initData() {
		List<Country> countrys = new ArrayList<>();

		Country espanha = new Country();
		espanha.setCapital("Capital 1");
		espanha.setCurrency(Currency.EUR);
		espanha.setName("Espanha");
		espanha.setPopulation(1000000);

		Country reinounido = new Country();
		reinounido.setCapital("Capital 1");
		reinounido.setCurrency(Currency.PLN);
		reinounido.setName("Reino Unido");
		reinounido.setPopulation(2000000);

		countrys.add(espanha);
		countrys.add(reinounido);

		for (Country country : countrys) {
			countries.put(country.getName(), country);
		}

	}

	public Country findCountry(String name) {
		return countries.get(name);
	}
}