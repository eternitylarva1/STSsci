# SlayTheSpire Mod制作教程完整索引

## 教程概述

本教程目录提供了完整的SlayTheSpire（杀戮尖塔）模组制作指南，从环境搭建到高级技巧，适合从入门到进阶的学习者。

---

## 基础教程 (按学习顺序排列)

### 00 - 环境配置
**内容：** 开发环境搭建和项目初始化
- **技术要求：** Java 8、Json基础知识、简单的图像处理
- **开发工具：** IntelliJ IDEA（推荐）或 VSCode
- **核心文件：**
  - `pom.xml` - Maven项目配置文件
  - `ModTheSpire.json` - Mod信息配置文件
- **学习要点：**
  - Java 8安装和环境配置
  - Maven项目创建和依赖配置
  - ModTheSpire.json参数说明
  - 项目打包和调试配置

### 01 - 模组核心
**内容：** Mod基础架构和事件订阅机制
- **核心概念：**
  - `@SpireInitializer`注解的作用
  - BaseMod事件订阅系统
  - 各种Subscriber接口的使用
- **关键代码：**
```java
@SpireInitializer
public class ExampleMod implements EditCardsSubscriber {
    public ExampleMod() {
        BaseMod.subscribe(this);
    }

    public static void initialize() {
        new ExampleMod();
    }
}
```

### 02 - 添加新卡牌
**内容：** 创建和使用自定义卡牌
- **卡牌类结构：**
  - 继承`CustomCard`类
  - 必重写的方法：`use()`、`upgrade()`
  - 卡牌常量定义（ID、NAME、IMG_PATH等）
- **卡牌属性详解：**
  - **ID：** 唯一标识符，需要mod前缀
  - **NAME：** 卡牌显示名称
  - **IMG_PATH：** 卡牌图片路径（需要500*380和250*190两个尺寸）
  - **COST：** 卡牌费用（-2不显示，-1为X费）
  - **DESCRIPTION：** 卡牌描述（使用!D!等特殊标记）
  - **TYPE：** 卡牌类型（ATTACK、SKILL、POWER等）
  - **COLOR：** 卡牌颜色
  - **RARITY：** 卡牌稀有度
  - **TARGET：** 目标类型

### 03 - 添加卡牌效果
**内容：** 卡牌效果实现和Action系统
- **use方法详解：**
  - 参数：`AbstractPlayer p, AbstractMonster m`
  - Action队列管理
- **基础Action类型：**
  - `DamageAction` - 造成伤害
  - `DrawCardAction` - 抽牌
- **伤害系统：**
  - `DamageInfo`类使用
  - 伤害类型：NORMAL、THORNS、HP_LOSS
- **升级系统：**
  - `upgradeName()` - 升级标记
  - `upgradeDamage()` - 伤害升级
  - 标签系统：`CardTags.STRIKE`等

### 04 - 本地化
**内容：** 多语言文本管理
- **本地化文件结构：**
  ```
  resources/
      localization/
          ZHS/
              cards.json
              characters.json
              relics.json
              powers.json
          ENG/
              cards.json
              ...
  ```
- **JSON格式：**
```json
{
  "ExampleMod:Strike": {
    "NAME": "打击",
    "DESCRIPTION": "造成 !D! 点伤害。",
    "UPGRADE_DESCRIPTION": "固有。 NL 造成 !D! 点伤害。"
  }
}
```
- **代码中加载：**
  - `CardStrings`类使用
  - `CardCrawlGame.languagePack.getCardStrings()`
- **高级优化：**
  - ID生成简化
  - 格式化字符串支持

### 05 - 添加新颜色
**内容：** 自定义卡牌颜色和UI资源
- **颜色定义：**
  - RGB颜色值计算（0.0-1.0范围）
  - 颜色注册：`BaseMod.addColor()`
- **资源文件需求：**
  - 卡牌背景图（512x512和1024x1024）
  - 能量图标（orb）
  - 人物选择按钮图片
  - 人物立绘
- **颜色枚举：** 需要使用`@SpireEnum`注解

### 06 - 添加新人物
**内容：** 创建全新可玩角色
- **人物类结构：**
  - 继承`CustomPlayer`类
  - 初始化参数：生命值、能量、初始卡组等
  - 人物图片和动画资源
- **核心方法：**
  - `getStartingDeck()` - 初始卡组
  - `getStartingRelics()` - 初始遗物
  - `getCardColor()` - 卡牌颜色
  - `getTitle()` - 人物标题
- **人物属性：**
  - 碰撞箱大小
  - 能量管理
  - 对话框位置
  - 特效配置

