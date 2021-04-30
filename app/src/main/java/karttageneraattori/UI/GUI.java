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
import karttageneraattori.Logic.Tile;
import karttageneraattori.Logic.Type;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;


public class GUI extends Application {

    private static int startingDimension = 700;
    private static int pad = 10;
    private Generator gen;
    private PixelWriter pw;

    private Group pixelMap(int width, int height) {
        Group mapField = new Group();
        
        gen.initMap();

        Map map = gen.getMap();

        WritableImage img = new WritableImage(width, height);

        pw = img.getPixelWriter();

        Tile inspected;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                inspected = map.getMap()[x][y];
                if (inspected.getType() == Type.SEA) {
                    pw.setColor(x, y, Color.NAVY);
                } else if (inspected.getType() == Type.LAND) {
                    pw.setColor(x, y, Color.MOCCASIN);
                } else if (inspected.getType() == Type.LAND_BORDER) {
                    pw.setColor(x, y, Color.DARKGOLDENROD);
                } else if (inspected.getType() == Type.LAKE) {
                    pw.setColor(x, y, Color.LIGHTBLUE);
                } else if (inspected.getType() == Type.LAKE_BORDER) {
                    pw.setColor(x, y, Color.SKYBLUE);
                } else if (inspected.getType() == Type.FOREST) {
                    pw.setColor(x, y, Color.FORESTGREEN);
                } else {
                    pw.setColor(x, y, Color.RED);
                }
            }
        }
        ImageView imgView = new ImageView(img);
        mapField.getChildren().add(imgView);
        return mapField;
    }

    private StackPane pixelMapField(int width, int height) {
        StackPane pane = new StackPane();

        gen.newValues();
        gen.newMap(width, height);

        Group mapField = pixelMap(width, height);
        pane.getChildren().add(mapField);
        pane.setAlignment(mapField, Pos.TOP_LEFT);

        return pane;
    }

    private double sceneWidth(int x) {
        return x + 150 + pad * 4;
    }

    private double sceneHeight(int y) {
        return y + pad * 2;
    }

    @Override
    public void start(Stage primaryStage) {
        gen = new Generator(new Random());

        HBox root = new HBox();
        root.setPadding(new Insets(0, pad * 2, pad * 2, 0));
        VBox controls = new VBox();
        controls.setPadding(new Insets(pad, pad, pad, pad));
        controls.setSpacing(pad);

        Label widthLabel = new Label("Width");
        TextField insertWidth = new TextField(startingDimension + "");
        Label heightLabel = new Label("Height");
        TextField insertHeight = new TextField(startingDimension + "");
        Button confirmBtn = new Button("Generate");
        Label errorMsg = new Label();

        controls.getChildren().addAll(widthLabel, insertWidth,
            heightLabel, insertHeight, confirmBtn, errorMsg);

        root.getChildren().add(controls);
        StackPane mapField;
        mapField = pixelMapField(startingDimension, startingDimension);
        root.getChildren().add(mapField);

        double sceneX = sceneWidth(startingDimension);
        double sceneY = sceneHeight(startingDimension);

        if (sceneY < 200) {
            sceneY = 200;
        }

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
                if (ok && (x > 2000  || y > 2000)) {
                    errorMsg.setText("Values are too high");
                    ok = false;
                } else if (ok && (x < 100 || y < 100)) {
                    errorMsg.setText("Values are too small");
                }
                if (ok) {
                    errorMsg.setText("");
                    StackPane mField = pixelMapField(x, y);
                    root.getChildren().remove(1);
                    root.getChildren().add(mField);
                    primaryStage.setWidth(sceneWidth(x));
                    double height = sceneHeight(y);
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
