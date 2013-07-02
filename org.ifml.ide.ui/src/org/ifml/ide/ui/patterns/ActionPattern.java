package org.ifml.ide.ui.patterns;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.graphiti.datatypes.IDimension;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.context.ILayoutContext;
import org.eclipse.graphiti.features.context.IResizeShapeContext;
import org.eclipse.graphiti.features.context.impl.LayoutContext;
import org.eclipse.graphiti.mm.algorithms.Polygon;
import org.eclipse.graphiti.mm.algorithms.Rectangle;
import org.eclipse.graphiti.mm.algorithms.Text;
import org.eclipse.graphiti.mm.algorithms.styles.Orientation;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeCreateService;
import org.eclipse.graphiti.ui.services.GraphitiUi;
import org.eclipse.graphiti.ui.services.IUiLayoutService;
import org.eclipse.graphiti.util.ColorConstant;
import org.ifml.ide.ui.graphics.IfmlColors;
import org.ifml.model.core.Action;
import org.ifml.model.core.ActionEvent;
import org.ifml.model.core.CorePackage;

final class ActionPattern extends AbstractIfmlShapePattern<Action> {

    private static final int PADDING = 10;

    public ActionPattern() {
        super(Action.class);
    }

    @Override
    public EClass getEClass() {
        return CorePackage.Literals.ACTION;
    }

    @Override
    protected PictogramElement addPictogramElement(IAddContext context) {

        /* retrieves services */
        IPeCreateService peCreateService = Graphiti.getPeCreateService();
        IGaService gaService = Graphiti.getGaService();

        /* creates the shape hierarchy: invisible rectangle > polygon > text */
        ContainerShape containerShape = peCreateService.createContainerShape(context.getTargetContainer(), true);
        Rectangle invisibleRect = gaService.createInvisibleRectangle(containerShape);
        gaService.setLocation(invisibleRect, context.getX() - EventPattern.SPACING, context.getY() - EventPattern.SPACING);
        Polygon p = gaService.createPolygon(invisibleRect);
        p.setForeground(manageColor(IfmlColors.DEFAULT_SHAPE_FOREGROUND));
        p.setBackground(manageColor(IfmlColors.DEFAULT_SHAPE_BACKGROUND));
        gaService.setLocation(p, EventPattern.SPACING, EventPattern.SPACING);
        Text text = gaService.createDefaultText(getDiagram(), p);
        text.setForeground(manageColor(ColorConstant.BLACK));
        text.setHorizontalAlignment(Orientation.ALIGNMENT_CENTER);
        text.setVerticalAlignment(Orientation.ALIGNMENT_CENTER);
        gaService.setLocation(text, PADDING, PADDING);
        peCreateService.createChopboxAnchor(containerShape);
        return containerShape;
    }

    @Override
    public boolean layout(ILayoutContext context) {
        Action action = (Action) getBusinessObjectForPictogramElement(context.getPictogramElement());

        /* retrieves services */
        IGaService gaService = Graphiti.getGaService();
        IUiLayoutService layoutService = GraphitiUi.getUiLayoutService();

        /* retrieves figures */
        ContainerShape containerShape = (ContainerShape) context.getPictogramElement();
        Rectangle invisibleRect = getGa(containerShape, Rectangle.class);
        Polygon p = getGa(containerShape, Polygon.class, 0);
        Text text = getGa(containerShape, Text.class, 0, 0);

        /* updates the text */
        text.setValue(action.getName());

        /* sets dimensions based on text size */
        IDimension textDim = layoutService.calculateTextSize(text.getValue(), text.getFont());
        int textWidth = Math.max(textDim.getWidth(), 25);
        int textHeight = Math.max(textDim.getHeight(), 14);
        int polygonWidth = textWidth + PADDING * 2;
        int polygonHeight = textHeight + PADDING * 2;
        int rectWidth = polygonWidth + EventPattern.SPACING * 2;
        int rectHeight = polygonHeight + EventPattern.SPACING * 2;
        p.getPoints().clear();
        int[] xy = new int[] { 0, polygonHeight / 2, PADDING, 0, polygonWidth - PADDING, 0, polygonWidth, polygonHeight / 2,
                polygonWidth - PADDING, polygonHeight, PADDING, polygonHeight };
        p.getPoints().addAll(gaService.createPointList(xy));
        gaService.setSize(invisibleRect, rectWidth, rectHeight);
        gaService.setSize(p, polygonWidth, polygonHeight);
        gaService.setSize(text, textWidth, textHeight);

        /* asks events to re-layout */
        for (ActionEvent event : action.getActionEvents()) {
            ContainerShape shape = (ContainerShape) getMappingProvider().getPictogramElementForBusinessObject(event);
            LayoutContext ctx = new LayoutContext(shape);
            ctx.putProperty(AVOID_PARENT_LAYOUT, Boolean.TRUE);
            getFeatureProvider().layoutIfPossible(ctx);
        }
        return true;
    }

    @Override
    public boolean canResizeShape(IResizeShapeContext context) {
        return false;
    }

    // private static Rectangle getInvisibleRectangle(ContainerShape containerShape) {
    // return (Rectangle) containerShape.getGraphicsAlgorithm();
    // }
    //
    // private static Polygon getPolygon(ContainerShape containerShape) {
    // Rectangle invisibleRect = getInvisibleRectangle(containerShape);
    // return (Polygon) invisibleRect.getGraphicsAlgorithmChildren().get(0);
    // }
    //
    // private static Text getText(ContainerShape containerShape) {
    // Polygon p = getPolygon(containerShape);
    // return (Text) p.getGraphicsAlgorithmChildren().get(0);
    // }

}
