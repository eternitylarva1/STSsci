package sciSTS.patchs;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.relics.StoneCalendar;
import java.time.LocalDate;
import java.time.DayOfWeek;

/**
 * 石头日历补丁 - 根据实际星期几设置计数器
 */
@SpirePatch(
        clz = StoneCalendar.class,
        method = "atTurnStart"
)
public class StoneCalendarPatch {

    @SpirePrefixPatch
    public static void Prefix(StoneCalendar __instance) {
        // 当counter为0时，根据当前星期几设置计数器
        if (__instance.counter == 0) {
            LocalDate today = LocalDate.now();
            DayOfWeek dayOfWeek = today.getDayOfWeek();
            __instance.counter += dayOfWeek.getValue();
            if (__instance.counter >= 7) {
                __instance.counter = 7;
            }
        }
    }
}
