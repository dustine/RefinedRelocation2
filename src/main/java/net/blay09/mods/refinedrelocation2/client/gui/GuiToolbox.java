package net.blay09.mods.refinedrelocation2.client.gui;

import net.blay09.mods.refinedrelocation2.container.ContainerToolbox;
import net.blay09.mods.refinedrelocation2.item.toolbox.ToolboxItemHandler;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

public class GuiToolbox extends GuiContainer {

    private static final ResourceLocation texture = new ResourceLocation("textures/gui/container/hopper.png");

    private final ToolboxItemHandler toolboxInventory;
    private final IInventory playerInventory;

    public GuiToolbox(EntityPlayer entityPlayer, ToolboxItemHandler toolboxInventory) {
        super(new ContainerToolbox(entityPlayer, toolboxInventory));
        this.toolboxInventory = toolboxInventory;
        playerInventory = entityPlayer.inventory;
        allowUserInput = false;
        ySize = 133;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1f, 1f, 1f, 1f);
        mc.getTextureManager().bindTexture(texture);
        int left = (width - xSize) / 2;
        int top = (height - ySize) / 2;
        drawTexturedModalRect(left, top, 0, 0, xSize, ySize);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        fontRendererObj.drawString(toolboxInventory.getDisplayName().getUnformattedText(), 8, 6, 4210752);
        fontRendererObj.drawString(playerInventory.getDisplayName().getUnformattedText(), 8, ySize - 96 + 2, 4210752);
    }

}
