package se.mickelus.tetra.blocks.workbench.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import se.mickelus.tetra.capabilities.Capability;
import se.mickelus.tetra.gui.*;
import se.mickelus.tetra.module.UpgradeSchema;

public class GuiCapabilityIndicator extends GuiElement {

    public static final int hammer = 1;

    private static final String texture = "textures/gui/workbench.png";

    private Capability capability;

    private GuiString levelIndicator;

    public GuiCapabilityIndicator(int x, int y, Capability capability) {
        super(x, y, 16, 16);
        this.capability = capability;

        levelIndicator = new GuiString(10, 10, "");
        addChild(levelIndicator);

        addChild(new GuiTexture(0, 0, 16, 16, capability.ordinal() * 16, 52, texture));
    }

    public void update(EntityPlayer player, UpgradeSchema schema, ItemStack[] materials) {
        int requiredLevel = schema.getRequiredCapabilityLevel(materials, capability);

        setVisible(requiredLevel > 0);

        if (requiredLevel > schema.getCapabilityLevel(player, capability)) {
            levelIndicator.setString("§c" + requiredLevel + "§r");
        } else {
            levelIndicator.setString("§a" + requiredLevel + "§r");
        }
    }
}