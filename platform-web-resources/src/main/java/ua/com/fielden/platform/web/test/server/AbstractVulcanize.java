package ua.com.fielden.platform.web.test.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import ua.com.fielden.platform.entity.AbstractEntity;
import ua.com.fielden.platform.swing.menu.MiWithConfigurationSupport;
import ua.com.fielden.platform.web.app.ISourceController;
import ua.com.fielden.platform.web.app.IWebUiConfig;
import ua.com.fielden.platform.web.interfaces.DeviceProfile;

import com.google.common.base.Charsets;
import com.google.inject.Injector;

/**
 * A set of utilities to facilitate Web UI application vulcanization.
 *
 * @author TG Team
 *
 */
public class AbstractVulcanize {
    private static final Logger logger = Logger.getLogger(AbstractVulcanize.class);

    protected static Logger logger() {
        return logger;
    }

    /**
     * Retrieves application properties from the specified file.
     *
     * @return
     */
    protected static Properties retrieveApplicationPropertiesAndConfigureLogging(final String fileName) {
        InputStream st = null;
        Properties props = null;
        try {
            st = new FileInputStream(fileName);
            props = new Properties();
            props.load(st);
        } catch (final Exception e) {
            System.out.println(String.format("Application property file %s could not be located or its values are not recognised.", fileName));
            e.printStackTrace();
            System.exit(1);
        } finally {
            try {
                st.close();
            } catch (final Exception e) {
                e.printStackTrace(); // can be ignored
            }
        }

        // needs to be overridden to start vulcanization in development mode (no need to calculate preloaded resources)
        props.setProperty("workflow", "development");

        // configure logging
        DOMConfigurator.configure(props.getProperty("log4j"));

        return props;
    }

    /**
     * Vulcanizes 'startup-resources-origin.html' file into 'startup-resources-vulcanized.html'.
     *
     * @param injector
     */
    protected static void vulcanize(final Injector injector, final String platformVendorResourcesPath, final String platformWebUiResourcesPath, final String appVendorResourcesPath, final String appWebUiResourcesPath, final String loginTargetPlatformSpecificPath, final String mobileAndDesktopAppSpecificPath) {
        logger.info("Vulcanizing...");
        final ISourceController sourceController = injector.getInstance(ISourceController.class);

        final IWebUiConfig webUiConfig = injector.getInstance(IWebUiConfig.class);

        // create the directory in which all needed resources will reside
        final File dir = new File("vulcan");
        dir.mkdir();

        copyStaticResources(platformVendorResourcesPath, platformWebUiResourcesPath, appVendorResourcesPath, appWebUiResourcesPath);
        logger.info("\t------------------------------");

        logger.info("\tVulcanizing login resources...");
        vulcanizeStartupResourcesFor("login", DeviceProfile.MOBILE, sourceController, loginTargetPlatformSpecificPath);
        logger.info("\tVulcanized login resources.");

        logger.info("\t------------------------------");

        downloadCommonGeneratedResources(webUiConfig, sourceController);
        logger.info("\t------------------------------");

        logger.info("\tVulcanizing mobile resources...");
        downloadSpecificGeneratedResourcesFor(DeviceProfile.MOBILE, sourceController);
        vulcanizeStartupResourcesFor("mobile", DeviceProfile.MOBILE, sourceController, mobileAndDesktopAppSpecificPath);
        logger.info("\tVulcanized mobile resources.");
        logger.info("\t------------------------------");

        logger.info("\tVulcanizing desktop resources...");
        downloadSpecificGeneratedResourcesFor(DeviceProfile.DESKTOP, sourceController);
        vulcanizeStartupResourcesFor("desktop", DeviceProfile.DESKTOP, sourceController, mobileAndDesktopAppSpecificPath);
        logger.info("\tVulcanized desktop resources.");
        logger.info("\t------------------------------");

        logger.info("\tClear obsolete files...");
        try {
            FileUtils.deleteDirectory(dir);
        } catch (final IOException e) {
            logger.error(e.getMessage(), e);
            throw new IllegalStateException(e);
        }
        logger.info("\tCleared obsolete files.");

        logger.info("Vulcanized.");
    }

