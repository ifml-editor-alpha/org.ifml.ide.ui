package org.ifml.ide.ui.features;

import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.features.context.ICustomContext;
import org.eclipse.graphiti.features.custom.ICustomFeature;
import org.eclipse.graphiti.pattern.DefaultFeatureProviderWithPatterns;
import org.eclipse.graphiti.pattern.IConnectionPattern;
import org.eclipse.graphiti.pattern.IPattern;
import org.ifml.ide.ui.patterns.InteractionFlowModelPatternSet;

/**
 * The IFML Graphiti feature provider for viewpoints.
 */
public final class ViewpointFeatureProvider extends DefaultFeatureProviderWithPatterns {

    /**
     * Constructs a new provider.
     * 
     * @param dtp
     *            the diagram type provider.
     */
    public ViewpointFeatureProvider(IDiagramTypeProvider dtp) {
        super(dtp);
        for (IPattern pattern : InteractionFlowModelPatternSet.getShapePatterns()) {
            addPattern(pattern);
        }
        for (IConnectionPattern pattern : InteractionFlowModelPatternSet.getConnectionPatterns()) {
            addConnectionPattern(pattern);
        }
    }

    @Override
    public ICustomFeature[] getCustomFeatures(ICustomContext context) {
        return new ICustomFeature[] { new AddParameterBindingGroupFeature(this), new AddAnnotationFeature(this) };
    }

}
