package net.blay09.mods.refinedrelocation2;

import mcmultipart.multipart.IMultipart;
import mcmultipart.multipart.IMultipartContainer;
import mcmultipart.multipart.MultipartHelper;
import net.blay09.mods.refinedrelocation2.api.IInternalMethods;
import net.blay09.mods.refinedrelocation2.api.grid.IWorldPos;
import net.blay09.mods.refinedrelocation2.api.capability.ISortingGridMember;
import net.blay09.mods.refinedrelocation2.api.capability.ISortingInventory;
import net.blay09.mods.refinedrelocation2.api.filter.IFilter;
import net.blay09.mods.refinedrelocation2.api.grid.ISortingGrid;
import net.blay09.mods.refinedrelocation2.api.gui.IRootFilterGui;
import net.blay09.mods.refinedrelocation2.client.gui.element.GuiButtonEditFilter;
import net.blay09.mods.refinedrelocation2.filter.RootFilter;
import net.blay09.mods.refinedrelocation2.grid.SortingGrid;
import net.blay09.mods.refinedrelocation2.item.toolbox.ItemToolbox;
import net.blay09.mods.refinedrelocation2.network.MessageOpenFilter;
import net.blay09.mods.refinedrelocation2.network.NetworkHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.IItemHandler;

public class InternalMethods implements IInternalMethods {

    @Override
    public IFilter createRootFilter() {
        return new RootFilter();
    }

    @Override
    public ISortingGridMember createSortingMember(IWorldPos worldPos) {
        ISortingGridMember sortingMember = RefinedRelocation2.SORTING_GRID_MEMBER.getDefaultInstance();
        sortingMember.setWorldPos(worldPos);
        return sortingMember;
    }

    @Override
    public ISortingInventory createSortingInventory(IWorldPos worldPos, IItemHandler itemHandler, boolean useRootFilter) {
        ISortingInventory sortingInventory = RefinedRelocation2.SORTING_INVENTORY.getDefaultInstance();
        sortingInventory.setWorldPos(worldPos);
        sortingInventory.setItemHandler(itemHandler);
        if(useRootFilter) {
            sortingInventory.setFilter(createRootFilter());
        }
        return sortingInventory;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiButton createOpenFilterButton(GuiContainer guiContainer) {
        return new GuiButtonEditFilter(0, guiContainer.guiLeft + guiContainer.xSize - 20, guiContainer.guiTop + 4, true);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void openRootFilterGui(BlockPos blockPos) {
        NetworkHandler.instance.sendToServer(new MessageOpenFilter(blockPos));
    }

    @Override
    public void addToSortingGrid(ISortingGridMember sortingMember) {
        ISortingGrid sortingGrid = sortingMember.getSortingGrid();
        if(sortingGrid != null) {
            return;
        }
        World world = sortingMember.getWorld();
        BlockPos pos = sortingMember.getPos();
        for(EnumFacing facing : EnumFacing.VALUES) {
            IMultipartContainer partContainer = MultipartHelper.getPartContainer(world, pos);
            if(partContainer != null) {
                for(IMultipart part : partContainer.getParts()) {
                    if(part instanceof ICapabilityProvider) {
                        ISortingGridMember otherMember = ((ICapabilityProvider) part).getCapability(RefinedRelocation2.SORTING_GRID_MEMBER, facing.getOpposite());
                        if (otherMember != null && otherMember.getSortingGrid() != null) {
                            if(sortingGrid != null) {
                                ((SortingGrid) sortingGrid).mergeWith(otherMember.getSortingGrid());
                            } else {
                                sortingGrid = otherMember.getSortingGrid();
                            }
                        }
                    }
                }
            } else {
                TileEntity tileEntity = world.getTileEntity(pos.offset(facing));
                if(tileEntity != null) {
                    ISortingGridMember otherMember = tileEntity.getCapability(RefinedRelocation2.SORTING_GRID_MEMBER, facing.getOpposite());
                    if (otherMember != null && otherMember.getSortingGrid() != null) {
                        if(sortingGrid != null) {
                            ((SortingGrid) sortingGrid).mergeWith(otherMember.getSortingGrid());
                        } else {
                            sortingGrid = otherMember.getSortingGrid();
                        }
                    }
                }
            }
        }
        if(sortingGrid == null) {
            sortingGrid = new SortingGrid();
        }
        sortingGrid.addMember(sortingMember);
    }

    @Override
    public void removeFromSortingGrid(ISortingGridMember sortingMember) {
        ISortingGrid sortingGrid = sortingMember.getSortingGrid();
        if(sortingGrid == null) {
            return;
        }
        sortingGrid.removeMember(sortingMember);
    }

    @Override
    public void registerToolboxItem(Item item) {
        ItemToolbox.toolboxRegistry.add(item);
    }

}
