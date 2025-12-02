# Slay the Spire Mod 通用开发规范

## 📋 核心原则

### 1. 不重复造轮子
- ✅ 优先使用游戏原生方法和系统
- ✅ 使用MCP查看原版实现方法
- ❌ 不要重新实现已有功能

### 2. 使用原生游戏系统
```java
// ✅ 正确做法 - 直接使用原版方法
card.use(AbstractDungeon.player, targetMonster);
AbstractDungeon.actionManager.addToTop(new DamageAction(target, damageInfo));

// ❌ 错误做法 - 自定义系统
// 不要自定义伤害计算、能量系统、效果触发
```

### 3. 安全第一原则
- ✅ 所有必要的安全检查和异常处理
- ✅ 确保不会导致游戏崩溃
- ✅ 降级处理：异常时安全退出

## 🎯 开发约束

### 1. 快捷键使用规范
- ❌ 随意添加键盘快捷键 (如 R、B、T 等)
- ✅ 只在调试/测试时添加，发布前移除
- ✅ 如需快捷键，优先使用游戏已有热键系统

### 2. 房间和状态检查
```java
// ✅ 标准检查模式
if (AbstractDungeon.getCurrRoom() != null) {
    if (!(AbstractDungeon.getCurrRoom() instanceof RestRoom)) {
        // 在非房间中执行逻辑
    }
}

if (CardCrawlGame.isInARun()) {
    // 在游戏运行时执行逻辑
}
```

### 3. 空指针和异常检查
```java
// ✅ 标准安全检查
if (player != null && targetMonster != null) {
    try {
        // 执行关键操作
    } catch (Exception e) {
        logger.error("操作失败: " + e.getMessage());
        // 安全退出，不崩溃
    }
}
```

## 🔧 技术实现规范

### 1. 动作系统使用
```java
// ✅ 正确动作系统使用
AbstractDungeon.actionManager.addToTop(new DamageAction(target, damageInfo));
AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(target, source, power));

// ✅ 卡牌使用
effectCard.use(AbstractDungeon.player, targetMonster);
```

### 2. 渲染和Patch规范
```java
// ✅ 标准渲染Patch
@SpirePatch(clz = AbstractPlayer.class, method = "render")
public static class RenderPatch {
    @SpirePostfixPatch
    public static void Postfix(AbstractPlayer _instance, SpriteBatch sb) {
        if (AbstractDungeon.getCurrRoom() != null) {
            // 渲染逻辑
        }
    }
}
```

### 3. 配置管理
```java
// ✅ 标准配置结构
public class MyModConfig extends EasyConfigPanel {
    public static boolean someFeature = true;
    public static int maxValue = 25;

    // 配置更新方法
    private void updateConfig() {
        // 从配置文件读取更新状态
    }
}
```

## 🎮 输入处理规范

### 1. 键盘输入检测
```java
// ✅ 标准键盘检测
if (Gdx.input.isKeyJustPressed(Input.Keys.T)) {
    // 只在调试时使用
}

// ✅ 使用InputHelper（如果可用）
if (InputHelper.isMouseDown) {
    // 鼠标检测
}
```

### 2. 自定义交互系统
```java
// ✅ 手动hover检测（如果需要自定义交互）
private boolean isHovered(float x, float y, float width, float height) {
    float mouseX = InputHelper.mX;
    float mouseY = InputHelper.mY;

    return mouseX >= x && mouseX <= x + width &&
           mouseY >= y && mouseY <= y + height;
}
```

## 📊 数据和存储规范

### 1. SaveFile相关
```java
// ✅ 标准SaveFile处理
@SpirePatch(clz = SaveFile.class, method = "loadSave")
public static class SaveLoadPatch {
    @SpirePostfixPatch
    public static void Postfix(SaveFile __instance) {
        // 存档加载后的处理
    }
}
```

### 2. 静态数据管理
```java
// ✅ 使用静态集合管理
public static List<GameObject> objects = new ArrayList<>();

// ✅ 及时清理资源
public static void clearResources() {
    objects.clear();
}
```

## 🧪 代码质量要求

### 1. 简洁性
- 单个方法不超过20行
- 避免深度嵌套（最多3层）
- 优先使用已有工具类

### 2. 性能优化
- 避免每帧复杂计算
- 合理使用缓存
- 及时清理资源

