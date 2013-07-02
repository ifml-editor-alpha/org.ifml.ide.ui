package org.ifml.ide.ui.diagrams;

import javax.annotation.Nullable;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.graphiti.dt.AbstractDiagramTypeProvider;
import org.eclipse.graphiti.mm.pictograms.PictogramLink;
import org.eclipse.graphiti.mm.pictograms.PictogramsFactory;
import org.eclipse.ui.part.EditorPart;
import org.ifml.eclipse.emf.ecore.EmfPredicates;
import org.ifml.model.IfmlModels;
import org.ifml.model.core.CorePackage;
import org.ifml.model.core.IFMLModel;

import com.google.common.base.Optional;
import com.google.common.collect.Iterables;

abstract class AbstractIfmlDiagramTypeProvider extends AbstractDiagramTypeProvider {

    @Override
    public final void postInit() {
        createModelIfNeeded(getSubModelType(), getSubModelFeature());
    }

    protected abstract EClass getSubModelType();

    protected abstract EStructuralFeature getSubModelFeature();

    private void createModelIfNeeded(EClass subModelType, EStructuralFeature subModelFeature) {
        Optional<IFMLModel> optionalIfmlModel = IfmlModels.getIfmlModel(getDiagram().eResource());
        if (!optionalIfmlModel.isPresent()) {

            /* creates the IFML model and the sub-model */
            createModel(subModelType, subModelFeature, null);
        } else {

            /* creates the sub-model only */
            IFMLModel ifmlModel = optionalIfmlModel.get();
            Optional<EObject> optionalSubModel = Iterables.tryFind(ifmlModel.eContents(), EmfPredicates.isInstance(subModelType));
            if (!optionalSubModel.isPresent()) {
                createModel(subModelType, subModelFeature, ifmlModel);
            }
        }
    }

    private void createModel(final EClass modelType, final EStructuralFeature feature, final @Nullable IFMLModel existingIfmlModel) {
        TransactionalEditingDomain editingDomain = getDiagramEditor().getEditingDomain();
        CommandStack commandStack = editingDomain.getCommandStack();
        commandStack.execute(new RecordingCommand(editingDomain) {

            @Override
            protected void doExecute() {
                IFMLModel ifmlModel = existingIfmlModel;
                if (ifmlModel == null) {

                    /* creates the IFML model */
                    ifmlModel = (IFMLModel) EcoreUtil.create(CorePackage.Literals.IFML_MODEL);

                    /* adds the IFML model as a sibling of the diagram */
                    getDiagram().eResource().getContents().add(ifmlModel);
                }

                /* creates the sub-model */
                EObject subModel = EcoreUtil.create(modelType);
                ifmlModel.eSet(feature, subModel);

                /* links the sub-model to the diagram */
                PictogramLink link = PictogramsFactory.eINSTANCE.createPictogramLink();
                link.getBusinessObjects().add(subModel);
                getDiagram().setLink(link);
            }
        });

        /* programmatically saves the diagram editor */
        ((EditorPart) getDiagramEditor()).doSave(new NullProgressMonitor());
    }

}
