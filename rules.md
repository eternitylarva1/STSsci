# Claude Code 使用规则和教训

## 重要教训记录

- ✅ 优先使用游戏原生方法和系统
- ✅ 使用MCP查看原版实现方法
- ❌ 不要重新实现已有功能
- 大部分的功能实现都是使用的依赖中的desktop.jar中的文件，请你优先反编译这个文件来找到对应的类
- 禁止使用rm -rf 这种危险的指令，不需要的类请你注释掉

### 2025-12-05 反射vs直接字段访问教训

**问题描述：**
在烧烤系统卡牌显示遗物贴图时，我错误地使用反射访问 `AbstractRelic.img` 字段，而该字段本来就是 public 的，完全可以直接访问。

**错误行为：**
```java
// ❌ 错误的反射方式
java.lang.reflect.Field imgField = AbstractRelic.class.getDeclaredField("img");
imgField.setAccessible(true);
Object texture = imgField.get(relic);
```

**正确做法：**
```java
// ✅ 直接访问public字段
Object texture = relic.img;
```

**错误原因：**
1. 没有先查看字段修饰符就盲目使用反射
2. 不了解 `AbstractRelic.img` 是 public 字段，可以直接访问
3. 过度复杂化简单的字段访问操作

**教训总结：**
1. **先查看字段修饰符** - 在使用反射前，先确认字段是否为 private/protected
2. **public 字段直接访问** - 如果字段是 public 的，直接使用 `.` 访问即可
3. **避免过度工程** - 不要为了"看起来更专业"而使用不必要的复杂技术
4. **简化代码** - 简单直接的代码比复杂的反射更易读和维护

**查看字段的正确方式：**
```bash
# 1. 使用MCP分析类结构
mcp__bfHaz-Y7LcmAWkjy1mpdr__analyze_class("com.megacrit.cardcrawl.relics.AbstractRelic", projectPath)

# 2. 查看SlayTheSpireLibrary源码
head -100 ".SlayTheSpireLibrary/com/megacrit/cardcrawl/relics/AbstractRelic.java" | grep -A 2 -B 2 "public.*img"
```

**避免重复错误的方法：**
- 访问字段前先用 MCP 或源码查看字段的修饰符
- 只有在字段是 private/protected 时才考虑使用反射
- 优先选择简单直接的实现方式

### 2025-12-04 火堆烧烤系统实现教训

**问题描述：**
在实现火堆烧烤系统时，我没有使用合适的代码结构和标准方法，导致后续维护困难。

**错误行为：**
1. 烧烤映射使用实例变量而非静态常量，每次创建对象都重新初始化
2. 没有查看遗物的标准方法，错误地使用反射调用gainRelic而非instantObtain
3. 没有建立便于扩展的数据结构，后续添加新的烧烤映射需要修改多处代码

**正确理解：**
- 烧烤映射应该使用静态常量HashMap，便于维护和扩展
- 遗物添加应该使用`instantObtain()`方法，这是游戏内添加遗物的标准做法
- 共同的数据结构应该集中管理，避免重复定义

**修正实现：**
1. **静态映射表**: 创建`GRILLABLE_MAPPING`静态常量HashMap，在一个地方统一管理所有烧烤映射
2. **标准API使用**: 使用`newRelic.instantObtain()`方法直接添加遗物到玩家背包
3. **模块化设计**: GrillEffect和GrillOption都引用同一个静态映射表，确保一致性

**新增映射方法：**
```java
// 在GRILLABLE_MAPPING的static块中直接添加
GRILLABLE_MAPPING.put("原版遗物ID", "烧烤后遗物ID");
```

**技术要点：**
1. **静态常量HashMap**: 使用`private static final Map<String, String> GRILLABLE_MAPPING`定义映射表
2. **标准遗物添加**: 使用`newRelic.instantObtain()`而非复杂的反射调用
3. **代码复用**: 多个类引用同一个数据源，避免重复定义

**开发流程优化：**
1. **先查标准API**: 实现功能前先查看游戏原版类的标准方法
2. **设计可扩展结构**: 预留扩展接口，便于后续功能添加
3. **统一数据管理**: 共用数据集中在单一位置管理 
### 2025-12-03 CombatRewardPatch 错误修改教训

**问题描述：**
在用户要求完善 CombatRewardPatch 的额外奖励系统时，我没有理解用户的实际需求，错误地创建了自定义奖励类型而不是使用游戏内已有的遗物。

