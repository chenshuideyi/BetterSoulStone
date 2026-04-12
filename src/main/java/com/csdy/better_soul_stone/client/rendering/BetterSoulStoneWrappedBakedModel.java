package com.csdy.better_soul_stone.client.rendering;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.ChunkRenderTypeSet;
import net.minecraftforge.client.model.data.ModelData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BetterSoulStoneWrappedBakedModel implements BakedModel {

    public BetterSoulStoneWrappedBakedModel(BakedModel original, RenderType renderType) {

        this.original = original;
        this.renderType = renderType;
    }

    private final BakedModel original;
    private final RenderType renderType;

    @Override public @NotNull List<RenderType> getRenderTypes(@NotNull ItemStack itemStack, boolean fabulous) {
        return List.of(renderType);
    }


    @Override
    public @NotNull List<BakedQuad> getQuads(@Nullable BlockState blockState, @Nullable Direction direction, @NotNull RandomSource randomSource) {
        //noinspection deprecation
        return original.getQuads(blockState, direction, randomSource);
    }

    @Override public boolean useAmbientOcclusion() {
        return original.useAmbientOcclusion();
    }

    @Override public boolean isGui3d() {
        return original.isGui3d();
    }

    @Override public boolean usesBlockLight() {
        return original.usesBlockLight();
    }

    @Override public boolean isCustomRenderer() {
        return original.isCustomRenderer();
    }

    @SuppressWarnings("deprecation") @Override public @NotNull TextureAtlasSprite getParticleIcon() {
        return original.getParticleIcon();
    }

    @SuppressWarnings("deprecation") @Override public @NotNull ItemTransforms getTransforms() {
        return original.getTransforms();
    }

    @Override public @NotNull ItemOverrides getOverrides() {
        return original.getOverrides();
    }

    @Override
    public @NotNull List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull RandomSource rand, @NotNull ModelData data, @Nullable RenderType renderType) {
        return original.getQuads(state, side, rand, data, renderType);
    }

    @Override public boolean useAmbientOcclusion(@NotNull BlockState state) {
        return original.useAmbientOcclusion(state);
    }

    @Override public boolean useAmbientOcclusion(@NotNull BlockState state, @NotNull RenderType renderType) {
        return original.useAmbientOcclusion(state, renderType);
    }

    @Override
    public @NotNull BakedModel applyTransform(@NotNull ItemDisplayContext transformType, @NotNull PoseStack poseStack, boolean applyLeftHandTransform) {
        return new BetterSoulStoneWrappedBakedModel(original.applyTransform(transformType, poseStack, applyLeftHandTransform), renderType);
    }

    @Override
    public @NotNull ModelData getModelData(@NotNull BlockAndTintGetter level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull ModelData modelData) {
        return original.getModelData(level, pos, state, modelData);
    }

    @Override public @NotNull TextureAtlasSprite getParticleIcon(@NotNull ModelData data) {
        return original.getParticleIcon(data);
    }

    @Override
    public @NotNull ChunkRenderTypeSet getRenderTypes(@NotNull BlockState state, @NotNull RandomSource rand, @NotNull ModelData data) {
        return original.getRenderTypes(state, rand, data);
    }
}
