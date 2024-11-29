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
): OncePerRequestFilter() {
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

        if (token != null &&
            !jwtService.isTokenValid(token) &&
            jwtService.isTokenExpired(token)) {
            response.status = HttpServletResponse.SC_UNAUTHORIZED
            return
        }

        if (token != null) {
            validateToken(token, request)
        }
        filterChain.doFilter(request, response)
    }

    private fun validateToken(token: String, request: HttpServletRequest) {
        val username = jwtService.extractUsername(token)

        if (StringUtils.isNotEmpty(username) &&
            SecurityContextHolder.getContext().authentication == null) {
            val userDetails = userService
                .userDetailsService()
                .loadUserByUsername(username)

            val context = SecurityContextHolder.createEmptyContext()
            val authToken = UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.authorities
            )
            authToken.details = WebAuthenticationDetailsSource().buildDetails(request)
            context.authentication = authToken
            SecurityContextHolder.setContext(context)
        }
    }

    private fun getTokenFromRequest(request: HttpServletRequest): String? {
        val bearerToken = request.getHeader(AUTH_HEADER)
        return if (bearerToken != null && bearerToken.startsWith(BEARER_PREFIX)) {
            bearerToken.substring(7)
        } else null
    }
}