package com.algaworks.ecommerce.hibernate;

import org.hibernate.context.spi.CurrentTenantIdentifierResolver;

public class EcmCurrentTenantIdentifierResolver implements CurrentTenantIdentifierResolver<String> {

    private static final String ROOT = "algaworks_ecommerce";
    private static final ThreadLocal<String> threadLocal = new ThreadLocal<>();

    public static void setTenantIdentifier(String tenantId) {
        threadLocal.set(tenantId);
    }
    @Override
    public String resolveCurrentTenantIdentifier() {
        if (threadLocal.get() == null)
            threadLocal.set(ROOT);

        return threadLocal.get();
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return !isRoot(threadLocal.get());
    }

    @Override
    public boolean isRoot(String tenantId) {
        return tenantId != null && tenantId.equals(ROOT);
    }
}