### 3. 可读性和维护性
```java
// ✅ 清晰的命名和方法结构
public void processPlayerDamage(AbstractPlayer player, DamageInfo damage) {
    if (!isValidPlayer(player)) {
        return;
    }

    applyDamage(player, damage);
    updateDamageEffects(player);
}
```

## 🔄 开发流程

### 1. 新功能开发流程
1. **MCP调研**：查看原版是否有类似功能
2. **最小实现**：使用最简单的代码实现核心需求
3. **安全测试**：添加空指针和异常检查
4. **简化删除**：删除不必要的复杂性

### 2. 代码审查清单
- [ ] MCP调研：原版是否有类似功能？
- [ ] 最小化：能否用更简单代码实现？
- [ ] 安全性：是否添加了必要的检查？
- [ ] 兼容性：是否影响现有功能？
- [ ] 性能：无显著性能下降

### 3. 提交前检查
- [ ] 编译测试：确保代码无编译错误
- [ ] 功能测试：核心功能正常工作
- [ ] 边界测试：异常情况安全处理
- [ ] 游戏测试：在真实游戏环境中测试

## 📋 常见陷阱和解决方案

### 1. 时间和Tick相关
```java
// ✅ 安全的时间管理
private float timer = 0.0f;

public void update(float deltaTime) {
    timer += deltaTime;
    if (timer >= 1.0f) {
        timer -= 1.0f;
        // 执行定时任务
    }
}
```

### 2. 资源和内存管理
```java
// ✅ 及时释放资源
public void dispose() {
    if (texture != null) {
        texture.dispose();
        texture = null;
    }
}
```

### 3. 线程安全
```java
// ✅ 主线程执行游戏逻辑
AbstractDungeon.actionManager.addToTop(new RunnableAction(() -> {
    // 线程安全的操作
}));
```

---

## 🔧 遗物开发特殊规则

### 遗物图像加载原则

#### 1. Texture加载方式
```java
// ✅ 正确方式 - 标准Texture加载
super(ID, new Texture("SciSTSResources/images/relics/文件名.png"), RelicTier, LandingSound);

// ❌ 错误方式 - 不要随意改为Gdx.files.internal
super(ID, new Texture(Gdx.files.internal("SciSTSResources/images/relics/文件名.png")), RelicTier, LandingSound);
```

#### 2. 图像文件引用原则
- **保持原始引用**: 即使引用的图像文件不存在，也不要随意改成其他文件
  - `tinyChest.png` → 保持 `tinyChest.png` (不要改为 `cage.png`)
  - `medicalKit.png` → 保持 `medicalKit.png` (不要改为 `pear.png`)
  - `dagger.png` → 保持 `dagger.png` (文件存在，正常引用)

- **只修复明显错误**: 只有在确定路径明显错误时才修改
  - 正常存在的文件: `cage.png`, `dagger.png`, `sword.png`, `mango.png`, `pear.png` 等
  - 缺失的文件: `tinyChest.png`, `medicalKit.png` 等

#### 3. 错误排查顺序
1. **首先检查Texture加载方式** - 确保使用标准的 `new Texture()`
2. **确认图像文件路径** - 检查路径是否正确
3. **验证import语句** - 确保所有必要的import都已添加
4. **不要轻易修改原始引用** - 特别是在不确定图像文件存在性时

#### 4. 常见错误示例
```java
// ❌ 错误: 随意更改Texture加载方式
super(ID, new Texture(Gdx.files.internal("SciSTSResources/path.png")), tier, sound);

// ❌ 错误: 随意更改文件引用
super(ID, "cage.png", tier, sound); // 原本是 "tinyChest.png"

// ❌ 错误: 没有检查就修改文件引用
super(ID, "pear.png", tier, sound); // 原本是 "medicalKit.png"

// ✅ 正确: 保持原始的加载方式和文件引用
super(ID, new Texture("SciSTSResources/images/relics/tinyChest.png"), tier, sound);
super(ID, new Texture("SciSTSResources/images/relics/medicalKit.png"), tier, sound);
```

#### 5. 开发态度
- **保守修改**: 在不确定的情况下，保持原始代码不变
- **测试优先**: 修改后立即测试，确保不引入新问题
- **文档记录**: 对于已知的缺失图像文件，记录在文档中但保持原引用
- **渐进修复**: 先让基础功能正常工作，再处理图像问题

---

*此规范适用于大部分Slay the Spire Mod开发，确保代码质量和游戏稳定性*