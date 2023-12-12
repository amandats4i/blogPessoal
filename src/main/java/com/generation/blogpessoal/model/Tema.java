package com.generation.blogpessoal.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "tb_tema")

public class Tema {

	@Id //
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull(message = "O Atributo descrição é obrigatório!")
	private String descricao;

	// Aqui, estamos estabelecendo a relação entre as tabelas.
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "tema", cascade = CascadeType.REMOVE) 
	@JsonIgnoreProperties("tema") // Ele salva uma vez e ignora o tema.
	
	// O fetch é uma forma de carregar os dados de um bd durante uma busca. 
	// Usamos o modo Lazy pois ele carrega os dados mas nao carrega o tema até seja estritamente
	// solicitado. O cascade faz cascateamento dos dados. Todas as vezes que eu
	// apagar um tema, ele apaga tudo o que está dentro dele.
	// Mapped By serve para indicar qual objeto irá identificar sua chave estrangeira.
	
	
	private List<Postagem> postagem;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public List<Postagem> getPostagem() {
		return postagem;
	}

	public void setPostagem(List<Postagem> postagem) {
		this.postagem = postagem;
	}

}
