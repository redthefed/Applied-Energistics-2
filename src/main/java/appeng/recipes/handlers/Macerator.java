/*
 * This file is part of Applied Energistics 2.
 * Copyright (c) 2013 - 2015, AlgorithmX2, All rights reserved.
 *
 * Applied Energistics 2 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Applied Energistics 2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Applied Energistics 2.  If not, see <http://www.gnu.org/licenses/lgpl>.
 */

package appeng.recipes.handlers;


import java.util.List;

import net.minecraft.item.ItemStack;

import appeng.api.exceptions.MissingIngredientException;
import appeng.api.exceptions.RecipeException;
import appeng.api.exceptions.RegistrationException;
import appeng.api.recipes.ICraftHandler;
import appeng.api.recipes.IIngredient;
import appeng.core.AELog;
import appeng.integration.Integrations;
import appeng.integration.abstraction.IIC2;
import appeng.recipes.RecipeHandler;
import appeng.util.Platform;


public class Macerator implements ICraftHandler, IWebsiteSerializer
{

	private IIngredient pro_input;
	private IIngredient[] pro_output;

	@Override
	public void setup( final List<List<IIngredient>> input, final List<List<IIngredient>> output ) throws RecipeException
	{
		if( input.size() == 1 && output.size() == 1 )
		{
			final int outs = output.get( 0 ).size();
			if( input.get( 0 ).size() == 1 && outs == 1 )
			{
				this.pro_input = input.get( 0 ).get( 0 );
				this.pro_output = output.get( 0 ).toArray( new IIngredient[outs] );
				return;
			}
		}
		throw new RecipeException( "Grind must have a single input, and single output." );
	}

	@Override
	public void register() throws RegistrationException, MissingIngredientException
	{
		IIC2 ic2 = Integrations.ic2();
		for( final ItemStack is : this.pro_input.getItemStackSet() )
		{
			try
			{
				ic2.maceratorRecipe( is, this.pro_output[0].getItemStack() );
			}
			catch( final java.lang.RuntimeException err )
			{
				AELog.info( "IC2 not happy - " + err.getMessage() );
			}
		}
	}

	@Override
	public String getPattern( final RecipeHandler h )
	{
		return null;
	}

	@Override
	public boolean canCraft( final ItemStack output ) throws RegistrationException, MissingIngredientException
	{
		return Platform.itemComparisons().isSameItem( this.pro_output[0].getItemStack(), output );
	}
}
