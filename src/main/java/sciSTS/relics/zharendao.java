package sciSTS.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import sciSTS.helpers.ModHelper;
import sciSTS.powers.ZhaRenDaoPower;

public class zharendao extends CustomRelic {
    public static final String ID = ModHelper.makePath(zharendao.class.getSimpleName());

    public zharendao() {
        super(ID, new Texture("SciSTSResources/images/relics/drag.png"), RelicTier.SPECIAL, LandingSound.CLINK);
    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public AbstractRelic makeCopy() {
        return new zharendao();
    }

    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        if (damageAmount > 0 && info.owner == AbstractDungeon.player && info.type == DamageInfo.DamageType.NORMAL && target instanceof AbstractMonster) {
            AbstractMonster m = (AbstractMonster) target;
            // 检查怪物是否已经有扎人书的刀的power
            ZhaRenDaoPower existingPower = (ZhaRenDaoPower) m.getPower(ZhaRenDaoPower.POWER_ID);

            if (existingPower == null) {
                // 如果没有该power，则添加一个
                this.flash();
                this.addToTop(new RelicAboveCreatureAction(m, this));
                this.addToTop(new ApplyPowerAction(m, AbstractDungeon.player, new ZhaRenDaoPower(m, 1), 1));
            } else {
                // 如果已经有该power，则增加伤害加成
                existingPower.increaseDamageBonus(1);
            }
        }
    }
}