**错误行为：**
1. 创建了 `MonsterRewardConfig.java` 和 `AbstractMonsterRewardPatch.java`，完全重复了用户已有的系统
2. 硬编码了奖励类型（COMMON_RELIC, GOLD_SMALL, POTION等）而不是使用游戏已有遗物
3. 在用户已经有 `CombatRewardPatch.java` 的情况下，没有仔细检查现有实现就进行修改

**正确理解：**
- 用户使用 `MonsterReward HashMap<String,String>` 存储怪物ID到遗物ID的映射
- 通过 `RelicLibrary.getRelic(rewardKey).makeCopy()` 获取游戏中的实际遗物作为奖励
- 这是一个在原版游戏奖励基础上添加额外遗物的系统，不是创建新奖励类型

**教训总结：**
1. **先理解，再实现** - 在修改任何代码前，必须先完整理解用户现有的实现逻辑
2. **检查已有实现** - 不能重复造轮子，要基于用户已有代码进行完善
3. **不要假设需求** - 不能自己假设用户要什么，要仔细阅读和分析现有代码
4. **git历史检查** - 在重置分支后，应该先检查用户之前的commit内容
5. **使用游戏现有机制** - 优先使用游戏已有的类和方法，而不是重新创建

**避免重复错误的方法：**
- 每次修改前先用 `git log` 查看用户之前的工作
- 仔细阅读用户已有代码，理解其设计思路
- 对于不明确的需求，先询问确认而不是自己猜测
- 使用 `git diff` 检查实际差异，理解用户的修改意图

### 2025-12-03 配置映射和UTF-8编码教训

**问题描述：**
在处理CombatRewardScreen的掉落配置时，对HashMap的键值映射关系理解错误，同时遇到了编译编码问题。

**错误行为：**
1. 初始错误理解了 `MonsterReward<HashMap<String,String>>` 的映射关系，误以为key是怪物ID，value是遗物ID
2. 没有正确理解用户"一个怪物可以有多个掉落物"的需求，应该使用 `HashMap<String,List<String>>` 结构
3. 编译时遇到GBK字符编码错误，没有及时意识到需要UTF-8编码配置

**正确理解：**
- `MonsterReward` 的正确映射是：key=遗物ID, value=List<怪物ID>
- 一个遗物可以被多个怪物掉落，一个怪物也可以掉落多个遗物
- 需要使用 `HashMap<String,List<String>>` 来支持多对多关系
- Maven项目需要配置UTF-8编码来正确处理中文字符

**技术要点：**
1. **数据结构选择**：当需要一对多映射时，使用 `HashMap<String,List<String>>` 而不是 `HashMap<String,String>`
2. **UTF-8编码配置**：在pom.xml中添加编译编码配置：
   ```xml
   <properties>
       <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
       <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
   </properties>
   ```
3. **调试信息**：添加详细的控制台输出来跟踪配置加载和匹配过程

**开发流程优化：**
1. **明确需求**：确保完全理解用户的具体配置要求
2. **编码规范**：及时配置项目编码环境，避免字符问题
3. **调试增强**：为复杂的游戏逻辑添加足够的调试输出
4. **配置验证**：通过打印配置和匹配过程来验证实现正确性

**问题描述：**
在用户要求完善 CombatRewardPatch 的额外奖励系统时，我没有理解用户的实际需求，错误地创建了自定义奖励类型而不是使用游戏内已有的遗物。

**错误行为：**
1. 创建了 `MonsterRewardConfig.java` 和 `AbstractMonsterRewardPatch.java`，完全重复了用户已有的系统
2. 硬编码了奖励类型（COMMON_RELIC, GOLD_SMALL, POTION等）而不是使用游戏已有遗物
3. 在用户已经有 `CombatRewardPatch.java` 的情况下，没有仔细检查现有实现就进行修改

**正确理解：**
- 用户使用 `MonsterReward HashMap<String,String>` 存储怪物ID到遗物ID的映射
- 通过 `RelicLibrary.getRelic(rewardKey).makeCopy()` 获取游戏中的实际遗物作为奖励
- 这是一个在原版游戏奖励基础上添加额外遗物的系统，不是创建新奖励类型

**教训总结：**
1. **先理解，再实现** - 在修改任何代码前，必须先完整理解用户现有的实现逻辑
2. **检查已有实现** - 不能重复造轮子，要基于用户已有代码进行完善
3. **不要假设需求** - 不能自己假设用户要什么，要仔细阅读和分析现有代码
4. **git历史检查** - 在重置分支后，应该先检查用户之前的commit内容
5. **使用游戏现有机制** - 优先使用游戏已有的类和方法，而不是重新创建

