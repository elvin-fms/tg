package ua.com.fielden.platform.web.resources;

import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import ua.com.fielden.platform.domaintree.IGlobalDomainTreeManager;
import ua.com.fielden.platform.domaintree.centre.ICentreDomainTreeManager.ICentreDomainTreeManagerAndEnhancer;
import ua.com.fielden.platform.serialisation.json.TgObjectMapper;
import ua.com.fielden.platform.web.centre.EntityCentre;

import com.fasterxml.jackson.core.JsonProcessingException;

public class CentreResource extends ServerResource {

    private final EntityCentre centre;
    private final IGlobalDomainTreeManager gdtm;

    public CentreResource(//
	    final EntityCentre centre,//
            final Context context, //
            final Request request, //
            final Response response, //
            final IGlobalDomainTreeManager gdtm) {
        init(context, request, response);
        this.centre = centre;
        this.gdtm = gdtm;
    }

    @Override
    protected Representation get() throws ResourceException {
	try {
	    gdtm.initEntityCentreManager(centre.getMenuItemType(), null);
	    final ICentreDomainTreeManagerAndEnhancer cdtmae = gdtm.getEntityCentreManager(centre.getMenuItemType(), null);

	    final String centreString = new TgObjectMapper().writeValueAsString(cdtmae);
	    return new JsonRepresentation(centreString);
   	} catch(final JsonProcessingException jpe) {
   	    jpe.printStackTrace();
   	    throw new RuntimeException(jpe);
	}
    }

//    private Representation retrieveAndInit(final EntityResultQueryModel<EntityCentreConfig> model) {
//        try {
//            final EntityCentreConfig entityCentre = eccc.getEntity(from(model).model());
//            final CentreDomainTreeManagerAndEnhancer cdtmae = serialiser.deserialise(entityCentre.getConfigBody(), CentreDomainTreeManagerAndEnhancer.class);
//            final String centreString = new TgObjectMapper().writeValueAsString(cdtmae);
//            return new JsonRepresentation(centreString);
//        } catch (final Exception e) {
//            throw new IllegalArgumentException("Couldn't deserialise the entity centre.");
//        }
//    }
//
//    private EntityResultQueryModel<EntityCentreConfig> modelSystemUser() {
//        final EntityResultQueryModel<EntityCentreConfig> model =
//        /*    */select(EntityCentreConfig.class).where().//
//        /*    */prop("owner.key").eq().val(username).and().// look for entity-centres for both users (current and its base)
//        /*    */prop("title").eq().val(centre.getMenuItemType().getName()).and().//
//        /*    */prop("menuItem.key").eq().val(centre.getMenuItemType().getName()).model();
//        return model;
//    }
}
