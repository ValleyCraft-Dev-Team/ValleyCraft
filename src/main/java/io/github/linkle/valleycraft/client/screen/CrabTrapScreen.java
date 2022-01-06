package io.github.linkle.valleycraft.client.screen;

import java.util.Random;

import com.mojang.blaze3d.systems.RenderSystem;

import io.github.linkle.valleycraft.ValleyMain;
import io.github.linkle.valleycraft.screen.CrabTrapScreenHandler;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public class CrabTrapScreen extends HandledScreen<CrabTrapScreenHandler> {
    private static final Identifier TEXTURE = new Identifier(ValleyMain.MOD_ID, "textures/gui/crab_trap.png");
    
    private float progress;

    public CrabTrapScreen(CrabTrapScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }
    
    @Override
    protected void init() {
        super.init();
        titleX = (backgroundWidth - textRenderer.getWidth(title)) / 2;
        progress = new Random().nextFloat();
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
        drawMouseoverTooltip(matrices, mouseX, mouseY);
    }

    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;
        
        drawTexture(matrices, x, y, 0, 0, backgroundWidth, backgroundHeight);
        
        if (handler.isInProgress()) {
            progress += delta * 0.03f;
            if (progress > 1f) {
                progress = 0;
            }
            int bar = (int)(progress * 24f);
            this.drawTexture(matrices, x + 61, y + 35, 176, 0, bar + 1, 16);
        } else {
            progress = 0;
        }
    }
}