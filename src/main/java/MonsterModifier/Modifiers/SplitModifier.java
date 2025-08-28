package MonsterModifier.Modifiers;

import MonsterModifier.utils.Invoker;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;

import java.util.ArrayList;

import static MonsterModifier.helpers.ModHelper.makePath;

public class SplitModifier extends AbstractMonsterModifier{
    public static final String ID = makePath(SplitModifier.class.getSimpleName());
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString( ID);
    public String GetModifierName() {
        return uiStrings.TEXT[0];
    }
    @Override
    public AbstractMonsterModifier makeCopy() {
        return new SplitModifier();
    }
    @Override public String identifier(AbstractMonster monster) {
        return ID;
    }
    @Override public boolean shouldApply(AbstractMonster monster) {
        return !monster.hasPower("Weak");
    }
    public void onModifierAdded(AbstractMonster monster) {
        monster.addPower(new WeakPower(monster, 2, false));
    }
    public void addTips(AbstractMonster  monster){
        ArrayList<PowerTip> tips = Invoker.getField(monster, "tips");
        if (tips != null) {
            tips.add(new PowerTip(uiStrings.TEXT[0], uiStrings.TEXT[1]));
        }
    }
    @Override
    public String ModifyName(AbstractMonster monster) {
        return String.format("%s %s", GetModifierName(),monster.name);
    }

}
