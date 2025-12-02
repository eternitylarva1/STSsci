# API
To add a custom top panel item you need to register your item with Basemod, you should do this in the `receivePostInitialize` callback from `PostInitializeSubscriber`.

* `BaseMod.addTopPanelItem(TopPanelItem topPanelItem)` - this is what you what you would use to register your top panel item with Basemod. This is all you need for Basemod to add the item to the top panel.

# Example
The `TopPanelItem` class should be extended to create your TopPanelItem. 
```Java
public class TopPanelItemExample extends TopPanelItem {
    private static final Texture IMG = new Texture("yourmodresources/images/icon.png");
    public static final String ID = "yourmodname:TopPanelItemExample";

    public TopPanelItemExample() {
	super(IMG, ID);
    }

    @Override
    protected void onClick() {
	// your onclick code
    }
}
```