package org.ifml.ide.ui.patterns;

import org.eclipse.emf.ecore.EClass;
import org.ifml.model.extensions.ExtensionsPackage;

class SimpleFieldPattern extends ViewComponentPartPattern {

    @Override
    public EClass getEClass() {
        return ExtensionsPackage.Literals.SIMPLE_FIELD;
    }

}
