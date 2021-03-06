package net.blay09.mods.refinedrelocation2.block;

import net.blay09.mods.refinedrelocation2.RefinedRelocation2;
import net.blay09.mods.refinedrelocation2.tile.TileBlockExtender;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockExtender extends BlockContainer {

    public static final PropertyEnum<EnumFacing> FACING = PropertyEnum.create("facing", EnumFacing.class);
    public static final PropertyBool DOWN = PropertyBool.create("down");
    public static final PropertyBool UP = PropertyBool.create("up");
    public static final PropertyBool NORTH = PropertyBool.create("north");
    public static final PropertyBool SOUTH = PropertyBool.create("south");
    public static final PropertyBool WEST = PropertyBool.create("west");
    public static final PropertyBool EAST = PropertyBool.create("east");

    public BlockExtender() {
        super(Material.iron);
        setRegistryName("block_extender");
        setUnlocalizedName(getRegistryName());
        setCreativeTab(RefinedRelocation2.creativeTab);
        setHardness(3f);
        setResistance(8f);
        GameRegistry.registerBlock(this);
    }

    @Override
    public IBlockState onBlockPlaced(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return getDefaultState().withProperty(FACING, facing.getOpposite());
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, FACING, DOWN, UP, NORTH, SOUTH, WEST, EAST);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(FACING, EnumFacing.getFront(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).ordinal();
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
        TileBlockExtender tileEntity = (TileBlockExtender) world.getTileEntity(pos);
        return state.withProperty(DOWN, tileEntity.hasVisibleConnection(EnumFacing.DOWN))
                    .withProperty(UP, tileEntity.hasVisibleConnection(EnumFacing.UP))
                    .withProperty(NORTH, tileEntity.hasVisibleConnection(EnumFacing.NORTH))
                    .withProperty(SOUTH, tileEntity.hasVisibleConnection(EnumFacing.SOUTH))
                    .withProperty(WEST, tileEntity.hasVisibleConnection(EnumFacing.WEST))
                    .withProperty(EAST, tileEntity.hasVisibleConnection(EnumFacing.EAST));
    }

    @Override
    public int getRenderType() {
        return 3;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.CUTOUT;
    }

    @Override
    public boolean canRenderInLayer(EnumWorldBlockLayer layer) {
        return layer == EnumWorldBlockLayer.CUTOUT || layer == EnumWorldBlockLayer.TRANSLUCENT;
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileBlockExtender();
    }

    @SideOnly(Side.CLIENT)
    public void registerModels(ItemModelMesher mesher) {
        Item item = Item.getItemFromBlock(this);
        ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
        mesher.register(item, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }


}
