package com.meal.config

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.security.Key
import java.util.Date

@Service
class JwtService {

    @Value("\${jwt.key}")
    private lateinit var JWT_KEY: String

    /**
     * Extract a username from a JWT.
     *
     * @param jwt - the token which should contain the username
     */
    fun extractUsername(jwt: String): String =
        extractClaim(jwt, Claims::getSubject)

    /**
     * Generate a JWT for given user specification and claims.
     *
     * @param userDetails - a user's details (username and password)
     * @param extraClaims - additional claims about a user
     * @return the generated JWT
     */
    fun generateToken(userDetails: UserDetails, extraClaims: Map<String, Any> = emptyMap()): String =
         Jwts.builder()
            .setClaims(extraClaims)
            .setSubject(userDetails.username)
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() + TOKEN_EXPIRE_TIME))
            .signWith(getSigningKey(), SignatureAlgorithm.HS256)
            .compact()

    /**
     * Check whether the token's username matches that of the given user and
     * if the token is not expired.
     *
     * @param jwt - the token to check
     * @param userDetails - the user details to compare the username to
     * @return true iff the token is valid and not expired
     */
    fun isTokenValid(jwt: String, userDetails: UserDetails) =
        extractUsername(jwt) == userDetails.username && !isTokenExpired(jwt)

    /**
     * Check if a JWT is expired.
     *
     * @param jwt - the token to check
     * @return true iff the token is expired
     */
    private fun isTokenExpired(jwt: String) =
        extractClaim(jwt, Claims::getExpiration).before(Date(System.currentTimeMillis()))

    /**
     * Extract a specific claim from a jwt.
     *
     * @param jwt - the token to extract the claim from
     * @param claimsResolver - the function specifying which part of all claims to retrieve
     * @return the value for the extracted claim
     */
    private fun <T> extractClaim(jwt: String, claimsResolver: (c: Claims) -> T): T =
        claimsResolver(extractAllClaims(jwt))

    /**
     * Extract all claims from a JWT.
     *
     * @param jwt - the token to extract claims from
     * @return Claims object
     */
    private fun extractAllClaims(jwt: String): Claims =
        Jwts.parserBuilder()
            .setSigningKey(getSigningKey())
            .build()
            .parseClaimsJws(jwt)
            .body

    /**
     * Get the signing key used for JWT generation.
     *
     * @return the signing key
     */
    private fun getSigningKey(): Key =
      Keys.hmacShaKeyFor(Decoders.BASE64.decode(JWT_KEY))

    companion object {
        private const val TOKEN_EXPIRE_TIME = 1000 * 60 * 24
    }

}
