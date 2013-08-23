package ua.com.fielden.platform.javafx.gis.gps;

import java.awt.geom.Point2D;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.shape.Circle;
import ua.com.fielden.platform.javafx.gis.IPoint;

/**
 * A "bead" implementation of {@link MessagePoint} view for non-moving machine.
 *
 * @author Developers
 *
 */
public class MessagePointBead extends Circle implements IMessagePointNode {
    private final MessagePointNodeMixin mixin;

    public MessagePointBead(final IPoint<MessagePoint> contract, final MessagePoint messagePoint, final double initialHalfSize, final int initialZoom, final Scene scene, final Group path) {
	super(0, 0, 0);

	this.mixin = new MessagePointNodeMixin(this, contract, messagePoint, initialHalfSize, initialZoom, 0.5, scene, null /* no special selectedColor */, path);

	setRadius(this.mixin.initialSize());
    }

    @Override
    public double getSize() {
	return mixin.getSize();
    }

    @Override
    public void updateSizesAndCentre(final double x1) {
	mixin.updateSizesAndCentre(x1);
    }

    @Override
    public MessagePoint messagePoint() {
	return mixin.messagePoint();
    }

    @Override
    public void updateTooltip() {
	mixin.updateTooltip();
    }

    @Override
    public void updateColor() {
	mixin.updateColor();
    }

    @Override
    public void clickedAction() {
	mixin.clickedAction();
    }

    @Override
    public void select() {
	mixin.select();
    }

    @Override
    public boolean selected() {
	return mixin.selected();
    }

    @Override
    public void unselect() {
	mixin.unselect();
    }

    @Override
    public void update() {
	mixin.update();
    }

    @Override
    public void setZoom(final int zoom) {
	mixin.setZoom(zoom);
    }

    @Override
    public void updateDirection(final double vectorAngle) {
	// no need to update direction for round shape
    }

    @Override
    public void makeVisibleAndUpdate(final Point2D xY, final Group parent, final int zoom) {
	mixin.makeVisibleAndUpdate(xY, parent, zoom);
    }

    @Override
    public void createAndAddCallout(final Group parent) {
	mixin.createAndAddCallout(parent);
    }

    @Override
    public void closeCallout() {
	mixin.closeCallout();
    }

    @Override
    public void makeInvisible() {
	mixin.makeInvisible();
    }

    @Override
    public void add(final Group parent) {
	mixin.add(parent);
    }
}