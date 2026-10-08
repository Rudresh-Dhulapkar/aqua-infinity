package com.ethereal.aquaffinity;

import net.fabricmc.api.ModInitializer;

import net.minecraft.resources.Identifier;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AquaAffinity implements ModInitializer {
	public static final String MOD_ID = "aqua-affinity";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {

		var tickCounter = new int[]{0};

		ServerTickEvents.END_SERVER_TICK.register(server -> {

			tickCounter[0]++;

			if (tickCounter[0] >= 20) {
				tickCounter[0] = 0;

				var players = server.getPlayerList().getPlayers();
				for (var player : players) {
					var helmet = player.getItemBySlot(EquipmentSlot.HEAD);

					var enchantment = player.level()
							.registryAccess()
							.lookupOrThrow(Registries.ENCHANTMENT)
							.getOrThrow(Enchantments.AQUA_AFFINITY);

					var aquaAffinityLevel = EnchantmentHelper.getItemEnchantmentLevel(enchantment, helmet);

					if (aquaAffinityLevel > 0) {
						var waterPosition = player.blockPosition().above();

						player.level().setBlock(
								waterPosition,
								Blocks.WATER.defaultBlockState(),
								3
						);

						LOGGER.info("Water Spqwned");
					}
				}
			}
		});
	}

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}
}
