package sciSTS.patchs;

import com.evacipated.cardcrawl.modthespire.Loader;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.rooms.CampfireUI;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;
import sciSTS.ui.PayOption;
import sciSTS.utils.Invoker;

import java.util.ArrayList;

public class CampfirePacth {
    @SpirePatch(clz = CampfireUI.class, method = "initializeButtons")
    public static class CampfireUIPatchStuff {
        @SpirePostfixPatch
        public static void patch(CampfireUI __instance) {
            ArrayList<AbstractCampfireOption> buttons = Invoker.getField(__instance, "buttons");
            if (buttons == null) {
                return;
            }
           buttons.add(new PayOption(true));


        }
    }
}
