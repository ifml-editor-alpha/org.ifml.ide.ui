package org.ifml.ide.ui.patterns;

import java.util.List;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.context.ILayoutContext;
import org.eclipse.graphiti.features.context.IMoveShapeContext;
import org.eclipse.graphiti.features.context.IResizeShapeContext;
import org.eclipse.graphiti.features.context.impl.LayoutContext;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.algorithms.Polyline;
import org.eclipse.graphiti.mm.algorithms.Rectangle;
import org.eclipse.graphiti.mm.algorithms.RoundedRectangle;
import org.eclipse.graphiti.mm.algorithms.Text;
import org.eclipse.graphiti.mm.algorithms.styles.Orientation;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeCreateService;
import org.eclipse.graphiti.util.ColorConstant;
import org.ifml.base.Objects2;
import org.ifml.eclipse.graphiti.geometry.Dimensions;
import org.ifml.ide.ui.graphics.IfmlColors;
import org.ifml.model.core.CorePackage;
import org.ifml.model.core.ViewComponent;
import org.ifml.model.core.ViewContainer;
import org.ifml.model.core.ViewElementEvent;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

class ViewComponentPattern extends AbstractIfmlShapePattern<ViewComponent> {

    private static final int PADDING = 2;

    private static final int CORNER_SIZE = 10;

    private static final int MIN_HEIGHT = 50;

    public ViewComponentPattern() {
        super(ViewComponent.class);
    }

    @Override
    public EClass getEClass() {
        return CorePackage.Literals.VIEW_COMPONENT;
    }

    @Override
    protected PictogramElement addPictogramElement(IAddContext context) {

        /* retrieves services */
        IPeCreateService peCreateService = Graphiti.getPeCreateService();
        IGaService gaService = Graphiti.getGaService();

        /* creates the shape hierarchy: invisible rectangle > rectangle > text, line */
        ContainerShape containerShape = peCreateService.createContainerShape(context.getTargetContainer(), true);
        Rectangle invisibleRect = gaService.createInvisibleRectangle(containerShape);
        gaService.setLocation(invisibleRect, context.getX() - EventPattern.SPACING, context.getY() - EventPattern.SPACING);
        RoundedRectangle rect = gaService.createRoundedRectangle(invisibleRect, CORNER_SIZE, CORNER_SIZE);
        gaService.setLocation(rect, EventPattern.SPACING, EventPattern.SPACING);
        rect.setForeground(manageColor(IfmlColors.DEFAULT_SHAPE_FOREGROUND));
        rect.setBackground(manageColor(IfmlColors.DEFAULT_SHAPE_BACKGROUND));
        Text text = gaService.createDefaultText(getDiagram(), rect);
        text.setForeground(manageColor(ColorConstant.BLACK));
        text.setHorizontalAlignment(Orientation.ALIGNMENT_CENTER);
        text.setVerticalAlignment(Orientation.ALIGNMENT_CENTER);
        gaService.setLocation(text, PADDING, PADDING);
        Polyline line = gaService.createPolyline(rect);
        line.setForeground(manageColor(IfmlColors.DEFAULT_SHAPE_FOREGROUND));
        peCreateService.createChopboxAnchor(containerShape);
        return containerShape;
    }

