package org.ifml.ide.ui.features;

import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICustomContext;
import org.eclipse.graphiti.features.context.impl.CreateContext;
import org.eclipse.graphiti.features.custom.AbstractCustomFeature;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.ifml.eclipse.graphiti.features.CustomContexts;
import org.ifml.eclipse.graphiti.platform.GfImageProviders;
import org.ifml.ide.ui.viewers.IfmlViewers;
import org.ifml.model.uml.Element;
import org.ifml.model.uml.UmlPackage;

final class AddAnnotationFeature extends AbstractCustomFeature {

    public AddAnnotationFeature(IFeatureProvider fp) {
        super(fp);
    }

    @Override
    public String getName() {
        return "Add Annotation";
    }

    @Override
    public String getDescription() {
        return "Add an annotation";
    }

    @Override
    public String getImageId() {
        return GfImageProviders.getId(IfmlViewers.getImageProvider(UmlPackage.Literals.ANNOTATION));
    }

    @Override
    public boolean canExecute(ICustomContext context) {
        return (CustomContexts.getSingleBusinessObject(context, getFeatureProvider()) instanceof Element);
    }

    @Override
    public void execute(ICustomContext context) {
        CreateContext createContext = new CreateContext();
        PictogramElement pe = CustomContexts.getSinglePictogramElement(context);
        if (pe instanceof ContainerShape) {
            createContext.setTargetContainer((ContainerShape) pe);
            GraphicsAlgorithm ga = pe.getGraphicsAlgorithm();
            createContext.setX(ga.getX() - ga.getWidth());
            createContext.setY(ga.getY() - ga.getHeight());
        } else if (pe instanceof Connection) {
            // TODO
            createContext.setTargetConnection((Connection) pe);
            GraphicsAlgorithm ga = pe.getGraphicsAlgorithm();
            createContext.setX(ga.getX() + (ga.getWidth() / 2));
            createContext.setY(ga.getY() - ga.getHeight());
        }
        for (ICreateFeature createFeature : getFeatureProvider().getCreateFeatures()) {
            if ("Annotation".equals(createFeature.getName()) && createFeature.canCreate(createContext)) {
                createFeature.execute(createContext);
                break;
            }
        }
    }

}
