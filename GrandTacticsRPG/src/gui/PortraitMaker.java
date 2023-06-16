package gui;

import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import unit.human.Demeanor;
import unit.human.Human;
import util.RNGStuff;

public class PortraitMaker {

	public static void makeHumanPortrait(Group portrait, Human h) {
		Canvas canvas = new Canvas(200, 200);
		GraphicsContext gc = canvas.getGraphicsContext2D();
		behindHair(gc, h.getAppearance()[7], h.getHairColor());
		ears(gc, h.getAppearance()[3], h.getSkinColor());
		face(gc, h.getAppearance()[0], h.getSkinColor());
		eyes(gc, h.getAppearance()[4], h.getEyeColor());
		nose(gc, h.getAppearance()[2], h.getSkinColor());
		mouth(gc, h.getAppearance()[1], h.getDemeanor().getMouthOrientation());
		brows(gc, h.getAppearance()[6], h.getDemeanor().getBrowOrientation(), h.getHairColor());
		frontHair(gc, h.getAppearance()[7], h.getHairColor());
		stache(gc, h.getAppearance()[8], h.getHairColor());
		beard(gc, h.getAppearance()[9], h.getHairColor());
		portrait.getChildren().add(canvas);
		portrait.setScaleY(-1);
	}
	public static void makeHumanPortrait(Group portrait, int[] components) {
		Canvas canvas = new Canvas(200, 200);
		GraphicsContext gc = canvas.getGraphicsContext2D();
		behindHair(gc, components[8], RNGStuff.HAIR_COLORS_IN_USE.colorAtIndex(components[11]));
		ears(gc, components[4], RNGStuff.SKIN_COLORS_IN_USE.colorAtIndex(components[12]));
		face(gc, components[1], RNGStuff.SKIN_COLORS_IN_USE.colorAtIndex(components[12]));
		eyes(gc, components[5], RNGStuff.EYE_COLORS_IN_USE.colorAtIndex(components[13]));
		nose(gc, components[2], RNGStuff.SKIN_COLORS_IN_USE.colorAtIndex(components[12]));
		mouth(gc, components[3], Demeanor.MOUTH_BLANK);
		brows(gc, components[7], Demeanor.BROW_BLANK, RNGStuff.HAIR_COLORS_IN_USE.colorAtIndex(components[11]));
		frontHair(gc, components[8], RNGStuff.HAIR_COLORS_IN_USE.colorAtIndex(components[11]));
		stache(gc, components[9], RNGStuff.HAIR_COLORS_IN_USE.colorAtIndex(components[11]));
		beard(gc, components[10], RNGStuff.HAIR_COLORS_IN_USE.colorAtIndex(components[11]));
		portrait.getChildren().add(canvas);
		portrait.setScaleY(-1);
//		allOpts.add(genderBox); //0
//		allOpts.add(faceBox); //1
//		allOpts.add(noseBox); //2
//		allOpts.add(lipsBox); //3
//		allOpts.add(earsBox); //4
//		allOpts.add(eyesBox); //5
//		allOpts.add(irisBox); //6
//		allOpts.add(browBox); //7
//		allOpts.add(styleBox); //8
//		allOpts.add(stacheBox); //9
//		allOpts.add(beardBox); //10
//		allOpts.add(hairBox); //11
//		allOpts.add(skinBox); //12
//		allOpts.add(eyeBox); //13

	}
	private static void behindHair(GraphicsContext gc, int style, Color color) {
		
	}
	private static void ears(GraphicsContext gc, int shape, Color color) {
		gc.beginPath();
		gc.moveTo(40, 100);
		//Right (apparent left) ear
		if (shape == 0) {
			gc.lineTo(20, 100);
			gc.lineTo(20, 140);
			gc.lineTo(40, 140);
			gc.lineTo(40, 100);
		} else if (shape == 1) {
			gc.lineTo(20, 140);
			gc.lineTo(40, 140);
			gc.lineTo(40, 100);
		} else if (shape == 2) {
			gc.lineTo(20, 100);
			gc.lineTo(20, 140);
			gc.lineTo(0, 140);
			gc.lineTo(0, 160);
			gc.lineTo(40, 160);
			gc.lineTo(40, 100);
		} else if (shape == 3) {
			gc.quadraticCurveTo(20, 120, 40, 140);
			gc.lineTo(40, 100);
		} else if (shape == 4) {
			gc.lineTo(20, 100);
			gc.lineTo(20, 160);
			gc.lineTo(40, 160);
			gc.lineTo(40, 100);
		} else if (shape == 5) {
			gc.lineTo(20, 100);
			gc.lineTo(40, 140);
			gc.lineTo(40, 100);
		} else if (shape == 6) {
			gc.quadraticCurveTo(20, 130, 40, 160);
			gc.lineTo(40, 100);
		}
		gc.closePath();
		gc.setFill(color);
		gc.fill();
		
		gc.beginPath();
		gc.moveTo(160, 100);
		//Left (apparent right) ear
		if (shape == 0) {
			gc.lineTo(180, 100);
			gc.lineTo(180, 140);
			gc.lineTo(160, 140);
			gc.lineTo(160, 100);
		} else if (shape == 1) {
			gc.lineTo(180, 140);
			gc.lineTo(160, 140);
			gc.lineTo(160, 100);
		} else if (shape == 2) {
			gc.lineTo(180, 100);
			gc.lineTo(180, 140);
			gc.lineTo(200, 140);
			gc.lineTo(200, 160);
			gc.lineTo(160, 160);
			gc.lineTo(160, 100);
		} else if (shape == 3) {
			gc.quadraticCurveTo(180, 120, 160, 140);
			gc.lineTo(160, 100);
		} else if (shape == 4) {
			gc.lineTo(180, 100);
			gc.lineTo(180, 160);
			gc.lineTo(160, 160);
			gc.lineTo(160, 100);
		} else if (shape == 5) {
			gc.lineTo(180, 100);
			gc.lineTo(160, 140);
			gc.lineTo(160, 100);
		} else if (shape == 6) {
			gc.quadraticCurveTo(180, 130, 160, 160);
			gc.lineTo(160, 100);
		}
		gc.closePath();
		gc.setFill(color);
		gc.fill();
	}
	private static void face(GraphicsContext gc, int shape, Color color) {
		gc.beginPath();
		gc.moveTo(40, 80);
		gc.lineTo(40, 160);
		gc.lineTo(60, 180);
		gc.lineTo(140, 180);
		gc.lineTo(160, 160);
		gc.lineTo(160, 80);
		
		if (shape == 0) {
			gc.lineTo(140, 60);
			gc.lineTo(60, 60);
			gc.lineTo(40, 80);
		} else if (shape == 1) {
			gc.lineTo(120, 40);
			gc.lineTo(80, 40);
			gc.lineTo(40, 80);
		} else if (shape == 2) {
			gc.lineTo(40, 80); //TODO maybe change
		} else if (shape == 3) {
			gc.lineTo(100, 20);
			gc.lineTo(40, 80);
		} else if (shape == 4) {
			gc.quadraticCurveTo(100, 60, 40, 80);
		} else if (shape == 5) {
			gc.lineTo(120, 40);
			gc.lineTo(100, 60);
			gc.lineTo(80, 40);
			gc.lineTo(40, 80);
		} else if (shape == 6) {
			gc.lineTo(160, 60);
			gc.lineTo(140, 40);
			gc.lineTo(60, 40);
			gc.lineTo(40, 60);
			gc.lineTo(40, 80);
		} else if (shape == 7) {
			gc.lineTo(140, 60);
			gc.lineTo(120, 60);
			gc.lineTo(100, 40);
			gc.lineTo(80, 60);
			gc.lineTo(60, 60);
			gc.lineTo(40, 80);
		} else if (shape == 8) {
			gc.quadraticCurveTo(100, 20, 40, 80);
		} else if (shape == 9) {
			gc.lineTo(140, 60);
			gc.lineTo(140, 40);
			gc.lineTo(60, 40);
			gc.lineTo(60, 60);
			gc.lineTo(40, 80);
		} else if (shape == 10) {
			gc.lineTo(160, 60);
			gc.lineTo(120, 20);
			gc.lineTo(80, 20);
			gc.lineTo(40, 60);
			gc.lineTo(40, 80);
		} else if (shape == 11) {
			gc.lineTo(160, 60);
			gc.lineTo(140, 40);
			gc.lineTo(120, 40);
			gc.lineTo(100, 20);
			gc.lineTo(80, 40);
			gc.lineTo(60, 40);
			gc.lineTo(40, 60);
			gc.lineTo(40, 80);
		} else if (shape == 12) {
			gc.lineTo(160, 60);
			gc.lineTo(40, 60);
			gc.lineTo(40, 80);
		} else if (shape == 13) {
			gc.lineTo(140, 80);
			gc.lineTo(100, 40);
			gc.lineTo(60, 80);
			gc.lineTo(40, 80);
		} else if (shape == 14) {
			gc.lineTo(140, 80);
			gc.lineTo(120, 60);
			gc.lineTo(80, 60);
			gc.lineTo(60, 80);
			gc.lineTo(40, 80);
		}
		
		gc.closePath();
		gc.setFill(color);
		gc.fill();
	}
	private static void eyes(GraphicsContext gc, int shape, Color color) {
		if (shape == 0) {
			gc.setFill(Color.WHITE); //Square eyes
			gc.fillRect(60, 120, 20, 20);
			gc.fillRect(120, 120, 20, 20);
			gc.setFill(color); //Square irises
			gc.fillRect(65, 120, 10, 10);
			gc.fillRect(125, 120, 10, 10);
		} else if (shape == 1) {
			gc.setFill(Color.WHITE); //Square eyes
			gc.fillRect(60, 120, 20, 20);
			gc.fillRect(120, 120, 20, 20);
			gc.setFill(color); //Circle irises
			gc.fillOval(65, 120, 10, 10);
			gc.fillOval(125, 120, 10, 10);
		} else if (shape == 2) {
			gc.beginPath(); //Diamond eyes
			gc.moveTo(70, 140);
			gc.lineTo(80, 130);
			gc.lineTo(70, 120);
			gc.lineTo(60, 130);
			gc.lineTo(70, 140);
			gc.closePath();
			gc.setFill(Color.WHITE);
			gc.fill();
			gc.beginPath();
			gc.moveTo(130, 140);
			gc.lineTo(140, 130);
			gc.lineTo(130, 120);
			gc.lineTo(120, 130);
			gc.lineTo(130, 140);
			gc.closePath();
			gc.setFill(Color.WHITE);
			gc.fill();
			gc.beginPath(); //Diamond irises
			gc.moveTo(70, 135);
			gc.lineTo(75, 130);
			gc.lineTo(70, 125);
			gc.lineTo(65, 130);
			gc.moveTo(70, 135);
			gc.closePath();
			gc.setFill(color);
			gc.fill();
			gc.beginPath();
			gc.moveTo(130, 135);
			gc.lineTo(135, 130);
			gc.lineTo(130, 125);
			gc.lineTo(125, 130);
			gc.moveTo(130, 135);
			gc.closePath();
			gc.setFill(color);
			gc.fill();
		} else if (shape == 3) {
			gc.beginPath(); //Diamond eyes
			gc.moveTo(70, 140);
			gc.lineTo(80, 130);
			gc.lineTo(70, 120);
			gc.lineTo(60, 130);
			gc.lineTo(70, 140);
			gc.closePath();
			gc.setFill(Color.WHITE);
			gc.fill();
			gc.beginPath();
			gc.moveTo(130, 140);
			gc.lineTo(140, 130);
			gc.lineTo(130, 120);
			gc.lineTo(120, 130);
			gc.lineTo(130, 140);
			gc.closePath();
			gc.setFill(Color.WHITE);
			gc.fill();
			gc.setFill(color); //Square irises
			gc.fillRect(65, 125, 10, 10);
			gc.fillRect(125, 125, 10, 10);
		} else if (shape == 4) {
			gc.beginPath(); //Diamond eyes
			gc.moveTo(70, 140);
			gc.lineTo(80, 130);
			gc.lineTo(70, 120);
			gc.lineTo(60, 130);
			gc.lineTo(70, 140);
			gc.closePath();
			gc.setFill(Color.WHITE);
			gc.fill();
			gc.beginPath();
			gc.moveTo(130, 140);
			gc.lineTo(140, 130);
			gc.lineTo(130, 120);
			gc.lineTo(120, 130);
			gc.lineTo(130, 140);
			gc.closePath();
			gc.setFill(Color.WHITE);
			gc.fill();
			gc.setFill(color); //Circle irises
			gc.fillOval(65, 125, 10, 10);
			gc.fillOval(125, 125, 10, 10);
		} else if (shape == 5) {
			gc.setFill(Color.WHITE); //Circle eyes
			gc.fillOval(60, 120, 20, 20);
			gc.fillOval(120, 120, 20, 20);
			gc.setFill(color); //Square irises
			gc.fillRect(65, 120, 10, 10);
			gc.fillRect(125, 120, 10, 10);
		} else if (shape == 6) {
			gc.setFill(Color.WHITE); //Circle eyes
			gc.fillOval(60, 120, 20, 20);
			gc.fillOval(120, 120, 20, 20);
			gc.setFill(color); //Circle irises
			gc.fillOval(65, 120, 10, 10);
			gc.fillOval(125, 120, 10, 10);
		} else if (shape == 7) {
			gc.setFill(Color.WHITE); //Circle eyes
			gc.fillOval(60, 120, 20, 20);
			gc.fillOval(120, 120, 20, 20);
			gc.beginPath(); //Diamond irises
			gc.moveTo(70, 135);
			gc.lineTo(75, 130);
			gc.lineTo(70, 125);
			gc.lineTo(65, 130);
			gc.moveTo(70, 135);
			gc.closePath();
			gc.setFill(color);
			gc.fill();
			gc.beginPath();
			gc.moveTo(130, 135);
			gc.lineTo(135, 130);
			gc.lineTo(130, 125);
			gc.lineTo(125, 130);
			gc.moveTo(130, 135);
			gc.closePath();
			gc.setFill(color);
			gc.fill();
		} else if (shape == 8) {
			gc.setFill(Color.WHITE); //Rectangle eyes
			gc.fillRect(60, 118, 30, 16);
			gc.fillRect(110, 118, 30, 16);
			gc.setFill(color); //Square irises
			gc.fillRect(70, 120, 10, 10);
			gc.fillRect(120, 120, 10, 10);
		} else if (shape == 9) {
			gc.setFill(Color.WHITE); //Rectangle eyes
			gc.fillRect(60, 118, 30, 16);
			gc.fillRect(110, 118, 30, 16);
			gc.setFill(color); //Circle irises
			gc.fillOval(70, 120, 10, 10);
			gc.fillOval(120, 120, 10, 10);
		} else if (shape == 10) {
			gc.setFill(Color.WHITE); //Horizontal oval eyes
			gc.fillOval(60, 120, 30, 20);
			gc.fillOval(110, 120, 30, 20);
			gc.setFill(color); //Circle irises
			gc.fillOval(70, 125, 10, 10);
			gc.fillOval(120, 125, 10, 10);
		} else if (shape == 11) {
			gc.setFill(Color.WHITE); //Horizontal oval eyes
			gc.fillOval(60, 120, 30, 20);
			gc.fillOval(110, 120, 30, 20);
			gc.setFill(color); //Square irises
			gc.fillRect(70, 125, 10, 10);
			gc.fillRect(120, 125, 10, 10);
		} else if (shape == 12) {
			gc.setFill(Color.WHITE); //Vertical oval eyes
			gc.fillOval(60, 115, 20, 30);
			gc.fillOval(120, 115, 20, 30);
			gc.setFill(color); //Square irises
			gc.fillRect(65, 120, 10, 10);
			gc.fillRect(125, 120, 10, 10);
		} else if (shape == 13) {
			gc.setFill(Color.WHITE); //Vertical oval eyes
			gc.fillOval(60, 115, 20, 30);
			gc.fillOval(120, 115, 20, 30);
			gc.setFill(color); //Circle irises
			gc.fillOval(65, 120, 10, 10);
			gc.fillOval(125, 120, 10, 10);
		} else if (shape == 14) {
			gc.setFill(Color.WHITE); //Vertical oval eyes
			gc.fillOval(60, 115, 20, 30);
			gc.fillOval(120, 115, 20, 30);
			gc.setFill(color); //Vertical oval irises
			gc.fillOval(65, 120, 10, 20);
			gc.fillOval(125, 120, 10, 20);
		}
	}
	private static void nose(GraphicsContext gc, int shape, Color color) {
		gc.setStroke(Color.BLACK);
		gc.setLineWidth(3);
		if (shape == 0) {
			gc.strokeLine(100, 120, 110, 110);
			gc.strokeLine(110, 110, 100, 100);
		} else if (shape == 1) {
			gc.strokeOval(90, 100, 20, 20);
		} else if (shape == 2) {
			gc.strokeLine(80, 120, 90, 100);
			gc.strokeLine(90, 100, 110, 100);
			gc.strokeLine(110, 100, 120, 120);
		} else if (shape == 3) {
			gc.strokeLine(80, 120, 100, 100);
			gc.strokeLine(100, 100, 120, 120);
		} else if (shape == 4) {
			gc.strokeRect(90, 100, 20, 20);
		} else if (shape == 5) {
			gc.strokeLine(90, 140, 90, 100);
			gc.strokeLine(90, 100, 110, 100);
			gc.strokeLine(110, 100, 110, 140);
		} else if (shape == 6) {
			gc.strokeArc(100, 100, 10, 20, 270, 180, ArcType.OPEN);
		} else if (shape == 7) {
			gc.strokeArc(80, 100, 40, 20, 0, 180, ArcType.OPEN);
		} else if (shape == 8) {
			gc.strokeArc(80, 100, 40, 20, 180, 180, ArcType.OPEN);
		} else if (shape == 9) {
			gc.setFill(Color.BLACK);
			gc.fillOval(85, 115, 10, 10);
			gc.fillOval(105, 115, 10, 10);
		} else if (shape == 10) {
			gc.strokeLine(90, 140, 100, 100);
			gc.strokeLine(100, 100, 110, 140);
		} else if (shape == 11) {
			gc.strokeArc(80, 110, 10, 10, 0, 180, ArcType.OPEN);
			gc.strokeArc(90, 100, 20, 20, 0, 180, ArcType.OPEN);
			gc.strokeArc(110, 110, 10, 10, 0, 180, ArcType.OPEN);
		} else if (shape == 12) {
			gc.strokeLine(100, 130, 120, 100);
			gc.strokeLine(120, 100, 100, 100);
		} else if (shape == 13) {
			gc.strokeOval(80, 100, 40, 20);
		} else if (shape == 14) {
			gc.strokeLine(80, 100, 100, 120);
			gc.strokeLine(100, 120, 120, 100);
		}
	}
	private static void mouth(GraphicsContext gc, int lipSize, int orientation) {
		if (lipSize == 0) {
			gc.setLineWidth(3);
			if (orientation == Demeanor.MOUTH_BLANK) {
				gc.strokeLine(70, 90, 130, 90);
			} else if (orientation == Demeanor.MOUTH_HAPPY) {
				gc.strokeArc(80, 80, 40, 20, 0, 180, ArcType.OPEN);
			} else if (orientation == Demeanor.MOUTH_UPSET) {
				gc.strokeArc(80, 80, 40, 15, 180, 180, ArcType.OPEN);
			} else if (orientation == Demeanor.MOUTH_SLANT) {
				gc.strokeLine(70, 80, 130, 100);
			}
		} else if (lipSize == 1) {
			if (orientation == Demeanor.MOUTH_BLANK) {
				gc.strokeArc(90, 91, 20, 4, 180, 180, ArcType.OPEN);
				gc.strokeLine(80, 90, 120, 90);
				gc.strokeArc(90, 84, 20, 4, 0, 180, ArcType.OPEN);
			} else if (orientation == Demeanor.MOUTH_HAPPY) {
				gc.strokeLine(90, 90, 110, 90);
				gc.strokeArc(80, 85, 40, 10, 0, 180, ArcType.OPEN);
				gc.strokeArc(90, 77, 20, 5, 0, 180, ArcType.OPEN);
			} else if (orientation == Demeanor.MOUTH_UPSET) {
				gc.strokeArc(80, 80, 40, 15, 180, 180, ArcType.OPEN);
				gc.strokeLine(90, 85, 110, 85);
			} else if (orientation == Demeanor.MOUTH_SLANT) {
				gc.strokeLine(70, 80, 130, 100);
				gc.strokeLine(90, 80, 120, 90);
			}
		}
	}
	private static void brows(GraphicsContext gc, int browSize, int orientation, Color color) {
		gc.setStroke(color);
		if (browSize == 0) {
			gc.setLineWidth(1);
		} else if (browSize == 1) {
			gc.setLineWidth(5);
		} else if (browSize == 2) {
			gc.setLineWidth(10);
		}
		if (orientation == Demeanor.BROW_BLANK) {
			gc.strokeLine(50, 150, 90, 150);
			gc.strokeLine(110, 150, 150, 150);
		} else if (orientation == Demeanor.BROW_HAPPY) {
			gc.strokeArc(50, 140, 40, 20, 180, 180, ArcType.OPEN);
			gc.strokeArc(110, 140, 40, 20, 180, 180, ArcType.OPEN);
		} else if (orientation == Demeanor.BROW_AFRAID) {
			gc.strokeLine(60, 140, 80, 160);
			gc.strokeLine(120, 160, 140, 140);
		} else if (orientation == Demeanor.BROW_ANGRY) {
			gc.strokeLine(70, 160, 90, 140);
			gc.strokeLine(110, 140, 130, 160);
		} else if (orientation == Demeanor.BROW_CONFUSED) {
			gc.strokeLine(50, 140, 70, 160); //First brow is squiggly
			gc.strokeLine(70, 160, 80, 140);
			gc.strokeLine(80, 140, 90, 160);
			gc.strokeLine(110, 150, 150, 150); //Second brow is straight
		}
	}
	private static void frontHair(GraphicsContext gc, int style, Color color) {
		
	}
	private static void stache(GraphicsContext gc, int style, Color color) {
		
	}
	private static void beard(GraphicsContext gc, int style, Color color) {
		
	}
}
