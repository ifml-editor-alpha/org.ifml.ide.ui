package org.ifml.ide.ui.patterns;

import org.eclipse.emf.ecore.EClass;
import org.ifml.model.extensions.ExtensionsPackage;

final class FormPattern extends ViewComponentPattern {

    @Override
    public EClass getEClass() {
        return ExtensionsPackage.Literals.FORM;
    }

    // @Override
    // protected EList<? extends EObject> getChildren(ViewComponent viewComp) {
    // return ((Form) viewComp).getFields();
    // }

}
