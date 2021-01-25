package com.venegaspiedraalberto.proyecto_breakout;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import static javafx.scene.input.KeyCode.LEFT;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;
import javafx.util.Duration;


/**
 * JavaFX App
 */
public class App extends Application {
    
    int SCENE_TAM_X = 800;
    int SCENE_TAM_Y = 600;
    int ANCHURA_PALA = 70;
    int ALTURA_PALA = 10;
    int ballCenterY = 30;
    int ballCurrentSpeedY =6;
    int ballCenterX = 10;
    int ballCurrentSpeedX =6;
    int stickCurrentSpeed = 0;
    int stickPosX =(SCENE_TAM_X - ANCHURA_PALA) /2;

    @Override
    public void start(Stage stage) {
        
        
       //Creación de personaje
       
        Rectangle rectangleCuerpo = new Rectangle (0,0,48,60);
        Rectangle rectanglePierna1 = new Rectangle (0,50,10,20);
        Rectangle rectanglePierna2 = new Rectangle (38,50,10,20);
        Rectangle rectangleMano1 = new Rectangle (-10,8,10,30);
        Rectangle rectangleMano2 = new Rectangle (50,8,10,30);
        Circle circlePierna1 = new Circle (5,70,5);
        
        //Creación de obstaculos
        Rectangle rectangleObstaculo1 = new Rectangle(5,250,193,50);
        Rectangle rectangleObstaculo2 = new Rectangle(203,250,193,50);
        Rectangle rectangleObstaculo3 = new Rectangle(403,250,193,50);
        Rectangle rectangleObstaculo4 = new Rectangle(603,250,193,50);
       
        //Background del juego
        Image img = new Image(getClass().getResourceAsStream("/images/bg_1_1.png"));
        ImageView imgView = new ImageView(img);
        
        //Color personaje
        rectangleCuerpo.setFill(Color.PURPLE);
        rectanglePierna1.setFill(Color.BROWN);
        rectanglePierna2.setFill(Color.BROWN);
        rectangleMano1.setFill(Color.RED);
        rectangleMano1.setRotate(225);
        rectangleMano2.setFill(Color.RED);
        rectangleMano2.setRotate(135);
        circlePierna1.setFill(Color.ORANGE);
        
        //Color Obstaculos
        rectangleObstaculo1.setFill(Color.BLUE);
        rectangleObstaculo2.setFill(Color.BLUE);
        rectangleObstaculo3.setFill(Color.BLUE);
        rectangleObstaculo4.setFill(Color.BLUE);
        
        //Unión partes del personaje
        Group groupPerson =new Group();
        groupPerson.getChildren().add(rectangleMano1);
        groupPerson.getChildren().add(rectangleMano2);
        groupPerson.getChildren().add(rectangleCuerpo);
        groupPerson.getChildren().add(circlePierna1);
        groupPerson.getChildren().add(rectanglePierna1);
        groupPerson.getChildren().add(rectanglePierna2);  
        
        groupPerson.setLayoutX(50);
        groupPerson.setLayoutY(50);
       
        Pane root = new Pane();
        Scene scene = new Scene(root, SCENE_TAM_X, SCENE_TAM_Y, Color.LIGHTSLATEGREY);
        root.getChildren().add(imgView);
        root.getChildren().add(groupPerson);
        root.getChildren().add(rectangleObstaculo1);
        root.getChildren().add(rectangleObstaculo2);
        root.getChildren().add(rectangleObstaculo3);
        root.getChildren().add(rectangleObstaculo4);      
        stage.setTitle("BreakoutFX");
        stage.setScene(scene);
        stage.show();
        
  
        
        
        //Creación de la pala del jugador
        Rectangle rectPala = new Rectangle(SCENE_TAM_X/2, SCENE_TAM_Y-70, ANCHURA_PALA, ALTURA_PALA );
        rectPala.setFill(Color.YELLOW);
        root.getChildren().add(rectPala);
        
        Circle circleBall =new Circle(ballCenterX,ballCenterY,8, Color.LIGHTGRAY);
        root.getChildren().add(circleBall);
        
        Timeline animationBall = new Timeline(
                new KeyFrame(Duration.seconds(0.017),(var ae) -> {
                    System.out.println(ballCenterY);
                    circleBall.setCenterY(ballCenterY);
                    ballCenterY +=ballCurrentSpeedY;
                    circleBall.setCenterX(ballCenterX);
                    ballCenterX += ballCurrentSpeedX;
                    stickPosX += stickCurrentSpeed;
                    rectPala.setX(stickPosX);
                    if(ballCenterX>=SCENE_TAM_X) {
                        ballCurrentSpeedX = -6;
                    }
                    if(ballCenterX<=0) {
                        ballCurrentSpeedX = 6;
                    }
                    if(ballCenterY>=SCENE_TAM_Y) {
                        ballCurrentSpeedY = -6;
                    }
                    if(ballCenterY<=0) {
                        ballCurrentSpeedY = 6;
                    }
                    Shape shapeColision = Shape.intersect(circleBall, rectPala);
                    boolean colisionVacia = shapeColision.getBoundsInLocal().isEmpty();
                    if(colisionVacia == false) {
                        ballCurrentSpeedY = -3;
                    }
                    calculateBallSpeed(getStickCollisionZone(circleBall, rectPala));
                })
        );
        animationBall.setCycleCount(Timeline. INDEFINITE);
        animationBall.play();
        
        //Movimiento de la paleta del jugador
        scene.setOnKeyPressed((KeyEvent event) -> {
            switch (event.getCode()) {
                case LEFT:
                    stickCurrentSpeed = -6;
                    break;
                case RIGHT:
                    stickCurrentSpeed = 6;
                    break;
            }
        });
        
        
        scene.setOnKeyReleased((KeyEvent event) -> {
            stickCurrentSpeed = 0;
        });
    }
    
    private int getStickCollisionZone(Circle ball, Rectangle stick){
        if (Shape.intersect(ball, stick).getBoundsInLocal().isEmpty()){
            return 0;
        }else {
            double offsetBallStick = ball.getCenterY()- stick.getY();
            if (offsetBallStick < stick.getHeight() -0.5) {
                return 1;
            } else if (offsetBallStick <stick.getHeight() / 2) {
                return 2;
            } else if (offsetBallStick <= stick.getHeight() /2 && offsetBallStick < stick.getHeight() * 0.9) {
                return 3;
            } else {
                return 4;
            }
        }
    }
    
    private void calculateBallSpeed (int collisionZone) {
        switch(collisionZone) {
            case 0: 
                break;
            case 1:
                ballCurrentSpeedX= -6;
                ballCurrentSpeedY = -6;
                break;
            case 2:
                ballCurrentSpeedX = -3;
                ballCurrentSpeedY = -3;
                break;
            case 3:
                ballCurrentSpeedX = 3;
                ballCurrentSpeedY = -3;
                break;
            case 4:
                ballCurrentSpeedX = 6;
                ballCurrentSpeedY = -6;
                break;                  
        }
    }

    public static void main(String[] args) {
        launch();
    }

}