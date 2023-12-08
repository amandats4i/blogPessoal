package com.generation.blogpessoal.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.generation.blogpessoal.model.Postagem;

//JpaRepository é uma interface já existente no Spring que importamos e setamos dois parametros: a Classe com a qual ela vai interagir (Postagem) e a chave primaria dessa classe (do tipo Long).
public interface PostagemRepository extends JpaRepository<Postagem, Long> {

}
