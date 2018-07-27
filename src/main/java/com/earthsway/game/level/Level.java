package com.earthsway.game.level;

import com.earthsway.Utilities;
import com.earthsway.game.entities.Entity;
import com.earthsway.game.entities.PlayerMP;
import com.earthsway.game.entities.utilities.EntityType;
import com.earthsway.game.entities.utilities.Vector2i;
import com.earthsway.game.gfx.Screen;
import com.earthsway.game.level.tiles.Tile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Level {

    private byte[] tiles;
    public int width;
    public int height;
    private List<Entity> entities = new ArrayList<>();

    private BufferedImage image;
    private String imagePath;

    public Level(String imagePath) {
        if (imagePath != null) {
            this.imagePath = imagePath;
            this.loadLevelFromFile();
        } else {
            tiles = new byte[width * height];
            this.width = 64;
            this.height = 64;
            this.generateLevel();
        }
    }

    private void loadLevelFromFile(){
        try {
            this.image = ImageIO.read(Level.class.getResource(imagePath));
            this.width = image.getWidth();
            this.height = image.getHeight();
            tiles = new byte[width * height];
            this.loadTiles();
        } catch (IOException e) {
            e.printStackTrace();
            Utilities.errorReport(e, getClass());
        }
    }

    private void loadTiles() {
        int[] tileColors = this.image.getRGB(0, 0, width, height, null, 0, width);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                tileCheck: for(Tile t : Tile.tiles){
                    if(t != null && t.getLevelColor() == tileColors[x + y * width]){
                        this.tiles[x + y * width] = t.getId();
                        break tileCheck;
                    }
                }
            }
        }
    }

    private void saveLevelToFile(){
        try{
            ImageIO.write(this.image, "png", new File(Level.class.getResource(this.imagePath).getFile()));
        } catch (IOException e) {
            e.printStackTrace();
            Utilities.errorReport(e, getClass());
        }
    }

    public void alterTile(int x, int y, Tile newTile){
        this.tiles[x + y * width] = newTile.getId();
        image.setRGB(x, y, newTile.getLevelColor());
    }

    private void generateLevel() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (x * y % 10 < 8/* generate amount*/) {
                    tiles[x + y * width] = Tile.GRASS.getId();
                } else {
                    tiles[x + y * width] = Tile.STONE.getId();
                }
            }
        }
    }

    public synchronized List<Entity> getEntities(){
        return this.entities;
    }

    public synchronized List<EntityType> getEntityTypes(){
        List<EntityType> toReturn = new ArrayList<>();
        for(Entity e : this.entities){
            toReturn.add(e.getEntityType());
        }
        return toReturn;
    }
    
    public void tick(){
        for(Entity e : getEntities()){
            e.tick();
        }
        for(Tile t : Tile.tiles){
            if(t == null) break;
            else t.tick();
        }
    }

    public void renderTiles(Screen screen, int xOffset, int yOffset) {
        if (xOffset < 0) xOffset = 0;
        if (xOffset > ((width << 3) - screen.width)) xOffset = ((width << 3) - screen.width);
        if (yOffset < 0) yOffset = 0;
        if (yOffset > ((height << 3) - screen.height)) yOffset = ((height << 3) - screen.height);

        screen.setOffset(xOffset, yOffset);

        for (int y = (yOffset >> 3); y < (yOffset + screen.height >> 3) + 1; y++) {
            for (int x = (xOffset >> 3); x < (xOffset + screen.width >> 3) + 1; x++) {
                getTile(x,y).render(screen, this, x << 3, y << 3);
            }
        }
    }

    public void renderEntities(Screen screen){
        for(Entity e : getEntities()){
            e.render(screen);
        }
    }

    public Tile getTile(int x, int y) {
        if(0 > x || x >= width || 0 > y || y >= height) return Tile.VOID;
        return Tile.tiles[tiles[x + y * width]];
    }

    public void addEntity(Entity entity){
        this.getEntities().add(entity);
    }

    public void removePlayerMP(String username) {
        int index = 0;
        for(Entity e : getEntities()){
            if(e instanceof PlayerMP && ((PlayerMP) e).getUsername().equals(username)){
                break;
            }
            index++;
        }
        this.getEntities().remove(index);
    }

    private int getPlayerMPIndex(String username){
        int index = 0;
        for(Entity e : getEntities()){
            if(e instanceof PlayerMP && ((PlayerMP) e).getUsername().equals(username)) break;
            index++;
        }
        return index;
    }

    public void movePlayer(String username, int x, int y, int numSteps, boolean isMoving, int movingDir){
        int index = getPlayerMPIndex(username);
        PlayerMP player = (PlayerMP) this.getEntities().get(index);
        player.x = x;
        player.y = y;
        player.setMoving(isMoving);
        player.setNumSteps(numSteps);
        player.setMovingDir(movingDir);
    }

    private Comparator<Node> nodeSorter = new Comparator<Node>() {

        public int compare(Node n0, Node n1) {
            if(n1.fCost < n0.fCost) return +1;
            if(n1.fCost > n0.fCost) return -1;
            return 0;
        }

    };

    public List<Node> findPath(Vector2i start, Vector2i goal){
        List<Node> openList = new ArrayList<Node>();
        List<Node> closedList = new ArrayList<Node>();
        Node current = new Node(start, null, 0, getDistance(start, goal));
        openList.add(current);
        while(openList.size() > 0){
            Collections.sort(openList, nodeSorter);
            current = openList.get(0);
            if(current.tile.equals(goal)){
                List<Node> path = new ArrayList<Node>();
                while (current.parent != null) {
                    path.add(current);
                    current = current.parent;
                }
                openList.clear();
                closedList.clear();
                return path;
            }
            openList.remove(current);
            closedList.add(current);
            for(int i = 0; i < 9; i++){
                if(i == 4) continue;
                int x = current.tile.getX();
                int y = current.tile.getY();
                int xi = (i % 3) - 1;
                int yi = (i / 3) - 1;
                Tile at = getTile(x + xi,y + yi);
                if(at == null) continue;
                if(at.isSolid()) continue;
                Vector2i a = new Vector2i(x + xi, y + yi);
                double gCost = current.gCost + (getDistance(current.tile, a) == 1 ? 1 : 0.95);
                double hCost = getDistance(a, goal);
                Node node = new Node(a, current, gCost, hCost);
                if(isVectorInList(closedList, a) && gCost >= node.gCost) continue;
                if(!isVectorInList(openList, a) || gCost < node.gCost) openList.add(node);
            }
        }
        closedList.clear();
        return null;
    }

    private boolean isVectorInList(List<Node> list, Vector2i vector){
        for(Node n : list){
            if(n.tile.equals(vector)) return true;
        }
        return false;
    }

    private double getDistance(Vector2i tile, Vector2i goal){
        double dx = tile.getX() - goal.getX();
        double dy = tile.getY() - goal.getY();
        return Math.sqrt(dx * dx + dy * dy);
    }

}
