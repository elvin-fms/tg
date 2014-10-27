package ua.com.fielden.platform.entity.proxy;

/**
 * Enumerates possible values for entity proxy mode, which governs the behavior of proxy instances.
 *
 * @author TG Team
 *
 */
public enum ProxyMode {
    LAZY, // lazily loads proxied entity instances
    STRICT; // throws StrictProxy exception upon accessing proxy properties except ID
}