package com.algaworks.ecommerce.hibernate;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

public class TenantFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        var request = (HttpServletRequest) servletRequest;
        String serverName = request.getServerName();
        String subdomain = serverName.substring(0, serverName.indexOf("."));

        EcmCurrentTenantIdentifierResolver.setTenantIdentifier(subdomain + "_ecommerce");

        filterChain.doFilter(servletRequest, servletResponse);
    }

}
