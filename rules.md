# Claude Code 使用规则和教训

## 重要教训记录

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
3. 不在不理解的情况下修改核心逻辑
4. 不重复实现用户已有的功能