package com.csdy.better_soul_stone.client.rendering.renderers;

import com.csdy.better_soul_stone.client.rendering.BetterSoulStoneRenderTypes;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.model.data.ModelData;
import org.jetbrains.annotations.NotNull;

public class BetterRenderer extends BlockEntityWithoutLevelRenderer {
    public final static BetterRenderer INSTANCE = new BetterRenderer(
            Minecraft.getInstance().getBlockEntityRenderDispatcher(),
            Minecraft.getInstance().getEntityModels()
    );

    public BetterRenderer(BlockEntityRenderDispatcher dispatcher, EntityModelSet modelSet) {
        super(dispatcher, modelSet);
    }


    private final ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
    private final RandomSource randomSource = RandomSource.create();

    @Override
    public void renderByItem(@NotNull ItemStack itemStack, @NotNull ItemDisplayContext itemDisplayContext,
                             @NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource,
                             int packedLight, int packedOverlay) {
        var renterType = BetterSoulStoneRenderTypes.getBetter();
        var itemModel = itemRenderer.getModel(itemStack, null, null, 0);
        var quads = itemModel.getQuads(null, null, randomSource, ModelData.EMPTY, null);
        var buffer = multiBufferSource.getBuffer(renterType);
        quads.forEach(quad -> buffer.putBulkData(poseStack.last(), quad, 1, 1, 1, 1, packedLight, packedOverlay, true));
    }
}
