package org.jglrxavpok.mods.decraft.item.uncrafting.handlers.external;

import org.apache.commons.lang3.ArrayUtils;
import org.jglrxavpok.mods.decraft.common.config.ModJsonConfiguration;
import org.jglrxavpok.mods.decraft.common.config.ModJsonConfiguration.ItemMapping;
import org.jglrxavpok.mods.decraft.item.uncrafting.handlers.NBTSensitiveRecipeHandlers.INBTSensitiveRecipeHandler;
import org.jglrxavpok.mods.decraft.item.uncrafting.handlers.RecipeHandlers.ShapedOreRecipeHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;


/**
 * Handlers for IRecipe implementations from the Tinker's Construct mod
 *
 */
public class TinkersRecipeHandlers
{

	/**
	 * Handler for Part Builders, Stencil Tables, Tool Forges and Tool Tables
	 *
	 */
	public static class TableRecipeHandler extends ShapedOreRecipeHandler implements INBTSensitiveRecipeHandler
	{

		public static final Class<? extends IRecipe> recipeClass = getRecipeClass("slimeknights.tconstruct.tools.common.TableRecipe");

		private ItemStack inputStack;


		@Override
		public ItemStack[] getCraftingGrid(IRecipe r)
		{
			ItemStack[] result = super.getCraftingGrid(r);

			ItemMapping mapping = ModJsonConfiguration.ITEM_MAPPINGS.get(inputStack);
			if (mapping != null)
			{
				if (mapping.replaceSlots != null)
				{
					ItemStack textureBlock = ItemStack.loadItemStackFromNBT(inputStack.getTagCompound().getCompoundTag("textureBlock"));
					for ( int i = 0 ; i < result.length ; i++ )
					{
						if (ArrayUtils.indexOf(mapping.replaceSlots, i) >= 0)
						{
							result[i] = textureBlock.copy();
						}
					}
				}
			}

			return result;
		}


		@Override
		public void setInputStack(ItemStack stack) { this.inputStack = stack; }

		@Override
		public ItemStack getInputStack() { return this.inputStack; }

	}

}
