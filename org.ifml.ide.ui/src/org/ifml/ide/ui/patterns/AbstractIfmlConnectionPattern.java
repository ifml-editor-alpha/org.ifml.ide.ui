package org.ifml.ide.ui.patterns;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.ifml.eclipse.graphiti.patterns.AbstractGfEmfConnectionPattern;
import org.ifml.eclipse.graphiti.platform.GfImageProviders;
import org.ifml.ide.ui.graphics.IfmlImage;
import org.ifml.ide.ui.viewers.IfmlViewers;
import org.ifml.model.IfmlModels;

import com.google.common.base.Optional;

abstract class AbstractIfmlConnectionPattern<T extends EObject> extends AbstractGfEmfConnectionPattern<T> {

    /**
     * Constructs a new pattern.
     * 
     * @param instanceClass
     *            the instance class.
     */
    public AbstractIfmlConnectionPattern(Class<T> instanceClass) {
        super(instanceClass);
    }

    @Override
    protected final Optional<EReference> getConnectionReference(EClass sourceClass) {
        return IfmlModels.getConnectionReference(sourceClass, getEClass());
    }

    @Override
    protected final Optional<EReference> getInverseConnectionReference(EClass targetClass) {
        return IfmlModels.getInverseConnectionReference(targetClass, getEClass());
    }

    @Override
    protected final Optional<EReference> getSourceReference() {
        return IfmlModels.getSourceReference(getEClass());
    }

    @Override
    protected final Optional<EReference> getTargetReference() {
        return IfmlModels.getTargetReference(getEClass());
    }

    @Override
    protected final boolean canStartFrom(EObject sourceObject) {
        return IfmlModels.canStartFrom(sourceObject, getEClass());
    }

    @Override
    protected final boolean canConnect(EObject sourceObject, EObject targetObject) {
        return IfmlModels.canConnect(sourceObject, targetObject, getEClass());
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

}
