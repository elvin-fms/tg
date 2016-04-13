package ua.com.fielden.platform.web.resources.webui;

import static java.lang.String.format;

import java.io.ByteArrayInputStream;
import java.util.Optional;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.Encoding;
import org.restlet.data.Form;
import org.restlet.data.Status;
import org.restlet.engine.application.EncodeRepresentation;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.InputRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Post;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import ua.com.fielden.platform.mail.SmtpEmailSender;
import ua.com.fielden.platform.security.user.IUser;
import ua.com.fielden.platform.security.user.User;
import ua.com.fielden.platform.utils.IUniversalConstants;
import ua.com.fielden.platform.utils.ResourceLoader;
import ua.com.fielden.platform.web.annotations.AppUri;

/**
 * A web resource to initiate user login recovery procedure.
 *
 * @author TG Team
 *
 */
public class LoginInitiateResetResource extends ServerResource {
    
    public static final String BINDING_PATH = "/forgotten";

    private static final String unidentifiedUserError = "Could not identify an application user.";
    private static final String missingEmailError = "User is missing email address.";

    private final Logger logger = Logger.getLogger(LoginInitiateResetResource.class);

    private final String appUri;
    private final IUser coUser;
    private final IUniversalConstants constants;

    /**
     * Creates {@link LoginInitiateResetResource}.
     */
    public LoginInitiateResetResource(//
            final @AppUri String appUri,
            final IUniversalConstants constants,
            final IUser coUser,
            final Context context, //
            final Request request, //
            final Response response) {
        init(context, request, response);
        this.appUri = appUri;
        this.constants = constants;
        this.coUser = coUser;
    }

    @Override
    protected Representation get() {
        return pageToProvideUsernameForPasswordReset("Login Reset Request", logger);
    }

    private static Representation pageToProvideUsernameForPasswordReset(final String title, final Logger logger) {
        try {
            final byte[] body = ResourceLoader.getText("ua/com/fielden/platform/web/login-initiate-reset.html")
                    .replace("@title", title)
                    .replace("@unidentifiedUser", unidentifiedUserError)
                    .replace("@missingEmail", missingEmailError)
                    .getBytes("UTF-8");
            return new EncodeRepresentation(Encoding.GZIP, new InputRepresentation(new ByteArrayInputStream(body)));
        } catch (final Exception ex) {
            logger.fatal(ex);
            throw new IllegalStateException(ex);
        }
    }
    
    @Post
    public void initiateLoginReset(final Representation entity) throws ResourceException {
        try {
            final Form form = new Form(entity);
            final String usernameOrEmail = form.getValues("username_or_email");
            
            final Optional<User> opUser = coUser.assignPasswordResetUuid(usernameOrEmail);
            
            if (opUser.isPresent()) {
                final User user = opUser.get(); 
                if (StringUtils.isEmpty(user.getEmail())) {
                    getResponse().setEntity(new JsonRepresentation(format("{\"msg\": \"%s\"}", missingEmailError)));
                    getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
                } else {
                    final String emailBody = makePasswordRestEmail(constants.appName(), appUri, user);
                    final SmtpEmailSender sender = new SmtpEmailSender(constants.smptServer());
                    sender.sendPlainMessage(constants.fromEmailAddress(), 
                                            user.getEmail(), 
                                            format("[%s] Please reset your password", constants.appName()), 
                                            emailBody);
                    getResponse().setEntity(new JsonRepresentation("{\"msg\": \"Reset password email is sent.\"}"));
                }
            } else {
                getResponse().setEntity(new JsonRepresentation(format("{\"msg\": \"%s\"}", unidentifiedUserError)));
                getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
            }
            
        } catch (final Exception ex) {
            logger.fatal(ex);
            getResponse().setEntity(new JsonRepresentation(format("{\"msg\": \"%s.\"}", ex.getMessage())));
            getResponse().setStatus(Status.SERVER_ERROR_INTERNAL);
        }
    }

    private String makePasswordRestEmail(final String appName, final String appUri, final User user) {
        final StringBuilder builder = new StringBuilder();
        builder.append(format("We heard that you lost your %s password. Sorry about that!\n\n", appName));
        builder.append("But don’t worry! You can use the following link within the next day to reset your password:\n\n");
        builder.append(format("%sreset_password/%s\n\n", appUri, user.getResetUuid()));
        builder.append(format("If you don’t use this link within 24 hours, it will expire. To get a new password reset link, visit %sforgotten\n\n", appUri));
        builder.append("Thanks,\n");
        builder.append("Your support team");

        return builder.toString();
    }

}