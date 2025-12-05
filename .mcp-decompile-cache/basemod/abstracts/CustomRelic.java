/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.badlogic.gdx.Gdx
 *  com.badlogic.gdx.graphics.Texture
 *  com.badlogic.gdx.graphics.Texture$TextureFilter
 *  com.megacrit.cardcrawl.helpers.ImageMaster
 *  com.megacrit.cardcrawl.relics.AbstractRelic
 *  com.megacrit.cardcrawl.relics.AbstractRelic$LandingSound
 *  com.megacrit.cardcrawl.relics.AbstractRelic$RelicTier
 */
package basemod.abstracts;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public abstract class CustomRelic
extends AbstractRelic {
    public CustomRelic(String id, Texture texture, AbstractRelic.RelicTier tier, AbstractRelic.LandingSound sfx) {
        super(id, "", tier, sfx);
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        this.setTexture(texture);
    }

    public CustomRelic(String id, Texture texture, Texture outline, AbstractRelic.RelicTier tier, AbstractRelic.LandingSound sfx) {
        super(id, "", tier, sfx);
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        outline.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        this.setTextureOutline(texture, outline);
    }

    public CustomRelic(String id, String imgName, AbstractRelic.RelicTier tier, AbstractRelic.LandingSound sfx) {
        super(id, imgName, tier, sfx);
    }

    public void setTexture(Texture t) {
        this.img = t;
        this.outlineImg = t;
    }

    public void setTextureOutline(Texture t, Texture o) {
        this.img = t;
        this.outlineImg = o;
    }

    public void loadLargeImg() {
        String path;
        if (this.largeImg == null && Gdx.files.internal(path = "images/largeRelics/" + this.imgUrl).exists()) {
            this.largeImg = ImageMaster.loadImage((String)path);
        }
    }

    public AbstractRelic makeCopy() {
        try {
            return (AbstractRelic)((Object)((Object)this)).getClass().newInstance();
        }
        catch (IllegalAccessException | InstantiationException e) {
            throw new RuntimeException("BaseMod failed to auto-generate makeCopy for relic: " + this.relicId);
        }
    }
}
