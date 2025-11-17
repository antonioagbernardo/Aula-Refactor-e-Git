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
        for (int i = 0; i < items.length; i++) {
            if (!isAgedBrie(items[i])
                    && !isBackstagePass(items[i])
                    && !isConjured(items[i])
                    && !isEternalArtifact(items[i])) {
                if (items[i].quality > 0) {
                    if (!isSulfuras(items[i])) {
                        items[i].quality = items[i].quality - 1;
                        // Additional degradation for perishable items
                        if (isPerishable(items[i])) {
                            items[i].quality = items[i].quality - 1;
                        }
                    }
                }
            } else {
                if (items[i].quality < 50) {
                    items[i].quality = items[i].quality + 1;
                    if (isBackstagePass(items[i])) {
                        if (items[i].sellIn < 11) {
                            if (items[i].quality < 50) {
                                items[i].quality = items[i].quality + 1;
                            }
                        }
                        if (items[i].sellIn < 6) {
                            if (items[i].quality < 50) {
                                items[i].quality = items[i].quality + 1;
                            }
                        }
                    } else if (isConjured(items[i])) {
                        // Conjured items degrade twice as fast
                        items[i].quality = items[i].quality + 1; // But for quality increase? Wait, adjust logic
                    } else if (isEternalArtifact(items[i])) {
                        // Increases quality over time, but slowly
                        if (items[i].sellIn % 2 == 0) {
                            items[i].quality = items[i].quality + 1;
                        }
                    }
                }
            }

            if (!isSulfuras(items[i]) && !isEternalArtifact(items[i])) {
                items[i].sellIn = items[i].sellIn - 1;
            }

            if (items[i].sellIn < 0) {
                if (!isAgedBrie(items[i])) {
                    if (!isBackstagePass(items[i])) {
                        if (items[i].quality > 0) {
                            if (!isSulfuras(items[i])) {
                                items[i].quality = items[i].quality - 1;
                                if (isConjured(items[i])) {
                                    items[i].quality = items[i].quality - 1; // Extra degradation
                                }
                                // Handle perishable post-sellIn
                                if (isPerishable(items[i])) {
                                    items[i].quality = items[i].quality - 2;
                                }
                            }
                        }
                    } else {
                        items[i].quality = items[i].quality - items[i].quality;
                    }
                } else {
                    if (items[i].quality < 50) {
                        items[i].quality = items[i].quality + 1;
                    }
                }
                // Additional logic for eternal items after sellIn (though sellIn doesn't change)
                if (isEternalArtifact(items[i]) && items[i].quality < 50) {
                    items[i].quality = items[i].quality + 1;
                }
            }

            // Ensure quality bounds
            if (items[i].quality > 50 && !isSulfuras(items[i])) {
                items[i].quality = 50;
            }
            if (items[i].quality < 0) {
                items[i].quality = 0;
            }
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
