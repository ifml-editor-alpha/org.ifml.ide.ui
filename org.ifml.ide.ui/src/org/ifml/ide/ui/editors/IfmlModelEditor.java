package org.ifml.ide.ui.editors;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.graphiti.ui.editor.DiagramEditorInput;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.editor.FormEditor;
import org.ifml.eclipse.core.runtime.Adaptables;
import org.ifml.model.IfmlModels;
import org.ifml.model.core.IFMLModel;

import com.google.common.base.Optional;

/**
 * The IFML model editor.
 */
public final class IfmlModelEditor extends FormEditor /* implements IResourceChangeListener , ISelectionListener */{

    private InteractionFlowModelEditor interactionFlowModelEditor;

    private ContentModelEditor contentModelEditor;

    /** Creates a new editor. */
    public IfmlModelEditor() {
        // ResourcesPlugin.getWorkspace().addResourceChangeListener(this);
    }

    private TransactionalEditingDomain createInteractionFlowModelPage() {
        try {
            interactionFlowModelEditor = new InteractionFlowModelEditor();
            int index = addPage(interactionFlowModelEditor, createDiagramEditorInput(0));
            setPageText(index, "Interaction Flow");
            return interactionFlowModelEditor.getEditingDomain();
        } catch (PartInitException e) {
            ErrorDialog.openError(getSite().getShell(), "Error creating interaction flow model editor", null, e.getStatus());
            return null;
        }
    }

    private void createContentModelPage(TransactionalEditingDomain editingDomain) {
        try {
            contentModelEditor = new ContentModelEditor();
            contentModelEditor.setEditingDomain(editingDomain);
            int index = addPage(contentModelEditor, createDiagramEditorInput(1));
            setPageText(index, "Content Model");
        } catch (PartInitException e) {
            ErrorDialog.openError(getSite().getShell(), "Error creating content model editor", null, e.getStatus());
        }
    }

    private void createViewPointPage(TransactionalEditingDomain editingDomain, int viewPointIndex) {
        try {
            ViewPointEditor viewPointEditor = new ViewPointEditor();
            viewPointEditor.setEditingDomain(editingDomain);
            int index = addPage(viewPointEditor, createDiagramEditorInput(2 + viewPointIndex));
            setPageText(index, "View Point #" + viewPointIndex + 1);
        } catch (PartInitException e) {
            ErrorDialog.openError(getSite().getShell(), "Error creating view point editor", null, e.getStatus());
        }
    }

    private IEditorInput createDiagramEditorInput(int diagramIndex) {
        IFile file = Adaptables.getAdapter(getEditorInput(), IFile.class);
        URI ifmlFileUri = getFileURI(file);
        URI diagramUri = ifmlFileUri.appendFragment("/" + diagramIndex);
        return new DiagramEditorInput(diagramUri, null);
    }

    private URI getFileURI(IFile file) {
        String pathName = file.getFullPath().toString();
        URI resourceURI = URI.createPlatformResourceURI(pathName, true);
        ResourceSet resourceSet = new ResourceSetImpl();
        resourceURI = resourceSet.getURIConverter().normalize(resourceURI);
        return resourceURI;
    }

    @Override
    protected void addPages() {
        TransactionalEditingDomain editingDomain = createInteractionFlowModelPage();
        createContentModelPage(editingDomain);
        ResourceSet resourceSet = editingDomain.getResourceSet();
        Optional<IFMLModel> ifmlModel = IfmlModels.getIfmlModel(resourceSet.getResources().get(0));
        if (ifmlModel.isPresent()) {
            int viewPointCount = ifmlModel.get().getInteractionFlowModelViewPoints().size();
            for (int i = 0; i < viewPointCount; i++) {
                createViewPointPage(editingDomain, i);
            }
        }
    }

    /**
     * Returns the transactional editing domain.
     * 
     * @return the transactional editing domain.
     */
    public TransactionalEditingDomain getEditingDomain() {
        return interactionFlowModelEditor.getEditingDomain();
    }

    @Override
    public void dispose() {
        // ResourcesPlugin.getWorkspace().removeResourceChangeListener(this);
        super.dispose();
    }

    @Override
    public void doSave(IProgressMonitor monitor) {
        int pageCount = getPageCount();
        for (int i = 0; i < pageCount; i++) {
            IEditorPart editor = getEditor(i);
            editor.doSave(monitor);
        }
    }

    @Override
    public void doSaveAs() {
        IEditorPart editor = getEditor(0);
        editor.doSaveAs();
        setPageText(0, editor.getTitle());
        setInput(editor.getEditorInput());
    }

    // public void gotoMarker(IMarker marker) {
    // setActivePage(0);
    // IDE.gotoMarker(getEditor(0), marker);
    // }

    @Override
    public void init(IEditorSite site, IEditorInput editorInput) throws PartInitException {
        if (!(editorInput instanceof IFileEditorInput)) {
            throw new PartInitException("Invalid Input: Must be IFileEditorInput");
        }
        super.init(site, editorInput);
        // getSite().getWorkbenchWindow().getSelectionService().addSelectionListener(this);
    }

    @Override
    public boolean isSaveAsAllowed() {
        return true;
    }

    // @Override
    // public void resourceChanged(final IResourceChangeEvent event) {
    // if (event.getType() == IResourceChangeEvent.PRE_CLOSE) {
    // Display.getDefault().asyncExec(new Runnable() {
    // @Override
    // public void run() {
    // IWorkbenchPage[] pages = getSite().getWorkbenchWindow().getPages();
    // for (int i = 0; i < pages.length; i++) {
    // if (((FileEditorInput) interactionFlowModelEditor.getEditorInput()).getFile().getProject()
    // .equals(event.getResource())) {
    // IEditorPart editorPart = pages[i].findEditor(interactionFlowModelEditor.getEditorInput());
    // pages[i].closeEditor(editorPart, true);
    // }
    // }
    // }
    // });
    // }
    // }

    // @Override
    // public void selectionChanged(IWorkbenchPart part, ISelection selection) {
    // int pageCount = getPageCount();
    // for (int i = 0; i < pageCount; i++) {
    // IEditorPart editor = getEditor(i);
    // if (editor instanceof ISelectionListener) {
    // ((ISelectionListener) editor).selectionChanged(part, selection);
    // }
    // }
    // }

}
