package ua.com.fielden.platform.web.test.server;

import java.util.Properties;

import org.apache.log4j.Logger;

import ua.com.fielden.platform.web.utils.VulcanizingUtility;

/**
 * Web UI vulcanization launching class for TG example web server.
 *
 * @author TG Team
 *
 */
public class Vulcanize extends VulcanizingUtility {
    /**
     * The procedure of vulcanization all-in-one.
     *
     * @param args
     */
    public static void main(final String[] args) {
        final Logger logger = Logger.getLogger(Vulcanize.class);
        
        final Properties props = retrieveApplicationPropertiesAndConfigureLogging("src/main/resources/application.properties");

        logger.info("Starting app...");
        final TgTestApplicationConfiguration component = new TgTestApplicationConfiguration(props);
        logger.info("Started app.");

        final String platformVendorResourcesPath = "../platform-web-ui/src/main/resources";
        final String platformWebUiResourcesPath = "../platform-web-ui/src/main/web/ua/com/fielden/platform/web";
        final String appVendorResourcesPath = null;
        final String appWebUiResourcesPath = null;
        final String loginTargetPlatformSpecificPath = "../platform-web-ui/src/main/web/ua/com/fielden/platform/web/";
        final String mobileAndDesktopAppSpecificPath = "../platform-web-ui/src/main/web/ua/com/fielden/platform/web/";

        vulcanize(component.injector(), platformVendorResourcesPath, platformWebUiResourcesPath, appVendorResourcesPath, appWebUiResourcesPath, loginTargetPlatformSpecificPath, mobileAndDesktopAppSpecificPath, logger);
    }

}