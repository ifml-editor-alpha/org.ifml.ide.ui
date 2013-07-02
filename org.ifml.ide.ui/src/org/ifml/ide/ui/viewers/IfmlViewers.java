package org.ifml.ide.ui.viewers;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.graphics.Image;
import org.ifml.base.WordFormat;
import org.ifml.eclipse.ui.viewers.BasicLabelProvider;
import org.ifml.ide.ui.graphics.IfmlImage;
import org.ifml.model.core.ActivationExpression;
import org.ifml.model.core.CorePackage;
import org.ifml.model.extensions.ExtensionsPackage;
import org.ifml.model.uml.Element;
import org.ifml.model.uml.NamedElement;
import org.ifml.model.uml.UmlPackage;

import com.google.common.base.Objects;
import com.google.common.base.Strings;

/**
 * Provides viewer items for IFML elements.
 */
public final class IfmlViewers {

    /** The shared label provider. */
    public static final ILabelProvider LABEL_PROVIDER = new LabelProvider();

    private IfmlViewers() {
    }

    /**
     * Returns the image provider for a specific IFML element.
     * 
     * @param eClass
     *            the element class.
     * @return the image provider.
     */
    public static IfmlImage getImageProvider(EClass eClass) {
        if (eClass.getEPackage() == CorePackage.eINSTANCE) {
            switch (eClass.getClassifierID()) {
            case CorePackage.ACTION:
                return IfmlImage.ACTION;
            case CorePackage.ACTION_EVENT:
                return IfmlImage.ACTION_EVENT;
            case CorePackage.ACTIVATION_EXPRESSION:
                return IfmlImage.ACTIVATION_EXPRESSION;
            case CorePackage.CONDITIONAL_EXPRESSION:
                return IfmlImage.CONDITIONAL_EXPRESSION;
            case CorePackage.DATA_BINDING:
                return IfmlImage.DATA_BINDING;
            case CorePackage.DATA_FLOW:
                return IfmlImage.DATA_FLOW;
            case CorePackage.DYNAMIC_BEHAVIOR:
                return IfmlImage.DYNAMIC_BEHAVIOR;
            case CorePackage.EXPRESSION:
                return IfmlImage.EXPRESSION;
            case CorePackage.INTERACTION_FLOW_MODEL:
                return IfmlImage.INTERACTION_FLOW_MODEL;
            case CorePackage.INTERACTION_FLOW_EXPRESSION:
                return IfmlImage.INTERACTION_FLOW_EXPRESSION;
            case CorePackage.NAVIGATION_FLOW:
                return IfmlImage.NAVIGATION_FLOW;
            case CorePackage.PARAMETER:
                return IfmlImage.PARAMETER;
            case CorePackage.PARAMETER_BINDING:
                return IfmlImage.PARAMETER_BINDING;
            case CorePackage.PARAMETER_BINDING_GROUP:
                return IfmlImage.PARAMETER_BINDING_GROUP;
            case CorePackage.SYSTEM_EVENT:
                return IfmlImage.SYSTEM_EVENT;
            case CorePackage.VIEW_COMPONENT:
                return IfmlImage.VIEW_COMPONENT;
            case CorePackage.VIEW_COMPONENT_PART:
                return IfmlImage.VIEW_COMPONENT_PART;
            case CorePackage.VIEW_CONTAINER:
                return IfmlImage.VIEW_CONTAINER;
            case CorePackage.VIEW_ELEMENT_EVENT:
                return IfmlImage.VIEW_ELEMENT_EVENT;
            case CorePackage.VIEW_POINT:
                return IfmlImage.VIEWPOINT;
            }
        } else if (eClass.getEPackage() == ExtensionsPackage.eINSTANCE) {
            switch (eClass.getClassifierID()) {
            case ExtensionsPackage.DETAILS:
                return IfmlImage.DETAILS;
            case ExtensionsPackage.FORM:
                return IfmlImage.FORM;
            case ExtensionsPackage.LIST:
                return IfmlImage.LIST;
            case ExtensionsPackage.SELECT_EVENT:
                return IfmlImage.SELECT_EVENT;
            case ExtensionsPackage.SELECTION_FIELD:
                return IfmlImage.SELECTION_FIELD;
            case ExtensionsPackage.SELECTOR:
                return IfmlImage.SELECTOR;
            case ExtensionsPackage.SIMPLE_FIELD:
                return IfmlImage.SIMPLE_FIELD;
            case ExtensionsPackage.SUBMIT_EVENT:
                return IfmlImage.SUBMIT_EVENT;
            case ExtensionsPackage.WINDOW:
                return IfmlImage.WINDOW;
            }
        } else if (eClass.getEPackage() == UmlPackage.eINSTANCE) {
            switch (eClass.getClassifierID()) {
            case UmlPackage.ANNOTATION:
                return IfmlImage.ANNOTATION;
            case UmlPackage.CLASSIFIER:
                return IfmlImage.CLASSIFIER;
            case UmlPackage.STRUCTURAL_FEATURE:
                return IfmlImage.STRUCTURAL_FEATURE;
            }
        }
        return null;
    }

    private static final class LabelProvider extends BasicLabelProvider {

        private LabelProvider() {
        }

        @Override
        public String getText(Object obj) {
            if (obj instanceof EClass) {
                return WordFormat.UPPER_CAMEL.to(WordFormat.CAPITALIZED_WORDS, ((EClass) obj).getName());
            }
            if (obj instanceof EStructuralFeature) {
                return WordFormat.LOWER_CAMEL.to(WordFormat.CAPITALIZED_WORDS, ((EStructuralFeature) obj).getName());
            }
            if (obj instanceof NamedElement) {
                NamedElement namedElem = (NamedElement) obj;
                String text = Strings.nullToEmpty((namedElem.getName()));
                if (!"".equals(text)) {
                    return text;
                }
            }
            if (obj instanceof ActivationExpression) {
                ActivationExpression ann = (ActivationExpression) obj;
                return Objects.firstNonNull(Strings.emptyToNull(ann.getBody()), "Activation Expression");
            }
            if (obj instanceof Element) {
                Element elem = (Element) obj;
                return getText(elem.eClass());
            }
            if (obj instanceof EObject) {
                EObject eObj = (EObject) obj;
                return eObj.eClass().getName();
            }
            return super.getText(obj);
        }

        @Override
        public Image getImage(Object element) {
            if (element instanceof EObject) {
                IfmlImage ifmlImage = getImageProvider(((EObject) element).eClass());
                if (ifmlImage != null) {
                    return ifmlImage.get();
                }
            }
            return super.getImage(element);
        }

    }

}
