package MapMarks.ui;

import MapMarks.MapMarks;
import MapMarks.utils.ColorDatabase;
import MapMarks.utils.ColorEnum;
import MapMarks.utils.SoundHelper;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import easel.ui.AbstractWidget;
import easel.ui.AnchorPosition;
import easel.ui.InterpolationSpeed;
import easel.utils.EaselInputHelper;
import easel.utils.colors.EaselColors;

import java.util.ArrayList;
import java.util.Arrays;

public class RadialMenu extends AbstractWidget<RadialMenu> {
    private static final float WIDTH = 500;
    private static final float HEIGHT = 500;

    private static final float THRESHOLD = 0.5f * RadialMenuObject.WIDTH; // TODO: maybe scale this up a tad (1.1x?)

    private static final float PI = (float) Math.PI;

    private final float thetaStart;
    private final float thetaDelta;

    private ArrayList<RadialMenuObject> objects;
    private RadialMenuObject centerObject;

    private static Color centerDefaultColor = Color.valueOf("8d8c80");

    int selectedIndex = -1;

    private boolean isOpen = false;
    private int startX;
    private int startY;

    private ColorEnum initialColorWhenOpened = ColorEnum.RED;

    public RadialMenu() {
        objects = new ArrayList<>(
                Arrays.asList(
                        new RadialMenuObject(ColorEnum.WHITE),
                        new RadialMenuObject(ColorEnum.RED),
                        new RadialMenuObject(ColorEnum.GREEN),
                        new RadialMenuObject(ColorEnum.BLUE),
                        new RadialMenuObject(ColorEnum.PURPLE),
                        new RadialMenuObject(ColorEnum.YELLOW)
                )
        );

        thetaDelta = (2.0f * PI) / objects.size();
        thetaStart = (PI / 2.0f) - thetaDelta;

        centerObject = new RadialMenuObject(centerDefaultColor);
    }


    public void open() {
        isOpen = true;

        if (selectedIndex != -1) {
            initialColorWhenOpened = objects.get(selectedIndex).getColor();
        }

        selectedIndex = -1;

        startX = EaselInputHelper.getMouseX();
        startY = EaselInputHelper.getMouseY();

        anchoredCenteredOnMouse();
    }

    public void close() {
        isOpen = false;
    }

    public boolean isMenuOpen() {
        return isOpen;
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public ColorEnum getSelectedColorOrDefault() {
        if (selectedIndex != -1) {
            return objects.get(selectedIndex).getColor();
        } else {
            //return objects.get(1).getColor();
            return initialColorWhenOpened;
        }
    }

    public void setSelectedColor(ColorEnum color)
    {
        for(int i = 0; i < objects.size(); i++)
        {
            if(objects.get(i).getColor() == color)
            {
                selectedIndex = i;
                return;
            }
        }

        selectedIndex = -1;
    }

    @Override
    public float getContentWidth() {
        return WIDTH;
    }

    @Override
    public float getContentHeight() {
        return HEIGHT;
    }

    @Override
    public RadialMenu anchoredAt(float x, float y, AnchorPosition anchorPosition, InterpolationSpeed movementSpeed) {
        super.anchoredAt(x, y, anchorPosition, movementSpeed);

        final float cx = getContentCenterX();
        final float cy = getContentCenterY();

        final float distance = 70;

        float theta = thetaStart;

        for (RadialMenuObject obj : objects) {
            float dx = (float) (distance * Math.cos(theta));
            float dy = (float) (distance * Math.sin(theta));

            obj.anchoredAt(cx + dx, cy + dy, AnchorPosition.CENTER, movementSpeed);

            theta += thetaDelta;
        }

        centerObject.anchoredAt(cx, cy, AnchorPosition.CENTER, movementSpeed);

        return this;
    }

    private int computeClosestObjectIndex(float theta) {
        if (theta < 0)
            theta += 2 * PI;

        float targetTheta = thetaStart;

        int target = -1;
        float thetaDifference = 2 * PI;

        for (int i = 0; i < objects.size(); ++i) {
            float potentialThetaDifference = Math.abs(targetTheta - theta);

            if (potentialThetaDifference < thetaDifference) {
                target = i;
                thetaDifference = potentialThetaDifference;
            }

            targetTheta += thetaDelta;
        }

        return target;
    }

    @Override
    protected void updateWidget() {
        super.updateWidget();

        if (isOpen) {
            // TODO
            int currX = EaselInputHelper.getMouseX();
            int currY = EaselInputHelper.getMouseY();

            int dx = currX - startX;
            int dy = currY - startY;

            float distanceFromStart = (float) Math.sqrt(dx * dx + dy * dy);

            if (distanceFromStart > THRESHOLD) {
                int nextSelectedIndex = computeClosestObjectIndex((float) Math.atan2(dy, dx));

                // Selection changed
                if (nextSelectedIndex != selectedIndex) {
                    this.selectedIndex = nextSelectedIndex;

                    if (selectedIndex != -1) {
                        SoundHelper.playRadialChangeSound(selectedIndex, objects.size());

                        RadialMenuObject selected = objects.get(selectedIndex);
                        selected.setDimmed(false);
                        MapMarks.legendObject.setColor(selected.getColor());
//                    centerObject.setBaseColor(selected.getBaseColor());

                        // Dim the rest
                        for (int i = 0; i < objects.size(); ++i) {
                            if (i != selectedIndex) {
                                objects.get(i).setDimmed(true);
                            }
                        }
                    }
                }
            } else {
//                centerObject.setBaseColor(centerDefaultColor);
                objects.forEach(object -> object.setDimmed(false));
                selectedIndex = -1;

                // Reset to the initial color when opened
                MapMarks.legendObject.setColor(initialColorWhenOpened);
            }

        }
    }

    @Override
    protected void renderWidget(SpriteBatch sb) {
        if (isOpen) {
            for (int i = 0; i < objects.size(); ++i) {
                if (i != selectedIndex) {
                    objects.get(i).render(sb);
                }
            }
//            objects.forEach(object -> object.render(sb));
            centerObject.render(sb);

            // Render the selected one on top
            if (selectedIndex != -1)
                objects.get(selectedIndex).render(sb);
        }
    }
}
