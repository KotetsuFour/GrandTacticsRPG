package gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.InputMismatchException;

import javax.swing.JButton;

import affiliation.Nation;
import battle.BattleGround;
import buildings.Building;
import buildings.Coliseum;
import buildings.Hospital;
import buildings.Port;
import buildings.ResearchCenter;
import buildings.Shipyard;
import buildings.Village;
import buildings.WarpPad;
import buildings.defendable.Barracks;
import buildings.defendable.Castle;
import buildings.defendable.Defendable;
import buildings.defendable.Fortress;
import buildings.defendable.Prison;
import buildings.defendable.TrainingFacility;
import buildings.goods_deliverer.Factory;
import buildings.goods_deliverer.Farm;
import buildings.goods_deliverer.MagicProcessingFacility;
import buildings.goods_deliverer.MiningFacility;
import buildings.goods_deliverer.Ranch;
import buildings.goods_deliverer.Storehouse;
import buildings.goods_deliverer.TradeCenter;
import data_structures.List;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import location.WMTileOccupant;
import location.WorldMap;
import location.WorldMapTile;
import manager.GeneralGameplayManager;
import ship.Ship;
import unit.Equippable;
import unit.Unit;
import unit.UnitGroup;
import unit.human.Demeanor;
import unit.human.Human;
import unit.human.Human.Interest;
import util.RNGStuff;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;


public class Main extends Application {
	
	public static Stage STAGE;
	public static Group ROOT;
	
	@Override
	public void start(Stage primaryStage) {
		STAGE = primaryStage;
		try {
//			BorderPane root = new BorderPane();
//			Scene scene = new Scene(root,400,400);
//			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			Group group = new Group();
			Scene scene = new Scene(group, 1200, 600);
			
			primaryStage.setTitle("Grand Tactical RPG Ver. 1");
			primaryStage.setScene(scene);
			primaryStage.show();
			
			ROOT = group;
			worldCustomization(primaryStage, group);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public static void worldCustomization(final Stage primaryStage, final Group root) {
		
		GeneralGameplayManager.indexesInitialization();
		
		Label title = new Label("World Settings");
		title.setFont(new Font(30));
		root.getChildren().add(title);
		
		Label langTitle = new Label("Character Naming Conventions (at least one)");
		langTitle.setFont(new Font(15));
		langTitle.setTranslateY(50);
		root.getChildren().add(langTitle);
		GridPane langs = new GridPane();
		final CheckBox[] useLangs = new CheckBox[RNGStuff.LANGUAGES.length];
		for (int q = 0; q < RNGStuff.LANGUAGES.length; q++) {
			useLangs[q] = new CheckBox(RNGStuff.LANGUAGES[q].titleOfNamingConvention());
			useLangs[q].setSelected(true);
			Button sample = new Button("Sample Names");
			sample.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					// TODO Auto-generated method stub
				}
			});
			sample.setTranslateY(20);
			Group langOpt = new Group();
			langOpt.getChildren().add(useLangs[q]);
			langOpt.getChildren().add(sample);
			langs.add(langOpt, q, 0);
		}
		langs.setTranslateY(80);
		root.getChildren().add(langs);
		
		final CheckBox aging = new CheckBox("Aging");
		aging.setSelected(true);
		aging.setTranslateY(150);
		root.getChildren().add(aging);
		
		Label lore = new Label("Lore Options Coming Soon");
		lore.setTranslateY(200);
		root.getChildren().add(lore);
		
