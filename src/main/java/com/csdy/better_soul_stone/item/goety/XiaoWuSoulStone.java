package com.csdy.better_soul_stone.item.goety;

import com.csdy.better_soul_stone.annotation.SoulStoneItems;
import com.csdy.better_soul_stone.item.BaseSoulStone;
import com.csdy.better_soul_stone.soul_stone.soul_stone_capability.ISoulStoneDoubleClick;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

@SoulStoneItems(id = "xiao_wu_soul_stone",
        parentIds = "gold_soul_stone",
        requiredMod = "goety")
public class XiaoWuSoulStone extends BaseSoulStone implements ISoulStoneDoubleClick {

    @Override
    public void onDoubleClick(ItemStack stack, Player player, String keyType) {

        float yaw = player.getYRot();
        double distance = 4.0;

        Vec3 moveVec = getVec3(keyType, yaw, distance);

        if (moveVec != Vec3.ZERO) {
            player.teleportTo(
                    player.getX() + moveVec.x,
                    player.getY() + moveVec.y,
                    player.getZ() + moveVec.z
            );
        }
    }

    private static @NotNull Vec3 getVec3(String keyType, float yaw, double distance) {
        Vec3 moveVec = Vec3.ZERO;

        Vec3 forward = Vec3.directionFromRotation(0, yaw);
        Vec3 right = Vec3.directionFromRotation(0, yaw + 90);

        switch (keyType) {
            case "key.forward" ->
                    moveVec = forward.scale(distance);
            case "key.back" ->
                    moveVec = forward.scale(-distance);
            case "key.left" ->
                    moveVec = right.scale(-distance);
            case "key.right" ->
                    moveVec = right.scale(distance);
            case "key.jump" ->
                    moveVec = new Vec3(0, distance, 0);
        }
        return moveVec;
    }
}
