package org.ifml.ide.ui.patterns;

import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.context.ILayoutContext;
import org.eclipse.graphiti.features.context.IMoveShapeContext;
import org.eclipse.graphiti.features.context.IResizeShapeContext;
import org.eclipse.graphiti.mm.algorithms.Ellipse;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.algorithms.Rectangle;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeCreateService;
import org.eclipse.graphiti.util.ColorConstant;
import org.eclipse.swt.graphics.Point;
import org.ifml.eclipse.graphiti.algorithms.GraphicsAlgorithms;
import org.ifml.eclipse.graphiti.services.GaServices;
import org.ifml.eclipse.ui.graphics.RectangleEdge;
import org.ifml.ide.ui.graphics.IfmlColors;
import org.ifml.model.core.Event;
import org.ifml.model.core.InteractionFlowModel;

abstract class EventPattern<T extends Event> extends AbstractIfmlShapePattern<T> {

    /**
     * Constructs a new pattern.
     * 
     * @param instanceClass
     *            the instance class.
     */
    public EventPattern(Class<T> instanceClass) {
        super(instanceClass);
    }

    static final int SPACING = 16;

    @Override
    protected final PictogramElement addPictogramElement(IAddContext context) {

        /* retrieves services */
        IPeCreateService peCreateService = Graphiti.getPeCreateService();
        IGaService gaService = Graphiti.getGaService();

        /* creates the circle */
        ContainerShape containerShape = peCreateService.createContainerShape(context.getTargetContainer(), true);
        Ellipse ellipse = gaService.createEllipse(containerShape);
        ellipse.setForeground(manageColor(IfmlColors.DEFAULT_SHAPE_FOREGROUND));
        ellipse.setBackground(manageColor(ColorConstant.WHITE));
        gaService.setSize(ellipse, SPACING, SPACING);

        /* creates the internal figure */
        addInternalFigure(ellipse);
        gaService.setLocation(ellipse, context.getX(), context.getY());
        peCreateService.createChopboxAnchor(containerShape);
        return containerShape;
    }

    protected void addInternalFigure(Ellipse ellipse) {
    }

    @Override
    public final boolean layout(ILayoutContext context) {
        IGaService gaService = Graphiti.getGaService();
        Event event = (Event) getBusinessObjectForPictogramElement(context.getPictogramElement());
        if (event.eContainer() instanceof InteractionFlowModel) {
            return false;
        }

        /* reallocates the circle on the boundaries of its parent figure */
        ContainerShape containerShape = (ContainerShape) context.getPictogramElement();
        Ellipse ellipse = (Ellipse) containerShape.getGraphicsAlgorithm();
        GraphicsAlgorithm parentGa = getParentGa(containerShape.getContainer());
        int x = 0;
        int y = 0;
        int minX = parentGa.getX() - ellipse.getWidth();
        int maxX = parentGa.getX() + parentGa.getWidth();
        int minY = parentGa.getY() - ellipse.getHeight();
        int maxY = parentGa.getY() + parentGa.getHeight();
        switch (RectangleEdge.get(GraphicsAlgorithms.getBounds(parentGa), new Point(ellipse.getX(), ellipse.getY()))) {
        case TOP:
            x = ellipse.getX();
            y = minY;
            break;
        case LEFT:
            x = minX;
            y = ellipse.getY();
            break;
        case BOTTOM:
            x = ellipse.getX();
            y = maxY;
            break;
        case RIGHT:
            x = maxX;
            y = ellipse.getY();
            break;
        default:
            break;
        }
        if (x < minX) {
            x = minX;
        } else if (x > maxX) {
            x = maxX;
        }
        if (y < minY) {
            y = minY;
        } else if (y > maxY) {
            y = maxY;
        }
        gaService.setLocation(ellipse, x, y);
        return true;
    }

    private static GraphicsAlgorithm getParentGa(ContainerShape containerShape) {
        Rectangle invisibleRect = (Rectangle) containerShape.getGraphicsAlgorithm();
        return invisibleRect.getGraphicsAlgorithmChildren().get(0);
    }

    @Override
    public final boolean canResizeShape(IResizeShapeContext context) {
        return false;
    }

    @Override
    public final boolean canMoveShape(IMoveShapeContext context) {
        return true;
    }

    @Override
    protected final void internalMove(IMoveShapeContext context) {
        GraphicsAlgorithm ga = context.getShape().getGraphicsAlgorithm();
        Point startPoint = GaServices.toAbsolute(ga.getX(), ga.getY(), context.getSourceContainer());
        Point endPoint = GaServices.toAbsolute(context.getX(), context.getY(), context.getTargetContainer());
        Graphiti.getGaService().setLocation(ga, ga.getX() + (endPoint.x - startPoint.x), ga.getY() + (endPoint.y - startPoint.y),
                false);

    }

    @Override
    protected final void postMoveShape(IMoveShapeContext context) {
        layoutPictogramElement(context.getPictogramElement());
    }

}
