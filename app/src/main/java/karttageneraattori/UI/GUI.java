package karttageneraattori.UI;

import java.util.Random;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import karttageneraattori.Logic.Map;
import karttageneraattori.Logic.Generator;
import karttageneraattori.Logic.Tile;
import karttageneraattori.Logic.Type;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;


public class GUI extends Application {

    private static int startingDimension = 500;
    private int tileSize = 2;
    private static int pad = 10;
    private Generator gen;
    private PixelWriter pw;

    private Group pixelMap(int width, int height) {
        Group mapField = new Group();

        Map map = gen.getMap();

        WritableImage img = new WritableImage(
            width * tileSize, height * tileSize);

        pw = img.getPixelWriter();

        Tile inspected;
        int pixelX = 0;
        int pixelY = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                inspected = map.getMap()[x][y];
                Color c = Color.RED;
                if (inspected.getType() == Type.SEA) {
                    c = Color.NAVY;
                } else if (inspected.getType() == Type.LAND) {
                    c = Color.MOCCASIN;
                } else if (inspected.getType() == Type.LAND_BORDER) {
                    c = Color.DARKGOLDENROD;
                } else if (inspected.getType() == Type.LAKE) {
                    c = Color.LIGHTBLUE;
                } else if (inspected.getType() == Type.LAKE_BORDER) {
                    c = Color.SKYBLUE;
                } else if (inspected.getType() == Type.FOREST) {
                    c = Color.FORESTGREEN;
                }
                for (int i = 0; i < tileSize; i++) {
                    for (int j = 0; j < tileSize; j++) {
                        pw.setColor(pixelX + i, pixelY + j, c);
                    }
                }
                
                pixelX += tileSize;
            }
            pixelX = 0;
            pixelY += tileSize;
        }
        ImageView imgView = new ImageView(img);
        mapField.getChildren().add(imgView);
        return mapField;
    }

    private StackPane pixelMapField(int width, int height,
        double landToSeaRatio, double forestChance,
        double chanceToDiscardSmall) {
        
        StackPane pane = new StackPane();

        gen.setRandomIslandNum();
        gen.setLandToSeaRatio(landToSeaRatio);
        gen.setForestChance(forestChance);
        gen.setChanceToDiscardSmall(chanceToDiscardSmall);

        gen.newMap(width, height);
        gen.initMap();

        Group mapField = pixelMap(width, height);
        pane.getChildren().add(mapField);
        pane.setAlignment(mapField, Pos.TOP_LEFT);

        return pane;
    }

    private StackPane randomPixelMapField(int width, int height) {
        
        StackPane pane = new StackPane();

        gen.newValues();
        gen.newMap(width, height);
        gen.initMap();

        Group mapField = pixelMap(width, height);
        pane.getChildren().add(mapField);
        pane.setAlignment(mapField, Pos.TOP_LEFT);

        return pane;
    }

    private StackPane refreshMap() {
        StackPane pane = new StackPane();

        Group mapField = pixelMap(
            gen.getMap().getWidth(), gen.getMap().getHeight());
        pane.getChildren().add(mapField);
        pane.setAlignment(mapField, Pos.TOP_LEFT);

        return pane;
    }

    private double sceneWidth(int x) {
        return x * tileSize + 180 + pad * 5;
    }

    private double sceneHeight(int y) {
        return y * tileSize + pad * 3;
    }

    @Override
    public void start(Stage primaryStage) {

        gen = new Generator(new Random());
        int minHeight = 570;

        HBox root = new HBox();
        root.setPadding(new Insets(0, pad * 2, pad * 2, 0));
        VBox controls = new VBox();
        controls.setPadding(new Insets(pad, pad, pad, pad));
        controls.setSpacing(pad);

        Label tileSizeLabel = new Label("Zoom Level");
        Slider tileSizeSlider = new Slider(1, 5, 2);
        tileSizeSlider.setShowTickMarks(true);
        tileSizeSlider.setShowTickLabels(true);
        tileSizeSlider.setMajorTickUnit(1);
        tileSizeSlider.setMinorTickCount(0);
        tileSizeSlider.setSnapToTicks(true);

        Button refreshButton = new Button("Refresh");

        Label widthLabel = new Label("Map Width");
        Slider widthSlider = new Slider(100, 1000, startingDimension);
        widthSlider.setShowTickMarks(true);
        widthSlider.setShowTickLabels(true);
        widthSlider.setMajorTickUnit(100);
        widthSlider.setMinorTickCount(0);
        widthSlider.setSnapToTicks(true);

        Label heightLabel = new Label("Map Height");
        Slider heightSlider = new Slider(100, 1000, startingDimension);
        heightSlider.setShowTickMarks(true);
        heightSlider.setShowTickLabels(true);
        heightSlider.setMajorTickUnit(100);
        heightSlider.setMinorTickCount(0);
        heightSlider.setSnapToTicks(true);

        Label landToSeaLabel = new Label("% of land");
        Slider landToSeaSlider = new Slider(0.2, 0.6, 0.3);
        landToSeaSlider.setShowTickMarks(true);
        landToSeaSlider.setShowTickLabels(true);
        landToSeaSlider.setMajorTickUnit(0.1f);
        landToSeaSlider.setMinorTickCount(1);
        landToSeaSlider.setSnapToTicks(true);

        Label forestChanceLabel = new Label("Forestation");
        String[] forestChoices = {
            "Rare", "Some",
            "Plenty", "Abundant"};
        ChoiceBox forestChanceChoice = new ChoiceBox<>(
            FXCollections.observableArrayList(forestChoices));

        Label smallIslandsAndLakesLabel = new Label("Small islands and lakes");
        String[] smallIslandsAndLakesChoices = {
            "Rare", "Some",
            "Plenty", "Abundant"};
        ChoiceBox smallIslandsAndLakesChoice = new ChoiceBox<>(
            FXCollections.observableArrayList(smallIslandsAndLakesChoices));
        
        Button confirmBtn = new Button("Generate");
        Button randomBtn = new Button("Random");
        Label errorMsg = new Label();

        controls.getChildren().addAll(
            tileSizeLabel, tileSizeSlider, refreshButton,
            widthLabel, widthSlider,
            heightLabel, heightSlider, 
            landToSeaLabel, landToSeaSlider,
            forestChanceLabel, forestChanceChoice,
            smallIslandsAndLakesLabel, smallIslandsAndLakesChoice,
            confirmBtn, randomBtn, errorMsg);

        root.getChildren().add(controls);
        StackPane mapField;
        mapField = randomPixelMapField(startingDimension, startingDimension);
        root.getChildren().add(mapField);

        double sceneX = sceneWidth(startingDimension);
        double sceneY = sceneHeight(startingDimension);

        if (sceneY < minHeight) {
            sceneY = minHeight;
        }

        controls.setMinWidth(200 + pad * 2);
        controls.setMaxWidth(200 + pad * 2);
        controls.setMinHeight(sceneY);
        controls.setMaxHeight(sceneY);

        Scene scene = new Scene(root, sceneX, sceneY);
        
        refreshButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                tileSize = (int) tileSizeSlider.getValue();
                StackPane mField = refreshMap();
                root.getChildren().remove(1);
                root.getChildren().add(mField);
                primaryStage.setWidth(sceneWidth(gen.getMap().getWidth()));
                double height = sceneHeight(gen.getMap().getHeight());
                if (height < minHeight) {
                    height = minHeight;
                }
                primaryStage.setHeight(height);
            }
        });

        confirmBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                boolean ok = true;
                int x = (int) widthSlider.getValue();
                int y = (int) heightSlider.getValue();

                if (forestChanceChoice.getValue() == null
                    || smallIslandsAndLakesChoice.getValue() == null) {
                    errorMsg.setText("Make all choices");
                    ok = false;
                }

                if (ok) {
                    errorMsg.setText("");
                    tileSize = (int) tileSizeSlider.getValue();
                    double landToSeaRatio = landToSeaSlider.getValue();
                    double forestChance = forestationValues(
                        (String) forestChanceChoice.getValue());
                    double chanceToDiscardSmall = smallIslandsAndLakesValues(
                        (String) smallIslandsAndLakesChoice.getValue());
                    StackPane mField = pixelMapField(x, y,
                        landToSeaRatio, forestChance, chanceToDiscardSmall);
                    root.getChildren().remove(1);
                    root.getChildren().add(mField);
                    primaryStage.setWidth(sceneWidth(x));
                    double height = sceneHeight(y);
                    if (height < minHeight) {
                        height = minHeight;
                    }
                    primaryStage.setHeight(height);
                }
            }
        });

        randomBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                int x = (new Random().nextInt(10) + 1) * 100;
                int y = (new Random().nextInt(10) + 1) * 100;
                widthSlider.setValue(x);
                heightSlider.setValue(y);

                errorMsg.setText("");
                tileSize = (int) tileSizeSlider.getValue();
                StackPane mField = randomPixelMapField(x, y);
                landToSeaSlider.setValue(gen.getLandToSeaRatio());
                forestChanceChoice.setValue(forestationValues(
                    gen.getForestChance()));
                smallIslandsAndLakesChoice.setValue(smallIslandsAndLakesValues(
                    gen.getChanceToDiscardSmall()));
                
                root.getChildren().remove(1);
                root.getChildren().add(mField);
                primaryStage.setWidth(sceneWidth(x));
                double height = sceneHeight(y);
                if (height < minHeight) {
                    height = minHeight;
                }
                primaryStage.setHeight(height);
                
            }
        });

        primaryStage.setTitle("Map Generator");
        primaryStage.setScene(scene);
        
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    // Helping functions that convert the words in choiceboxes into doubles
    // or vice versa

    private static double forestationValues(String s) {
        if (s.equals("Rare")) {
            return 0.3;
        } else if (s.equals("Some")) {
            return 0.5;
        } else if (s.equals("Plenty")) {
            return 0.7;
        } else if (s.equals("Abundant")) {
            return 0.9;
        } else {
            return 0.6;
        }
    }

    private static String forestationValues(double d) {
        if (d >= 0.3 && d < 0.4) {
            return "Rare";
        } else if (d >= 0.4 && d < 0.6) {
            return "Some";
        } else if (d >= 0.6 && d < 0.8) {
            return "Plenty";
        } else if (d >= 0.8) {
            return "Abundant";
        } else {
            return "Rare";
        }
    }

    private static double smallIslandsAndLakesValues(String s) {
        if (s.equals("Rare")) {
            return 0.8;
        } else if (s.equals("Some")) {
            return 0.6;
        } else if (s.equals("Plenty")) {
            return 0.4;
        } else if (s.equals("Abundant")) {
            return 0.2;
        } else {
            return 0.5;
        }
    }

    private static String smallIslandsAndLakesValues(double d) {
        if (d >= 0.7) {
            return "Rare";
        } else if (d >= 0.5) {
            return "Some";
        } else if (d >= 0.3) {
            return "Plenty";
        } else if (d >= 0.2) {
            return "Abundant";
        } else {
            return "Abundant";
        }
    }
}
