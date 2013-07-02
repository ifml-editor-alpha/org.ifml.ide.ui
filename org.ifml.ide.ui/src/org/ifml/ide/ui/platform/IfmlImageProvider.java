package org.ifml.ide.ui.platform;

import org.eclipse.graphiti.platform.AbstractExtension;
import org.eclipse.graphiti.ui.platform.IImageProvider;
import org.ifml.eclipse.ui.graphics.ImageProviders;
import org.ifml.ide.ui.IfmlIdeUi;
import org.ifml.ide.ui.graphics.IfmlImage;

/**
 * A Graphiti image provider for the images provided by {@link IfmlImage}.
 */
public final class IfmlImageProvider extends AbstractExtension implements IImageProvider {

    private static final String PREFIX = IfmlImage.class.getCanonicalName() + "/";

    @Override
    public void setPluginId(String pluginId) {
        // ignored
    }

    @Override
    public String getPluginId() {
        return IfmlIdeUi.ID;
    }

    @Override
    public String getImageFilePath(String imageId) {
        if (imageId.startsWith(PREFIX)) {
            try {
                IfmlImage img = IfmlImage.valueOf(imageId.substring(PREFIX.length()));
                return ImageProviders.getPath(img);
            } catch (IllegalArgumentException e) {
            }
        }
        return null;
    }

}
