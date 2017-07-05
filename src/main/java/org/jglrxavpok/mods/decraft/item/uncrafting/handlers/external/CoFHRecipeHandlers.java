package org.jglrxavpok.mods.decraft.item.uncrafting.handlers.external;

import org.jglrxavpok.mods.decraft.item.uncrafting.handlers.NBTSensitiveRecipeHandlers.INBTSensitiveRecipeHandler;
import org.jglrxavpok.mods.decraft.item.uncrafting.handlers.RecipeHandlers.RecipeHandler;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

public class CoFHRecipeHandlers
{
	public static class CoverRecipeHandler extends RecipeHandler implements INBTSensitiveRecipeHandler
	{

		private ItemStack inputStack;

		public static Class<? extends IRecipe> recipeClass;

		static
		{
			try
			{
				recipeClass = Class.forName("cofh.thermaldynamics.util.RecipeCover").asSubclass(IRecipe.class);
			}
			catch(ClassNotFoundException ex) { }
		}

		@Override
		public NonNullList<ItemStack> getCraftingGrid(IRecipe r)
		{
			NonNullList<ItemStack> recipeStacks = NonNullList.<ItemStack>withSize(9, ItemStack.EMPTY);

			recipeStacks.set(0, new ItemStack(Item.REGISTRY.getObject(new ResourceLocation("thermaldynamics:thermaldynamics_48")), 1));

			NBTTagCompound tag = this.inputStack.getTagCompound();
			recipeStacks.set(1, new ItemStack(Block.REGISTRY.getObject(new ResourceLocation(tag.getString("Block"))), 1, tag.getInteger("Meta")));

			return recipeStacks;
		}

		@Override
		public void setInputStack(ItemStack stack) { this.inputStack = stack; }

		@Override
		public ItemStack getInputStack() { return this.inputStack; }

	}
}