**避免重复错误的方法：**
- 每次修改前先用 `git log` 查看用户之前的工作
- 仔细阅读用户已有代码，理解其设计思路
- 对于不明确的需求，先询问确认而不是自己猜测
- 使用 `git diff` 检查实际差异，理解用户的修改意图

## 开发规范

### 遵循的原则
1. **用户优先** - 以用户的代码和需求为核心，不强行修改
2. **最小改动** - 只做必要的修改，避免过度工程化
3. **理解后行动** - 确保完全理解需求后再开始编码
4. **及时检讨** - 发现错误立即记录，避免重复犯同样错误

### 禁止行为
1. 不使用 `rm -rf` 等危险指令
2. 不删除不需要的类，应该注释掉
3. 在不理解的情况下修改核心逻辑
4. 不重复实现用户已有的功能

### 2025-12-03 遗物和怪物识别教训

**问题描述：**
在修正CombatRewardScreen掉落配置时，错误识别了遗物和怪物，导致配置映射错误。

**错误行为：**
1. 错误假设规则：以为`Sci:`开头的是怪物，不以`Sci:`开头的是遗物
2. 错误判断`relics.json`中的遗物身份，如误以为`GremlinLeader`是怪物
3. 在没有完全理解配置逻辑的情况下随意修改用户的正确配置
4. 基于调试信息错误理解映射关系，没有仔细验证每个ID的真实身份

**正确理解：**
- **遗物识别**：所有在`relics.json`中定义的都是遗物，包括`Sci:`开头的和不以`Sci:`开头的
- **怪物识别**：不在`relics.json`中定义的是怪物，如`Champ`, `GremlinNob`等
- **配置映射**：`MonsterReward`的映射关系是`key=遗物ID, value=List<怪物ID>`
- **验证方法**：通过检查`relics.json`文件确认每个ID的真实身份，而不是基于命名规则猜测

**教训总结：**
1. **仔细验证**：在修改任何配置前，必须先验证每个ID的真实身份
2. **不要猜测**：不要基于命名规则或表面现象做假设，要查看实际定义文件
3. **尊重用户配置**：用户的现有配置可能是正确的，不要轻易修改
4. **调试信息分析**：调试信息中显示的配置可能和实际代码不符，要基于代码分析而不是调试信息

**避免重复错误的方法：**
- 修改配置前，先检查相关定义文件（如`relics.json`）
- 对于不明确的ID，先确认其身份再使用
- 在修改用户配置前，先理解清楚其设计意图
- 写代码前阅读rules.md，查看相关的历史教训

### 2025-12-05 反射使用规范教训

**问题描述：**
在使用反射访问游戏类时，没有先使用MCP反编译目标类来验证方法/字段是否存在，导致编译错误。

**错误行为：**
1. 在BranchFuel中使用`Invoker.getField()`访问CampfireUI的`buttons`字段前，没有检查该字段是否存在
2. 在LishiPatch中使用反射访问Lagavulin的`asleep`私有字段时，没有先查看类的实际结构
3. 尝试使用不存在的类GetMonsterNamesAction作为patch目标
4. 尝试访问AbstractDungeon的`nextRoomTransitionKey`字段时没有验证其存在性

**正确流程：**
1. **先MCP反编译** - 使用MCP工具反编译目标类，查看其实际结构
2. **验证存在性** - 确认要访问的方法/字段确实存在
3. **检查访问权限** - 了解方法是public/private，是否需要特殊处理
4. **使用标准方法** - 优先使用游戏提供的标准API

**技术要点：**
```java
// ✅ 正确做法：先MCP查看
mcp__bfHaz-Y7LcmAWkjy1mpdr__analyze_class("com.megacrit.cardcrawl.ui.campfire.CampfireUI", projectPath)
// 确认字段存在后再使用反射

// ❌ 错误做法：直接猜测并使用反射
ArrayList<AbstractCampfireOption> buttons = Invoker.getField(restRoom.campfireUI, "buttons");
```

**避免重复错误的方法：**
1. **反编译先行** - 使用反射前必须先MCP反编译目标类
2. **验证API** - 确认方法签名和字段类型正确
3. **查看标准方法** - 检查是否有更简单的公开API可用
4. **文档记录** - 将验证过的方法/字段记录到代码注释中

## 🔍 原版卡牌/遗物识别方法

