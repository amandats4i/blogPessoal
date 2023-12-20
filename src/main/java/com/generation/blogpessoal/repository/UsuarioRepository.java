package com.generation.blogpessoal.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.generation.blogpessoal.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

	// Esse método vai buscar o usário pelo e-mail. É do tipo OPTIONAL porque valida
	// de o user existe ou não.

	public Optional<Usuario> findByUsuario(String usuario);

}
