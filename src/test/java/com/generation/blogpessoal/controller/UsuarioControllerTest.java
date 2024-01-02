package com.generation.blogpessoal.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.generation.blogpessoal.model.Usuario;
import com.generation.blogpessoal.repository.UsuarioRepository;
import com.generation.blogpessoal.service.UsuarioService;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // AQUI, INDICAMOS QUE O CICLO DE VIDA DO TESTE É POR CLASSE.
public class UsuarioControllerTest {

	// Resposnável por enviar testes para API, igual ao Insomnia.
	@Autowired
	private TestRestTemplate testRestTemplate;

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@BeforeAll // ANTES DE TUDO, EXECUTE ISSO:
	void start() {

		// AQUI, O MÉTODO VAI DELETAR O BANCO DE DADOS CASO HAJA ALGUMA INFO NELE.
		usuarioRepository.deleteAll();

		usuarioService.cadastrarUsuario(new Usuario(0L, "Root", "root@root.com", "rootroot", " "));
	}

	// TEM ESSE NOME PARA QUE OUTROS DEVS SAIBAM O QUE ESSE TESTE FAZ
	@Test
	@DisplayName("Cadastrar Usuário 🙂")
	public void deveCriarUmUsuario() {

		//Corpo da requisição
		HttpEntity<Usuario> corpoRequisicao = new HttpEntity<Usuario>(
				new Usuario(0L, "Gabriel Rodrigues", "gabriel@email.com.br", "12345678", " "));

		//Requisição HTTP
		ResponseEntity<Usuario> corpoResposta = testRestTemplate
				.exchange("/usuarios/cadastrar", HttpMethod.POST, corpoRequisicao, Usuario.class);
		
		assertEquals(HttpStatus.CREATED, corpoResposta.getStatusCode());
	}
	
	@Test
	@DisplayName("Não Deve Duplicar Usuário 🙂")
	public void naoDeveDuplicarUsuario() {
		
		usuarioService.cadastrarUsuario(new Usuario(
				0L, "Amanda Tsai", "amanda@email.com.br", "12345678", " "));

		//Corpo da requisição
		HttpEntity<Usuario> corpoRequisicao = new HttpEntity<Usuario>(
				new Usuario(0L, "Amanda Tsai", "amanda@email.com.br", "12345678", " "));

		//Requisição HTTP
		ResponseEntity<Usuario> corpoResposta = testRestTemplate
				.exchange("/usuarios/cadastrar", HttpMethod.POST, corpoRequisicao, Usuario.class);
		
		assertEquals(HttpStatus.BAD_REQUEST, corpoResposta.getStatusCode());
	}
	
	@Test
	@DisplayName("Deve Atualizar Usuário 🙂")
	public void deveAtualizarUsuario() {
		
		Optional<Usuario>usuarioCadastrado = usuarioService.cadastrarUsuario(
				new Usuario(0L, "Kendal Katherine", "kendal@email.com.br", "12345678", " "));

		//Corpo da requisição
		HttpEntity<Usuario> corpoRequisicao = new HttpEntity<Usuario>(
				new Usuario(usuarioCadastrado.get().getId(), "Kendal Katherine Correia", "kendalk@email.com.br", "78945612", " "));

		//Requisição HTTP
		ResponseEntity<Usuario> corpoResposta = testRestTemplate
				.withBasicAuth("root@root.com", "rootroot")
				.exchange("/usuarios/atualizar", HttpMethod.PUT, corpoRequisicao, Usuario.class);
		
		//Verifica o HTTP Status Code
		assertEquals(HttpStatus.OK, corpoResposta.getStatusCode());
	}
	
	@Test
	@DisplayName("Deve Listar os Usuários 🙂")
	public void deveListarTodosUsuarios() {
		
		usuarioService.cadastrarUsuario(new Usuario(
				0L, "Amanda Tsai", "amanda@email.com.br", "12345678", " "));
		
		usuarioService.cadastrarUsuario(new Usuario(
				0L, "Vitor Nascimento", "vitor@email.com.br", "12345678", " "));

		//Requisição HTTP
		ResponseEntity<String> corpoResposta = testRestTemplate
				.withBasicAuth("root@root.com", "rootroot")
				.exchange("/usuarios", HttpMethod.GET, null, String.class);
		
		//Verifica o HTTP Status Code
		assertEquals(HttpStatus.OK, corpoResposta.getStatusCode());
	}

	//CRIAR TESTE PARA BUSCAR POR ID E AUTENTICAR LOGAR (USER LOGIN)
}
