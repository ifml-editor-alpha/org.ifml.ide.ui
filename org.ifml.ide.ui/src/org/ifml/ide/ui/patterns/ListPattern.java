package org.ifml.ide.ui.patterns;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.ifml.model.core.ViewComponent;
import org.ifml.model.extensions.ExtensionsPackage;

final class ListPattern extends ViewComponentPattern {

    @Override
    public EClass getEClass() {
        return ExtensionsPackage.Literals.LIST;
    }

    @Override
    protected EList<? extends EObject> getChildren(ViewComponent viewComp) {
        return viewComp.getViewComponentParts();
    }

}
