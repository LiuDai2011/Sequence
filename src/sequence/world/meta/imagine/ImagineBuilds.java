package sequence.world.meta.imagine;

import arc.math.Mathf;
import mindustry.entities.Damage;
import mindustry.gen.Building;

import static mindustry.Vars.tilesize;

public class ImagineBuilds {
    public static void checkRevTimer(Building build) {
        BuildingIEc iec = iec(build);
        if (iec.revTimer() < 0f || Mathf.zero(iec.revTimer())) build.kill();
    }

    public static void onKill(Building build) {
        BuildingIEc iec = iec(build);
        Damage.damage(build.team,
                build.x, build.y, (build.block.size + 2) * tilesize,
                iec.ieAmount() * (iec.ieActivity() + 1) * (iec.ieInstability() + 1)
        );
    }

    private static BuildingIEc iec(Building build) {
        if (!(build instanceof BuildingIEc iec)) throw new IllegalArgumentException("Must give a instance of BuildingIEc for argument");
        return iec;
    }
}