    private static void downloadCommonGeneratedResources(final IWebUiConfig webUiConfig, final ISourceController sourceController) {
        logger.info("\tDownloading common generated resources...");
        downloadSource("app", "tg-app-config.html", sourceController, null);
        downloadSource("app", "tg-reflector.html", sourceController, null);
        for (final Class<? extends AbstractEntity<?>> masterType : webUiConfig.getMasters().keySet()) {
            downloadSource("master_ui", masterType.getName(), sourceController, null);
        }
        for (final Class<? extends MiWithConfigurationSupport<?>> centreMiType : webUiConfig.getCentres().keySet()) {
            downloadSource("centre_ui", centreMiType.getName(), sourceController, null);
            downloadSource("centre_ui/egi", centreMiType.getName(), sourceController, null);
        }
        logger.info("\tDownloaded common generated resources.");
    }

    private static void downloadSpecificGeneratedResourcesFor(final DeviceProfile deviceProfile, final ISourceController sourceController) {
        logger.info("\t\tDownloading " + deviceProfile + " generated resources...");
        downloadSource("app", "tg-app.html", sourceController, deviceProfile);
        downloadSource("app", "tg-element-loader.html", sourceController, deviceProfile);
        logger.info("\t\tDownloaded " + deviceProfile + " generated resources.");
    }

    private static void vulcanizeStartupResourcesFor(final String prefix, final DeviceProfile deviceProfile, final ISourceController sourceController, final String targetAppSpecificPath) {
        logger.info("\t\tVulcanizing [" + prefix + "-startup-resources-origin.html]...");
        try {
            final ProcessBuilder pb = new ProcessBuilder("/bin/bash", "vulcanize", "-p", "'vulcan/'", "/" + prefix + "-startup-resources-origin.html", ">", prefix
                    + "-startup-resources-origin-vulcanized.html");
            //            final ProcessBuilder pb = new ProcessBuilder("/bin/bash", prefix + "-script.sh");
            pb.redirectErrorStream(true);
            final Process process = pb.start();
            process.waitFor();

            //            final Runtime r = Runtime.getRuntime();
            //            r.exec("/home/oleh/.nvm/versions/v0.12.4/bin/vulcanize -p 'vulcan/' /" + prefix + "-startup-resources-origin.html > " + prefix
            //                    + "-startup-resources-origin-vulcanized.html").waitFor();
        } catch (final IOException | InterruptedException e) {
            logger.error(e.getMessage(), e);
            throw new IllegalStateException(e);
        }
        logger.info("\t\tVulcanized [" + prefix + "-startup-resources-origin.html].");

        logger.info("\t\tInlining styles / scripts in [" + prefix + "-startup-resources-origin-vulcanized.html]...");
        try {
            final FileInputStream fileInputStream = new FileInputStream(prefix + "-startup-resources-origin-vulcanized.html");
            final String vulcanized = IOUtils.toString(fileInputStream, Charsets.UTF_8.name());
            fileInputStream.close();

            final PrintStream ps = new PrintStream(prefix + "-startup-resources-origin-vulcanized.html");
            ps.print(inlineScripts(inlineStyles(vulcanized, sourceController, deviceProfile), sourceController, deviceProfile));
            ps.close();
        } catch (final IOException e) {
            logger.error(e.getMessage(), e);
            throw new IllegalStateException(e);
        }
        logger.info("\t\tInlined styles / scripts in [" + prefix + "-startup-resources-origin-vulcanized.html].");

        logger.info("\t\tMove vulcanized file to its destination...");
        try {
            FileUtils.copyFile(new File(prefix + "-startup-resources-origin-vulcanized.html"), new File(targetAppSpecificPath + prefix + "-startup-resources-vulcanized.html"));
            new File(prefix + "-startup-resources-origin-vulcanized.html").delete();
        } catch (final IOException e) {
            logger.error(e.getMessage(), e);
            throw new IllegalStateException(e);
        }
        logger.info("\t\tMoved vulcanized file to its destination.");
    }

