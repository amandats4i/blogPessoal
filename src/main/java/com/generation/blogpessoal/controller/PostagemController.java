package com.generation.blogpessoal.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.generation.blogpessoal.model.Postagem;
import com.generation.blogpessoal.repository.PostagemRepository;
import com.generation.blogpessoal.repository.TemaRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/postagens")
@CrossOrigin(origins = "*", allowedHeaders = "*") /*
													 * Temos que definir o CrossOrigin para liberar requisições de
													 * outros servidores que vão acessar o front-end
													 */

public class PostagemController {

	@Autowired
	private PostagemRepository postagemRepository;

	@Autowired
	private TemaRepository temaRepository;

	@GetMapping
	public ResponseEntity<List<Postagem>> getAll() {
		return ResponseEntity.ok(postagemRepository.findAll()); // o findAll é equivalente ao: SELECT * FROM
																// tb_postagens;

	}

	// O que está dentro da chave é uma variável que receberá um valor e será usada
	// pelo metodo
	@GetMapping("/{id}")
	public ResponseEntity<Postagem> getById(@PathVariable Long id) {
		return postagemRepository.findById(id)
				.map(resposta -> ResponseEntity.ok(resposta)) // O Resultado do método fica salvo na variavel "resposta"
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());

		// A função acima é equivalente a linha do MySQL: SELECT * FROM tb_postagens
		// WHERE id = ?;

	}

	// MÉTODO PARA BUSCA PERSONALIZADA DO TITULO
	@GetMapping("/titulo/{titulo}")
	public ResponseEntity<List<Postagem>> getByTitulo(@PathVariable String titulo) {
		return ResponseEntity.ok(postagemRepository.findAllByTituloContainingIgnoreCase(titulo));
	}

	@PostMapping
	public ResponseEntity<Postagem> post(@Valid @RequestBody Postagem postagem) {
		if (temaRepository.existsById(postagem.getTema().getId()))
			return ResponseEntity.status(HttpStatus.CREATED).body(postagemRepository.save(postagem));
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tema não existe.", null);

		// A linha acima é equivalente a INSERT INTO tb_postagens (titulo, texto) VALUES
		// (?,?) do SQL;
	}

	@PutMapping 
	public ResponseEntity<Postagem> put (@Valid @RequestBody Postagem postagem){
		
		if (postagemRepository.existsById(postagem.getId())) {
				
			if (temaRepository.existsById(postagem.getTema().getId())) 
				return ResponseEntity.status(HttpStatus.OK).body(postagemRepository.save((postagem)));
				
			 throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tema não existe.", null);
			
		}
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();	
		
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT) // Aqui a gente força o retorno do status "204 - No Content"
	public void delete(@PathVariable Long id) {

		Optional<Postagem> postagem = postagemRepository.findById(id);

		if (postagem.isEmpty())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);

		postagemRepository.deleteById(id);

		// É equivalente ao SQL DELETE FROM tb_postagens WHERE id = ?;
	}

}
