package com.codelectro.taskmanager.security

import com.codelectro.taskmanager.security.SecurityConstant.AUTH_HEADER
import com.codelectro.taskmanager.security.SecurityConstant.AUTH_TYPE
import com.codelectro.taskmanager.security.SecurityConstant.START_TOKEN_INDEX
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import org.springframework.web.filter.OncePerRequestFilter

@Component
class AuthTokenFilter(
    private val jwtUtils: JwtUtils,
    private val userDetailsService: UserDetailsServiceImpl
) : OncePerRequestFilter() {

    private val logger = LoggerFactory.getLogger(AuthTokenFilter::class.java)

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val header = request.getHeader(AUTH_HEADER)
        if (header == null || !header.startsWith(AUTH_TYPE)) {
            filterChain.doFilter(request, response)
            logger.info("AuthTokenFilter: $AUTH_HEADER header is not valid")
            return
        }

        val jwtToken = parseJwt(header)
        val userEmail = jwtUtils.extractUserEmail(jwtToken)
        if (userEmail != null && jwtToken != null && SecurityContextHolder.getContext().authentication == null) {
            val userDetails = userDetailsService.loadUserByUsername(userEmail)
            if (jwtUtils.isTokenValid(jwtToken, userDetails)) {
                UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
                    .apply {
                        details = WebAuthenticationDetailsSource().buildDetails(request)
                        SecurityContextHolder.getContext().authentication = this
                    }
            }

        }
        filterChain.doFilter(request, response)
    }

    private fun parseJwt(header: String): String? {
        if (StringUtils.hasText(header)) {
            return header.substring(START_TOKEN_INDEX)
        }
        return null
    }
}