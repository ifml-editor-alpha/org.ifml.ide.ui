package org.ifml.ide.ui.patterns;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.graphiti.mm.algorithms.Ellipse;
import org.eclipse.graphiti.mm.algorithms.Polygon;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.ifml.ide.ui.graphics.IfmlColors;
import org.ifml.model.extensions.ExtensionsPackage;
import org.ifml.model.extensions.SelectEvent;

final class SelectEventPattern extends ViewElementEventPattern<SelectEvent> {

    public SelectEventPattern() {
        super(SelectEvent.class);
    }

    @Override
    public EClass getEClass() {
        return ExtensionsPackage.Literals.SELECT_EVENT;
    }

    @Override
    protected void addInternalFigure(Ellipse ellipse) {

        /* retrieves services */
        IGaService gaService = Graphiti.getGaService();

        /* adds the v-check */
        Polygon p = gaService.createPolygon(ellipse);
        p.setForeground(manageColor(IfmlColors.DEFAULT_SHAPE_FOREGROUND));
        p.setBackground(manageColor(IfmlColors.DEFAULT_SHAPE_FOREGROUND));
        p.getPoints().clear();
        p.getPoints().addAll(gaService.createPointList(new int[] { 2, 6, 6, 9, 13, 4, 6, 12 }));
        gaService.setLocation(p, 0, 0);
    }
}