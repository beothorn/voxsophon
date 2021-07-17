package br.com.isageek.automata.testSupport;

import br.com.isageek.automata.automata.AutomataStartState;
import br.com.isageek.automata.automata.states.AutomataSearch;
import br.com.isageek.automata.forge.BlockStateHolder;
import br.com.isageek.automata.forge.WorldController;
import net.minecraft.util.math.BlockPos;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import static br.com.isageek.automata.forge.BlockStateHolder.block;

public class FakeWorld extends WorldController {

    public static final String WATER = "Water";
    public static final String LAVA = "Lava";
    public static final String TNT = "tnt";
    public static final String STONE = "stone";
    public static final String AUTOMATA = "Automata";
    public static final String AUTOMATA_START = "AutomataStart";
    public static final String AUTOMATA_PLACEHOLDER = "AutomataPlaceholder";
    public static final String AIR_PLACEHOLDER = "AirPlaceholder";
    public static final String WATER_PLACEHOLDER = "WaterPlaceholder";
    public static final String LAVA_PLACEHOLDER = "LavaPlaceholder";
    public static final String TNT_PLACEHOLDER = "TntPlaceholder";
    public static final String BEDROCK_PLACEHOLDER = "BedrockPlaceholder";
    public static final String AUTOMATA_Y_ROTATION = "AutomataYRotation";
    public static final String TERMINATOR = "Terminator";
    public static final String BEDROCK = "Bedrock";
    public static final String AIR = "air";
    public static final String CAVE_AIR = "caveAir";
    public static final String ANY = "air";

    private final BlockStateHolder[][][] fakeWorld;

    private final boolean[][][] redstoneSignal;

    private final Map<BlockPos, FakeTileEntity> entitiesOnPositions = new LinkedHashMap<>();

    private static final int WORLD_CENTER = 100;

    public FakeWorld() {
        super(
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null
        );
        fakeWorld = new BlockStateHolder[WORLD_CENTER * 2][WORLD_CENTER * 2][WORLD_CENTER * 2];
        for (int x = 0; x < WORLD_CENTER * 2; x++) {
            for (int y = 0; y < WORLD_CENTER * 2; y++) {
                for (int z = 0; z < WORLD_CENTER * 2; z++) {
                    fakeWorld[x][y][z] = block(AIR);
                }
            }
        }
        redstoneSignal = new boolean[WORLD_CENTER * 2][WORLD_CENTER * 2][WORLD_CENTER * 2];
    }

    public void tick(){
        Set<Map.Entry<BlockPos, FakeTileEntity>> entries = entitiesOnPositions.entrySet();
        for (Map.Entry<BlockPos, FakeTileEntity> entry : entries) {
            BlockPos coord = entry.getKey();
            FakeTileEntity automataTileEntity = entry.getValue();
            automataTileEntity.tick(coord, this);
        }
    }

    public void setAt(BlockPos p, String id){
        setAt(p.getX(), p.getY(), p.getZ(), id);
    }

    public void setAt(int x, int y, int z, String id){
        fakeWorld[WORLD_CENTER +x][WORLD_CENTER +y][WORLD_CENTER +z] = block(id);
        if(id.equals(AUTOMATA_START)){
            FakeTileEntity automataTileEntity = new FakeTileEntity(new AutomataSearch());
            entitiesOnPositions.put(new BlockPos(x, y, z), automataTileEntity);
        }else{
            entitiesOnPositions.remove(new BlockPos(x, y, z));
        }
    }

    @Override
    public void setBlock(BlockStateHolder blockState, BlockPos p) {
        setAt(p.getX(), p.getY(), p.getZ(), blockState.descriptionId);
    }

    public void setSurrounding(BlockPos p, String[][][] surroundingIds){
        setSurrounding(p.getX(), p.getY(), p.getZ(), surroundingIds);
    }

    public void setSurrounding(int x, int y, int z, String[][][] surroundingIds){
        for (int ix = -1; ix <= 1; ix++) {
            for (int iy = -1; iy <= 1; iy++) {
                for (int iz = -1; iz <= 1; iz++) {
                    fakeWorld[x+ix+ WORLD_CENTER][y+iy+ WORLD_CENTER][z+iz+ WORLD_CENTER] = block(surroundingIds[ix + 1][iy + 1][iz + 1]);
                }
            }
        }
    }

    @Override
    public boolean isTerminator(BlockPos p) {
        return getAt(p.getX(), p.getY(), p.getZ()).equals(TERMINATOR);
    }

    @Override
    public boolean isAutomata(BlockPos p) {
        return getAt(p.getX(), p.getY(), p.getZ()).equals(AUTOMATA);
    }

    @Override
    public boolean isBedrock(BlockPos p) {
        return getAt(p.getX(), p.getY(), p.getZ()).equals(BEDROCK);
    }

