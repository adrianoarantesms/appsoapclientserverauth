package com.example.demosoapclient;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import hello.ProcessamentoDispatchHelper;


public class ProcessarDocumentoTeste {

	@Test
	public void contextLoads() {
		String retorno = ProcessamentoDispatchHelper.processar();
		
		System.out.println(retorno);
		assertTrue("resposta..: " + retorno, retorno != null);
	}
	
	
}



