package org.ifml.ide.ui.graphics;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.ifml.eclipse.ui.graphics.IImageProvider;
import org.ifml.eclipse.ui.graphics.ImageProviders;

/**
 * IFML images.
 */
public enum IfmlImage implements IImageProvider {

    /** The action. */
    ACTION,

    /** The action event. */
    ACTION_EVENT,

    /** The activation expression. */
    ACTIVATION_EXPRESSION,

    /** The annotation. */
    ANNOTATION,

    /** The classifier. */
    CLASSIFIER,

    /** The conditional expression. */
    CONDITIONAL_EXPRESSION,

    /** The data binding. */
    DATA_BINDING,

    /** The data flow. */
    DATA_FLOW,

    /** The details view component. */
    DETAILS,

    /** The dynamic behavior. */
    DYNAMIC_BEHAVIOR,

    /** The entity. */
    ENTITY,

    /** The expression. */
    EXPRESSION,

    /** The form view component. */
    FORM,

    /** The interaction flow expression. */
    INTERACTION_FLOW_EXPRESSION,

    /** The interaction flow model. */
    INTERACTION_FLOW_MODEL,

    /** The list view component. */
    LIST,

    /** The navigation flow. */
    NAVIGATION_FLOW,

    /** The parameter. */
    PARAMETER,

    /** The parameter binding. */
    PARAMETER_BINDING,

    /** The parameter binding group. */
    PARAMETER_BINDING_GROUP,

    /** The pop-up. */
    POP_UP,

    /** The select event. */
    SELECT_EVENT,

    /** The selection field. */
    SELECTION_FIELD,

    /** The selector. */
    SELECTOR,

    /** The simple field. */
    SIMPLE_FIELD,

    /** The structural feature. */
    STRUCTURAL_FEATURE,

    /** The submit event. */
    SUBMIT_EVENT,

    /** The system event. */
    SYSTEM_EVENT,

    /** The view component. */
    VIEW_COMPONENT,

    /** The view component part. */
    VIEW_COMPONENT_PART,

    /** The view container. */
    VIEW_CONTAINER,

    /** The view element event. */
    VIEW_ELEMENT_EVENT,

    /** The viewpoint. */
    VIEWPOINT,

    /** The window. */
    WINDOW;

    @Override
    public Image get() {
        return ImageProviders.get(this);
    }

    @Override
    public Image getDisabled() {
        return ImageProviders.getDisabled(this);
    }

    @Override
    public ImageDescriptor getDescriptor() {
        return ImageProviders.getDescriptor(this);
    }

    @Override
    public ImageDescriptor getDisabledDescriptor() {
        return ImageProviders.getDisabledDescriptor(this);
    }

    @Override
    public Image get(IImageProvider topLeftOverlayProvider, IImageProvider topRightOverlayProvider,
            IImageProvider bottomRightOverlayProvider, IImageProvider bottomLeftOverlayProvider) {
        return ImageProviders.get(this, topLeftOverlayProvider, topRightOverlayProvider, bottomRightOverlayProvider,
                bottomLeftOverlayProvider);
    }

    @Override
    public Image getDisabled(IImageProvider topLeftOverlayProvider, IImageProvider topRightOverlayProvider,
            IImageProvider bottomRightOverlayProvider, IImageProvider bottomLeftOverlayProvider) {
        return ImageProviders.getDisabled(this, topLeftOverlayProvider, topRightOverlayProvider, bottomRightOverlayProvider,
                bottomLeftOverlayProvider);
    }

    @Override
    public ImageDescriptor getDescriptor(IImageProvider topLeftOverlayProvider, IImageProvider topRightOverlayProvider,
            IImageProvider bottomRightOverlayProvider, IImageProvider bottomLeftOverlayProvider) {
        return ImageProviders.getDescriptor(this, topLeftOverlayProvider, topRightOverlayProvider, bottomRightOverlayProvider,
                bottomLeftOverlayProvider);
    }

    @Override
    public ImageDescriptor getDisabledDescriptor(IImageProvider topLeftOverlayProvider, IImageProvider topRightOverlayProvider,
            IImageProvider bottomRightOverlayProvider, IImageProvider bottomLeftOverlayProvider) {
        return ImageProviders.getDisabledDescriptor(this, topLeftOverlayProvider, topRightOverlayProvider, bottomRightOverlayProvider,
                bottomLeftOverlayProvider);
    }

}
