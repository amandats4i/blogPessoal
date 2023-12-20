package com.generation.blogpessoal.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.generation.blogpessoal.model.Usuario;
import com.generation.blogpessoal.model.UsuarioLogin;
import com.generation.blogpessoal.repository.UsuarioRepository;
import com.generation.blogpessoal.service.UsuarioService;

import jakarta.validation.Valid;

@RestController
@RequestMapping ("/usuarios")
@CrossOrigin (origins = "*", allowedHeaders = "*")

public class UsuarioController {
	
	//INJEÇAO DE DEPENDENCIAS DE USUARIO SERVICE
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	
	@PostMapping("/cadastrar") 
	public ResponseEntity<Usuario> postUsuario(@Valid @RequestBody Usuario usuario){
		return usuarioService.cadastrarUsuario(usuario)
				.map(resposta -> ResponseEntity.status(HttpStatus.CREATED).body(resposta))
				.orElse(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
	}
	
	
	//PARA PODER ATUALIZAR, PRECISAMOS DEFINIR O TOKEN NO INSOMNIA EM "HEADERS"
	@PutMapping("/atualizar") 
	public Optional<Object> atualizarUsuario(@Valid @RequestBody Usuario usuario){
		
		if(usuarioRepository.findById(usuario.getId()).isPresent()) {
			
			Optional<Usuario> buscaUsuario = usuarioRepository.findByUsuario(usuario.getUsuario()); 
				
				if((buscaUsuario.isPresent()) && (buscaUsuario.get().getId() != usuario.getId()))
						throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuario já existe.", null);
				
				usuario.setSenha(criptografarSenha(usuario.getSenha()));
				 return Optional.ofNullable(usuarioRepository.save(usuario));

	    }
		
		return Optional.empty();
	}
	
	private String criptografarSenha(String senha) {
		
		return null;
	}


	//CONSULTAR USUARIO PELO ID
	
	@GetMapping("/id/{id}")
	public ResponseEntity<Optional<Usuario>> getById(@PathVariable Long id) {
		return ResponseEntity.ok(usuarioRepository.findById(id));
	}
	
	@PostMapping("/logar") 
	public ResponseEntity<UsuarioLogin> autenticarUsuario(@Valid @RequestBody Optional <UsuarioLogin> usuarioLogin){
		return usuarioService.autenticarUsuario(usuarioLogin)
				.map(resposta -> ResponseEntity.status(HttpStatus.OK).body(resposta))
				.orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
	}
}
