package org.ifml.ide.ui.diagrams;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.palette.IPaletteCompartmentEntry;

final class ViewpointToolBehaviorProvider extends AbstractIfmlToolBehaviorProvider {

    public ViewpointToolBehaviorProvider(IDiagramTypeProvider diagramTypeProvider, EClass rootModelType) {
        super(diagramTypeProvider, rootModelType);
    }

    @Override
    public IPaletteCompartmentEntry[] getPalette() {
        return new IPaletteCompartmentEntry[] {};
    }

}
