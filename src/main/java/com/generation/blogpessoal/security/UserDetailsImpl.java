package com.generation.blogpessoal.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.generation.blogpessoal.model.Usuario;

public class UserDetailsImpl implements UserDetails {

	private static final long serialVersionUID = 1L;

	private String userName; //nome do usuario
	private String password; //senha
	
	// AUTORIZAÇÕES DO USUÁRIO
	private List<GrantedAuthority> authorities; 

	public UserDetailsImpl(Usuario user) {
		this.userName = user.getUsuario();
		this.password = user.getSenha();
	}

	public UserDetailsImpl() {	}

	//O AUTHORITIES AQUI É VAZIO, OU SEJA, O USUARIO AUTENTICADO TEM ACESSO A TUDO
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {

		return authorities;
	}

	@Override
	public String getPassword() {

		return password;
	}

	@Override
	public String getUsername() {

		return userName;
	}

	
	//VERIFICA SE A CONTA SEMPRE ESTARÁ ATIVA
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	//VERIFICA SE A CONTA ATIVA
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	//VERIFICA SE A SENHA NUNCA SERÁ ALTERADA
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	
	@Override
	public boolean isEnabled() {
		return true;
	}

}
