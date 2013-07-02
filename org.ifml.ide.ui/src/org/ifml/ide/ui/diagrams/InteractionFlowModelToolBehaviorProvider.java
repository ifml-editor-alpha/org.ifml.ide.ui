package org.ifml.ide.ui.diagrams;

import java.util.Set;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.features.ICreateConnectionFeature;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.palette.IPaletteCompartmentEntry;
import org.eclipse.graphiti.palette.impl.ConnectionCreationToolEntry;
import org.eclipse.graphiti.palette.impl.ObjectCreationToolEntry;
import org.eclipse.graphiti.palette.impl.PaletteCompartmentEntry;
import org.eclipse.graphiti.pattern.CreateFeatureForPattern;
import org.ifml.ide.ui.patterns.InteractionFlowModelPatternSet;
import org.ifml.model.core.CorePackage;
import org.ifml.model.extensions.ExtensionsPackage;
import org.ifml.model.uml.UmlPackage;

import com.google.common.collect.Sets;

final class InteractionFlowModelToolBehaviorProvider extends AbstractIfmlToolBehaviorProvider {

    public InteractionFlowModelToolBehaviorProvider(IDiagramTypeProvider diagramTypeProvider, EClass rootModelType) {
        super(diagramTypeProvider, rootModelType);
    }

    @Override
    public IPaletteCompartmentEntry[] getPalette() {
        return new IPaletteCompartmentEntry[] { createConnectionCompartment(),
                createNodeCompartment(CorePackage.eINSTANCE, UmlPackage.eINSTANCE), createNodeCompartment(ExtensionsPackage.eINSTANCE) };
    }

    private IPaletteCompartmentEntry createConnectionCompartment() {
        PaletteCompartmentEntry compartment = new PaletteCompartmentEntry("Connection", null);
        for (ICreateConnectionFeature feature : getFeatureProvider().getCreateConnectionFeatures()) {
            ConnectionCreationToolEntry entry = new ConnectionCreationToolEntry(feature.getCreateName(),
                    feature.getCreateDescription(), feature.getCreateImageId(), feature.getCreateLargeImageId());
            entry.addCreateConnectionFeature(feature);
            compartment.addToolEntry(entry);
        }
        return compartment;
    }

    private IPaletteCompartmentEntry createNodeCompartment(EPackage... pckgs) {
        Set<EPackage> pckgSet = Sets.newHashSet(pckgs);
        PaletteCompartmentEntry compartment = new PaletteCompartmentEntry(pckgs[0].getName(), null);
        for (ICreateFeature feature : getFeatureProvider().getCreateFeatures()) {
            if (feature instanceof CreateFeatureForPattern) {
                EClass eClass = InteractionFlowModelPatternSet.getEClass(((CreateFeatureForPattern) feature).getPattern());
                if (pckgSet.contains(eClass.getEPackage())) {
                    ObjectCreationToolEntry entry = new ObjectCreationToolEntry(feature.getCreateName(),
                            feature.getCreateDescription(), feature.getCreateImageId(), feature.getCreateLargeImageId(), feature);
                    compartment.addToolEntry(entry);
                }
            }
        }
        return compartment;
    }

}
