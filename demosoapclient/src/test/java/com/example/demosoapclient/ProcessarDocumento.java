package com.example.demosoapclient;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import hello.ProcessamentoDispatchHelper;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProcessarDocumento {

	@Test
	public void contextLoads() {
		String retorno = ProcessamentoDispatchHelper.processar();
		
		System.out.println(retorno);
		assertTrue("resposta..: " + retorno, retorno != null);
	}
	
	
}



