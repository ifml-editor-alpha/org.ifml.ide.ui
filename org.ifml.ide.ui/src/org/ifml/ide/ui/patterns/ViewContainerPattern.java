package org.ifml.ide.ui.patterns;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.context.ILayoutContext;
import org.eclipse.graphiti.features.context.IMoveShapeContext;
import org.eclipse.graphiti.features.context.impl.LayoutContext;
import org.eclipse.graphiti.mm.algorithms.Rectangle;
import org.eclipse.graphiti.mm.algorithms.Text;
import org.eclipse.graphiti.mm.algorithms.styles.Orientation;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeCreateService;
import org.eclipse.graphiti.util.ColorConstant;
import org.ifml.base.Objects2;
import org.ifml.eclipse.graphiti.geometry.Rectangles;
import org.ifml.ide.ui.graphics.IfmlColors;
import org.ifml.model.core.CorePackage;
import org.ifml.model.core.ViewContainer;
import org.ifml.model.core.ViewElementEvent;

import com.google.common.base.Strings;

class ViewContainerPattern<T extends ViewContainer> extends AbstractIfmlShapePattern<T> {

    private static final int MIN_WIDTH = 100;

    private static final int MIN_HEIGHT = 75;

    private static final int TEXT_PADDING = 2;

    private static final int BOUNDS_PADDING = 5;

    public static ViewContainerPattern<ViewContainer> newInstance() {
        return new ViewContainerPattern<ViewContainer>(ViewContainer.class);
    }

    protected ViewContainerPattern(Class<T> instanceClass) {
        super(instanceClass);
    }

    @Override
    public EClass getEClass() {
        return CorePackage.Literals.VIEW_CONTAINER;
    }

    @Override
    protected final PictogramElement addPictogramElement(IAddContext context) {

        /* retrieves services */
        IPeCreateService peCreateService = Graphiti.getPeCreateService();
        IGaService gaService = Graphiti.getGaService();

        /* creates the shape hierarchy: invisible rectangle > rectangle > title rectangle > text */
        ContainerShape containerShape = peCreateService.createContainerShape(context.getTargetContainer(), true);
        Rectangle invisibleRect = gaService.createInvisibleRectangle(containerShape);
        gaService.setLocationAndSize(invisibleRect, context.getX() - EventPattern.SPACING, context.getY() - EventPattern.SPACING,
                MIN_WIDTH, MIN_HEIGHT);
        Rectangle rect = gaService.createRectangle(invisibleRect);
        gaService.setLocation(rect, EventPattern.SPACING, EventPattern.SPACING);
        rect.setForeground(manageColor(IfmlColors.DEFAULT_SHAPE_FOREGROUND));
        rect.setBackground(manageColor(ColorConstant.WHITE));
        Rectangle titleRect = gaService.createRectangle(rect);
        gaService.setLocation(titleRect, 0, 0);
        titleRect.setForeground(manageColor(IfmlColors.DEFAULT_SHAPE_FOREGROUND));
        titleRect.setBackground(manageColor(ColorConstant.WHITE));
        Text text = gaService.createDefaultText(getDiagram(), titleRect);
        text.setForeground(manageColor(ColorConstant.BLACK));
        text.setHorizontalAlignment(Orientation.ALIGNMENT_CENTER);
        text.setVerticalAlignment(Orientation.ALIGNMENT_CENTER);
        gaService.setLocation(text, TEXT_PADDING, TEXT_PADDING);
        peCreateService.createChopboxAnchor(containerShape);
        return containerShape;
    }

    @Override
    public final boolean layout(ILayoutContext context) {
        ViewContainer viewCont = (ViewContainer) getBusinessObjectForPictogramElement(context.getPictogramElement());

        /* retrieves services */
        IGaService gaService = Graphiti.getGaService();

        /* retrieves figures */
        ContainerShape containerShape = (ContainerShape) context.getPictogramElement();
        Rectangle invisibleRect = getGa(containerShape, Rectangle.class);
        Rectangle rect = getGa(containerShape, Rectangle.class, 0);
        Rectangle titleRect = getGa(containerShape, Rectangle.class, 0, 0);
        Text text = getGa(containerShape, Text.class, 0, 0, 0);

        /* updates the text */
        text.setValue(String.format("%s %s %s", getModifiers(viewCont), getStereotype(), Strings.nullToEmpty(viewCont.getName())));

        /* computes the text size */
        Dimension textDim = calculateSize(text, 25, 14);
        org.eclipse.draw2d.geometry.Rectangle childrenRect = Rectangles.union(calculateBounds(viewCont.getViewElements())).expand(
                BOUNDS_PADDING, BOUNDS_PADDING);

        /* sets dimensions */
        int invisibleRectWidth = Math.max(textDim.width + TEXT_PADDING * 2 + EventPattern.SPACING * 2,
                Math.max(childrenRect.width, MIN_WIDTH));
        int invisibleRectHeight = Math.max(textDim.height + TEXT_PADDING * 2 + EventPattern.SPACING * 2,
                Math.max(childrenRect.height, MIN_HEIGHT));
        gaService.setSize(invisibleRect, invisibleRectWidth, invisibleRectHeight);
        int rectWidth = invisibleRectWidth - EventPattern.SPACING * 2;
        int rectHeight = invisibleRectHeight - EventPattern.SPACING * 2;
        gaService.setSize(rect, rectWidth, rectHeight);
        gaService.setSize(titleRect, rectWidth, textDim.height + TEXT_PADDING * 2);
        gaService.setSize(text, textDim.width, textDim.height);

        /* asks events to re-layout */
        for (ViewElementEvent event : viewCont.getViewElementEvents()) {
            ContainerShape shape = (ContainerShape) getMappingProvider().getPictogramElementForBusinessObject(event);
            LayoutContext ctx = new LayoutContext(shape);
            ctx.putProperty(AVOID_PARENT_LAYOUT, Boolean.TRUE);
            getFeatureProvider().layoutIfPossible(ctx);
        }

        /* asks the parent view container (if any) to re-layout */
        relayoutParentViewContainer(viewCont);
        return true;
    }

    @Override
    public void moveShape(IMoveShapeContext context) {
        super.moveShape(context);
        ViewContainer viewCont = (ViewContainer) getBusinessObjectForPictogramElement(context.getPictogramElement());
        relayoutParentViewContainer(viewCont);
    }

    private void relayoutParentViewContainer(ViewContainer viewCont) {
        ViewContainer parentViewCont = Objects2.as(viewCont.eContainer(), ViewContainer.class);
        if (parentViewCont != null) {
            ContainerShape shape = (ContainerShape) getMappingProvider().getPictogramElementForBusinessObject(parentViewCont);
            LayoutContext ctx = new LayoutContext(shape);
            getFeatureProvider().layoutIfPossible(ctx);
        }
    }

    protected String getModifiers(ViewContainer context) {
        StringBuilder modifiers = new StringBuilder();

        if (context.isIsLandMark()) {
            modifiers.append("[L]");
        }
        if (context.isIsDefault()) {
            modifiers.append("[D]");
        }

        return modifiers.toString();
    }

}
