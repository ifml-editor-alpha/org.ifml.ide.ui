package org.ifml.ide.ui.wizards;

import java.util.Iterator;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.model.WorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.ifml.eclipse.core.resources.Workspaces;
import org.ifml.eclipse.core.runtime.Adaptables;
import org.ifml.eclipse.ui.viewers.Selections;
import org.ifml.eclipse.ui.widgets.LabelBuilder;
import org.ifml.eclipse.ui.wizards.CommonWizardPage;

import com.google.common.base.Strings;

final class NewIfmlModelWizardPage extends CommonWizardPage {

    private TreeViewer projectsViewer;

    private Text nameText;

    private final IStructuredSelection selection;

    public NewIfmlModelWizardPage(IStructuredSelection selection) {
        setTitle("IFML Model");
        setDescription("Create an IFML model in the workspace.");
        this.selection = selection;
    }

    @Override
    protected Control doCreateControl(Composite parent) {
        Composite mainArea = new Composite(parent, SWT.NONE);
        GridDataFactory.fillDefaults().grab(true, true).applyTo(mainArea);
        GridLayoutFactory.swtDefaults().applyTo(mainArea);

        /* creates the projects section */
        Label projectLabel = new LabelBuilder().text("Select the target container:").build(mainArea);
        GridDataFactory.swtDefaults().applyTo(projectLabel);
        Tree projectsTable = new Tree(mainArea, SWT.SINGLE | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
        projectsViewer = new TreeViewer(projectsTable);
        projectsViewer.setContentProvider(new WorkbenchContentProvider());
        projectsViewer.setLabelProvider(new WorkbenchLabelProvider());
        projectsViewer.setSorter(new ViewerSorter());
        projectsViewer.setInput(Workspaces.getRoot());
        GridDataFactory.fillDefaults().grab(true, true).applyTo(projectsTable);
        for (Iterator<?> i = selection.iterator(); i.hasNext();) {
            Object obj = i.next();
            IResource res = null;
            if (obj instanceof IResource) {
                res = (IResource) obj;
            } else if (obj instanceof IAdaptable) {
                res = Adaptables.getAdapter((IAdaptable) obj, IResource.class);
            }
            if (res instanceof IContainer) {
                projectsViewer.setSelection(new StructuredSelection(res), true);
                break;
            }
        }
        projectsViewer.addSelectionChangedListener(new ISelectionChangedListener() {
            @Override
            public void selectionChanged(SelectionChangedEvent event) {
                setPageComplete(validatePage());
            }
        });

        /* creates the name section */
        Composite nameArea = new Composite(mainArea, SWT.NONE);
        GridDataFactory.swtDefaults().grab(true, false).align(SWT.FILL, SWT.CENTER).applyTo(nameArea);
        GridLayoutFactory.swtDefaults().numColumns(2).applyTo(nameArea);
        Label nameLabel = new LabelBuilder().text("Model name:").build(nameArea);
        GridDataFactory.swtDefaults().applyTo(nameLabel);
        nameText = new Text(nameArea, SWT.BORDER);
        GridDataFactory.swtDefaults().grab(true, false).align(SWT.FILL, SWT.CENTER).applyTo(nameText);
        nameText.addModifyListener(new ModifyListener() {
            @Override
            public void modifyText(ModifyEvent e) {
                setPageComplete(validatePage());
            }
        });
        setPageComplete(validatePage());
        setMessage(null);
        setErrorMessage(null);
        return mainArea;
    }

    private boolean validatePage() {
        IContainer container = getWorkspaceContainer();
        if (container == null) {
            setMessage("No container selected.");
            return false;
        }
        String name = getModelName();
        if (Strings.isNullOrEmpty(name)) {
            setErrorMessage("The IFML model name is empty.");
            return false;
        }
        IStatus nameStatus = ResourcesPlugin.getWorkspace().validateName(name, IResource.FILE);
        if (!nameStatus.isOK()) {
            setErrorMessage(nameStatus.getMessage());
            return false;
        }
        try {
            if (Workspaces.getMemberIgnoringCase(container, name) != null) {
                setErrorMessage("The selected project contains a resource with the same name but with a different case. This can cause conflicts.");
                return false;
            }
        } catch (CoreException e) {
        }
        setMessage(null);
        setErrorMessage(null);
        return true;
    }

    IContainer getWorkspaceContainer() {
        return (IContainer) Selections.getSingleElement(projectsViewer);
    }

    String getModelName() {
        String name = Strings.nullToEmpty(nameText.getText()).trim();
        if (!name.equals("") && !name.endsWith(".ifml")) {
            name = name + ".ifml";
        }
        return name;
    }

}
