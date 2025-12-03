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
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import sciSTS.helpers.ModHelper;

public class zharendao extends CustomRelic {
    public static final String ID = ModHelper.makePath(zharendao.class.getSimpleName());

    public zharendao() {
        super(ID, new Texture("SciSTSResources/images/relics/drag.png"), RelicTier.SPECIAL, LandingSound.CLINK);
    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public void atPreBattle() {
        // 空实现
    }

    public int onAttacked(DamageInfo info, int damageAmount) {
        // 空实现，被攻击时不做处理
        return damageAmount;
    }

    // 这个方法需要通过补丁或者事件监听来实现
    // 临时留下一个框架实现，用户可以指导正确的实现方法
    public void onPlayerAttack(AbstractMonster target) {
        // 这个效果需要通过补丁拦截玩家的攻击事件
        // 目前只是框架，实际需要通过BaseMod事件监听或SpirePatch实现
    }



    public AbstractRelic makeCopy() {
        return new zharendao();
    }
}