### 07 - 添加新遗物
**内容：** 创建自定义遗物
- **遗物类结构：**
  - 继承`CustomRelic`类
  - 遗物等级：STARTER、COMMON、UNCOMMON、RARE、SPECIAL、CURSE
  - 音效配置：LandingSound
- **效果时机：**
  - `atBattleStart()` - 战斗开始
  - `onAttack()` - 攻击时
  - `onExhaust()` - 耗牌时
  - `onVictory()` - 胜利时
- **遗物类型：** SHARED（共享）或特定角色

### 08 - 添加新关键词
**内容：** 创建游戏术语和关键词系统
- **关键词注册：**
  - `BaseMod.addKeyword()`方法
  - JSON格式加载
- **关键词格式：**
```json
[
  {
    "NAMES": ["恐惧"],
    "DESCRIPTION": "拥有 #y恐惧 的角色造成的伤害减少。"
  }
]
```
- **使用方法：** 在描述中使用`#y关键词`格式

### 09 - 添加新能力
**内容：** 创建buff/debuff能力
- **能力类结构：**
  - 继承`AbstractPower`类
  - 能力类型：BUFF、DEBUFF、NEUTRAL
  - 可叠加性设置
- **图像资源：** 84x84和32x32两种尺寸
- **效果时机：**
  - `onAttacked()` - 被攻击时
  - `onUseCard()` - 使用卡牌时
  - `onDamageTaken()` - 受到伤害时
- **描述格式化：** 使用`String.format()`优化

### 10 - 添加Action
**内容：** 自定义游戏动作系统
- **Action基础：**
  - 继承`AbstractGameAction`类
  - `update()`方法实现
- **队列管理：**
  - `addToBot()` - 添加到队列底部
  - `addToTop()` - 添加到队列顶部
- **Action生命周期：**
  - 执行条件判断
  - 效果实现
  - `isDone = true`标记完成

### 11 - 如何上传mod
**内容：** Steam工坊发布流程
- **上传工具：** mod-uploader.jar
- **工作区管理：**
  - 创建：`java -jar mod-uploader.jar new -w [mod名]`
  - 上传：`java -jar mod-uploader.jar upload -w [mod名]`
- **配置文件：** config.json设置
- **注意事项：** 避免中文，使用英文标签

### 12 - 添加新怪物
**内容：** 创建自定义怪物
- **怪物类结构：**
  - 继承`CustomMonster`类
  - 生命值和伤害设置
  - 行为模式配置
- **AI系统：**
  - `getMove()` - 意图判定
  - `takeTurn()` - 行动执行
  - 伤害计算和意图显示
- **遭遇配置：**
  - 普通遭遇
  - 精英遭遇
  - BOSS遭遇

---

## 进阶教程

### 高级技巧
#### 01 - Patch
- 代码注入和修改技术
- 游戏原类功能扩展

#### 02 - 依赖其他mod
- 模组间依赖管理
- API接口调用

#### 03 - 保存数据
- 游戏数据持久化
- 自定义保存内容

#### 04 - BaseMod提供的工具
- 自动化工具使用
- 辅助功能实现

### 前人的代码基础
#### 包装卡牌类
- 卡牌功能封装
- 重构和优化

#### 分开独立的能力
- 能力模块化设计
- 功能分离

#### 匿名函数
- Lambda表达式使用
- 代码简化

#### 添加音频及注意事项
- 音效系统实现
- 音频资源管理

### 新手必备知识
- 游戏基础概念
- 常用工具介绍
- 开发技巧汇总

---

## 实用资源

### 工具推荐
1. **JD-GUI** - Java反编译工具
2. **STS裁图器** - 卡牌图片裁剪
3. **龙骨/Spine** - 2D动画制作

### 重要链接
- [ModTheSpire Wiki](https://github.com/kiooeht/ModTheSpire/wiki) - MTS官方文档
- [BaseMod Wiki](https://github.com/daviscook477/BaseMod/wiki) - BaseMod API文档
- [示例模组](https://github.com/Rita-Bernstein/Warlord-Emblem) - 标准化mod范例

### 交流群组
- 个人交流群：542370192
- 杀戮尖塔mod交流群：723677239

---

## 学习建议

### 学习路径
1. **环境配置** - 确保开发环境正常运行
2. **模组核心** - 理解基础架构
3. **添加卡牌** - 制作第一个可运行的内容
4. **本地化** - 添加多语言支持
5. **新人物** - 完整角色制作
6. **其他元素** - 遗物、能力、怪物等
7. **高级技巧** - 深入功能实现

### 注意事项
- 需要Java编程基础
- 图像处理知识有助于UI设计
- 耐心和调试能力很重要
- 参考原版代码学习最佳实践

### 常见问题
- 版本兼容性
- 资源文件管理
- 错误调试技巧
- 性能优化方法

---

*最后更新时间：2025年12月2日*