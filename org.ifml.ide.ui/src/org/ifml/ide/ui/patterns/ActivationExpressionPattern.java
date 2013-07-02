package org.ifml.ide.ui.patterns;

import org.eclipse.emf.ecore.EClass;
import org.ifml.model.core.CorePackage;

final class ActivationExpressionPattern extends ExpressionPattern {

    @Override
    public EClass getEClass() {
        return CorePackage.Literals.ACTIVATION_EXPRESSION;
    }

}
