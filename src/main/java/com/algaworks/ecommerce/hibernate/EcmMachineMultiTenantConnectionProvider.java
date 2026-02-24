package com.algaworks.ecommerce.hibernate;

import org.hibernate.HibernateException;
import org.hibernate.engine.config.spi.ConfigurationService;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.hibernate.hikaricp.internal.HikariCPConnectionProvider;
import org.hibernate.service.spi.ServiceRegistryAwareService;
import org.hibernate.service.spi.ServiceRegistryImplementor;
import org.hibernate.service.spi.Startable;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class EcmMachineMultiTenantConnectionProvider implements
        MultiTenantConnectionProvider<String>, ServiceRegistryAwareService, Startable {

    private static final Map<String, ConnectionProvider> connectionProviders = new HashMap<>();
    private static Map<String, Object> properties = null;

    @Override
    public Connection getConnection(String tenantIdentifier) {
        try {
            return connectionProviders.get(tenantIdentifier).getConnection();
        } catch (SQLException e) {
            throw new HibernateException("Não foi possível alterar para o host " +
                    tenantIdentifier + ".", e);
        }
    }

    @Override
    public Connection getAnyConnection() throws SQLException {
        return getAnyConnectionProvider().getConnection();
    }

    @Override
    public void releaseConnection(String tenantIdentifier, Connection connection) throws SQLException {
        releaseAnyConnection(connection);
    }

    @Override
    public void releaseAnyConnection(Connection connection) throws SQLException {
        getAnyConnectionProvider().closeConnection(connection);
    }

    @Override
    public boolean supportsAggressiveRelease() {
        return getAnyConnectionProvider().supportsAggressiveRelease();
    }

    @Override
    public boolean isUnwrappableAs(Class<?> unwrapType) {
        return getAnyConnectionProvider().isUnwrappableAs(unwrapType);
    }

    @Override
    public <T> T unwrap(Class<T> unwrapType) {
        return getAnyConnectionProvider().unwrap(unwrapType);
    }

    @Override
    public void injectServices(ServiceRegistryImplementor serviceRegistry) {
        this.properties = serviceRegistry
                .getService(ConfigurationService.class)
                .getSettings();
    }

    @Override
    public void start() {

        try {
            File configFile = new File("src/main/resources/META-INF/banco-de-dados/config.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(configFile);
            doc.getDocumentElement().normalize();

            NodeList tenantList = doc.getElementsByTagName("tenant");

            for (int temp = 0; temp < tenantList.getLength(); temp++) {
                Element tenantElement = (Element) tenantList.item(temp);
                String tenantName = tenantElement.getAttribute("name");
                String url = getElementByTagName(tenantElement, "url");
                String usuario = getElementByTagName(tenantElement, "user");
                String senha = getElementByTagName(tenantElement, "password");
                String pool = getElementByTagName(tenantElement, "poolSize");

                configureTenant(tenantName, url, usuario, senha, pool);
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao ler config.xml", e);
        }
    }

    private void configureTenant(String tenant, String url, String usuario, String senha, String pool) {
        Map<String, Object> props = new HashMap<>(properties);

        props.put("jakarta.persistence.jdbc.url", url);
        props.put("hibernate.connection.url", url);

        props.put("jakarta.persistence.jdbc.user", usuario);
        props.put("hibernate.connection.username", usuario);

        props.put("jakarta.persistence.jdbc.password", senha);
        props.put("hibernate.connection.password", senha);

        props.put("hibernate.connection.maximumPoolSize", pool);

        HikariCPConnectionProvider cp = new HikariCPConnectionProvider();
        cp.configure(props);

        connectionProviders.put(tenant, cp);
    }

    private ConnectionProvider getAnyConnectionProvider() {
        return connectionProviders.values().iterator().next();
    }

    private static String getElementByTagName(Element tenantElement, String tagName) {
        return Objects.requireNonNull(tenantElement.getElementsByTagName(tagName).item(0)).getTextContent();
    }
}
