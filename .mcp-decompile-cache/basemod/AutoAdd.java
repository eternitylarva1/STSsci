/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  basemod.AutoAdd$Info
 *  basemod.AutoAdd$NotPackageFilter
 *  basemod.AutoAdd$PackageFilter
 *  basemod.BaseMod
 *  com.evacipated.cardcrawl.modthespire.Loader
 *  com.evacipated.cardcrawl.modthespire.ModInfo
 *  com.megacrit.cardcrawl.cards.AbstractCard
 *  com.megacrit.cardcrawl.unlock.UnlockTracker
 *  javassist.ClassPool
 *  javassist.CtClass
 *  javassist.NotFoundException
 *  org.clapper.util.classutil.AbstractClassFilter
 *  org.clapper.util.classutil.AndClassFilter
 *  org.clapper.util.classutil.ClassFilter
 *  org.clapper.util.classutil.ClassFinder
 *  org.clapper.util.classutil.ClassInfo
 *  org.clapper.util.classutil.ClassModifiersClassFilter
 *  org.clapper.util.classutil.InterfaceOnlyClassFilter
 *  org.clapper.util.classutil.NotClassFilter
 */
package basemod;

import basemod.AutoAdd;
import basemod.BaseMod;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.evacipated.cardcrawl.modthespire.ModInfo;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.BiConsumer;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import org.clapper.util.classutil.AbstractClassFilter;
import org.clapper.util.classutil.AndClassFilter;
import org.clapper.util.classutil.ClassFilter;
import org.clapper.util.classutil.ClassFinder;
import org.clapper.util.classutil.ClassInfo;
import org.clapper.util.classutil.ClassModifiersClassFilter;
import org.clapper.util.classutil.InterfaceOnlyClassFilter;
import org.clapper.util.classutil.NotClassFilter;

public class AutoAdd {
    private ClassFinder finder = new ClassFinder();
    private List<ClassFilter> filters;
    private ClassPool pool;
    private Boolean defaultSeenOverride = null;

    public AutoAdd(String modID) {
        try {
            for (ModInfo info : Loader.MODINFOS) {
                if (info == null || modID == null || !modID.equals(info.ID) || info.jarURL == null) continue;
                this.finder.add(new File(info.jarURL.toURI()));
            }
        }
        catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        this.filters = new ArrayList<ClassFilter>();
        this.pool = Loader.getClassPool();
    }

    public AutoAdd overrideClassPool(ClassPool pool) {
        this.pool = pool;
        return this;
    }

    public AutoAdd setDefaultSeen(boolean seen) {
        this.defaultSeenOverride = seen;
        return this;
    }

    public AutoAdd filter(ClassFilter filter) {
        this.filters.add(filter);
        return this;
    }

    public AutoAdd packageFilter(String packageName) {
        return this.filter((ClassFilter)new PackageFilter(packageName));
    }

    public AutoAdd packageFilter(Class<?> packageClass) {
        return this.filter((ClassFilter)new PackageFilter(packageClass));
    }

    public AutoAdd notPackageFilter(String packageName) {
        return this.filter((ClassFilter)new NotPackageFilter(packageName));
    }

    public AutoAdd notPackageFilter(Class<?> packageClass) {
        return this.filter((ClassFilter)new NotPackageFilter(packageClass));
    }

    public <T> Collection<CtClass> findClasses(Class<T> type) {
        try {
            ArrayList<ClassFilter> tmp = new ArrayList<ClassFilter>();
            tmp.addAll(Arrays.asList(new NotClassFilter((ClassFilter)new InterfaceOnlyClassFilter()), new NotClassFilter((ClassFilter)new AbstractClassFilter()), new ClassModifiersClassFilter(1)));
            tmp.addAll(this.filters);
            AndClassFilter filter = new AndClassFilter(tmp.toArray(new ClassFilter[0]));
            ArrayList foundClasses = new ArrayList();
            this.finder.findClasses(foundClasses, (ClassFilter)filter);
            ArrayList<CtClass> ret = new ArrayList<CtClass>();
            block2: for (ClassInfo classInfo : foundClasses) {
                CtClass ctClass;
                CtClass ctSuperClass = ctClass = this.pool.get(classInfo.getClassName());
                do {
                    if (!ctSuperClass.getName().equals(type.getName())) continue;
                    ret.add(ctClass);
                    continue block2;
                } while ((ctSuperClass = ctSuperClass.getSuperclass()) != null);
            }
            return ret;
        }
        catch (NotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> void any(Class<T> type, BiConsumer<Info, T> add) {
        try {
            Collection<CtClass> foundClasses = this.findClasses(type);
            for (CtClass ctClass : foundClasses) {
                Info info = new Info(this, ctClass, null);
                if (info.ignore) continue;
                T t = type.cast(this.pool.getClassLoader().loadClass(ctClass.getName()).newInstance());
                add.accept(info, (Info)t);
            }
        }
        catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            throw new RuntimeException(e);
        }
    }

    public void cards() {
        this.any(AbstractCard.class, (info, card) -> {
            BaseMod.addCard((AbstractCard)card);
            if (info.seen) {
                UnlockTracker.unlockCard((String)card.cardID);
            }
        });
    }

    static /* synthetic */ Boolean access$100(AutoAdd x0) {
        return x0.defaultSeenOverride;
    }
}
