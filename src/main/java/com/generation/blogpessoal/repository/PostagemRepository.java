package com.generation.blogpessoal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.generation.blogpessoal.model.Postagem;

//JpaRepository é uma interface já existente no Spring que importamos e setamos dois parametros: a Classe com a qual ela vai interagir (Postagem) e a chave primaria dessa classe (do tipo Long).
public interface PostagemRepository extends JpaRepository<Postagem, Long> {

	
	// BUSCA PERSONALIZADA
	List<Postagem> findAllByTituloContainingIgnoreCase(@Param("titulo") String titulo);

	// A linha acima é equivalente em SQL a SELECT * FROM tb_postagens WHERE titulo
	// LIKE "%?%"
	
	// Utilizamos o @Param sempre quando estamos buscando por uma String. Ele torna
	// a String titulo um parâmetro que vai receber o que estiver dentro das aspas.
}