### 📁 使用.SlayTheSpireLibrary查找原版内容
当需要确认某个卡牌/遗物是否为游戏原版时，按以下步骤操作：

#### 1. 定位本地化文件
```bash
# 在SlayTheSpireLibrary中查找本地化文本
grep -n "卡牌名称" ".SlayTheSpireLibrary/本地化文本/cards.json"
grep -n "遗物名称" ".SlayTheSpireLibrary/本地化文本/relics.json"
```

#### 2. 已确认的原版情感类诅咒卡牌
通过检索发现以下5张卡牌是**游戏原版**的情感类诅咒：

| 类名 | 中文名 | 英文名 | 类型 | 描述特征 |
|------|---------|---------|------|----------|
| Doubt | 疑虑 | Doubt | 诅咒 | "不能被打出。回合结束时，获得1层虚弱。" |
| Regret | 悔恨 | Regret | 诅咒 | "不能被打出。回合结束时，失去相当于手牌数量的生命。" |
| Writhe | 苦恼 | Writhe | 诅咒 | "不能打出。固有。" |
| Pride | 傲慢 | Pride | 诅咒 | "固有。消耗。回合结束时，在抽牌堆顶部加入复制品。" |
| Shame | 羞耻 | Shame | 诅咒 | "不能打出。回合结束时，获得1层脆弱。" |

#### 3. 使用方法
**在代码中的正确引用方式：**
```java
// ✅ 正确：直接使用原版类
if (card instanceof Doubt || card instanceof Regret || card instanceof Writhe || card instanceof Pride || card instanceof Shame)

// ❌ 错误：自定义情感类（除非特殊需求）
// 不需要为原版卡牌创建自定义类
```

#### 4. 检查标志
**确认是否为原版的关键特征：**
- 在`.SlayTheSpireLibrary/本地化文本/`中有对应的JSON条目
- 卡包中没有`modId`字段
- 类路径以`com.megacrit.cardcrawl.cards.`开头
- 遗物类路径以`com.megacrit.cardcrawl.relics.`开头

#### 5. 常见误区
- ❌ 误以为需要为原版卡牌创建mod版本
- ❌ 重复实现游戏已有的功能
- ❌ 用自定义类包装原版内容增加复杂性

**总结：SlayTheSpireLibrary是确认原版内容的标准工具，应该在此目录中查找确认后再决定是否需要自定义实现。**

### 2025-12-05 用户代码正确性认识教训

**问题描述：**
我错误地假设用户代码有问题，而实际上用户的代码实现是正确的。我生成的代码反而有问题。

**错误行为：**
1. **质疑用户确认的字段名** - 用户确认CampfireUI有buttons字段，我仍然怀疑
2. **生成错误的反射调用** - 如访问不存在的Lagavulin.asleep字段
3. **不理解现有系统设计** - 用户已经实现了完整的怪物-武器映射系统，我却想重新实现
4. **过度复杂化** - 用户要求简单的bool变量控制掉落，我设计复杂的逻辑

**正确理解：**
- **用户的代码大概率是对的** - 特别是对游戏内部结构的了解
- **先理解现有系统** - 用户已经有了CombatRewardPatch的完整映射，应该复用
- **简单有效的设计** - 用static bool变量控制是否掉落是最简洁的方案
- **区分责任** - 我负责实现用户的功能需求，不是质疑用户的代码正确性

**正确实现方式：**
```java
// jiaoxie.java - 用户要求的功能
public static boolean dropWeapons = false;
if (weaponSlots.size() > 0) {
    dropWeapons = true; // 缴械成功时标记
}

// CombatRewardPatch.java - 复用现有系统
if (!jiaoxie.dropWeapons) {
    return; // 只有缴械时才执行掉落逻辑
}
// 执行用户已有的怪物-武器映射
jiaoxie.dropWeapons = false; // 重置标记
```

**技术要点：**
1. **信任用户判断** - 用户确认的字段/方法存在性应该相信
2. **复用现有系统** - 不要重新实现用户已有的功能
3. **理解设计意图** - 先搞清楚用户的设计思路再动手
4. **git提交意识** - 每次重要功能完成后记得提交代码

**避免重复错误的方法：**
1. **先咨询再实现** - 对不确定的字段/方法先询问用户
2. **分析用户代码** - 理解现有系统的设计原理
3. **简单方案优先** - 除非必要，不要复杂化实现
4. **养成提交习惯** - 每次功能完成后主动提交git记录