package org.ifml.ide.ui.properties;

import java.util.Set;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.viewers.ComboViewer;
import org.ifml.eclipse.emf.ui.properties.IEmfPropertyComboHandler;
import org.ifml.eclipse.emf.ui.viewers.EmfContentProviders;
import org.ifml.eclipse.ui.viewers.PredicateViewerFilterBuilder;
import org.ifml.ide.ui.viewers.IfmlViewers;
import org.ifml.model.core.ActivationExpression;
import org.ifml.model.core.CorePackage;
import org.ifml.model.core.ViewElement;
import org.ifml.model.uml.Element;

import com.google.common.base.Optional;
import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableSet;

final class ViewElementPropertyConfiguration extends AbstractIfmlPropertyConfiguration<ViewElement> {

    public ViewElementPropertyConfiguration() {
        super(ViewElement.class, CorePackage.Literals.VIEW_ELEMENT);
    }

    @Override
    public Optional<IEmfPropertyComboHandler<ViewElement>> getComboHandler(EStructuralFeature feature) {
        if (feature == CorePackage.Literals.VIEW_ELEMENT__ACTIVATION_EXPRESSION) {
            return Optional.<IEmfPropertyComboHandler<ViewElement>> of(ActivationExpressionComboHandler.INSTANCE);
        }
        return Optional.absent();
    }

    @Override
    public Set<EStructuralFeature> getFeaturesToIgnore() {
        return ImmutableSet.<EStructuralFeature> of(CorePackage.Literals.VIEW_ELEMENT__VIEW_CONTAINER);
    }

    private static enum ActivationExpressionComboHandler implements IEmfPropertyComboHandler<ViewElement> {

        INSTANCE;

        @Override
        public void configureComboViewer(ComboViewer comboViewer, EStructuralFeature feature) {
            comboViewer.setLabelProvider(IfmlViewers.LABEL_PROVIDER);
            comboViewer.setContentProvider(EmfContentProviders
                    .newChildrenContentProvider(CorePackage.Literals.INTERACTION_FLOW_MODEL__INTERACTION_FLOW_MODEL_ELEMENTS));
            comboViewer.addFilter(new PredicateViewerFilterBuilder<Element>().predicate(ActivationExpression.class,
                    Predicates.alwaysTrue()).build());
        }

        @Override
        public Object getComboViewerInput(ViewElement elem, EStructuralFeature feature) {
            return getInteractionFlowModel(elem);
        }

    }

}
