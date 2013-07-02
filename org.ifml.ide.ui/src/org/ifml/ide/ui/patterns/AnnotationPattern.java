package org.ifml.ide.ui.patterns;

import java.util.List;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.context.ILayoutContext;
import org.eclipse.graphiti.features.context.IResizeShapeContext;
import org.eclipse.graphiti.mm.algorithms.Polygon;
import org.eclipse.graphiti.mm.algorithms.Polyline;
import org.eclipse.graphiti.mm.algorithms.Text;
import org.eclipse.graphiti.mm.algorithms.styles.Orientation;
import org.eclipse.graphiti.mm.pictograms.Anchor;
import org.eclipse.graphiti.mm.pictograms.AnchorContainer;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeCreateService;
import org.eclipse.graphiti.util.ColorConstant;
import org.ifml.ide.ui.graphics.IfmlColors;
import org.ifml.model.core.ContentModel;
import org.ifml.model.core.InteractionFlowModel;
import org.ifml.model.uml.Annotation;
import org.ifml.model.uml.UmlPackage;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;

final class AnnotationPattern extends AbstractIfmlShapePattern<Annotation> {

    private static final int PADDING = 2;

    private static final int CORNER_OFFSET = 10;

    private static final int MIN_HEIGHT = 30;

    public AnnotationPattern() {
        super(Annotation.class);
    }

    @Override
    public EClass getEClass() {
        return UmlPackage.Literals.ANNOTATION;
    }

    @Override
    protected List<PictogramElement> addPictogramElements(IAddContext context) {
        PictogramElement mainPe = addPictogramElement(context);
        PictogramElement subPe = addConnectionPictogramElement(mainPe, context);
        if (subPe != null) {
            return ImmutableList.of(mainPe, subPe);
        } else {
            return ImmutableList.of(mainPe);
        }
    }

    @Override
    protected PictogramElement addPictogramElement(IAddContext context) {

        /* retrieves services */
        IPeCreateService peCreateService = Graphiti.getPeCreateService();
        IGaService gaService = Graphiti.getGaService();

        /* creates the shape hierarchy: polygon > stereotype text, text, corner polygon */
        ContainerShape targetShape = getDiagram();
        ContainerShape containerShape = peCreateService.createContainerShape(targetShape, true);
        Polygon p = gaService.createPolygon(containerShape);
        p.setForeground(manageColor(IfmlColors.DEFAULT_SHAPE_FOREGROUND));
        p.setBackground(manageColor(IfmlColors.DEFAULT_SHAPE_BACKGROUND));
        gaService.setLocation(p, context.getX(), context.getY());
        Text stereotypeText = gaService.createDefaultText(getDiagram(), p);
        stereotypeText.setForeground(manageColor(ColorConstant.BLACK));
        stereotypeText.setHorizontalAlignment(Orientation.ALIGNMENT_CENTER);
        stereotypeText.setVerticalAlignment(Orientation.ALIGNMENT_CENTER);
        Text textText = gaService.createDefaultText(getDiagram(), p);
        textText.setForeground(manageColor(ColorConstant.BLACK));
        textText.setHorizontalAlignment(Orientation.ALIGNMENT_CENTER);
        textText.setVerticalAlignment(Orientation.ALIGNMENT_CENTER);
        Polygon cp = gaService.createPolygon(p);
        cp.setForeground(manageColor(IfmlColors.DEFAULT_SHAPE_FOREGROUND));
        cp.setBackground(manageColor(ColorConstant.WHITE));
        cp.getPoints().addAll(gaService.createPointList(new int[] { 0, CORNER_OFFSET, 0, 0, CORNER_OFFSET, CORNER_OFFSET }));
        gaService.setSize(cp, CORNER_OFFSET, CORNER_OFFSET);
        peCreateService.createChopboxAnchor(containerShape);
        return containerShape;
    }

    private PictogramElement addConnectionPictogramElement(PictogramElement mainPe, IAddContext context) {
        Object targetObj = getBusinessObjectForPictogramElement(context.getTargetContainer());
        if ((targetObj instanceof InteractionFlowModel) || (targetObj instanceof ContentModel)) {
            return null;
        }

        /* retrieves services */
        IPeCreateService peCreateService = Graphiti.getPeCreateService();
        IGaService gaService = Graphiti.getGaService();

        /* creates the polyline */
        Connection conn = peCreateService.createFreeFormConnection(getDiagram());
        Anchor sourceAnchor = Graphiti.getPeService().getChopboxAnchor((AnchorContainer) mainPe);
        Anchor targetAnchor = null;
        if (context.getTargetContainer() != null) {
            targetAnchor = Graphiti.getPeService().getChopboxAnchor(context.getTargetContainer());
        } else {
            targetAnchor = Graphiti.getPeService().getChopboxAnchor(context.getTargetConnection().getConnectionDecorators().get(1));
        }
        conn.setStart(sourceAnchor);
        conn.setEnd(targetAnchor);
        Polyline polyline = gaService.createPlainPolyline(conn);
        polyline.setForeground(manageColor(IfmlColors.DEFAULT_SHAPE_FOREGROUND));
        return conn;
    }

    @Override
    public boolean layout(ILayoutContext context) {
        if (context.getPictogramElement() instanceof Connection) {
            return true; // TODO
        }
        Annotation ann = (Annotation) getBusinessObjectForPictogramElement(context.getPictogramElement());

        /* retrieves services */
        IGaService gaService = Graphiti.getGaService();

        /* retrieves figures */
        ContainerShape containerShape = (ContainerShape) context.getPictogramElement();
        Polygon p = getGa(containerShape, Polygon.class);
        Text stereotypeText = getGa(containerShape, Text.class, 0);
        Text textText = getGa(containerShape, Text.class, 1);
        Polygon cp = getGa(containerShape, Polygon.class, 2);

        /* updates the text */
        stereotypeText.setValue(getStereotype());
        textText.setValue(Strings.nullToEmpty(ann.getText()));

        /* computes the text size */
        Dimension stereotypeTextDim = calculateSize(stereotypeText, 25, 14);
        Dimension textTextDim = calculateSize(textText, 25, 14);

        /* sets locations and dimensions */
        int polygonWidth = Math.max(stereotypeTextDim.width, textTextDim.width) + PADDING * 2 + CORNER_OFFSET;
        int polygonHeight = Math.max(stereotypeTextDim.height + PADDING * 4 + textTextDim.height, MIN_HEIGHT);
        gaService.setLocationAndSize(stereotypeText, PADDING, PADDING, polygonWidth - PADDING * 2 - CORNER_OFFSET,
                stereotypeTextDim.height);
        gaService.setLocationAndSize(textText, PADDING, stereotypeTextDim.height + PADDING * 3, polygonWidth - PADDING * 2
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
    public boolean canResizeShape(IResizeShapeContext context) {
        return false;
    }

}
