package sciSTS.utils;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.esotericsoftware.spine.Slot;
import com.esotericsoftware.spine.attachments.Attachment;
import com.esotericsoftware.spine.attachments.MeshAttachment;
import com.esotericsoftware.spine.attachments.RegionAttachment;

public class SpineRegionExtractor {

    /**
     * 从指定的 Slot 中获取 TextureRegion
     * @param slot 要处理的 Slot 对象
     * @param atlas 纹理图集
     * @return 对应的 TextureRegion，如果不存在则返回 null
     */
    public static TextureRegion getTextureRegionFromSlot(Slot slot, TextureAtlas atlas) {
        if (slot == null || atlas == null) {
            return null;
        }
        Attachment attachment = slot.getAttachment();
        String regionName = null;
        if (attachment instanceof RegionAttachment) {
            regionName = ((RegionAttachment) attachment).getPath();
        } else if (attachment instanceof MeshAttachment) {
            regionName = ((MeshAttachment) attachment).getPath();
        }
        System.out.println("Attachment path: " + regionName);
        if (attachment != null) {
            if (attachment instanceof RegionAttachment) {
                RegionAttachment regionAttachment = (RegionAttachment) attachment;
               regionName = regionAttachment.getPath();
                TextureRegion region = atlas.findRegion(regionName);

                if (region != null) {
                    return region;
                }
            } else if (attachment instanceof MeshAttachment) {
                MeshAttachment meshAttachment = (MeshAttachment) attachment;
              regionName = meshAttachment.getPath();
                TextureRegion region = atlas.findRegion(regionName);
                if (region != null) {
                    return region;
                }
            }
        }
        return null;
    }

    /**
     * 从指定的 Slot 中获取 Texture
     * @param slot 要处理的 Slot 对象
     * @param atlas 纹理图集
     * @return 对应的 Texture，如果不存在则返回 null
     */
    public static Texture getTextureFromSlot(Slot slot, TextureAtlas atlas) {
        TextureRegion region = getTextureRegionFromSlot(slot, atlas);
        if (region != null) {
            return region.getTexture();
        }
        return null;
    }

    /**
     * 将纹理缩放到长度小于 64 像素
     * @param texture 要缩放的纹理
     * @return 缩放后的纹理
     */
    public static Texture scaleTextureToUnder64(Texture texture) {
        if (texture == null) {
            return null;
        }
        int width = texture.getWidth();
        int height = texture.getHeight();
        int maxLength = Math.max(width, height);

        if (maxLength < 64) {
            return texture; // 如果纹理已经小于 64 像素，直接返回
        }

        float scaleFactor = 64f / maxLength;
        int newWidth = (int) (width * scaleFactor);
        int newHeight = (int) (height * scaleFactor);

        // 获取纹理数据并确保其已准备好
        texture.getTextureData().prepare();
        com.badlogic.gdx.graphics.Pixmap srcPixmap = texture.getTextureData().consumePixmap();

        com.badlogic.gdx.graphics.Pixmap scaledPixmap = new com.badlogic.gdx.graphics.Pixmap(newWidth, newHeight, com.badlogic.gdx.graphics.Pixmap.Format.RGBA8888);
        scaledPixmap.drawPixmap(srcPixmap, 0, 0, width, height, 0, 0, newWidth, newHeight);

        // 如果 consumePixmap 返回的是一个新的 Pixmap 实例，则释放它
        if (texture.getTextureData().getType() != TextureData.TextureDataType.Custom) {
            srcPixmap.dispose();
        }

        // 创建一个新的纹理
        Texture scaledTexture = new Texture(scaledPixmap);
        scaledPixmap.dispose();

        return scaledTexture;
    }
}