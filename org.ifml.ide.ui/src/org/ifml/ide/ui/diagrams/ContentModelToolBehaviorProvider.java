package org.ifml.ide.ui.diagrams;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.palette.IPaletteCompartmentEntry;
import org.eclipse.graphiti.palette.impl.ObjectCreationToolEntry;
import org.eclipse.graphiti.palette.impl.PaletteCompartmentEntry;
import org.eclipse.graphiti.pattern.CreateFeatureForPattern;
import org.ifml.model.uml.UmlPackage;

final class ContentModelToolBehaviorProvider extends AbstractIfmlToolBehaviorProvider {

    public ContentModelToolBehaviorProvider(IDiagramTypeProvider diagramTypeProvider, EClass rootModelType) {
        super(diagramTypeProvider, rootModelType);
    }

    @Override
    public IPaletteCompartmentEntry[] getPalette() {
        return new IPaletteCompartmentEntry[] { createNodeCompartment(UmlPackage.eINSTANCE) };
    }

    private IPaletteCompartmentEntry createNodeCompartment(EPackage pckg) {

        /* adds UML nodes */
        PaletteCompartmentEntry compartment = new PaletteCompartmentEntry(pckg.getName(), null);
        for (ICreateFeature feature : getFeatureProvider().getCreateFeatures()) {
            if (feature instanceof CreateFeatureForPattern) {
                ObjectCreationToolEntry entry = new ObjectCreationToolEntry(feature.getCreateName(), feature.getCreateDescription(),
                        feature.getCreateImageId(), feature.getCreateLargeImageId(), feature);
                compartment.addToolEntry(entry);
            }
        }
        return compartment;
    }

}
