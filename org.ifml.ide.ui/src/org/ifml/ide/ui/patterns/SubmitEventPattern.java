package org.ifml.ide.ui.patterns;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.graphiti.mm.algorithms.Ellipse;
import org.eclipse.graphiti.mm.algorithms.Polygon;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.ifml.ide.ui.graphics.IfmlColors;
import org.ifml.model.extensions.ExtensionsPackage;
import org.ifml.model.extensions.SubmitEvent;

final class SubmitEventPattern extends ViewElementEventPattern<SubmitEvent> {

    public SubmitEventPattern() {
        super(SubmitEvent.class);
    }

    @Override
    public EClass getEClass() {
        return ExtensionsPackage.Literals.SUBMIT_EVENT;
    }

    @Override
    protected void addInternalFigure(Ellipse ellipse) {

        /* retrieves services */
        IGaService gaService = Graphiti.getGaService();

        /* adds the arrow */
        Polygon p = gaService.createPolygon(ellipse);
        p.setForeground(manageColor(IfmlColors.DEFAULT_SHAPE_FOREGROUND));
        p.setBackground(manageColor(IfmlColors.DEFAULT_SHAPE_FOREGROUND));
        p.getPoints().clear();
        p.getPoints().addAll(gaService.createPointList(new int[] { 2, 6, 9, 6, 9, 3, 14, 8, 9, 13, 9, 10, 2, 10 }));
        gaService.setLocation(p, 0, 0);
    }

}