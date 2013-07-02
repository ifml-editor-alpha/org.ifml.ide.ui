package org.ifml.ide.ui.diagrams;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.graphiti.tb.IToolBehaviorProvider;
import org.ifml.ide.ui.features.InteractionFlowModelFeatureProvider;
import org.ifml.model.core.CorePackage;

/**
 * The Graphiti interaction flow model diagram type provider.
 */
public final class InteractionFlowModelDiagramTypeProvider extends AbstractIfmlDiagramTypeProvider {

    private IToolBehaviorProvider[] toolBehaviorProviders;

    /** Constructs a new diagram type provider. */
    public InteractionFlowModelDiagramTypeProvider() {
        setFeatureProvider(new InteractionFlowModelFeatureProvider(this));
    }

    @Override
    public IToolBehaviorProvider[] getAvailableToolBehaviorProviders() {
        if (toolBehaviorProviders == null) {
            toolBehaviorProviders = new IToolBehaviorProvider[] { new InteractionFlowModelToolBehaviorProvider(this, getSubModelType()) };
        }
        return toolBehaviorProviders;
    }

    @Override
    protected EClass getSubModelType() {
        return CorePackage.Literals.INTERACTION_FLOW_MODEL;
    }

    @Override
    protected EStructuralFeature getSubModelFeature() {
        return CorePackage.Literals.IFML_MODEL__INTERACTION_FLOW_MODEL;
    }

}
