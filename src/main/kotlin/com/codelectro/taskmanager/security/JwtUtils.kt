package com.codelectro.taskmanager.security

import io.jsonwebtoken.*
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.security.Key
import java.util.*


@Component
class JwtUtils {

    companion object {
        private val logger = LoggerFactory.getLogger(JwtUtils::class.java)
    }

    @Value("\${task.app.jwtSecret}")
    private lateinit var jwtSecret: String

    @Value("\${task.app.jwtExpirationMs}")
    private var jwtExpirationMs = 0

    fun generateJwtToken(authentication: Authentication): String? {
        val email = (authentication.principal as User).username
        return Jwts.builder()
            .setSubject(email)
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + jwtExpirationMs))
            .signWith(getSignInKey(), SignatureAlgorithm.HS512)
            .compact()
    }

    private fun getSignInKey(): Key? {
        val keyBytes = Decoders.BASE64.decode(jwtSecret)
        return Keys.hmacShaKeyFor(keyBytes)
    }

    private fun isTokenExpired(token: String) = extractExpiration(token).before(Date())
    fun isTokenValid(token: String, user: UserDetails): Boolean {
        val userEmail = extractUserEmail(token)
        return user.username.equals(userEmail) && !isTokenExpired(token)
    }

    fun extractUserEmail(token: String?): String? = token?.let { extractClaim(it, Claims::getSubject) }

    private fun extractExpiration(token: String) = extractClaim(token, Claims::getExpiration)

    private fun <T> extractClaim(token: String, claimsResolver: (claim: Claims) -> T): T {
        val claims = extractAllClaims(token)
        return claimsResolver(claims)
    }

    private fun extractAllClaims(token: String) =
        Jwts
            .parserBuilder()
            .setSigningKey(getSignInKey())
            .build()
            .parseClaimsJws(token)
            .body

}