package com.akkarimzai.eventticket.services.impl

import com.akkarimzai.eventticket.entities.User

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
import java.security.Key
import org.springframework.stereotype.Service
import java.security.SignatureException
import java.util.*

@Service
class JwtService @Autowired constructor(
    @Value("\${security.signing.key}") private var jwtKey: String,
    @Value("\${security.signing.ttl}") private var ttl: Long?
) {
    private val logger = KotlinLogging.logger {}


    fun extractUsername(token: String): String {
        return extractClaim(token, Claims::getSubject)
    }

    fun isTokenExpired(token: String): Boolean {
        return extractExpiration(token).before(Date())
    }

    fun isTokenValid(token: String): Boolean {
        val isValid: Boolean =  try {
            extractAllClaims(token)
            true
        } catch (e: Exception) {
            false
        }
        return isValid
    }

    fun generateToken(userDetails: UserDetails): String {
        val claims = HashMap<String, Any>()

        if (userDetails is User) {
            claims["id"] = userDetails.id!!
            claims["email"] = userDetails.email
            claims["username"] = userDetails.username
        }
        return generateToken(claims, userDetails)
    }

    private fun generateToken(extraClaims: HashMap<String, Any>, userDetails: UserDetails): String {
        return Jwts.builder().claims(extraClaims).subject(userDetails.username)
            .issuedAt(Date(System.currentTimeMillis()))
            .expiration(Date(System.currentTimeMillis() + ttl!!))
            .signWith(getSigningKey()).compact()
    }

    private fun <T> extractClaim(token: String, claimsResolver: (Claims) -> T): T {
        val claims = extractAllClaims(token)
        return claimsResolver(claims)
    }

    private fun extractAllClaims(token: String): Claims {
        return Jwts.parser().setSigningKey(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .payload
    }

    private fun getSigningKey(): Key {
        val keyBytes = Decoders.BASE64.decode(jwtKey)
        return Keys.hmacShaKeyFor(keyBytes)
    }

    private fun extractExpiration(token: String): Date {
        return extractClaim(token, Claims::getExpiration)
    }
}