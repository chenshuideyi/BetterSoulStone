package com.csdy.better_soul_stone.client.rendering;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraftforge.client.RenderTypeGroup;

public class BetterSoulStoneRenderTypes extends RenderType {
    public BetterSoulStoneRenderTypes(String name, VertexFormat format, VertexFormat.Mode mode, int bufferSize,
                                      boolean affectsCrumbling, boolean sortOnUpload, Runnable setupState, Runnable clearState) {
        super(name, format, mode, bufferSize, affectsCrumbling, sortOnUpload, setupState, clearState);
    }

    private static RenderType Better;
    private static RenderTypeGroup BetterGroup;

    public static RenderType getBetter() {
        if (Better == null) Better = create(
                "better",
                DefaultVertexFormat.NEW_ENTITY,
                VertexFormat.Mode.QUADS,
                256,
                true,
                false,
                RenderType.CompositeState.builder()
                        //.setShaderState(RenderType.RENDERTYPE_ENTITY_TRANSLUCENT_SHADER)
                        .setShaderState(new RenderStateShard.ShaderStateShard(BetterSoulStoneShaders::setupBetter))
                        .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                        .setCullState(NO_CULL)
                        .setTextureState(new RenderStateShard.TextureStateShard(
                                InventoryMenu.BLOCK_ATLAS, // 【核心】指向方块/物品纹理图集
                                false,
                                false
                        ))
                        .setLightmapState(LIGHTMAP)
                        .setOverlayState(OVERLAY)
                        .createCompositeState(true)
        );
        return Better;
    }

    public static RenderTypeGroup getTinkerMappingGroup() {
        if (BetterGroup == null)
            BetterGroup = new RenderTypeGroup(RenderType.translucent(), getBetter());
        return BetterGroup;
    }
}
