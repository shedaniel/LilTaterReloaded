package mods.ltr.compat.rei;

import me.shedaniel.rei.api.RecipeHelper;
import me.shedaniel.rei.api.plugins.REIPluginV0;
import mods.ltr.compat.LilTaterMeditationAbility;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.SemanticVersion;
import net.fabricmc.loader.util.version.VersionParsingException;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;

import static mods.ltr.LilTaterReloaded.getId;
import static mods.ltr.registry.LilTaterAtlas.taterAtlas;
import static mods.ltr.util.RenderStateSetup.toTitleCase;
import static mods.ltr.util.RenderStateSetup.validPrefixes;

@Environment(EnvType.CLIENT)
public class LilTaterReloadedREIPlugin implements REIPluginV0 {
    private static final MinecraftClient client = MinecraftClient.getInstance();
    public static final Identifier PLUGIN = getId("rei_plugin");
    public static final Identifier LTR = getId("ltr_catalogue");
    private static boolean SHOW_TATERS = false;

    @Override
    public Identifier getPluginIdentifier() {
        return PLUGIN;
    }

    @Override
    public SemanticVersion getMinimumVersion() throws VersionParsingException {
        return SemanticVersion.parse("3.6.14");
    }

    @Override
    public void registerPluginCategories(RecipeHelper recipeHelper) {
        recipeHelper.registerCategory(new LilTaterReloadedREICategory());
    }

    @Override
    public void registerRecipeDisplays(RecipeHelper recipeHelper) {
        taterAtlas.keySet().stream().sorted().forEach(name -> {
            if (!"lil_tater".equals(name)) {
                recipeHelper.registerDisplay(new LilTaterReloadedREIDisplay(toTitleCase(name), false));
            }
        });
        recipeHelper.registerDisplay(new LilTaterReloadedREIDisplay("Dinnerbone", false));
        recipeHelper.registerDisplay(new LilTaterReloadedREIDisplay("Pahimar", false));
        recipeHelper.registerDisplay(new LilTaterReloadedREIDisplay("Imitater", false));
        recipeHelper.registerDisplay(new LilTaterReloadedREIDisplay("Rotater", false));
        validPrefixes.forEach(prefix -> {
            switch (prefix) {
                case "RGB":
                case "HSV":
                    prefix = prefix + "_palettater";
                    break;
                case "rotated":
                    prefix = "45_" + prefix + "_lil_tater";
                    break;
                case "counter-clockwise":
                    prefix = "0.5_" + prefix + "_rotater";
                    break;
                case "pehkui":
                    prefix = "0.4_" + prefix + "_lil tater";
                    break;
                case "tinted":
                    prefix = "0x946DFF_"+ prefix +"_irritated_lil_tater";
                    break;
                default:
                    prefix = prefix + "_lil_tater";
            }
            recipeHelper.registerDisplay(new LilTaterReloadedREIDisplay(toTitleCase(prefix), true));
        });
    }

    //todo handle
    @Override
    public void registerOthers(RecipeHelper recipeHelper) {
        recipeHelper.registerRecipeVisibilityHandler(((category, display) -> {
            if (!SHOW_TATERS) {
                if (display.getRecipeCategory().equals(LTR)) {
                    ClientPlayerEntity player = client.player;
                    if (player != null) {
                        PlayerEntity playerEntity = player.world.getPlayerByUuid(player.getUuid());
                        if (((LilTaterMeditationAbility) playerEntity.abilities).ltr_hasMeditated()) {
                            SHOW_TATERS=true;
                            return ActionResult.SUCCESS;
                        } else return ActionResult.FAIL;
                    }
                }
            }
            return ActionResult.PASS;
        }));
        recipeHelper.removeAutoCraftButton(LTR);
    }
}
