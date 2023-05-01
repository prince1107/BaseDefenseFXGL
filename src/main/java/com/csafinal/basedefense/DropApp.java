/*
 * FXGL - JavaFX Game Library. The MIT License (MIT).
 * Copyright (c) AlmasB (almaslvl@gmail.com).
 * See LICENSE for details.
 */

package com.csafinal.basedefense;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.components.HealthIntComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.input.UserAction;
import com.csafinal.basedefense.components.PlayerComponent;
import com.csafinal.basedefense.data.TowerData;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.util.Duration;

import java.util.List;

import static com.almasb.fxgl.dsl.FXGL.*;
// NOTE: this import above is crucial, it pulls in many useful methods

@SuppressWarnings({"JavadocLinkAsPlainText", "JavadocBlankLines"})
public class DropApp extends GameApplication {

    List<String> levelNames = List.of(
            "level1.json"
    );

    public void onResourceClicked(Entity e) {
        if (e.getProperties().exists("resourceHarvester")) {
            onHarvesterClicked(e);
        } else {
//        towerSelectionBox.setCell(cell);
//        towerSelectionBox.setVisible(true);

            var x = e.getX() > getAppWidth() / 2.0 ? e.getX() - 250 : e.getX();

//        towerSelectionBox.setTranslateX(x);
//        towerSelectionBox.setTranslateY(cell.getY());
        }
    }

    private void onHarvesterClicked(Entity e) {
    }

    /**
     * Types of entities in this game.
     */
        public enum Type {
        BUILDING, ENEMY, PLAYER, TREE, STONE, BULLET

    }

    boolean downPress, upPress, leftPress, rightPress;

    private Entity player;

    private static List<TowerData> towerData;

    private PlayerComponent playerComponent;

    @Override
    protected void initSettings(GameSettings settings) {
        // initialize common game / window settings.
        settings.setTitle("Zombs");
        settings.setVersion("1.0");
        settings.setWidth(2000);
        settings.setHeight(1000);
    }


    public void onCellClicked(Entity cell) {
        // if we already have a tower on this tower base, ignore call
        if (cell.getProperties().exists("tower"))
            return;

//        towerSelectionBox.setCell(cell);
//        towerSelectionBox.setVisible(true);

        var x = cell.getX() > getAppWidth() / 2.0 ? cell.getX() - 250 : cell.getX();

//        towerSelectionBox.setTranslateX(x);
//        towerSelectionBox.setTranslateY(cell.getY());
    }



    @Override
    protected void initGame() {
        getGameWorld().addEntityFactory(new gameFactory());
        loadCurrentLevel();
        loadTowerData();

        player = spawn("player");

        run(() -> spawn("enemy"), Duration.seconds(3));
        run(()-> spawn("building"), Duration.seconds(5));

        playerComponent = player.getComponent(PlayerComponent.class);
        playerComponent.setPlayer(player);
//        player.rota
//        loopBGM("bgm.mp3");

//        loadTowerData();

        // construct UI objects
//        towerSelectionBox = new TowerSelectionBox(towerData);
    }
    @Override
    protected void initInput(){
        getInput().addAction(new UserAction("Left"){
            @Override
            protected void onAction(){
                playerComponent.left();
                leftPress = true;
            }
            @Override
            protected void onActionEnd(){
                leftPress = false;
            }
        }, KeyCode.A);

        getInput().addAction(new UserAction("Right"){
            @Override
            protected void onAction(){
                playerComponent.right();
                rightPress = true;
            }

            @Override
            protected void onActionEnd(){
                rightPress = false;
            }
        }, KeyCode.D);

        getInput().addAction(new UserAction("Down"){
            @Override
            protected void onAction(){
                playerComponent.down();
                downPress = true;
            }
            @Override
            protected void onActionEnd(){
                downPress = false;
            }
        }, KeyCode.S);

        getInput().addAction(new UserAction("Up"){
            @Override
            protected void onAction(){
                playerComponent.up();
                upPress = true;
            }

            @Override
            protected void onActionEnd(){
                upPress = false;
            }
        }, KeyCode.W);
    }

    @Override
    protected void initPhysics() {
        onCollisionBegin(Type.BUILDING, Type.ENEMY, (building, enemy) -> {

            // code in this block is called when there is a collision between Type.BUCKET and Type.DROPLET
            // remove the collided enemy from the game
            enemy.removeFromWorld();

            // play a sound effect located in /resources/assets/sounds/
            play("drop.wav");

            var hp = building.getComponent(HealthIntComponent.class);

            System.out.println(hp.getValue());
            if (hp.getValue() > 1){
                hp.damage(1);
                return;
            }
            building.removeFromWorld();

        });
        onCollision(Type.PLAYER, Type.ENEMY, (player, enemy) -> {
            player.setPosition(checkCollisionLocation(player, enemy));
            play("drop.wav");
        });
    }


    private Point2D checkCollisionLocation(Entity thing1, Entity thing2){
        double xLoc = 0, yLoc = 0;
        if(thing1.getX()<thing2.getRightX()){

            xLoc = thing2.getX()-thing1.getWidth();
            System.out.println(thing1.getX()+" "+thing2.getRightX()+" "+xLoc);
        }
        if(thing1.getRightX()<thing2.getX()){

            xLoc = thing2.getRightX();
            System.out.println(thing1.getRightX()+ " "+thing2.getX()+" "+xLoc);
        }
        if(thing1.getY()<thing2.getBottomY()){
            yLoc = thing2.getBottomY();
            System.out.println(thing1.getY()+" "+thing2.getBottomY()+ " "+xLoc);
        }
        if(thing1.getBottomY()<thing2.getY()){
            yLoc = thing2.getY()-thing1.getHeight();
            System.out.println(thing1.getBottomY()+" "+thing2.getY()+" "+xLoc);
        }
        System.out.println("it checks things");
        Point2D newPoint = new Point2D(xLoc, yLoc);
        System.out.println(newPoint);
        return newPoint;

    }

    @Override
    protected void onUpdate(double tpf) {
    }

    private void loadTowerData() {
        List<String> towerNames = List.of(
                "tower1.json",
                "tower2.json",
                "tower3.json",
                "tower4.json",
                "tower5.json",
                "tower6.json"
        );

        towerData = towerNames.stream()
                .map(name -> getAssetLoader().loadJSON("towers/" + name, TowerData.class).get())
                .toList();
    }

    public static TowerData getTower() {
        return towerData.get(0);
    }

    private void loadCurrentLevel() {
        setLevelFromMap("tmx/td1.tmx");

        getGameWorld().getEntitiesFiltered(e -> e.isType("TiledMapLayer"))
                .forEach(e -> {
                    e.getViewComponent().addOnClickHandler(event -> {
//                        towerSelectionBox.setVisible(false);
                    });
                });
    }


//    public void onTowerSelected(Entity cell, TowerData data) {
////            towerSelectionBox.setVisible(false);
//
//            var tower = spawnWithScale(
//                    "Tower",
//                    new SpawnData(cell.getPosition()).put("towerData", data),
//                    Duration.seconds(0.85),
//                    Interpolators.ELASTIC.EASE_OUT()
//            );
//
//            cell.setProperty("tower", tower);
//        }
//    }



    public static void main(String[] args) {
        launch(args);
    }
}
