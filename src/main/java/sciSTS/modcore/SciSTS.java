package sciSTS.modcore;



import basemod.*;
import basemod.devcommands.relic.Relic;
import basemod.helpers.RelicType;
import basemod.interfaces.*;
import com.badlogic.gdx.Gdx;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.blue.SelfRepair;
import com.megacrit.cardcrawl.cards.purple.ForeignInfluence;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.Keyword;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.relics.*;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import sciSTS.relics.FullCage;
import sciSTS.relics.UncrackedCore;
import sciSTS.screens.EventScreen;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;


import static com.megacrit.cardcrawl.core.Settings.language;


@SpireInitializer
public class SciSTS implements EditCardsSubscriber,PostUpdateSubscriber,PostInitializeSubscriber,EditKeywordsSubscriber,OnStartBattleSubscriber, PostBattleSubscriber,StartActSubscriber , EditStringsSubscriber, EditRelicsSubscriber,OnPlayerTurnStartSubscriber { // 实现接口
    public SciSTS() {
        BaseMod.subscribe(this); // 告诉basemod你要订阅事件
    }
    public static int turn=0;
    public static final String MyModID = "SciSTS";
    ModPanel settingsPanel = new ModPanel();
    public static SpireConfig config;
    public static void initialize() throws IOException {

        new SciSTS();

        config=new SpireConfig("sciSTS", "sciSTS");
        config.load();

    }
public static boolean shouldDraw() {
        if (AbstractDungeon.player != null&&AbstractDungeon.player.hasRelic(PenNib.ID)&&AbstractDungeon.player.hasRelic(InkBottle.ID)){
            return true;
        }
        return false;

}
    // 当basemod开始注册mod卡牌时，便会调用这个函数

    @Override
    public void receiveStartAct() {

    }

    @Override
    public void receiveEditRelics() {

        BaseMod.addRelic(new sciSTS.relics.EmptyCage(), RelicType.SHARED);
        BaseMod.addRelic(new FullCage(), RelicType.SHARED);
        BaseMod.addRelic(new UncrackedCore(), RelicType.SHARED);
        BaseMod.addRelic(new  sciSTS.relics.Pear(), RelicType.SHARED);
        BaseMod.addRelic(new  sciSTS.relics.Strawberry(), RelicType.SHARED);
        BaseMod.addRelic(new sciSTS.relics.Mango(), RelicType.SHARED);

    }

    @Override
    public void receiveEditStrings() {
        String lang;
        if (language == Settings.GameLanguage.ZHS) {
            lang = "ZHS"; // 如果语言设置为简体中文，则加载ZHS文件夹的资源
        } else {
            lang = "ENG"; // 如果没有相应语言的版本，默认加载英语
        }
   BaseMod.loadCustomStringsFile(RelicStrings.class, "SciSTSResources/localization/" + lang + "/relics.json");
        BaseMod.loadCustomStringsFile(UIStrings.class, "SciSTSResources/localization/" + lang + "/ui.json");
        BaseMod.loadCustomStringsFile(CardStrings.class, "SciSTSResources/localization/" + lang + "/cards.json");

    }
    public static float getYPos(float y) {
        return Settings.HEIGHT/(2160/y);
    }
    public static float getXPos(float x) {
        return Settings.WIDTH/(3840/x);
    }
    @Override
    public void receivePostInitialize() {
BaseMod.addCustomScreen(new EventScreen());
        BaseMod.removeRelic(RelicLibrary.getRelic(Pear.ID));
        BaseMod.removeRelic(RelicLibrary.getRelic(Strawberry.ID));
        BaseMod.removeRelic(RelicLibrary.getRelic(Mango.ID));
        BaseMod.removeRelic(RelicLibrary.getRelic(EmptyCage.ID));
    }



    @Override
    public void receiveOnBattleStart(AbstractRoom abstractRoom) {

    }

    @Override
    public void receiveEditKeywords() {
        Gson gson = new Gson();
        String lang = "ENG";
        if (language == Settings.GameLanguage.ZHS) {
            lang = "ZHS";
        }

        String json = Gdx.files.internal("SciSTSResources/localization/" + lang + "/keywords.json")
                .readString(String.valueOf(StandardCharsets.UTF_8));
        Keyword[] keywords = gson.fromJson(json, Keyword[].class);
        /*
        if (keywords != null) {
            for (Keyword keyword : keywords) {
                // 这个id要全小写
                BaseMod.addKeyword("muban", keyword.NAMES[0], keyword.NAMES, keyword.DESCRIPTION);
            }
        }*/
    }

