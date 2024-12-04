package com.akkarimzai.eventticket.controllers.middlewares

import com.akkarimzai.eventticket.services.UserService
import com.akkarimzai.eventticket.services.impl.JwtService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.apache.commons.lang3.StringUtils
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource

@Component
class JwtAuthenticationMiddleware(
    private val jwtService: JwtService,
    private val userService: UserService
) : OncePerRequestFilter() {

    companion object {
        private const val BEARER_PREFIX = "Bearer "
        private const val AUTH_HEADER = "Authorization"
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val token = getTokenFromRequest(request)

        try {
            if (token == null) {
                filterChain.doFilter(request, response)
            } else if (!jwtService.isTokenValid(token) || jwtService.isTokenExpired(token)) {
                response.status = HttpServletResponse.SC_UNAUTHORIZED
            } else {
                validateToken(token, request)
            }
        } catch (e: Exception) {
            response.status = HttpServletResponse.SC_UNAUTHORIZED
            response.writer.write("Unauthorized: Invalid or expired token")
            return
        }

        filterChain.doFilter(request, response)
    }

    private fun validateToken(token: String, request: HttpServletRequest) {
        val username = jwtService.extractUsername(token)

        if (username.isNotBlank() &&
            SecurityContextHolder.getContext().authentication == null
        ) {
            val userDetails = userService
                .userDetailsService()
                .loadUserByUsername(username)

            val authToken = UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.authorities
            )
            authToken.details = WebAuthenticationDetailsSource().buildDetails(request)

            SecurityContextHolder.getContext().authentication = authToken
        }
    }

    private fun getTokenFromRequest(request: HttpServletRequest): String? {
        val bearerToken = request.getHeader(AUTH_HEADER)
        return if (!bearerToken.isNullOrBlank() && bearerToken.startsWith(BEARER_PREFIX)) {
            bearerToken.substring(BEARER_PREFIX.length)
        } else null
    }
}