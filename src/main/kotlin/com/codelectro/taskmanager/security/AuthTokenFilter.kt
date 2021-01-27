package com.codelectro.taskmanager.security

import com.codelectro.taskmanager.security.SecurityConstant.AUTH_HEADER
import com.codelectro.taskmanager.security.SecurityConstant.AUTH_TYPE
import com.codelectro.taskmanager.security.SecurityConstant.START_TOKEN_INDEX
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class AuthTokenFilter(
        private val jwtUtils: JwtUtils,
        private val userDetailsService: UserDetailsServiceImpl
) : OncePerRequestFilter() {
    private val logger = LoggerFactory.getLogger(AuthTokenFilter::class.java)

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {

        try {
            val jwtToken = parseJwt(request)
            if (jwtToken != null && jwtUtils.validateJwtToken(jwtToken)) {
                val email = jwtUtils.getUserEmailFromJwtToken(jwtToken)
                val userDetails = userDetailsService.loadUserByUsername(email)
                UsernamePasswordAuthenticationToken(userDetails, null, emptyList())
                        .apply {
                            details = WebAuthenticationDetailsSource().buildDetails(request)
                            SecurityContextHolder.getContext().authentication = this
                        }

            }

        } catch (e: Exception) {
            logger.error("Cannot set user authentication: {}", e)
        }

        filterChain.doFilter(request, response)
    }

    private fun parseJwt(request: HttpServletRequest): String? {
        val header = request.getHeader(AUTH_HEADER)
        if (StringUtils.hasText(header) && header.startsWith(AUTH_TYPE)) {
            return header.substring(START_TOKEN_INDEX)
        }
        return null;
    }
}