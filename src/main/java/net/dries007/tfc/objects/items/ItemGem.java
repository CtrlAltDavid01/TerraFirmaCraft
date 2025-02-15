/*
 * Work under Copyright. Licensed under the EUPL.
 * See the project README.md and LICENSE.txt for more information.
 */

package net.dries007.tfc.objects.items;

import java.util.EnumMap;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import mcp.MethodsReturnNonnullByDefault;
import net.dries007.tfc.api.capability.size.Size;
import net.dries007.tfc.api.capability.size.Weight;
import net.dries007.tfc.objects.Gem;
import net.dries007.tfc.util.OreDictionaryHelper;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ItemGem extends ItemTFC
{
    private static final EnumMap<Gem, ItemGem> MAP = new EnumMap<>(Gem.class);

    public static ItemGem get(Gem gem)
    {
        return MAP.get(gem);
    }

    public static ItemStack get(Gem ore, Gem.Grade grade, int amount)
    {
        return new ItemStack(MAP.get(ore), amount, grade.ordinal());
    }

    public final Gem gem;

    public ItemGem(Gem gem)
    {
        this.gem = gem;
        if (MAP.put(gem, this) != null) throw new IllegalStateException("There can only be one.");
        setMaxDamage(0);
        setHasSubtypes(true);
        OreDictionaryHelper.register(this, "gem");
        OreDictionaryHelper.register(this, "gem", gem);
        for (Gem.Grade grade : Gem.Grade.values())
        {
            OreDictionaryHelper.registerMeta(this, grade.ordinal(), "gem", gem, grade);
            OreDictionaryHelper.registerMeta(this, grade.ordinal(), "gem", grade);
        }
    }

    @Override
    public String getTranslationKey(ItemStack stack)
    {
        Gem.Grade grade = getGradeFromStack(stack);
        return super.getTranslationKey(stack) + "." + grade.name().toLowerCase();
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
    {
        if (isInCreativeTab(tab))
        {
            for (Gem.Grade grade : Gem.Grade.values())
            {
                items.add(new ItemStack(this, 1, grade.ordinal()));
            }
        }
    }

    @Nonnull
    @Override
    public Size getSize(ItemStack stack)
    {
        return Size.TINY;
    }

    @Nonnull
    @Override
    public Weight getWeight(ItemStack stack)
    {
        return Weight.LIGHT;
    }

    @SuppressWarnings("ConstantConditions")
    private Gem.Grade getGradeFromStack(ItemStack stack)
    {
        return Gem.Grade.valueOf(stack.getItemDamage());
    }
}
