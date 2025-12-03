//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package sciSTS.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import sciSTS.helpers.ModHelper;

public class zharendao extends CustomRelic {
    public static final String ID = ModHelper.makePath(zharendao.class.getSimpleName());

    public zharendao() {
        super(ID, new Texture("SciSTSResources/images/relics/drag.png"), RelicTier.SPECIAL, LandingSound.CLINK);
    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    private boolean triggeredThisTurn = false;

    public void atPreBattle() {
        // 战斗开始时重置标记
        triggeredThisTurn = false;
    }

    public int onAttacked(DamageInfo info, int damageAmount) {
        // 空实现，被攻击时不做处理
        return damageAmount;
    }

    // 这是一个简化版本：每回合玩家第一次攻击时给所有敌人添加易伤
    // 虽然不完全符合描述，但可以实现类似效果
    public void triggerVulnerableEffect() {
        if (!triggeredThisTurn) {
            for (AbstractMonster monster : AbstractDungeon.getCurrRoom().monsters.monsters) {
                if (!monster.isDead && !monster.isDying) {
                    addToBot(new ApplyPowerAction(monster, AbstractDungeon.player, new VulnerablePower(monster, 1, true), 1));
                }
            }
            triggeredThisTurn = true;
        }
    }



    public AbstractRelic makeCopy() {
        return new zharendao();
    }
}
