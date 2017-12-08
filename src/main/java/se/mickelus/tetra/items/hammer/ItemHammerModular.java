package se.mickelus.tetra.items.hammer;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import se.mickelus.tetra.blocks.workbench.BlockWorkbench;
import se.mickelus.tetra.items.ItemModular;
import se.mickelus.tetra.module.BasicSchema;
import se.mickelus.tetra.module.ItemUpgradeRegistry;
import se.mickelus.tetra.module.RepairSchema;
import se.mickelus.tetra.network.PacketPipeline;


public class ItemHammerModular extends ItemModular {

    public final static String headKey = "head";

    public final static String handleKey = "handle";
    public final static String bindingKey = "binding";

    private static final String unlocalizedName = "hammer_modular";

    public ItemHammerModular() {

        setUnlocalizedName(unlocalizedName);
        setRegistryName(unlocalizedName);
        setMaxStackSize(1);

        majorModuleNames = new String[]{"Head", "Handle"};
        majorModuleKeys = new String[]{headKey, handleKey};
        minorModuleNames = new String[]{"Binding"};
        minorModuleKeys = new String[]{bindingKey};

        new BasicHeadModule(headKey);
        new BasicHandleModule(handleKey);
    }


    @Override
    public void init(PacketPipeline packetPipeline) {

        new BasicSchema("basic_head_schema", BasicHeadModule.instance, this);
        new BasicSchema("basic_handle_schema", BasicHandleModule.instance, this);

        new RepairSchema(this);

        ItemUpgradeRegistry.instance.registerPlaceholder(this::replaceHammer);
    }

    private ItemStack replaceHammer(ItemStack originalStack) {
        if (originalStack.getItem() instanceof ItemHammerBasic) {
            return createDefaultStack(originalStack);
        }

        return null;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(CreativeTabs creativeTabs, NonNullList<ItemStack> itemList) {
        if (isInCreativeTab(creativeTabs)) {
            itemList.add(createDefaultStack(null));
        }
    }

    private ItemStack createDefaultStack(ItemStack originalStack) {
        ItemStack itemStack = new ItemStack(this);

        if (originalStack != null) {
            itemStack.setItemDamage(originalStack.getItemDamage());
        }
        BasicHeadModule.instance.addModule(itemStack, new ItemStack[]{new ItemStack(Blocks.LOG)}, false);
        BasicHandleModule.instance.addModule(itemStack, new ItemStack[]{new ItemStack(Items.STICK)}, false);
        return itemStack;
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack itemStack = player.getHeldItem(hand);
        if (!player.canPlayerEdit(pos.offset(facing), facing, itemStack)) {
            return EnumActionResult.FAIL;
        }

        if (world.getBlockState(pos).getBlock().equals(Blocks.CRAFTING_TABLE)) {

            world.playSound(player, pos, SoundEvents.BLOCK_WOOD_PLACE, SoundCategory.BLOCKS, 1.0F, 1.0F);

            if (!world.isRemote) {
                world.setBlockState(pos, BlockWorkbench.instance.getDefaultState());
            }
            return EnumActionResult.SUCCESS;
        }

        return EnumActionResult.PASS;
    }
}
