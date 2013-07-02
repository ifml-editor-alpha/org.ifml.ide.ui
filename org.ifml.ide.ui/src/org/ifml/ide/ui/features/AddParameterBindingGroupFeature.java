package org.ifml.ide.ui.features;

import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICustomContext;
import org.eclipse.graphiti.features.context.impl.CreateContext;
import org.eclipse.graphiti.features.custom.AbstractCustomFeature;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.ifml.eclipse.graphiti.features.CustomContexts;
import org.ifml.eclipse.graphiti.platform.GfImageProviders;
import org.ifml.ide.ui.viewers.IfmlViewers;
import org.ifml.model.core.CorePackage;
import org.ifml.model.core.InteractionFlow;

final class AddParameterBindingGroupFeature extends AbstractCustomFeature {

    public AddParameterBindingGroupFeature(IFeatureProvider fp) {
        super(fp);
    }

    @Override
    public String getName() {
        return "Add Parameter Binding Group";
    }

    @Override
    public String getDescription() {
        return "Add a parameter binding group";
    }

    @Override
    public String getImageId() {
        return GfImageProviders.getId(IfmlViewers.getImageProvider(CorePackage.Literals.PARAMETER_BINDING_GROUP));
    }

    @Override
    public boolean canExecute(ICustomContext context) {
        return (CustomContexts.getSingleBusinessObject(context, getFeatureProvider()) instanceof InteractionFlow);
    }

    @Override
    public void execute(ICustomContext context) {
        CreateContext createContext = new CreateContext();
        createContext.setTargetConnection((Connection) CustomContexts.getSinglePictogramElement(context));
        for (ICreateFeature createFeature : getFeatureProvider().getCreateFeatures()) {
            if ("Parameter Binding Group".equals(createFeature.getName()) && createFeature.canCreate(createContext)) {
                createFeature.execute(createContext);
                break;
            }
        }
    }

}