    @Override
    public boolean layout(ILayoutContext context) {
        ViewComponent viewComp = (ViewComponent) getBusinessObjectForPictogramElement(context.getPictogramElement());
        EList<? extends EObject> children = getChildren(viewComp);

        /* retrieves services */
        IGaService gaService = Graphiti.getGaService();

        /* retrieves figures */
        ContainerShape containerShape = (ContainerShape) context.getPictogramElement();
        Rectangle invisibleRect = getGa(containerShape, Rectangle.class);
        RoundedRectangle rect = getGa(containerShape, RoundedRectangle.class, 0);
        Text text = getGa(containerShape, Text.class, 0, 0);
        Polyline line = getGa(containerShape, Polyline.class, 0, 1);

        /* updates the text */
        text.setValue(String.format("%s %s", getStereotype(), Strings.nullToEmpty(viewComp.getName())));

        /* computes the text size */
        Dimension textDim = calculateSize(text, 25, 14);

        /* asks children to re-layout */
        for (EObject child : children) {
            ContainerShape shape = (ContainerShape) getMappingProvider().getPictogramElementForBusinessObject(child);
            LayoutContext ctx = new LayoutContext(shape);
            ctx.putProperty(AVOID_PARENT_LAYOUT, Boolean.TRUE);
            getFeatureProvider().layoutIfPossible(ctx);
        }

        /* retrieves sizes of children */
        List<Dimension> childDims = calculateSizes(children);

        /* sets locations and dimensions */
        int rectWidth = Dimensions.maxWidth(Iterables.concat(childDims, ImmutableList.of(textDim))) + PADDING * 2;
        int rectHeight = Dimensions.sumHeight(Iterables.concat(childDims, ImmutableList.of(textDim))) + PADDING * 2
                * (children.size() + 1);
        rectHeight = Math.max(rectHeight, MIN_HEIGHT);
        gaService.setSize(text, rectWidth - PADDING * 2, textDim.height);
        int childHeight = textDim.height + PADDING * 2;
        for (int i = 0; i < children.size(); i++) {
            EObject child = children.get(i);
            Dimension childDim = childDims.get(i);
            ContainerShape shape = (ContainerShape) getMappingProvider().getPictogramElementForBusinessObject(child);
            GraphicsAlgorithm ga = shape.getGraphicsAlgorithm();
            gaService.setLocationAndSize(ga, EventPattern.SPACING + PADDING, EventPattern.SPACING + childHeight, rectWidth - PADDING
                    * 2, childDim.height);
            childHeight += childDim.height + PADDING * 2;
        }
        gaService.setSize(invisibleRect, rectWidth + EventPattern.SPACING * 2, rectHeight + EventPattern.SPACING * 2);
        gaService.setSize(rect, rectWidth, rectHeight);
        line.getPoints().clear();
        int[] lineXY = new int[] { 0, 0, rectWidth, 0 };
        line.getPoints().addAll(gaService.createPointList(lineXY));
        gaService.setLocationAndSize(line, 0, textDim.height, rectWidth, textDim.height + PADDING * 2);

        /* asks events to re-layout */
        for (ViewElementEvent event : viewComp.getViewElementEvents()) {
            ContainerShape shape = (ContainerShape) getMappingProvider().getPictogramElementForBusinessObject(event);
            LayoutContext ctx = new LayoutContext(shape);
            ctx.putProperty(AVOID_PARENT_LAYOUT, Boolean.TRUE);
            getFeatureProvider().layoutIfPossible(ctx);
        }

        /* asks the parent view container (if any) to re-layout */
        relayoutParentViewContainer(viewComp);
        return true;
    }

    @Override
    public void moveShape(IMoveShapeContext context) {
        super.moveShape(context);
        ViewComponent viewComp = (ViewComponent) getBusinessObjectForPictogramElement(context.getPictogramElement());
        relayoutParentViewContainer(viewComp);
    }

    private void relayoutParentViewContainer(ViewComponent viewComp) {
        ViewContainer viewCont = Objects2.as(viewComp.eContainer(), ViewContainer.class);
        if (viewCont != null) {
            ContainerShape shape = (ContainerShape) getMappingProvider().getPictogramElementForBusinessObject(viewCont);
            LayoutContext ctx = new LayoutContext(shape);
            getFeatureProvider().layoutIfPossible(ctx);
        }
    }

    protected EList<? extends EObject> getChildren(ViewComponent viewComp) {
        return viewComp.getViewComponentParts();
    }

    @Override
    public boolean canResizeShape(IResizeShapeContext context) {
        return false;
    }

}
