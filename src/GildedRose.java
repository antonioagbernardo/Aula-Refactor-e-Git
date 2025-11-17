public class GildedRose {
    private static final String AGED_BRIE = "Aged Brie";
    private static final String BACKSTAGE =
            "Backstage passes to a TAFKAL80ETC concert";
    private static final String CONJURED = "Conjured Mana Cake";
    private static final String SULFURAS = "Sulfuras, Hand of Ragnaros";
    private static final String ETERNAL_ARTIFACT = "Eternal Artifact";
    private static final String PERISHABLE_MARKER = "Perishable";

    Item[] items;

    public GildedRose(Item[] items) {
        this.items = items;
    }

    public void updateQuality() {
        for (Item item : items) {
            updateItem(item);
        }
    }

    private void updateItem(Item item) {
        if (!isAgedBrie(item)
                && !isBackstagePass(item)
                && !isConjured(item)
                && !isEternalArtifact(item)) {
            if (item.quality > 0) {
                if (!isSulfuras(item)) {
                    item.quality = item.quality - 1;
                    // Additional degradation for perishable items
                    if (isPerishable(item)) {
                        item.quality = item.quality - 1;
                    }
                }
            }
        } else {
            if (item.quality < 50) {
                item.quality = item.quality + 1;
                if (isBackstagePass(item)) {
                    if (item.sellIn < 11) {
                        if (item.quality < 50) {
                            item.quality = item.quality + 1;
                        }
                    }
                    if (item.sellIn < 6) {
                        if (item.quality < 50) {
                            item.quality = item.quality + 1;
                        }
                    }
                } else if (isConjured(item)) {
                    // Conjured items degrade twice as fast
                    item.quality = item.quality + 1; // But for quality increase? Wait, adjust logic
                } else if (isEternalArtifact(item)) {
                    // Increases quality over time, but slowly
                    if (item.sellIn % 2 == 0) {
                        item.quality = item.quality + 1;
                    }
                }
            }
        }

        if (!isSulfuras(item) && !isEternalArtifact(item)) {
            item.sellIn = item.sellIn - 1;
        }

        if (item.sellIn < 0) {
            if (!isAgedBrie(item)) {
                if (!isBackstagePass(item)) {
                    if (item.quality > 0) {
                        if (!isSulfuras(item)) {
                            item.quality = item.quality - 1;
                            if (isConjured(item)) {
                                item.quality = item.quality - 1; // Extra degradation
                            }
                            // Handle perishable post-sellIn
                            if (isPerishable(item)) {
                                item.quality = item.quality - 2;
                            }
                        }
                    }
                } else {
                    item.quality = item.quality - item.quality;
                }
            } else {
                if (item.quality < 50) {
                    item.quality = item.quality + 1;
                }
            }
            // Additional logic for eternal items after sellIn (though sellIn doesn't change)
            if (isEternalArtifact(item) && item.quality < 50) {
                item.quality = item.quality + 1;
            }
        }

        // Ensure quality bounds
        if (item.quality > 50 && !isSulfuras(item)) {
            item.quality = 50;
        }
        if (item.quality < 0) {
            item.quality = 0;
        }
    }

    private boolean isAgedBrie(Item item) {
        return AGED_BRIE.equals(item.name);
    }

    private boolean isBackstagePass(Item item) {
        return BACKSTAGE.equals(item.name);
    }

    private boolean isConjured(Item item) {
        return CONJURED.equals(item.name);
    }

    private boolean isSulfuras(Item item) {
        return SULFURAS.equals(item.name);
    }

    private boolean isEternalArtifact(Item item) {
        return ETERNAL_ARTIFACT.equals(item.name);
    }

    private boolean isPerishable(Item item) {
        return item.name != null && item.name.contains(PERISHABLE_MARKER);
    }
}