    @Override
    public boolean isAutomataPlaceholder(BlockStateHolder blockStateHolder) {
        return blockStateHolder.descriptionId.equals(AUTOMATA_PLACEHOLDER);
    }

    @Override
    public boolean isAirPlaceholder(BlockStateHolder blockStateHolder) {
        return blockStateHolder.descriptionId.equals(AIR_PLACEHOLDER);
    }

    @Override
    public boolean isWaterPlaceholder(BlockStateHolder blockStateHolder) {
        return blockStateHolder.descriptionId.equals(WATER_PLACEHOLDER);
    }

    @Override
    public boolean isLavaPlaceholder(BlockStateHolder blockStateHolder) {
        return blockStateHolder.descriptionId.equals(LAVA_PLACEHOLDER);
    }

    @Override
    public boolean isTntPlaceholder(BlockStateHolder blockStateHolder) {
        return blockStateHolder.descriptionId.equals(TNT_PLACEHOLDER);
    }

    @Override
    public boolean isBedrockPlaceholder(BlockStateHolder blockStateHolder) {
        return blockStateHolder.descriptionId.equals(BEDROCK_PLACEHOLDER);
    }

    @Override
    public boolean isYRotation(BlockStateHolder blockStateHolder) {
        return blockStateHolder.descriptionId.equals(AUTOMATA_Y_ROTATION);
    }

    @Override
    public boolean isAny(BlockStateHolder blockStateHolder) {
        return blockStateHolder.descriptionId.equals(AIR) || blockStateHolder.descriptionId.equals(CAVE_AIR);
    }

    @Override
    public BlockStateHolder[] surrounding(BlockPos center) {
        int x = center.getX();
        int y = center.getY();
        int z = center.getZ();
        BlockStateHolder[] result = new BlockStateHolder[27];
        int i = 0;
        for (int ix = -1; ix <= 1; ix++) {
            for (int iy = -1; iy <= 1; iy++) {
                for (int iz = -1; iz <= 1; iz++) {
                    result[i++] = fakeWorld[x+ix+ WORLD_CENTER][y+iy+ WORLD_CENTER][z+iz+ WORLD_CENTER];
                }
            }
        }

        return result;
    }

    @Override
    public BlockStateHolder replacePlaceholder(BlockStateHolder blockState) {
        if(isAirPlaceholder(blockState)) return block(AIR);
        if(isWaterPlaceholder(blockState)) return block(WATER);
        if(isLavaPlaceholder(blockState)) return block(LAVA);
        if(isTntPlaceholder(blockState)) return block(TNT);
        if(isBedrockPlaceholder(blockState)) return block(BEDROCK);
        if(isAutomataPlaceholder(blockState)) return block(AUTOMATA);
        return blockState;
    }

    public String[][][] getSurroundingIds(BlockPos p) {
        return getSurroundingIds(p.getX(), p.getY(), p.getZ());
    }

    public String[][][] getSurroundingIds(int x, int y, int z) {
        String[][][] result = new String[3][3][3];
        for (int ix = -1; ix <= 1; ix++) {
            for (int iy = -1; iy <= 1; iy++) {
                for (int iz = -1; iz <= 1; iz++) {
                    result[ix+1][iy+1][iz+1] = fakeWorld[x+ix+ WORLD_CENTER][y+iy+ WORLD_CENTER][z+iz+ WORLD_CENTER].descriptionId;
                }
            }
        }
        return result;
    }

    public String getAt(int x, int y, int z) {
        if(
                x <= -WORLD_CENTER
                || x >= WORLD_CENTER
                || y <= -WORLD_CENTER
                || y >= WORLD_CENTER
                || z <= -WORLD_CENTER
                || z >= WORLD_CENTER
        ) return AIR;
        return fakeWorld[x+ WORLD_CENTER][y+ WORLD_CENTER][z+ WORLD_CENTER].descriptionId;
    }

    public void redSignalAt(BlockPos p, boolean signalState) {
        redSignalAt(p.getX(), p.getY(), p.getZ(), signalState);
    }

    public void redSignalAt(int x, int y, int z, boolean signalState) {
        redstoneSignal[WORLD_CENTER + x][WORLD_CENTER + y][WORLD_CENTER + z] = signalState;
    }

    public boolean hasNeighborSignal(BlockPos p) {
        return redstoneSignal[WORLD_CENTER + p.getX()][WORLD_CENTER + p.getY()][WORLD_CENTER + p.getZ()];
    }

    public BlockStateHolder getAutomata() { return block(AUTOMATA); }
    public BlockStateHolder getAir() { return block(AIR); }

    public void setStateAt(BlockPos pos, AutomataStartState state){
    }
}