		Label hairTitle = new Label("Hair Colors (at least one)");
		hairTitle.setFont(new Font(15));
		hairTitle.setTranslateY(300);
		root.getChildren().add(hairTitle);
		GridPane hairs = new GridPane();
		final CheckBox[] useHairs = new CheckBox[RNGStuff.HAIR_COLORS.length];
		for (int q = 0; q < RNGStuff.HAIR_COLORS.length; q++) {
			useHairs[q] = new CheckBox(RNGStuff.HAIR_COLORS[q].getTitle());
			Button sample = new Button("Sample Names");
			sample.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					// TODO Auto-generated method stub
				}
			});
			sample.setTranslateY(20);
			Group hairOpt = new Group();
			hairOpt.getChildren().add(useHairs[q]);
			hairOpt.getChildren().add(sample);
			hairs.add(hairOpt, q, 0);
		}
		useHairs[0].setSelected(true);
		hairs.setTranslateY(330);
		root.getChildren().add(hairs);
		
		Label skinTitle = new Label("Skin Colors (at least one)");
		skinTitle.setFont(new Font(15));
		skinTitle.setTranslateY(400);
		root.getChildren().add(skinTitle);
		GridPane skins = new GridPane();
		final CheckBox[] useSkins = new CheckBox[RNGStuff.SKIN_COLORS.length];
		for (int q = 0; q < RNGStuff.SKIN_COLORS.length; q++) {
			useSkins[q] = new CheckBox(RNGStuff.SKIN_COLORS[q].getTitle());
			Button sample = new Button("Sample Names");
			sample.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					// TODO Auto-generated method stub
				}
			});
			sample.setTranslateY(20);
			Group skinOpt = new Group();
			skinOpt.getChildren().add(useSkins[q]);
			skinOpt.getChildren().add(sample);
			skins.add(skinOpt, q, 0);
		}
		useSkins[0].setSelected(true);
		skins.setTranslateY(430);
		root.getChildren().add(skins);

		Label eyeTitle = new Label("Eye Colors (at least one)");
		eyeTitle.setFont(new Font(15));
		eyeTitle.setTranslateY(500);
		root.getChildren().add(eyeTitle);
		GridPane eyes = new GridPane();
		final CheckBox[] useEyes = new CheckBox[RNGStuff.EYE_COLORS.length];
		for (int q = 0; q < RNGStuff.EYE_COLORS.length; q++) {
			useEyes[q] = new CheckBox(RNGStuff.EYE_COLORS[q].getTitle());
			Button sample = new Button("Sample Names");
			sample.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					// TODO Auto-generated method stub
				}
			});
			sample.setTranslateY(20);
			Group eyeOpt = new Group();
			eyeOpt.getChildren().add(useEyes[q]);
			eyeOpt.getChildren().add(sample);
			eyes.add(eyeOpt, q, 0);
		}
		useEyes[0].setSelected(true);
		eyes.setTranslateY(530);
		root.getChildren().add(eyes);

		Rectangle nextRect = new Rectangle(600, 20);
		nextRect.setFill(Color.CYAN);
		Label nextLabel = new Label("NEXT>>");
		Group nextGroup = new Group();
		nextGroup.getChildren().add(nextRect);
		nextGroup.getChildren().add(nextLabel);
		nextGroup.setTranslateY(580);
		root.getChildren().add(nextGroup);
		nextRect.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				// TODO Auto-generated method stub
				GeneralGameplayManager.setAging(aging.isSelected());
				//TODO add lore options later
				List<Integer> langs = new List<>();
				List<Integer> hairs = new List<>();
				List<Integer> skins = new List<>();
				List<Integer> eyes = new List<>();
				for (int q = 0; q < useLangs.length; q++) {
					if (useLangs[q].isSelected()) {
						langs.add(q);
					}
				}
				for (int q = 0; q < useHairs.length; q++) {
					if (useHairs[q].isSelected()) {
						hairs.add(q);
					}
				}
				for (int q = 0; q < useSkins.length; q++) {
					if (useSkins[q].isSelected()) {
						skins.add(q);
					}
				}
				for (int q = 0; q < useEyes.length; q++) {
					if (useEyes[q].isSelected()) {
						eyes.add(q);
					}
				}
				if (langs.isEmpty() || hairs.isEmpty() || skins.isEmpty()
						|| eyes.isEmpty()) {
					return;
				}
				RNGStuff.useLanguage(langs);
				RNGStuff.useColors(hairs, skins, eyes);

				nationCustomization(primaryStage, root);
			}
		});
	}
	public static void nationCustomization(final Stage primaryStage, final Group root) {
		root.getChildren().clear();
		
		Label title = new Label("Your Nation");
		title.setFont(new Font(30));
		root.getChildren().add(title);
		
		Label the = new Label("The");
		the.setFont(new Font(20));
		final ComboBox<String> type = new ComboBox<>();
		for (int q = 0; q < Nation.NATION_TYPES.length; q++) {
			type.getItems().add(Nation.NATION_TYPES[q]);
		}
		type.getSelectionModel().selectFirst();
		Label of = new Label("of");
		of.setFont(new Font(20));
		final TextField name = new TextField();
		type.setTranslateX(40);
		of.setTranslateX(150);
		name.setTranslateX(180);
		Group nameNation = new Group();
		nameNation.getChildren().add(the);
		nameNation.getChildren().add(type);
		nameNation.getChildren().add(of);
		nameNation.getChildren().add(name);
		nameNation.setTranslateY(50);
		root.getChildren().add(nameNation);
		
		Label cap = new Label("Capital City Name:");
		cap.setFont(new Font(20));
		final TextField capitalName = new TextField();
		capitalName.setTranslateX(200);
		Group capitalNation = new Group();
		capitalNation.getChildren().add(cap);
		capitalNation.getChildren().add(capitalName);
		capitalNation.setTranslateY(100);
		root.getChildren().add(capitalNation);
		
		Label pickLang = new Label("Character Naming Convention");
		pickLang.setFont(new Font(20));
		final ComboBox<String> lang = new ComboBox<>();
		for (int q = 0; q < RNGStuff.LANGUAGES_IN_USE.length; q++) {
			lang.getItems().add(RNGStuff.LANGUAGES_IN_USE[q].titleOfNamingConvention());
		}
		lang.getItems().add("--Random--");
		lang.getSelectionModel().selectLast();
		Button sample = new Button("Sample Names");
		sample.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				// TODO Auto-generated method stub
			}
		});
		lang.setTranslateX(300);
		sample.setTranslateX(500);
		Group langGroup = new Group();
		langGroup.getChildren().add(pickLang);
		langGroup.getChildren().add(lang);
		langGroup.getChildren().add(sample);
		langGroup.setTranslateY(150);
		root.getChildren().add(langGroup);
		
		Rectangle nextRect = new Rectangle(600, 20);
		nextRect.setFill(Color.CYAN);
		Label next = new Label("NEXT>>");
		Group nextGroup = new Group();
		nextGroup.getChildren().add(nextRect);
		nextGroup.getChildren().add(next);
		nextGroup.setTranslateY(580);
		root.getChildren().add(nextGroup);
		nextRect.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				// TODO Auto-generated method stub
				try {
					int selectedLang = lang.getSelectionModel().getSelectedIndex();
					if (selectedLang == RNGStuff.LANGUAGES_IN_USE.length) {
						selectedLang = RNGStuff.nextInt(RNGStuff.LANGUAGES_IN_USE.length);
					}
					String pNationName = name.getText();
					if (pNationName == null || pNationName.equals("")) {
						pNationName = RNGStuff.randomName(selectedLang);
					} else {
						pNationName = validateInputName(pNationName, "Name");
					}
					String pCapitalName = capitalName.getText();
					if (pCapitalName == null || pCapitalName.equals("")) {
						pCapitalName = RNGStuff.randomName(selectedLang);
					} else {
						pCapitalName = validateInputName(pCapitalName, "Capital");
					}
					int pNationType = type.getSelectionModel().getSelectedIndex();
					
					GeneralGameplayManager.initializePlayerNation(pNationName, pNationType,
							pCapitalName, selectedLang);
					
					avatarCustomization(primaryStage, root);
				} catch (Exception ex) {
					//TODO error message
				}
			}
		});
	}
	public static void avatarCustomization(final Stage primaryStage, final Group root) {
		root.getChildren().clear();
		GridPane appearance = new GridPane();
		final Group portrait = new Group();
		portrait.setTranslateX(800);
		portrait.setTranslateY(100);
		root.getChildren().add(portrait);
		
		//Name
		appearance.add(new Label("Name:"), 0, 0);
		final TextField nameField = new TextField();
		appearance.add(nameField, 1, 0);
		//Gender
		final ComboBox<String> genderBox = new ComboBox<>();
		genderBox.getItems().add("Male");
		genderBox.getItems().add("Female");
		genderBox.getItems().add("--Random--");
		genderBox.getSelectionModel().selectLast();
		appearance.add(new Label("Gender:"), 0, 1);
		appearance.add(genderBox, 1, 1);
		//Face
		appearance.add(new Label("Face Shape"), 0, 2);
		final ComboBox<String> faceBox = new ComboBox<String>();
		for (int q = 0; q < 15; q++) {
			faceBox.getItems().add((q + 1) + "");
		}
		faceBox.getItems().add("--Random--");
		faceBox.getSelectionModel().selectLast();
		appearance.add(faceBox, 1, 2);
		
		appearance.add(new Label("Skin Color"), 2, 2);
		final ComboBox<String> skinBox = new ComboBox<>();
		for (int q = 0; q < RNGStuff.SKIN_COLORS_IN_USE.size(); q++) {
			skinBox.getItems().add("" + (q + 1));
		}
		skinBox.getItems().add("--Random--");
		skinBox.getSelectionModel().selectLast();
		appearance.add(skinBox, 3, 2);
		//Nose
		appearance.add(new Label("Nose Shape"), 0, 3);
		final ComboBox<String> noseBox = new ComboBox<String>();
		for (int q = 0; q < 15; q++) {
			noseBox.getItems().add((q + 1) + "");
		}
		noseBox.getItems().add("--Random--");
		noseBox.getSelectionModel().selectLast();
		appearance.add(noseBox, 1, 3);
		//Lips
		appearance.add(new Label("Lips Shape"), 0, 4);
		final ComboBox<String> lipsBox = new ComboBox<String>();
		for (int q = 0; q < 2; q++) {
			lipsBox.getItems().add((q + 1) + "");
		}
		lipsBox.getItems().add("--Random--");
		lipsBox.getSelectionModel().selectLast();
		appearance.add(lipsBox, 1, 4);
		//Ear shape
		appearance.add(new Label("Ear Shape"), 0, 5);
		final ComboBox<String> earsBox = new ComboBox<String>();
		for (int q = 0; q < 7; q++) {
			earsBox.getItems().add((q + 1) + "");
		}
		earsBox.getItems().add("--Random--");
		earsBox.getSelectionModel().selectLast();
		appearance.add(earsBox, 1, 5);
		//Eye shape
		appearance.add(new Label("Eye Shape"), 0, 6);
		final ComboBox<String> eyesBox = new ComboBox<String>();
		for (int q = 0; q < 15; q++) {
			eyesBox.getItems().add((q + 1) + "");
		}
		eyesBox.getItems().add("--Random--");
		eyesBox.getSelectionModel().selectLast();
		appearance.add(eyesBox, 1, 6);
		
		appearance.add(new Label("Eye Color"), 2, 6);
		final ComboBox<String> eyeBox = new ComboBox<>();
		for (int q = 0; q < RNGStuff.EYE_COLORS_IN_USE.size(); q++) {
			eyeBox.getItems().add("" + (q + 1));
		}
		eyeBox.getItems().add("--Random--");
		eyeBox.getSelectionModel().selectLast();
		appearance.add(eyeBox, 3, 6);
		//Iris Appearance
		//TODO remove this. We won't use it
		appearance.add(new Label("Iris Appearance"), 0, 7);
		final ComboBox<String> irisBox = new ComboBox<String>();
		for (int q = 0; q < 7; q++) {
			irisBox.getItems().add((q + 1) + "");
		}
		irisBox.getItems().add("--Random--");
		irisBox.getSelectionModel().selectLast();
		appearance.add(irisBox, 1, 7);
		//Eyebrows
		appearance.add(new Label("Eyebrows"), 0, 8);
		final ComboBox<String> browBox = new ComboBox<String>();
		for (int q = 0; q < 3; q++) {
			browBox.getItems().add((q + 1) + "");
		}
		browBox.getItems().add("--Random--");
		browBox.getSelectionModel().selectLast();
		appearance.add(browBox, 1, 8);
		//Hairstyles
		appearance.add(new Label("Hair/Mustache/Beard"), 0, 9);
		final ComboBox<String> styleBox = new ComboBox<String>();
		for (int q = 0; q < 15; q++) {
			styleBox.getItems().add((q + 1) + "");
		}
		styleBox.getItems().add("--Random--");
		styleBox.getSelectionModel().selectLast();
		appearance.add(styleBox, 1, 9);
		
		final ComboBox<String> stacheBox = new ComboBox<String>();
		for (int q = 0; q < 12; q++) {
			stacheBox.getItems().add((q + 1) + "");
		}
		stacheBox.getItems().add("--Random--");
		stacheBox.getSelectionModel().selectLast();
		appearance.add(stacheBox, 2, 9);

		final ComboBox<String> beardBox = new ComboBox<String>();
		for (int q = 0; q < 12; q++) {
			beardBox.getItems().add((q + 1) + "");
		}
		beardBox.getItems().add("--Random--");
		beardBox.getSelectionModel().selectLast();
		appearance.add(beardBox, 3, 9);

		//Haircolor
		appearance.add(new Label("Hair Color"), 0, 10);
		final ComboBox<String> hairBox = new ComboBox<String>();
		for (int q = 0; q < RNGStuff.HAIR_COLORS_IN_USE.size(); q++) {
			hairBox.getItems().add((q + 1) + "");
		}
		hairBox.getItems().add("--Random--");
		hairBox.getSelectionModel().selectLast();
		appearance.add(hairBox, 1, 10);
		
		final List<ComboBox<String>> allOpts = new List<>();
		allOpts.add(genderBox); //0
		allOpts.add(faceBox); //1
		allOpts.add(noseBox); //2
		allOpts.add(lipsBox); //3
		allOpts.add(earsBox); //4
		allOpts.add(eyesBox); //5
		allOpts.add(irisBox); //6
		allOpts.add(browBox); //7
		allOpts.add(styleBox); //8
		allOpts.add(stacheBox); //9
		allOpts.add(beardBox); //10
		allOpts.add(hairBox); //11
		allOpts.add(skinBox); //12
		allOpts.add(eyeBox); //13
		
		EventHandler<ActionEvent> updatePortrait = new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				makePortrait(portrait, allOpts);
			}
		};
		for (int q = 0; q < allOpts.size(); q++) {
			allOpts.get(q).setOnAction(updatePortrait);
		}
		
		
		GridPane traits = new GridPane();
		//Interests and Disinterests
		Human.Interest[] intVals = Human.Interest.values();
		final ComboBox<String> interest1 = new ComboBox<>();
		final ComboBox<String> interest2 = new ComboBox<>();
		final ComboBox<String> interest3 = new ComboBox<>();
		final ComboBox<String> disinterest1 = new ComboBox<>();
		final ComboBox<String> disinterest2 = new ComboBox<>();
		final ComboBox<String> disinterest3 = new ComboBox<>();
		for (int q = 0; q < intVals.length; q++) {
			interest1.getItems().add(intVals[q].getDisplayName());
			interest2.getItems().add(intVals[q].getDisplayName());
			interest3.getItems().add(intVals[q].getDisplayName());
			disinterest1.getItems().add(intVals[q].getDisplayName());
			disinterest2.getItems().add(intVals[q].getDisplayName());
			disinterest3.getItems().add(intVals[q].getDisplayName());
		}
		interest1.getItems().add("--Random--");
		interest2.getItems().add("--Random--");
		interest3.getItems().add("--Random--");
		disinterest1.getItems().add("--Random--");
		disinterest2.getItems().add("--Random--");
		disinterest3.getItems().add("--Random--");
		interest1.getSelectionModel().selectLast();
		interest2.getSelectionModel().selectLast();
		interest3.getSelectionModel().selectLast();
		disinterest1.getSelectionModel().selectLast();
		disinterest2.getSelectionModel().selectLast();
		disinterest3.getSelectionModel().selectLast();
		traits.add(new Label("Interests"), 0, 0);
		traits.add(interest1, 1, 0);
		traits.add(interest2, 2, 0);
		traits.add(interest3, 3, 0);
		traits.add(new Label("Disinterests"), 0, 1);
		traits.add(disinterest1, 1, 1);
		traits.add(disinterest2, 2, 1);
		traits.add(disinterest3, 3, 1);
		//Supports
		Human.CombatTrait[] supVals = Human.CombatTrait.values();
		final ComboBox<String> support = new ComboBox<>();
		for (int q = 0; q < supVals.length; q++) {
			support.getItems().add(supVals[q].getDisplayName());
		}
		support.getItems().add("--Random--");
		support.getSelectionModel().selectLast();
		traits.add(new Label("Support Trait"), 0, 2);
		traits.add(support, 1, 2);
		//Demeanor
		Demeanor[] demVals = Demeanor.values();
		final ComboBox<String> demeanor = new ComboBox<>();
		for (int q = 0; q < demVals.length; q++) {
			demeanor.getItems().add(demVals[q].getDisplayName());
		}
		demeanor.getItems().add("--Random--");
		demeanor.getSelectionModel().selectLast();
		traits.add(new Label("Demeanor"), 0, 3);
		traits.add(demeanor, 1, 3);
		//Boons and Banes
		String[] hpOpts = new String[] {"Head", "Torso", "Arms", "Legs", "--Random--"};
		String[] statOpts = new String[] {"Magic", "Skill", "Reflex", "Awareness", "Resistance", "--Random--"};
		final ComboBox<String> hpBoon = new ComboBox<>();
		final ComboBox<String> hpBane = new ComboBox<>();
		final ComboBox<String> statBoon = new ComboBox<>();
		final ComboBox<String> statBane = new ComboBox<>();
		for (int q = 0; q < hpOpts.length; q++) {
			hpBoon.getItems().add(hpOpts[q]);
			hpBane.getItems().add(hpOpts[q]);
		}
		for (int q = 0; q < statOpts.length; q++) {
			statBoon.getItems().add(statOpts[q]);
			statBane.getItems().add(statOpts[q]);
		}
		hpBoon.getSelectionModel().selectLast();
		hpBane.getSelectionModel().selectLast();
		statBoon.getSelectionModel().selectLast();
		statBane.getSelectionModel().selectLast();
		traits.add(new Label("HP Boon"), 2, 2);
		traits.add(hpBoon, 3, 2);
		traits.add(new Label("Attribute Boon"), 4, 2);
		traits.add(statBoon, 5, 2);
		traits.add(new Label("HP Bane"), 2, 3);
		traits.add(hpBane, 3, 3);
		traits.add(new Label("Attribute Bane"), 4, 3);
		traits.add(statBane, 5, 3);
		
		traits.setHgap(10);
		traits.setVgap(10);
		
		traits.setTranslateY(300);
		portrait.setTranslateX(600);
		root.getChildren().add(appearance);
		root.getChildren().add(traits);
		
		Rectangle nextRect = new Rectangle(600, 20);
		nextRect.setFill(Color.CYAN);
		Label next = new Label("NEXT>>");
		Group nextGroup = new Group();
		nextGroup.getChildren().add(nextRect);
		nextGroup.getChildren().add(next);
		nextGroup.setTranslateY(580);
		root.getChildren().add(nextGroup);
		nextRect.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				try {
					String pName = nameField.getText();
					if (pName == null || pName.equals("")) {
						pName = RNGStuff.randomName(playerNation().getNationalLanguage());
					} else {
						pName = validateInputName(pName, "Name");
					}
					
					int sGender = genderBox.getSelectionModel().getSelectedIndex();
					boolean pGender = false;
					if (sGender == 1) {
						pGender = true;
					} else if (sGender == 2) {
						pGender = RNGStuff.nextBoolean();
					}
					
					int pFace = readRandoComboBoxOption(faceBox);
					int pNose = readRandoComboBoxOption(noseBox);
					int pLips = readRandoComboBoxOption(lipsBox);
					int pEar = readRandoComboBoxOption(earsBox);
					int pEye = readRandoComboBoxOption(eyesBox);
					int pIris = readRandoComboBoxOption(irisBox);
					int pBrow = readRandoComboBoxOption(browBox);
					int pHair = readRandoComboBoxOption(styleBox);
					int pStache = readRandoComboBoxOption(stacheBox);
					int pBeard = readRandoComboBoxOption(beardBox);
					if (pGender) {
						pStache = 0;
						pBeard = 0;
					}
					int pHairColor = readRandoComboBoxOption(hairBox);
					int pSkinColor = readRandoComboBoxOption(skinBox);
					int pEyeColor = readRandoComboBoxOption(eyeBox);
					
					int pInterest1 = readRandoComboBoxOption(interest1);
					int pInterest2 = readRandoComboBoxOption(interest2);
					while (pInterest2 == pInterest1) {
						pInterest2++;
						if (pInterest2 == Interest.values().length) {
							pInterest2 = 0;
						}
					}
					int pInterest3 = readRandoComboBoxOption(interest3);
					while (pInterest3 == pInterest1 || pInterest3 == pInterest2) {
						pInterest3++;
						if (pInterest3 == Interest.values().length) {
							pInterest3 = 0;
						}
					}
					int pInterest4 = readRandoComboBoxOption(disinterest1);
					while (pInterest4 == pInterest1 || pInterest4 == pInterest2
							|| pInterest4 == pInterest3) {
						pInterest4++;
						if (pInterest4 == Interest.values().length) {
							pInterest4 = 0;
						}
					}
					int pInterest5 = readRandoComboBoxOption(disinterest2);
					while (pInterest5 == pInterest1 || pInterest5 == pInterest2
							|| pInterest5 == pInterest3 || pInterest5 == pInterest4) {
						pInterest5++;
						if (pInterest5 == Interest.values().length) {
							pInterest5 = 0;
						}
					}
					int pInterest6 = readRandoComboBoxOption(disinterest3);
					while (pInterest6 == pInterest1 || pInterest6 == pInterest2
							|| pInterest6 == pInterest3 || pInterest6 == pInterest4
							|| pInterest6 == pInterest5) {
						pInterest6++;
						if (pInterest6 == Interest.values().length) {
							pInterest6 = 0;
						}
					}
					
					int pTrait = readRandoComboBoxOption(support);
					int pDemeanor = demeanor.getSelectionModel().getSelectedIndex();
					if (pDemeanor == demeanor.getItems().size() - 1) {
						pDemeanor = RNGStuff.nextInt(Demeanor.values().length);
						if (Demeanor.values()[pDemeanor].getRarity() > 0) {
							pDemeanor = RNGStuff.nextInt(Demeanor.values().length);
						}
					}
					
					int pHpBoon = readRandoComboBoxOption(hpBoon);
					int pHpBane = readRandoComboBoxOption(hpBane);
					if (pHpBane == pHpBoon) {
						pHpBane++;
						if (pHpBane == hpBane.getItems().size() - 1) {
							pHpBane = 0;
						}
					}
	
					int pAttributeBoon = readRandoComboBoxOption(statBoon);
					int pAttributeBane = readRandoComboBoxOption(statBane);
					if (pAttributeBoon == pAttributeBane) {
						pAttributeBane++;
						if (pAttributeBane == statBane.getItems().size() - 1) {
							pAttributeBane = 0;
						}
					}
					
					double r = RNGStuff.SKIN_COLORS_IN_USE.colorAtIndex(pSkinColor).getRed();
					double g = RNGStuff.SKIN_COLORS_IN_USE.colorAtIndex(pSkinColor).getGreen();
					double b = RNGStuff.SKIN_COLORS_IN_USE.colorAtIndex(pSkinColor).getBlue();
					GeneralGameplayManager.initializePlayer(pName, pGender, pFace, pNose,
							pLips, pEar, pEye, pIris, pBrow, pHair, pStache, pBeard,
							pInterest1, pInterest2, pInterest3, pInterest4,
							pInterest5, pInterest6, pTrait, pDemeanor, pHpBoon, pHpBane,
							pAttributeBoon, pAttributeBane, pHairColor,
							r, g, b,
							pEyeColor);
					
					GeneralGameplayManager.initializeGeneralWorld();
					switchToWorldMap(0, 0);
				} catch (Exception ex) {
					ex.printStackTrace();
				}

			}
		});
		
	}
	
	private static int readRandoComboBoxOption(ComboBox<String> box) {
		if (box.getSelectionModel().getSelectedIndex() == box.getItems().size() - 1) {
			return RNGStuff.nextInt(box.getItems().size() - 1);
		}
		return box.getSelectionModel().getSelectedIndex();
	}
	
	public static void makePortrait(Group portrait, List<ComboBox<String>> opts) {
		portrait.getChildren().clear();
		int[] vals = new int[opts.size()];
		for (int q = 0; q < opts.size(); q++) {
			ComboBox<String> box = opts.get(q);
			if (box.getSelectionModel().getSelectedIndex() == box.getItems().size() - 1) {
				vals[q] = 0;
			} else {
				vals[q] = box.getSelectionModel().getSelectedIndex();
			}
		}
		PortraitMaker.makeHumanPortrait(portrait, vals);
	}
	public static void makePortrait(Group portrait, Unit muse) {
		if (muse instanceof Human) {
			Human h = (Human)muse;
			PortraitMaker.makeHumanPortrait(portrait, h);
		} else {
			//TODO monster portraits
		}
	}
	public static void makeHumanPortrait(boolean gender, int face, int lips, int nose, int ear,
			int eyes, int iris, int eyebrows, int hairstyle, int mustache, int beard, int hairColor,
			int skinColor, int eyeColor) {
		
	}
	public static void switchToWorldMap(final double paneX, final double paneY) {
		ROOT.getChildren().clear();
		GridPane grid = new GridPane();
		ScrollPane pane = new ScrollPane();
		
		Group labels = new Group();
		final Group tileGroup = new Group(new Label("Tile"));
		final Group buildGroup = new Group(new Label("Building"));
		final Group unitGroup = new Group(new Label("Unit"));
		final Group battleGroup = new Group(new Label("Battle"));
		final Group newsGroup = new Group(new Label("News"));
		buildGroup.setTranslateX(250);
		unitGroup.setTranslateX(500);
		battleGroup.setTranslateX(750);
		newsGroup.setTranslateX(1000);
		labels.getChildren().add(tileGroup);
		labels.getChildren().add(buildGroup);
		labels.getChildren().add(unitGroup);
		labels.getChildren().add(battleGroup);
		labels.getChildren().add(newsGroup);
		labels.setTranslateY(450);
		ROOT.getChildren().add(labels);
		
		WorldMap map = worldMap();
		for (int q = 0; q < WorldMap.SQRT_OF_MAP_SIZE; q++) {
			for (int w = 0; w < WorldMap.SQRT_OF_MAP_SIZE; w++) {
				final int x = q;
				final int y = w;
				Group tileRep = new Group();
				final WorldMapTile tile = map.at(q, w);
				decorateTileDefault(tileRep, tile, 0);
				//TODO button function
				tileRep.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						giveTileOptionInfo(tile, x, y, tileGroup, buildGroup, unitGroup,
								battleGroup, paneX, paneY);
					}
				});
				grid.add(tileRep, q, w);
			}
		}
		grid.setGridLinesVisible(true);
		pane.setContent(grid);
		pane.setMaxHeight(450);
		pane.setMaxWidth(1200);
		pane.setHvalue(paneX);
		pane.setVvalue(paneY);
		ROOT.getChildren().add(pane);
		

	}
	/**
	 * Create tile appearance
	 * @param tileRep
	 * @param tile
	 * @param traversalShade is 0 for none, 1 for blue (traversable) and 2 for red (attackable)
	 */
	public static void decorateTileDefault(Group tileRep, WorldMapTile tile, int traversalShade) {
		try {
			Rectangle rect = new Rectangle(50, 50);
			rect.setFill(tile.getType().getDisplayColor());
			if (tile.getAffiliation() != null) {
				if (tile.getAffiliation() == playerNation()) {
					rect.setStroke(Color.BLUE);
				} else if (playerNation().isAlliedWith(tile.getAffiliation())) {
					rect.setStroke(Color.GREEN);
				} else if (playerNation().isAtWarWith(tile.getAffiliation())) {
					rect.setStroke(Color.RED);
				} else {
					rect.setStroke(Color.YELLOW);
				}
			}
			tileRep.getChildren().add(rect);
			if (tile.getBuilding() != null) {
				Image im = new Image(new FileInputStream("buildings/" + tile.getBuilding().getType() + ".jpg"),
						45, 45, false, false);
				ImageView view = new ImageView(im);
				view.setTranslateX(2.5);
				view.setTranslateY(2.5);
				tileRep.getChildren().add(view);
			}
			if (traversalShade == 1) {
				Rectangle shade = new Rectangle(50, 50);
				shade.setFill(new Color(0, 0, 1, 0.25));
				tileRep.getChildren().add(shade);
			} else if (traversalShade == 2) {
				Rectangle shade = new Rectangle(50, 50);
				shade.setFill(new Color(1, 0, 0, 0.25));
				tileRep.getChildren().add(shade);
			}
			if (!tile.isVacant()) {
				Rectangle outline = new Rectangle(40, 40);
				outline.setTranslateX(5);
				outline.setTranslateY(5);
				WMTileOccupant unit = tile.getGroupPresent();
				if (unit.getAffiliation() == playerNation()) {
					outline.setFill(Color.BLUE);
				} else if (playerNation().isAlliedWith(unit.getAffiliation())) {
					outline.setFill(Color.GREEN);
				} else if (playerNation().isAtWarWith(unit.getAffiliation())) {
					outline.setFill(Color.RED);
				} else {
					outline.setFill(Color.YELLOW);
				}
				Image im = null;
				if (unit instanceof Ship) {
					im = new Image(new FileInputStream("units/ship.jpg"),
							30, 30, false, false);
				} else if (unit instanceof UnitGroup) {
					im = new Image(new FileInputStream("units/group.jpg"),
							30, 30, false, false);
				}
				ImageView view = new ImageView(im);
				view.setTranslateX(10);
				view.setTranslateY(10);
				tileRep.getChildren().add(outline);
				tileRep.getChildren().add(view);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public static void decorateTileMoving(Group tileRep, WorldMapTile tile, WorldMapTile dest, WMTileOccupant unit) {
		decorateTileDefault(tileRep, tile, 0);
		if (tile == dest) {
			Rectangle outline = new Rectangle(40, 40);
			outline.setTranslateX(5);
			outline.setTranslateY(5);
			if (unit.getAffiliation() == playerNation()) {
				outline.setFill(Color.LIGHTBLUE);
			} else if (playerNation().isAlliedWith(unit.getAffiliation())) {
				outline.setFill(Color.LIGHTGREEN);
			} else if (playerNation().isAtWarWith(unit.getAffiliation())) {
				outline.setFill(Color.ORANGE);
			} else {
				outline.setFill(Color.LIGHTYELLOW);
			}
			try {
				Image im = null;
				if (unit instanceof Ship) {
					im = new Image(new FileInputStream("units/ship.jpg"),
							30, 30, false, false);
				} else if (unit instanceof UnitGroup) {
					im = new Image(new FileInputStream("units/group.jpg"),
							30, 30, false, false);
				}
				ImageView view = new ImageView(im);
				view.setTranslateX(10);
				view.setTranslateY(10);
				tileRep.getChildren().add(outline);
				tileRep.getChildren().add(view);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
	
	public static void giveTileOptionInfo(final WorldMapTile tile, int x, int y,
			Group tileGroup, Group buildGroup, Group unitGroup, Group battleGroup,
			final double paneX, final double paneY) {
		// TODO Auto-generated method stub
		tileGroup.getChildren().clear();
		buildGroup.getChildren().clear();
		unitGroup.getChildren().clear();
		battleGroup.getChildren().clear();
		
		tileGroup.getChildren().add(new Label("Tile"));
		Label terrainLabel = new Label("Terrain: " + tile.getType().getName());
		Label magicLabel = new Label("Magic: " + tile.getMagicTypeAsString() + " (" + tile.getMagicPotency() + ")");
		terrainLabel.setTranslateY(25);
		magicLabel.setTranslateY(50);
		tileGroup.getChildren().add(terrainLabel);
		tileGroup.getChildren().add(magicLabel);
		if (tile.getAffiliation() != null) {
			Button nationLabel = new Button("Nation: " + tile.getAffiliation().getName());
			Label cityLabel = new Label("City: " + tile.getOwner().getName());
			nationLabel.setTranslateY(75);
			cityLabel.setTranslateY(100);
			tileGroup.getChildren().add(nationLabel);
			tileGroup.getChildren().add(cityLabel);
			nationLabel.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					// TODO go to nation page
				}
			});
			if (tile.getAffiliation() != playerNation()) {
				Button diplomacy = new Button("Diplomacy");
				diplomacy.setTranslateY(125);
				tileGroup.getChildren().add(diplomacy);
				diplomacy.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						// TODO go to diplomacy page
					}
				});
			}
		}
		
		buildGroup.getChildren().add(new Label("Building"));
		if (tile.getBuilding() != null) {
			Label nameLabel = new Label("Name: " + tile.getBuilding().getNameAndType());
			Label hpLabel = new Label("HP: " + tile.getBuilding().getCurrentHP() + "/" + tile.getBuilding().getMaximumHP());
			nameLabel.setTranslateY(25);
			hpLabel.setTranslateY(50);
			buildGroup.getChildren().add(nameLabel);
			buildGroup.getChildren().add(hpLabel);
			if (tile.getAffiliation() == playerNation()) {
				Button enter = new Button("Enter");
				final TextField newName = new TextField();
				Button rename = new Button("Rename");
				Button destroy = new Button("Destroy");
				enter.setTranslateY(75);
				newName.setTranslateY(100);
				rename.setTranslateY(100);
				rename.setTranslateX(150);
				destroy.setTranslateY(125);
				buildGroup.getChildren().add(enter);
				buildGroup.getChildren().add(newName);
				buildGroup.getChildren().add(rename);
				buildGroup.getChildren().add(destroy);
				enter.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						enterBuilding(tile.getBuilding(), paneX, paneY);
//						Thread testThread = new Thread(() -> {
//							Platform.runLater(() -> {
//								Rectangle test = new Rectangle(100, 100);
//								ROOT.getChildren().add(test);
//								long start = System.currentTimeMillis();
//								while(System.currentTimeMillis() < start + 2000) {
//								}
//								test.setFill(Color.RED);
//								while(System.currentTimeMillis() < start + 4000) {
//								}
//								test.setFill(Color.GREEN);
//							});
//						});

					}
				});
				rename.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						try {
							String name = validateInputName(newName.getText(), "New Name");
							tile.getBuilding().rename(name);
							newName.setText("");
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}
				});
				destroy.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						// TODO are you sure of destruction?
					}
				});
			}
		}
		
		unitGroup.getChildren().add(new Label("Unit"));
		if (tile.getGroupPresent() != null) {
			Button team = new Button();
			if (tile.getGroupPresent() instanceof UnitGroup) {
				UnitGroup group = (UnitGroup)tile.getGroupPresent();
				team.setText(group.getLeader().getName() + "'s Team");
			} else if (tile.getGroupPresent() instanceof Ship) {
				Ship ship = (Ship)tile.getGroupPresent();
				team.setText(ship.assignedGroup().getLeader().getName() + "'s Ship");
			}
			Button aff = new Button("Affiliation: " + tile.getGroupPresent().getAffiliation().getName());
			Label move = new Label("Movement: " + tile.getGroupPresent().getMovement());
			team.setTranslateY(25);
			aff.setTranslateY(50);
			move.setTranslateY(75);
			unitGroup.getChildren().add(team);
			unitGroup.getChildren().add(aff);
			unitGroup.getChildren().add(move);
			team.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					//TODO to team page
				}
			});
			aff.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					//TODO to nation page
				}
			});

			if (tile.getGroupPresent().getAffiliation() == playerNation()) {
				Button order = getUnitOrders(tile.getGroupPresent(), x, y, paneX, paneY);
				order.setTranslateY(100);
				unitGroup.getChildren().add(order);
				order.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						//TODO perform orders
					}
				});
			}
		}
		
		battleGroup.getChildren().add(new Label("Battle"));
		if (tile.getBattle() != null) {
			GridPane parts = new GridPane();
			for (int q = 0; q < tile.getBattle().getCombatants().size(); q++) {
				WMTileOccupant occ = tile.getBattle().getCombatants().get(q);
				Button team = new Button(occ.getLeader().getName() + " (" + occ.getAffiliation().getName() + ")");
				team.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						//TODO to team page
					}
				});
				parts.add(team, 0, q);
			}
			ScrollPane pane = new ScrollPane(parts);
			pane.setVmax(100);
			pane.setHmax(200);
			Button enter = new Button("Enter");
			enter.setTranslateY(125);
			battleGroup.getChildren().add(pane);
			battleGroup.getChildren().add(enter);
			enter.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					//TODO to battle page
				}
			});
		}
	}
	
	private static Button getUnitOrders(final WMTileOccupant occ, final int x, final int y,
			final double paneX, final double paneY) {
		
		//TODO figure out the  pane positioning
		
		Button orders = new Button("Orders");
		orders.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				final List<Button> possibleOrders = new List<>();
				ROOT.getChildren().clear();
				GridPane grid = new GridPane();
				final ScrollPane pane = new ScrollPane();

				if (occ instanceof UnitGroup) {
					final UnitGroup group = (UnitGroup)occ;
					HashMap<WorldMapTile, Integer> traversable;
					if (GeneralGameplayManager.eventTime()) {
						traversable = GeneralGameplayManager.getTraversableWorldMapTilesEventTime(group, x, y);
					} else {
						traversable = GeneralGameplayManager.getTraversableWorldMapTilesPeaceTime(group, x, y);
					}
					for (int q = 0; q < WorldMap.SQRT_OF_MAP_SIZE; q++) {
						for (int w = 0; w < WorldMap.SQRT_OF_MAP_SIZE; w++) {
							Group tileRep = new Group();
							final int destX = q;
							final int destY = w;
							if (traversable.get(worldMap().at(destX, destY)) == null) {
								decorateTileDefault(tileRep, worldMap().at(destX, destY), 0);
							} else {
								decorateTileDefault(tileRep, worldMap().at(destX, destY), 1);
								tileRep.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
									@Override
									public void handle(MouseEvent event) {
										GridPane newGrid = new GridPane();
										double newPaneX = pane.getHvalue();
										double newPaneY = pane.getVvalue();
										GridPane showOrders = new GridPane();
										final WorldMapTile here = worldMap().at(destX, destY);
										for (int q = 0; q < WorldMap.SQRT_OF_MAP_SIZE; q++) {
											for (int w = 0; w < WorldMap.SQRT_OF_MAP_SIZE; w++) {
												Group tileRep = new Group();
												decorateTileMoving(tileRep, worldMap().at(q, w), here, occ);
												newGrid.add(tileRep, q, w);
											}
										}
										//TODO somehow figure out how to add grid lines here
										
										final List<WorldMapTile> adjacentTilesWithAllies = worldMap().getAdjacentTilesWithAllies(group, destX, destY);
										List<WorldMapTile> adjacentTilesWithUnassignedShips =
												worldMap().getAdjacentTilesWithUnassignedShips(group, destX, destY);
										Button standby = new Button("Standby");
										standby.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
											@Override
											public void handle(MouseEvent event) {
												here.sendHere(group);
												group.exhaust();
												switchToWorldMap(paneX, paneY);
											}
										});
										possibleOrders.add(standby);
										
										if (!(adjacentTilesWithAllies.isEmpty())) {
											Button trade = new Button("Trade");
											trade.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
												@Override
												public void handle(MouseEvent event) {
													GridPane newGrid = new GridPane();
													double newPaneX = pane.getHvalue();
													double newPaneY = pane.getVvalue();
													for (int q = 0; q < WorldMap.SQRT_OF_MAP_SIZE; q++) {
														for (int w = 0; w < WorldMap.SQRT_OF_MAP_SIZE; w++) {
															Group tile = new Group();
															final int actX = q;
															final int actY = w;
															if (adjacentTilesWithAllies.contains(worldMap().at(actX, actY))) {
																decorateTileDefault(tile, worldMap().at(actX, actY), 1);
																tile.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
																	@Override
																	public void handle(MouseEvent event) {
																		here.sendHere(group);
																		group.exhaust();
																		//TODO open trading menu
																	}
																});
															} else {
																decorateTileMoving(tile, worldMap().at(actX, actY), here, group);
															}
															newGrid.add(tile, actX, actY);
														}
													}
													newGrid.setGridLinesVisible(true);
													pane.setContent(newGrid);
													pane.setHvalue(newPaneX);
													pane.setVvalue(newPaneY);
												}
											});
											possibleOrders.add(trade);
											Button transUnit = new Button("Transer Units");
											transUnit.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
												@Override
												public void handle(MouseEvent event) {
													GridPane newGrid = new GridPane();
													double newPaneX = pane.getHvalue();
													double newPaneY = pane.getVvalue();
													for (int q = 0; q < WorldMap.SQRT_OF_MAP_SIZE; q++) {
														for (int w = 0; w < WorldMap.SQRT_OF_MAP_SIZE; w++) {
															Group tile = new Group();
															final int actX = q;
															final int actY = w;
															if (adjacentTilesWithAllies.contains(worldMap().at(actX, actY))) {
																decorateTileDefault(tile, worldMap().at(actX, actY), 1);
																tile.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
																	@Override
																	public void handle(MouseEvent event) {
																		here.sendHere(group);
																		group.exhaust();
																		//TODO open tranfer menu
																	}
																});
															} else {
																decorateTileMoving(tile, worldMap().at(actX, actY), here, group);
															}
															newGrid.add(tile, actX, actY);
														}
													}
													newGrid.setGridLinesVisible(true);
													pane.setContent(newGrid);
													pane.setHvalue(newPaneX);
													pane.setVvalue(newPaneY);
												}
											});
											possibleOrders.add(transUnit);
											Button transPrisoner = new Button("Transfer Prisoners");
											transPrisoner.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
												@Override
												public void handle(MouseEvent event) {
													GridPane newGrid = new GridPane();
													double newPaneX = pane.getHvalue();
													double newPaneY = pane.getVvalue();
													for (int q = 0; q < WorldMap.SQRT_OF_MAP_SIZE; q++) {
														for (int w = 0; w < WorldMap.SQRT_OF_MAP_SIZE; w++) {
															Group tile = new Group();
															final int actX = q;
															final int actY = w;
															UnitGroup other;
															if (adjacentTilesWithAllies.contains(worldMap().at(actX, actY))) {
																other = (UnitGroup)worldMap().at(actX, actY).getGroupPresent();
															} else {
																other = null;
															}
															if (other != null && (group.getPrisoners() != null) || other.getPrisoners() != null) {
																decorateTileDefault(tile, worldMap().at(actX, actY), 1);
																tile.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
																	@Override
																	public void handle(MouseEvent event) {
																		here.sendHere(group);
																		group.exhaust();
																		//TODO open prisoner transfer menu
																	}
																});
															} else {
																decorateTileMoving(tile, worldMap().at(actX, actY), here, group);
															}
															newGrid.add(tile, actX, actY);
														}
													}
													newGrid.setGridLinesVisible(true);
													pane.setContent(newGrid);
													pane.setHvalue(newPaneX);
													pane.setVvalue(newPaneY);
												}
											});
											possibleOrders.add(transPrisoner);
										}
										
										if (group.size() > 1 && worldMap().getAdjacentUnoccupiedTile(destX, destY) != null) {
											Button split = new Button("Divide Team");
											split.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
												@Override
												public void handle(MouseEvent event) {
													List<WorldMapTile> allAdjacent = worldMap().getAllTraversableAdjacentTiles(group, destX, destY);
													GridPane newGrid = new GridPane();
													double newPaneX = pane.getHvalue();
													double newPaneY = pane.getVvalue();
													for (int q = 0; q < WorldMap.SQRT_OF_MAP_SIZE; q++) {
														for (int w = 0; w < WorldMap.SQRT_OF_MAP_SIZE; w++) {
															final int actX = q;
															final int actY = w;
															Group tile = new Group();
															if (allAdjacent.contains(worldMap().at(actX, actY))) {
																decorateTileDefault(tile, worldMap().at(actX, actY), 1);
																tile.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
																	@Override
																	public void handle(MouseEvent event) {
																		here.sendHere(group);
																		group.exhaust();
																		//TODO split via transfer units menu
																	}
																});
															} else {
																decorateTileMoving(tile, worldMap().at(actX, actY), here, group);
															}
															newGrid.add(tile, actX, actY);
														}
													}
													newGrid.setGridLinesVisible(true);
													pane.setContent(newGrid);
													pane.setHvalue(newPaneX);
													pane.setVvalue(newPaneY);
												}
											});
											possibleOrders.add(split);
										}
										
										if (here.getOwner() != null && here.getOwner().getNation() == group.getAffiliation()
												&& here.getBuilding() != null) {
											
											if (here.getBuilding() instanceof Defendable
													&& ((Defendable)here.getBuilding()).getAssignedGroup() == null) {
												Button assign = new Button("Defend Building");
												assign.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
													@Override
													public void handle(MouseEvent event) {
														// TODO Auto-generated method stub
														here.sendHere(group);
														group.exhaust();
														here.removeGroupOrShip();
														group.giveAssignment((Defendable)here.getBuilding());
														switchToWorldMap(pane.getHvalue(), pane.getVvalue());
													}
												});
												possibleOrders.add(assign);
												
												if (here.getBuilding() instanceof Prison
														&& group.getPrisoners() != null
														&& ((Prison)here.getBuilding()).canAcceptPrisonersFromGroup(group)) {
													Button imprison = new Button("Deposit Prisoners");
													imprison.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
														@Override
														public void handle(MouseEvent event) {
															here.sendHere(group);
															group.exhaust();
															((Prison)here.getBuilding()).acceptPrisonersFromGroup(group);
														}
													});
													possibleOrders.add(imprison);
												}

											}

										}
										
										if (!(adjacentTilesWithUnassignedShips.isEmpty())) {
											Button assign = new Button("Enter Ship");
											assign.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
												@Override
												public void handle(MouseEvent event) {
													here.sendHere(group);
													group.exhaust();
													//TODO assign to ship
												}
											});
											possibleOrders.add(assign);
										}
										
										if (GeneralGameplayManager.eventTime()) {
											final List<WorldMapTile> adjacentTilesWithEnemies = worldMap().getAdjacentTilesWithAttackableEnemies(group, destX, destY);
											if (!(adjacentTilesWithEnemies.isEmpty())) {
												Button engage = new Button("Engage Enemy");
												engage.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
													@Override
													public void handle(MouseEvent event) {
														GridPane newGrid = new GridPane();
														final double newPaneX = pane.getHvalue();
														final double newPaneY = pane.getVvalue();
														for (int q = 0; q < WorldMap.SQRT_OF_MAP_SIZE; q++) {
															for (int w = 0; w < WorldMap.SQRT_OF_MAP_SIZE; w++) {
																Group tile = new Group();
																final int actX = q;
																final int actY = w;
																if (adjacentTilesWithEnemies.contains(worldMap().at(q, w))) {
																	decorateTileDefault(tile, worldMap().at(actX, actY), 2);
																	tile.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
																		@Override
																		public void handle(MouseEvent event) {
																			here.sendHere(group);
																			group.exhaust();
																			WorldMapTile otherTile = worldMap().at(actX, actY);
																			WMTileOccupant otherGroup;
																			if (otherTile.getGroupPresent() == null) {
																				otherGroup = ((Defendable)otherTile.getBuilding()).getAssignedGroup();
																			} else {
																				otherGroup = otherTile.getGroupPresent();
																			}
																			int xDiff = destX - actX;
																			WMTileOccupant firstGroup;
																			WMTileOccupant secondGroup;
																			boolean horizontal;
																			WorldMapTile firstTile;
																			WorldMapTile secondTile;
																			if (xDiff == 0) {
																				horizontal = false;
																				if (destY - actY < 0) {
																					firstGroup = group;
																					firstTile = here;
																					secondGroup = otherGroup;
																					secondTile = otherTile;
																				} else {
																					firstGroup = otherGroup;
																					firstTile = otherTile;
																					secondGroup = group;
																					secondTile = here;
																				}
																			} else {
																				horizontal = true;
																				if (xDiff < 0) {
																					firstGroup = group;
																					firstTile = here;
																					secondGroup = otherGroup;
																					secondTile = otherTile;
																				} else {
																					firstGroup = otherGroup;
																					firstTile = otherTile;
																					secondGroup = group;
																					secondTile = here;
																				}
																			}
																			new BattleGround(new WorldMapTile[] {firstTile, secondTile},
																					horizontal,
																					new WMTileOccupant[] {firstGroup, secondGroup});
																			switchToWorldMap(newPaneX, newPaneY);
																		}
																	});
																} else {
																	decorateTileDefault(tile, worldMap().at(actX, actY), 0);
																}
																newGrid.add(tile, actX, actY);
															}
														}
														newGrid.setGridLinesVisible(true);
														pane.setContent(newGrid);
														pane.setHvalue(newPaneX);
														pane.setVvalue(newPaneY);
													}
												});
												possibleOrders.add(engage);
											}
											if (here.getOwner() != null
													&& group.getAffiliation().isAtWarWith(here.getOwner().getNation())
													&& here.getBuilding() != null
													&& (!(here.getBuilding() instanceof Defendable)
															|| ((Defendable)here.getBuilding()).getAssignedGroup() == null)) {
												Button destroy = new Button("Destroy Building");
												destroy.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
													@Override
													public void handle(MouseEvent event) {
														here.sendHere(group);
														group.exhaust();
														int[] power = group.getPower();
														here.getBuilding().takeDamage(false, power[0]);
														here.getBuilding().takeDamage(true, power[1]);
														if (here.getBuilding().getCurrentHP() <= 0) {
															//TODO get rid of building
														}
													}
												});
												possibleOrders.add(destroy);
												if (here.getBuilding() instanceof Castle) {
													Button seize = new Button("Seize");
													seize.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
														@Override
														public void handle(MouseEvent event) {
															here.sendHere(group);
															group.exhaust();
															//TODO seize control of city
														}
													});
													possibleOrders.add(seize);
												}
											}
										}
										possibleOrders.add(worldMapButton("Cancel", newPaneX, newPaneY));
										
										newGrid.setGridLinesVisible(true);
										pane.setContent(newGrid);
										pane.setHvalue(newPaneX);
										pane.setVvalue(newPaneY);
										
										for (int q = 0; q < possibleOrders.size(); q++) {
											showOrders.add(possibleOrders.get(q), q % 4, q / 4);
										}
										showOrders.setHgap(50);
										showOrders.setTranslateX(100);
										showOrders.setTranslateY(450);
										ROOT.getChildren().add(showOrders);
									}
								});
							}
							
							grid.add(tileRep, destX, destY);
						}
					}
				} else if (occ instanceof Ship) {
					final Ship group = (Ship)occ;
					HashMap<WorldMapTile, Integer> traversable =
							GeneralGameplayManager.getTraversableTilesForShipPeaceTime(group, x, y);
					GridPane newGrid = new GridPane();
					final double newPaneX = pane.getHvalue();
					final double newPaneY = pane.getVvalue();
					for (int q = 0; q < WorldMap.SQRT_OF_MAP_SIZE; q++) {
						for (int w = 0; w < WorldMap.SQRT_OF_MAP_SIZE; w++) {
							Group tile = new Group();
							final int destX = q;
							final int destY = w;
							if (traversable.get(worldMap().at(q, w)) == null) {
								decorateTileDefault(tile, worldMap().at(destX, destY), 0);
							} else {
								decorateTileDefault(tile, worldMap().at(destX, destY), 1);
								tile.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
									@Override
									public void handle(MouseEvent event) {
										final WorldMapTile here = worldMap().at(destX, destY);
										GridPane newGrid = new GridPane();
										final double newPaneX = pane.getHvalue();
										final double newPaneY = pane.getVvalue();
										GridPane showOrders = new GridPane();
										for (int q = 0; q < WorldMap.SQRT_OF_MAP_SIZE; q++) {
											for (int w = 0; w < WorldMap.SQRT_OF_MAP_SIZE; w++) {
												Group tile = new Group();
												decorateTileDefault(tile, worldMap().at(q, w), 0);
												newGrid.add(tile, q, w);
											}
										}
										List<WorldMapTile> adjacentTilesWithAllies = worldMap().getAdjacentTilesWithAllies(group, destX, destY);
										List<WorldMapTile> adjacentTraversableTiles = worldMap().getAllTraversableAdjacentTiles(group, destX, destY);
										List<WorldMapTile> adjacentTilesWithPorts = new List<>();
										List<WorldMapTile> adjacent = worldMap().getAllAdjacentTiles(destX, destY);
										for (int q = 0; q < adjacent.size(); q++) {
											WorldMapTile current = adjacent.get(q);
											if (current.getAffiliation() == group.getAffiliation()
													&& current.getBuilding() instanceof Port
													&& !((Port)current.getBuilding()).isFull()) {
												adjacentTilesWithPorts.add(current);
											}
										}
										if (!(adjacentTraversableTiles.isEmpty())) {
											Button drop = new Button("Drop Team");
											drop.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
												@Override
												public void handle(MouseEvent event) {
													// TODO Auto-generated method stub
												}
											});
											possibleOrders.add(drop);
										}
										if (!(adjacentTilesWithAllies.isEmpty())) {
											if (!group.getPrisoners().isEmpty()) {
												Button transPrisoner = new Button("Transfer Prisoners");
												transPrisoner.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
													@Override
													public void handle(MouseEvent event) {
														// TODO Auto-generated method stub
													}
												});
												possibleOrders.add(transPrisoner);
											}
											Button trade = new Button("Trade");
											trade.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
												@Override
												public void handle(MouseEvent event) {
													// TODO Auto-generated method stub
												}
											});
											possibleOrders.add(trade);
										}
										if (!(adjacentTilesWithPorts.isEmpty())) {
											Button ports = new Button("Enter Port");
											ports.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
												@Override
												public void handle(MouseEvent event) {
													// TODO Auto-generated method stub
												}
											});
											possibleOrders.add(ports);
										}
										
										if (GeneralGameplayManager.eventTime()) {
											List<WorldMapTile> adjacentAttackableTiles = worldMap().getAdjacentTilesWithAttackableEnemies(group, destX, destY);
											List<WorldMapTile> attackableFromWorldMap = worldMap().getTilesAttackableWithShip(group, destX, destY);
											if (!(adjacentAttackableTiles.isEmpty())) {
												Button engage = new Button("Engage");
												engage.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
													@Override
													public void handle(MouseEvent event) {
														// TODO Auto-generated method stub
													}
												});
												possibleOrders.add(engage);
											}
											if (!(attackableFromWorldMap.isEmpty())) {
												Button attack = new Button("Attack");
												attack.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
													@Override
													public void handle(MouseEvent event) {
														// TODO Auto-generated method stub
													}
												});
												possibleOrders.add(attack);
											}
										}
										
										possibleOrders.add(worldMapButton("Cancel", newPaneX, newPaneY));
										
										newGrid.setGridLinesVisible(true);
										pane.setContent(newGrid);
										pane.setHvalue(newPaneX);
										pane.setVvalue(newPaneY);
										
										for (int q = 0; q < possibleOrders.size(); q++) {
											showOrders.add(possibleOrders.get(q), q % 4, q / 4);
										}
										showOrders.setHgap(50);
										showOrders.setTranslateX(100);
										showOrders.setTranslateY(450);
										ROOT.getChildren().add(showOrders);

									}
								});
							}
							newGrid.add(tile, destX, destY);
						}
					}
					newGrid.setGridLinesVisible(true);
					pane.setContent(newGrid);
					pane.setHvalue(newPaneX);
					pane.setVvalue(newPaneY);
				}
				
				grid.setGridLinesVisible(true);
				pane.setContent(grid);
				pane.setMaxHeight(450);
				pane.setMaxWidth(1200);
				pane.setHvalue(paneX);
				pane.setVvalue(paneY);
				ROOT.getChildren().add(pane);
			}
		});
		
		return orders;
	}
	
	public static void showUnitStats(final Unit u, final EventHandler<MouseEvent> back) {
		ROOT.getChildren().clear();
		try {
			Image im = new Image(new FileInputStream("stats/unitStats.jpg"),
					1200, 580, false, false);
			ImageView view = new ImageView(im);
			ROOT.getChildren().add(view);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		Rectangle backRect = new Rectangle(1200, 20);
		backRect.setFill(Color.CYAN);
		backRect.addEventFilter(MouseEvent.MOUSE_CLICKED, back);
		Label sayBack = new Label("Return");
		backRect.setTranslateY(580);
		sayBack.setTranslateY(580);
		ROOT.getChildren().add(backRect);
		ROOT.getChildren().add(sayBack);
		
		Rectangle frame = new Rectangle(200, 200);
		frame.setFill(Color.WHITE);
		Group portrait = new Group();
		makePortrait(portrait, u);
		ROOT.getChildren().add(frame);
		ROOT.getChildren().add(portrait);
		
		GridPane basicInfo = new GridPane();
		basicInfo.add(new Label("Name"), 0, 0);
		basicInfo.add(new Label("Class"), 1, 0);
		basicInfo.add(new Label("Level (LVL)"), 2, 0);
		basicInfo.add(new Label("Experience (EXP)"), 3, 0);
		basicInfo.add(new Label("Find Unit"), 4, 0);
		basicInfo.add(new Label(u.getDisplayName()), 0, 1);
		basicInfo.add(new Label(u.getUnitClassName()), 1, 1);
		basicInfo.add(new Label("" + u.getLevel()), 2, 1);
		basicInfo.add(new Label("" + u.getExperience()), 3, 1);
		Button locate = new Button("Locate");
		locate.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				// TODO Auto-generated method stub
			}
		});
		basicInfo.add(locate, 4, 1);
		basicInfo.add(new Label("Attack (ATK)"), 0, 2);
		basicInfo.add(new Label("Accuracy (ACC)"), 1, 2);
		basicInfo.add(new Label("Avoidance (AVO)"), 2, 2);
		basicInfo.add(new Label("Critical Hit (CRT)"), 3, 2);
		basicInfo.add(new Label("Critical Security (SEC)"), 4, 2);
		basicInfo.add(new Label("" + u.attackStrength()), 0, 3);
		basicInfo.add(new Label("" + u.accuracy()), 1, 3);
		basicInfo.add(new Label("" + u.avoidance(Unit.TORSO)), 2, 3);
		basicInfo.add(new Label("" + u.criticalHitRate()), 3, 3);
		basicInfo.add(new Label("" + u.criticalHitAvoid()), 4, 3);
		basicInfo.setGridLinesVisible(true);
		basicInfo.setHgap(5);
		Rectangle basicBack = new Rectangle(550, 100);
		basicBack.setFill(Color.WHITE);
		basicBack.setTranslateX(250);
		basicInfo.setTranslateX(265);
		ROOT.getChildren().add(basicBack);
		ROOT.getChildren().add(basicInfo);
		
		GridPane stats = new GridPane();
		stats.add(new Label("Stats"), 0, 0);
		stats.add(new Label("Value"), 1, 0);
		stats.add(new Label("Growth Rate"), 2, 0);
		stats.add(new Label("Magic (MAG)"), 0, 1);
		stats.add(new Label("" + u.getMagic()), 1, 1);
		stats.add(new Label(u.getMagicGrowth() + "%"), 2, 1);
		stats.add(new Label("Skill (SKL)"), 0, 2);
		stats.add(new Label("" + u.getSkill()), 1, 2);
		stats.add(new Label(u.getSkillGrowth() + "%"), 2, 2);
		stats.add(new Label("Reflex (RFX)"), 0, 3);
		stats.add(new Label("" + u.getReflex()), 1, 3);
		stats.add(new Label(u.getReflexGrowth() + "%"), 2, 3);
		stats.add(new Label("Awareness (AWR)"), 0, 4);
		stats.add(new Label("" + u.getAwareness()), 1, 4);
		stats.add(new Label(u.getAwarenessGrowth() + "%"), 2, 4);
		stats.add(new Label("Resistance (RES)"), 0, 5);
		stats.add(new Label("" + u.getResistance()), 1, 5);
		stats.add(new Label(u.getResistanceGrowth() + "%"), 2, 5);
		stats.add(new Label("Movement (MOV)"), 0, 6);
		stats.add(new Label("" + u.getMovement()), 1, 6);
		stats.add(new Label("Leadership (LDR)"), 0, 7);
		stats.add(new Label("" + u.getLeadership()), 1, 7);
		stats.setHgap(50);
		stats.setVgap(15);
		stats.setGridLinesVisible(true);
		stats.setTranslateY(250);
		Rectangle statsBack = new Rectangle(300, 245);
		statsBack.setFill(Color.WHITE);
		statsBack.setTranslateY(250);
		ROOT.getChildren().add(statsBack);
		ROOT.getChildren().add(stats);
		
		GridPane bodyParts = new GridPane();
		bodyParts.add(new Label("Body Part"), 0, 0);
		bodyParts.add(new Label("Health"), 1, 0);
		bodyParts.add(new Label("Growth Rate"), 2, 0);
		bodyParts.add(new Label("Armor"), 3, 0);
		for (int q = 0; q < u.getBodyPartsNames().length; q++) {
			bodyParts.add(new Label(u.getBodyPartsNames()[q]), 0, q + 1);
			bodyParts.add(new Label(u.getCurrentHPOfBodyPart(q) + "/" + u.getMaximumHPOfBodyPart(q)), 1, q + 1);
			bodyParts.add(new Label(u.getGrowthRateOfBodyPart(q) + "%"), 2, q + 1);
			bodyParts.add(new Label(u.defense(false, q) + ""), 3, q + 1);
		}
		bodyParts.setHgap(30);
		bodyParts.setVgap(15);
		bodyParts.setGridLinesVisible(true);
		Rectangle backParts = new Rectangle(300, 310);
		backParts.setFill(Color.WHITE);
		backParts.setTranslateX(300);
		backParts.setTranslateY(220);
		bodyParts.setTranslateX(305);
		bodyParts.setTranslateY(220);
		ROOT.getChildren().add(backParts);
		ROOT.getChildren().add(bodyParts);
		
		Group bodyDiagram = getBodyDiagram(u);
		bodyDiagram.setTranslateX(650);
		bodyDiagram.setTranslateY(220);
		ROOT.getChildren().add(bodyDiagram);
		
		if (u instanceof Equippable) {
			Equippable eq = (Equippable)u;
			GridPane profs = new GridPane();
			profs.add(new Label("Weapon"), 0, 0);
			profs.add(new Label("Sword"), 0, 1);
			profs.add(new Label("Spear"), 0, 2);
			profs.add(new Label("Axe"), 0, 3);
			profs.add(new Label("Bow"), 0, 4);
			profs.add(new Label("Knife"), 0, 5);
			profs.add(new Label("Ballista"), 0, 6);
			profs.add(new Label("Earth"), 0, 7);
			profs.add(new Label("Light"), 0, 8);
			profs.add(new Label("Dark"), 0, 9);
			profs.add(new Label("Staff"), 0, 10);
			profs.add(new Label("Proficiency"), 1, 0);
			//TODO Figure out how to change this magic number in the for loop
			for (int q = 0; q < 10; q++) {
				profs.add(new Label(eq.proficiencyWith(q) + ""), 1, q + 1);
			}
			profs.setHgap(30);
			Rectangle backProfs = new Rectangle(200, 270);
			backProfs.setFill(Color.WHITE);
			backProfs.setTranslateX(850);
			backProfs.setTranslateY(200);
			profs.setTranslateX(870);
			profs.setTranslateY(220);
			ROOT.getChildren().add(backProfs);
			ROOT.getChildren().add(profs);
		}
		
		Button page2 = new Button("PAGE 2 >>");
		page2.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				showUnitStatsPage2(u, back);
			}
		});
		page2.setTranslateX(1000);
		page2.setTranslateY(530);
		ROOT.getChildren().add(page2);
	}
	
	public static Group getBodyDiagram(Unit u) {
		Group diagram = new Group();
		
		if (u instanceof Human) {
			Human h = (Human)u;
			Rectangle head = new Rectangle(70, 70);
			Rectangle torso = new Rectangle(50, 100);
			Rectangle right_arm = new Rectangle(50, 30);
			Rectangle left_arm = new Rectangle(50, 30);
			Rectangle right_leg = new Rectangle(25, 50);
			Rectangle left_leg = new Rectangle(25, 50);
			Rectangle right_eye = new Rectangle(10, 5);
			Rectangle left_eye = new Rectangle(10, 5);
			Rectangle mount = new Rectangle(100, 30);
			head.setTranslateX(40);
			torso.setTranslateX(50);
			torso.setTranslateY(70);
			right_arm.setTranslateY(70);
			left_arm.setTranslateX(100);
			left_arm.setTranslateY(70);
			right_leg.setTranslateX(50);
			right_leg.setTranslateY(120);
			left_leg.setTranslateX(75);
			left_leg.setTranslateY(120);
			right_eye.setTranslateX(60);
			right_eye.setTranslateY(30);
			left_eye.setTranslateX(80);
			left_eye.setTranslateY(30);
			mount.setTranslateX(25);
			mount.setTranslateY(170);
			List<Rectangle> parts = new List<>();
			parts.add(head);
			parts.add(torso);
			parts.add(right_arm);
			parts.add(left_arm);
			parts.add(right_leg);
			parts.add(left_leg);
			parts.add(right_eye);
			parts.add(left_eye);
			parts.add(mount);
			for (int q = 0; q < parts.size(); q++) {
				if (h.getMaximumHPOfBodyPart(q) <= 0) {
					parts.get(q).setFill(Color.GREY);
				} else {
					double percentHealth = h.percentHealthOfPart(q);
					int red = Math.max(0, Math.min(1, (int)Math.round((1 - percentHealth))));
					int green = Math.max(0, Math.min(1, (int)Math.round(percentHealth)));
					parts.get(q).setFill(new Color(red, green, 0, 1));
				}
				parts.get(q).setStroke(Color.BLACK);
				diagram.getChildren().add(parts.get(q));
			}
		} else {
			//TODO monster diagrams
		}
		return diagram;
	}
		
	public static void showUnitStatsPage2(final Unit u, final EventHandler<MouseEvent> back) {
		ROOT.getChildren().clear();
		try {
			Image im = new Image(new FileInputStream("stats/unitStats.jpg"),
					1200, 580, false, false);
			ImageView view = new ImageView(im);
			ROOT.getChildren().add(view);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		Rectangle backRect = new Rectangle(1200, 20);
		backRect.setFill(Color.CYAN);
		backRect.addEventFilter(MouseEvent.MOUSE_CLICKED, back);
		Label sayBack = new Label("Return");
		backRect.setTranslateY(580);
		sayBack.setTranslateY(580);
		ROOT.getChildren().add(backRect);
		ROOT.getChildren().add(sayBack);
		
		Rectangle frame = new Rectangle(200, 200);
		frame.setFill(Color.WHITE);
		Group portrait = new Group();
		makePortrait(portrait, u);
		ROOT.getChildren().add(frame);
		ROOT.getChildren().add(portrait);
		
		GridPane stats = new GridPane();
		stats.add(new Label("National Affiliation"), 0, 0);
		stats.add(new Label(u.getAffiliation().getFullName()), 1, 0);
		Button nationStats = new Button("View Nation");
		nationStats.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				// TODO nation stats panel
			}
		});
		stats.add(nationStats, 2, 0);
		stats.add(new Label("Team Leader"), 0, 1);
		if (u.getGroup() == null) {
			stats.add(new Label("None"), 1, 1);
		} else {
			stats.add(new Label(u.getGroup().getLeader().getDisplayName()), 1, 1);
			Button teamStats = new Button("View Team");
			teamStats.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					// TODO Auto-generated method stub
				}
			});
			stats.add(teamStats, 2, 1);
		}
		if (u instanceof Human) {
			final Human h = (Human)u;
			stats.add(new Label("Support Partner"), 0, 2);
			if (h.getSupportPartner() == null) {
				stats.add(new Label("None"), 1, 2);
			} else {
				stats.add(new Label(h.getSupportPartnerName()), 1, 2);
				Button supportStats = new Button("View Partner");
				supportStats.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						showUnitStats(h.getSupportPartner(), back);
					}
				});
				stats.add(supportStats, 2, 2);
			}
			stats.add(new Label("Relationship with Player"), 0, 3);
			if (h == player()) {
				stats.add(new Label("--"), 1, 3);
			} else {
				stats.add(new Label(h.getRelationshipWithPlayer() + ""), 1, 3);
				Button interact = new Button("Interact");
				interact.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						// TODO Auto-generated method stub
					}
				});
				stats.add(interact, 2, 3);
			}
		} else {
			stats.add(new Label(), 0, 2);
			stats.add(new Label(), 0, 3);
		}
		stats.add(new Label("Carrying"), 0, 4);
		if (u.getPassenger() == null) {
			stats.add(new Label("None"), 1, 4);
		} else {
			stats.add(new Label(u.getPassenger().getDisplayName()), 1, 4);
		}
		if (u instanceof Human) {
			Human h = (Human)u;
			stats.add(new Label("Gender"), 0, 5);
			stats.add(new Label(h.getGenderAsString()), 1, 5);
			if (h.isMortal()) {
				stats.add(new Label("Age"), 0, 6);
				stats.add(new Label(h.getAge() + ""), 1, 6);
				stats.add(new Label("Mortal"), 2, 6);
			} else {
				stats.add(new Label("Apparent Age"), 0, 6);
				stats.add(new Label(h.getAge() + ""), 1, 6);
				stats.add(new Label("Immortal"), 2, 6);
			}
		} else {
			stats.add(new Label(), 0, 5);
			stats.add(new Label(), 0, 6);
		}
		stats.add(new Label("Morale"), 0, 7);
		stats.add(new Label(u.getMorale() + ""), 1, 7);
		if (u instanceof Human) {
			//Ugh, I should probably just put all of the Human-only stats together
			//But I probably won't
			Human h = (Human)u;
			stats.add(new Label("Supported Trait"), 0, 8);
			stats.add(new Label(h.getValuedTraitAsString()), 1, 8);
			stats.add(new Label("Demeanor"), 0, 9);
			stats.add(new Label(h.getDemeanorAsString()), 1, 9);
		} else {
			stats.add(new Label(), 0, 8);
			stats.add(new Label(), 0, 9);
		}
		stats.add(new Label("Wars"), 0, 10);
		stats.add(new Label(u.getWars() + ""), 1, 10);
		stats.add(new Label("Battles"), 0, 11);
		stats.add(new Label(u.getBattles() + ""), 1, 11);
		stats.add(new Label("Kills"), 0, 12);
		stats.add(new Label(u.getKills() + ""), 1, 12);
		stats.add(new Label("Important Character?"), 0, 13);
		if (u.isImportant()) {
			stats.add(new Label("Yes"), 1, 13);
		} else {
			stats.add(new Label("No"), 1, 13);
		}
		if (u == player()) {
			stats.add(new Label("Locked"), 2, 13);
		} else {
			Button toggle = new Button("Toggle");
			toggle.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					u.toggleImportance();
					showUnitStatsPage2(u, back);
				}
			});
			stats.add(toggle, 2, 13);
		}
		stats.setHgap(30);
		stats.setVgap(20);
		Rectangle backStats = new Rectangle(410, 520);
		backStats.setFill(Color.WHITE);
		stats.setTranslateX(250);
		backStats.setTranslateX(245);
		ROOT.getChildren().add(backStats);
		ROOT.getChildren().add(stats);
		
		if (u == player() || u == player().getSupportPartner()) {
			Human h = (Human)u;
			GridPane traits = new GridPane();
			traits.add(new Label("Militarism"), 0, 0);
			traits.add(new Label(h.getMilitarism() + ""), 1, 0);
			traits.add(new Label("Altruism"), 0, 1);
			traits.add(new Label(h.getAltruism() + ""), 1, 1);
			traits.add(new Label("Familism"), 0, 2);
			traits.add(new Label(h.getFamilism() + ""), 1, 2);
			traits.add(new Label("Nationalism"), 0, 3);
			traits.add(new Label(h.getNationalism() + ""), 1, 3);
			traits.add(new Label("Confidence"), 0, 4);
			traits.add(new Label(h.getConfidence() + ""), 1, 4);
			traits.add(new Label("Tolerance"), 0, 5);
			traits.add(new Label(h.getTolerance() + ""), 1, 5);
			traits.add(new Label("Interests"), 0, 6);
			traits.add(new Label(h.getInterest1() + ""), 1, 6);
			traits.add(new Label(), 0, 7);
			traits.add(new Label(h.getInterest2() + ""), 1, 7);
			traits.add(new Label(), 0, 8);
			traits.add(new Label(h.getInterest3() + ""), 1, 8);
			traits.add(new Label("Disinterests"), 0, 9);
			traits.add(new Label(h.getDisinterest1() + ""), 1, 9);
			traits.add(new Label(), 0, 10);
			traits.add(new Label(h.getDisinterest2() + ""), 1, 10);
			traits.add(new Label(), 0, 11);
			traits.add(new Label(h.getDisinterest3() + ""), 1, 11);
			traits.setHgap(30);
			traits.setVgap(20);
			//Note: this rectangle's X can be shortened to make more room
			Rectangle backTraits = new Rectangle(350, 450);
			backTraits.setFill(Color.WHITE);
			traits.setTranslateX(680);
			traits.setTranslateY(105);
			backTraits.setTranslateX(675);
			backTraits.setTranslateY(100);
			ROOT.getChildren().add(backTraits);
			ROOT.getChildren().add(traits);
		}
		
		Button page1 = new Button("<< PAGE 1");
		page1.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				showUnitStats(u, back);
			}
		});
		page1.setTranslateX(200);
		page1.setTranslateY(530);
		ROOT.getChildren().add(page1);
	}
	public static void enterBuilding(Building b, double paneX, double paneY) {
		ROOT.getChildren().clear();
		try {
			Image im = new Image(new FileInputStream("interior/" + b.getType() + ".jpg"),
					1200, 580, false, false);
			ImageView view = new ImageView(im);
			ROOT.getChildren().add(view);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		addWorldMapButton(paneX, paneY);
		if (b instanceof Coliseum) {
			enterColiseum((Coliseum)b, paneX, paneY);
		} else if (b instanceof Hospital) {
			enterHospital((Hospital)b, paneX, paneY);
		} else if (b instanceof Port) {
			enterPort((Port)b, paneX, paneY);
		} else if (b instanceof ResearchCenter) {
			enterResearchCenter((ResearchCenter)b, paneX, paneY);
		} else if (b instanceof Shipyard) {
			enterShipyard((Shipyard)b, paneX, paneY);
		} else if (b instanceof Village) {
			enterVillage((Village)b, paneX, paneY);
		} else if (b instanceof WarpPad) {
			//TODO
		} else if (b instanceof Barracks) {
			enterBarracks((Barracks)b, paneX, paneY);
		} else if (b instanceof Castle) {
			enterCastle((Castle)b, paneX, paneY);
		} else if (b instanceof Fortress) {
			enterFortress((Fortress)b, paneX, paneY);
		} else if (b instanceof Prison) {
			enterPrison((Prison)b, paneX, paneY);
		} else if (b instanceof TrainingFacility) {
			enterTrainingFacility((TrainingFacility)b, paneX, paneY);
		} else if (b instanceof Factory) {
			enterFactory((Factory)b, paneX, paneY);
		} else if (b instanceof Farm) {
			enterFarm((Farm)b, paneX, paneY);
		} else if (b instanceof MagicProcessingFacility) {
			enterMagicProcessingFacility((MagicProcessingFacility)b, paneX, paneY);
		} else if (b instanceof MiningFacility) {
			enterMiningFacility((MiningFacility)b, paneX, paneY);
		} else if (b instanceof Ranch) {
			enterRanch((Ranch)b, paneX, paneY);
		} else if (b instanceof Storehouse) {
			enterStorehouse((Storehouse)b, paneX, paneY);
		} else if (b instanceof TradeCenter) {
			enterTradeCenter((TradeCenter)b, paneX, paneY);
		}
	}
	private static void enterColiseum(Coliseum b, double paneX, double paneY) {
		addGeneralBuildingOptions(b, paneX, paneY);
		// TODO Auto-generated method stub
	}
	private static void enterHospital(Hospital b, double paneX, double paneY) {
		addGeneralBuildingOptions(b, paneX, paneY);
		// TODO Auto-generated method stub
	}
	private static void enterPort(Port b, double paneX, double paneY) {
		addGeneralBuildingOptions(b, paneX, paneY);
		// TODO Auto-generated method stub
	}
	private static void enterResearchCenter(ResearchCenter b, double paneX, double paneY) {
		addGeneralBuildingOptions(b, paneX, paneY);
		// TODO Auto-generated method stub
	}
	private static void enterShipyard(Shipyard b, double paneX, double paneY) {
		addGeneralBuildingOptions(b, paneX, paneY);
		// TODO Auto-generated method stub
	}
	private static void enterVillage(Village b, double paneX, double paneY) {
		addGeneralBuildingOptions(b, paneX, paneY);
		// TODO Auto-generated method stub
	}
	private static void enterBarracks(Barracks b, double paneX, double paneY) {
		addGeneralBuildingOptions(b, paneX, paneY);
		// TODO Auto-generated method stub
		addDefendableBuildingOptions(b, paneX, paneY);
	}
	private static void enterCastle(Castle b, double paneX, double paneY) {
		addGeneralBuildingOptions(b, paneX, paneY);
		// TODO Auto-generated method stub
		addDefendableBuildingOptions(b, paneX, paneY);
		try {
			Image swd = new Image(new FileInputStream("interior/sword.jpg"),
					100, 200, false, false);
			ImageView swdView = new ImageView(swd);
			swdView.setTranslateX(550);
			swdView.setTranslateY(380);
			ROOT.getChildren().add(swdView);
			Button outfit = new Button("Outfit");
			outfit.setTranslateX(550);
			outfit.setTranslateY(500);
			ROOT.getChildren().add(outfit);
			Button train = new Button("Train");
			train.setTranslateX(550);
			train.setTranslateY(550);
			ROOT.getChildren().add(train);
			
			Image ally = new Image(new FileInputStream("interior/partners.jpg"),
					100, 100, false, false);
			ImageView allyView = new ImageView(ally);
			allyView.setTranslateX(1100);
			allyView.setTranslateY(350);
			ROOT.getChildren().add(allyView);
			Button support = new Button("Support\nPartners");
			support.setTranslateX(1100);
			support.setTranslateY(430);
			ROOT.getChildren().add(support);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}
	private static void enterFortress(Fortress b, double paneX, double paneY) {
		addGeneralBuildingOptions(b, paneX, paneY);
		// TODO Auto-generated method stub
		addDefendableBuildingOptions(b, paneX, paneY);
	}
	private static void enterPrison(Prison b, double paneX, double paneY) {
		addGeneralBuildingOptions(b, paneX, paneY);
		// TODO Auto-generated method stub
		addDefendableBuildingOptions(b, paneX, paneY);
	}
	private static void enterTrainingFacility(TrainingFacility b, double paneX, double paneY) {
		addGeneralBuildingOptions(b, paneX, paneY);
		// TODO Auto-generated method stub
		addDefendableBuildingOptions(b, paneX, paneY);
	}
	private static void enterFactory(Factory b, double paneX, double paneY) {
		addGeneralBuildingOptions(b, paneX, paneY);
		// TODO Auto-generated method stub
	}
	private static void enterFarm(Farm b, double paneX, double paneY) {
		addGeneralBuildingOptions(b, paneX, paneY);
		// TODO Auto-generated method stub
	}
	private static void enterMagicProcessingFacility(MagicProcessingFacility b,
			double paneX, double paneY) {
		addGeneralBuildingOptions(b, paneX, paneY);
		// TODO Auto-generated method stub
	}
	private static void enterMiningFacility(MiningFacility b, double paneX, double paneY) {
		addGeneralBuildingOptions(b, paneX, paneY);
		// TODO Auto-generated method stub
	}
	private static void enterRanch(Ranch b, double paneX, double paneY) {
		addGeneralBuildingOptions(b, paneX, paneY);
		// TODO Auto-generated method stub
	}
	private static void enterStorehouse(Storehouse b, double paneX, double paneY) {
		addGeneralBuildingOptions(b, paneX, paneY);
		// TODO Auto-generated method stub
	}
	private static void enterTradeCenter(TradeCenter b, double paneX, double paneY) {
		addGeneralBuildingOptions(b, paneX, paneY);
		// TODO Auto-generated method stub
	}

	private static Button worldMapButton(String name, final double paneX, final double paneY) {
		Button ret = new Button(name);
		ret.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				switchToWorldMap(paneX, paneY);
			}
		});
		return ret;
	}
	
	private static void addWorldMapButton(final double paneX, final double paneY) {
		Rectangle backRect = new Rectangle(1200, 20);
		backRect.setFill(Color.CYAN);
		Label backLabel = new Label("<< BACK TO WORLD MAP");
		Group backGroup = new Group(backRect, backLabel);
		backGroup.setTranslateY(580);
		backRect.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				switchToWorldMap(paneX, paneY);
			}
		});
		ROOT.getChildren().add(backGroup);
	}
	
	private static void addGeneralBuildingOptions(final Building b, final double paneX, final double paneY) {
		Group portrait = new Group();
		makePortrait(portrait, b.getOwner());
		portrait.setTranslateX(500);
		portrait.setTranslateY(0);
		Rectangle introRect = new Rectangle(100, 50);
		introRect.setFill(Color.ALICEBLUE);
		Label intro = new Label("Owner:\n" + b.getOwner().getDisplayName());
		intro.setMaxWidth(100);
		intro.setWrapText(true);
		introRect.setTranslateX(500);
		introRect.setTranslateY(200);
		intro.setTranslateX(500);
		intro.setTranslateY(200);
		Button examine = new Button("View Character");
		examine.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				EventHandler<MouseEvent> back = new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						enterBuilding(b, paneX, paneY);
					}
				};
				showUnitStats(b.getOwner(), back);
			}
		});
		examine.setTranslateX(600);
		examine.setTranslateY(200);
		ROOT.getChildren().add(portrait);
		ROOT.getChildren().add(introRect);
		ROOT.getChildren().add(intro);
		ROOT.getChildren().add(examine);
		
		Rectangle statsRect = new Rectangle(200, 120);
		statsRect.setFill(Color.ALICEBLUE);
		Label nameLabel = new Label(b.getNameAndType());
		Label hpLabel = new Label("Building HP: " + b.getCurrentHP() + "/" + b.getMaximumHP());
		Label defLabel = new Label("Building Defense: " + b.getDurability());
		Label resLabel = new Label("Building Magic Resistance: " + b.getResistance());
		nameLabel.setTranslateY(10);
		hpLabel.setTranslateY(40);
		defLabel.setTranslateY(70);
		resLabel.setTranslateY(100);
		ROOT.getChildren().add(statsRect);
		ROOT.getChildren().add(nameLabel);
		ROOT.getChildren().add(hpLabel);
		ROOT.getChildren().add(defLabel);
		ROOT.getChildren().add(resLabel);
		
		//TODO inventory display
	}

	private static void addDefendableBuildingOptions(final Defendable d,
			final double paneX, final double paneY) {
		try {
			if (!d.getDefenses().isEmpty()) {
				Image bal = new Image(new FileInputStream("interior/ballista.jpg"),
						200, 100, false, false);
				ImageView balView = new ImageView(bal);
				balView.setTranslateX(20);
				balView.setTranslateY(480);
				Button setWeapons = new Button("Position Weapons"); //TODO functionality
				setWeapons.setTranslateX(20);
				setWeapons.setTranslateY(550);
				ROOT.getChildren().add(balView);
				ROOT.getChildren().add(setWeapons);
			}
			if (d.getAssignedGroup() != null) {
				Image grd = new Image(new FileInputStream("interior/guard.jpg"),
						100, 200, false, false);
				ImageView grdView = new ImageView(grd);
				grdView.setTranslateX(250);
				grdView.setTranslateY(380);
				Button position = new Button("Position Team"); //TODO functionality
				Button view = new Button("View Team"); //TODO functionality
				Button dismiss = new Button("Dismiss Team");
				dismiss.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						d.dismissAssignedGroup();
						enterBuilding(d, paneX, paneY);
					}
				});
				dismiss.setTranslateX(250);
				dismiss.setTranslateY(550);
				position.setTranslateX(250);
				position.setTranslateY(500);
				view.setTranslateX(250);
				view.setTranslateY(450);
				ROOT.getChildren().add(grdView);
				ROOT.getChildren().add(dismiss);
				ROOT.getChildren().add(position);
				ROOT.getChildren().add(view);
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	private static Nation playerNation() {
		return GeneralGameplayManager.getPlayerNation();
	}
	private static Human player() {
		return GeneralGameplayManager.getPlayer();
	}
	private static WorldMap worldMap() {
		return GeneralGameplayManager.getWorldMap();
	}
	private static String validateInputName(String s, String field) {
		//Assume the string is not null or empty, because that just means a random name
		//should be used
		s.trim();
		if (s.length() > 24) {
			throw new IllegalArgumentException("Too long name for " + field);
		}
		if (s.length() < 1) {
			throw new IllegalArgumentException("Input for " + field + " cannot just be whitespace");
		}
		for (int q = 0; q < s.length(); q++) {
			char c = s.charAt(q);
			if (!Character.isLetter(c)
					&& !Character.isDigit(c)
					&& c != ' '
					&& c != '-'
					&& c != '_'
					&& c != '.'
					) {
				throw new IllegalArgumentException(field + " contains an illegal character");
			}
		}
		return s;
	}
	private static int parseValidDigitWithinBounds(String s, int min, int max, String field)
			throws IllegalArgumentException {
		s.trim();
		try {
			int num = Integer.parseInt(s);
			if (num >= min && num <= max) {
				return num;
			}
		} catch (InputMismatchException e) {
			throw new IllegalArgumentException("Entered a non-integer for " + field);
		}
		throw new IllegalArgumentException("Entered an out-of-bounds value for " + field);
	}

}
