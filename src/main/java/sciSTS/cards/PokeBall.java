package sciSTS.cards;

import basemod.abstracts.CustomCard;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.shrines.Nloth;
import com.megacrit.cardcrawl.events.shrines.WeMeetAgain;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.beyond.AwakenedOne;
import com.megacrit.cardcrawl.monsters.beyond.Darkling;
import com.megacrit.cardcrawl.monsters.city.Byrd;
import com.megacrit.cardcrawl.monsters.city.Chosen;
import com.megacrit.cardcrawl.monsters.exordium.Cultist;
import com.megacrit.cardcrawl.relics.PreservedInsect;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import sciSTS.relics.EmptyCage;
import sciSTS.relics.FullCage;


import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.getMonsters;
import static com.megacrit.cardcrawl.monsters.AbstractMonster.EnemyType.BOSS;
import static com.megacrit.cardcrawl.monsters.AbstractMonster.EnemyType.ELITE;

public class PokeBall extends CustomCard {
    private static final CardStrings cardStrings;
    public static final String ID = "SciPokeBall";
    public static final String NAME;
    public static final String DESCRIPTION;
    private  int s=0;

    private EmptyCage pokeGo;

    public PokeBall(EmptyCage pokeGo) {
        super(ID, NAME, "SciSTSResources/images/cards/PokeBall.png", 1, DESCRIPTION, CardType.SKILL, CardColor.COLORLESS, CardRarity.UNCOMMON, CardTarget.ENEMY);
        this.exhaust = true;
        this.isEthereal = true;
        this.pokeGo = pokeGo;

    }

    public void use(AbstractPlayer p, AbstractMonster m) {
if (isBird(m)){
   if (m.currentHealth>1){
       return;
   }
    AbstractDungeon.topLevelEffectsQueue.add(new AbstractGameEffect() {
        public void update() {
            isDone=true;
            AbstractDungeon.player.loseRelic(EmptyCage.ID);
            AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float)(Settings.WIDTH / 2), (float)(Settings.HEIGHT / 2), new FullCage());
            m.die();
            m.hideHealthBar();
        }
        @Override
        public void render(SpriteBatch spriteBatch) {
            isDone=true;

        }

        @Override
        public void dispose() {

        }
    });

}
    }
    public static boolean isBird(AbstractMonster m){
        if (m.name.contains(Cultist.NAME)||m.name.contains(Byrd.NAME)||m.name.contains(AwakenedOne.NAME)||m.name.contains(Chosen.NAME)) {
            return true;
        }
        return false;
        }
    public AbstractCard makeCopy() {
        return new PokeBall(pokeGo);
    }

    public void upgrade() {
    }

    @Override
    public boolean canUpgrade() {
        return false;
    }

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
        NAME = cardStrings.NAME;
        DESCRIPTION = cardStrings.DESCRIPTION;
    }
}