    /**
     * Copies static resources from the places that should be relative to the application module, in which concrete Vulcanize utility reside.
     *
     * @param platformVendorResourcesPath
     * @param platformWebUiResourcesPath
     * @param appVendorResourcesPath
     * @param appWebUiResourcesPath
     */
    private static void copyStaticResources(final String platformVendorResourcesPath, final String platformWebUiResourcesPath, final String appVendorResourcesPath, final String appWebUiResourcesPath) {
        logger.info("\tCopying static resources...");
        new File("vulcan/resources").mkdir();
        try {
            // Application resources take precedence over the platform resources. Also our resources take precedence over vendor resources.
            FileUtils.copyDirectory(new File(platformVendorResourcesPath), new File("vulcan/resources"));
            FileUtils.copyDirectory(new File(platformWebUiResourcesPath), new File("vulcan/resources"));
            if (appVendorResourcesPath != null) { // TODO remove if statement
                FileUtils.copyDirectory(new File(appVendorResourcesPath), new File("vulcan/resources"));
            }
            if (appWebUiResourcesPath != null) { // TODO remove if statement
                FileUtils.copyDirectory(new File(appWebUiResourcesPath), new File("vulcan/resources"));
            }
            FileUtils.copyFile(new File("vulcan/resources/desktop-startup-resources-origin.html"), new File("vulcan/desktop-startup-resources-origin.html"));
            FileUtils.copyFile(new File("vulcan/resources/mobile-startup-resources-origin.html"), new File("vulcan/mobile-startup-resources-origin.html"));
            FileUtils.copyFile(new File("vulcan/resources/login-startup-resources-origin.html"), new File("vulcan/login-startup-resources-origin.html"));
        } catch (final IOException e) {
            logger.error(e.getMessage(), e);
            throw new IllegalStateException(e);
        }
        logger.info("\tCopied static resources.");
    }

    private static void downloadSource(final String dir, final String name, final ISourceController sourceController, final DeviceProfile deviceProfile) {
        PrintStream ps;
        try {
            final File directory = new File("vulcan/" + dir);
            if (!directory.exists()) {
                directory.mkdir();
            }

            final String pathAndName = "/" + dir + "/" + name;
            ps = new PrintStream("vulcan" + pathAndName);
            ps.println(sourceController.loadSource(pathAndName, deviceProfile));
            ps.close();
        } catch (final FileNotFoundException e) {
            logger.error(e.getMessage(), e);
            throw new IllegalStateException(e);
        }
    }

    /**
     * Inlines stylesheets inside the source (vulcanized).
     *
     * Format of styles to be inlined: <link rel="import" type="css" href="/resources/polymer/paper-item/paper-item-shared.css">
     *
     * @param source
     * @param sourceController
     *
     * @return
     */
    private static String inlineStyles(final String source, final ISourceController sourceController, final DeviceProfile deviceProfile) {
        // TODO FRAGILE APPROACH! please, provide better implementation (whitespaces, exchanged rel, type and href, double or single quotes etc.?):
        final String searchString = "<link rel=\"import\" type=\"css\" href=\"";
        final int indexOfCssImport = source.indexOf(searchString);
        if (indexOfCssImport > -1) {
            final String endSearchString = "\">";
            final int endIndex = source.indexOf(endSearchString, indexOfCssImport + searchString.length()) + endSearchString.length();
            final String importStatement = source.substring(indexOfCssImport, endIndex);
            final String uri = importStatement.substring(searchString.length(), importStatement.length() - endSearchString.length());
            logger.info("\t\t\tInlining style [" + uri + "]...");
            return inlineStyles(source.replace(importStatement, "<style>" + sourceController.loadSource(uri, deviceProfile) + "\n</style>"), sourceController, deviceProfile);
        } else {
            return source;
        }
    }

    /**
     * Manually inlines scripts inside the source (vulcanized).
     *
     * Format of scripts to be inlined: <script src="/resources/lodash/3.5.0/lodash.min.js"></script>
     *
     * @param source
     * @param sourceController
     *
     * @return
     */
    private static String inlineScripts(final String source, final ISourceController sourceController, final DeviceProfile deviceProfile) {
        // TODO FRAGILE APPROACH! please, provide better implementation (whitespaces, exchanged charset and src, double or single quotes etc.?)
        final String searchString = "<script src=\"";
        final int indexOfScriptTag = source.indexOf(searchString);
        if (indexOfScriptTag > -1) {
            final String endSearchString = "\"></script>";
            final int endIndex = source.indexOf(endSearchString, indexOfScriptTag + searchString.length()) + endSearchString.length();
            final String scriptTag = source.substring(indexOfScriptTag, endIndex);
            final String uri = scriptTag.substring(searchString.length(), scriptTag.length() - endSearchString.length());
            logger.info("\t\t\tInlining script [" + uri + "]...");
            return inlineScripts(source.replace(scriptTag, "<script>" + sourceController.loadSource(uri, deviceProfile).replace("//# sourceMappingURL", "//") + "\n</script>"), sourceController, deviceProfile);
        } else {
            return source;
        }
    }
}
