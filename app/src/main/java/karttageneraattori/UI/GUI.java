package karttageneraattori.UI;

import java.util.Random;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
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
import javafx.scene.shape.Rectangle;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;


public class GUI extends Application {

    private static boolean isHexagonal = false;
    private static int tileSize = 3;
    private static int startingDimension = 100;
    private static int pad = 10;
    private Generator gen;
    private PixelWriter pw;


    // Style is unconfirmed; hexagonal style is left here
    // in case it will be chosen later
    private Group hexagonalMap(int width, int height) {
        Group mapField = new Group();
        
        gen.initMap();

        Map map = gen.getMap();

        //HEXAGONAL MAP 2
        Tile inspected;
        Canvas land = new Canvas(width * tileSize * 1.5 + tileSize * 0.57 + 2,
            height * tileSize * Math.sqrt(3) + tileSize + 2);
        Canvas sea = new Canvas(width * tileSize * 1.5 + tileSize * 0.57 + 2,
            height * tileSize * Math.sqrt(3) + tileSize + 2);
        GraphicsContext landContext = land.getGraphicsContext2D();
        GraphicsContext seaContext = sea.getGraphicsContext2D();
        landContext.setFill(Color.MOCCASIN);
        seaContext.setFill(Color.DARKBLUE);
        int row = 0;
        int col = 0;
        for (double y = 0; row < height; y += tileSize * Math.sqrt(3)) {
            for (double x = 0, dy = y; col < width; x += 1.5 * tileSize) {
                inspected = map.getMap()[col][row];
                col++;
                double[] xPoints = {x, x + tileSize, x + tileSize * 1.5,
                    x + tileSize, x, x - (tileSize * 0.5)};
                double[] yPoints = {dy, dy, dy + tileSize * Math.sqrt(3) * 0.5,
                    dy + tileSize * Math.sqrt(3), dy + tileSize * Math.sqrt(3),
                    dy + tileSize * Math.sqrt(3) * 0.5};
                if (inspected.getType() == Type.LAND) {
                    landContext.fillPolygon(xPoints, yPoints, 6);
                } else if (inspected.getType() == Type.SEA) {
                    seaContext.fillPolygon(xPoints, yPoints, 6);
                } else {

                }
                dy = dy == y ? dy + tileSize * Math.sqrt(3) * 0.5 : y;
            }
            row++;
            col = 0;
        }
        
        // // HEXAGONAL MAP 1
        // int row = 0;
        // int col = 0;
        // for (double y = 0; row < height; y += tileSize * Math.sqrt(3)) {
        //     for (double x = 0, dy = y; col < width; x += 1.5 * tileSize) {
        //         inspected = map.getMap()[col][row];
        //         col++;
        //         Polygon tile = new Polygon();
        //         tile.getPoints().addAll(new Double[]{
        //             x, dy,
        //             x + tileSize, dy,
        //             x + tileSize * 1.5, dy + tileSize * Math.sqrt(3) * 0.5,
        //             x + tileSize, dy + tileSize * Math.sqrt(3),
        //             x, dy + tileSize * Math.sqrt(3),
        //             x - (tileSize * 0.5), dy + tileSize * Math.sqrt(3) * 0.5
        //         });
        //         if (inspected.getType() == Type.SEA) {
        //             tile.setFill(Color.DARKBLUE);
        //         } else if (inspected.getType() == Type.LAND) {
        //             tile.setFill(Color.MOCCASIN);
        //         } else if (inspected.getType() == Type.LAKE) {
        //             tile.setFill(Color.LIGHTBLUE);
        //         } else {
        //             // Something has gone wrong
        //             tile.setFill(Color.RED);
        //         }
        //         double strokeWidth = tileSize * 0.07;
        //         if (strokeWidth < 1) {
        //             strokeWidth = 1;
        //         }
        //         tile.setStrokeWidth(strokeWidth);
        //         tile.setStrokeType(StrokeType.CENTERED);
        //         tile.setStrokeLineJoin(StrokeLineJoin.ROUND);
        //         tile.setStroke(tile.getFill());
        //         mapField.getChildren().add(tile);
        //         dy = dy == y ? dy + tileSize * Math.sqrt(3) * 0.5 : y;
        //     }
        //     row++;
        //     col = 0;
        // }

        mapField.getChildren().add(sea);
        mapField.getChildren().add(land);
        return mapField;
    }

    private Group pixelMap(int width, int height) {
        Group mapField = new Group();
        
        gen.initMap();

        Map map = gen.getMap();

        WritableImage img = new WritableImage(width, height);

        pw = img.getPixelWriter();

        Tile inspected;
        // PIXEL MAP
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // int i = y * width + x;
                inspected = map.getMap()[x][y];
                if (inspected.getType() == Type.SEA) {
                    pw.setColor(x, y, Color.DARKBLUE);
                } else if (inspected.getType() == Type.LAND) {
                    pw.setColor(x, y, Color.MOCCASIN);
                } else if (inspected.getType() == Type.LAKE) {
                    pw.setColor(x, y, Color.LIGHTBLUE);
                } else {
                    // Something has gone wrong
                    pw.setColor(x, y, Color.RED);
                }
                double strokeWidth = tileSize * 0.07;
                if (strokeWidth < 1) {
                    strokeWidth = 1;
                }
            }
        }
        ImageView imgView = new ImageView(img);
        mapField.getChildren().add(imgView);
        return mapField;
    }

    private StackPane pixelMapField(int width, int height) {
        StackPane pane = new StackPane();

        Rectangle base = new Rectangle(0, 0, width, height);
        base.setFill(Color.DARKBLUE);
        pane.getChildren().add(base);
        pane.setAlignment(base, Pos.TOP_LEFT);

        gen.newValues();
        gen.newMap(width, height);

        Group mapField = pixelMap(width, height);
        pane.getChildren().add(mapField);
        pane.setAlignment(mapField, Pos.TOP_LEFT);

        return pane;
    }

    private StackPane hexagonMapField(int width, int height) {
        StackPane pane = new StackPane();
        
        Rectangle base = new Rectangle(0, 0,
            width * tileSize * 1.5 + tileSize * 0.57 + 2,
            height * tileSize * Math.sqrt(3) + tileSize + 2);
        base.setFill(Color.DARKBLUE);
        pane.getChildren().add(base);
        pane.setAlignment(base, Pos.TOP_LEFT);

        gen.newValues();
        gen.newMap(width, height);

        Group mapField = hexagonalMap(width, height);
        pane.getChildren().add(mapField);
        pane.setAlignment(mapField, Pos.TOP_LEFT);

        return pane;
    }

    private double sceneWidth(int x) {
        if (isHexagonal) {
            return x * tileSize * 1.5
                + tileSize * 0.57 + 150 + pad * 4;
        } else {
            return x + 150 + pad * 4;
        }
    }

    private double sceneHeight(int y) {
        if (isHexagonal) {
            return (y * tileSize * Math.sqrt(3)
                + tileSize * 2.5) + pad * 3.5;
        } else {
            return y + pad * 2;
        }
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
        if (isHexagonal) {
            mapField = hexagonMapField(startingDimension, startingDimension);
        } else {
            mapField = pixelMapField(startingDimension, startingDimension);
        }
        root.getChildren().add(mapField);

        // Dimensions for hexagonal map
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
                    StackPane mField; 
                    if (isHexagonal) {
                        mField = hexagonMapField(x, y);
                    } else {
                        mField = pixelMapField(x, y);
                    }
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
