    package me.mouseman.mousessets.managers;

    import me.mouseman.mousessets.definitions.TriggerType;
    import me.mouseman.mousessets.items.SetItemKeys;
    import me.mouseman.mousessets.sets.ActiveSetState;
    import me.mouseman.mousessets.sets.MilestoneSet;

    import org.bukkit.Material;
    import org.bukkit.entity.EntityType;
    import org.bukkit.entity.Player;
    import org.bukkit.inventory.ItemStack;
    import org.bukkit.persistence.PersistentDataType;
    import org.bukkit.entity.Entity;
    import org.bukkit.plugin.java.JavaPlugin;
    import org.bukkit.scheduler.BukkitTask;

    import java.util.*;

    public class SetManager {

        private final ProgressService progressService;

        private final Map<String, MilestoneSet> setsById;

        private final EnumMap<Material, List<MilestoneSet>> blockMineIndex = new EnumMap<>(Material.class);
        private final EnumMap<Material, List<MilestoneSet>> itemUseIndex = new EnumMap<>(Material.class);
        private final EnumMap<Material, List<MilestoneSet>> itemCraftIndex = new EnumMap<>(Material.class);
        private final EnumMap<Material, List<MilestoneSet>> itemBreakIndex = new EnumMap<>(Material.class);

        private final EnumMap<EntityType, List<MilestoneSet>> entityKillIndex = new EnumMap<>(EntityType.class);
        private final EnumMap<EntityType, List<MilestoneSet>> entityKilledByIndex = new EnumMap<>(EntityType.class);

        private final List<MilestoneSet> genericStatSets = new ArrayList<>();

        private final Map<UUID, Long> pendingUpdates = new HashMap<>();
        private final Map<UUID, ActiveSetState> activeSets = new HashMap<>();

        private final SetItemKeys keys;
        private final JavaPlugin plugin;
        private BukkitTask genericTask;

        public SetManager(
                JavaPlugin plugin,
                ProgressService progressService,
                Map<String, MilestoneSet> sets,
                SetItemKeys keys
        ) {
            this.plugin = plugin;
            this.progressService = progressService;
            this.setsById = sets;
            this.keys = keys;

            for (MilestoneSet set : sets.values()) {
                indexSet(set);
            }

            startGenericStatTask();
        }

        public void handleBlockBreak(Player player, Material block) {
            List<MilestoneSet> sets = blockMineIndex.get(block);
            if (sets == null) return;

            process(player, sets, block, null);
        }

        public void handleEntityKill(Player player, EntityType entity) {
            List<MilestoneSet> sets = entityKillIndex.get(entity);
            if (sets == null) return;

            process(player, sets, null, entity);
        }

        public void handleItemUse(Player player, Material item) {
            List<MilestoneSet> sets = itemUseIndex.get(item);
            if (sets == null) return;

            process(player, sets, item, null);
        }

        public void handleItemBreak(Player player, Material item) {
            List<MilestoneSet> sets = itemBreakIndex.get(item);
            if (sets == null) return;

            process(player, sets, item, null);
        }

        public void handleItemCraft(Player player, Material item) {
            List<MilestoneSet> sets = itemCraftIndex.get(item);
            if (sets == null) return;

            process(player, sets, item, null);
        }

        public void handleEntityKilledBy(Player victim, Entity killerEntity) {
            if (killerEntity == null) return;

            EntityType type = killerEntity.getType();
            List<MilestoneSet> sets = entityKilledByIndex.get(type);

            if (sets == null) return;

            process(victim, sets, null, type);
        }

        private int getStat(Player player, MilestoneSet set, Material material, EntityType entity) {

            switch (set.getTriggerType()) {

                case BLOCK_MINED, ITEM_USED, ITEM_CRAFTED, ITEM_BROKEN -> {
                    if (material == null) return 0;
                    return player.getStatistic(set.getStatistic(), material);
                }

                case ENTITY_KILLED, ENTITY_KILLED_BY -> {
                    if (entity == null) return 0;
                    return player.getStatistic(set.getStatistic(), entity);
                }

                case GENERIC_STAT -> {
                    return player.getStatistic(set.getStatistic());
                }

                default -> {
                    return 0;
                }
            }
        }

        private void startGenericStatTask() {

            long delay = 20L * 10;

            genericTask = plugin.getServer().getScheduler().runTaskTimer(
                    plugin,
                    this::updateGenericStats,
                    delay,
                    delay
            );
        }

        private void updateGenericStats() {

            if (genericStatSets.isEmpty()) return;

            for (Player player : plugin.getServer().getOnlinePlayers()) {

                for (MilestoneSet set : genericStatSets) {

                    int currentStage = progressService.getProgress(
                            player.getUniqueId(),
                            set.getId()
                    );

                    int nextStage = currentStage + 1;

                    int requirement = set.getRequirement(nextStage);
                    if (requirement == -1) continue;

                    int stat = player.getStatistic(set.getStatistic());

                    if (stat >= requirement) {

                        progressService.setProgress(
                                player.getUniqueId(),
                                set.getId(),
                                nextStage
                        );

                        player.sendMessage("Unlocked " + set.getId() + " stage " + nextStage);
                    }
                }
            }
        }

        private void process(
                Player player,
                List<MilestoneSet> sets,
                Material material,
                EntityType entity
        ) {

            for (MilestoneSet set : sets) {

                int currentStage = progressService.getProgress(
                        player.getUniqueId(),
                        set.getId()
                );

                int nextStage = currentStage + 1;

                int requirement = set.getRequirement(nextStage);
                if (requirement == -1) continue;

                int stat = getStat(player, set, material, entity);

                if (stat >= requirement) {

                    progressService.setProgress(
                            player.getUniqueId(),
                            set.getId(),
                            nextStage
                    );

                    player.sendMessage("Unlocked " + set.getId() + " stage " + nextStage);
                }
            }
        }

        private void indexSet(MilestoneSet set) {

            switch (set.getTriggerType()) {

                case BLOCK_MINED, ITEM_USED, ITEM_CRAFTED, ITEM_BROKEN -> {
                    if (set.getMaterialTarget() == null) return;

                    EnumMap<Material, List<MilestoneSet>> map =
                            switch (set.getTriggerType()) {
                                case BLOCK_MINED -> blockMineIndex;
                                case ITEM_USED -> itemUseIndex;
                                case ITEM_CRAFTED -> itemCraftIndex;
                                case ITEM_BROKEN -> itemBreakIndex;
                                default -> throw new IllegalStateException();
                            };

                    map.computeIfAbsent(set.getMaterialTarget(), k -> new ArrayList<>())
                            .add(set);
                }

                case ENTITY_KILLED -> {
                    if (set.getEntityTarget() == null) return;

                    entityKillIndex
                            .computeIfAbsent(set.getEntityTarget(), k -> new ArrayList<>())
                            .add(set);
                }

                case ENTITY_KILLED_BY -> {
                    if (set.getEntityTarget() == null) return;

                    entityKilledByIndex
                            .computeIfAbsent(set.getEntityTarget(), k -> new ArrayList<>())
                            .add(set);
                }

                case GENERIC_STAT -> genericStatSets.add(set);
            }
        }

        private boolean matches(ItemStack item, ItemStack expected) {

            if (item == null || expected == null) return false;
            if (item.getType() != expected.getType()) return false;

            var itemMeta = item.getItemMeta();
            var expectedMeta = expected.getItemMeta();

            if (itemMeta == null || expectedMeta == null) return false;

            var itemData = itemMeta.getPersistentDataContainer();
            var expectedData = expectedMeta.getPersistentDataContainer();

            return itemData.equals(expectedData);
        }

        public boolean isValidSetPiece(ItemStack item, String setId, String piece) {

            if (item == null || !item.hasItemMeta()) return false;

            var pdc = item.getItemMeta().getPersistentDataContainer();

            String id = pdc.get(keys.SET_ID, PersistentDataType.STRING);
            String p = pdc.get(keys.SET_PIECE, PersistentDataType.STRING);

            if (id == null || p == null) return false;

            return setId.equals(id) && piece.equalsIgnoreCase(p);
        }

        private boolean isFullyEquipped(Player player, MilestoneSet set) {

            return isValidSetPiece(player.getInventory().getHelmet(), set.getId(), "helmet")
                    && isValidSetPiece(player.getInventory().getChestplate(), set.getId(), "chestplate")
                    && isValidSetPiece(player.getInventory().getLeggings(), set.getId(), "leggings")
                    && isValidSetPiece(player.getInventory().getBoots(), set.getId(), "boots");
        }

        public void updatePlayerSet(Player player) {

            MilestoneSet matched = null;

            for (MilestoneSet set : setsById.values()) {

                if (isFullyEquipped(player, set)) {
                    matched = set;
                    break;
                }
            }

            activeSets
                    .computeIfAbsent(player.getUniqueId(), k -> new ActiveSetState())
                    .setActiveSet(matched);
        }

        public void reload(Map<String, MilestoneSet> sets) {

            setsById.clear();

            blockMineIndex.clear();
            itemUseIndex.clear();
            itemCraftIndex.clear();
            itemBreakIndex.clear();
            entityKillIndex.clear();
            entityKilledByIndex.clear();
            genericStatSets.clear();

            setsById.putAll(sets);

            for (MilestoneSet set : sets.values()) {
                indexSet(set);
            }

            if (genericTask != null) {
                genericTask.cancel();
            }

            startGenericStatTask();
        }

        public JavaPlugin getPlugin() {
            return plugin;
        }

        public void scheduleUpdate(Player player) {

            UUID uuid = player.getUniqueId();
            long now = System.currentTimeMillis();

            Long last = pendingUpdates.get(uuid);
            if (last != null && now - last < 50) return;

            pendingUpdates.put(uuid, now);

            plugin.getServer().getScheduler().runTask(
                    plugin,
                    () -> updatePlayerSet(player)
            );
        }
    }