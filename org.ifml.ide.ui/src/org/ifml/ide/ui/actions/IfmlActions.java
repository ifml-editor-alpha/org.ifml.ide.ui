package org.ifml.ide.ui.actions;

import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.ifml.eclipse.graphiti.actions.AddChildAction;
import org.ifml.eclipse.ui.actions.ContextAction;
import org.ifml.ide.ui.viewers.IfmlViewers;
import org.ifml.model.core.CorePackage;
import org.ifml.model.uml.Element;

import com.google.common.collect.Lists;

/**
 * Provides utility methods for IFML actions.
 */
public final class IfmlActions {

    /**
     * Creates the list of all actions provided by the context menu in the content outline page.
     * 
     * @param domain
     *            the editing domain.
     * @return the list of actions.
     */
    public static List<ContextAction<Element>> createContentOutlineActions(TransactionalEditingDomain domain) {
        List<ContextAction<Element>> actions = Lists.newArrayList();
        actions.add(createAddChildAction(CorePackage.Literals.INTERACTION_FLOW_ELEMENT, CorePackage.Literals.PARAMETER,
                CorePackage.Literals.INTERACTION_FLOW_ELEMENT__PARAMETERS, domain));
        actions.add(createAddChildAction(CorePackage.Literals.EVENT, CorePackage.Literals.INTERACTION_FLOW_EXPRESSION,
                CorePackage.Literals.EVENT__INTERACTION_FLOW_EXPRESSION, domain));
        actions.add(createAddChildAction(CorePackage.Literals.SYSTEM_EVENT, CorePackage.Literals.EXPRESSION,
                CorePackage.Literals.SYSTEM_EVENT__TRIGGERING_EXPRESSIONS, domain));
        actions.add(createAddChildAction(CorePackage.Literals.ACTION, CorePackage.Literals.DYNAMIC_BEHAVIOR,
                CorePackage.Literals.ACTION__DYNAMIC_BEHAVIOR, domain));
        return actions;
    }

    private static ContextAction<Element> createAddChildAction(EClass parentClass, EClass childClass, EStructuralFeature feature,
            TransactionalEditingDomain domain) {
        AddChildAction<Element> action = new AddChildAction<Element>(parentClass, childClass, feature, domain);
        action.setImageDescriptor(IfmlViewers.getImageProvider(childClass).getDescriptor());
        action.setText(String.format("Add %s", childClass.getName()));
        return action;
    }
}
