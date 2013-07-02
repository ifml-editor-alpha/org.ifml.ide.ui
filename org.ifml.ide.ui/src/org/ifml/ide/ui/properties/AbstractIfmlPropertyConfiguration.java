package org.ifml.ide.ui.properties;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.ifml.eclipse.emf.ui.properties.EmfPropertyConfiguration;
import org.ifml.model.IfmlModels;
import org.ifml.model.core.IFMLModel;
import org.ifml.model.core.InteractionFlowModel;
import org.ifml.model.uml.Element;

import com.google.common.base.Optional;

abstract class AbstractIfmlPropertyConfiguration<T extends EObject> extends EmfPropertyConfiguration<T> {

    public AbstractIfmlPropertyConfiguration(Class<T> instanceClass, EClass eClass) {
        super(instanceClass, eClass);
    }

    protected static final InteractionFlowModel getInteractionFlowModel(Element elem) {
        Optional<IFMLModel> ifmlModel = IfmlModels.getIfmlModel(elem.eResource());
        if (ifmlModel.isPresent()) {
            return ifmlModel.get().getInteractionFlowModel();
        } else {
            return null;
        }
    }

}
