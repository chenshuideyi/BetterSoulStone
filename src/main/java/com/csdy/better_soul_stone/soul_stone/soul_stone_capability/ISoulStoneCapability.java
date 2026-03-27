package com.csdy.better_soul_stone.soul_stone.soul_stone_capability;

public interface ISoulStoneCapability {

    /** 定义所属分组，例如 "ore", "combat" */
    default String getGroupName() { return "general"; }

    /** 基础描述 */
    default String getDescriptionKey() { return ""; }


}