    @Override
    public void receiveOnPlayerTurnStart() {
        turn++;
if (AbstractDungeon.player != null&&AbstractDungeon.player.hasRelic(sciSTS.relics.Pear.ID)){
    if (turn==1){
        AbstractDungeon.player.getRelic(sciSTS.relics.Pear.ID).flash();
        AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(1));
    }
}if (AbstractDungeon.player != null&&AbstractDungeon.player.hasRelic(sciSTS.relics.Strawberry.ID)){
            if (turn==2){
                AbstractDungeon.player.getRelic(sciSTS.relics.Strawberry.ID).flash();
                AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(1));
            }
        }if (AbstractDungeon.player != null&&AbstractDungeon.player.hasRelic(sciSTS.relics.Mango.ID)){
            if (turn==3){
                AbstractDungeon.player.getRelic(sciSTS.relics.Mango.ID).flash();
                AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(1));
            }
        }


    }

    @Override
    public void receivePostBattle(AbstractRoom abstractRoom) {
turn=0;
    }
    int iamount=1000;
        @Override
    public void receivePostUpdate() {
            if ( iamount %1000==0 ){
                iamount++;
                return;
            }
if (AbstractDungeon.player != null&&AbstractDungeon.player.hasRelic(Sundial.ID)){
    AbstractRelic sundial = AbstractDungeon.player.getRelic(Sundial.ID);

    sundial.tips.removeIf(powerTip -> powerTip.header.equals(sundial.DESCRIPTIONS[2]));

       Calendar calendar = Calendar.getInstance();
       int hour = calendar.get(Calendar.HOUR_OF_DAY);
if (hour>=6&&hour<=18){
    sundial.tips.add(new PowerTip(sundial.DESCRIPTIONS[2],sundial.DESCRIPTIONS[3]+hour+sundial.DESCRIPTIONS[4]));
}else
    sundial.tips.add(new PowerTip(sundial.DESCRIPTIONS[2],sundial.DESCRIPTIONS[5]));

}
if (AbstractDungeon.player != null&&AbstractDungeon.player.hasRelic(Pocketwatch.ID)){
    AbstractRelic pocketwatch = AbstractDungeon.player.getRelic(Pocketwatch.ID);

    pocketwatch.tips.removeIf(powerTip -> powerTip.header.equals(pocketwatch.DESCRIPTIONS[1]));

       Calendar calendar = Calendar.getInstance();
       int hour = calendar.get(Calendar.HOUR_OF_DAY);
       int minute = calendar.get(Calendar.MINUTE);
       int second = calendar.get(Calendar.SECOND);
       String time = String.format("%02d:%02d:%02d", 3, minute, second);
    pocketwatch.tips.add(new PowerTip(pocketwatch.DESCRIPTIONS[1],pocketwatch.DESCRIPTIONS[2]+time));



}
if (AbstractDungeon.player != null&&AbstractDungeon.player.hasRelic(StoneCalendar.ID)){
    LocalDate today = LocalDate.now();

    // 获取星期几
    DayOfWeek dayOfWeek = today.getDayOfWeek();

    // 格式化显示日期
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日");
    String formattedDate = today.format(formatter);

    // 星期几的中文表示
    String[] chineseweekdays = {"星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日"};
    String[] englishWeekdays = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
String[] trueWeekdays = language == Settings.GameLanguage.ZHS ? chineseweekdays : englishWeekdays;
    String weekday = trueWeekdays[dayOfWeek.getValue() - 1];

    AbstractRelic stoneCalendar = AbstractDungeon.player.getRelic(StoneCalendar.ID);
    stoneCalendar.tips.removeIf(powerTip -> powerTip.header.equals(stoneCalendar.DESCRIPTIONS[3]));
    stoneCalendar.tips.add(new PowerTip(stoneCalendar.DESCRIPTIONS[3],stoneCalendar.DESCRIPTIONS[4]+weekday));
            }
    }

    @Override
    public void receiveEditCards() {
        BaseMod.addCard(new sciSTS.cards.SelfRepair());
        BaseMod.addCard(new  sciSTS.cards.ForeignInfluence());
    }
}