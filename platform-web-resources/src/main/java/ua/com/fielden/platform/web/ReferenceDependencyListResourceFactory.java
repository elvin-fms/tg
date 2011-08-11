package ua.com.fielden.platform.web;

import org.restlet.Restlet;
import org.restlet.data.Method;
import org.restlet.data.Request;
import org.restlet.data.Response;

import ua.com.fielden.platform.serialisation.api.ISerialiser;
import ua.com.fielden.platform.web.resources.ReferenceDependencyListResource;
import ua.com.fielden.platform.web.resources.RestServerUtil;

import com.google.inject.Injector;

/**
 * A factory for {@link ReferenceDependencyListResource}.
 * 
 * @author TG Team
 * 
 */
public class ReferenceDependencyListResourceFactory extends Restlet {
    private final String dependencyLocation;
    private final RestServerUtil restUtil;

    public ReferenceDependencyListResourceFactory(final String dependencyLocation, final Injector injector) {
	this.dependencyLocation = dependencyLocation;
	this.restUtil = new RestServerUtil(injector.getInstance(ISerialiser.class));
    }

    @Override
    public void handle(final Request request, final Response response) {
	super.handle(request, response);
	if (Method.GET == request.getMethod()) {
	    final ReferenceDependencyListResource resource = new ReferenceDependencyListResource(dependencyLocation, restUtil, getContext(), request, response);
	    resource.handleGet();
	}
    }
}
