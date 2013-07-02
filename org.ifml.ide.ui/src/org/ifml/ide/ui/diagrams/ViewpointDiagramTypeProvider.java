package org.ifml.ide.ui.diagrams;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.graphiti.tb.IToolBehaviorProvider;
import org.ifml.ide.ui.features.ViewpointFeatureProvider;
import org.ifml.model.core.CorePackage;

/**
 * The Graphiti viewpoint diagram type provider.
 */
public final class ViewpointDiagramTypeProvider extends AbstractIfmlDiagramTypeProvider {

    private IToolBehaviorProvider[] toolBehaviorProviders;

    /** Constructs a new diagram type provider. */
    public ViewpointDiagramTypeProvider() {
        setFeatureProvider(new ViewpointFeatureProvider(this));
    }

    @Override
    public IToolBehaviorProvider[] getAvailableToolBehaviorProviders() {
        if (toolBehaviorProviders == null) {
            toolBehaviorProviders = new IToolBehaviorProvider[] { new ViewpointToolBehaviorProvider(this, getSubModelType()) };
        }
        return toolBehaviorProviders;
    }

    @Override
    protected EClass getSubModelType() {
        return CorePackage.Literals.VIEW_POINT;
    }

    @Override
    protected EStructuralFeature getSubModelFeature() {
        return CorePackage.Literals.IFML_MODEL__INTERACTION_FLOW_MODEL_VIEW_POINTS;
    }

}
