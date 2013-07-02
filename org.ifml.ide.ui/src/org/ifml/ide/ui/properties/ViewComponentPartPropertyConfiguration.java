package org.ifml.ide.ui.properties;

import java.util.Set;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.ifml.model.core.CorePackage;
import org.ifml.model.core.ViewComponentPart;

import com.google.common.collect.ImmutableSet;

final class ViewComponentPartPropertyConfiguration extends AbstractIfmlPropertyConfiguration<ViewComponentPart> {

    public ViewComponentPartPropertyConfiguration() {
        super(ViewComponentPart.class, CorePackage.Literals.VIEW_COMPONENT_PART);
    }

    @Override
    public Set<EStructuralFeature> getFeaturesToIgnore() {
        return ImmutableSet.<EStructuralFeature> of(CorePackage.Literals.VIEW_COMPONENT_PART__PARENT_VIEW_COMPONENT_PART,
                CorePackage.Literals.VIEW_COMPONENT_PART__SUB_VIEW_COMPONENT_PARTS);
    }

}
