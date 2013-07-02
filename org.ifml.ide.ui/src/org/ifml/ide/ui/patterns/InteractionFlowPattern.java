package org.ifml.ide.ui.patterns;

import org.eclipse.graphiti.features.context.IAddConnectionContext;
import org.eclipse.graphiti.mm.algorithms.Polygon;
import org.eclipse.graphiti.mm.algorithms.Polyline;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.graphiti.mm.pictograms.ConnectionDecorator;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeCreateService;
import org.ifml.ide.ui.graphics.IfmlColors;
import org.ifml.model.core.InteractionFlow;

abstract class InteractionFlowPattern extends AbstractIfmlConnectionPattern<InteractionFlow> {

    public InteractionFlowPattern() {
        super(InteractionFlow.class);
    }

    @Override
    protected final PictogramElement addPictogramElement(IAddConnectionContext context) {

        /* retrieves services */
        IPeCreateService peCreateService = Graphiti.getPeCreateService();
        IGaService gaService = Graphiti.getGaService();

        /* creates the polyline */
        Connection connection = peCreateService.createFreeFormConnection(getDiagram());
        connection.setStart(context.getSourceAnchor());
        connection.setEnd(context.getTargetAnchor());
        Polyline polyline = gaService.createPlainPolyline(connection);
        polyline.setForeground(manageColor(IfmlColors.DEFAULT_SHAPE_FOREGROUND));

        /* adds an arrow decorator at the end */
        ConnectionDecorator arrowDec = peCreateService.createConnectionDecorator(connection, false, 1.0, true);
        Polygon arrow = createArrow(arrowDec);
        update(polyline, arrow);

        /* adds a middle decorator for attaching a parameter binding group */
        ConnectionDecorator middleDec = peCreateService.createConnectionDecorator(connection, true, 0.5, true);
        gaService.createInvisibleRectangle(middleDec);
        peCreateService.createChopboxAnchor(middleDec);
        return connection;
    }

    /**
     * Creates an arrow.
     * 
     * @param dec
     *            the parent connection decorator.
     * @return the newly created arrow.
     */
    protected Polygon createArrow(ConnectionDecorator dec) {
        IGaService gaService = Graphiti.getGaService();
        Polygon arrow = gaService.createPolygon(dec, new int[] { -10, 6, 0, 0, -10, -6 });
        arrow.setLineWidth(2);
        arrow.setBackground(manageColor(IfmlColors.DEFAULT_SHAPE_FOREGROUND));
        arrow.setForeground(manageColor(IfmlColors.DEFAULT_SHAPE_FOREGROUND));
        return arrow;
    }

    /**
     * Customizes the polyline and the arrow.
     * 
     * @param polyline
     *            the polyline.
     * @param arrow
     *            the arrow.
     */
    protected abstract void update(Polyline polyline, Polygon arrow);

}
