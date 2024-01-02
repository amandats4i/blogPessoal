package com.generation.blogpessoal.model;

/*  ESSA CLASSE SERVE PARA FACILITAR A VIDA DO USUÁRIO. ELA É TIPO UM ESPELHO DA CLASSE MODEL USUARIO, 
 * A FIM DE GUARDAR AS INFORMAÇÕES PRINCIPAIS DE UMA CONTA PARA EVITAR QUE A ESCREVAMOS TODA VEZ QUE FOI LOGAR. 
 * SEU OBJETIVO É SÓ LEVAR OS DADOS PARA FACILITAR O LOGIN, ELA NAO GERA UMA TABELA NO MYSQL. 
 * DEIXA AS INFOS EM UMA CAIXA CHAMADA CONTEXT NO FRONT-END. 
 * CRIA UM OBJETO DA CLASSE USARIO LOGIN SÓ COM DUAS INFOS: Usuario (email) e Senha, PARA AUTENTICAR O USUARIO E PERMITIR ACESSO ÀS INFOS DA CLASSE MODEL USUÁRIO. */

public class UsuarioLogin {

	private Long id;
	private String nome;
	private String usuario;
	private String senha;
	private String foto;
	private String token;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return this.nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getUsuario() {
		return this.usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getSenha() {
		return this.senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getFoto() {
		return this.foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}

	public String getToken() {
		return this.token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}
