package org.example.pixel_art_generator;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.event.EventHandler;
import java.util.ArrayList;
import java.io.*;
public class HelloController {
    @FXML
    private Label welcomeText;
    @FXML
    private Button Open;
    @FXML
    private Button New;
    @FXML
    private Button Up;
    @FXML
    private Button Left;
    @FXML
    private Button Down;
    @FXML
    private Button Right;
    @FXML
    private GridPane Grid;
    @FXML
    private ColorPicker Colour;
    @FXML
    private Button Save;
    @FXML
    private Button Anima;
    @FXML
    private TextField FrameName;

    private ArrayList<String> FrameNames = new ArrayList<String>();
    private int currentFrame = 0;
    private ArrayList<Integer> xDisps = new ArrayList<Integer>();
    private ArrayList<Integer> yDisps = new ArrayList<Integer>();
    private ArrayList<Paint> Colours = new ArrayList<Paint>();
    private ArrayList<ArrayList<int[]>> Maps = new ArrayList<ArrayList<int[]>>();
    private Rectangle[] Pixels = new Rectangle[16*16];

    @FXML
    protected void onOpenButtonClick() {
        FileChooser OpenChooser = new FileChooser();
        FileChooser.ExtensionFilter fileExtension = new FileChooser.ExtensionFilter("select your art file (*.csv)", "*.csv");
        OpenChooser.getExtensionFilters().add(fileExtension);
        File artFile= OpenChooser.showOpenDialog(null);
        if (artFile != null) {
            try {
                BufferedReader br = new BufferedReader(new FileReader(artFile));
                String[] fields = br.readLine().split(",", -1);
                xDisps.add(Integer.parseInt(fields[0]));
                yDisps.add(Integer.parseInt(fields[1]));
                int i = 2;
                while(true){
                    if(Boolean.parseBoolean(fields[i])){break;}
                    else{
                        Colours.add(Paint.valueOf(fields[i]));
                        i++;
                    }
                }
                i++;
                ArrayList<int[]> temp2 = new ArrayList<int[]>();
                while(true){
                    if(fields[i].equals("")){ break;}
                    else{
                        int[] temp = {Integer.parseInt(fields[i]),Integer.parseInt(fields[i+1]),Integer.parseInt(fields[i+2])};
                        temp2.add(temp);
                        i=i+3;
                    }
                }
                Maps.add(temp2);
                Open.setVisible(false);
                New.setVisible(false);
                Up.setVisible(true);
                Left.setVisible(true);
                Down.setVisible(true);
                Right.setVisible(true);
                Colour.setVisible(true);
                Save.setVisible(true);
                int x = 1;
                int y = 1;
                while(y<17){
                    while(x<17){
                        int tempx = x;
                        int tempy = y;
                        Rectangle pixel = new Rectangle();
                        Pixels[(x-1)+((y-1)*16)] = pixel;
                        pixel.setWidth(273/16);
                        pixel.setHeight(416/16);
                        pixel.setFill(Color.GRAY);

                        pixel.setOnMouseEntered(new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent event) {
                                welcomeText.setText(" "+tempx+" "+tempy);
                                pixel.setFill(Color.web(Colour.getValue().toString().replace("0x", "#")));
                                pixel.setOpacity(1);
                            }
                        });

                        pixel.setOnMouseExited(new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent event2) {
                                boolean failed = true;
                                if (Maps.size()>currentFrame) {
                                    for (int i = 0; i < Maps.get(currentFrame).size(); i++) {
                                        if (Maps.get(currentFrame).get(i)[0] == tempx+xDisps.get(currentFrame) && Maps.get(currentFrame).get(i)[1] == tempy+yDisps.get(currentFrame)) {
                                            pixel.setFill(Colours.get(Maps.get(currentFrame).get(i)[2]));
                                            failed = false;
                                            break;
                                        }
                                    }
                                }
                                if (failed){
                                    pixel.setFill(Color.BLACK);
                                    pixel.setOpacity(0);
                                }

                            }
                        });

                        pixel.setOnMouseClicked(new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent event3) {
                                welcomeText.setText(" "+tempx+" "+tempy);
                                if(Maps.size()>currentFrame) {
                                    for (int i = 0; i < Maps.get(currentFrame).size(); i++) {
                                        if (Maps.get(currentFrame).get(i)[0] == tempx + xDisps.get(currentFrame) && Maps.get(currentFrame).get(i)[1] == tempy + yDisps.get(currentFrame)) {
                                            Maps.get(currentFrame).remove(i);
                                        }
                                    }
                                }
                                if(Colours == null){
                                    Colours.add(Color.web(Colour.getValue().toString().replace("0x", "#")));
                                    int[] temp = {tempx+xDisps.get(currentFrame), tempy+yDisps.get(currentFrame), 0};
                                    if(Maps.size()>currentFrame) {
                                        Maps.get(currentFrame).add(temp);
                                    }
                                    else {
                                        ArrayList<int[]> temp2 =new ArrayList<int[]>();
                                        temp2.add(temp);
                                        Maps.add(temp2);
                                    }
                                }
                                else {
                                    boolean failed = true;
                                    for(int i = 0; i<Colours.size();i++){
                                        if(Color.web(Colour.getValue().toString().replace("0x", "#"))==Colours.get(i)){
                                            int[] temp = {tempx+xDisps.get(currentFrame), tempy+yDisps.get(currentFrame), i};
                                            if(Maps.size()>currentFrame) {
                                                Maps.get(currentFrame).add(temp);
                                            }
                                            else {
                                                ArrayList<int[]> temp2 =new ArrayList<int[]>();
                                                temp2.add(temp);
                                                Maps.add(temp2);
                                            }
                                            failed = false;
                                            break;
                                        }
                                    }
                                    if(failed){
                                        Colours.add(Color.web(Colour.getValue().toString().replace("0x", "#")));
                                        int[] temp = {tempx+xDisps.get(currentFrame), tempy+yDisps.get(currentFrame), Colours.size()-1};
                                        if(Maps.size()>currentFrame) {
                                            Maps.get(currentFrame).add(temp);
                                        }
                                        else {
                                            ArrayList<int[]> temp2 =new ArrayList<int[]>();
                                            temp2.add(temp);
                                            Maps.add(temp2);
                                        }
                                    }
                                }
                            }
                        });

                        Grid.add(pixel, tempx, tempy);
                        x++;
                    }
                    y++;
                    x = 1;
                }
                Redraw();

            } catch (IOException ex) {
                System.out.println("Open IO Exception");
            }
        } else {
            System.out.println("Incorrect file");
        }
    }
    @FXML
    protected void onSaveButtonClick() {
        FileChooser SaveChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
        SaveChooser.getExtensionFilters().add(extFilter);
        File file = SaveChooser.showSaveDialog(null);
        if (file != null) {
            try {
                PrintWriter writer = new PrintWriter(file);
                String save = ""+xDisps.get(currentFrame)+","+yDisps.get(currentFrame)+",";
                for(Paint paint : Colours){
                    save = save + paint + ",";
                }
                save = save + true + ",";
                for(int[] Iarr : Maps.get(currentFrame)){
                    save = save + Iarr[0] + "," + Iarr[1] + "," + Iarr[2] + ",";
                }
                writer.print(save);
                writer.close();
            } catch (IOException ex) {
                System.out.println("Save IO Exception");
            }
        }
    }
    @FXML
    protected void onNewButtonClick() {
        Open.setVisible(false);
        New.setVisible(false);
        Anima.setVisible(true);
        Up.setVisible(true);
        Left.setVisible(true);
        Down.setVisible(true);
        Right.setVisible(true);
        Colour.setVisible(true);
        Save.setVisible(true);
        int x = 1;
        int y = 1;
        xDisps.add(0);
        yDisps.add(0);
        while(y<17){
            while(x<17){
                int tempx = x;
                int tempy = y;
                Rectangle pixel = new Rectangle();
                Pixels[(x-1)+((y-1)*16)] = pixel;
                pixel.setWidth(257/16);
                pixel.setHeight(400/16);
                pixel.setFill(Color.GRAY);
                pixel.setOpacity(0);

                pixel.setOnMouseEntered(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        welcomeText.setText(" "+tempx+" "+tempy);
                        pixel.setFill(Color.web(Colour.getValue().toString().replace("0x", "#")));
                        pixel.setOpacity(1);
                    }
                });

                pixel.setOnMouseExited(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event2) {
                        boolean failed = true;
                        if (Maps.size()>currentFrame) {
                            for (int i = 0; i < Maps.get(currentFrame).size(); i++) {
                                if (Maps.get(currentFrame).get(i)[0] == tempx+xDisps.get(currentFrame) && Maps.get(currentFrame).get(i)[1] == tempy+yDisps.get(currentFrame)) {
                                    pixel.setFill(Colours.get(Maps.get(currentFrame).get(i)[2]));
                                    failed = false;
                                    break;
                                }
                            }
                        }
                        if (failed){
                            pixel.setOpacity(0);
                        }

                    }
                });

                pixel.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event3) {
                        welcomeText.setText(" "+tempx+" "+tempy);
                        if(Maps.size()>currentFrame) {
                            for (int i = 0; i < Maps.get(currentFrame).size(); i++) {
                                if (Maps.get(currentFrame).get(i)[0] == tempx + xDisps.get(currentFrame) && Maps.get(currentFrame).get(i)[1] == tempy + yDisps.get(currentFrame)) {
                                    Maps.get(currentFrame).remove(i);
                                }
                            }
                        }
                        if(Colours == null){
                            Colours.add(Color.web(Colour.getValue().toString().replace("0x", "#")));
                            int[] temp = {tempx+xDisps.get(currentFrame), tempy+yDisps.get(currentFrame), 0};
                            if(Maps.size()>currentFrame) {
                                Maps.get(currentFrame).add(temp);
                            }
                            else {
                                ArrayList<int[]> temp2 =new ArrayList<int[]>();
                                temp2.add(temp);
                                Maps.add(temp2);
                            }
                        }
                        else {
                            boolean failed = true;
                            for(int i = 0; i<Colours.size();i++){
                                if(Color.web(Colour.getValue().toString().replace("0x", "#"))==Colours.get(i)){
                                    int[] temp = {tempx+xDisps.get(currentFrame), tempy+yDisps.get(currentFrame), i};
                                    if(Maps.size()>currentFrame) {
                                        Maps.get(currentFrame).add(temp);
                                    }
                                    else {
                                        ArrayList<int[]> temp2 =new ArrayList<int[]>();
                                        temp2.add(temp);
                                        Maps.add(temp2);
                                    }
                                    failed = false;
                                    break;
                                }
                            }
                            if(failed){
                                Colours.add(Color.web(Colour.getValue().toString().replace("0x", "#")));
                                int[] temp = {tempx+xDisps.get(currentFrame), tempy+yDisps.get(currentFrame), Colours.size()-1};
                                if(Maps.size()>currentFrame) {
                                    Maps.get(currentFrame).add(temp);
                                }
                                else {
                                    ArrayList<int[]> temp2 =new ArrayList<int[]>();
                                    temp2.add(temp);
                                    Maps.add(temp2);
                                }
                            }
                        }
                    }
                });

                Grid.add(pixel, tempx, tempy);
                x++;
            }
            y++;
            x = 1;
        }

    }
    @FXML
    protected  void onAnimaButtonClick() {

    };
    @FXML
    protected void onUpButtonClick() {
        yDisps.set(currentFrame,yDisps.get(currentFrame)+1);
        Redraw();
    }
    @FXML
    protected void onLeftButtonClick() {
        xDisps.set(currentFrame, xDisps.get(currentFrame)+1);
        Redraw();
    }
    @FXML
    protected void onDownButtonClick() {
        yDisps.set(currentFrame,yDisps.get(currentFrame)-1);
        Redraw();
    }
    @FXML
    protected void onRightButtonClick() {
        xDisps.set(currentFrame, xDisps.get(currentFrame)-1);
        Redraw();
    }
    void Redraw()
    {
        int x = 1;
        int y = 1;
        while(y<17){
            while(x<17){
                Rectangle pixel = Pixels[(x-1)+((y-1)*16)];
                boolean failed = true;
                if (Maps.size()>currentFrame) {
                    for (int i = 0; i < Maps.get(currentFrame).size(); i++) {
                        if (Maps.get(currentFrame).get(i)[0] == x+xDisps.get(currentFrame) && Maps.get(currentFrame).get(i)[1] == y+yDisps.get(currentFrame)) {
                            pixel.setOpacity(1);
                            pixel.setFill(Colours.get(Maps.get(currentFrame).get(i)[2]));
                            failed = false;
                            break;
                        } else
                        {pixel.setOpacity(0);}
                    }
                }
                if (failed){
                    pixel.setFill(Color.BLACK);
                }
                x++;
            }


            x=1;
            y++;
        }

    }
}