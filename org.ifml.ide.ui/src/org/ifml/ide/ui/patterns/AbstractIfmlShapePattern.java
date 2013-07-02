package org.ifml.ide.ui.patterns;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.graphiti.features.context.IDeleteContext;
import org.eclipse.graphiti.features.context.impl.LayoutContext;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.ifml.eclipse.graphiti.patterns.AbstractGfEmfShapePattern;
import org.ifml.eclipse.graphiti.platform.GfImageProviders;
import org.ifml.ide.ui.graphics.IfmlImage;
import org.ifml.ide.ui.viewers.IfmlViewers;
import org.ifml.model.IfmlModels;
import org.ifml.model.uml.Element;
import org.ifml.model.uml.NamedElement;

import com.google.common.base.Optional;

abstract class AbstractIfmlShapePattern<T extends EObject> extends AbstractGfEmfShapePattern<T> {

    protected static final String AVOID_PARENT_LAYOUT = "AVOID_PARENT_LAYOUT";

    /**
     * Constructs a new pattern.
     * 
     * @param instanceClass
     *            the instance class.
     */
    public AbstractIfmlShapePattern(Class<T> instanceClass) {
        super(instanceClass);
    }

    @Override
    protected final Optional<EReference> getContainmentReference(EObject parentObject, EObject childObject) {
        return IfmlModels.getContainmentReference(parentObject, childObject);
    }

    @Override
    protected final boolean canAddTo(EObject parentObject) {
        return IfmlModels.canAdd(parentObject.eClass(), getEClass());
    }

    @Override
    protected void postCreate(EObject newObject) {
        super.postCreate(newObject);
        // TODO: move to EMF model
        if (newObject instanceof Element) {
            ((Element) newObject).setId(EcoreUtil.generateUUID());
        }
        if (newObject instanceof NamedElement) {
            ((NamedElement) newObject).setName(getCreateName());
        }
    }

    @Override
    public void delete(IDeleteContext context) {
        ContainerShape parentContainer = ((ContainerShape) context.getPictogramElement()).getContainer();
        super.delete(context);

        /* forces a re-layout of the parent container */
        getFeatureProvider().layoutIfPossible(new LayoutContext(parentContainer));
    }

    @Override
    public String getCreateImageId() {
        IfmlImage img = IfmlViewers.getImageProvider(getEClass());
        if (img != null) {
            return GfImageProviders.getId(img);
        } else {
            return super.getCreateImageId();
        }
    }

    protected final String getStereotype() {
        return String.format("<<%s>>", getEClass().getName());
    }

}
