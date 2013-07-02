package org.ifml.ide.ui.patterns;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.context.ILayoutContext;
import org.eclipse.graphiti.features.context.IResizeShapeContext;
import org.eclipse.graphiti.mm.algorithms.Polygon;
import org.eclipse.graphiti.mm.algorithms.Text;
import org.eclipse.graphiti.mm.algorithms.styles.Orientation;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeCreateService;
import org.eclipse.graphiti.util.ColorConstant;
import org.ifml.ide.ui.graphics.IfmlColors;
import org.ifml.model.core.Expression;

import com.google.common.base.Strings;

abstract class ExpressionPattern extends AbstractIfmlShapePattern<Expression> {

    private static final int PADDING = 2;

    private static final int CORNER_OFFSET = 10;

    private static final int MIN_HEIGHT = 30;

    public ExpressionPattern() {
        super(Expression.class);
    }

    @Override
    protected final PictogramElement addPictogramElement(IAddContext context) {

        /* retrieves services */
        IPeCreateService peCreateService = Graphiti.getPeCreateService();
        IGaService gaService = Graphiti.getGaService();

        /* creates the shape hierarchy: polygon > stereotype text, body text, corner polygon */
        ContainerShape containerShape = peCreateService.createContainerShape(context.getTargetContainer(), true);
        Polygon p = gaService.createPolygon(containerShape);
        p.setForeground(manageColor(IfmlColors.DEFAULT_SHAPE_FOREGROUND));
        p.setBackground(manageColor(IfmlColors.DEFAULT_SHAPE_BACKGROUND));
        gaService.setLocation(p, context.getX(), context.getY());
        Text stereotypeText = gaService.createDefaultText(getDiagram(), p);
        stereotypeText.setForeground(manageColor(ColorConstant.BLACK));
        stereotypeText.setHorizontalAlignment(Orientation.ALIGNMENT_CENTER);
        stereotypeText.setVerticalAlignment(Orientation.ALIGNMENT_CENTER);
        Text bodyText = gaService.createDefaultText(getDiagram(), p);
        bodyText.setForeground(manageColor(ColorConstant.BLACK));
        bodyText.setHorizontalAlignment(Orientation.ALIGNMENT_CENTER);
        bodyText.setVerticalAlignment(Orientation.ALIGNMENT_CENTER);
        Polygon cp = gaService.createPolygon(p);
        cp.setForeground(manageColor(IfmlColors.DEFAULT_SHAPE_FOREGROUND));
        cp.setBackground(manageColor(ColorConstant.WHITE));
        cp.getPoints().addAll(gaService.createPointList(new int[] { 0, CORNER_OFFSET, 0, 0, CORNER_OFFSET, CORNER_OFFSET }));
        gaService.setSize(cp, CORNER_OFFSET, CORNER_OFFSET);
        peCreateService.createChopboxAnchor(containerShape);
        return containerShape;
    }

    @Override
    public final boolean layout(ILayoutContext context) {
        Expression expr = (Expression) getBusinessObjectForPictogramElement(context.getPictogramElement());

        /* retrieves services */
        IGaService gaService = Graphiti.getGaService();

        /* retrieves figures */
        ContainerShape containerShape = (ContainerShape) context.getPictogramElement();
        Polygon p = getGa(containerShape, Polygon.class);
        Text stereotypeText = getGa(containerShape, Text.class, 0);
        Text bodyText = getGa(containerShape, Text.class, 1);
        Polygon cp = getGa(containerShape, Polygon.class, 2);

        /* updates the text */
        stereotypeText.setValue(getStereotype());
        bodyText.setValue(Strings.nullToEmpty(expr.getBody()));

        /* computes the text size */
        Dimension stereotypeTextDim = calculateSize(stereotypeText, 25, 14);
        Dimension bodyTextDim = calculateSize(bodyText, 25, 14);

        /* sets locations and dimensions */
        int polygonWidth = Math.max(stereotypeTextDim.width, bodyTextDim.width) + PADDING * 2 + CORNER_OFFSET;
        int polygonHeight = Math.max(stereotypeTextDim.height + PADDING * 4 + bodyTextDim.height, MIN_HEIGHT);
        gaService.setLocationAndSize(stereotypeText, PADDING, PADDING, polygonWidth - PADDING * 2 - CORNER_OFFSET,
                stereotypeTextDim.height);
        gaService.setLocationAndSize(bodyText, PADDING, stereotypeTextDim.height + PADDING * 3, polygonWidth - PADDING * 2
                - CORNER_OFFSET, stereotypeTextDim.height);
        p.getPoints().clear();
        int[] xy = new int[] { 0, polygonHeight, 0, 0, polygonWidth - CORNER_OFFSET, 0, polygonWidth, CORNER_OFFSET, polygonWidth,
                polygonHeight };
        p.getPoints().addAll(gaService.createPointList(xy));
        gaService.setSize(p, polygonWidth, polygonHeight);
        gaService.setLocation(cp, polygonWidth - CORNER_OFFSET, 0);
        return true;
    }

    @Override
    public final boolean canResizeShape(IResizeShapeContext context) {
        return false;
    }

}
