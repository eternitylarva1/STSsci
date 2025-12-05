package sciSTS.patchs;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

/**
 * 怪物死亡补丁 - 目前为空实现
 * 为AbstractMonster的die方法添加前置补丁
 */
@SpirePatch(
        clz = AbstractMonster.class,
        method = "die",
        paramtypez = {boolean.class}
)
public class DiePatch {

    @SpirePrefixPatch
    public static SpireReturn Prefix(AbstractMonster __instance, boolean trigger) {
        // 当前为空实现，预留扩展接口
        return SpireReturn.Continue();
    }
}
