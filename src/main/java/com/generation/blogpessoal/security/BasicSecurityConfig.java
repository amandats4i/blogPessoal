package com.generation.blogpessoal.security;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

//A CLASSE CONFIGURATION POSSUI UMA FONTE DE CONFIGURAÇÃO DE BEANS. 
@Configuration

//HABILITA A CONFIGURATION COMO PADRÃO
@EnableWebSecurity
public class BasicSecurityConfig {

	@Autowired
	private JwtAuthFilter authFilter;

	//OS BEANS SÃO UMA INJEÇÃO DE DEPENDENCIAS QUE PODE SER ACESSADA EM QUALQUER LUGAR DO PROJETO.
	@Bean
	UserDetailsService userDetailsService() {

		return new UserDetailsServiceImpl();
	}

	//CRIPTOGRAFA A SENHA
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	
	//AUTENTICA O USERNAME PELO BANCO
	@Bean
	AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(userDetailsService());
		authenticationProvider.setPasswordEncoder(passwordEncoder());
		return authenticationProvider;
	}

	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
			throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		http.sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.csrf(csrf -> csrf.disable()) //NOMEIA AS ALTERAÇÕES DE POST E PUT. AQUI, DESABILITAMOS A FUNÇAO.
				.cors(withDefaults()); //ESSE É O CROSS ORIGINS, QUE LIBERA O ACESSO DE OUTROS SERVIDORES AO CABEÇALHO.

		//ABAIXO, ESTÃO OS RECURSOS QUE SAO LIBERADOS, QUE NAO POSSUEM AUTENTICAÇÃO DO TOKEN: LOGAR E CADASTRAR
		http.authorizeHttpRequests((auth) -> auth.requestMatchers("/usuarios/logar").permitAll()
				.requestMatchers("/usuarios/cadastrar").permitAll().requestMatchers("/error/**").permitAll()
				.requestMatchers(HttpMethod.OPTIONS).permitAll().anyRequest().authenticated())
				.authenticationProvider(authenticationProvider())
				.addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class).httpBasic(withDefaults());

		return http.build();

	}

}
