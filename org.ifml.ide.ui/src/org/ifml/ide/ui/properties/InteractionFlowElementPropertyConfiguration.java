package org.ifml.ide.ui.properties;

import java.util.Set;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.ifml.model.core.CorePackage;
import org.ifml.model.core.InteractionFlowElement;

import com.google.common.collect.ImmutableSet;

final class InteractionFlowElementPropertyConfiguration extends AbstractIfmlPropertyConfiguration<InteractionFlowElement> {

    public InteractionFlowElementPropertyConfiguration() {
        super(InteractionFlowElement.class, CorePackage.Literals.INTERACTION_FLOW_ELEMENT);
    }

    @Override
    public Set<EStructuralFeature> getFeaturesToIgnore() {
        return ImmutableSet.<EStructuralFeature> of(CorePackage.Literals.INTERACTION_FLOW_ELEMENT__IN_INTERACTION_FLOWS);
    }

}
