package org.ifml.ide.ui.patterns;

import org.eclipse.emf.ecore.EClass;
import org.ifml.model.core.CorePackage;

class ConditionalExpressionPattern extends ViewComponentPartPattern {

    @Override
    public EClass getEClass() {
        return CorePackage.Literals.CONDITIONAL_EXPRESSION;
    }

}
