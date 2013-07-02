package org.ifml.ide.ui.diagrams;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.graphiti.tb.IToolBehaviorProvider;
import org.ifml.ide.ui.features.ContentModelFeatureProvider;
import org.ifml.model.core.CorePackage;

/**
 * The Graphiti content model diagram type provider.
 */
public final class ContentModelDiagramTypeProvider extends AbstractIfmlDiagramTypeProvider {

    private IToolBehaviorProvider[] toolBehaviorProviders;

    /** Constructs a new diagram type provider. */
    public ContentModelDiagramTypeProvider() {
        setFeatureProvider(new ContentModelFeatureProvider(this));
    }

    @Override
    public IToolBehaviorProvider[] getAvailableToolBehaviorProviders() {
        if (toolBehaviorProviders == null) {
            toolBehaviorProviders = new IToolBehaviorProvider[] { new ContentModelToolBehaviorProvider(this, getSubModelType()) };
        }
        return toolBehaviorProviders;
    }

    @Override
    protected EClass getSubModelType() {
        return CorePackage.Literals.CONTENT_MODEL;
    }

    @Override
    protected EStructuralFeature getSubModelFeature() {
        return CorePackage.Literals.IFML_MODEL__CONTENT_MODEL;
    }

}
