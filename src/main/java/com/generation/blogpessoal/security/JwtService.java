package com.generation.blogpessoal.security;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;


//ESSA CLASSE VAI GERAR E VALIDAR O TOKEN.
@Component
public class JwtService {

	//TIPO FINAL = NÃO É ALTERÁVEL, É UMA CONSTANTE.
	//ESSE CÓDIGO "SECRET" É A CHAVE DE ASSINATURA DO TOKEN JWT, QUE FOI GERADA NO SITE keygen.io.
	public static final String SECRET = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";

	
	//PEGA A CHAVE SECRET E A TRANSFORMA EM UMA CHAVE CRIPTOGRAFADA
	private Key getSignKey() {
		byte[] keyBytes = Decoders.BASE64.decode(SECRET);
		return Keys.hmacShaKeyFor(keyBytes);
	}

	
	//PEGA O TOKEN E ABRE O OPENLOAD E LÊ SUAS CLAIMS (usuario, data de criação e expiração do código).
	private Claims extractAllClaims(String token) {
		return Jwts.parserBuilder()
				.setSigningKey(getSignKey()).build()
				.parseClaimsJws(token).getBody();
	}

	
	//ESPECIFICA QUAL CLAIM DO TOKEN VOCÊ QUER PEGAR
	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	
	//PEGA O USERNAME
	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	//PEGA A DATA DE EXPIRAÇÃO DO TOKEN
	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}
	
	//INDICA SE O TOKEN EXPIROU OU NAO
	private Boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}
	
	
	//VALIDA O TOKEN
	public Boolean validateToken(String token, UserDetails userDetails) {
		final String username = extractUsername(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}

	
	//CRIA O TOKEN JWT
	private String createToken(Map<String, Object> claims, String userName) {
		return Jwts.builder()
					.setClaims(claims)
					.setSubject(userName)
					.setIssuedAt(new Date(System.currentTimeMillis()))
					.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // Aqui, usamos um cálculo pois o tempo é medido em milisegundos. Então, multiplicamos por 60 para transformar em minuto.
					.signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
	}

	public String generateToken(String userName) {
		Map<String, Object> claims = new HashMap<>(); //Map é a collection que trabalha no mesmo formato do JSON: chave e valor.
		return createToken(claims, userName);
	}

}