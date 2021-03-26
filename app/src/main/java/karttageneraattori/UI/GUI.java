package karttageneraattori.UI;

import java.util.Random;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import karttageneraattori.Logic.Map;
import karttageneraattori.Logic.Generator;
import karttageneraattori.Logic.TileType;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.shape.StrokeType;


public class GUI extends Application {

    private static int tileSize = 5;
    private static int startingDimension = 50;
    private static int pad = 10;

    private StackPane mapField(int width, int height) {
        StackPane pane = new StackPane();
        Group mapField = new Group();
        Rectangle base = new Rectangle(0, 0,
            width * tileSize * 1.5 + tileSize * 0.57 + 2,
            height * tileSize * Math.sqrt(3) + tileSize + 2);
        base.setFill(Color.DARKBLUE);
        pane.getChildren().add(base);
        pane.setAlignment(base, Pos.TOP_LEFT);
        pane.setAlignment(mapField, Pos.TOP_LEFT);


        Map map = new Map(width, height);
        Generator gen = new Generator(2, new Random());
        gen.initMap(map);
        TileType inspected;
        
        int row = 0;
        int col = 0;
        for (double y = 0; row < height; y += tileSize * Math.sqrt(3)) {
            for (double x = 0, dy = y; col < width; x += 1.5 * tileSize) {
                inspected = map.getMap()[col][row].getType();
                col++;
                Polygon tile = new Polygon();
                tile.getPoints().addAll(new Double[]{
                    x, dy,
                    x + tileSize, dy,
                    x + tileSize * 1.5, dy + tileSize * Math.sqrt(3) * 0.5,
                    x + tileSize, dy + tileSize * Math.sqrt(3),
                    x, dy + tileSize * Math.sqrt(3),
                    x - (tileSize * 0.5), dy + tileSize * Math.sqrt(3) * 0.5
                });
                if (inspected == TileType.SEA) {
                    tile.setFill(Color.DARKBLUE);
                } else if (inspected == TileType.SAND) {
                    tile.setFill(Color.MOCCASIN);
                } else {
                    // Something has gone wrong
                    tile.setFill(Color.RED);
                }
                double strokeWidth = tileSize * 0.07;
                if (strokeWidth < 1) {
                    strokeWidth = 1;
                }
                tile.setStrokeWidth(strokeWidth);
                tile.setStrokeType(StrokeType.CENTERED);
                tile.setStrokeLineJoin(StrokeLineJoin.ROUND);
                tile.setStroke(tile.getFill());
                mapField.getChildren().add(tile);
                dy = dy == y ? dy + tileSize * Math.sqrt(3) * 0.5 : y;
            }
            row++;
            col = 0;
        }

        pane.getChildren().add(mapField);
        return pane;
    }


    @Override
    public void start(Stage primaryStage) {
        
        HBox root = new HBox();
        root.setPadding(new Insets(0, pad * 2, pad * 2, 0));
        VBox controls = new VBox();
        controls.setPadding(new Insets(pad, pad, pad, pad));
        controls.setSpacing(pad);

        Label widthLabel = new Label("Width");
        TextField insertWidth = new TextField();
        Label heightLabel = new Label("Height");
        TextField insertHeight = new TextField();
        Button confirmBtn = new Button("Generate");
        Label errorMsg = new Label();

        controls.getChildren().addAll(widthLabel, insertWidth,
            heightLabel, insertHeight, confirmBtn, errorMsg);

        root.getChildren().add(controls);
        StackPane mapField = mapField(startingDimension, startingDimension);
        root.getChildren().add(mapField);

        double sceneX = startingDimension * tileSize * 1.5
            + tileSize * 0.57 + 150 + pad * 4;
        double sceneY = startingDimension * tileSize * Math.sqrt(3)
            + tileSize + pad * 2;

        controls.setMinWidth(150 + pad * 2);
        controls.setMaxWidth(150 + pad * 2);
        controls.setMinHeight(sceneY);
        controls.setMaxHeight(sceneY);

        Scene scene = new Scene(root, sceneX, sceneY);
        

        confirmBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                boolean ok = true;
                int x = 100;
                try {
                    x = Integer.parseInt(insertWidth.getText());
                } catch (NumberFormatException ex) {
                    errorMsg.setText("Enter an integer value");
                    ok = false;
                }
                int y = 100;
                try {
                    y = Integer.parseInt(insertHeight.getText());
                } catch (NumberFormatException ex) {
                    errorMsg.setText("Enter an integer value");
                    ok = false;
                }
                if (ok && (x > 250  || y > 250)) {
                    errorMsg.setText("Values are too high");
                    ok = false;
                }
                if (ok) {
                    errorMsg.setText("");
                    StackPane mField = mapField(x, y);
                    root.getChildren().remove(1);
                    root.getChildren().add(mField);
                    double width = x * tileSize * 1.5
                        + tileSize * 0.57 + 150 + pad * 4;
                    primaryStage.setWidth(width);
                    double height = (y * tileSize * Math.sqrt(3)
                        + tileSize * 2.5) + pad * 3.5;
                    if (height < 200) {
                        height = 200;
                    }
                    primaryStage.setHeight(height);
                }
            }
        });

        primaryStage.setTitle("Hello Map!");
        primaryStage.setScene(scene);
        
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
