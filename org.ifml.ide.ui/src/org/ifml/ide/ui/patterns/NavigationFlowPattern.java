package org.ifml.ide.ui.patterns;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.graphiti.mm.algorithms.Polygon;
import org.eclipse.graphiti.mm.algorithms.Polyline;
import org.ifml.model.core.CorePackage;

final class NavigationFlowPattern extends InteractionFlowPattern {

    @Override
    protected EClass getEClass() {
        return CorePackage.Literals.NAVIGATION_FLOW;
    }

    @Override
    protected void update(Polyline polyline, Polygon arrow) {
    }

}
