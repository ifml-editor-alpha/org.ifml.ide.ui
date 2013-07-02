package org.ifml.ide.ui.properties;

import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.properties.tabbed.ITabDescriptor;
import org.eclipse.ui.views.properties.tabbed.ITabDescriptorProvider;
import org.ifml.eclipse.emf.ui.properties.AbstractEmfPropertyTabDescriptor;
import org.ifml.eclipse.emf.ui.properties.EmfPropertySection;
import org.ifml.eclipse.graphiti.properties.GfEmfPropertySection;
import org.ifml.model.core.CorePackage;
import org.ifml.model.extensions.ExtensionsPackage;
import org.ifml.model.uml.UmlPackage;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

/**
 * A tab descriptor provider for IFML properties.
 */
public final class IfmlTabDescriptorProvider implements ITabDescriptorProvider {

    private ITabDescriptor[] tabDescriptors;

    @Override
    public ITabDescriptor[] getTabDescriptors(IWorkbenchPart part, ISelection selection) {
        if (tabDescriptors == null) {
            tabDescriptors = new ITabDescriptor[] { new MainTabDescriptor() };
        }
        return tabDescriptors;
    }

    private static final class MainTabDescriptor extends AbstractEmfPropertyTabDescriptor {

        public MainTabDescriptor() {
            super("IFML", "Main");
        }

        @Override
        protected List<EmfPropertySection<?>> createPropertySections() {
            List<EmfPropertySection<?>> sections = Lists.newArrayList();
            addPropertySections(UmlPackage.eINSTANCE, sections);
            addPropertySections(CorePackage.eINSTANCE, sections);
            addPropertySections(ExtensionsPackage.eINSTANCE, sections);
            return sections;
        }

        private void addPropertySections(EPackage ePackage, List<EmfPropertySection<?>> sections) {
            for (EClass eClass : Iterables.filter(ePackage.getEClassifiers(), EClass.class)) {
                sections.add(createPropertySection(eClass));
            }
        }

        @SuppressWarnings("unchecked")
        private <T extends EObject> EmfPropertySection<?> createPropertySection(EClass eClass) {
            return new GfEmfPropertySection<T>((Class<T>) eClass.getInstanceClass(), eClass, IfmlPropertyConfigurations.CONFIG_SET);
        }

    }

}
