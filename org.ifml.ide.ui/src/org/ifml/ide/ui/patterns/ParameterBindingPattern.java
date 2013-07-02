package org.ifml.ide.ui.patterns;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.context.ILayoutContext;
import org.eclipse.graphiti.features.context.IMoveShapeContext;
import org.eclipse.graphiti.features.context.IResizeShapeContext;
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
import org.ifml.ide.ui.graphics.IfmlColors;
import org.ifml.ide.ui.viewers.IfmlViewers;
import org.ifml.model.core.CorePackage;
import org.ifml.model.core.Parameter;
import org.ifml.model.core.ParameterBinding;

class ParameterBindingPattern extends AbstractIfmlShapePattern<ParameterBinding> {

    private static final int TEXT_PADDING = 2;

    public ParameterBindingPattern() {
        super(ParameterBinding.class);
    }

    @Override
    public EClass getEClass() {
        return CorePackage.Literals.PARAMETER_BINDING;
    }

    @Override
    protected PictogramElement addPictogramElement(IAddContext context) {

        /* retrieves services */
        IPeCreateService peCreateService = Graphiti.getPeCreateService();
        IGaService gaService = Graphiti.getGaService();

        /* creates the shape hierarchy: rectangle > text */
        ContainerShape containerShape = peCreateService.createContainerShape(context.getTargetContainer(), true);
        Rectangle rect = gaService.createRectangle(containerShape);
        gaService.setLocation(rect, context.getX(), context.getY());
        rect.setForeground(manageColor(IfmlColors.DEFAULT_SHAPE_BACKGROUND));
        rect.setBackground(manageColor(IfmlColors.DEFAULT_SHAPE_BACKGROUND));
        Text text = gaService.createDefaultText(getDiagram(), rect);
        text.setForeground(manageColor(ColorConstant.BLACK));
        text.setHorizontalAlignment(Orientation.ALIGNMENT_CENTER);
        text.setVerticalAlignment(Orientation.ALIGNMENT_CENTER);
        gaService.setLocation(text, TEXT_PADDING, TEXT_PADDING);
        peCreateService.createChopboxAnchor(containerShape);
        return containerShape;
    }

    @Override
    protected void layoutPictogramElement(PictogramElement pe) {
        super.layoutPictogramElement(pe);

        /* re-layouts the parent container */
        LayoutContext context = new LayoutContext(((ContainerShape) pe).getContainer());
        getFeatureProvider().layoutIfPossible(context);
    }

    @Override
    public boolean layout(ILayoutContext context) {
        ParameterBinding binding = (ParameterBinding) getBusinessObjectForPictogramElement(context.getPictogramElement());
        Parameter srcParam = binding.getSourceParameter();
        Parameter targetParam = binding.getTargetParameter();

        /* retrieves services */
        IGaService gaService = Graphiti.getGaService();

        /* retrieves figures */
        ContainerShape containerShape = (ContainerShape) context.getPictogramElement();
        Rectangle rect = getGa(containerShape, Rectangle.class);
        Text text = getGa(containerShape, Text.class, 0);

        /* updates the text */
        text.setValue(String.format("%s -> %s", getParameterLabel(srcParam), getParameterLabel(targetParam)));

        /* updates sizes */
        Dimension textDim = calculateSize(text, 25, 14);
        gaService.setSize(text, textDim.width, textDim.height);
        gaService.setSize(rect, textDim.width + TEXT_PADDING * 2, textDim.height + TEXT_PADDING * 2);

        /* asks the parent to re-layout */
        if (!Boolean.TRUE.equals(context.getProperty(AVOID_PARENT_LAYOUT))) {
            getFeatureProvider().layoutIfPossible(new LayoutContext(containerShape.getContainer()));
        }
        return true;
    }

    private Object getParameterLabel(Parameter param) {
        if (param == null) {
            return "??";
        }
        return IfmlViewers.LABEL_PROVIDER.getText(param);
    }

    @Override
    public boolean canResizeShape(IResizeShapeContext context) {
        return false;
    }

    @Override
    public boolean canMoveShape(IMoveShapeContext context) {
        return false;
    }

}
