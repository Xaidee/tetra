package se.mickelus.tetra.gui.statbar.getter;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public class TooltipGetterAttackSpeed implements ITooltipGetter {

    private final IStatGetter statGetter;
    private static final String localizationKey = "tetra.stats.speed.tooltip";

    public TooltipGetterAttackSpeed(IStatGetter statGetter) {
        this.statGetter = statGetter;
    }


    @Override
    public String getTooltipBase(PlayerEntity player, ItemStack itemStack) {
        double speed = statGetter.getValue(player, itemStack);
        return I18n.format(localizationKey, String.format("%.2f", 1 / speed), String.format("%.2f", speed));
    }
}