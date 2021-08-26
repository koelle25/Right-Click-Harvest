package info.tehnut.harvest.config;

import com.google.common.collect.Lists;
import info.tehnut.harvest.Crop;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import net.minecraft.block.Blocks;
import net.minecraft.state.property.Properties;

import java.util.List;

@Config(name = "right-click-harvest")
public class HarvestConfig implements ConfigData {

    public float exhaustionPerHarvest = 0.005f;

    public boolean additionalLogging = false;

    @ConfigEntry.Gui.CollapsibleObject
    public CropsConfig crops = new CropsConfig();

    static class CropsConfig {
        public boolean wheatEnabled = true;

        @ConfigEntry.BoundedDiscrete(min = 0, max = 7)
        public int wheatStage = 7;

        public boolean netherWartEnabled = true;

        @ConfigEntry.BoundedDiscrete(min = 0, max = 3)
        public int netherWartStage = 3;

        public boolean carrotEnabled = true;

        @ConfigEntry.BoundedDiscrete(min = 0, max = 7)
        public int carrotStage = 7;

        public boolean potatoEnabled = true;

        @ConfigEntry.BoundedDiscrete(min = 0, max = 7)
        public int potatoStage = 7;

        public boolean beetrootEnabled = true;

        @ConfigEntry.BoundedDiscrete(min = 0, max = 3)
        public int beetrootStage = 3;
    }

    public List<Crop> getCrops() {
        List<Crop> crops = Lists.newArrayList();

        if (this.crops.wheatEnabled) {
            crops.add(new Crop(Blocks.WHEAT.getDefaultState().with(Properties.AGE_7, this.crops.wheatStage)));
        }

        if (this.crops.netherWartEnabled) {
            crops.add(new Crop(Blocks.NETHER_WART.getDefaultState().with(Properties.AGE_3, this.crops.netherWartStage)));
        }

        if (this.crops.carrotEnabled) {
            crops.add(new Crop(Blocks.CARROTS.getDefaultState().with(Properties.AGE_7, this.crops.carrotStage)));
        }

        if (this.crops.potatoEnabled) {
            crops.add(new Crop(Blocks.POTATOES.getDefaultState().with(Properties.AGE_7, this.crops.potatoStage)));
        }

        if (this.crops.beetrootEnabled) {
            crops.add(new Crop(Blocks.BEETROOTS.getDefaultState().with(Properties.AGE_3, this.crops.beetrootStage)));
        }

        return crops;
    }
}
