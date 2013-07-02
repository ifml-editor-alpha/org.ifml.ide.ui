package org.ifml.ide.ui.patterns;

import java.util.List;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.context.ILayoutContext;
import org.eclipse.graphiti.features.context.IResizeShapeContext;
import org.eclipse.graphiti.features.context.impl.LayoutContext;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
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
import org.ifml.eclipse.graphiti.geometry.Dimensions;
import org.ifml.ide.ui.graphics.IfmlColors;
import org.ifml.model.core.CorePackage;
import org.ifml.model.core.ParameterBinding;
import org.ifml.model.core.ParameterBindingGroup;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

final class ParameterBindingGroupPattern extends AbstractIfmlShapePattern<ParameterBindingGroup> {

    public ParameterBindingGroupPattern() {
        super(ParameterBindingGroup.class);
    }

    private static final int PADDING = 2;

    private static final int CORNER_OFFSET = 10;

    private static final int MIN_HEIGHT = 30;

    @Override
    public EClass getEClass() {
        return CorePackage.Literals.PARAMETER_BINDING_GROUP;
    }

    @Override
    protected List<PictogramElement> addPictogramElements(IAddContext context) {
        PictogramElement mainPe = addPictogramElement(context);
        PictogramElement subPe = addConnectionPictogramElement(mainPe, context);
        return ImmutableList.of(mainPe, subPe);
    }

    @Override
    protected PictogramElement addPictogramElement(IAddContext context) {

        /* retrieves services */
        IPeCreateService peCreateService = Graphiti.getPeCreateService();
        IGaService gaService = Graphiti.getGaService();

        /* creates the shape hierarchy: polygon > text */
        ContainerShape targetShape = null;
        PictogramElement targetElem = Objects.firstNonNull(context.getTargetContainer(), context.getTargetConnection());
        if (targetElem instanceof ContainerShape) {
            targetShape = (ContainerShape) targetElem;
        } else { // Connection
            targetShape = getDiagram();
        }
        ContainerShape containerShape = peCreateService.createContainerShape(targetShape, true);
        Polygon p = gaService.createPolygon(containerShape);
        p.setForeground(manageColor(IfmlColors.DEFAULT_SHAPE_FOREGROUND));
        p.setBackground(manageColor(IfmlColors.DEFAULT_SHAPE_BACKGROUND));
        gaService.setLocation(p, context.getX(), context.getY());
        Text text = gaService.createDefaultText(getDiagram(), p);
        text.setForeground(manageColor(ColorConstant.BLACK));
        text.setHorizontalAlignment(Orientation.ALIGNMENT_CENTER);
        text.setVerticalAlignment(Orientation.ALIGNMENT_CENTER);
        peCreateService.createChopboxAnchor(containerShape);
        return containerShape;
    }

    private PictogramElement addConnectionPictogramElement(PictogramElement mainPe, IAddContext context) {

        /* retrieves services */
        IPeCreateService peCreateService = Graphiti.getPeCreateService();
        IGaService gaService = Graphiti.getGaService();

        /* creates the polyline */
        Connection conn = peCreateService.createFreeFormConnection(getDiagram());
        Anchor sourceAnchor = Graphiti.getPeService().getChopboxAnchor((AnchorContainer) mainPe);
        Connection targetConn = context.getTargetConnection();
        Anchor targetAnchor = Graphiti.getPeService().getChopboxAnchor(targetConn.getConnectionDecorators().get(1));
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
        ParameterBindingGroup bindingGroup = (ParameterBindingGroup) getBusinessObjectForPictogramElement(context
                .getPictogramElement());
        EList<ParameterBinding> children = bindingGroup.getParameterBindings();

        /* retrieves services */
        IGaService gaService = Graphiti.getGaService();

        /* retrieves figures */
        ContainerShape containerShape = (ContainerShape) context.getPictogramElement();
        Polygon p = getGa(containerShape, Polygon.class);
        Text text = getGa(containerShape, Text.class, 0);

        /* updates the text */
        text.setValue(getStereotype());

        /* computes the text size */
        Dimension textDim = calculateSize(text, 25, 14);

        /* asks children to re-layout */
        for (ParameterBinding binding : children) {
            ContainerShape shape = (ContainerShape) getMappingProvider().getPictogramElementForBusinessObject(binding);
            LayoutContext ctx = new LayoutContext(shape);
            ctx.putProperty(AVOID_PARENT_LAYOUT, Boolean.TRUE);
            getFeatureProvider().layoutIfPossible(ctx);
        }

        /* retrieves sizes of children */
        List<Dimension> childDims = calculateSizes(children);

        /* sets locations and dimensions */
        int polygonWidth = Dimensions.maxWidth(Iterables.concat(childDims, ImmutableList.of(textDim))) + PADDING * 2 + CORNER_OFFSET
                * 2;
        int polygonHeight = Dimensions.sumHeight(Iterables.concat(childDims, ImmutableList.of(textDim))) + PADDING * 2
                * (children.size() + 1);
        polygonHeight = Math.max(polygonHeight, MIN_HEIGHT);
        gaService.setLocationAndSize(text, PADDING + CORNER_OFFSET, PADDING, polygonWidth - PADDING * 2 - CORNER_OFFSET * 2,
                textDim.height);
        int childHeight = textDim.height + PADDING * 2;
        for (int i = 0; i < children.size(); i++) {
            EObject child = children.get(i);
            Dimension childDim = childDims.get(i);
            ContainerShape shape = (ContainerShape) getMappingProvider().getPictogramElementForBusinessObject(child);
            GraphicsAlgorithm ga = shape.getGraphicsAlgorithm();
            gaService.setLocationAndSize(ga, PADDING + CORNER_OFFSET, childHeight, polygonWidth - PADDING * 2 - CORNER_OFFSET * 2,
                    childDim.height);
            childHeight += childDim.height + PADDING * 2;
        }
        p.getPoints().clear();
        int[] xy = new int[] { 0, polygonHeight, CORNER_OFFSET, 0, polygonWidth, 0, polygonWidth - CORNER_OFFSET, polygonHeight };
        p.getPoints().addAll(gaService.createPointList(xy));
        gaService.setSize(p, polygonWidth, polygonHeight);
        return true;
    }

    @Override
    public boolean canResizeShape(IResizeShapeContext context) {
        return false;
    }

}
