package ua.com.fielden.platform.dao2;

import ua.com.fielden.platform.attachment.Attachment;
import ua.com.fielden.platform.attachment.IAttachmentController2;
import ua.com.fielden.platform.entity.query.IFilter;
import ua.com.fielden.platform.swing.review.annotations.EntityType;

import com.google.inject.Inject;

@EntityType(Attachment.class)
public class AttachmentDao2 extends CommonEntityDao2<Attachment> implements IAttachmentController2 {

    @Inject
    protected AttachmentDao2(final IFilter filter) {
	super(filter);
    }

    @Override
    public byte[] download(final Attachment attachment) {
	throw new UnsupportedOperationException("");
    }

    @Override
    public void delete(final Attachment entity) {
        defaultDelete(entity);
    }

}