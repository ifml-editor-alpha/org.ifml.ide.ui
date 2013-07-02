package org.ifml.ide.ui.patterns;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.graphiti.mm.algorithms.Polygon;
import org.eclipse.graphiti.mm.algorithms.Polyline;
import org.eclipse.graphiti.mm.algorithms.styles.LineStyle;
import org.ifml.model.core.CorePackage;

final class DataFlowPattern extends InteractionFlowPattern {

    @Override
    protected EClass getEClass() {
        return CorePackage.Literals.DATA_FLOW;
    }

    @Override
    protected void update(Polyline polyline, Polygon arrow) {
        polyline.setLineStyle(LineStyle.DASH);
    }

}
