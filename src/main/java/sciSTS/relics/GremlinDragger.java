//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package sciSTS.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.CrackedCore;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import sciSTS.helpers.ModHelper;

public class GremlinDragger extends CustomRelic {
    public static final String ID = ModHelper.makePath(GremlinDragger.class.getSimpleName());

    public GremlinDragger() {
        super(ID, new Texture("SciSTSResources/images/relics/dagger.png"), RelicTier.SPECIAL, LandingSound.CLINK);
    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public void atPreBattle() {
        // 统计友军数量（通过检查意图类型，友军通常有防御意图而非攻击意图）
        int friendlyCount = 0;
        for (AbstractMonster monster : AbstractDungeon.getCurrRoom().monsters.monsters) {
            if (!monster.isDead && !monster.isDying && monster.intent != AbstractMonster.Intent.ATTACK && monster.intent != AbstractMonster.Intent.ATTACK_BUFF && monster.intent != AbstractMonster.Intent.ATTACK_DEBUFF && monster.intent != AbstractMonster.Intent.ATTACK_DEFEND) {
                friendlyCount++;
            }
        }

        // 如果友军数量>=2，给所有友军加5力量
        if (friendlyCount >= 2) {
            for (AbstractMonster monster : AbstractDungeon.getCurrRoom().monsters.monsters) {
                if (!monster.isDead && !monster.isDying && monster.intent != AbstractMonster.Intent.ATTACK && monster.intent != AbstractMonster.Intent.ATTACK_BUFF && monster.intent != AbstractMonster.Intent.ATTACK_DEBUFF && monster.intent != AbstractMonster.Intent.ATTACK_DEFEND) {
                    addToBot(new ApplyPowerAction(monster, AbstractDungeon.player, new StrengthPower(monster, 5), 5));
                }
            }
        }
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {

        return super.onAttacked(info, damageAmount);
        }



    public AbstractRelic makeCopy() {
        return new GremlinDragger();
    }
}
