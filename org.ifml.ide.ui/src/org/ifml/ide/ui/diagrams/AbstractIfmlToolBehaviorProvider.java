package org.ifml.ide.ui.diagrams;

import java.util.List;

import javax.annotation.Nullable;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.features.context.ICustomContext;
import org.eclipse.graphiti.features.context.IPictogramElementContext;
import org.eclipse.graphiti.features.custom.ICustomFeature;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.tb.ContextMenuEntry;
import org.eclipse.graphiti.tb.DefaultToolBehaviorProvider;
import org.eclipse.graphiti.tb.IContextButtonPadData;
import org.eclipse.graphiti.tb.IContextMenuEntry;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;

import com.google.common.collect.Lists;

abstract class AbstractIfmlToolBehaviorProvider extends DefaultToolBehaviorProvider {

    private final EClass rootModelType;

    public AbstractIfmlToolBehaviorProvider(IDiagramTypeProvider diagramTypeProvider, EClass rootModelType) {
        super(diagramTypeProvider);
        this.rootModelType = rootModelType;
    }

    @Override
    public Object getAdapter(Class<?> type) {
        if (IContentOutlinePage.class == type) {
            return new IfmlContentOutlinePage(getDiagramTypeProvider(), rootModelType);
        }
        return null;
    }

    @Override
    public GraphicsAlgorithm getSelectionBorder(PictogramElement pe) {
        GraphicsAlgorithm ga = pe.getGraphicsAlgorithm();

        /* searches the first visible graphics algorithm descendant */
        GraphicsAlgorithm selectionBorder = getSelectionBorder(ga);
        if (selectionBorder != null) {
            return selectionBorder;
        } else {
            return ga;
        }
    }

    private GraphicsAlgorithm getSelectionBorder(@Nullable GraphicsAlgorithm ga) {
        if (ga != null) {
            if (ga.getLineVisible() || ga.getFilled()) {
                return ga;
            } else {
                for (GraphicsAlgorithm subGa : ga.getGraphicsAlgorithmChildren()) {
                    GraphicsAlgorithm subGaSelectionBorder = getSelectionBorder(subGa);
                    if (subGaSelectionBorder != null) {
                        return subGaSelectionBorder;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public GraphicsAlgorithm[] getClickArea(PictogramElement pe) {
        return new GraphicsAlgorithm[] { getSelectionBorder(pe) };
    }

    @Override
    public IContextMenuEntry[] getContextMenu(ICustomContext context) {
        List<IContextMenuEntry> entries = Lists.newArrayList();
        ICustomFeature[] customFeatures = getFeatureProvider().getCustomFeatures(context);
        for (ICustomFeature customFeature : customFeatures) {
            if (customFeature.isAvailable(context)) {
                ContextMenuEntry entry = new ContextMenuEntry(customFeature, context);
                entries.add(entry);
            }
        }
        return entries.toArray(new IContextMenuEntry[0]);
    }

    @Override
    public GraphicsAlgorithm getChopboxAnchorArea(PictogramElement pe) {
        return super.getChopboxAnchorArea(pe);
    }

    @Override
    public IContextButtonPadData getContextButtonPad(IPictogramElementContext context) {
        IContextButtonPadData data = super.getContextButtonPad(context);
        data.getGenericContextButtons().clear();
        return data;
    }

}
