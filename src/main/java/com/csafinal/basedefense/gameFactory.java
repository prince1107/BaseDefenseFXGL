package com.csafinal.basedefense;

import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.HealthIntComponent;
import com.almasb.fxgl.dsl.components.ProjectileComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.csafinal.basedefense.components.PlayerComponent;
import javafx.beans.binding.Bindings;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import static com.almasb.fxgl.dsl.FXGL.*;
import static com.almasb.fxgl.dsl.FXGL.entityBuilder;


public class gameFactory implements EntityFactory{

    @Spawns("player")
    public Entity spawnPlayer(SpawnData data){
        HealthIntComponent hp = new HealthIntComponent(10);
        return entityBuilder()
                .type(DropApp.Type.BUILDING)
                .at(100, 100)
                .with(hp)
                .viewWithBBox("bucket.png")
                .collidable()
                .with(new PlayerComponent())
                .build();
    }

    @Spawns("enemy")
    public Entity spawnDrops(SpawnData data ){
        int xval = FXGLMath.random(0, getAppWidth() - 64);
        int yval = 0;
        int targetx = 200;
        int targety = 200;
        System.out.println(xval);
        return entityBuilder()
                .type(DropApp.Type.ENEMY)
                .at(xval, yval)
                .with(new ProjectileComponent(new Point2D((int) (targetx - xval), (int) (targety - yval)), 150))
                .viewWithBBox("droplet.png")
                .collidable()
                .build();
    }

    @Spawns("tree")
    public Entity newTree(SpawnData data) {
        var rect = new Rectangle(64, 64, Color.GREEN);
        rect.setOpacity(0.25);

        var cell = entityBuilder(data)
                .type(DropApp.Type.TREE)
                .viewWithBBox(rect)
                .onClick(e -> {
                    FXGL.<DropApp>getAppCast().onResourceClicked(e);
                })
                .build();

        rect.fillProperty().bind(
                Bindings.when(cell.getViewComponent().getParent().hoverProperty())
                        .then(Color.DARKGREEN)
                        .otherwise(Color.GREEN)
        );

        return cell;
    }

    @Spawns("stone")
    public Entity newStone(SpawnData data) {
        var rect = new Rectangle(64, 64, Color.GREEN);
        rect.setOpacity(0.25);

        var cell = entityBuilder(data)
                .type(DropApp.Type.TREE)
                .viewWithBBox(rect)
                .onClick(e -> {
                    FXGL.<DropApp>getAppCast().onResourceClicked(e);
                })
                .build();

        rect.fillProperty().bind(
                Bindings.when(cell.getViewComponent().getParent().hoverProperty())
                        .then(Color.DARKGREEN)
                        .otherwise(Color.GREEN)
        );

        return cell;
    }

}
