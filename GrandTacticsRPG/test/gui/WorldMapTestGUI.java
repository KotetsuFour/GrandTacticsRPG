package gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.InputMismatchException;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import affiliation.CityState;
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
import buildings.goods_deliverer.GoodsDeliverer;
import buildings.goods_deliverer.MagicProcessingFacility;
import buildings.goods_deliverer.MiningFacility;
import buildings.goods_deliverer.Ranch;
import buildings.goods_deliverer.Storehouse;
import buildings.goods_deliverer.TradeCenter;
import data_structures.List;
import inventory.InventoryIndex;
import inventory.Lootable;
import inventory.item.Armor;
import inventory.item.Item;
import inventory.item.ManufacturableItem;
import inventory.item.Resource;
import inventory.item.UsableItem;
import inventory.weapon.HandheldWeapon;
import inventory.weapon.StationaryWeapon;
import inventory.weapon.Weapon;
import location.BattlegroundTile;
import location.WMTileOccupant;
import location.WorldMap;
import location.WorldMapTile;
import location.WorldMapTile.WorldMapTileType;
import manager.BattleManager;
import manager.GeneralGameplayManager;
import politics.DiplomaticRelation;
import politics.Festival;
import politics.MajorEvent;
import politics.SportingEvent;
import politics.War;
import politics.War.WarCause;
import reference.UnitClassIndex;
import report.StandardBattleReport;
import ship.Ship;
import unit.Assignable;
import unit.Equippable;
import unit.Unit;
import unit.UnitClass;
import unit.UnitClass.Mount;
import unit.UnitGroup;
import unit.human.Demeanor;
import unit.human.Human;
import unit.human.Human.Interest;
import unit.monster.Monster;
import util.RNGStuff;

public class WorldMapTestGUI extends JFrame {

	/**Obligatory serial version*/
	private static final long serialVersionUID = 1L;

	private JPanel p;

	private CardLayout cl;
	
	public static final String VIEW = "View";
	
	public static final String MAP = "Map";
	
	public static WorldMapPanel worldMapPanel;

	public WorldMapTestGUI() {

		p = new JPanel();
		cl = new CardLayout();
		p.setLayout(cl);
		
		//Window formatting
	    setDefaultCloseOperation(EXIT_ON_CLOSE);
	    setSize(1300, 700);
	    setTitle("Grand Tactical RPG Ver. 1");

	    p.add(new IntroPanel(), "Intro");	    
	    cl.show(p, "Intro");
	    getContentPane().add(p, BorderLayout.CENTER);

	      
	    setVisible(true);
	}

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		WorldMapTestGUI hg = new WorldMapTestGUI();
	}
	
	private class IntroPanel extends JPanel {
		public IntroPanel() {
			
			setLayout(new BorderLayout());
			JPanel gameOptions = new JPanel();
			gameOptions.setLayout(new GridLayout(1, 4));
			
			for (int q = 1; q <= 3; q++) {
				JPanel gameFile = new JPanel();
				gameFile.setBackground(Color.BLACK);
				JButton loadFile = new JButton("Game " + q);
				loadFile.setBackground(Color.BLUE);
				loadFile.setForeground(Color.BLACK);
				loadFile.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						GeneralGameplayManager.getWorldMap();
						switchToPanel(new WorldCustomizationPanel(), VIEW);
					}
				});
				gameFile.add(loadFile);
				gameOptions.add(gameFile);
			}
			JPanel extras = new JPanel();
			extras.setBackground(Color.BLACK);
			JButton selectExtras = new JButton("Extras");
			selectExtras.setBackground(Color.RED);
			selectExtras.setForeground(Color.BLACK);
			selectExtras.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
				}
			});
			extras.add(selectExtras);
			gameOptions.add(extras);
			add(gameOptions, BorderLayout.SOUTH);
		}
	}
	
	private class WorldCustomizationPanel extends JPanel {
		public WorldCustomizationPanel() {
			
			GeneralGameplayManager.indexesInitialization();
			
			setLayout(new BorderLayout());
			JPanel langChoices = new JPanel(new GridLayout(2, RNGStuff.LANGUAGES.length));
			JPanel agingOption = new JPanel(new BorderLayout());
			JPanel loreOptions = new JPanel(); //TODO coming soon
			JPanel hairColorOptions = new JPanel(new GridLayout(2, RNGStuff.HAIR_COLORS.length));
			JPanel skinColorOptions = new JPanel(new GridLayout(2, RNGStuff.SKIN_COLORS.length));
			JPanel eyeColorOptions = new JPanel(new GridLayout(2, RNGStuff.EYE_COLORS.length));
			final JPanel sampleNames = new JPanel();
			final JPanel showColors = new JPanel();
			
			final JCheckBox[] langsOpts = new JCheckBox[RNGStuff.LANGUAGES.length];
			for (int q = 0; q < langsOpts.length; q++) {
				langsOpts[q] = new JCheckBox(RNGStuff.LANGUAGES[q].titleOfNamingConvention(), true);
				langChoices.add(langsOpts[q]);
			}
			for (int q = 0; q < langsOpts.length; q++) {
				final int idx = q;
				JButton showSampleNames = new JButton("Sample Names");
				showSampleNames.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						StringBuilder sb = new StringBuilder();
						for (int w = 0; w < 15; w++) {
							sb.append("<html>" + RNGStuff.LANGUAGES[idx].getName() + "<br/>");
						}
						sampleNames.removeAll();
						sampleNames.add(new JLabel(sb.toString()));
						sampleNames.validate();
						sampleNames.repaint();
					}
				});
				langChoices.add(showSampleNames);
			}
			
			final JCheckBox allowAging = new JCheckBox("Aging", true);
			agingOption.add(allowAging);
			
			loreOptions.add(new JLabel("Lore Options Coming Soon"));
			
			final JCheckBox[] hairOpts = new JCheckBox[RNGStuff.HAIR_COLORS.length];
			for (int q = 0; q < hairOpts.length; q++) {
				hairOpts[q] = new JCheckBox(RNGStuff.HAIR_COLORS[q].getTitle());
				hairColorOptions.add(hairOpts[q]);
			}
			hairOpts[0].setSelected(true);
			
			for (int q = 0; q < hairOpts.length; q++) {
				final int idx = q;
				JButton checkColors = new JButton("View Colors");
				checkColors.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						showColors.removeAll();
						int firstDimension = 4;
						int otherDimension = RNGStuff.HAIR_COLORS[idx].size() / firstDimension;
						if (RNGStuff.HAIR_COLORS[idx].size() % 4 != 0) {
							otherDimension++;
						}
						showColors.setLayout(new GridLayout(otherDimension, firstDimension));
//						for (int w = 0; w < RNGStuff.HAIR_COLORS[idx].size(); w++) {
//							JPanel color = new JPanel();
//							color.setBackground(RNGStuff.HAIR_COLORS[idx].colorAtIndex(w));
//							showColors.add(color);
//						}
						showColors.validate();
						showColors.repaint();
					}
				});
				hairColorOptions.add(checkColors);
			}
			final JCheckBox[] skinOpts = new JCheckBox[RNGStuff.SKIN_COLORS.length];
			for (int q = 0; q < skinOpts.length; q++) {
				skinOpts[q] = new JCheckBox(RNGStuff.SKIN_COLORS[q].getTitle());
				skinColorOptions.add(skinOpts[q]);
			}
			skinOpts[0].setSelected(true);
			
			for (int q = 0; q < skinOpts.length; q++) {
				final int idx = q;
				JButton checkColors = new JButton("View Colors");
				checkColors.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						showColors.removeAll();
						int firstDimension = 4;
						int otherDimension = RNGStuff.SKIN_COLORS[idx].size() / firstDimension;
						if (RNGStuff.SKIN_COLORS[idx].size() % 4 != 0) {
							otherDimension++;
						}
						showColors.setLayout(new GridLayout(otherDimension, firstDimension));
//						for (int w = 0; w < RNGStuff.SKIN_COLORS[idx].size(); w++) {
//							JPanel color = new JPanel();
//							color.setBackground(RNGStuff.SKIN_COLORS[idx].colorAtIndex(w));
//							showColors.add(color);
//						}
						showColors.validate();
						showColors.repaint();
					}
				});
				skinColorOptions.add(checkColors);
			}
			final JCheckBox[] eyeOpts = new JCheckBox[RNGStuff.EYE_COLORS.length];
			for (int q = 0; q < eyeOpts.length; q++) {
				eyeOpts[q] = new JCheckBox(RNGStuff.EYE_COLORS[q].getTitle());
				eyeColorOptions.add(eyeOpts[q]);
			}
			eyeOpts[0].setSelected(true);
			
			for (int q = 0; q < eyeOpts.length; q++) {
				final int idx = q;
				JButton checkColors = new JButton("View Colors");
				checkColors.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						showColors.removeAll();
						int firstDimension = 4;
						int otherDimension = RNGStuff.EYE_COLORS[idx].size() / firstDimension;
						if (RNGStuff.EYE_COLORS[idx].size() % 4 != 0) {
							otherDimension++;
						}
						showColors.setLayout(new GridLayout(otherDimension, firstDimension));
//						for (int w = 0; w < RNGStuff.EYE_COLORS[idx].size(); w++) {
//							JPanel color = new JPanel();
//							color.setBackground(RNGStuff.EYE_COLORS[idx].colorAtIndex(w));
//							showColors.add(color);
//						}
						showColors.validate();
						showColors.repaint();
					}
				});
				eyeColorOptions.add(checkColors);
			}
			
			JButton next = new JButton("NEXT");
			next.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					GeneralGameplayManager.setAging(allowAging.isSelected());
					//TODO add lores later
					List<Integer> langs = new List<>();
					List<Integer> hairs = new List<>();
					List<Integer> skins = new List<>();
					List<Integer> eyes = new List<>();
					for (int q = 0; q < langsOpts.length; q++) {
						if (langsOpts[q].isSelected()) {
							langs.add(q);
						}
					}
					for (int q = 0; q < hairOpts.length; q++) {
						if (hairOpts[q].isSelected()) {
							hairs.add(q);
						}
					}
					for (int q = 0; q < skinOpts.length; q++) {
						if (skinOpts[q].isSelected()) {
							skins.add(q);
						}
					}
					for (int q = 0; q < eyeOpts.length; q++) {
						if (eyeOpts[q].isSelected()) {
							eyes.add(q);
						}
					}
					RNGStuff.useLanguage(langs);
					RNGStuff.useColors(hairs, skins, eyes);
					
					switchToPanel(new NationCustomizationPanel(), VIEW);
				}
			});
			
			JPanel languages = new JPanel(new BorderLayout());
			JPanel aging = new JPanel(new BorderLayout());
			JPanel lore = new JPanel(new BorderLayout());
			JPanel hairColors = new JPanel(new BorderLayout());
			JPanel skinColors = new JPanel(new BorderLayout());
			JPanel eyeColors = new JPanel(new BorderLayout());
			
			languages.add(new JLabel("Select usable character naming conventions"), BorderLayout.NORTH);
			languages.add(langChoices);
			
			aging.add(new JLabel("Can characters age and die of natural causes?"));
			aging.add(agingOption);
			
			lore.add(new JLabel("Select lore elements"));
			lore.add(loreOptions);
			
			hairColors.add(new JLabel("Select sets of usable hair colors"), BorderLayout.NORTH);
			hairColors.add(hairColorOptions);
			
			skinColors.add(new JLabel("Select sets of usable skin colors"), BorderLayout.NORTH);
			skinColors.add(skinColorOptions);
			
			eyeColors.add(new JLabel("Select sets of usable eye colors"), BorderLayout.NORTH);
			eyeColors.add(eyeColorOptions);
			
			JPanel worldSettings = new JPanel(new GridLayout(6, 1));
			worldSettings.add(languages);
			worldSettings.add(aging);
			worldSettings.add(lore);
			worldSettings.add(hairColors);
			worldSettings.add(skinColors);
			worldSettings.add(eyeColors);
			
			JPanel worldOptions = new JPanel(new BorderLayout());
			worldOptions.add(new JLabel("Customize World Settings"), BorderLayout.NORTH);
			worldOptions.add(worldSettings);
			
			JPanel samples = new JPanel(new GridLayout(2, 1));
			samples.add(sampleNames);
			samples.add(showColors);
			
			JPanel fullDisplay = new JPanel(new GridLayout(1, 2));
			fullDisplay.add(worldOptions);
			fullDisplay.add(samples);
			
			add(fullDisplay);
			add(next, BorderLayout.SOUTH);
		}
		
	}
	
	private class NationCustomizationPanel extends JPanel {
		public NationCustomizationPanel() {
			setLayout(new BorderLayout());
			JPanel info = new JPanel(new GridLayout(1, 3));
			JPanel name = new JPanel(new GridLayout(1, 4));
			JPanel capital = new JPanel(new GridLayout(1, 4));
			JPanel language = new JPanel(new GridLayout(1, 4));
			
			info.add(new JLabel("Customize Your Nation"));
			info.add(new JPanel());
			info.add(new JLabel("You can leave text fields blank to make them random"));
			
			name.add(new JLabel("The"));
			final JComboBox<String> nationType= new JComboBox<String>(Nation.NATION_TYPES);
			name.add(nationType);
			name.add(new JLabel("of"));
			final JTextField nationName = new JTextField();
			name.add(nationName);
			
			capital.add(new JLabel("Capital City Name:"));
			final JTextField capitalName = new JTextField();
			capital.add(capitalName);
			capital.add(new JPanel());
			capital.add(new JPanel());
			
			language.add(new JLabel("Character Naming Convention:"));
			String[] conventions = new String[RNGStuff.LANGUAGES_IN_USE.length + 1];
			conventions[0] = "--Random--";
			for (int q = 1; q < conventions.length; q++) {
				conventions[q] = RNGStuff.LANGUAGES_IN_USE[q - 1].titleOfNamingConvention();
			}
			final JComboBox<String> namingConvention = new JComboBox<String>(conventions);
			language.add(namingConvention);
			language.add(new JPanel());
			language.add(new JPanel());
			
			final JPanel samples = new JPanel(new BorderLayout());
			JPanel genSamplesPanel = new JPanel(new GridLayout(4, 1));
			JButton genSamples = new JButton("Sample Character Names");
			genSamples.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					StringBuilder sb = new StringBuilder();
					if (namingConvention.getSelectedIndex() == 0) {
						sb.append("<html>Naming convention is currently set to random<br/>"
								+ "<html>Please select a naming convention to test.<br/>");
					} else {
						for (int q = 0; q < 15; q++) {
							sb.append("<html>"
									+ RNGStuff.LANGUAGES_IN_USE[namingConvention.getSelectedIndex() - 1].getName()
									+ "<br/>");
						}
					}
					samples.removeAll();
					samples.add(new JLabel(sb.toString()), BorderLayout.CENTER);
					samples.validate();
					samples.repaint();
				}
			});
			genSamplesPanel.add(genSamples);
			genSamplesPanel.add(new JPanel());
			genSamplesPanel.add(new JPanel());
			genSamplesPanel.add(new JPanel());
			
			JButton next = new JButton("NEXT");
			next.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						int pLanguage = namingConvention.getSelectedIndex();
						if (pLanguage == 0) {
							pLanguage = RNGStuff.nextInt(namingConvention.getItemCount() - 1);
						} else {
							pLanguage--;
						}
						String pNationName;
						if (nationName.getText() == null || nationName.getText().equals("")) {
							pNationName = RNGStuff.newLocationName(pLanguage);
						} else {
							pNationName = validateInputName(nationName.getText(), "Name");
						}
						int pNationType = nationType.getSelectedIndex();
						String pCapitalName;
						if (capitalName.getText() == null || capitalName.getText().equals("")) {
							pCapitalName = RNGStuff.newLocationName(pLanguage);
						} else {
							pCapitalName = validateInputName(capitalName.getText(), "Capital");
						}
						
						GeneralGameplayManager.initializePlayerNation(pNationName, pNationType,
								pCapitalName, pLanguage);
						
						switchToPanel(new AvatarCustomizationPanel(), VIEW);
					} catch (IllegalArgumentException ex) {
						JOptionPane.showMessageDialog(null, ex.getMessage());
					}
				}
			});
			
			JPanel topHalf = new JPanel(new GridLayout(3, 1));
			topHalf.add(name);
			topHalf.add(capital);
			topHalf.add(language);
			
			JPanel bottomHalf = new JPanel(new GridLayout(1, 2));
			bottomHalf.add(genSamplesPanel);
			bottomHalf.add(samples);

			JPanel options = new JPanel(new GridLayout(2, 1));
			options.add(topHalf);
			options.add(bottomHalf);
			
			add(info, BorderLayout.NORTH);
			add(options);
			add(next, BorderLayout.SOUTH);
		}
	}
	
	private class AvatarCustomizationPanel extends JPanel {
		public AvatarCustomizationPanel() {
			setLayout(new BorderLayout());
			JPanel info = new JPanel(new GridLayout(1, 3));
			JPanel appearance = new JPanel(new BorderLayout());
			JPanel appearanceMods = new JPanel(new GridLayout(11, 4));
			JPanel preferences = new JPanel(new GridLayout(2, 4));
			JPanel traitAndDemeanor = new JPanel(new GridLayout(2, 2));
			JPanel boonsAndBanes = new JPanel(new GridLayout(2, 4));
			
			info.add(new JLabel("Customize Your Avatar"));
			info.add(new JPanel());
			info.add(new JPanel());
			
			final JPanel portrait = new JPanel();
			appearance.add(portrait);
			appearance.add(new JLabel("Image will go here"), BorderLayout.SOUTH);
			
			//Name
			appearanceMods.add(new JLabel("Name"));
			final JTextField name = new JTextField();
			appearanceMods.add(name);
			appearanceMods.add(new JPanel());
			appearanceMods.add(new JPanel());
			//Gender
			appearanceMods.add(new JLabel("Gender"));
			String[] gendOpts = {"--Random--", "Male", "Female"};
			final JComboBox<String> gender = new JComboBox<>(gendOpts);
			appearanceMods.add(gender);
			appearanceMods.add(new JPanel());
			appearanceMods.add(new JPanel());
			//Face
			appearanceMods.add(new JLabel("Face Shape"));
			String[] faceOpts = {"--Random--", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
					"11", "12", "13", "14", "15"};
			final JComboBox<String> face = new JComboBox<String>(faceOpts);
			appearanceMods.add(face);
			appearanceMods.add(new JLabel("Skin Color"));
			String[] skinColorOpts = new String[RNGStuff.SKIN_COLORS_IN_USE.size() + 1];
			skinColorOpts[0] = "Random";
			for (int q = 0; q < RNGStuff.SKIN_COLORS_IN_USE.size(); q++) {
				skinColorOpts[q + 1] = q + "";
			}
			final JComboBox<String> skinColor = new JComboBox<String>(skinColorOpts);
			appearanceMods.add(skinColor);
			//Nose
			appearanceMods.add(new JLabel("Nose Shape"));
			String[] noseOpts = {"--Random--", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
					"11", "12", "13", "14", "15"};
			final JComboBox<String> nose = new JComboBox<String>(noseOpts);
			appearanceMods.add(nose);
			appearanceMods.add(new JPanel());
			appearanceMods.add(new JPanel());
			//Lips
			appearanceMods.add(new JLabel("Lips Shape"));
			String[] lipsOpts = {"--Random--", "1", "2"};
			final JComboBox<String> lips = new JComboBox<String>(lipsOpts);
			appearanceMods.add(lips);
			appearanceMods.add(new JPanel());
			appearanceMods.add(new JPanel());
			//Ear Shape
			appearanceMods.add(new JLabel("Ear Shape"));
			String[] earOpts = {"--Random--", "1", "2", "3", "4", "5", "6", "7"};
			final JComboBox<String> ear = new JComboBox<String>(earOpts);
			appearanceMods.add(ear);
			appearanceMods.add(new JPanel());
			appearanceMods.add(new JPanel());
			//Eye Shape
			appearanceMods.add(new JLabel("Eye Shape"));
			String[] eyeOpts = {"--Random--", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
					"11", "12", "13", "14", "15"};
			final JComboBox<String> eye = new JComboBox<String>(eyeOpts);
			appearanceMods.add(eye);
			appearanceMods.add(new JLabel("Eye Color"));
			String[] eyeColorOpts = new String[RNGStuff.EYE_COLORS_IN_USE.size() + 1];
			eyeColorOpts[0] = "Random";
			for (int q = 0; q < RNGStuff.EYE_COLORS_IN_USE.size(); q++) {
				eyeColorOpts[q + 1] = q + "";
			}
			final JComboBox<String> eyeColor = new JComboBox<String>(eyeColorOpts);
			appearanceMods.add(eyeColor);
			//Iris Appearance
			appearanceMods.add(new JLabel("Iris Appearance"));
			String[] irisOpts = {"--Random--", "1", "2", "3", "4", "5", "6", "7"};
			final JComboBox<String> iris = new JComboBox<String>(irisOpts);
			appearanceMods.add(iris);
			appearanceMods.add(new JPanel());
			appearanceMods.add(new JPanel());
			//Eyebrows
			appearanceMods.add(new JLabel("Eyebrows"));
			String[] browOpts = {"--Random--", "1", "2", "3"};
			final JComboBox<String> brow = new JComboBox<String>(browOpts);
			appearanceMods.add(brow);
			appearanceMods.add(new JPanel());
			appearanceMods.add(new JPanel());
			//Hairstyle
			appearanceMods.add(new JLabel("Hair/Mustache/Beard Styles"));
			String[] hairOpts = {"--Random--", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
					"11", "12", "13", "14", "15"};
			final JComboBox<String> hair = new JComboBox<String>(hairOpts);
			String[] stacheOpts = {"--Random--", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
					"11", "12"};
			final JComboBox<String> stache = new JComboBox<String>(stacheOpts);
			String[] beardOpts = {"--Random--", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
					"11", "12"};
			final JComboBox<String> beard = new JComboBox<String>(beardOpts);
			appearanceMods.add(hair);
			appearanceMods.add(stache);
			appearanceMods.add(beard);
			//TODO hair, eye, skin colors
			appearanceMods.add(new JLabel("Hair Color"));
			String[] hairColorOpts = new String[RNGStuff.HAIR_COLORS_IN_USE.size() + 1];
			hairColorOpts[0] = "Random";
			for (int q = 0; q < RNGStuff.HAIR_COLORS_IN_USE.size(); q++) {
				hairColorOpts[q + 1] = q + "";
			}
			final JComboBox<String> hairColor = new JComboBox<String>(hairColorOpts);
			appearanceMods.add(hairColor);
			
			final List<JComboBox<String>> allAppearanceOpts = new List<JComboBox<String>>(14);
			allAppearanceOpts.add(gender); //0
			allAppearanceOpts.add(face); //1
			allAppearanceOpts.add(nose); //2
			allAppearanceOpts.add(lips); //3
			allAppearanceOpts.add(ear); //4
			allAppearanceOpts.add(eye); //5
			allAppearanceOpts.add(iris); //6
			allAppearanceOpts.add(brow); //7
			allAppearanceOpts.add(hair); //8
			allAppearanceOpts.add(stache); //9
			allAppearanceOpts.add(beard); //10
			allAppearanceOpts.add(hairColor); //11
			allAppearanceOpts.add(skinColor); //12
			allAppearanceOpts.add(eyeColor); //13
			ActionListener updatePortrait = new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					makePortrait(portrait, allAppearanceOpts);
					portrait.validate();
					portrait.repaint();
				}
			};
			gender.addActionListener(updatePortrait);
			face.addActionListener(updatePortrait);
			nose.addActionListener(updatePortrait);
			lips.addActionListener(updatePortrait);
			ear.addActionListener(updatePortrait);
			eye.addActionListener(updatePortrait);
			iris.addActionListener(updatePortrait);
			brow.addActionListener(updatePortrait);
			hair.addActionListener(updatePortrait);
			stache.addActionListener(updatePortrait);
			beard.addActionListener(updatePortrait);
			hairColor.addActionListener(updatePortrait);
			skinColor.addActionListener(updatePortrait);
			eyeColor.addActionListener(updatePortrait);
			
			preferences.add(new JLabel("Interests"));
			String[] intOpts = new String[Human.Interest.values().length + 1];
			intOpts[0] = "--Random--";
			for (int q = 1; q < intOpts.length; q++) {
				intOpts[q] = Human.Interest.values()[q - 1].getDisplayName();
			}
			final JComboBox<String> interest1 = new JComboBox<String>(intOpts);
			preferences.add(interest1);
			final JComboBox<String> interest2 = new JComboBox<String>(intOpts);
			preferences.add(interest2);
			final JComboBox<String> interest3 = new JComboBox<String>(intOpts);
			preferences.add(interest3);
			
			preferences.add(new JLabel("Disinterests"));
			final JComboBox<String> interest4 = new JComboBox<String>(intOpts);
			preferences.add(interest4);
			final JComboBox<String> interest5 = new JComboBox<String>(intOpts);
			preferences.add(interest5);
			final JComboBox<String> interest6 = new JComboBox<String>(intOpts);
			preferences.add(interest6);
			
			traitAndDemeanor.add(new JLabel("Support Trait"));
			String[] traitOpts = new String[Human.CombatTrait.values().length + 1];
			traitOpts[0] = "--Random--";
			for (int q = 1; q < traitOpts.length; q++) {
				traitOpts[q] = Human.CombatTrait.values()[q - 1].getDisplayName();
			}
			final JComboBox<String> trait = new JComboBox<String>(traitOpts);
			traitAndDemeanor.add(trait);
			
			traitAndDemeanor.add(new JLabel("Demeanor"));
			String[] demeanorOpts = new String[Demeanor.values().length + 1];
			demeanorOpts[0] = "--Random--";
			for (int q = 1; q < demeanorOpts.length; q++) {
				demeanorOpts[q] = Demeanor.values()[q - 1].getDisplayName();
			}
			final JComboBox<String> demeanor = new JComboBox<String>(demeanorOpts);
			traitAndDemeanor.add(demeanor);
			
			boonsAndBanes.add(new JLabel("HP Boon"));
			String[] hpBoonOpts = {"--Random--", "Head", "Torso", "Arms", "Legs"};
			final JComboBox<String> hpBoon = new JComboBox<String>(hpBoonOpts);
			boonsAndBanes.add(hpBoon);
			boonsAndBanes.add(new JLabel("Attribute Boon"));
			String[] attributeBoonOpts = {"--Random--", "Magic", "Skill", "Reflex", "Awareness", "Resistance"};
			final JComboBox<String> attributeBoon = new JComboBox<String>(attributeBoonOpts);
			boonsAndBanes.add(attributeBoon);
			boonsAndBanes.add(new JLabel("HP Bane"));
			String[] hpBaneOpts = {"--Random--", "Head", "Torso", "Arms", "Legs"};
			final JComboBox<String> hpBane = new JComboBox<String>(hpBaneOpts);
			boonsAndBanes.add(hpBane);
			boonsAndBanes.add(new JLabel("Attribute Bane"));
			String[] attributeBaneOpts = {"--Random--", "Magic", "Skill", "Reflex", "Awareness", "Resistance"};
			final JComboBox<String> attributeBane = new JComboBox<String>(attributeBaneOpts);
			boonsAndBanes.add(attributeBane);
			
			JButton next = new JButton("START");
			next.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						String pName = name.getText();
						if (pName == null || pName.equals("")) {
							pName = RNGStuff.randomName(playerNation().getNationalLanguage());
						} else {
							pName = validateInputName(pName, "Name");
						}
						
						int sGender = gender.getSelectedIndex();
						boolean pGender = false;
						if (sGender == 0) {
							pGender = RNGStuff.nextBoolean();
						} else if (sGender == 1) {
							pGender = false;
						} else if (sGender == 2) {
							pGender = true;
						}
						int pFace = appearanceOption(face);
						int pNose = appearanceOption(nose);
						int pLips = appearanceOption(lips);
						int pEar = appearanceOption(ear);
						int pEye = appearanceOption(eye);
						int pIris = appearanceOption(iris);
						int pBrow = appearanceOption(brow);
						int pHair = appearanceOption(hair);
						int pStache;
						if (pGender) {
							pStache = 0;
						} else {
							pStache = appearanceOption(stache);
						}
						int pBeard;
						if (pGender) {
							pBeard = 0;
						} else {
							pBeard = appearanceOption(beard);
						}
						int pHairColor = appearanceOption(hairColor);
						int pSkinColor = appearanceOption(skinColor);
						int pEyeColor = appearanceOption(eyeColor);
						
						int pInterest1 = appearanceOption(interest1);
						int pInterest2 = appearanceOption(interest2);
						while (pInterest2 == pInterest1) {
							pInterest2++;
							if (pInterest2 == Interest.values().length) {
								pInterest2 = 0;
							}
						}
						int pInterest3 = appearanceOption(interest3);
						while (pInterest3 == pInterest1 || pInterest3 == pInterest2) {
							pInterest3++;
							if (pInterest3 == Interest.values().length) {
								pInterest3 = 0;
							}
						}
						int pInterest4 = appearanceOption(interest4);
						while (pInterest4 == pInterest1 || pInterest4 == pInterest2
								|| pInterest4 == pInterest3) {
							pInterest4++;
							if (pInterest4 == Interest.values().length) {
								pInterest4 = 0;
							}
						}
						int pInterest5 = appearanceOption(interest5);
						while (pInterest5 == pInterest1 || pInterest5 == pInterest2
								|| pInterest5 == pInterest3 || pInterest5 == pInterest4) {
							pInterest5++;
							if (pInterest5 == Interest.values().length) {
								pInterest5 = 0;
							}
						}
						int pInterest6 = appearanceOption(interest6);
						while (pInterest6 == pInterest1 || pInterest6 == pInterest2
								|| pInterest6 == pInterest3 || pInterest6 == pInterest4
								|| pInterest6 == pInterest5) {
							pInterest6++;
							if (pInterest6 == Interest.values().length) {
								pInterest6 = 0;
							}
						}
						
						int pTrait = appearanceOption(trait);
						int pDemeanor = appearanceOption(demeanor);
						if (Demeanor.values()[pDemeanor].getRarity() > 0) {
							pDemeanor =  RNGStuff.nextInt(Demeanor.values().length);
						}
						
						int pHpBoon = appearanceOption(hpBoon);
						int pHpBane = appearanceOption(hpBane);
						if (pHpBane == pHpBoon) {
							pHpBane++;
							if (pHpBane == hpBane.getItemCount() - 1) {
								pHpBane = 0;
							}
						}
						int pAttributeBoon = appearanceOption(attributeBoon);
						int pAttributeBane = appearanceOption(attributeBane);
						if (pAttributeBoon == pAttributeBane) {
							pAttributeBane++;
							if (pAttributeBane == attributeBane.getItemCount() - 1) {
								pAttributeBane = 0;
							}
						}
						double red = RNGStuff.SKIN_COLORS_IN_USE.colorAtIndex(pSkinColor).getRed();
						double green = RNGStuff.SKIN_COLORS_IN_USE.colorAtIndex(pSkinColor).getGreen();
						double blue = RNGStuff.SKIN_COLORS_IN_USE.colorAtIndex(pSkinColor).getBlue();
						GeneralGameplayManager.initializePlayer(pName, pGender, pFace, pNose,
								pLips, pEar, pEye, pIris, pBrow, pHair, pStache, pBeard,
								pInterest1, pInterest2, pInterest3, pInterest4,
								pInterest5, pInterest6, pTrait, pDemeanor, pHpBoon, pHpBane,
								pAttributeBoon, pAttributeBane, pHairColor,
								red, green, blue,
								pEyeColor);
						
						GeneralGameplayManager.initializeGeneralWorld();
						worldMapPanel = new WorldMapPanel();
						switchToPanel(worldMapPanel, MAP);
					} catch (IllegalArgumentException ex) {
						JOptionPane.showMessageDialog(null, ex.getMessage());
					}
				}
			});
			
			JPanel appearanceEditor = new JPanel(new GridLayout(1, 2));
			appearanceEditor.add(appearance);
			appearanceEditor.add(appearanceMods);
			
			JPanel furtherPersonalization = new JPanel(new GridLayout(1, 2));
			furtherPersonalization.add(traitAndDemeanor);
			furtherPersonalization.add(boonsAndBanes);
			
			JPanel personalityEditor = new JPanel(new GridLayout(2, 1));
			personalityEditor.add(preferences);
			personalityEditor.add(furtherPersonalization);
			
			JPanel fullDisplay = new JPanel(new GridLayout(2, 1));
			fullDisplay.add(appearanceEditor);
			fullDisplay.add(personalityEditor);
			
			add(info, BorderLayout.NORTH);
			add(fullDisplay);
			add(next, BorderLayout.SOUTH);
		}
		
	}
	
	private int appearanceOption(JComboBox<String> opts) {
		if (opts.getSelectedIndex() == 0) {
			return RNGStuff.nextInt(opts.getItemCount() - 1);
		}
		return opts.getSelectedIndex() - 1;
	}
	private JPanel makePortrait(JPanel ret, List<JComboBox<String>> opts) {
		ret.removeAll();
		ret.setLayout(new BorderLayout());
		JPanel hair = new JPanel();
		JPanel face = new JPanel(new GridLayout(1, 2));
		int hairRed = (int) Math.round(RNGStuff.HAIR_COLORS_IN_USE.colorAtIndex(appearanceOption(opts.get(11))).getRed() * 255);
		int hairGreen =  (int) Math.round(RNGStuff.HAIR_COLORS_IN_USE.colorAtIndex(appearanceOption(opts.get(11))).getGreen() * 255);
		int hairBlue =  (int) Math.round(RNGStuff.HAIR_COLORS_IN_USE.colorAtIndex(appearanceOption(opts.get(11))).getBlue() * 255);
		int skinRed = (int) Math.round(RNGStuff.HAIR_COLORS_IN_USE.colorAtIndex(appearanceOption(opts.get(12))).getRed() * 255);
		int skinGreen =  (int) Math.round(RNGStuff.HAIR_COLORS_IN_USE.colorAtIndex(appearanceOption(opts.get(12))).getGreen() * 255);
		int skinBlue =  (int) Math.round(RNGStuff.HAIR_COLORS_IN_USE.colorAtIndex(appearanceOption(opts.get(12))).getBlue() * 255);
		int eyeRed = (int) Math.round(RNGStuff.HAIR_COLORS_IN_USE.colorAtIndex(appearanceOption(opts.get(13))).getRed() * 255);
		int eyeGreen =  (int) Math.round(RNGStuff.HAIR_COLORS_IN_USE.colorAtIndex(appearanceOption(opts.get(13))).getGreen() * 255);
		int eyeBlue =  (int) Math.round(RNGStuff.HAIR_COLORS_IN_USE.colorAtIndex(appearanceOption(opts.get(13))).getBlue() * 255);
		Color hairColor = new Color(hairRed, hairGreen, hairBlue);
		Color skinColor = new Color(skinRed, skinGreen, skinBlue);
		Color eyeColor = new Color(eyeRed, eyeGreen, eyeBlue);
//		Color hairColor = RNGStuff.HAIR_COLORS_IN_USE.colorAtIndex(appearanceOption(opts.get(11)));
//		Color skinColor = RNGStuff.SKIN_COLORS_IN_USE.colorAtIndex(appearanceOption(opts.get(12)));
//		Color eyeColor = RNGStuff.EYE_COLORS_IN_USE.colorAtIndex(appearanceOption(opts.get(13)));
		int gender = appearanceOption(opts.get(0));
		for (int q = 0; q < 2; q++) {
			JPanel part = new JPanel();
			part.setBackground(skinColor);
			JPanel eye = new JPanel();
			eye.setBackground(eyeColor);
			part.add(eye);
			face.add(part);
		}
		hair.setBackground(hairColor);
		ret.add(hair, BorderLayout.NORTH);
		ret.add(face);
		if (gender == 1) {
			JPanel longHair1 = new JPanel();
			longHair1.setBackground(hairColor);
			JPanel longHair2 = new JPanel();
			longHair2.setBackground(hairColor);
			ret.add(longHair1, BorderLayout.WEST);
			ret.add(longHair2, BorderLayout.EAST);
		} else if (opts.get(9).getSelectedIndex() > 0 || opts.get(10).getSelectedIndex() > 0) {
			JPanel faceHair = new JPanel();
			faceHair.setBackground(hairColor);
			ret.add(faceHair, BorderLayout.SOUTH);
		}
		return ret;
	}
	private class WorldMapPanel extends JPanel {
		
		private JPanel timePanel;
		private JPanel controlDisplay;
		private JPanel mapPanel;
		
		public WorldMapPanel() {
			setLayout(new BorderLayout());
			this.timePanel = new JPanel(new GridLayout(1, 3));
			this.controlDisplay = new JPanel(new BorderLayout());
			this.mapPanel = new JPanel();
			this.refresh();
			add(new JScrollPane(mapPanel));
			add(controlDisplay, BorderLayout.PAGE_END);
			add(timePanel, BorderLayout.NORTH);
		}
		
		public void refresh() {
			timePanel.removeAll();
			timePanel.setLayout(new GridLayout(1, 3));
			timePanel.add(new JLabel("Time: " + GeneralGameplayManager.getTimeAsString()));
			timePanel.add(new JLabel("Actions: " + GeneralGameplayManager.getActionsLeft()));
			JButton endTurn = new JButton("End Turn");
			endTurn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					String[] opts = {"Yes", "No"};
					int decision = JOptionPane.showOptionDialog(null,
							"End Your Turn?", "CONFIRM",
							JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
							null, opts, opts[0]);
					if (decision == 0) {
						GeneralGameplayManager.endPlayerTurn();
						worldMapPanel.refresh();
						switchToPanel(MAP);
					}
				}
			});
			timePanel.add(endTurn);
			JPanel controlPanel = new JPanel(new GridLayout(10, 1));
			for (int q = 0; q < 10; q++) {
				controlPanel.add(new JPanel());
			}
			controlDisplay.removeAll();
			controlDisplay.setLayout(new BorderLayout());
			controlDisplay.add(controlPanel);
			JPanel controlLabels = new JPanel(new GridLayout(1, 5));
			controlLabels.add(new JLabel("Tile"));
			controlLabels.add(new JLabel("Building"));
			controlLabels.add(new JLabel("Unit"));
			controlLabels.add(new JLabel("Battle"));
			controlLabels.add(new JLabel("News"));
			controlDisplay.add(controlLabels, BorderLayout.NORTH);
			mapPanel.removeAll();
			mapPanel.setLayout(new GridLayout(WorldMap.SQRT_OF_MAP_SIZE, WorldMap.SQRT_OF_MAP_SIZE));
			for (int q = 0; q < WorldMap.SQRT_OF_MAP_SIZE; q++) {
				for (int w = 0; w < WorldMap.SQRT_OF_MAP_SIZE; w++) {
					mapPanel.add(getTile(q, w, controlPanel));
				}
			}
		}
		
		public JPanel controlDisplay() {
			return this.controlDisplay;
		}
		
		public JPanel mapPanel() {
			return mapPanel;
		}
	}
	
	private JButton getTile(final int x, final int y, final JPanel controlPanel) {
		WorldMap map = GeneralGameplayManager.getWorldMap();
		JButton ret = new JButton();
		final WorldMapTile tile = map.at(x, y);
		final CityState cs = tile.getOwner();
		
		alterWorldMapTileAppearance(ret, x, y, false, null);
		
		final List<WorldMapTile> surroundings = new List<>(4, 4);
		if (x > 0) {
			surroundings.add(map.at(x - 1, y));
		}
		if (x < WorldMap.SQRT_OF_MAP_SIZE - 1) {
			surroundings.add(map.at(x + 1, y));
		}
		if (y > 0) {
			surroundings.add(map.at(x, y - 1));
		}
		if (y < WorldMap.SQRT_OF_MAP_SIZE - 1) {
			surroundings.add(map.at(x, y + 1));
		}
		ret.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controlPanel.removeAll();
				controlPanel.setLayout(new GridLayout(1, 5));
				List<Component> tileParts = new List<>();
				JPanel terrain = new JPanel(new GridLayout(1, 2));
				terrain.add(new JLabel("Terrain:"));
				terrain.add(new JLabel(tile.getType().getName()));
				tileParts.add(terrain);
				JPanel magic = new JPanel(new GridLayout(1, 2));
				magic.add(new JLabel("Magic:"));
				magic.add(new JLabel(String.format("%s (%d)",
						tile.getMagicTypeAsString(), tile.getMagicPotency())));
				tileParts.add(magic);
				if (cs == null) {
					boolean canClaim = false;
					final List<CityState> joinable = new List<>(4, 4);
					for (int q = 0; q < surroundings.size(); q++) {
						CityState test = surroundings.get(q).getOwner();
						if (test != null && test.getNation() == playerNation()) {
							canClaim = true;
							if (test.canExpand()) {
								joinable.addWithoutRepeating(test);
							}
						}
					}
					int size = joinable.size();
					if (canClaim) {
						size++; //Plus one for creating a new city
						final String[] opts = new String[size + 1];
						for (int q = 0; q < joinable.size(); q++) {
							opts[q] = joinable.get(q).getName();
						}
						final String newCity = "*New City*";
						opts[opts.length - 2] = newCity;
						opts[opts.length - 1] = "Cancel";
						JButton claim = new JButton("Claim Tile");
						claim.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								int selected = JOptionPane.showOptionDialog(null, "Claim for Which City?", "CLAIM",
										JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
										null, opts, opts[0]);
								if (opts[selected].equals(newCity)) {
									String name = JOptionPane.showInputDialog("Name of new city (leave blank for random)");
									if (name != null) {
										if (name.equals("")) {
											GeneralGameplayManager.addPlayerCityState(tile);
											worldMapPanel.refresh();
											switchToPanel(MAP);
										} else {
											try {
												name = validateInputName(name, "City Name");
												GeneralGameplayManager.addPlayerCityState(name, tile);
												worldMapPanel.refresh();
												switchToPanel(MAP);
											} catch (IllegalArgumentException ex) {
												JOptionPane.showMessageDialog(null, ex.getMessage());
											}
										}
									}
								} else if (selected < opts.length - 1) {
									GeneralGameplayManager.claimTileForNation(joinable.get(selected), tile);
									worldMapPanel.refresh();
									switchToPanel(MAP);
								}
							}
						});
						tileParts.add(claim);
					}
				} else {
					JPanel nation = new JPanel(new GridLayout(1, 2));
					nation.add(new JLabel("Nation:"));
					JButton viewNation = new JButton(cs.getNation().getName());
					viewNation.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							switchToPanel(new NationStatsPanel(cs.getNation()), VIEW);
						}
					});
					nation.add(viewNation);
					tileParts.add(nation);
					JPanel city = new JPanel(new GridLayout(1, 2));
					city.add(new JLabel("City:"));
					city.add(new JLabel(cs.getName()));
					tileParts.add(city);
					if (cs.getNation() != playerNation()) {
						JButton manage = new JButton("Diplomacy");
						manage.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								DiplomaticRelation dr = playerNation().relationshipWith(cs.getNation());
								switchToPanel(new DiplomacyStatsPanel(dr), VIEW);
							}
						});
						tileParts.add(manage);
					}
				}
				JPanel tileInfo = new JPanel(new GridLayout(tileParts.size(), 1));
				for (int q = 0; q < tileParts.size(); q++) {
					tileInfo.add(tileParts.get(q));
				}
				controlPanel.add(tileInfo);
				
				if (tile.getBuilding() == null && tile.getType() != WorldMapTileType.DEEP_WATER
						&& tile.getType() != WorldMapTileType.SHALLOW_WATER) {
					if (cs != null && cs.getNation() == playerNation()) {
						List<String> optsList = new List<>();
						if (!(cs.getResidentialAreas().isFull())) {
							optsList.add(Building.VILLAGE);
						}
						if (!(cs.getNobleResidences().isFull())) {
							optsList.add(Building.CASTLE);
						}
						if (!(cs.getOtherBuildings().isFull())) {
							optsList.add(Building.HOSPITAL);
							optsList.add(Building.COLISEUM);
							optsList.add(Building.RESEARCH_CENTER);
							optsList.add(Building.FACTORY);
							optsList.add(Building.FARM);
							optsList.add(Building.MAGIC_PROCESSING_FACILITY);
							optsList.add(Building.MINING_FACILITY);
							optsList.add(Building.RANCH);
							optsList.add(Building.STOREHOUSE);
							optsList.add(Building.TRADE_CENTER);
							optsList.add(Building.BARRACKS);
							optsList.add(Building.FORTRESS);
							optsList.add(Building.PRISON);
							optsList.add(Building.TRAINING_FACILITY);
							for (int q = 0; q < surroundings.size(); q++) {
								if (surroundings.get(q).getType() == WorldMapTileType.DEEP_WATER
										|| surroundings.get(q).getType() == WorldMapTileType.SHALLOW_WATER) {
									optsList.add(Building.PORT);
									optsList.add(Building.SHIPYARD);
									break;
								}
							}
							//TODO if warp pad technology is unlocked, make Warp Pad an option
						}
						String[] opts = new String[optsList.size()];
						for (int q = 0; q < opts.length; q++) {
							opts[q] = optsList.get(q);
						}
						final JComboBox<String> buildOpts = new JComboBox<>(opts);
						JButton build = new JButton("Build");
						if (buildOpts.getItemCount() > 0) {
							build.addActionListener(new ActionListener() {
								@Override
								public void actionPerformed(ActionEvent e) {
									try {
										String buildName = JOptionPane.showInputDialog("Name of building (leave blank for random)");
										if (buildName == null) {
											throw new IllegalArgumentException("Build Canceled");
										} else if (buildName.equals("")) {
											buildName = RNGStuff.newLocationName(cs.getLanguage());
										} else {
											buildName = validateInputName(buildName, "Building Name");
										}
										if (buildOpts.getSelectedItem().equals(Building.VILLAGE)) {
											GeneralGameplayManager.addBuildingToCityAndMap(new Village(buildName, cs),
													cs, tile);
										} else if (buildOpts.getSelectedItem().equals(Building.CASTLE)) {
											GeneralGameplayManager.addBuildingToCityAndMap(new Castle(buildName, tile),
													cs, tile);
										} else if (buildOpts.getSelectedItem().equals(Building.HOSPITAL)) {
											GeneralGameplayManager.addBuildingToCityAndMap(new Hospital(buildName,
													Human.completelyRandomHuman(cs)),
													cs, tile);
										} else if (buildOpts.getSelectedItem().equals(Building.COLISEUM)) {
											GeneralGameplayManager.addBuildingToCityAndMap(new Coliseum(buildName,
													Human.completelyRandomHuman(cs)),
													cs, tile);
										} else if (buildOpts.getSelectedItem().equals(Building.RESEARCH_CENTER)) {
											GeneralGameplayManager.addBuildingToCityAndMap(new ResearchCenter(buildName,
													Human.completelyRandomHuman(cs)),
													cs, tile);
										} else if (buildOpts.getSelectedItem().equals(Building.FACTORY)) {
											GeneralGameplayManager.addBuildingToCityAndMap(new Factory(buildName,
													Human.completelyRandomHuman(cs), tile),
													cs, tile);
										} else if (buildOpts.getSelectedItem().equals(Building.FARM)) {
											GeneralGameplayManager.addBuildingToCityAndMap(new Farm(buildName,
													Human.completelyRandomHuman(cs), tile),
													cs, tile);
										} else if (buildOpts.getSelectedItem().equals(Building.MAGIC_PROCESSING_FACILITY)) {
											GeneralGameplayManager.addBuildingToCityAndMap(new MagicProcessingFacility(buildName,
													Human.completelyRandomHuman(cs), tile),
													cs, tile);
										} else if (buildOpts.getSelectedItem().equals(Building.MINING_FACILITY)) {
											GeneralGameplayManager.addBuildingToCityAndMap(new MiningFacility(buildName,
													Human.completelyRandomHuman(cs), tile),
													cs, tile);
										} else if (buildOpts.getSelectedItem().equals(Building.RANCH)) {
											GeneralGameplayManager.addBuildingToCityAndMap(new Ranch(buildName,
													Human.completelyRandomHuman(cs), tile),
													cs, tile);
										} else if (buildOpts.getSelectedItem().equals(Building.STOREHOUSE)) {
											GeneralGameplayManager.addBuildingToCityAndMap(new Storehouse(buildName,
													Human.completelyRandomHuman(cs), tile),
													cs, tile);
										} else if (buildOpts.getSelectedItem().equals(Building.TRADE_CENTER)) {
											GeneralGameplayManager.addBuildingToCityAndMap(new TradeCenter(buildName,
													Human.completelyRandomHuman(cs), tile),
													cs, tile);
										} else if (buildOpts.getSelectedItem().equals(Building.BARRACKS)) {
											GeneralGameplayManager.addBuildingToCityAndMap(new Barracks(buildName,
													Human.completelyRandomHuman(cs), tile),
													cs, tile);
										} else if (buildOpts.getSelectedItem().equals(Building.FORTRESS)) {
											GeneralGameplayManager.addBuildingToCityAndMap(new Fortress(buildName,
													Human.completelyRandomHuman(cs), tile),
													cs, tile);
										} else if (buildOpts.getSelectedItem().equals(Building.PRISON)) {
											GeneralGameplayManager.addBuildingToCityAndMap(new Prison(buildName,
													Human.completelyRandomHuman(cs), tile),
													cs, tile);
										} else if (buildOpts.getSelectedItem().equals(Building.TRAINING_FACILITY)) {
											GeneralGameplayManager.addBuildingToCityAndMap(new TrainingFacility(buildName,
													Human.completelyRandomHuman(cs), tile), cs, tile);
										} else if (buildOpts.getSelectedItem().equals(Building.PORT)) {
											GeneralGameplayManager.addBuildingToCityAndMap(new Port(buildName,
													Human.completelyRandomHuman(cs)),
													cs, tile);
										} else if (buildOpts.getSelectedItem().equals(Building.SHIPYARD)) {
											GeneralGameplayManager.addBuildingToCityAndMap(new Shipyard(buildName,
													Human.completelyRandomHuman(cs)),
													cs, tile);
										} else if (buildOpts.getSelectedItem().equals(Building.WARP_PAD)) {
											GeneralGameplayManager.addBuildingToCityAndMap(new WarpPad(buildName,
													Human.completelyRandomHuman(cs)),
													cs, tile);
										}
										worldMapPanel.refresh();
										switchToPanel(MAP);
									} catch (IllegalArgumentException ex) {
										JOptionPane.showMessageDialog(null, ex.getMessage());
									}
								}
							});
						} else {
							JOptionPane.showMessageDialog(null, "Cannot build here");
						}
						JPanel makeBuilds = new JPanel(new GridLayout(1, 2));
						makeBuilds.add(buildOpts);
						makeBuilds.add(build);
						controlPanel.add(makeBuilds);
					} else {
						controlPanel.add(new JPanel());
					}
				} else {
					final Building b = tile.getBuilding();
					List<Component> buildingParts = new List<>();
					JPanel name = new JPanel(new GridLayout(1, 2));
					name.add(new JLabel("Name:"));
					name.add(new JLabel(b.getNameAndType()));
					buildingParts.add(name);
					JPanel hp = new JPanel(new GridLayout(1, 2));
					hp.add(new JLabel("HP:"));
					hp.add(new JLabel(b.getCurrentHP() + "/" + b.getMaximumHP()));
					buildingParts.add(hp);
					if (cs.getNation() == playerNation()) {
						JButton enter = enterBuildingButton(b, "Enter");
						buildingParts.add(enter);
						final JTextField newName = new JTextField();
						JButton rename = new JButton("Rename");
						rename.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								try {
									if (newName.getText() == null || newName.getText().equals("")) {
										throw new IllegalArgumentException("Name cannot be blank");
									}
									String toName = validateInputName(newName.getText(), "New Name");
									String[] opts = {"No", "Yes"};
									int decision = JOptionPane.showOptionDialog(null,
											String.format("Rename %s to %s?", b.getName(), toName),
											"CONFIRM",
											JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
											null, opts, opts[0]);
									if (decision == 1) {
										b.rename(toName);
										worldMapPanel.refresh();
										switchToPanel(MAP);
									}
								} catch (IllegalArgumentException ex) {
									JOptionPane.showMessageDialog(null, ex.getMessage());
								}
							}
						});
						JPanel nameDecision = new JPanel(new GridLayout(1, 2));
						nameDecision.add(newName);
						nameDecision.add(rename);
						buildingParts.add(nameDecision);
						JButton destroy = new JButton("Destroy");
						destroy.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								String[] opts = {"No", "Yes"};
								int decision = JOptionPane.showOptionDialog(null,
										String.format("Destroy %s?", b.getName()),
										"CONFIRM",
										JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
										null, opts, opts[0]);
								if (decision == 1) {
									//TODO destroy
								}
							}
						});
						buildingParts.add(destroy);
						JPanel buildingInfo = new JPanel(new GridLayout(buildingParts.size(), 1));
						for (int q = 0; q < buildingParts.size(); q++) {
							buildingInfo.add(buildingParts.get(q));
						}
						controlPanel.add(buildingInfo);
					} else {
						controlPanel.add(new JPanel());
					}
				}
				
				if (tile.getGroupPresent() == null) {
					controlPanel.add(new JPanel());
				} else {
					List<Component> unitParts = new List<>();
					if (tile.getGroupPresent() instanceof UnitGroup) {
						final UnitGroup u = (UnitGroup)tile.getGroupPresent();
						JPanel leader = new JPanel(new GridLayout(1, 2));
						leader.add(new JLabel("Team:"));
						JButton viewTeam = new JButton(u.getLeader().getName() + "'s Team");
						viewTeam.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								switchToPanel(new UnitGroupStatsPanel(u), VIEW);
							}
						});
						leader.add(viewTeam);
						unitParts.add(leader);
						JPanel nation = new JPanel(new GridLayout(1, 2));
						nation.add(new JLabel("Affiliation:"));
						if (u.getAffiliation() == null) {
							nation.add(new JLabel("None"));
						} else {
							JButton viewNation = new JButton(u.getAffiliation().getName());
							viewNation.addActionListener(new ActionListener() {
								@Override
								public void actionPerformed(ActionEvent e) {
									switchToPanel(new NationStatsPanel(u.getAffiliation()), VIEW);
								}
							});
							nation.add(viewNation);
						}
						unitParts.add(nation);
						JPanel move = new JPanel(new GridLayout(1, 2));
						move.add(new JLabel("Movement:"));
						String moveAndFly = u.getMovement() + "";
						if (u.canFly()) {
							moveAndFly += " (Fly)";
						}
						move.add(new JLabel(moveAndFly));
						unitParts.add(move);
						if (u.getPrisoners() != null) {
							UnitGroup pr = u.getPrisoners();
							JPanel prisoners = new JPanel(new GridLayout(1, 2));
							prisoners.add(new JLabel("Prisoners:"));
							JButton viewPrisoners = new JButton(pr.getLeader().getName() + "'s Team");
							viewPrisoners.addActionListener(new ActionListener() {
								@Override
								public void actionPerformed(ActionEvent e) {
									switchToPanel(new UnitGroupStatsPanel(u.getPrisoners()), VIEW);
								}
							});
							prisoners.add(viewPrisoners);
							unitParts.add(prisoners);
						}
					} else if (tile.getGroupPresent() instanceof Ship) {
						//TODO
						final Ship ship = (Ship)tile.getGroupPresent();
						JPanel name = new JPanel(new GridLayout(1, 2));
						name.add(new JLabel("Ship:"));
						JButton viewShip = new JButton(ship.getShipDescription());
						viewShip.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								switchToPanel(new ShipStatsPanel(ship), VIEW);
							}
						});
						name.add(viewShip);
						unitParts.add(name);
						JPanel nation = new JPanel(new GridLayout(1, 2));
						nation.add(new JLabel("Affiliation:"));
						if (ship.assignedGroup() == null) {
							nation.add(new JLabel("None"));
						} else {
							JButton viewNation = new JButton(ship.getAffiliation().getName());
							viewNation.addActionListener(new ActionListener() {
								@Override
								public void actionPerformed(ActionEvent e) {
									switchToPanel(new NationStatsPanel(ship.assignedGroup().getAffiliation()), VIEW);
								}
							});
							nation.add(viewNation);
						}
						unitParts.add(nation);
						JPanel move = new JPanel(new GridLayout(1, 2));
						move.add(new JLabel("Movement:"));
						move.add(new JLabel(ship.getMovement() + ""));
						unitParts.add(move);
					}
					
					if (tile.getGroupPresent().getAffiliation() == playerNation()) {
						JButton order = getUnitOrders(tile.getGroupPresent(), x, y);
						unitParts.add(order);
					}
					
					JPanel unitInfo = new JPanel(new GridLayout(unitParts.size(), 1));
					for (int q = 0; q < unitParts.size(); q++) {
						unitInfo.add(unitParts.get(q));
					}
					controlPanel.add(unitInfo);
				}
				
				if (tile.getBattle() == null) {
					controlPanel.add(new JPanel());
				} else {
					//TODO battle info
					List<Component> battleParts = new List<>(4, 4);
					
					JPanel groupsDisplay = new JPanel(new GridLayout(1, 2));
					groupsDisplay.add(new JLabel("Participants:"));
					List<UnitGroup> combatants = tile.getBattle().getCombatants();
					JPanel groupsList = new JPanel(new GridLayout(combatants.size(), 1));
					for (int q = 0; q < combatants.size(); q++) {
						final UnitGroup ug = combatants.get(q);
						JButton view = new JButton(ug.getLeader().getName() + " (" + ug.getAffiliation().getName() + ")");
						view.setPreferredSize(new Dimension(10, 5));
						view.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								switchToPanel(new UnitGroupStatsPanel(ug), VIEW);
							}
						});
						groupsList.add(view);
					}
					groupsDisplay.add(new JScrollPane(groupsList));
					battleParts.add(groupsDisplay);
					
					for (int q = 0; q < combatants.size(); q++) {
						if (combatants.get(q).getAffiliation() == playerNation()) {
							JButton enter = new JButton("Enter");
							enter.addActionListener(new ActionListener() {
								@Override
								public void actionPerformed(ActionEvent e) {
									switchToPanel(new BattleMapPanel(tile.getBattle()), VIEW);
								}
							});
							battleParts.add(enter);
							break;
						}
					}
					
					JPanel fullBattleInfo = new JPanel(new GridLayout(battleParts.size(), 1));
					for (int q = 0; q < battleParts.size(); q++) {
						fullBattleInfo.add(battleParts.get(q));
					}
					controlPanel.add(fullBattleInfo);
				}
				
				//TODO add news
				controlPanel.add(new JPanel());
				
				controlPanel.validate();
				controlPanel.repaint();
				
			}
		});
		return ret;
	}
	
	private void alterWorldMapTileAppearance(JButton tileButton, int x, int y, boolean select,
			WorldMapTile dest) {
		//TODO properly make appearance
		tileButton.setPreferredSize(new Dimension(40, 40));
		WorldMapTile tile = worldMap().at(x, y);
		if (tile.getBuilding() == null) {
			if (tile.getOwner() == null) {
				tileButton.setBackground(Color.GREEN);
			} else {
				tileButton.setBackground(Color.YELLOW);
			}
		} else {
			tileButton.setBackground(Color.RED);
		}
		if (select) {
			tileButton.setBackground(Color.BLUE);
		}
		if (tile.getGroupPresent() != null) {
			tileButton.setBackground(Color.CYAN);
		}
		if (tile.getBattle() != null) {
			tileButton.setBackground(Color.WHITE);
		}
		if (dest == tile) {
			tileButton.setBackground(Color.GRAY);
		}
		
	}
	
	private JButton getUnitOrders(final WMTileOccupant occ, final int x, final int y) {
		JButton ret = new JButton("Order");
		ret.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				final JPanel controlPanel = worldMapPanel.controlDisplay();
				final JPanel map = worldMapPanel.mapPanel();
				final List<Component> possibleOrders = new List<>();
				if (occ instanceof UnitGroup) {
					final UnitGroup group = (UnitGroup)occ;
					HashMap<WorldMapTile, Integer> traversable;
					if (GeneralGameplayManager.eventTime()) {
						traversable = GeneralGameplayManager.getTraversableWorldMapTilesEventTime(group, x, y);
					} else {
						traversable = GeneralGameplayManager.getTraversableWorldMapTilesPeaceTime(group, x, y);
					}
					map.removeAll();
					map.setLayout(new GridLayout(WorldMap.SQRT_OF_MAP_SIZE, WorldMap.SQRT_OF_MAP_SIZE));
					for (int q = 0; q < WorldMap.SQRT_OF_MAP_SIZE; q++) {
						for (int w = 0; w < WorldMap.SQRT_OF_MAP_SIZE; w++) {
							JButton tile = new JButton();
							final int destX = q;
							final int destY = w;
							if (traversable.get(worldMap().at(q, w)) == null) {
								alterWorldMapTileAppearance(tile, q, w, false, null);
							} else {
								alterWorldMapTileAppearance(tile, q, w, true, null);
								tile.addActionListener(new ActionListener() {
									@Override
									public void actionPerformed(ActionEvent e) {
										final WorldMapTile here = worldMap().at(destX, destY);
										map.removeAll();
										map.setLayout(new GridLayout(WorldMap.SQRT_OF_MAP_SIZE, WorldMap.SQRT_OF_MAP_SIZE));
										for (int q = 0; q < WorldMap.SQRT_OF_MAP_SIZE; q++) {
											for (int w = 0; w < WorldMap.SQRT_OF_MAP_SIZE; w++) {
												JButton tile = new JButton();
												alterWorldMapTileAppearance(tile, q, w, false, here);
												map.add(tile);
											}
										}
										final List<WorldMapTile> adjacentTilesWithAllies = worldMap().getAdjacentTilesWithAllies(group, destX, destY);
										List<WorldMapTile> adjacentTilesWithUnassignedShips =
												worldMap().getAdjacentTilesWithUnassignedShips(group, destX, destY);
										JButton standby = new JButton("Standby");
										standby.addActionListener(new ActionListener() {
											@Override
											public void actionPerformed(ActionEvent e) {
												here.sendHere(group);
												group.exhaust();
												switchToWorldMap();
											}
										});
										possibleOrders.add(standby);
										if (!(adjacentTilesWithAllies.isEmpty())) {
											JButton trade = new JButton("Trade");
											trade.addActionListener(new ActionListener() {
												@Override
												public void actionPerformed(ActionEvent e) {
													map.removeAll();
													map.setLayout(new GridLayout(WorldMap.SQRT_OF_MAP_SIZE, WorldMap.SQRT_OF_MAP_SIZE));
													for (int q = 0; q < WorldMap.SQRT_OF_MAP_SIZE; q++) {
														for (int w = 0; w < WorldMap.SQRT_OF_MAP_SIZE; w++) {
															JButton tile = new JButton();
															final int actX = q;
															final int actY = w;
															if (adjacentTilesWithAllies.contains(worldMap().at(q, w))) {
																alterWorldMapTileAppearance(tile, q, w, true, here);
																tile.addActionListener(new ActionListener() {
																	@Override
																	public void actionPerformed(ActionEvent e) {
																		here.sendHere(group);
																		group.exhaust();
																		switchToPanel(groupTradingPanel(group,
																				(UnitGroup)worldMap().at(actX, actY).getGroupPresent()),
																				VIEW);
																	}
																});
															} else {
																alterWorldMapTileAppearance(tile, q, w, false, here);
															}
															map.add(tile);
														}
													}
													controlPanel.removeAll();
													controlPanel.setLayout(new BorderLayout());
													controlPanel.add(returnToWorldMap("Cancel"));
													validate();
													repaint();
												}
											});
											JButton transUnit = new JButton("Transfer Units");
											transUnit.addActionListener(new ActionListener() {
												@Override
												public void actionPerformed(ActionEvent e) {
													map.removeAll();
													map.setLayout(new GridLayout(WorldMap.SQRT_OF_MAP_SIZE, WorldMap.SQRT_OF_MAP_SIZE));
													for (int q = 0; q < WorldMap.SQRT_OF_MAP_SIZE; q++) {
														for (int w = 0; w < WorldMap.SQRT_OF_MAP_SIZE; w++) {
															JButton tile = new JButton();
															final int actX = q;
															final int actY = w;
															if (adjacentTilesWithAllies.contains(worldMap().at(q, w))) {
																alterWorldMapTileAppearance(tile, q, w, true, here);
																tile.addActionListener(new ActionListener() {
																	@Override
																	public void actionPerformed(ActionEvent e) {
																		here.sendHere(group);
																		group.exhaust();
																		UnitGroup other = (UnitGroup)worldMap().at(actX, actY).getGroupPresent();
																		switchToPanel(new TransferUnitsPanel(group,
																				other, here, other.getLocation()),
																				VIEW);
																	}
																});
															} else {
																alterWorldMapTileAppearance(tile, q, w, false, here);
															}
															map.add(tile);
														}
													}
													controlPanel.removeAll();
													controlPanel.setLayout(new BorderLayout());
													controlPanel.add(returnToWorldMap("Cancel"));
													validate();
													repaint();
												}
											});
											JButton transPrisoner = new JButton("Transfer Prisoners");
											transPrisoner.addActionListener(new ActionListener() {
												@Override
												public void actionPerformed(ActionEvent e) {
													map.removeAll();
													map.setLayout(new GridLayout(WorldMap.SQRT_OF_MAP_SIZE, WorldMap.SQRT_OF_MAP_SIZE));
													for (int q = 0; q < WorldMap.SQRT_OF_MAP_SIZE; q++) {
														for (int w = 0; w < WorldMap.SQRT_OF_MAP_SIZE; w++) {
															JButton tile = new JButton();
															final int actX = q;
															final int actY = w;
															final UnitGroup other;
															if (adjacentTilesWithAllies.contains(worldMap().at(q, w))) {
																other = (UnitGroup)worldMap().at(actX, actY).getGroupPresent();
															} else {
																other = null;
															}
															if (other != null && (group.getPrisoners() != null || other.getPrisoners() != null)) {
																alterWorldMapTileAppearance(tile, q, w, true, here);
																if (group.getPrisoners() != null) {
																	tile.addActionListener(new ActionListener() {
																		@Override
																		public void actionPerformed(ActionEvent e) {
																			here.sendHere(group);
																			group.exhaust();
																			switchToPanel(new TransferUnitsPanel(group.getPrisoners(),
																					other.getPrisoners(), null, null),
																					VIEW);
																		}
																	});
																} else {
																	tile.addActionListener(new ActionListener() {
																		@Override
																		public void actionPerformed(ActionEvent e) {
																			here.sendHere(group);
																			group.exhaust();
																			switchToPanel(new TransferUnitsPanel(other.getPrisoners(),
																					group.getPrisoners(), null, null),
																					VIEW);
																		}
																	});
																}
															} else {
																alterWorldMapTileAppearance(tile, q, w, false, here);
															}
															map.add(tile);
														}
													}
													controlPanel.removeAll();
													controlPanel.setLayout(new BorderLayout());
													controlPanel.add(returnToWorldMap("Cancel"));
													validate();
													repaint();
												}
											});
											possibleOrders.add(trade);
											possibleOrders.add(transUnit);
											possibleOrders.add(transPrisoner);
										}
										if (group.size() > 1
												&& worldMap().getAdjacentUnoccupiedTile(destX, destY) != null) {
											JButton split = new JButton("Divide Team");
											split.addActionListener(new ActionListener() {
												@Override
												public void actionPerformed(ActionEvent e) {
													final List<WorldMapTile> allAdjacent = worldMap().getAllTraversableAdjacentTiles(group, destX, destY);
													map.removeAll();
													map.setLayout(new GridLayout(WorldMap.SQRT_OF_MAP_SIZE, WorldMap.SQRT_OF_MAP_SIZE));
													for (int q = 0; q < WorldMap.SQRT_OF_MAP_SIZE; q++) {
														for (int w = 0; w < WorldMap.SQRT_OF_MAP_SIZE; w++) {
															JButton tile = new JButton();
															final int actX = q;
															final int actY = w;
															if (allAdjacent.contains(worldMap().at(q, w))) {
																alterWorldMapTileAppearance(tile, q, w, true, here);
																tile.addActionListener(new ActionListener() {
																	@Override
																	public void actionPerformed(ActionEvent e) {
																		here.sendHere(group);
																		group.exhaust();
																		switchToPanel(new TransferUnitsPanel(group,
																				null, here,
																				worldMap().at(actX, actY),
																				group.size() - 1),
																				VIEW);
																	}
																});
															} else {
																alterWorldMapTileAppearance(tile, q, w, false, here);
															}
															map.add(tile);
														}
													}
													controlPanel.removeAll();
													controlPanel.setLayout(new BorderLayout());
													controlPanel.add(returnToWorldMap("Cancel"));
													validate();
													repaint();
												}
											});
											possibleOrders.add(split);
										}
										if (here.getOwner() != null && here.getOwner().getNation() == group.getAffiliation()
												&& here.getBuilding() != null) {
											
											if (here.getBuilding() instanceof Defendable
													&& ((Defendable)here.getBuilding()).getAssignedGroup() == null) {
												JButton assign = new JButton("Defend Building");
												assign.addActionListener(new ActionListener() {
													@Override
													public void actionPerformed(ActionEvent e) {
														here.sendHere(group);
														group.exhaust();
														here.removeGroupOrShip();
														group.giveAssignment((Defendable)here.getBuilding());
														switchToWorldMap();
													}
												});
												possibleOrders.add(assign);
											}
											
											if (here.getBuilding() instanceof Prison
													&& ((Prison)here.getBuilding()).canAcceptPrisonersFromGroup(group)
													&& group.getPrisoners() != null) {
												JButton imprison = new JButton("Deposit Prisoners");
												imprison.addActionListener(new ActionListener() {
													@Override
													public void actionPerformed(ActionEvent e) {
														((Prison)here.getBuilding()).acceptPrisonersFromGroup(group);
													}
												});
											}
										}
										
										
										if (!(adjacentTilesWithUnassignedShips.isEmpty())) {
											JButton assign = new JButton("Enter Ship");
											assign.addActionListener(new ActionListener() {
												@Override
												public void actionPerformed(ActionEvent e) {
													here.sendHere(group);
													group.exhaust();
													// TODO assign to ship
												}
											});
											possibleOrders.add(assign);
										}
										
										if (GeneralGameplayManager.eventTime()) {
											//TODO event time commands
											final List<WorldMapTile> adjacentTilesWithEnemies = worldMap().getAdjacentTilesWithAttackableEnemies(group, destX, destY);
											if (!(adjacentTilesWithEnemies.isEmpty())) {
												JButton engage = new JButton("Engage Enemy");
												engage.addActionListener(new ActionListener() {
													@Override
													public void actionPerformed(ActionEvent e) {
														map.removeAll();
														map.setLayout(new GridLayout(WorldMap.SQRT_OF_MAP_SIZE, WorldMap.SQRT_OF_MAP_SIZE));
														for (int q = 0; q < WorldMap.SQRT_OF_MAP_SIZE; q++) {
															for (int w = 0; w < WorldMap.SQRT_OF_MAP_SIZE; w++) {
																JButton tile = new JButton();
																final int actX = q;
																final int actY = w;
																if (adjacentTilesWithEnemies.contains(worldMap().at(q, w))) {
																	alterWorldMapTileAppearance(tile, q, w, true, here);
																	tile.addActionListener(new ActionListener() {
																		@Override
																		public void actionPerformed(ActionEvent e) {
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
																			switchToWorldMap();
																		}
																	});
																} else {
																	alterWorldMapTileAppearance(tile, q, w, false, here);
																}
																map.add(tile);
															}
														}
														controlPanel.removeAll();
														controlPanel.setLayout(new BorderLayout());
														controlPanel.add(returnToWorldMap("Cancel"));
														validate();
														repaint();
													}
												});
												possibleOrders.add(engage);
											}
											if (here.getOwner() != null
													&& group.getAffiliation().isAtWarWith(here.getOwner().getNation())
													&& here.getBuilding() != null
													&& (!(here.getBuilding() instanceof Defendable)
															|| ((Defendable)here.getBuilding()).getAssignedGroup() == null)) {
												JButton destroy = new JButton("Destroy Building");
												destroy.addActionListener(new ActionListener() {
													@Override
													public void actionPerformed(ActionEvent e) {
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
													JButton seize = new JButton("Seize");
													seize.addActionListener(new ActionListener() {
														@Override
														public void actionPerformed(ActionEvent e) {
															here.sendHere(group);
															group.exhaust();
															//TODO seize control of city
														}
													});
													possibleOrders.add(seize);
												}
											}
										}
										possibleOrders.add(returnToWorldMap("Cancel"));
										
										int gridWidth = 2;
										int gridHeight = possibleOrders.size() / gridWidth;
										if (possibleOrders.size() % gridWidth != 0) {
											gridHeight++;
										}
										controlPanel.removeAll();
										controlPanel.setLayout(new GridLayout(gridHeight, gridWidth));
										for (int order = 0; order < possibleOrders.size(); order++) {
											controlPanel.add(possibleOrders.get(order));
										}
										
										validate();
										repaint();
									}
								});
								
							}
							map.add(tile);
						}
					}
					controlPanel.removeAll();
					controlPanel.setLayout(new BorderLayout());
					controlPanel.add(returnToWorldMap("Cancel"));
					validate();
					repaint();
				} else if (occ instanceof Ship) {
					final Ship group = (Ship)occ;
					HashMap<WorldMapTile, Integer> traversable =
							GeneralGameplayManager.getTraversableTilesForShipPeaceTime(group, x, y);
					map.removeAll();
					map.setLayout(new GridLayout(WorldMap.SQRT_OF_MAP_SIZE, WorldMap.SQRT_OF_MAP_SIZE));
					for (int q = 0; q < WorldMap.SQRT_OF_MAP_SIZE; q++) {
						for (int w = 0; w < WorldMap.SQRT_OF_MAP_SIZE; w++) {
							JButton tile = new JButton();
							final int destX = q;
							final int destY = w;
							if (traversable.get(worldMap().at(q, w)) == null) {
								alterWorldMapTileAppearance(tile, q, w, false, null);
							} else {
								alterWorldMapTileAppearance(tile, q, w, true, null);
								tile.addActionListener(new ActionListener() {
									@Override
									public void actionPerformed(ActionEvent e) {
										final WorldMapTile here = worldMap().at(destX, destY);
										map.removeAll();
										map.setLayout(new GridLayout(WorldMap.SQRT_OF_MAP_SIZE, WorldMap.SQRT_OF_MAP_SIZE));
										for (int q = 0; q < WorldMap.SQRT_OF_MAP_SIZE; q++) {
											for (int w = 0; w < WorldMap.SQRT_OF_MAP_SIZE; w++) {
												JButton tile = new JButton();
												alterWorldMapTileAppearance(tile, q, w, false, here);
												map.add(tile);
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
											JButton drop = new JButton("Drop Team");
											drop.addActionListener(new ActionListener() {
												@Override
												public void actionPerformed(ActionEvent e) {
													// TODO Auto-generated method stub
												}
											});
											possibleOrders.add(drop);
										}
										if (!(adjacentTilesWithAllies.isEmpty())) {
											JButton transPrisoner = new JButton("Drop Team");
											transPrisoner.addActionListener(new ActionListener() {
												@Override
												public void actionPerformed(ActionEvent e) {
													// TODO Auto-generated method stub
												}
											});
											JButton trade = new JButton("Trade");
											trade.addActionListener(new ActionListener() {
												@Override
												public void actionPerformed(ActionEvent e) {
													// TODO Auto-generated method stub
												}
											});
											possibleOrders.add(transPrisoner);
											possibleOrders.add(trade);
										}
										if (!(adjacentTilesWithPorts.isEmpty())) {
											JButton ports = new JButton("Drop Team");
											ports.addActionListener(new ActionListener() {
												@Override
												public void actionPerformed(ActionEvent e) {
													// TODO Auto-generated method stub
												}
											});
											possibleOrders.add(ports);
										}
										
										if (GeneralGameplayManager.eventTime()) {
											List<WorldMapTile> adjacentAttackableTiles = worldMap().getAdjacentTilesWithAttackableEnemies(group, destX, destY);
											List<WorldMapTile> attackableFromWorldMap = worldMap().getTilesAttackableWithShip(group, destX, destY);
											if (!(adjacentAttackableTiles.isEmpty())) {
												JButton engage = new JButton("Engage");
												engage.addActionListener(new ActionListener() {
													@Override
													public void actionPerformed(ActionEvent e) {
														// TODO Auto-generated method stub
													}
												});
												possibleOrders.add(engage);
											}
											if (!(attackableFromWorldMap.isEmpty())) {
												JButton attack = new JButton("Attack");
												attack.addActionListener(new ActionListener() {
													@Override
													public void actionPerformed(ActionEvent e) {
														// TODO Auto-generated method stub
													}
												});
												possibleOrders.add(attack);
											}
										}
										
										possibleOrders.add(returnToWorldMap("Cancel"));
										
										int gridWidth = 2;
										int gridHeight = possibleOrders.size() / gridWidth;
										if (possibleOrders.size() % gridWidth != 0) {
											gridHeight++;
										}
										controlPanel.removeAll();
										controlPanel.setLayout(new GridLayout(gridHeight, gridWidth));
										for (int order = 0; order < possibleOrders.size(); order++) {
											controlPanel.add(possibleOrders.get(order));
										}
										
										validate();
										repaint();

									}
								});
							}
						}
					}
				}
			}
		});
		return ret;
	}
	
	private JPanel groupTradingPanel(final UnitGroup g1, final UnitGroup g2) {
		JPanel ret = new JPanel(new BorderLayout());
		//TODO
		ret.add(returnToWorldMap("DONE"), BorderLayout.SOUTH);
		return ret;
	}
	
	private class TransferUnitsPanel extends JPanel {
		public TransferUnitsPanel(final UnitGroup g1, final UnitGroup g2,
				final WorldMapTile g1Tile, final WorldMapTile g2Tile) {
			this(g1, g2, g1Tile, g2Tile, UnitGroup.CAPACITY);
		}
		public TransferUnitsPanel(final UnitGroup g1, final UnitGroup g2,
				final WorldMapTile g1Tile, final WorldMapTile g2Tile, final int sizeLimit) {
			
			setLayout(new BorderLayout());
			final JPanel unitPreview = new JPanel();
			final JPanel urgentDecision = new JPanel();
			int y = Math.max(g1.size() / 4, 4);
			if (g1.size() % 4 != 0) {
				y++;
			}
			final JPanel group1List = new JPanel(new GridLayout(y, 4));
			final JPanel group2List = new JPanel();
			for (int q = 0; q < g1.size(); q++) {
				final Unit u = g1.get(q);
				JPanel portraitAndStats = unitPortraitAndStats(u);
				JButton preview = new JButton("View");
				preview.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						unitPreview.removeAll();
						unitPreview.setLayout(new BorderLayout());
						unitPreview.add(unitPreview(u));
						JButton toPage = new JButton("View All Stats");
						toPage.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								JButton back = new JButton("BACK");
								back.addActionListener(new ActionListener() {
									@Override
									public void actionPerformed(ActionEvent e) {
										switchToPanel(new TransferUnitsPanel(g1, g2, g1Tile, g2Tile, sizeLimit), VIEW);
									}
								});
								switchToPanel(new UnitStatsPanel(u, back), VIEW);
							}
						});
						unitPreview.add(toPage, BorderLayout.SOUTH);
						unitPreview.validate();
						unitPreview.repaint();
					}
				});
				JButton transfer = new JButton("Transfer");
				transfer.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						if (g2 == null) {
							//Both groups had people when the transfer process started, so if
							//group2 is now empty, then group1 has at least 2 people. So no need
							//to check for group1 being empty
							g1.remove(u);
							UnitGroup created = new UnitGroup(u);
							if (g2Tile != null) {
								g2Tile.sendHere(created);
							}
							switchToPanel(new TransferUnitsPanel(g1, created, g1Tile, g2Tile, sizeLimit), VIEW);
						} else if (g2.size() == sizeLimit) {
							group1List.removeAll();
							group2List.removeAll();
							urgentDecision.removeAll();
							urgentDecision.setLayout(new BorderLayout());
							urgentDecision.add(urgentDecision(u, g1, g2, g1Tile, g2Tile, true, sizeLimit));
							validate();
							repaint();
						} else {
							g1.remove(u);
							g2.add(u);
							if (g1.getMembers().isEmpty()) {
								if (g1Tile != null) {
									g1Tile.removeGroupOrShip();
								}
								//Switch positions to keep recursion correct
								switchToPanel(new TransferUnitsPanel(g2, null, g2Tile, g1Tile, sizeLimit), VIEW);
							} else {
								switchToPanel(new TransferUnitsPanel(g1, g2, g1Tile, g2Tile, sizeLimit), VIEW);
							}
						}
					}
				});
				
				JPanel unitDisplay = new JPanel(new BorderLayout());
				unitDisplay.add(portraitAndStats);
				JPanel options = new JPanel(new GridLayout(1, 2));
				options.add(preview);
				options.add(transfer);
				unitDisplay.add(options, BorderLayout.SOUTH);
				
				group1List.add(unitDisplay);
			}
			
			int fill = (y * 4) - g1.size();
			for (int q = 0; q < fill; q++) {
				//Make sure it's even
				group1List.add(new JPanel());
			}
			
			if (g2 != null) {
				y = Math.max(g2.size() / 4, 4);
				if (g2.size() % 4 != 0) {
					y++;
				}
				group2List.setLayout(new GridLayout(y, 4));
				for (int q = 0; q < g2.size(); q++) {
					final Unit u = g2.get(q);
					JPanel portraitAndStats = unitPortraitAndStats(u);
					JButton preview = new JButton("View");
					preview.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							unitPreview.removeAll();
							unitPreview.setLayout(new BorderLayout());
							unitPreview.add(unitPreview(u));
							JButton toPage = new JButton("View All Stats");
							toPage.addActionListener(new ActionListener() {
								@Override
								public void actionPerformed(ActionEvent e) {
									JButton back = new JButton("BACK");
									back.addActionListener(new ActionListener() {
										@Override
										public void actionPerformed(ActionEvent e) {
											switchToPanel(new TransferUnitsPanel(g1, g2, g1Tile, g2Tile, sizeLimit), VIEW);
										}
									});
									switchToPanel(new UnitStatsPanel(u, back), VIEW);
								}
							});
							unitPreview.add(toPage, BorderLayout.SOUTH);
							unitPreview.validate();
							unitPreview.repaint();
						}
					});
					JButton transfer = new JButton("Transfer");
					transfer.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							//Group1 is never null
							if (g1.size() == sizeLimit) {
								group1List.removeAll();
								group2List.removeAll();
								urgentDecision.removeAll();
								urgentDecision.setLayout(new BorderLayout());
								urgentDecision.add(urgentDecision(u, g1, g2, g1Tile, g2Tile, false, sizeLimit));
								validate();
								repaint();
							} else {
								g2.remove(u);
								g1.add(u);
								if (g2.getMembers().isEmpty()) {
									if (g2Tile != null) {
										g2Tile.removeGroupOrShip();
									}
									switchToPanel(new TransferUnitsPanel(g1, null, g1Tile, g2Tile, sizeLimit), VIEW);
								} else {
									switchToPanel(new TransferUnitsPanel(g1, g2, g1Tile, g2Tile, sizeLimit), VIEW);
								}
							}
						}
					});
					
					JPanel unitDisplay = new JPanel(new BorderLayout());
					unitDisplay.add(portraitAndStats);
					JPanel options = new JPanel(new GridLayout(1, 2));
					options.add(preview);
					options.add(transfer);
					unitDisplay.add(options, BorderLayout.SOUTH);
					
					group2List.add(unitDisplay);
				}
				
				fill = (y * 4) - g2.size();
				for (int q = 0; q < fill; q++) {
					//Make sure it's even
					group2List.add(new JPanel());
				}
			}
			
			JPanel fullDisplay = new JPanel(new GridLayout(2, 2));
			fullDisplay.add(new JScrollPane(group1List));
			fullDisplay.add(new JScrollPane(group2List));
			fullDisplay.add(unitPreview);
			fullDisplay.add(urgentDecision);
			add(new JLabel("Tranfer Units"), BorderLayout.NORTH);
			add(fullDisplay);
			add(returnToWorldMap("DONE"), BorderLayout.SOUTH);
		}
		private JPanel urgentDecision(final Unit unit, final UnitGroup g1, final UnitGroup g2,
				final WorldMapTile g1Tile, final WorldMapTile g2Tile, boolean firstGroup,
				final int sizeLimit) {
			//Neither group is empty and neither will be emptied, so no need to know the tiles
			final UnitGroup from;
			final UnitGroup dest;
			if (firstGroup) {
				from = g1;
				dest = g2;
			} else {
				from = g2;
				dest = g1;
			}
			int y = Math.max(from.size() / 4, 4);
			if (from.size() % 4 != 0) {
				y++;
			}
			JPanel groupList = new JPanel(new GridLayout(y, 4));
			for (int q = 0; q < dest.size(); q++) {
				final Unit replacement = dest.get(q);
				JPanel portraitAndStats = unitPortraitAndStats(replacement);
				JButton select = new JButton("Switch");
				select.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						if (from.isFull()) {
							from.remove(unit);
							dest.remove(replacement);
							from.add(replacement);
							dest.add(unit);
						} else {
							dest.remove(replacement);
							from.add(replacement);
							from.remove(unit);
							dest.add(unit);
						}
						switchToPanel(new TransferUnitsPanel(g1, g2, g1Tile, g2Tile, sizeLimit), VIEW);
					}
				});
				JPanel unitDisplay = new JPanel(new BorderLayout());
				unitDisplay.add(portraitAndStats);
				unitDisplay.add(select, BorderLayout.SOUTH);
				groupList.add(unitDisplay);
			}
			JButton cancel = new JButton("Cancel");
			cancel.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					switchToPanel(new TransferUnitsPanel(g1, g2, g1Tile, g2Tile, sizeLimit), VIEW);
				}
			});
			
			JPanel fullDisplay = new JPanel(new BorderLayout());
			fullDisplay.add(new JScrollPane(groupList));
			fullDisplay.add(cancel, BorderLayout.SOUTH);
			fullDisplay.add(new JLabel("Switch with which unit?"),
					BorderLayout.NORTH);
			return fullDisplay;
		}
	}
	private JPanel transferPrisonersPanel(UnitGroup g1, UnitGroup g2) {
		JPanel ret = new JPanel(new BorderLayout());
		//TODO
		ret.add(returnToWorldMap("Done"), BorderLayout.SOUTH);
		return ret;
	}
	
	private class BattleMapPanel extends JPanel {
		
		private BattleGround battleground;
		private JPanel ordersPanel;
		private JPanel controlPanel;
		private JPanel mapPanel;
		private final int ORDERS_BUTTON_WIDTH = 450;
		
		public BattleMapPanel(BattleGround b) {
			setLayout(new BorderLayout());
			this.battleground = b;
			this.ordersPanel = new JPanel();
			this.controlPanel = new JPanel(new BorderLayout());
			this.mapPanel = new JPanel();
			this.refresh();
			add(new JScrollPane(mapPanel));
			add(controlPanel, BorderLayout.PAGE_END);
			add(ordersPanel, BorderLayout.EAST);
		}
		
		public void refresh() {
			ordersPanel.removeAll();
			ordersPanel.setPreferredSize(new Dimension(ORDERS_BUTTON_WIDTH, 100));
			
			controlPanel.removeAll();
			int width = 10;
			controlPanel.setLayout(new GridLayout(width, 1));
			for (int q = 0; q < width; q++) {
				controlPanel.add(new JPanel());
			}
//			JPanel controlLabels = new JPanel(new GridLayout(1, 5));
//			controlLabels.add(new JLabel("Tile"));
//			controlLabels.add(new JLabel("Building"));
//			controlLabels.add(new JLabel("Unit"));
//			controlLabels.add(new JLabel("Battle"));
//			controlLabels.add(new JLabel("News"));
//			controlDisplay.add(controlLabels, BorderLayout.NORTH);
			mapPanel.removeAll();
			int[] dimensions = battleground.getDimensions();
			mapPanel.setLayout(new GridLayout(dimensions[0], dimensions[1]));
			for (int q = 0; q < dimensions[0]; q++) {
				for (int w = 0; w < dimensions[1]; w++) {
					mapPanel.add(getBattleTile(q, w));
				}
			}
			validate();
			repaint();
		}
		
		public JPanel controlPanel() {
			return this.controlPanel;
		}
		
		public JPanel mapPanel() {
			return mapPanel;
		}
		
		public JPanel ordersPanel() {
			return ordersPanel;
		}
		
		public BattleMapPanel getBattleMap() {
			return this;
		}
		
		private JButton getBattleTile(final int x, final int y) {
			final BattlegroundTile tile = battleground.getMap()[x][y];
			JButton tileButton = new JButton();
			alterBattlegroundTileAppearance(tileButton, x, y, false, null);
			
			tileButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					//Prepare controlPanel
					controlPanel.removeAll();
					ordersPanel.removeAll();
					controlPanel.setLayout(new GridLayout(1, 6));
					ordersPanel.setLayout(new GridLayout(2, 1));
					
					//For components, first initialize terrain part of control panel
					JPanel terrain = new JPanel(new GridLayout(2, 1));
					terrain.setPreferredSize(new Dimension(200, 100));
					terrain.add(new JLabel("Terrain: " + tile.getType().getName()));
					terrain.add(new JLabel("Avoid Bonus: " + tile.getType().getAvoidanceBonus() + "%"));
					controlPanel.add(terrain);
					
					//Then character info, or just fill with blanks if no one is here
					if (tile.getUnit() == null) {
						controlPanel.add(new JPanel());
						controlPanel.add(new JPanel());
						controlPanel.add(new JPanel());
						controlPanel.add(new JPanel());
						JPanel added = new JPanel();
						added.setPreferredSize(new Dimension(ORDERS_BUTTON_WIDTH, 500));
						ordersPanel.add(added);
						ordersPanel.add(new JPanel());
					} else {
						//Portrait
						Unit u = tile.getUnit();
						controlPanel.add(makePortrait(u));
						
						//Stats part 1
						JPanel stats1 = new JPanel(new GridLayout(5, 1));
						stats1.add(new JLabel(u.getName()));
						stats1.add(new JLabel(u.getAffiliation().getFullName()));
						stats1.add(new JLabel(u.getUnitClassName()));
						stats1.add(new JLabel(String.format("LVL: %d EXP: %d", u.getLevel(), u.getExperience())));
						
						//Stats part 2
						JPanel stats2 = new JPanel(new GridLayout(5, 1));
						stats2.add(new JLabel("ATK: " + u.attackStrength()));
						stats2.add(new JLabel("ACC: " + u.accuracy()));
						stats2.add(new JLabel("CRT: " + u.criticalHitRate()));
						stats2.add(new JLabel("AS: " + u.attackSpeed()));
						
						if (u instanceof Equippable) {
							Equippable eq = (Equippable)u;
							stats1.add(new JLabel("Weapon: " + eq.getWeaponName()));
							stats2.add(new JLabel("Armor: " + eq.getArmorName()));
						}
						
						//Add stats, parts 1 and 2
						controlPanel.add(stats1);
						controlPanel.add(stats2);
						
						//Add body health display
						if (u instanceof Human) {
							controlPanel.add(humanBodyPercentageHealthDiagram((Human)u));
						} else {
							//TODO
							controlPanel.add(new JLabel("Percentage HP diagram"));
						}
						
						//Add exit button
						JButton exit = new JButton("World Map");
						exit.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								battleground.exitBattleground();
								
								switchToWorldMap();
							}
						});
						controlPanel.add(exit);
						
						JButton examine = new JButton("Examine");
						examine.setPreferredSize(new Dimension(ORDERS_BUTTON_WIDTH, 500));
						examine.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								switchToPanel(new UnitStatsPanel(tile.getUnit(), backButton(getBattleMap())), VIEW);
							}
						});
						ordersPanel.add(examine);
						
						if (tile.getUnit().getAffiliation() == playerNation()) {
							JButton orders = getUnitBattlegroundOrders(tile.getUnit(), x, y);
							orders.setPreferredSize(new Dimension(ORDERS_BUTTON_WIDTH, 500));
							orders.addActionListener(new ActionListener() {
								@Override
								public void actionPerformed(ActionEvent e) {
									// TODO Auto-generated method stub
								}
							});
							ordersPanel.add(orders);
						} else {
							JButton viewMoves = new JButton("View Range");
							viewMoves.setPreferredSize(new Dimension(ORDERS_BUTTON_WIDTH, 500));
							viewMoves.addActionListener(new ActionListener() {
								@Override
								public void actionPerformed(ActionEvent e) {
									// TODO Auto-generated method stub
								}
							});
							ordersPanel.add(viewMoves);
						}
					}
					
					validate();
					repaint();
				}
			});
			return tileButton;
		}
		
		private void alterBattlegroundTileAppearance(JButton tileButton, int x, int y,
				boolean select, BattlegroundTile dest) {
			//TODO properly make appearance
			tileButton.setPreferredSize(new Dimension(40, 40));
			BattlegroundTile tile = battleground.getMap()[x][y];
			if (select) {
				tileButton.setBackground(Color.BLUE);
			}
			if (tile.getUnit() != null) {
				tileButton.setBackground(Color.CYAN);
			} else if (tile.getInanimateObjectOccupant() != null) {
				tileButton.setBackground(Color.YELLOW);
			}
			if (dest == tile) {
				tileButton.setBackground(Color.GRAY);
			}
			
		}
		
		private JButton getUnitBattlegroundOrders(final Unit u, final int x, final int y) {
			JButton ret = new JButton("Order");
			//TODO
			ret.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					final JPanel orders = ordersPanel();
					final JPanel map = mapPanel();
					final List<Component> possibleOrders = new List<>();
					final JButton cancel = new JButton("Cancel");
					cancel.setPreferredSize(new Dimension(ORDERS_BUTTON_WIDTH, 50));
					cancel.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							refresh();
						}
					});
					HashMap<BattlegroundTile, Integer> traversable = 
							GeneralGameplayManager.getTraversableBattlegroundTiles(battleground, u, x, y);
					map.removeAll();
					orders.removeAll();
					final int[] dimensions = battleground.getDimensions();
					orders.setLayout(new GridLayout(1, 1));
					orders.add(cancel);
					map.setLayout(new GridLayout(dimensions[0], dimensions[1]));
					for (int q = 0; q < dimensions[0]; q++) {
						for (int w = 0; w < dimensions[1]; w++) {
							JButton tile = new JButton();
							final int destX = q;
							final int destY = w;
							if (traversable.get(battleground.getMap()[q][w]) == null) {
								alterBattlegroundTileAppearance(tile, q, w, false, null);
							} else {
								alterBattlegroundTileAppearance(tile, q, w, true, null);
								tile.addActionListener(new ActionListener() {
									@Override
									public void actionPerformed(ActionEvent e) {
										final BattlegroundTile here = battleground.getMap()[destX][destY];
										//Orders panel should already have just the cancel button, as intended
										map.removeAll();
										map.setLayout(new GridLayout(dimensions[0], dimensions[1]));
										for (int q = 0; q < dimensions[0]; q++) {
											for (int w = 0; w < dimensions[1]; w++) {
												JButton tile = new JButton();
												alterBattlegroundTileAppearance(tile, q, w, false, here);
												map.add(tile);
											}
										}
										
										List<BattlegroundTile> adjacentEmpty = battleground.getAdjacentAvailableTiles(u, destX, destY);
										
										JButton standby = new JButton("Standby");
										standby.setPreferredSize(new Dimension(ORDERS_BUTTON_WIDTH, 100));
										standby.addActionListener(new ActionListener() {
											@Override
											public void actionPerformed(ActionEvent e) {
												battleground.moveUnit(u, new int[] {destX, destY},  battleground.getMap());
												refresh();
											}
										});
										possibleOrders.add(standby);
										
										if (u instanceof Equippable) {
											JButton items = new JButton("Inventory");
											items.addActionListener(new ActionListener() {
												@Override
												public void actionPerformed(ActionEvent e) {
													inventoryOptions(u, destX, destY, cancel);
												}
											});
											possibleOrders.add(items);
										}
										
										final HashMap<BattlegroundTile, Integer> enemyTiles =
												GeneralGameplayManager.getAttackableBattlegroundTilesFromDestination(
														battleground, u, destX, destY, null);
										if (!(enemyTiles.isEmpty())) {
											JButton attack = new JButton("Attack");
											attack.addActionListener(new ActionListener() {
												@Override
												public void actionPerformed(ActionEvent e) {
													attackPart1(destX, destY, enemyTiles, u, cancel);
												}
											});
											possibleOrders.add(attack);
										}
										
										//TODO staff options if has staff and can use
										
										if (here.getInanimateObjectOccupant() instanceof Lootable) {
											JButton steal = new JButton("Steal");
											steal.addActionListener(new ActionListener() {
												@Override
												public void actionPerformed(ActionEvent e) {
													// TODO Auto-generated method stub
												}
											});
											possibleOrders.add(steal);
										}
										
										List<BattlegroundTile> alliedTiles = battleground.getAlliedTiles(u, destX, destY);
										if (!(alliedTiles.isEmpty())) {
											JButton trade = new JButton("Trade");
											trade.addActionListener(new ActionListener() {
												@Override
												public void actionPerformed(ActionEvent e) {
													// TODO Auto-generated method stub
												}
											});
											possibleOrders.add(trade);
											
											if (u.getPassenger() != null) {
												List<BattlegroundTile> transferTiles = new List<>();
												for (int q = 0; q < alliedTiles.size(); q++) {
													if (alliedTiles.get(q).getUnit().canCarryUnit()) {
														transferTiles.add(alliedTiles.get(q));
													}
												}
												if (!(transferTiles.isEmpty())) {
													JButton transfer = new JButton("Transfer");
													transfer.addActionListener(new ActionListener() {
														@Override
														public void actionPerformed(ActionEvent e) {
															// TODO Auto-generated method stub
														}
													});
													possibleOrders.add(transfer);
												}
											} else {
												List<BattlegroundTile> pickupTiles = battleground.getAdjacentTilesWithCarriableAllies(u, destX, destY);
												if (!(pickupTiles.isEmpty())) {
													JButton rescue = new JButton("Rescue");
													rescue.addActionListener(new ActionListener() {
														@Override
														public void actionPerformed(ActionEvent e) {
															// TODO Auto-generated method stub
														}
													});
													possibleOrders.add(rescue);
												}
											}
										}
										
										if (u.getPassenger() != null && !adjacentEmpty.isEmpty()) {
											JButton drop = new JButton("Drop");
											drop.addActionListener(new ActionListener() {
												@Override
												public void actionPerformed(ActionEvent e) {
													// TODO Auto-generated method stub
												}
											});
											possibleOrders.add(drop);
										}
										
										if (u instanceof Equippable
												&& here.getInanimateObjectOccupant() instanceof StationaryWeapon
												&& ((Equippable)u).canUse((StationaryWeapon)here.getInanimateObjectOccupant())) {
											Equippable eq = (Equippable)u;
											StationaryWeapon weapon = (StationaryWeapon)here.getInanimateObjectOccupant();
											JButton artillery = new JButton("Artillery");
											artillery.addActionListener(new ActionListener() {
												@Override
												public void actionPerformed(ActionEvent e) {
													// TODO Auto-generated method stub
												}
											});
										}
										
										orders.removeAll();
										orders.setLayout(new GridLayout(possibleOrders.size() + 1, 1));
										for (int q = 0; q < possibleOrders.size(); q++) {
											orders.add(possibleOrders.get(q));
										}
										orders.add(cancel);
										// TODO Auto-generated method stub
										validate();
										repaint();
									}
								});
							}
							map.add(tile);
						}
					}
					validate();
					repaint();
				}
			});
			return ret;
		}
		
		private void inventoryOptions(final Unit u, final int x, final int y,
				final JButton cancel) {
			final Equippable eq = (Equippable)u;
			JPanel invDisplay = new JPanel(new GridLayout(eq.getInventory().length, 4));
			for (int q = 0; q < eq.getInventory().length; q++) {
				if (eq.getInventory()[q] != null) {
					final int idx = q;
					int[] itemArray = eq.getInventory()[q];
					final Item item = InventoryIndex.getElement(itemArray);
					invDisplay.add(new JLabel(item.getName()));
					
					String[] stats = item.getInformationDisplayArray(itemArray);
					JPanel statsPanel = new JPanel(new GridLayout(stats.length, 1));
					for (int w = 0; w < stats.length; w++) {
						statsPanel.add(new JLabel(stats[w]));
					}
					invDisplay.add(statsPanel);
					
					if (item instanceof HandheldWeapon) {
						JButton equip = new JButton("Equip");
						equip.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								eq.equip(idx);
								inventoryOptions(u, x, y, cancel);
							}
						});
						invDisplay.add(equip);
					} else if (item instanceof UsableItem) {
						JButton use = new JButton("Use");
						use.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								battleground.moveUnit(u, new int[] {x, y},  battleground.getMap());
								//TODO use item
							}
						});
						invDisplay.add(use);
					} else {
						invDisplay.add(new JPanel());
					}
					
					JButton drop = new JButton("Drop");
					drop.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							boolean decision = confirmChoiceDialog("Drop " + item.getName() + "?");
							if (decision) {
								eq.getInventory()[idx] = null;
								inventoryOptions(u, x, y, cancel);
							}
						}
					});
					invDisplay.add(drop);
				} else {
					invDisplay.add(new JPanel());
					invDisplay.add(new JPanel());
					invDisplay.add(new JPanel());
					invDisplay.add(new JPanel());
				}
			}
			ordersPanel.removeAll();
			ordersPanel.setLayout(new BorderLayout());
			ordersPanel.add(invDisplay);
			ordersPanel.add(cancel, BorderLayout.SOUTH);
			ordersPanel.validate();
			ordersPanel.repaint();
		}
		
		private void attackPart1(final int x, final int y,
				final HashMap<BattlegroundTile, Integer> attackable, final Unit u,
				final JButton cancel) {
			final BattlegroundTile here = battleground.getTileAtCoords(new int[] {x, y});
			int[] dimensions = battleground.getDimensions();
			mapPanel.removeAll();
			mapPanel.setLayout(new GridLayout(dimensions[0], dimensions[1]));
			for (int q = 0; q < dimensions[0]; q++) {
				for (int w = 0; w < dimensions[1]; w++) {
					final BattlegroundTile tile = battleground.getMap()[q][w];
					JButton tileButton = new JButton();
					if (attackable.get(tile) == null) {
						alterBattlegroundTileAppearance(tileButton, q, w, false, here);
					} else {
						alterBattlegroundTileAppearance(tileButton, q, w, true, here);
						tileButton.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								attackPart2(x, y, attackable, u, cancel, tile);
							}
						});
					}
					
					mapPanel.add(tileButton);
				}
			}
			ordersPanel.removeAll();
			ordersPanel.setLayout(new BorderLayout());
			ordersPanel.add(cancel);
			
			validate();
			repaint();
		}
		
		private void attackPart2(final int x, final int y,
				final HashMap<BattlegroundTile, Integer> attackable,
				final Unit u, final JButton cancel, final BattlegroundTile target) {
			final BattlegroundTile here = battleground.getTileAtCoords(new int[] {x, y});
			final boolean canCounter = attackable.get(target) <= 1
					|| (target.getUnit() instanceof Equippable
							&& ((Equippable)target.getUnit()).getEquippedWeapon() != null
							&& ((Equippable)target.getUnit()).getEquippedWeapon().maxRange() >= attackable.get(target));
			int[] dimensions = battleground.getDimensions();
			mapPanel.removeAll();
			mapPanel.setLayout(new GridLayout(dimensions[0], dimensions[1]));
			for (int q = 0; q < dimensions[0]; q++) {
				for (int w = 0; w < dimensions[1]; w++) {
					final BattlegroundTile tile = battleground.getMap()[q][w];
					JButton tileButton = new JButton();
					if (tile == target) {
						alterBattlegroundTileAppearance(tileButton, q, w, true, here);
					} else {
						alterBattlegroundTileAppearance(tileButton, q, w, false, here);
					}
					mapPanel.add(tileButton);
				}
			}
			//TODO orders
			
			final JPanel forecast = new JPanel();
			final String defaultForEquippable = "No Weapon --/--";
			final List<String> weaponNames = new List<>(3, 3);
			final List<Integer> weaponIndexes = new List<>(3, 3);
			int unequippedIndex = 0;
			if (u instanceof Equippable) {
				Equippable eq = (Equippable)u;
				List<Weapon> weapons = new List<>();
				for (int q = 0; q < eq.getInventory().length; q++) {
					if (eq.getInventory()[q] != null
							&& eq.getInventory()[q][0] == InventoryIndex.HANDHELD_WEAPON) {
						int[] wArray = eq.getInventory()[q];
						HandheldWeapon w = (HandheldWeapon)InventoryIndex.getElement(wArray);
						if (w.maxRange() >= attackable.get(target)) {
							weapons.add(w);
							weaponNames.add(String.format("%s %d/%d", w.getName(), wArray[2], w.getInitialUses()));
							weaponIndexes.add(q);
						}
					} else {
						unequippedIndex = q;
					}
				}
				if (attackable.get(target) <= 1
						&& weaponNames.size() < eq.getInventory().length) {
					weaponNames.add(0, defaultForEquippable);
					weaponIndexes.add(unequippedIndex);
				}
			} else {
				weaponNames.add("Default monster weapon");
				//TODO properly add monster weapon names
			}
			String[] wepNamesArray = new String[weaponNames.size()];
			for (int q = 0; q < weaponNames.size(); q++) {
				wepNamesArray[q] = weaponNames.get(q);
			}
			final JComboBox<String> weaponOptions = new JComboBox<>(wepNamesArray);
			if (wepNamesArray.length > 0 && wepNamesArray[0] == defaultForEquippable) {
				weaponOptions.setSelectedIndex(1);
			}
			
			String[] allPartsNames = target.getUnit().getBodyPartsNames();
			List<String> partsNames = new List<>(allPartsNames.length);
			final List<Integer> partsIndexes = new List<>(allPartsNames.length);
			for (int q = 0; q < allPartsNames.length; q++) {
				if (target.getUnit().getCurrentHPOfBodyPart(q) > 0) {
					partsNames.add(allPartsNames[q]);
					partsIndexes.add(q);
				}
			}
			String[] availablePartsNames = new String[partsNames.size()];
			for (int q = 0; q < partsNames.size(); q++) {
				availablePartsNames[q] = partsNames.get(q);
			}
			final JComboBox<String> partsOptions = new JComboBox<>(availablePartsNames);
			partsOptions.setSelectedIndex(1); //Always set to torso by default
			
			weaponOptions.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (u instanceof Equippable) {
						Equippable eq = (Equippable)u;
						if (weaponOptions.getSelectedItem() == defaultForEquippable) {
							for (int q = 0; q < eq.getInventory().length; q++) {
								if (eq.getInventory()[q] != null
										&& eq.getInventory()[q][0] != InventoryIndex.HANDHELD_WEAPON) {
									eq.equip(q);
									break;
								}
							}
						} else {
							//TODO figure out how to properly equip
//							((Equippable)u).equip(weaponIndexes.get(weaponOptions.getSelectedIndex()));
//							int temp = weaponIndexes.get(weaponOptions.getSelectedIndex());
//							weaponIndexes.set(weaponOptions.getSelectedIndex(), 0);
//							weaponIndexes.set(0, temp);
						}
					}
					showForecast(forecast, here, u, target, target.getUnit(),
							partsIndexes.get(partsOptions.getSelectedIndex()), canCounter);
					validate();
					repaint();
				}
			});
			
			partsOptions.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (u instanceof Equippable) {
						//TODO figure out how to properly equip
//						((Equippable)u).equip(weaponIndexes.get(weaponOptions.getSelectedIndex()));
//						int temp = weaponIndexes.get(weaponOptions.getSelectedIndex());
//						weaponIndexes.set(weaponOptions.getSelectedIndex(), 0);
//						weaponIndexes.set(0, temp);
					}
					showForecast(forecast, here, u, target, target.getUnit(),
							partsIndexes.get(partsOptions.getSelectedIndex()), canCounter);
					validate();
					repaint();
				}
			});
			
			JButton confirm = new JButton("Confirm");
			confirm.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					battleground.moveUnit(u, new int[] {x, y},  battleground.getMap());
					Unit atk = u;
					Unit dfd = target.getUnit();
					StandardBattleReport report = BattleManager.performBattleSequence(u, target.getUnit(),
							partsIndexes.get(partsOptions.getSelectedIndex()),
							here, target, battleground, canCounter);
					//TODO animate battle
					animateBattle(report, atk, dfd, Human.BODY_PARTS_STRINGS[partsIndexes.get(partsOptions.getSelectedIndex())]);

					refresh();
				}
			});
			JButton back = new JButton("Back");
			back.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					attackPart1(x, y, attackable, u, cancel);
				}
			});
			
			if (u instanceof Equippable) {
				//TODO figure out how to properly equip
//				((Equippable)u).equip(weaponIndexes.get(weaponOptions.getSelectedIndex()));
//				int temp = weaponIndexes.get(weaponOptions.getSelectedIndex());
//				weaponIndexes.set(weaponOptions.getSelectedIndex(), 0);
//				weaponIndexes.set(0, temp);
			}
			showForecast(forecast, here, u, target, target.getUnit(),
					partsIndexes.get(partsOptions.getSelectedIndex()), canCounter);

			
			JPanel optionsPanel = new JPanel(new GridLayout(4, 1));
			optionsPanel.add(partsOptions);
			optionsPanel.add(weaponOptions);
			optionsPanel.add(confirm);
			optionsPanel.add(back);
			
			ordersPanel.removeAll();
			ordersPanel.setLayout(new GridLayout(2, 1));
			ordersPanel.add(optionsPanel);
			ordersPanel.add(forecast);
			validate();
			repaint();
		}
		
		private void showForecast(JPanel forecastPanel, BattlegroundTile atkTile, Unit atk,
				BattlegroundTile dfdTile, Unit dfd, int bodyPart, boolean canCounter) {
			
			int[] report = BattleManager.normalBattleForecast(atk, dfd, bodyPart, atkTile, dfdTile, canCounter);
			forecastPanel.removeAll();
			forecastPanel.setLayout(new GridLayout(6, 3));
			
			forecastPanel.add(new JLabel(atk.getName()));
			forecastPanel.add(new JLabel("--Name--"));
			forecastPanel.add(new JLabel(dfd.getName()));
			
			if (atk instanceof Equippable) {
				forecastPanel.add(new JLabel(((Equippable)atk).getWeaponName()));
			} else {
				forecastPanel.add(new JLabel("Default Monster Weapon"));
			}
			forecastPanel.add(new JLabel("--Weapon--"));
			if (dfd instanceof Equippable) {
				forecastPanel.add(new JLabel(((Equippable)dfd).getWeaponName()));
			} else {
				forecastPanel.add(new JLabel("Default Monster Weapon"));
			}
			
			forecastPanel.add(new JLabel("" + report[0]));
			forecastPanel.add(new JLabel(String.format(
					"<html>--HP--<br/>"
					+ "<html>--Torso<br/>" //Defender always targets torso
					+ "<html>%s--<br/>",
					dfd.getBodyPartsNames()[bodyPart])));
			forecastPanel.add(new JLabel("" + report[5]));
			
			String atkDmg = "" + report[1];
			if (report[4] == 2) {
				atkDmg += " x 2";
			}
			forecastPanel.add(new JLabel(atkDmg));
			forecastPanel.add(new JLabel("--Damage--"));
			if (report[9] == -1) {
				forecastPanel.add(new JLabel("--"));
			} else {
				String dfdDmg = "" + report[6];
				if (report[9] == 2) {
					dfdDmg += " x 2";
				}
				forecastPanel.add(new JLabel(dfdDmg));
			}
			
			forecastPanel.add(new JLabel(report[2] + "%"));
			forecastPanel.add(new JLabel("--Hit Rate--"));
			if (report[9] == -1) {
				forecastPanel.add(new JLabel("--"));
			} else {
				forecastPanel.add(new JLabel(report[7] + "%"));
			}
			
			forecastPanel.add(new JLabel(report[3] + "%"));
			forecastPanel.add(new JLabel("--Critical Rate--"));
			if (report[9] == -1) {
				forecastPanel.add(new JLabel("--"));
			} else {
				forecastPanel.add(new JLabel(report[8] + "%"));
			}
		}
		
		private void animateBattle(final StandardBattleReport report, Unit atk, Unit dfd, String part) {
//			StandardBattleReport report = BattleManager.performBattleSequence(u, target.getUnit(),
//					partsIndexes.get(partsOptions.getSelectedIndex()),
//					here, target, battleground, canCounter);
			
			JPanel display = new JPanel(new GridLayout(12, 1));
			display.add(new JLabel("Eventually, this will be an actual animation"));
			int[] seq = report.getDetails();
			StringBuilder sb = new StringBuilder(atk.getName());
			if (seq[0] == 0) {
				sb.append(" missed ");
			} else if (seq[0] == 1) {
				sb.append(" hit ");
			} else if (seq[0] == 2) {
				sb.append(" crit ");
			}
			sb.append(dfd.getName() + ", reducing their HP from ");
			sb.append(seq[1] + " to " + seq[2] + ".");
			display.add(new JLabel(sb.toString()));
			sb = new StringBuilder(dfd.getName());
			if (seq[3] == -1) {
				sb.append(" could not counter attack.");
				display.add(new JLabel(sb.toString()));
				sb = new StringBuilder();
			} else {
				if (seq[3] == 0) {
					sb.append(" missed ");
				} else if (seq[3] == 1) {
					sb.append(" hit ");
				} else if (seq[3] == 2) {
					sb.append(" crit ");
				}
				sb.append(atk.getName() + ", reducing their torso HP from ");
				sb.append(seq[4] + " to " + seq[5] + ".");
				display.add(new JLabel(sb.toString()));
				sb = new StringBuilder();
				if (seq[6] == 0) {
					sb.append("There was no double attack.");
					display.add(new JLabel(sb.toString()));
					sb = new StringBuilder();
				} else {
					Unit d = null;
					Unit f = null;
					if (seq[6] == 1) {
						sb.append(atk.getName() + " double attacks.");
						d = atk;
						f = dfd;
					} else if (seq[6] == 2) {
						sb.append(dfd.getName() + " double attacks.");
						d = dfd;
						f = atk;
					}
					display.add(new JLabel(sb.toString()));
					sb = new StringBuilder(d.getName());
					if (seq[7] == 0) {
						sb.append(" missed ");
					} else if (seq[7] == 1) {
						sb.append(" hit ");
					} else if (seq[7] == 2) {
						sb.append(" crit ");
					}
					sb.append(f.getName() + ", reducing their HP from ");
					sb.append(seq[8] + " to " + seq[9] + ".");
					display.add(new JLabel(sb.toString()));
				}
			}
			if (!atk.isAlive()) {
				display.add(new JLabel(atk.getName() + " died"));
			} else if (!dfd.isAlive()) {
				display.add(new JLabel(dfd.getName() + " died"));
			}
			if (report.getDeadSpeaker() != null) {
				display.add(new JLabel(report.getDeadSpeaker().getName() + ": " + report.getDeathQuote()));
			}
			if (report.getDeadSpeakerSupportPartner() != null) {
				display.add(new JLabel(report.getDespairQuote()));
				display.add(new JLabel(report.getDeadSpeakerSupportPartner().getName() + ": " + report.getDeathQuote()));
			}
			if (report.isAtkGainedLevel()) {
				display.add(new JLabel(atk.getName() + " gained a level"));
			}
			if (report.isDfdGainedLevel()) {
				display.add(new JLabel(dfd.getName() + " gained a level"));
			}
			
			JButton back = new JButton("BACK");
			final JPanel ref = this;
			back.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					refresh();
					switchToPanel(ref, VIEW);
					if (report.shouldEndBattle()) {
						JOptionPane.showMessageDialog(null, "The battle is ended");
						battleground.endBattle();
						switchToWorldMap();
					}
				}
			});
			display.add(back);
			switchToPanel(display, VIEW);
		}
		
	}
	
	private class NationStatsPanel extends JPanel {
		public NationStatsPanel(final Nation n) {
			setLayout(new BorderLayout());
			JPanel name = new JPanel();
			name.add(new JLabel(n.getFullName() + " (Population: " + n.getPopulation() + "K)"));
			
			JPanel allCitiesDisplay = new JPanel(new GridLayout(n.getCityStates().size(), 1));
			for (int q = 0; q < n.getCityStates().size(); q++) {
				CityState cs = n.getCityStates().get(q);
				JPanel cityDisplay = new JPanel(new GridLayout(1, 2));
				JPanel cityName = new JPanel(new BorderLayout());
				cityName.add(new JLabel("<html>" + cs.getName() + "<br/><html>Population: "
						+ cs.getPopulation() + "K<br/>"));
				JPanel cityStats = new JPanel(new GridLayout(3, 2));
				cityStats.add(new JLabel("Altruism: " + cs.getAltruism()));
				cityStats.add(new JLabel("Familism: " + cs.getFamilism()));
				cityStats.add(new JLabel("Militarism: " + cs.getMilitarism()));
				cityStats.add(new JLabel("Nationalism: " + cs.getNationalism()));
				cityStats.add(new JLabel("Independence: " + cs.getConfidence()));
				cityStats.add(new JLabel("Compliance: " + cs.getTolerance()));
				cityDisplay.add(cityName);
				cityDisplay.add(cityStats);
				allCitiesDisplay.add(cityDisplay);
			}
			
			JPanel militaryStats = new JPanel(new GridLayout(7, 2));
			int[] power = n.getPower();
			militaryStats.add(new JLabel("Military Stats"));
			JButton goToStatsPage = new JButton("View All Combatants");
			goToStatsPage.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					switchToPanel(new ArmyStatsPanel(n), VIEW);
				}
			});
			militaryStats.add(goToStatsPage);
			militaryStats.add(new JLabel("Size: " + n.getArmy().size()));
			militaryStats.add(new JLabel("Total Physical Might: " + power[0]));
			militaryStats.add(new JLabel("Total Magical Might: " + power[1]));
			militaryStats.add(new JLabel("Total Accuracy: " + power[2]));
			militaryStats.add(new JLabel("Total Avoidance: " + power[3]));
			militaryStats.add(new JLabel("Total Critical Rate: " + power[4]));
			militaryStats.add(new JLabel("Total Critical Security: " + power[5]));
			militaryStats.add(new JLabel("Average Attack Speed: " + power[6]));
			militaryStats.add(new JLabel("Total Defense: " + power[7]));
			militaryStats.add(new JLabel("Total Resistance: " + power[8]));
			militaryStats.add(new JLabel("Total Head HP: " + power[9]));
			militaryStats.add(new JLabel("Total Torso HP: " + power[10]));
			
			JPanel ruler = new JPanel(new BorderLayout());
			if (n.getRuler() == null) {
				ruler.add(new JLabel("Ruler: None"));
			} else {
				ruler.add(new JLabel("Ruler: " + n.getRuler().getDisplayName()), BorderLayout.NORTH);
				JButton rulerStats = new JButton("Go To Page");
				rulerStats.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						switchToPanel(new UnitStatsPanel(n.getRuler()), VIEW);
					}
				});
				ruler.add(rulerStats, BorderLayout.SOUTH);
				ruler.add(makePortrait(n.getRuler()));
			}
			
			JPanel relations = new JPanel(new GridLayout(10, 1));
			//TODO figure out how this should work (by figuring out if relations
			//are stored in a list or a map)
			
			JButton history = new JButton("View History");
			history.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					switchToPanel(new HistoryPanel(n), VIEW);
				}
			});
			
			JPanel fullDisplay = new JPanel(new GridLayout(3, 2));
			fullDisplay.add(name);
			fullDisplay.add(ruler);
			fullDisplay.add(new JScrollPane(allCitiesDisplay));
			fullDisplay.add(relations);
			fullDisplay.add(militaryStats);
			fullDisplay.add(history);
			
			add(new JScrollPane(fullDisplay));
			add(returnToWorldMap("WORLD MAP"), BorderLayout.SOUTH);
		}
		public NationStatsPanel(Nation n, JButton backButton) {
			this(n);
			add(backButton, BorderLayout.SOUTH);
		}
	}
	private class ArmyStatsPanel extends JPanel {
		public ArmyStatsPanel(final Nation n) {
			//TODO
			setLayout(new BorderLayout());
			JPanel stats = new JPanel(new GridLayout(6, 2));
			int[] power = n.getPower();
			stats.add(new JLabel("Size: " + n.getArmy().size()));
			stats.add(new JLabel("Total Physical Might: " + power[0]));
			stats.add(new JLabel("Total Magical Might: " + power[1]));
			stats.add(new JLabel("Total Accuracy: " + power[2]));
			stats.add(new JLabel("Total Avoidance: " + power[3]));
			stats.add(new JLabel("Total Critical Hit Rate: " + power[4]));
			stats.add(new JLabel("Total Critical Security: " + power[5]));
			stats.add(new JLabel("Average Attack Speed: " + power[6]));
			stats.add(new JLabel("Total Defense: " + power[7]));
			stats.add(new JLabel("Total Resistance: " + power[8]));
			stats.add(new JLabel("Total Head HP: " + power[9]));
			stats.add(new JLabel("Total Torso HP: " + power[10]));
			JPanel statsDisplay = new JPanel(new BorderLayout());
			statsDisplay.add(stats);
			statsDisplay.add(new JLabel("Military Stats"), BorderLayout.NORTH);
			
			final JPanel unitPreview = new JPanel();
			
			int y = Math.max(n.getArmy().size() / 4, 4);
			if (n.getArmy().size() % 4 != 0) {
				y++;
			}
			JPanel unitList = new JPanel(new GridLayout(y, 4));
			for (int q = 0; q < n.getArmy().size(); q++) {
				final Unit u = n.getArmy().get(q);
				JPanel portraitAndStats = unitPortraitAndStats(u);
				JButton preview = new JButton("View");
				preview.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						unitPreview.removeAll();
						unitPreview.setLayout(new BorderLayout());
						unitPreview.add(unitPreview(u));
						JButton toPage = new JButton("View All Stats");
						toPage.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								JButton back = new JButton("BACK");
								back.addActionListener(new ActionListener() {
									@Override
									public void actionPerformed(ActionEvent e) {
										switchToPanel(new ArmyStatsPanel(u.getAffiliation()), VIEW);
									}
								});
								switchToPanel(new UnitStatsPanel(u, back), VIEW);
							}
						});
						unitPreview.add(toPage, BorderLayout.SOUTH);
						unitPreview.validate();
						unitPreview.repaint();
					}
				});
				
				JPanel unitDisplay = new JPanel(new BorderLayout());
				unitDisplay.add(portraitAndStats);
				unitDisplay.add(preview, BorderLayout.SOUTH);
				
				unitList.add(unitDisplay);
			}
			
			int fill = (y * 4) - n.getArmy().size();
			for (int q = 0; q < fill; q++) {
				//Make sure it's even
				unitList.add(new JPanel());
			}
			
			JPanel left = new JPanel(new GridLayout(2, 1));
			left.add(statsDisplay);
			left.add(unitPreview);
			
			JPanel right = new JPanel(new BorderLayout());
			right.add(new JScrollPane(unitList));
			
			JPanel fullDisplay = new JPanel(new GridLayout(1, 2));
			fullDisplay.add(left);
			fullDisplay.add(right);
			
			JButton toNation = new JButton("Back to Nation Stats");
			toNation.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					switchToPanel(new NationStatsPanel(n), VIEW);
				}
			});
			
			add(fullDisplay);
			add(toNation, BorderLayout.SOUTH);
		}
	}
	private JPanel unitPortraitAndStats(Unit u) {
		JPanel ret = new JPanel(new GridLayout(1, 2));
		ret.add(makePortrait(u));
		JPanel basicInfo = new JPanel(new GridLayout(3, 1));
		basicInfo.add(new JLabel(u.getDisplayName()));
		basicInfo.add(new JLabel("Class: " + u.getUnitClassName()));
		basicInfo.add(new JLabel(String.format("LVL: %d EXP: %d", u.getLevel(), u.getExperience())));
		ret.add(basicInfo);
		return ret;
	}
	private class UnitStatsPanel extends JPanel {
		public UnitStatsPanel(final Unit u) {
			setLayout(new BorderLayout());
			
			JPanel portrait = makePortrait(u);
			JPanel basicInfo = new JPanel(new GridLayout(4, 2));
			basicInfo.add(new JLabel(u.getDisplayName()));
			basicInfo.add(new JLabel(String.format("ATK: %3d (Max Range: %d)", u.attackStrength(), u.getRanges()[1])));
			if (u.getUnitClass() == null) {
				basicInfo.add(new JLabel("No Class Assigned"));
			} else {
				basicInfo.add(new JLabel(String.format("%s (Tier %d)", u.getUnitClassName(), u.getUnitClass().getTier())));
			}
			basicInfo.add(new JLabel(String.format("ACC: %3d", u.accuracy())));
			basicInfo.add(new JLabel(String.format("LVL: %d EXP: %d", u.getLevel(), u.getExperience())));
			basicInfo.add(new JLabel(String.format("AVO: %3d AS: %d", u.avoidance(Unit.TORSO), u.attackSpeed())));
			JButton locate = new JButton("Locate");
			locate.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
				}
			});
			basicInfo.add(locate);
			//SEC is short for security
			basicInfo.add(new JLabel(String.format("CRT: %3d SEC %3d", u.criticalHitRate(), u.criticalHitAvoid())));
			
			JPanel statsDisplay = attributesDisplay(u);
			
			JPanel body = new JPanel(new GridLayout(1, 2));
			if (u instanceof Human) {
				makeHumanBodyPanel(body, (Human)u);
			} else {
				//TODO
			}
			JPanel misc = new JPanel(new GridLayout(14, 3));
			misc.add(new JLabel("National Affiliation:"));
			misc.add(new JLabel(u.getAffiliation().getFullName()));
			JButton viewNation = new JButton("View Nation");
			viewNation.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					switchToPanel(new NationStatsPanel(u.getAffiliation()), VIEW);
				}
			});
			misc.add(viewNation);
			misc.add(new JLabel("Team Leader:"));
			if (u.getGroup() == null) {
				misc.add(new JLabel("N/A"));
				misc.add(new JPanel());
			} else {
				misc.add(new JLabel(u.getGroup().getLeader().getDisplayName()));
				JButton viewGroup = new JButton("View Team");
				viewGroup.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						switchToPanel(new UnitGroupStatsPanel(u.getGroup()), VIEW);
					}
				});
				misc.add(viewGroup);
			}
			if (u instanceof Human) {
				Human h = (Human)u;
				misc.add(new JLabel("Support Partner:"));
				if (h.getSupportPartner() == null) {
					misc.add(new JLabel("None"));
					misc.add(new JPanel());
				} else {
					if (h.getSupportPartner() == player()) {
						misc.add(new JLabel(player().getDisplayName()
								+ " (" + h.getRelationshipWithPlayer() + ")"));
					} else {
						misc.add(new JLabel(h.getSupportPartner().getDisplayName()
								+ " (" + h.getRelationshipWithSupportPartner() + ")"));
					}
					JButton viewPartner = new JButton("View Character");
					viewPartner.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							switchToPanel(new UnitStatsPanel(((Human)u).getSupportPartner()), VIEW);
						}
					});
					misc.add(viewPartner);
				}
				misc.add(new JLabel("Relationship with Player:"));
				if (h == player()) {
					misc.add(new JLabel("--"));
					misc.add(new JPanel());
				} else {
					misc.add(new JLabel("" + h.getRelationshipWithPlayer()));
					JButton interact = new JButton("Interact");
					interact.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							// TODO interaction page
						}
					});
					misc.add(interact);
				}
			} else {
				Monster m = (Monster)u;
				misc.add(new JLabel("Master:"));
				misc.add(new JLabel(m.getMaster().getDisplayName()));
				JButton viewMaster = new JButton("View Character");
				viewMaster.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						switchToPanel(new UnitStatsPanel(((Monster)u).getMaster()), VIEW);
					}
				});
				misc.add(viewMaster);
				misc.add(new JPanel());
				misc.add(new JPanel());
				misc.add(new JPanel());
			}
			misc.add(new JLabel("Carrying:"));
			if (u.getPassenger() == null) {
				misc.add(new JLabel("None"));
				misc.add(new JPanel());
			} else {
				misc.add(new JLabel(u.getPassenger().getDisplayName()));
				JButton examine = new JButton("Examine");
				if (u.getPassenger() instanceof Unit) {
					examine.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							switchToPanel(new UnitStatsPanel((Unit)u.getPassenger()), VIEW);
						}
					});
				} else {
					//TODO
				}
				misc.add(examine);
			}
			if (u instanceof Human) {
				Human h = (Human)u;
				misc.add(new JLabel("Gender:"));
				misc.add(new JLabel(h.getGenderAsString()));
				misc.add(new JPanel());
				if (h.isMortal()) {
					misc.add(new JLabel("Age:"));
					misc.add(new JLabel("" + h.getAge()));
					misc.add(new JLabel("Mortal"));
				} else {
					misc.add(new JLabel("Apparent Age:"));
					misc.add(new JLabel("" + h.getAge()));
					misc.add(new JLabel("Immortal"));
				}
			} else {
				misc.add(new JPanel());
				misc.add(new JPanel());
				misc.add(new JPanel());
				misc.add(new JPanel());
				misc.add(new JPanel());
				misc.add(new JPanel());
			}
			misc.add(new JLabel("Morale:"));
			misc.add(new JLabel("" + u.getMorale()));
			misc.add(new JPanel());
			if (u instanceof Human) {
				Human h = (Human)u;
				misc.add(new JLabel("Supported Trait:"));
				misc.add(new JLabel(h.getValuedTraitAsString()));
				misc.add(new JPanel());
				misc.add(new JLabel("Demeanor:"));
				misc.add(new JLabel(h.getDemeanorAsString()));
				misc.add(new JPanel());
			} else {
				misc.add(new JPanel());
				misc.add(new JPanel());
				misc.add(new JPanel());
				misc.add(new JPanel());
				misc.add(new JPanel());
				misc.add(new JPanel());
			}
			misc.add(new JLabel("Wars:"));
			misc.add(new JLabel("" + u.getWars()));
			misc.add(new JPanel());
			misc.add(new JLabel("Battles:"));
			misc.add(new JLabel("" + u.getBattles()));
			misc.add(new JPanel());
			misc.add(new JLabel("Kills:"));
			misc.add(new JLabel("" + u.getKills()));
			misc.add(new JPanel());
			misc.add(new JLabel("Important Character?"));
			final JLabel tellImportance = new JLabel();
			if (u.isImportant()) {
				tellImportance.setText("Yes");
			} else {
				tellImportance.setText("No");
			}
			misc.add(tellImportance);
			if (u == player()) {
				misc.add(new JLabel("Locked"));
			} else {
				JButton toggleImportance = new JButton("Toggle");
				toggleImportance.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						u.toggleImportance();
						if (u.isImportant()) {
							tellImportance.setText("Yes");
						} else {
							tellImportance.setText("No");
						}
						tellImportance.validate();
						tellImportance.repaint();
					}
				});
				misc.add(toggleImportance);
			}
			
			JPanel weapons;
			if (u instanceof Equippable) {
				weapons = proficienciesDisplay((Equippable)u);
			} else {
				weapons = null;
				//TODO
			}
			
			JPanel fullDisplay = new JPanel(new GridLayout(3, 2));
			fullDisplay.add(portrait);
			fullDisplay.add(basicInfo);
			fullDisplay.add(statsDisplay);
			fullDisplay.add(body);
			fullDisplay.add(misc);
			fullDisplay.add(weapons);
			
			add(new JScrollPane(fullDisplay));
			add(returnToWorldMap("WORLD MAP"), BorderLayout.SOUTH);
		}
		
		public UnitStatsPanel(Unit u, JButton backButton) {
			this(u);
			add(backButton, BorderLayout.SOUTH);
		}
	}
	private static JPanel proficienciesDisplay(Equippable p) {
		JPanel weapons = new JPanel(new GridLayout(10, 1)); //Inventory at bottom
		weapons.add(new JLabel("Sword: " + p.proficiencyWith(Weapon.SWORD)));
		weapons.add(new JLabel("Spear: " + p.proficiencyWith(Weapon.LANCE)));
		weapons.add(new JLabel("Axe: " + p.proficiencyWith(Weapon.AXE)));
		weapons.add(new JLabel("Bow: " + p.proficiencyWith(Weapon.BOW)));
		weapons.add(new JLabel("Knife: " + p.proficiencyWith(Weapon.KNIFE)));
		weapons.add(new JLabel("Ballista: " + p.proficiencyWith(Weapon.BALLISTA)));
		weapons.add(new JLabel("Earth: " + p.proficiencyWith(Weapon.ANIMA)));
		weapons.add(new JLabel("Light: " + p.proficiencyWith(Weapon.LIGHT)));
		weapons.add(new JLabel("Dark: " + p.proficiencyWith(Weapon.DARK)));
		weapons.add(new JLabel("Staff: " + p.proficiencyWith(Weapon.STAFF)));
		int[][] inv = p.getInventory();
		JPanel inventory = new JPanel(new GridLayout(1, inv.length));
		for (int q = 0; q < inv.length; q++) {
			final Item i = InventoryIndex.getElement(inv[q]);
			if (i == null) {
				inventory.add(new JPanel());
			} else {
				JButton examine = new JButton(i.getName());
				examine.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						JOptionPane.showMessageDialog(null, "Here will be a description of " + i.getName());
					}
				});
				inventory.add(examine);
			}
		}
		JPanel fullDisplay = new JPanel(new BorderLayout());
		fullDisplay.add(weapons);
		fullDisplay.add(inventory, BorderLayout.SOUTH);
		return fullDisplay;
	}
	private static JPanel attributesDisplay(Unit u) {
		JPanel stats = new JPanel(new GridLayout(7, 3));
		JButton magInfo = new JButton("MAG");
		magInfo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "Magic: Used for magic-based attacks and other things");
			}
		});
		stats.add(magInfo);
		stats.add(new JLabel("" + u.getMagic()));
		stats.add(new JLabel(u.getMagicGrowth() + "%"));
		JButton sklInfo = new JButton("SKL");
		sklInfo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "Skill: Used for accuracy and critical hit rate");
			}
		});
		stats.add(sklInfo);
		stats.add(new JLabel("" + u.getSkill()));
		stats.add(new JLabel(u.getSkillGrowth() + "%"));
		JButton rfxInfo = new JButton("RFX");
		rfxInfo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "Reflex: Used for avoidance and attack speed");
			}
		});
		stats.add(rfxInfo);
		stats.add(new JLabel("" + u.getReflex()));
		stats.add(new JLabel(u.getReflexGrowth() + "%"));
		JButton awrInfo = new JButton("AWR");
		awrInfo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "Awareness: Used for accuracy, avoidance, and security");
			}
		});
		stats.add(awrInfo);
		stats.add(new JLabel("" + u.getAwareness()));
		stats.add(new JLabel(u.getAwarenessGrowth() + "%"));
		JButton resInfo = new JButton("RES");
		resInfo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "Resistance: Used for defense against magic");
			}
		});
		stats.add(resInfo);
		stats.add(new JLabel("" + u.getResistance()));
		stats.add(new JLabel(u.getResistanceGrowth() + "%"));
		JButton movInfo = new JButton("MOV");
		movInfo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "Movement: Moveable distance in one turn");
			}
		});
		stats.add(movInfo);
		stats.add(new JLabel("" + u.getMovement()));
		stats.add(new JPanel());
		JButton ldrInfo = new JButton("LDR");
		ldrInfo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "Leadership: Gives a bonus to character's team if the character is the team leader");
			}
		});
		stats.add(ldrInfo);
		stats.add(new JLabel("" + u.getLeadership()));
		stats.add(new JPanel());
		JPanel statsHeader = new JPanel(new GridLayout(1, 3));
		statsHeader.add(new JLabel("Stat"));
		statsHeader.add(new JLabel("Current Value"));
		statsHeader.add(new JLabel("Growth Rate"));
		JPanel statsDisplay = new JPanel(new BorderLayout());
		statsDisplay.add(statsHeader, BorderLayout.NORTH);
		statsDisplay.add(stats);
		
		return statsDisplay;
	}
	private static JPanel humanHPsDisplay(Human h) {
		JPanel bodyStats = new JPanel(new GridLayout(10, 3));
		JButton head = new JButton("Head");
		head.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "Affects accuracy. Character will die if HP reaches 0");
			}
		});
		bodyStats.add(head);
		bodyStats.add(new JLabel(String.format("%d/%d (Armor: %d)",
				h.getCurrentHPOfBodyPart(Human.HEAD),
				h.getMaximumHPOfBodyPart(Human.HEAD),
				h.defense(false, Human.HEAD))));
		bodyStats.add(new JLabel(h.getGrowthRateOfBodyPart(Human.HEAD) + "%"));
		JButton torso = new JButton("Torso");
		torso.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "Center of the body. Character will die if HP reaches 0");
			}
		});
		bodyStats.add(torso);
		bodyStats.add(new JLabel(String.format("%d/%d (Armor: %d)",
				h.getCurrentHPOfBodyPart(Human.TORSO),
				h.getMaximumHPOfBodyPart(Human.TORSO),
				h.defense(false, Human.TORSO))));
		bodyStats.add(new JLabel(h.getGrowthRateOfBodyPart(Human.TORSO) + "%"));
		JButton arm1 = new JButton("Right Arm");
		arm1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "Used for physical strength and accuracy");
			}
		});
		bodyStats.add(arm1);
		bodyStats.add(new JLabel(String.format("%d/%d (Armor: %d)",
				h.getCurrentHPOfBodyPart(Human.RIGHT_ARM),
				h.getMaximumHPOfBodyPart(Human.RIGHT_ARM),
				h.defense(false, Human.RIGHT_ARM))));
		bodyStats.add(new JLabel(h.getGrowthRateOfBodyPart(Human.RIGHT_ARM) + "%"));
		JButton arm2 = new JButton("Left Arm");
		arm2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "Used for physical strength and accuracy");
			}
		});
		bodyStats.add(arm2);
		bodyStats.add(new JLabel(String.format("%d/%d (Armor: %d)",
				h.getCurrentHPOfBodyPart(Human.LEFT_ARM),
				h.getMaximumHPOfBodyPart(Human.LEFT_ARM),
				h.defense(false, Human.LEFT_ARM))));
		bodyStats.add(new JLabel(h.getGrowthRateOfBodyPart(Human.LEFT_ARM) + "%"));
		JButton leg1 = new JButton("Right Leg");
		leg1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "Affects movement for unmounted characters");
			}
		});
		bodyStats.add(leg1);
		bodyStats.add(new JLabel(String.format("%d/%d (Armor: %d)",
				h.getCurrentHPOfBodyPart(Human.RIGHT_LEG),
				h.getMaximumHPOfBodyPart(Human.RIGHT_LEG),
				h.defense(false, Human.RIGHT_LEG))));
		bodyStats.add(new JLabel(h.getGrowthRateOfBodyPart(Human.RIGHT_LEG) + "%"));
		JButton leg2 = new JButton("Left Leg");
		leg2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "Affects movement for unmounted characters");
			}
		});
		bodyStats.add(leg2);
		bodyStats.add(new JLabel(String.format("%d/%d (Armor: %d)",
				h.getCurrentHPOfBodyPart(Human.LEFT_LEG),
				h.getMaximumHPOfBodyPart(Human.LEFT_LEG),
				h.defense(false, Human.LEFT_LEG))));
		bodyStats.add(new JLabel(h.getGrowthRateOfBodyPart(Human.LEFT_LEG) + "%"));
		JButton eye1 = new JButton("Right Eye");
		eye1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "Affects accuracy and avoidance.");
			}
		});
		bodyStats.add(eye1);
		bodyStats.add(new JLabel(String.format("%d/%d (Armor: %d)",
				h.getCurrentHPOfBodyPart(Human.RIGHT_EYE),
				h.getMaximumHPOfBodyPart(Human.RIGHT_EYE),
				h.defense(false, Human.RIGHT_EYE))));
		bodyStats.add(new JPanel());
		JButton eye2 = new JButton("Left Eye");
		eye2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "Affects accuracy and avoidance.");
			}
		});
		bodyStats.add(eye2);
		bodyStats.add(new JLabel(String.format("%d/%d (Armor: %d)",
				h.getCurrentHPOfBodyPart(Human.LEFT_EYE),
				h.getMaximumHPOfBodyPart(Human.LEFT_EYE),
				h.defense(false, Human.LEFT_EYE))));
		bodyStats.add(new JPanel());
		if (h.getCurrentHPOfBodyPart(Human.MOUNT) > 0) {
			JButton mount = new JButton("Mount (" + h.getUnitClass().getMount().getDisplayName() + ")");
			mount.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					JOptionPane.showMessageDialog(null, "Grants extra movement and avoidance. Mount will die if HP reaches 0");
				}
			});
			bodyStats.add(mount);
			bodyStats.add(new JLabel(String.format("%d/%d (Armor: %d)",
					h.getCurrentHPOfBodyPart(Human.MOUNT),
					h.getMaximumHPOfBodyPart(Human.MOUNT),
					h.defense(false, Human.MOUNT))));
			bodyStats.add(new JLabel("" + h.getGrowthRateOfBodyPart(Human.MOUNT) + "%"));
		} else {
			bodyStats.add(new JPanel());
			bodyStats.add(new JPanel());
			bodyStats.add(new JPanel());
		}
		final Armor a = h.getArmor();
		if (a != null) {
			bodyStats.add(new JLabel("Wearing:"));
			bodyStats.add(new JLabel(a.getName()));
			JButton examine = new JButton("Examine");
			examine.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					JOptionPane.showMessageDialog(null, "Here will be a description of " + a.getName());
				}
			});
			bodyStats.add(examine);
		}
		
		JPanel header = new JPanel(new GridLayout(1, 3));
		header.add(new JLabel("Part"));
		header.add(new JLabel("Health"));
		header.add(new JLabel("Growth Rate"));
		
		JPanel fullBodyStatsDisplay = new JPanel(new BorderLayout());
		fullBodyStatsDisplay.add(header, BorderLayout.NORTH);
		fullBodyStatsDisplay.add(bodyStats);
		
		return fullBodyStatsDisplay;
	}
	private static void makeHumanBodyPanel(JPanel body, Human h) {
		JPanel fullBodyStatsDisplay = humanHPsDisplay(h);
		body.add(humanBodyPercentageHealthDiagram(h));
		body.add(fullBodyStatsDisplay);
	}
	
	private static JPanel humanBodyPercentageHealthDiagram(Human h) {
		JPanel diagram = new JPanel(new GridLayout(3, 1));
		JPanel head = unitBodyDiagramComponent(h, Human.HEAD);
		JPanel torso = unitBodyDiagramComponent(h, Human.TORSO);
		JPanel rightArm = unitBodyDiagramComponent(h, Human.RIGHT_ARM);
		JPanel leftArm = unitBodyDiagramComponent(h, Human.LEFT_ARM);
		JPanel rightLeg = unitBodyDiagramComponent(h, Human.RIGHT_LEG);
		JPanel leftLeg = unitBodyDiagramComponent(h, Human.LEFT_LEG);
		JPanel rightEye = unitBodyDiagramComponent(h, Human.RIGHT_EYE);
		JPanel leftEye = unitBodyDiagramComponent(h, Human.LEFT_EYE);
		
		JPanel top = new JPanel(new GridLayout(1, 3));
		top.add(rightEye);
		top.add(head);
		top.add(leftEye);
		JPanel middle = new JPanel(new GridLayout(1, 3));
		middle.add(rightArm);
		middle.add(torso);
		middle.add(leftArm);
		JPanel bottom = new JPanel(new GridLayout(1, 2));
		bottom.add(rightLeg);
		bottom.add(leftLeg);
		
		diagram.add(top);
		diagram.add(middle);
		diagram.add(bottom);
		
		return diagram;
	}
	
	private static JPanel unitBodyDiagramComponent(Unit h, int part) {
		JPanel ret = new JPanel();
		double percentHealth = h.percentHealthOfPart(part);
		if (percentHealth == -1) {
			ret.setBackground(Color.BLACK);
		} else {
			int red = Math.max(0, Math.min(255, (int)Math.round((1 - percentHealth) * 255)));
			int green = Math.max(0, Math.min(255, (int)Math.round(percentHealth * 255)));
			ret.setBackground(new Color(red, green, 0));
		}
		
		ret.setLayout(new BorderLayout());
		ret.add(new JLabel(h.getBodyPartsNames()[part] + ": " + h.getCurrentHPOfBodyPart(part) + "/" + h.getMaximumHPOfBodyPart(part)));
		
		return ret;
	}
	
	private static JPanel makePortrait(Unit u) {
		//TODO temporary method since artwork is not made yet
		JPanel ret = new JPanel(new BorderLayout());
		if (u instanceof Human) {
			Human h = (Human) u;
			
			int hairRed = (int) Math.round(h.getHairColor().getRed() * 255);
			int hairGreen =  (int) Math.round(h.getHairColor().getGreen() * 255);
			int hairBlue =  (int) Math.round(h.getHairColor().getBlue() * 255);
			int skinRed = (int) Math.round(h.getSkinColor().getRed() * 255);
			int skinGreen =  (int) Math.round(h.getSkinColor().getGreen() * 255);
			int skinBlue =  (int) Math.round(h.getSkinColor().getBlue() * 255);
			int eyeRed = (int) Math.round(h.getEyeColor().getRed() * 255);
			int eyeGreen =  (int) Math.round(h.getEyeColor().getGreen() * 255);
			int eyeBlue =  (int) Math.round(h.getEyeColor().getBlue() * 255);
			Color hairColor = new Color(hairRed, hairGreen, hairBlue);
			Color skinColor = new Color(skinRed, skinGreen, skinBlue);
			Color eyeColor = new Color(eyeRed, eyeGreen, eyeBlue);

			JPanel hair = new JPanel();
			JPanel face = new JPanel(new GridLayout(1, 2));
			for (int q = 0; q < 2; q++) {
				JPanel part = new JPanel();
				part.setBackground(skinColor);
				JPanel eye = new JPanel();
				eye.setBackground(eyeColor);
				part.add(eye);
				face.add(part);
			}
			hair.setBackground(hairColor);
			ret.add(hair, BorderLayout.NORTH);
			ret.add(face);
			if (h.getGender()) {
				JPanel longHair1 = new JPanel();
				longHair1.setBackground(hairColor);
				JPanel longHair2 = new JPanel();
				longHair2.setBackground(hairColor);
				ret.add(longHair1, BorderLayout.WEST);
				ret.add(longHair2, BorderLayout.EAST);
			} else if (h.getAppearance()[8] > 0 || h.getAppearance()[9] > 0) {
				JPanel faceHair = new JPanel();
				faceHair.setBackground(hairColor);
				ret.add(faceHair, BorderLayout.SOUTH);
			}
		} else {
			//TODO make a temporary solution
		}
		return ret;
	}
	
	private static JPanel unitPreview(Unit u) {
		JPanel ret = new JPanel(new GridLayout(2, 1));
		
		JPanel generalInfo = new JPanel(new GridLayout(1, 3));
		generalInfo.add(makePortrait(u));

		JPanel name = new JPanel(new GridLayout(3, 1));
		name.add(new JLabel(u.getDisplayName()));
		if (u.getUnitClass() == null) {
			name.add(new JLabel("Class Not Assigned"));
		} else {
			name.add(new JLabel("Class: " + u.getUnitClassName()));
		}
		name.add(new JLabel("LVL: " + u.getLevel() + " EXP: " + u.getExperience()));
		generalInfo.add(name);
		
		if (u instanceof Equippable) {
			generalInfo.add(proficienciesDisplay((Equippable)u));
		} else {
			//TODO
			generalInfo.add(new JPanel());
		}
		
		JPanel affectedStats = new JPanel(new GridLayout(1, 2));
		affectedStats.add(attributesDisplay(u));
		if (u instanceof Human) {
			affectedStats.add(humanHPsDisplay((Human)u));
		} else {
			//TODO
			affectedStats.add(new JPanel());
		}
		
		ret.add(generalInfo);
		ret.add(affectedStats);
		
		return ret;
	}

	private JPanel unitPreview(Human h, UnitClass uc) {
		JPanel ret = new JPanel(new GridLayout(2, 1));
		
		JPanel generalInfo = new JPanel(new GridLayout(1, 3));
		generalInfo.add(makePortrait(h));

		JPanel name = new JPanel(new GridLayout(3, 1));
		name.add(new JLabel(h.getDisplayName()));
		name.add(new JLabel("None >> " + uc.getName()));
		name.add(new JLabel("LVL: " + h.getLevel() + " EXP: " + h.getExperience()));
		generalInfo.add(name);
		JPanel weapons = new JPanel(new GridLayout(10, 1)); //Inventory at bottom
		weapons.add(new JLabel("Sword: " + h.proficiencyWith(Weapon.SWORD) + " >> " + uc.getProficiencyModifiers()[Weapon.SWORD]));
		weapons.add(new JLabel("Spear: " + h.proficiencyWith(Weapon.LANCE) + " >> " + uc.getProficiencyModifiers()[Weapon.LANCE]));
		weapons.add(new JLabel("Axe: " + h.proficiencyWith(Weapon.AXE) + " >> " + uc.getProficiencyModifiers()[Weapon.AXE]));
		weapons.add(new JLabel("Bow: " + h.proficiencyWith(Weapon.BOW) + " >> " + uc.getProficiencyModifiers()[Weapon.BOW]));
		weapons.add(new JLabel("Knife: " + h.proficiencyWith(Weapon.KNIFE) + " >> " + uc.getProficiencyModifiers()[Weapon.KNIFE]));
		weapons.add(new JLabel("Ballista: " + h.proficiencyWith(Weapon.BALLISTA) + " >> " + uc.getProficiencyModifiers()[Weapon.BALLISTA]));
		weapons.add(new JLabel("Earth: " + h.proficiencyWith(Weapon.ANIMA) + " >> " + uc.getProficiencyModifiers()[Weapon.ANIMA]));
		weapons.add(new JLabel("Light: " + h.proficiencyWith(Weapon.LIGHT) + " >> " + uc.getProficiencyModifiers()[Weapon.LIGHT]));
		weapons.add(new JLabel("Dark: " + h.proficiencyWith(Weapon.DARK) + " >> " + uc.getProficiencyModifiers()[Weapon.DARK]));
		weapons.add(new JLabel("Staff: " + h.proficiencyWith(Weapon.STAFF) + " >> " + uc.getProficiencyModifiers()[Weapon.STAFF]));
		int[][] inv = h.getInventory();
		JPanel inventory = new JPanel(new GridLayout(1, inv.length));
		for (int q = 0; q < inv.length; q++) {
			final Item i = InventoryIndex.getElement(inv[q]);
			if (i == null) {
				inventory.add(new JPanel());
			} else {
				JButton examine = new JButton(i.getName());
				examine.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						JOptionPane.showMessageDialog(null, "Here will be a description of " + i.getName());
					}
				});
				inventory.add(examine);
			}
		}
		JPanel fullProfDisplay = new JPanel(new BorderLayout());
		fullProfDisplay.add(weapons);
		fullProfDisplay.add(inventory, BorderLayout.SOUTH);
		generalInfo.add(fullProfDisplay);
		
		JPanel stats = new JPanel(new GridLayout(7, 3));
		JButton magInfo = new JButton("MAG");
		magInfo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "Magic: Used for magic-based attacks and other things");
			}
		});
		stats.add(magInfo);
		stats.add(new JLabel("" + h.getMagic()));
		stats.add(new JLabel(h.getMagicGrowth() + "% >> " + (h.getMagicGrowth() + uc.getGrowthModifiers()[4]) + "%"));
		JButton sklInfo = new JButton("SKL");
		sklInfo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "Skill: Used for accuracy and critical hit rate");
			}
		});
		stats.add(sklInfo);
		stats.add(new JLabel("" + h.getSkill()));
		stats.add(new JLabel(h.getSkillGrowth() + "% >> " + (h.getSkillGrowth() + uc.getGrowthModifiers()[5]) + "%"));
		JButton rfxInfo = new JButton("RFX");
		rfxInfo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "Reflex: Used for avoidance and attack speed");
			}
		});
		stats.add(rfxInfo);
		stats.add(new JLabel("" + h.getReflex()));
		stats.add(new JLabel(h.getReflexGrowth() + "% >> " + (h.getReflexGrowth() + uc.getGrowthModifiers()[6]) + "%"));
		JButton awrInfo = new JButton("AWR");
		awrInfo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "Awareness: Used for accuracy, avoidance, and security");
			}
		});
		stats.add(awrInfo);
		stats.add(new JLabel("" + h.getAwareness()));
		stats.add(new JLabel(h.getAwarenessGrowth() + "% >> " + (h.getAwarenessGrowth() + uc.getGrowthModifiers()[7]) + "%"));
		JButton resInfo = new JButton("RES");
		resInfo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "Resistance: Used for defense against magic");
			}
		});
		stats.add(resInfo);
		stats.add(new JLabel("" + h.getResistance()));
		stats.add(new JLabel(h.getResistanceGrowth() + "% >> " + (h.getResistanceGrowth() + uc.getGrowthModifiers()[8]) + "%"));
		JButton movInfo = new JButton("MOV");
		movInfo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "Movement: Moveable distance in one turn");
			}
		});
		stats.add(movInfo);
		stats.add(new JLabel("" + h.getMovement()));
		stats.add(new JPanel());
		JButton ldrInfo = new JButton("LDR");
		ldrInfo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "Leadership: Gives a bonus to character's team if the character is the team leader");
			}
		});
		stats.add(ldrInfo);
		stats.add(new JLabel("" + h.getLeadership()));
		stats.add(new JPanel());
		JPanel statsHeader = new JPanel(new GridLayout(1, 3));
		statsHeader.add(new JLabel("Stat"));
		statsHeader.add(new JLabel("Current Value"));
		statsHeader.add(new JLabel("Growth Rate"));
		JPanel statsDisplay = new JPanel(new BorderLayout());
		statsDisplay.add(statsHeader, BorderLayout.NORTH);
		statsDisplay.add(stats);

		JPanel bodyStats = new JPanel(new GridLayout(10, 3));
		JButton head = new JButton("Head");
		head.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "Affects accuracy. Character will die if HP reaches 0");
			}
		});
		bodyStats.add(head);
		bodyStats.add(new JLabel(String.format("%d/%d (Armor: %d)",
				h.getCurrentHPOfBodyPart(Human.HEAD),
				h.getMaximumHPOfBodyPart(Human.HEAD),
				h.defense(false, Human.HEAD))));
		bodyStats.add(new JLabel(h.getGrowthRateOfBodyPart(Human.HEAD) + "% >> " + (h.getGrowthRateOfBodyPart(Human.HEAD) + uc.getGrowthModifiers()[0]) + "%"));
		JButton torso = new JButton("Torso");
		torso.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "Center of the body. Character will die if HP reaches 0");
			}
		});
		bodyStats.add(torso);
		bodyStats.add(new JLabel(String.format("%d/%d (Armor: %d)",
				h.getCurrentHPOfBodyPart(Human.TORSO),
				h.getMaximumHPOfBodyPart(Human.TORSO),
				h.defense(false, Human.TORSO))));
		bodyStats.add(new JLabel(h.getGrowthRateOfBodyPart(Human.TORSO) + "% >> " + (h.getGrowthRateOfBodyPart(Human.TORSO) + uc.getGrowthModifiers()[1]) + "%"));
		JButton arm1 = new JButton("Right Arm");
		arm1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "Used for physical strength and accuracy");
			}
		});
		bodyStats.add(arm1);
		bodyStats.add(new JLabel(String.format("%d/%d (Armor: %d)",
				h.getCurrentHPOfBodyPart(Human.RIGHT_ARM),
				h.getMaximumHPOfBodyPart(Human.RIGHT_ARM),
				h.defense(false, Human.RIGHT_ARM))));
		bodyStats.add(new JLabel(h.getGrowthRateOfBodyPart(Human.RIGHT_ARM) + "% >> " + (h.getGrowthRateOfBodyPart(Human.RIGHT_ARM) + uc.getGrowthModifiers()[2]) + "%"));
		JButton arm2 = new JButton("Left Arm");
		arm2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "Used for physical strength and accuracy");
			}
		});
		bodyStats.add(arm2);
		bodyStats.add(new JLabel(String.format("%d/%d (Armor: %d)",
				h.getCurrentHPOfBodyPart(Human.LEFT_ARM),
				h.getMaximumHPOfBodyPart(Human.LEFT_ARM),
				h.defense(false, Human.LEFT_ARM))));
		bodyStats.add(new JLabel(h.getGrowthRateOfBodyPart(Human.LEFT_ARM) + "% >> " + (h.getGrowthRateOfBodyPart(Human.LEFT_ARM) + uc.getGrowthModifiers()[2]) + "%"));
		JButton leg1 = new JButton("Right Leg");
		leg1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "Affects movement for unmounted characters");
			}
		});
		bodyStats.add(leg1);
		bodyStats.add(new JLabel(String.format("%d/%d (Armor: %d)",
				h.getCurrentHPOfBodyPart(Human.RIGHT_LEG),
				h.getMaximumHPOfBodyPart(Human.RIGHT_LEG),
				h.defense(false, Human.RIGHT_LEG))));
		bodyStats.add(new JLabel(h.getGrowthRateOfBodyPart(Human.RIGHT_LEG) + "% >> " + (h.getGrowthRateOfBodyPart(Human.RIGHT_LEG) + uc.getGrowthModifiers()[3]) + "%"));
		JButton leg2 = new JButton("Left Leg");
		leg2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "Affects movement for unmounted characters");
			}
		});
		bodyStats.add(leg2);
		bodyStats.add(new JLabel(String.format("%d/%d (Armor: %d)",
				h.getCurrentHPOfBodyPart(Human.LEFT_LEG),
				h.getMaximumHPOfBodyPart(Human.LEFT_LEG),
				h.defense(false, Human.LEFT_LEG))));
		bodyStats.add(new JLabel(h.getGrowthRateOfBodyPart(Human.LEFT_LEG) + "% >> " + (h.getGrowthRateOfBodyPart(Human.LEFT_LEG) + uc.getGrowthModifiers()[3]) + "%"));
		JButton eye1 = new JButton("Right Eye");
		eye1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "Affects accuracy and avoidance.");
			}
		});
		bodyStats.add(eye1);
		bodyStats.add(new JLabel(String.format("%d/%d (Armor: %d)",
				h.getCurrentHPOfBodyPart(Human.RIGHT_EYE),
				h.getMaximumHPOfBodyPart(Human.RIGHT_EYE),
				h.defense(false, Human.RIGHT_EYE))));
		bodyStats.add(new JPanel());
		JButton eye2 = new JButton("Left Eye");
		eye2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "Affects accuracy and avoidance.");
			}
		});
		bodyStats.add(eye2);
		bodyStats.add(new JLabel(String.format("%d/%d (Armor: %d)",
				h.getCurrentHPOfBodyPart(Human.LEFT_EYE),
				h.getMaximumHPOfBodyPart(Human.LEFT_EYE),
				h.defense(false, Human.LEFT_EYE))));
		bodyStats.add(new JPanel());
		if (uc.getMount() != null) {
			JButton mount = new JButton("Mount (" + uc.getMount().getDisplayName() + ")");
			mount.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					JOptionPane.showMessageDialog(null, "Grants extra movement and avoidance. Mount will die if HP reaches 0");
				}
			});
			bodyStats.add(mount);
			bodyStats.add(new JLabel(String.format("%d/%d >> ??/?? (Armor: %d)",
					h.getCurrentHPOfBodyPart(Human.MOUNT),
					h.getMaximumHPOfBodyPart(Human.MOUNT),
					h.defense(false, Human.MOUNT))));
			bodyStats.add(new JLabel(h.getGrowthRateOfBodyPart(Human.MOUNT) + "% >> ??%"));
		} else {
			bodyStats.add(new JPanel());
			bodyStats.add(new JPanel());
			bodyStats.add(new JPanel());
		}
		final Armor a = h.getArmor();
		if (a != null) {
			bodyStats.add(new JLabel("Wearing:"));
			bodyStats.add(new JLabel(a.getName()));
			JButton examine = new JButton("Examine");
			examine.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					JOptionPane.showMessageDialog(null, "Here will be a description of " + a.getName());
				}
			});
			bodyStats.add(examine);
		}
		
		JPanel header = new JPanel(new GridLayout(1, 3));
		header.add(new JLabel("Part"));
		header.add(new JLabel("Health"));
		header.add(new JLabel("Growth Rate"));
		
		JPanel fullBodyStatsDisplay = new JPanel(new BorderLayout());
		fullBodyStatsDisplay.add(header, BorderLayout.NORTH);
		fullBodyStatsDisplay.add(bodyStats);
		
		JPanel affectedStats = new JPanel(new GridLayout(1, 2));
		affectedStats.add(statsDisplay);
		affectedStats.add(fullBodyStatsDisplay);
		
		ret.add(generalInfo);
		ret.add(affectedStats);
		
		return ret;
	}
	private class HistoryPanel extends JPanel {
		public HistoryPanel(Nation n) {
			//TODO
			setLayout(new BorderLayout());
			add(new JLabel("TODO"));
			add(returnToWorldMap("WORLD MAP"), BorderLayout.SOUTH);
		}
	}
	private class UnitGroupStatsPanel extends JPanel {
		public UnitGroupStatsPanel(final UnitGroup g) {
			//TODO
			setLayout(new BorderLayout());
			
			final JPanel unitPreview = new JPanel();
			
			JPanel units = new JPanel(new GridLayout(g.size(), 5));
			for (int q = 0; q < g.size(); q++) {
				final int idx = q;
				final Unit u = g.get(q);
				units.add(new JLabel(u.getName()));
				units.add(new JLabel(u.getUnitClassName()));
				units.add(new JLabel(String.format("%d(%d)", u.getLevel(), u.getExperience())));
				JButton preview = new JButton("View");
				preview.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						unitPreview.removeAll();
						unitPreview.setLayout(new BorderLayout());
						unitPreview.add(unitPreview(u));
						JButton viewAll = new JButton("View All Stats");
						viewAll.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								JButton back = new JButton("BACK");
								back.addActionListener(new ActionListener() {
									@Override
									public void actionPerformed(ActionEvent e) {
										switchToPanel(new UnitGroupStatsPanel(g), VIEW);
									}
								});
								switchToPanel(new UnitStatsPanel(u, back), VIEW);
							}
						});
						unitPreview.add(viewAll, BorderLayout.SOUTH);
						unitPreview.validate();
						unitPreview.repaint();
					}
				});
				units.add(preview, BorderLayout.SOUTH);
				if (q == 0) {
					units.add(new JLabel("*"));
				} else if (g.getAffiliation() == playerNation()) {
					JButton appoint = new JButton("Appoint");
					appoint.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							g.assignLeader(idx);
							switchToPanel(new UnitGroupStatsPanel(g), VIEW);
						}
					});
					units.add(appoint);
				} else {
					units.add(new JLabel("--"));
				}
			}
			JPanel unitLabels = new JPanel(new GridLayout(1, 5));
			unitLabels.add(new JLabel("Name"));
			unitLabels.add(new JLabel("Class"));
			unitLabels.add(new JLabel("LVL (EXP)"));
			unitLabels.add(new JLabel("View"));
			unitLabels.add(new JLabel("Appoint Leader"));
			
			JPanel unitsDisplay = new JPanel(new BorderLayout());
			unitsDisplay.add(new JScrollPane(units));
			unitsDisplay.add(unitLabels, BorderLayout.NORTH);
			
			JPanel details = new JPanel(new GridLayout(8, 2));
			details.add(new JLabel("Affiliation:"));
			if (g.getAffiliation() == null) {
				details.add(new JLabel("None"));
			} else {
				JButton nation = new JButton(g.getAffiliation().getName());
				nation.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						switchToPanel(new NationStatsPanel(g.getAffiliation()), VIEW);
					}
				});
				details.add(nation);
			}
			details.add(new JLabel("Movement:"));
			String move = "" + g.getMovement();
			if (g.canFly()) {
				move += " (Can Fly)";
			}
			details.add(new JLabel(move));
			final JLabel aiControl = new JLabel();
			final String waiting = "Waiting for Orders";
			final String freeroam = "AI-Controlled";
			if (g.getAffiliation() == playerNation() && !(g.isAIControlled())) {
				aiControl.setText(waiting);
			} else {
				aiControl.setText(freeroam);
			}
			details.add(aiControl);
			if (g.getAffiliation() == playerNation()) {
				JButton toggle = new JButton("Toggle");
				toggle.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						g.toggleAIControl();
						if (g.getAffiliation() == playerNation() && !(g.isAIControlled())) {
							aiControl.setText(waiting);
						} else {
							aiControl.setText(freeroam);
						}
						aiControl.validate();
						aiControl.repaint();
					}
				});
				details.add(toggle);
			} else {
				details.add(new JLabel("Locked"));
			}
			
			details.add(new JLabel("Assignment:"));
			if (g.getAssignedThing() == null) {
				details.add(new JLabel("None"));
			} else {
				JButton assignment = new JButton(g.getAssignedThing().getName());
				assignment.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						if (g.getAssignedThing() instanceof Ship) {
							switchToPanel(new ShipStatsPanel((Ship)g.getAssignedThing()), VIEW);
						} else if (g.getAssignedThing() instanceof Barracks) {
							switchToPanel(new BarracksStatsPanel((Barracks)g.getAssignedThing()), VIEW);
						} else if (g.getAssignedThing() instanceof Castle) {
							switchToPanel(new CastleStatsPanel((Castle)g.getAssignedThing()), VIEW);
						} else if (g.getAssignedThing() instanceof Fortress) {
							switchToPanel(new FortressStatsPanel((Fortress)g.getAssignedThing()), VIEW);
						} else if (g.getAssignedThing() instanceof Prison) {
							switchToPanel(new PrisonStatsPanel((Prison)g.getAssignedThing()), VIEW);
						} else if (g.getAssignedThing() instanceof TrainingFacility) {
							switchToPanel(new TrainingFacilityStatsPanel((TrainingFacility)g.getAssignedThing()), VIEW);
						}
					}
				});
				details.add(assignment);
			}
			JButton locate = new JButton("Locate");
			locate.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
				}
			});
			details.add(locate);
			details.add(new JPanel());
			
			JButton manageInventory = new JButton("Manage Inventory");
			manageInventory.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
				}
			});
			details.add(manageInventory);
			details.add(new JPanel());
			
			JButton managePositions = new JButton("Battle Positions");
			managePositions.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
				}
			});
			details.add(managePositions);
			details.add(new JPanel());
			
			details.add(new JLabel("Prisoners:"));
			if (g.getPrisoners() == null) {
				details.add(new JLabel("None"));
			} else {
				JButton managePrisoners = new JButton();
				managePrisoners.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
					}
				});
				details.add(managePrisoners);
			}
			
			JPanel stats = new JPanel(new GridLayout(6, 2));
			int[] power = g.getPower();
			stats.add(new JLabel("Size: " + g.size()));
			stats.add(new JLabel("Total Physical Might: " + power[0]));
			stats.add(new JLabel("Total Magical Might: " + power[1]));
			stats.add(new JLabel("Total Accuracy: " + power[2]));
			stats.add(new JLabel("Total Avoidance: " + power[3]));
			stats.add(new JLabel("Total Critical Hit Rate: " + power[4]));
			stats.add(new JLabel("Total Critical Security: " + power[5]));
			stats.add(new JLabel("Average Attack Speed: " + power[6]));
			stats.add(new JLabel("Total Defense: " + power[7]));
			stats.add(new JLabel("Total Resistance: " + power[8]));
			stats.add(new JLabel("Total Head HP: " + power[9]));
			stats.add(new JLabel("Total Torso HP: " + power[10]));
			
			JPanel fullDisplay = new JPanel(new GridLayout(2, 2));
			fullDisplay.add(unitsDisplay);
			fullDisplay.add(details);
			fullDisplay.add(unitPreview);
			fullDisplay.add(stats);
			
			add(fullDisplay);
			add(returnToWorldMap("WORLD MAP"), BorderLayout.SOUTH);
		}
		
		public UnitGroupStatsPanel(UnitGroup g, JButton back) {
			this(g);
			add(back, BorderLayout.SOUTH);
		}
	}
	
	private class DiplomacyStatsPanel extends JPanel {
		public DiplomacyStatsPanel(final DiplomaticRelation dr) {
			setLayout(new BorderLayout());
			final Nation n1 = dr.getNation1();
			final Nation n2 = dr.getNation2();
			//TODO
			JPanel nations = new JPanel(new GridLayout(1, 3));
			
			final JButton back = new JButton("BACK");
			back.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					switchToPanel(new DiplomacyStatsPanel(dr), VIEW);
				}
			});
			JPanel nation1Description = new JPanel(new BorderLayout());
			JButton nation1Name = new JButton(n1.getFullName());
			nation1Name.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					switchToPanel(new NationStatsPanel(n1, back), VIEW);
				}
			});
			nation1Description.add(nation1Name, BorderLayout.NORTH);
			if (n1.getRuler() == null) {
				nation1Description.add(new JLabel("Ruler: None"), BorderLayout.SOUTH);
			} else {
				nation1Description.add(makePortrait(n1.getRuler()));
				JButton nation1Leader = new JButton("Ruler: " + n1.getRuler().getDisplayName());
				nation1Leader.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						switchToPanel(new UnitStatsPanel(n1.getRuler(), back), VIEW);
					}
				});
				nation1Description.add(nation1Leader, BorderLayout.SOUTH);
			}
			
			JPanel relationshipStats = new JPanel(new GridLayout(3, 1));
			relationshipStats.add(new JLabel("Relationship Standing"));
			relationshipStats.add(new JLabel(dr.getRelationshipStrengthDisplay()));
			if (dr.getCurrentEvent() == null) {
				relationshipStats.add(new JLabel("No Current Events"));
			} else {
				JButton event = new JButton("Current Event: " + dr.getCurrentEvent().getName());
				event.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						JButton back = new JButton("BACK");
						back.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								switchToPanel(new DiplomacyStatsPanel(dr), VIEW);
							}
						});
						if (dr.getCurrentEvent() instanceof War) {
							switchToPanel(new WarStatsPanel((War)dr.getCurrentEvent(), back), VIEW);
						} else if (dr.getCurrentEvent() instanceof Festival) {
							switchToPanel(new FestivalStatsPanel((Festival)dr.getCurrentEvent(), back), VIEW);
						} else if (dr.getCurrentEvent() instanceof SportingEvent) {
							switchToPanel(new SportsStatsPanel((SportingEvent)dr.getCurrentEvent(), back), VIEW);
						}
					}
				});
				relationshipStats.add(event);
			}
			
			JPanel nation2Description = new JPanel(new BorderLayout());
			JButton nation2Name = new JButton(n2.getFullName());
			nation2Name.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					switchToPanel(new NationStatsPanel(n2, back), VIEW);
				}
			});
			nation2Description.add(nation2Name, BorderLayout.NORTH);
			if (n2.getRuler() == null) {
				nation2Description.add(new JLabel("Ruler: None"), BorderLayout.SOUTH);
			} else {
				nation2Description.add(makePortrait(n2.getRuler()));
				JButton nation2Leader = new JButton("Ruler: " + n2.getRuler().getDisplayName());
				nation2Leader.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						switchToPanel(new UnitStatsPanel(n2.getRuler(), back), VIEW);
					}
				});
				nation2Description.add(nation2Leader, BorderLayout.SOUTH);
			}
			
			JPanel tradePanel = new JPanel(new GridLayout(1, 2));
			
			final List<int[]> tradeDeals = dr.getTradeDeals();
			JPanel dealsList = new JPanel(new GridLayout(tradeDeals.size(), 4));
			for (int q = 0; q < tradeDeals.size(); q++) {
				final int idx = q;
				int[] deal = tradeDeals.get(q);
				JLabel part1 = new JLabel();
				JLabel exchange = new JLabel(" for ");
				JLabel part2 = new JLabel();
				if (deal[0] < 0) {
					part1.add(new JLabel(deal[1] + " " + Mount.values()[(deal[0] + 1) * -1].getDisplayName()));
				} else {
					part1.add(new JLabel(deal[2] + " " + InventoryIndex.getElement(deal).getName()));
				}
				if (deal[3] < 0) {
					part2.add(new JLabel(deal[4] + " " + Mount.values()[(deal[3] + 1) * -1].getDisplayName()));
				} else {
					part2.add(new JLabel(deal[2] + " " + InventoryIndex.getElement(new int[] {deal[3], deal[4]}).getName()));
				}
				JButton cancel = new JButton("Cancel Deal");
				cancel.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						String[] opts = new String[] {"No", "Yes"};
						int decision = JOptionPane.showOptionDialog(null, "Cancel Trade Deal?", "CANCEL", JOptionPane.DEFAULT_OPTION,
								JOptionPane.PLAIN_MESSAGE, null, opts, opts[0]);
						if (decision == 1) {
							dr.cancelTradeDeal(idx);
							switchToPanel(new DiplomacyStatsPanel(dr), VIEW);
						}
					}
				});
				dealsList.add(part1);
				dealsList.add(exchange);
				dealsList.add(part2);
				dealsList.add(cancel);
			}
			tradePanel.add(new JScrollPane(dealsList));
			
			if (dr.getCurrentEvent() instanceof War) {
				tradePanel.add(new JPanel());
			} else {
				JButton makeTradeDeal = new JButton("New Trade Deal");
				makeTradeDeal.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
					}
				});
				tradePanel.add(makeTradeDeal);
			}
			
			MajorEvent event = dr.getCurrentEvent();
			
			JPanel sportPanel = new JPanel(new GridLayout(1, 2));
			JPanel festivalPanel = new JPanel(new GridLayout(1, 2));
			JPanel warPanel = new JPanel(new GridLayout(1, 2));
			sportPanel.add(new JLabel("Past Sporting Events: " + dr.getSportsCount()));
			festivalPanel.add(new JLabel("Past Festivals: " + dr.getFestivalsCount()));
			warPanel.add(new JLabel("Past Wars: " + dr.getWarsCount()));
			if (event == null) {
				JButton proposeSport = new JButton("Propose Sporting Event");
				proposeSport.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
					}
				});
				sportPanel.add(proposeSport);
				JButton proposeFestival = new JButton("Propose Festival");
				proposeFestival.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
					}
				});
				festivalPanel.add(proposeFestival);
				JButton declareWar = new JButton("Declare War");
				declareWar.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						WarCause[] opts = WarCause.getAllInitiatorOptions();
						JComboBox<WarCause> causes = new JComboBox<>(opts);
						JOptionPane.showMessageDialog(null, causes, "<html>Specify a reason for declaring war.<br/>"
								+ "<html>Note that declaring war will cancel all trade deals between<br/>"
								+ "<html>" + n1.getName() + " and " + n2.getName() + ", and will negatively impact their relationship.<br/>",
								JOptionPane.QUESTION_MESSAGE);
						WarCause choice = (WarCause)causes.getSelectedItem();
						if (choice != null && choice != WarCause.CANCEL) {
							n1.declareWar(n2, choice, GeneralGameplayManager.getDaysSinceGameStart());
							GeneralGameplayManager.switchToEventTime();
							
							switchToPanel(new DiplomacyStatsPanel(dr), VIEW);
							//TODO figure out messaging system
						}
					}
				});
				warPanel.add(declareWar);
			} else if (event instanceof SportingEvent) {
				JButton cancelSport = new JButton("Cancel Current Sporting Event");
				cancelSport.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						//TODO confirm, then cancel the sporting event
					}
				});
				sportPanel.add(cancelSport);
				festivalPanel.add(new JPanel());
				warPanel.add(new JPanel());
			} else if (event instanceof Festival) {
				JButton cancelFestival = new JButton("Cancel Festival");
				cancelFestival.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						//TODO confirm, then cancel the festival
					}
				});
				sportPanel.add(new JPanel());
				festivalPanel.add(cancelFestival);
				warPanel.add(new JPanel());
			} else if (event instanceof War) {
				JButton attemptPeace = new JButton("Try for Peace");
				attemptPeace.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						//TODO give options for trying for peace
					}
				});
				sportPanel.add(new JPanel());
				festivalPanel.add(new JPanel());
				warPanel.add(attemptPeace);
			}
			
			JPanel totalDisplay = new JPanel(new GridLayout(5, 1));
			nations.add(nation1Description);
			nations.add(relationshipStats);
			nations.add(nation2Description);
			totalDisplay.add(nations);
			totalDisplay.add(tradePanel);
			totalDisplay.add(sportPanel);
			totalDisplay.add(warPanel);
			totalDisplay.add(festivalPanel);
			
			add(totalDisplay);
			add(returnToWorldMap("WORLD MAP"), BorderLayout.SOUTH);
		}
	}
	
	private class WarStatsPanel extends JPanel {
		
		public WarStatsPanel(final War w, final JButton back) {
			
			setLayout(new BorderLayout());
			
			JPanel infoDisplay = new JPanel(new GridLayout(3, 1));	
			
			JPanel name = new JPanel(new GridLayout(1, 3));
			name.add(new JLabel(w.getName()));
			final JTextField newName = new JTextField();
			name.add(newName);
			JButton rename = new JButton("Rename");
			rename.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (newName.getText() == null || newName.getText().equals("")) {
						JOptionPane.showMessageDialog(null, "You must input a name to rename the war");
					} else {
						//TODO error check for bad names
						if (confirmChoiceDialog("Rename " + w.getName() + " to " + newName.getText() + "?")) {
							//TODO trim whitespace and stuff
							w.rename(newName.getText());
							switchToPanel(new WarStatsPanel(w, back), VIEW);
						}
					}
				}
			});
			name.add(rename);
			infoDisplay.add(name);
			
			infoDisplay.add(new JLabel(w.getDescription()));
			
			JPanel deathStats = new JPanel(new GridLayout(5, 3));
			deathStats.add(new JLabel("Death Counts"));
			deathStats.add(new JLabel(w.getInitiator().getName()));
			deathStats.add(new JLabel(w.getResponder().getName()));
			deathStats.add(new JLabel("Recruited Soldiers"));
			deathStats.add(new JLabel("" + w.getInitiatorRecruitDeaths()));
			deathStats.add(new JLabel("" + w.getResponderRecruitDeaths()));
			deathStats.add(new JLabel("Civilians"));
			deathStats.add(new JLabel("" + w.getInitiatorCivilianDeaths()));
			deathStats.add(new JLabel("" + w.getResponderCivilianDeaths()));
			deathStats.add(new JLabel("Clones"));
			deathStats.add(new JLabel("" + w.getInitiatorCloneDeaths()));
			deathStats.add(new JLabel("" + w.getResponderCloneDeaths()));
			deathStats.add(new JLabel("Monsters"));
			deathStats.add(new JLabel("" + w.getInitiatorMonsterDeaths()));
			deathStats.add(new JLabel("" + w.getResponderMonsterDeaths()));
			infoDisplay.add(deathStats);
			
			add(infoDisplay);
			
			add(back, BorderLayout.SOUTH);
		}
		
	}
	
	private class FestivalStatsPanel extends JPanel {
		public FestivalStatsPanel(Festival f, JButton back) {
			//TODO
			setLayout(new BorderLayout());
			add(new JLabel("TODO"));
			add(returnToWorldMap("WORLD MAP"), BorderLayout.SOUTH);
		}
	}
	
	private class SportsStatsPanel extends JPanel {
		public SportsStatsPanel(SportingEvent s, JButton back) {
			//TODO
			setLayout(new BorderLayout());
			add(new JLabel("TODO"));
			add(returnToWorldMap("WORLD MAP"), BorderLayout.SOUTH);
		}
	}
	
	private class ShipStatsPanel extends JPanel {
		public ShipStatsPanel(Ship s) {
			//TODO
			setLayout(new BorderLayout());
			add(new JLabel("TODO"));
			add(returnToWorldMap("WORLD MAP"), BorderLayout.SOUTH);
		}
	}
	private class ColiseumStatsPanel extends JPanel {
		public ColiseumStatsPanel(Coliseum c) {
			//TODO
			setLayout(new BorderLayout());
			add(new JLabel("TODO"));
			add(returnToWorldMap("WORLD MAP"), BorderLayout.SOUTH);
		}
	}
	private class HospitalStatsPanel extends JPanel {
		public HospitalStatsPanel(Hospital h) {
			//TODO
			setLayout(new BorderLayout());
			add(new JLabel("TODO"));
			add(returnToWorldMap("WORLD MAP"), BorderLayout.SOUTH);
		}
	}
	private class PortStatsPanel extends JPanel {
		public PortStatsPanel(Port p) {
			//TODO
			setLayout(new BorderLayout());
			add(new JLabel("TODO"));
			add(returnToWorldMap("WORLD MAP"), BorderLayout.SOUTH);
		}
	}
	private class ResearchCenterStatsPanel extends JPanel {
		public ResearchCenterStatsPanel(ResearchCenter r) {
			//TODO
			setLayout(new BorderLayout());
			add(new JLabel("TODO"));
			add(returnToWorldMap("WORLD MAP"), BorderLayout.SOUTH);
		}
	}
	private class ShipyardStatsPanel extends JPanel {
		public ShipyardStatsPanel(Shipyard s) {
			//TODO
			setLayout(new BorderLayout());
			add(new JLabel("TODO"));
			add(returnToWorldMap("WORLD MAP"), BorderLayout.SOUTH);
		}
	}
	private class VillageStatsPanel extends JPanel {
		public VillageStatsPanel(final Village v) {
			
			setLayout(new BorderLayout());
			
			JPanel basicInfo = getBuildingBasicInfo(v);
			
			JPanel popAndValues = new JPanel(new GridLayout(1, 2));
			popAndValues.add(new JLabel("Population: " + v.getPopulation() + "K"));
			JPanel values = new JPanel(new GridLayout(3, 2));
			values.add(new JLabel("Altruism: " + v.getCityState().getAltruism()));
			values.add(new JLabel("Familism: " + v.getCityState().getFamilism()));
			values.add(new JLabel("Militarism: " + v.getCityState().getMilitarism()));
			values.add(new JLabel("Nationalism: " + v.getCityState().getNationalism()));
			values.add(new JLabel("Independence: " + v.getCityState().getConfidence()));
			values.add(new JLabel("Compliance: " + v.getCityState().getTolerance()));
			JPanel valuesDisplay = new JPanel(new BorderLayout());
			valuesDisplay.add(values);
			valuesDisplay.add(new JLabel("City of " + v.getCityState().getName()), BorderLayout.NORTH);
			popAndValues.add(valuesDisplay);
			
			JPanel activities = new JPanel();
			//TODO make activities panel
			
			JPanel recruitment = new JPanel(new GridLayout(1, 3));
			final JTextField num = new JTextField();
			final List<TrainingFacility> choices = v.recruitDestinations();
			TrainingFacility[] opts = new TrainingFacility[choices.size()];
			for (int q = 0; q < choices.size(); q++) {
				opts[q] = choices.get(q);
			}
			final JComboBox<TrainingFacility> dest = new JComboBox<>(opts);
			
			JButton recruit = new JButton("Recruit");
			if (dest.getItemCount() > 0) {
				recruit.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						try {
							int numToTake = parseValidDigitWithinBounds(num.getText(), 1, 20, "Num");
							TrainingFacility tf = choices.get(dest.getSelectedIndex());
							if (numToTake + tf.getTrainees().size() > UnitGroup.CAPACITY
									|| numToTake + v.getCityState().getNation().getArmy().size() > Nation.MAX_ARMY_SIZE) {
								throw new IllegalArgumentException("Cannot send this many recruits to this training facility");
							} else {
								//TODO subtract actions
								for (int q = 0; q < numToTake; q++) {
									tf.addUnit(Human.completelyRandomHuman(v.getCityState()));
								}
								JOptionPane.showMessageDialog(null, "Sent!");
							}
						} catch (Exception x) {
							JOptionPane.showMessageDialog(null, x.getMessage());
						}
					}
				});
			} else {
				recruit.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						JOptionPane.showMessageDialog(null, "There are no training facilities in this city");
					}
				});
			}
			recruitment.add(num);
			recruitment.add(dest);
			recruitment.add(recruit);
			JPanel recruitmentDisplay = new JPanel(new BorderLayout());
			recruitmentDisplay.add(recruitment);
			recruitmentDisplay.add(new JLabel("Specify a number of recruits and a training facility to send them to"), BorderLayout.NORTH);
			
			JPanel veteran = new JPanel(new GridLayout(v.getVeterans().size(), 3));
			for (int q = 0; q < v.getVeterans().size(); q++) {
				final Human current = v.getVeterans().get(q);
				veteran.add(makePortrait(current));
				veteran.add(new JLabel(current.getDisplayName()));
				JButton info = new JButton("View Character");
				info.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						switchToPanel(new UnitStatsPanel(current), VIEW);
					}
				});
				veteran.add(info);
			}
			JPanel veteranInfo = new JPanel(new BorderLayout());
			JPanel veteranInfoExplain = new JPanel(new GridLayout(1, 3));
			veteranInfoExplain.add(new JLabel("Portrait"));
			veteranInfoExplain.add(new JLabel("Name"));
			veteranInfoExplain.add(new JLabel("Info"));
			veteranInfo.add(new JScrollPane(veteran));
			veteranInfo.add(veteranInfoExplain, BorderLayout.NORTH);
			JPanel veteranDisplay = new JPanel(new BorderLayout());
			veteranDisplay.add(veteranInfo);
			veteranDisplay.add(new JLabel("Resident Veterans"), BorderLayout.NORTH);
			
			JPanel civilian = new JPanel(new GridLayout(1, 2));
			civilian.add(popAndValues);
			civilian.add(activities);
			
			JPanel military = new JPanel(new GridLayout(1, 2));
			military.add(recruitmentDisplay);
			military.add(veteranDisplay);
			
			JPanel fullDisplay = new JPanel(new GridLayout(3, 1));
			fullDisplay.add(basicInfo);
			fullDisplay.add(civilian);
			fullDisplay.add(military);
			
			add(fullDisplay);
			add(returnToWorldMap("WORLD MAP"), BorderLayout.SOUTH);
		}
	}
	private class BarracksStatsPanel extends JPanel {
		public BarracksStatsPanel(Barracks b) {
			//TODO
			setLayout(new BorderLayout());
			add(new JLabel("TODO"));
			add(returnToWorldMap("WORLD MAP"), BorderLayout.SOUTH);
		}
	}
	private class CastleStatsPanel extends JPanel {
		
		private Castle castle;
		
		public CastleStatsPanel(Castle c) {
			
			this.castle = c;
			
			setLayout(new BorderLayout());
			JPanel basicInfo = getBuildingBasicInfo(c);
			JPanel assignedGroup = getAssignedUnitsDisplay(c);
			
			JPanel armors = getInventoryDisplay(c.getArmors());
			JPanel armorInfo = new JPanel(new GridLayout(1, 3));
			armorInfo.add(new JLabel("Armor Name"));
			armorInfo.add(new JLabel("Amount"));
			armorInfo.add(new JLabel("Info"));
			JPanel armorsDisplay = new JPanel(new BorderLayout());
			armorsDisplay.add(new JScrollPane(armors));
			armorsDisplay.add(armorInfo, BorderLayout.NORTH);
			
			JPanel staves = getInventoryDisplay(c.getStaves());
			JPanel staffInfo = new JPanel(new GridLayout(1, 3));
			staffInfo.add(new JLabel("Staff Name"));
			staffInfo.add(new JLabel("Amount"));
			staffInfo.add(new JLabel("Info"));
			JPanel stavesDisplay = new JPanel(new BorderLayout());
			stavesDisplay.add(new JScrollPane(staves));
			stavesDisplay.add(staffInfo, BorderLayout.NORTH);
			
			JPanel mountsInfo = new JPanel(new GridLayout(1, 3));
			mountsInfo.add(new JLabel("Mount Type"));
			mountsInfo.add(new JLabel("Amount"));
			mountsInfo.add(new JLabel("Info"));
			JPanel mounts = new JPanel(new GridLayout(Mount.values().length, 3));
			for (int q = 0; q < Mount.values().length; q++) {
				final Mount m = Mount.values()[q];
				mounts.add(new JLabel(m.getDisplayName()));
				mounts.add(new JLabel("" + c.getMounts()[m.getId()]));
				JButton desc = new JButton("Info");
				desc.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						JOptionPane.showMessageDialog(null, m.getDescription());
					}
				});
				mounts.add(desc);
			}
			JPanel mountsDisplay = new JPanel(new BorderLayout());
			mountsDisplay.add(mountsInfo, BorderLayout.NORTH);
			mountsDisplay.add(new JScrollPane(mounts));
			
			JPanel activities = new JPanel();
			//TODO activities
			
			JPanel personal = new JPanel();
			if (c.getOwner() == player()) {
				personal.setLayout(new GridLayout(2, 2));
				JButton outfit = new JButton("Outfit");
				outfit.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						if (castle.canOutfitOwner()) {
							switchToPanel(outfitTraineesPanel(), VIEW);
						} else {
							JOptionPane.showMessageDialog(null, "No owner present to outfit");
						}
					}
				});
				JButton support = new JButton("Assign Support Partners");
				support.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
					}
				});
				JButton train = new JButton("Train");
				train.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						JOptionPane.showMessageDialog(null, castle.trainOwner());
						switchToPanel(new CastleStatsPanel(castle), VIEW);
					}
				});
				JPanel dunnoYet = new JPanel();
				personal.add(outfit);
				personal.add(support);
				personal.add(train);
				personal.add(dunnoYet);
			}
			
			JPanel partTwo = new JPanel(new GridLayout(1, 2));
			partTwo.add(assignedGroup);
			partTwo.add(armorsDisplay);
			JPanel partThree = new JPanel(new GridLayout(1, 2));
			partThree.add(activities);
			partThree.add(stavesDisplay);
			JPanel partFour = new JPanel(new GridLayout(1, 2));
			partFour.add(personal);
			partFour.add(mountsDisplay);
			
			JPanel fullDisplay = new JPanel(new GridLayout(4, 1));
			fullDisplay.add(basicInfo);
			fullDisplay.add(partTwo);
			fullDisplay.add(partThree);
			fullDisplay.add(partFour);
			
			add(fullDisplay);
			add(returnToWorldMap("WORLD MAP"), BorderLayout.SOUTH);
		}
		
		private JPanel outfitTraineesPanel() {
			JPanel fullDisplay = new JPanel(new GridLayout(1, 2));
			
			final JPanel ruler = new JPanel(new BorderLayout());
			ruler.add(unitPreview(castle.getOwner()));
			
			
			JPanel weapons = getInventoryDisplay(castle.getMaterials());
			JPanel weaponSelect = new JPanel(new GridLayout(castle.getMaterials().size(), 1));
			for (int q = 0; q < castle.getMaterials().size(); q++) {
				final int current = q;
				JButton giveWep = new JButton("+");
				giveWep.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						String weaponName = InventoryIndex.getElement(castle.getMaterials().get(current)).getName();
						if (castle.assignWeapon(current)) {
							JOptionPane.showMessageDialog(null, String.format("Gave %s the %s!",
									castle.getOwner().getName(),
									weaponName));
							switchToPanel(outfitTraineesPanel(), VIEW);
						} else {
							JOptionPane.showMessageDialog(null, "Cannot give this item");
						}
					}
				});
				weaponSelect.add(giveWep);
			}
			JPanel weaponsDisplay = new JPanel(new BorderLayout());
			weaponsDisplay.add(weapons);
			weaponsDisplay.add(weaponSelect, BorderLayout.EAST);
			JButton autoWeapon = new JButton("Auto-Assign Weapon");
			autoWeapon.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					castle.autoAssignWeapon();
					switchToPanel(outfitTraineesPanel(), VIEW);
				}
			});
			JPanel fullWeaponsDisplay = new JPanel(new BorderLayout());
			fullWeaponsDisplay.add(new JScrollPane(weaponsDisplay));
			fullWeaponsDisplay.add(new JLabel("Weapons"), BorderLayout.NORTH);
			fullWeaponsDisplay.add(autoWeapon, BorderLayout.SOUTH);
			
			JPanel armors = getInventoryDisplay(castle.getArmors());
			JPanel armorSelect = new JPanel(new GridLayout(castle.getArmors().size(), 1));
			for (int q = 0; q < castle.getArmors().size(); q++) {
				final int current = q;
				JButton giveArm = new JButton("+");
				giveArm.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						String armorName = InventoryIndex.getElement(castle.getArmors().get(current)).getName();
						if (castle.assignArmor(current)) {
							JOptionPane.showMessageDialog(null, String.format("Gave %s the %s!",
									castle.getOwner().getName(),
									armorName));
							switchToPanel(outfitTraineesPanel(), VIEW);
						} else {
							JOptionPane.showMessageDialog(null, "Cannot give this item");
						}
					}
				});
				armorSelect.add(giveArm);
			}
			JPanel armorsDisplay = new JPanel(new BorderLayout());
			armorsDisplay.add(armors);
			armorsDisplay.add(armorSelect, BorderLayout.EAST);
			JButton autoArmor = new JButton("Auto-Assign Armor");
			autoArmor.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					castle.autoAssignArmor();
					switchToPanel(outfitTraineesPanel(), VIEW);
				}
			});
			JPanel fullArmorsDisplay = new JPanel(new BorderLayout());
			fullArmorsDisplay.add(new JScrollPane(armorsDisplay));
			fullArmorsDisplay.add(new JLabel("Armors"), BorderLayout.NORTH);
			fullArmorsDisplay.add(autoArmor, BorderLayout.SOUTH);
			
			JPanel staves = getInventoryDisplay(castle.getStaves());
			JPanel staffSelect = new JPanel(new GridLayout(castle.getStaves().size(), 1));
			for (int q = 0; q < castle.getStaves().size(); q++) {
				final int current = q;
				JButton giveStf = new JButton("+");
				giveStf.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						String staffName = InventoryIndex.getElement(castle.getStaves().get(current)).getName();
						if (castle.assignStaff(current)) {
							JOptionPane.showMessageDialog(null, String.format("Gave %s the %s!",
									castle.getOwner().getName(),
									staffName));
							switchToPanel(outfitTraineesPanel(), VIEW);
						} else {
							JOptionPane.showMessageDialog(null, "Cannot give this item");
						}
					}
				});
				staffSelect.add(giveStf);
			}
			JPanel stavesDisplay = new JPanel(new BorderLayout());
			stavesDisplay.add(staves);
			stavesDisplay.add(staffSelect, BorderLayout.EAST);
			JButton autoStaff = new JButton("Auto-Assign Staff");
			autoStaff.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					castle.autoAssignStaff();
					switchToPanel(outfitTraineesPanel(), VIEW);
				}
			});
			JPanel fullStavesDisplay = new JPanel(new BorderLayout());
			fullStavesDisplay.add(new JScrollPane(stavesDisplay));
			fullStavesDisplay.add(new JLabel("Staves"), BorderLayout.NORTH);
			fullStavesDisplay.add(autoStaff, BorderLayout.SOUTH);
			
			final JPanel unitClasses = new JPanel(new BorderLayout());
			if (castle.getOwner().getUnitClass() == null) {
				List<UnitClass> potential = UnitClassIndex.getClassesForUnitType(UnitClassIndex.HUMAN);
				List<UnitClass> available =  new List<>(potential.size());
				for (int q = 0; q < potential.size(); q++) {
					UnitClass c = potential.get(q);
					if (c.getTier() == 1 && c.canTrainUnitWithMaterials(castle.getOwner(), castle.getMounts())) {
						available.add(c);
					}
				}
				JPanel classes = new JPanel(new GridLayout(available.size(), 3));
				for (int q = 0; q < available.size(); q++) {
					final UnitClass c = available.get(q);
					classes.add(new JLabel(c.getName()));
					JButton info = new JButton("Info");
					info.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							JOptionPane.showMessageDialog(null, "Here will be a description of " + c.getName());
						}
					});
					classes.add(info);
					JButton preview = new JButton("Preview Assignment");
					preview.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							ruler.removeAll();
							ruler.setLayout(new BorderLayout());
							ruler.add(unitPreview(castle.getOwner(), c));
							JButton confirm = new JButton("Confirm Class Assignment (Cannot Be Undone)");
							confirm.addActionListener(new ActionListener() {
								@Override
								public void actionPerformed(ActionEvent e) {
									castle.assignClass(c);
									switchToPanel(outfitTraineesPanel(), VIEW);
								}
							});
							ruler.add(confirm, BorderLayout.SOUTH);
							ruler.validate();
							ruler.repaint();
						}
					});
					classes.add(preview);
				}
				unitClasses.add(new JScrollPane(classes));
			}
			JButton autoClass = new JButton("Auto-Assign Class");
			autoClass.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (confirmChoiceDialog("<html>Are you sure you want to auto-assign a class?<br/>"
							+ "<html>This cannot be undone!<br/>")) {
						castle.autoAssignClass();
						switchToPanel(outfitTraineesPanel(), VIEW);
					}
				}
			});
			unitClasses.add(autoClass, BorderLayout.SOUTH);
			
			JPanel resourcesView = new JPanel(new GridLayout(4, 1));
			resourcesView.add(fullWeaponsDisplay);
			resourcesView.add(fullArmorsDisplay);
			resourcesView.add(fullStavesDisplay);
			resourcesView.add(unitClasses);
			
			fullDisplay.add(ruler);
			fullDisplay.add(resourcesView);
			
			JButton back = enterBuildingButton(castle, "BACK");
			
			JPanel ret = new JPanel(new BorderLayout());
			ret.add(fullDisplay);
			ret.add(back, BorderLayout.SOUTH);
			
			return ret;
		}

	}
	private class FortressStatsPanel extends JPanel {
		public FortressStatsPanel(Fortress f) {
			//TODO
			setLayout(new BorderLayout());
			add(new JLabel("TODO"));
			add(returnToWorldMap("WORLD MAP"), BorderLayout.SOUTH);
		}
	}
	private class PrisonStatsPanel extends JPanel {
		public PrisonStatsPanel(Prison p) {
			//TODO
			setLayout(new BorderLayout());
			add(new JLabel("TODO"));
			add(returnToWorldMap("WORLD MAP"), BorderLayout.SOUTH);
		}
	}
	private class TrainingFacilityStatsPanel extends JPanel {
		private TrainingFacility tf;
		private int traineeToExamine;
		private UnitClass classToAssign;
		
		public TrainingFacilityStatsPanel(final TrainingFacility t) {
			this.tf = t;
			setLayout(new BorderLayout());
			JPanel top = getBuildingBasicInfo(t);
			
			JPanel traineesInfo = new JPanel(new GridLayout(1, 4));
			traineesInfo.add(new JLabel("Trainee Name"));
			traineesInfo.add(new JLabel("Class"));
			traineesInfo.add(new JLabel("Level (Experience)"));
			traineesInfo.add(new JLabel("Info Page"));
			JPanel trainees = new JPanel(new GridLayout(t.getTrainees().size(), 4));
			for (int q = 0; q < t.getTrainees().size(); q++) {
				final Human h = t.getTrainees().get(q);
				trainees.add(new JLabel(h.getName()));
				trainees.add(new JLabel(h.getUnitClassName()));
				trainees.add(new JLabel(String.format("%d (%d)", h.getLevel(), h.getExperience())));
				JButton examine = new JButton("View Character");
				examine.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						JButton back = enterBuildingButton(tf, "BACK");
						switchToPanel(new UnitStatsPanel(h, back), VIEW);
					}
				});
				trainees.add(examine);
			}
			JPanel traineesDisplay = new JPanel(new BorderLayout());
			traineesDisplay.add(traineesInfo, BorderLayout.NORTH);
			traineesDisplay.add(new JScrollPane(trainees));
			
			JPanel defendersDisplay = getAssignedUnitsDisplay(t);
			
			JPanel choices = new JPanel(new GridLayout(2, 2));
			JButton outfit = new JButton("Outfit Trainees");
			outfit.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					switchToPanel(outfitTraineesPanel(), VIEW);
				}
			});
			choices.add(outfit);
			JButton assignSupportPartners = new JButton("Assign Support Partners");
			assignSupportPartners.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
				}
			});
			choices.add(assignSupportPartners);
			JButton graduate = new JButton("Graduate Trainees");
			graduate.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (tf.getTrainees().isEmpty()) {
						JOptionPane.showMessageDialog(null, "There are currently no soldier trainees in this facility");
					} else if (tf.readyToGraduate()) {
						if (tf.graduateUnits(worldMap())) {
							switchToPanel(new TrainingFacilityStatsPanel(tf), VIEW);
						} else {
							JOptionPane.showMessageDialog(null, "The building is being blocked by another unit");
						}
					} else {
						String[] opt = {"Yes", "No"};
						int resp = JOptionPane.showOptionDialog(null,
								"<html>One or more trainees have not yet reached level " + TrainingFacility.MAX_TRAINABLE_LEVEL + ",<br/>"
								+ "<html>or have not been assigned a class to begin training. Graduate anyway?<br/>",
								"confirm",
								JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
								null, opt, opt[0]);
						if (resp == 0) {
							if (tf.graduateUnits(worldMap())) {
								switchToPanel(new TrainingFacilityStatsPanel(tf), VIEW);
							} else {
								JOptionPane.showMessageDialog(null, "The building is being blocked by another unit");
							}
						}
					}
				}
			});
			choices.add(graduate);
			choices.add(new JPanel());
			
			JPanel armors = getInventoryDisplay(t.getArmors());
			JPanel armorInfo = new JPanel(new GridLayout(1, 3));
			armorInfo.add(new JLabel("Armor Name"));
			armorInfo.add(new JLabel("Amount"));
			armorInfo.add(new JLabel("Info"));
			JPanel armorsDisplay = new JPanel(new BorderLayout());
			armorsDisplay.add(new JScrollPane(armors));
			armorsDisplay.add(armorInfo, BorderLayout.NORTH);
			
			JPanel staves = getInventoryDisplay(t.getStaves());
			JPanel staffInfo = new JPanel(new GridLayout(1, 3));
			staffInfo.add(new JLabel("Staff Name"));
			staffInfo.add(new JLabel("Amount"));
			staffInfo.add(new JLabel("Info"));
			JPanel stavesDisplay = new JPanel(new BorderLayout());
			stavesDisplay.add(new JScrollPane(staves));
			stavesDisplay.add(staffInfo, BorderLayout.NORTH);
			
			JPanel mountsInfo = new JPanel(new GridLayout(1, 3));
			mountsInfo.add(new JLabel("Mount Type"));
			mountsInfo.add(new JLabel("Amount"));
			mountsInfo.add(new JLabel("Info"));
			JPanel mounts = new JPanel(new GridLayout(Mount.values().length, 3));
			for (int q = 0; q < Mount.values().length; q++) {
				final Mount m = Mount.values()[q];
				mounts.add(new JLabel(m.getDisplayName()));
				mounts.add(new JLabel("" + t.getMounts()[m.getId()]));
				JButton desc = new JButton("Info");
				desc.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						JOptionPane.showMessageDialog(null, m.getDescription());
					}
				});
				mounts.add(desc);
			}
			JPanel mountsDisplay = new JPanel(new BorderLayout());
			mountsDisplay.add(mountsInfo, BorderLayout.NORTH);
			mountsDisplay.add(new JScrollPane(mounts));
			
			JPanel partTwo = new JPanel(new GridLayout(1, 2));
			partTwo.add(traineesDisplay);
			partTwo.add(armorsDisplay);
			JPanel partThree = new JPanel(new GridLayout(1, 2));
			partThree.add(defendersDisplay);
			partThree.add(stavesDisplay);
			JPanel partFour = new JPanel(new GridLayout(1, 2));
			partFour.add(choices);
			partFour.add(mountsDisplay);
			
			JPanel fullDisplay = new JPanel(new GridLayout(4, 1));
			fullDisplay.add(top);
			fullDisplay.add(partTwo);
			fullDisplay.add(partThree);
			fullDisplay.add(partFour);
			
			add(fullDisplay);
			add(returnToWorldMap("WORLD MAP"), BorderLayout.SOUTH);
		}
		
		private JPanel outfitTraineesPanel() {
			JPanel fullDisplay = new JPanel(new GridLayout(1, 2));
			traineeToExamine = -1;
			
			final JPanel selectedUnit = new JPanel();
			final JPanel unitClasses = new JPanel();
			
			JPanel trainees = new JPanel(new GridLayout(tf.getTrainees().size(), 4));
			for (int q = 0; q < tf.getTrainees().size(); q++) {
				final int current = q;
				final Human h = tf.getTrainees().get(q);
				trainees.add(new JLabel(h.getName()));
				trainees.add(new JLabel(h.getUnitClassName()));
				trainees.add(new JLabel(h.getLevel() + " (" + h.getExperience() + ")"));
				JButton select = new JButton("Select");
				select.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						traineeToExamine = current;
						selectedUnit.removeAll();
						selectedUnit.setLayout(new BorderLayout());
						selectedUnit.add(unitPreview(h));
						selectedUnit.validate();
						selectedUnit.repaint();
						
						unitClasses.removeAll();
						unitClasses.setLayout(new BorderLayout());
						if (h.getUnitClass() == null) {
							List<UnitClass> potential = UnitClassIndex.getClassesForUnitType(UnitClassIndex.HUMAN);
							List<UnitClass> available =  new List<>(potential.size());
							for (int q = 0; q < potential.size(); q++) {
								UnitClass c = potential.get(q);
								if (c.getTier() == 1 && c.canTrainUnitWithMaterials(h, tf.getMounts())) {
									available.add(c);
								}
							}
							JPanel classes = new JPanel(new GridLayout(available.size(), 3));
							for (int q = 0; q < available.size(); q++) {
								final UnitClass c = available.get(q);
								classes.add(new JLabel(c.getName()));
								JButton info = new JButton("Info");
								info.addActionListener(new ActionListener() {
									@Override
									public void actionPerformed(ActionEvent e) {
										JOptionPane.showMessageDialog(null, "Here will be a description of " + c.getName());
									}
								});
								classes.add(info);
								JButton preview = new JButton("Preview Assignment");
								preview.addActionListener(new ActionListener() {
									@Override
									public void actionPerformed(ActionEvent e) {
										classToAssign = c;
										selectedUnit.removeAll();
										selectedUnit.setLayout(new BorderLayout());
										selectedUnit.add(unitPreview(h, c));
										JButton confirm = new JButton("Confirm Class Assignment");
										confirm.addActionListener(new ActionListener() {
											@Override
											public void actionPerformed(ActionEvent e) {
												h.assignClass(c);
												switchToPanel(outfitTraineesPanel(), VIEW);
											}
										});
										selectedUnit.add(confirm, BorderLayout.SOUTH);
										selectedUnit.validate();
										selectedUnit.repaint();
									}
								});
								classes.add(preview);
							}
							unitClasses.add(new JScrollPane(classes));
						}
						JButton autoClass = new JButton("Auto-Assign Class");
						autoClass.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								tf.autoAssignClass(traineeToExamine);
								switchToPanel(outfitTraineesPanel(), VIEW);
							}
						});
						
						JButton autoAllClasses = new JButton("Auto-Assign Classes for All");
						autoAllClasses.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								String[] opt = {"Yes", "No"};
								int resp = JOptionPane.showOptionDialog(null, "Auto-Assign Classes for all trainees? (this cannot be undone)", "confirm",
										JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
										null, opt, opt[0]);
								if (resp == 0) {
									tf.autoAssignClasses();
									switchToPanel(outfitTraineesPanel(), VIEW);
								}
							}
						});
						
						JPanel autos = new JPanel(new GridLayout(1, 2));
						autos.add(autoClass);
						autos.add(autoAllClasses);
						unitClasses.add(autos, BorderLayout.SOUTH);
						unitClasses.validate();
						unitClasses.repaint();
					}
				});
				trainees.add(select);
			}
			JPanel traineesInfo = new JPanel(new GridLayout(1, 4));
			traineesInfo.add(new JLabel("Name"));
			traineesInfo.add(new JLabel("Class"));
			traineesInfo.add(new JLabel("Level (Experience)"));
			traineesInfo.add(new JLabel("Select"));
			JPanel traineesDisplay = new JPanel(new BorderLayout());
			traineesDisplay.add(new JScrollPane(trainees));
			traineesDisplay.add(traineesInfo, BorderLayout.NORTH);
			
			JPanel weapons = getInventoryDisplay(tf.getMaterials());
			JPanel weaponSelect = new JPanel(new GridLayout(tf.getMaterials().size(), 1));
			for (int q = 0; q < tf.getMaterials().size(); q++) {
				final int current = q;
				JButton giveWep = new JButton("+");
				giveWep.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						if (traineeToExamine == -1) {
							JOptionPane.showMessageDialog(null, "No trainee is selected");
						} else {
							String weaponName = InventoryIndex.getElement(tf.getMaterials().get(current)).getName();
							if (tf.assignWeapon(traineeToExamine, current)) {
								JOptionPane.showMessageDialog(null, String.format("Gave %s the %s!",
										tf.getTrainees().get(traineeToExamine).getName(),
										weaponName));
								switchToPanel(outfitTraineesPanel(), VIEW);
							} else {
								JOptionPane.showMessageDialog(null, "Cannot give this item");
							}
						}
					}
				});
				weaponSelect.add(giveWep);
			}
			JPanel weaponsDisplay = new JPanel(new BorderLayout());
			weaponsDisplay.add(weapons);
			weaponsDisplay.add(weaponSelect, BorderLayout.EAST);
			JButton autoWeapon = new JButton("Auto-Assign Weapon");
			autoWeapon.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
				}
			});
			JButton autoWeaponAll = new JButton("Auto-Assign Weapons for All");
			autoWeaponAll.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					tf.autoAssignAllWeapons();
					switchToPanel(outfitTraineesPanel(), VIEW);
				}
			});
			JPanel autoWeaponOpts = new JPanel(new GridLayout(1, 2));
			autoWeaponOpts.add(autoWeapon);
			autoWeaponOpts.add(autoWeaponAll);
			JPanel fullWeaponsDisplay = new JPanel(new BorderLayout());
			fullWeaponsDisplay.add(new JScrollPane(weaponsDisplay));
			fullWeaponsDisplay.add(new JLabel("Weapons"), BorderLayout.NORTH);
			fullWeaponsDisplay.add(autoWeaponOpts, BorderLayout.SOUTH);
			fullWeaponsDisplay.add(autoWeaponOpts, BorderLayout.SOUTH);
			
			JPanel armors = getInventoryDisplay(tf.getArmors());
			JPanel armorSelect = new JPanel(new GridLayout(tf.getArmors().size(), 1));
			for (int q = 0; q < tf.getArmors().size(); q++) {
				final int current = q;
				JButton giveArm = new JButton("+");
				giveArm.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						if (traineeToExamine == -1) {
							JOptionPane.showMessageDialog(null, "No trainee is selected");
						} else {
							String armorName = InventoryIndex.getElement(tf.getMaterials().get(current)).getName();
							if (tf.assignArmor(traineeToExamine, current)) {
								JOptionPane.showMessageDialog(null, String.format("Gave %s the %s!",
										tf.getTrainees().get(traineeToExamine).getName(),
										armorName));
								switchToPanel(outfitTraineesPanel(), VIEW);
							} else {
								JOptionPane.showMessageDialog(null, "Cannot give this item");
							}
						}
					}
				});
				armorSelect.add(giveArm);
			}
			JPanel armorsDisplay = new JPanel(new BorderLayout());
			armorsDisplay.add(armors);
			armorsDisplay.add(armorSelect, BorderLayout.EAST);
			JButton autoArmor = new JButton("Auto-Assign Armor");
			autoArmor.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
				}
			});
			JButton autoArmorAll = new JButton("Auto-Assign Armor for All");
			autoArmorAll.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					tf.autoAssignAllArmors();
					switchToPanel(outfitTraineesPanel(), VIEW);
				}
			});
			JPanel autoArmorOpts = new JPanel(new GridLayout(1, 2));
			autoArmorOpts.add(autoArmor);
			autoArmorOpts.add(autoArmorAll);
			JPanel fullArmorsDisplay = new JPanel(new BorderLayout());
			fullArmorsDisplay.add(new JScrollPane(armorsDisplay));
			fullArmorsDisplay.add(new JLabel("Armors"), BorderLayout.NORTH);
			fullArmorsDisplay.add(autoArmorOpts, BorderLayout.SOUTH);
			
			JPanel staves = getInventoryDisplay(tf.getStaves());
			JPanel staffSelect = new JPanel(new GridLayout(tf.getStaves().size(), 1));
			for (int q = 0; q < tf.getStaves().size(); q++) {
				final int current = q;
				JButton giveStf = new JButton("+");
				giveStf.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						if (traineeToExamine == -1) {
							JOptionPane.showMessageDialog(null, "No trainee is selected");
						} else {
							String staffName = InventoryIndex.getElement(tf.getMaterials().get(current)).getName();
							if (tf.assignStaff(traineeToExamine, current)) {
								JOptionPane.showMessageDialog(null, String.format("Gave %s the %s!",
										tf.getTrainees().get(traineeToExamine).getName(),
										staffName));
								switchToPanel(outfitTraineesPanel(), VIEW);
							} else {
								JOptionPane.showMessageDialog(null, "Cannot give this item");
							}
						}
					}
				});
				staffSelect.add(giveStf);
			}
			JPanel stavesDisplay = new JPanel(new BorderLayout());
			stavesDisplay.add(staves);
			stavesDisplay.add(staffSelect, BorderLayout.EAST);
			JButton autoStaff = new JButton("Auto-Assign Staff");
			autoStaff.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
				}
			});
			JButton autoStaffAll = new JButton("Auto-Assign Staves for All");
			autoStaffAll.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					tf.autoAssignAllStaves();
					switchToPanel(outfitTraineesPanel(), VIEW);
				}
			});
			JPanel autoStaffOpts = new JPanel(new GridLayout(1, 2));
			autoStaffOpts.add(autoStaff);
			autoStaffOpts.add(autoStaffAll);
			JPanel fullStavesDisplay = new JPanel(new BorderLayout());
			fullStavesDisplay.add(new JScrollPane(stavesDisplay));
			fullStavesDisplay.add(new JLabel("Staves"), BorderLayout.NORTH);
			fullStavesDisplay.add(autoStaffOpts, BorderLayout.SOUTH);
			
			JPanel traineesView = new JPanel(new GridLayout(2, 1));
			traineesView.add(traineesDisplay);
			traineesView.add(selectedUnit);
			JPanel resourcesView = new JPanel(new GridLayout(4, 1));
			resourcesView.add(unitClasses);
			resourcesView.add(fullWeaponsDisplay);
			resourcesView.add(fullArmorsDisplay);
			resourcesView.add(fullStavesDisplay);
			
			fullDisplay.add(traineesView);
			fullDisplay.add(resourcesView);
			
			JButton back = enterBuildingButton(tf, "BACK");
			
			JPanel ret = new JPanel(new BorderLayout());
			ret.add(fullDisplay);
			ret.add(back, BorderLayout.SOUTH);
			
			return ret;
		}
		
	}
	private class FactoryStatsPanel extends JPanel {
		public FactoryStatsPanel(final Factory f) {
			//TODO
			setLayout(new BorderLayout());
			
			JLabel productsDisplay = new JLabel();
			if (f.getAssignment() == null) {
				productsDisplay.setText("Undelivered Products: None");
			} else {
				int[] productsArray = f.getProducts();
				Item products = InventoryIndex.getElement(productsArray);
				productsDisplay.setText(String.format("Undelivered Products: %d %s", productsArray[2], products.getName()));
			}
			
			JPanel fullDeliverProductsPanel = getDeliveryPanel(f);
			
			JPanel fullCurrentAssignmentDisplay = new JPanel(new BorderLayout());
			int[] assignment = f.getAssignment();
			if (assignment == null) {
				fullCurrentAssignmentDisplay.add(new JLabel("Assignment: None"));
			} else {
				JPanel currentAssignmentDisplay = new JPanel(new GridLayout(2, 1));
				Item item = InventoryIndex.getElement(assignment);
				if (f.isDeliveringContinuously()) {
					currentAssignmentDisplay.add(new JLabel(String.format("<html>Assignment:<br/>"
							+ "<html>Continually produce %s and deliver to %s<br/>",
							item.getName(), f.getCustomer().getNameAndType())));
				} else {
					currentAssignmentDisplay.add(new JLabel(String.format("<html>Assignment:<br/>"
							+ "<html>Produce %d %s, then deliver to %s<br/>",
							assignment[2], item.getName(), f.getCustomer().getNameAndType())));
				}
				JPanel recipe = new JPanel(new BorderLayout());
				recipe.add(new JLabel("Recipe for one " + item.getName()), BorderLayout.NORTH);
				recipe.add(new JScrollPane(getInventoryDisplay(((ManufacturableItem)item).getRecipe())));
				currentAssignmentDisplay.add(recipe);
				fullCurrentAssignmentDisplay.add(currentAssignmentDisplay);
			}
			
			JPanel fullMakeAssignmentDisplay = new JPanel(new BorderLayout());
			JPanel instr = new JPanel(new GridLayout(1, 5));
			instr.add(new JLabel("Set New Assignment:"));
			instr.add(new JLabel("Amount"));
			instr.add(new JLabel("Product"));
			instr.add(new JLabel("Recipient"));
			instr.add(new JLabel());
			fullMakeAssignmentDisplay.add(instr, BorderLayout.NORTH);
			if (f.getProducts()[2] == 0) {
				JPanel makeAssignment = new JPanel(new GridLayout(1, 5));
				
				final JCheckBox continuousProduction = new JCheckBox("<html>Produce<br/><html>Continuously<br/>");
				makeAssignment.add(continuousProduction);
				
				final JTextField amount = new JTextField();
				makeAssignment.add(amount);
				
				continuousProduction.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						amount.setEnabled(!continuousProduction.isSelected());
						amount.validate();
						amount.repaint();
					}
				});

				List<Item> possProd = f.possibleProducts();
				final Item[] prodOpts = new Item[possProd.size()];
				for (int q = 0; q < prodOpts.length; q++) {
					prodOpts[q] = possProd.get(q);
				}
				final JComboBox<Item> prodSelect = new JComboBox<>(prodOpts);
				makeAssignment.add(prodSelect);
				
				List<Building> recipients = f.possibleRecipientsOfItem((Item)prodSelect.getSelectedItem());
				Building[] destOpts = new Building[recipients.size()];
				for (int q = 0; q < destOpts.length; q++) {
					destOpts[q] = recipients.get(q);
				}
				final JComboBox<Building> destSelect = new JComboBox<>(destOpts);
				makeAssignment.add(destSelect);

				prodSelect.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						List<Building> recipients = f.possibleRecipientsOfItem((Item)prodSelect.getSelectedItem());
						destSelect.removeAllItems();
						for (int q = 0; q < recipients.size(); q++) {
							destSelect.addItem(recipients.get(q));
						}
						destSelect.validate();
						destSelect.repaint();
					}
				});
				
				JButton assign = new JButton("Assign");
				assign.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						if (!(continuousProduction.isSelected())
								&& (amount.getText() == null || amount.getText().equals(""))) {
							JOptionPane.showMessageDialog(null, "Must specify an amount");
							return;
						}
						boolean continuous = continuousProduction.isSelected();
						Item a = prodOpts[prodSelect.getSelectedIndex()];
						int[] prod;
						if (continuousProduction.isSelected()) {
							prod = new int[] {a.getGeneralItemId(), a.getSpecificItemId(), 0};
						} else {
							prod = new int[] {a.getGeneralItemId(), a.getSpecificItemId(),
									parseValidDigitWithinBounds(amount.getText(), 1, 10000, "Amount")};
						}
						Building dest = (Building)destSelect.getSelectedItem();
						f.giveAssignment(continuous, prod, dest);
						enterBuilding(f);
					}
				});
				makeAssignment.add(assign);
				
				fullMakeAssignmentDisplay.add(makeAssignment);
			} else {
				fullMakeAssignmentDisplay.add(new JLabel("Must ship current products before giving a new assignment"));
			}
			
			JPanel secondPart = new JPanel(new GridLayout(1, 2));
			secondPart.add(productsDisplay);
			secondPart.add(fullDeliverProductsPanel);
			JPanel thirdPart = new JPanel(new GridLayout(1, 2));
			thirdPart.add(fullCurrentAssignmentDisplay);
			thirdPart.add(fullMakeAssignmentDisplay);
			
			JPanel fullDisplay = new JPanel(new GridLayout(3, 1));
			fullDisplay.add(getBuildingBasicInfo(f));
			fullDisplay.add(secondPart);
			fullDisplay.add(thirdPart);

			add(fullDisplay);
			add(returnToWorldMap("WORLD MAP"), BorderLayout.SOUTH);
		}
	}
	private class FarmStatsPanel extends JPanel {
		public FarmStatsPanel(final Farm f) {
			setLayout(new BorderLayout());
			
			JLabel terrainDescription = new JLabel("Terrain: " + f.getTerrainType().getName()
					+ " (" + (Math.round(f.getTerrainType().getProliferability()) * 100) + "% farming effectiveness)");
			
			JPanel fullDeliverCropsPanel = getDeliveryPanel(f);
			
			int[] assignment = f.getAssignment();
			JLabel assignmentDisplay = new JLabel();
			if (assignment == null) {
				assignmentDisplay.setText("Current Assignment: None");
			} else {
				Item item = InventoryIndex.getElement(assignment);
				assignmentDisplay.setText(String.format("Current Assignment: %s - (%d left to produce)",
					item.getName(), assignment[2]));
			}
			
			JPanel fullMakeAssignmentPanel = new JPanel(new BorderLayout());
			fullMakeAssignmentPanel.add(new JLabel("Set New Assignment"), BorderLayout.NORTH);
			JPanel makeAssignment = new JPanel(new GridLayout(1, 3));
			final JTextField amount = new JTextField();
			makeAssignment.add(amount);
			final List<Item> possProds = f.possibleProducts();
			Item[] prodOpts = new Item[possProds.size()];
			for (int q = 0; q < possProds.size(); q++) {
				prodOpts[q] = possProds.get(q);
			}
			final JComboBox<Item> productionOptions = new JComboBox<>(prodOpts);
			makeAssignment.add(productionOptions);
			JButton assign = new JButton("Assign");
			assign.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (amount.getText() == null || amount.getText().equals("")) {
						JOptionPane.showMessageDialog(null, "Must specify an amount to produce");
						return;
					}
					Item chosenItem = possProds.get(productionOptions.getSelectedIndex());
					int quant = parseValidDigitWithinBounds(amount.getText(), 0, 10000, "Amount");
					int[] a = {chosenItem.getGeneralItemId(), chosenItem.getSpecificItemId(), quant};
					f.giveAssignment(a);
					JOptionPane.showMessageDialog(null,
							String.format("Now growing %d %s", quant, chosenItem.getName()));
					switchToPanel(new FarmStatsPanel(f), VIEW);
				}
			});
			makeAssignment.add(assign);
			fullMakeAssignmentPanel.add(makeAssignment);
			
			JPanel secondPart = new JPanel(new GridLayout(1, 2));
			secondPart.add(terrainDescription);
			secondPart.add(fullDeliverCropsPanel);
			JPanel thirdPart = new JPanel(new GridLayout(1, 2));
			thirdPart.add(assignmentDisplay);
			thirdPart.add(fullMakeAssignmentPanel);
			
			JPanel fullDisplay = new JPanel(new GridLayout(3, 1));
			fullDisplay.add(getBuildingBasicInfo(f));
			fullDisplay.add(secondPart);
			fullDisplay.add(thirdPart);

			add(fullDisplay);
			add(returnToWorldMap("WORLD MAP"), BorderLayout.SOUTH);
		}
	}
	private class MagicProcessingFacilityStatsPanel extends JPanel {
		public MagicProcessingFacilityStatsPanel(MagicProcessingFacility m) {
			//TODO
			setLayout(new BorderLayout());
			add(new JLabel("TODO"));
			add(returnToWorldMap("WORLD MAP"), BorderLayout.SOUTH);
		}
	}
	private class MiningFacilityStatsPanel extends JPanel {
		public MiningFacilityStatsPanel(final MiningFacility m) {
			//TODO
			setLayout(new BorderLayout());
			
			JLabel terrainDescription = new JLabel("Terrain: " + m.getTerrainType().getName()
					+ " (" + (m.getTerrainType().getMinability() * 10) + "% mining effectiveness)");
			
			JPanel fullDeliverMaterialsPanel = getDeliveryPanel(m);
			
			List<int[]> productsCount = m.getMaterials();
			
			JPanel fullProductsPanel = new JPanel(new BorderLayout());
			fullProductsPanel.add(new JLabel("Products"), BorderLayout.NORTH);
			JPanel productsDisplay = new JPanel(new GridLayout(productsCount.size(), 2));
			for (int q = 0; q < productsCount.size(); q++) {
				Item item = InventoryIndex.getElement(new int[] {InventoryIndex.RESOURCE, q});
				productsDisplay.add(new JLabel(item.getName() + ":"));
				productsDisplay.add(new JLabel("" + productsCount.get(q)[2]));
			}
			fullProductsPanel.add(productsDisplay);
			
			JPanel fullMineOptionsPanel = new JPanel(new BorderLayout());
			fullMineOptionsPanel.add(new JLabel("What To Mine"), BorderLayout.NORTH);
			JPanel assignmentDisplay = new JPanel(new GridLayout(productsCount.size(), 3));
			for (int q = 0; q < productsCount.size(); q++) {
				final int idx = q;
				Item item = InventoryIndex.getElement(new int[] {InventoryIndex.RESOURCE, idx});
				assignmentDisplay.add(new JLabel(item.getName()));
				if (m.isProducing(idx)) {
					assignmentDisplay.add(new JLabel("Yes"));
				} else {
					assignmentDisplay.add(new JLabel("No"));
				}
				if (((Resource)item).canBeFoundHere(m.getTerrainType())) {
					JButton toggle = new JButton("Toggle");
					toggle.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							m.toggleProduction(idx);
							switchToPanel(new MiningFacilityStatsPanel(m), VIEW);
						}
					});
					assignmentDisplay.add(toggle);
				} else {
					assignmentDisplay.add(new JLabel("Not found in this environment"));
				}
			}
			fullMineOptionsPanel.add(assignmentDisplay);
			
			JPanel secondPart = new JPanel(new GridLayout(1, 2));
			secondPart.add(terrainDescription);
			secondPart.add(fullDeliverMaterialsPanel);
			JPanel thirdPart = new JPanel(new GridLayout(1, 2));
			thirdPart.add(fullProductsPanel);
			thirdPart.add(fullMineOptionsPanel);
			
			JPanel fullDisplay = new JPanel(new GridLayout(3, 1));
			fullDisplay.add(getBuildingBasicInfo(m));
			fullDisplay.add(secondPart);
			fullDisplay.add(thirdPart);

			add(fullDisplay);
			add(returnToWorldMap("WORLD MAP"), BorderLayout.SOUTH);
		}
	}
	private class RanchStatsPanel extends JPanel {
		public RanchStatsPanel(final Ranch r) {
			setLayout(new BorderLayout());
			
			JLabel terrainDescription = new JLabel();
			if (r.getNumAnimals() > 0) {
				terrainDescription.setText(String.format("Currently holding %d %s",
						r.getNumAnimals(), Mount.values()[r.getAssignment()[0]].getDisplayName()));
			} else {
				terrainDescription.setText("No animals held");
			}
			
			JPanel fullDeliverAnimalsPanel = getDeliveryPanel(r);
			
			int[] assignment = r.getAssignment();
			JLabel assignmentDisplay = new JLabel();
			if (assignment == null) {
				assignmentDisplay.setText("Current Assignment: None");
			} else {
				Mount m = Mount.values()[assignment[0]];
				assignmentDisplay.setText(String.format("Current Assignment: %s - %d/%d grown",
					m.getDisplayName(), r.getNumAnimals(), assignment[1]));
			}
			
			JPanel fullMakeAssignmentPanel = new JPanel(new BorderLayout());
			fullMakeAssignmentPanel.add(new JLabel("Set New Assignment"), BorderLayout.NORTH);
			if (r.getNumAnimals() == 0) {
				JPanel makeAssignment = new JPanel(new GridLayout(1, 3));
				final JTextField amount = new JTextField();
				makeAssignment.add(amount);
				Mount[] mountTypes = Mount.values();
				String[] prodOpts = new String[mountTypes.length];
				for (int q = 0; q < prodOpts.length; q++) {
					prodOpts[q] = mountTypes[q].getDisplayName();
				}
				final JComboBox<String> productionOptions = new JComboBox<>(prodOpts);
				makeAssignment.add(productionOptions);
				JButton assign = new JButton("Assign");
				assign.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						if (amount.getText() == null || amount.getText().equals("")) {
							JOptionPane.showMessageDialog(null, "Must specify an amount to produce");
							return;
						}
						int type = productionOptions.getSelectedIndex();
						int quant = parseValidDigitWithinBounds(amount.getText(), 0, 10000, "Amount");
						r.giveAssignment(type, quant);
						JOptionPane.showMessageDialog(null,
								String.format("Now growing %d %s", quant, Mount.values()[type].getDisplayName()));
						switchToPanel(new RanchStatsPanel(r), VIEW);
					}
				});
				makeAssignment.add(assign);
				fullMakeAssignmentPanel.add(makeAssignment);
			} else {
				fullMakeAssignmentPanel.add(new JLabel("Must ship current animals before making a new assignment"));
			}
			
			JPanel secondPart = new JPanel(new GridLayout(1, 2));
			secondPart.add(terrainDescription);
			secondPart.add(fullDeliverAnimalsPanel);
			JPanel thirdPart = new JPanel(new GridLayout(1, 2));
			thirdPart.add(assignmentDisplay);
			thirdPart.add(fullMakeAssignmentPanel);
			
			JPanel fullDisplay = new JPanel(new GridLayout(3, 1));
			fullDisplay.add(getBuildingBasicInfo(r));
			fullDisplay.add(secondPart);
			fullDisplay.add(thirdPart);

			add(fullDisplay);
			add(returnToWorldMap("WORLD MAP"), BorderLayout.SOUTH);
		}
	}
	private class StorehouseStatsPanel extends JPanel {
		public StorehouseStatsPanel(Storehouse s) {
			//TODO
			setLayout(new BorderLayout());
			add(new JLabel("TODO"));
			add(returnToWorldMap("WORLD MAP"), BorderLayout.SOUTH);
		}
	}
	private class TradeCenterStatsPanel extends JPanel {
		public TradeCenterStatsPanel(TradeCenter t) {
			//TODO
			setLayout(new BorderLayout());
			add(new JLabel("TODO"));
			add(returnToWorldMap("WORLD MAP"), BorderLayout.SOUTH);
		}
	}
	
	private JPanel getBuildingBasicInfo(final Building b) {
		JPanel stats = new JPanel(new GridLayout(4, 1));
		stats.add(new JLabel(b.getNameAndType()));
		stats.add(new JLabel(String.format("HP: %d/%d", b.getCurrentHP(), b.getMaximumHP())));
		stats.add(new JLabel(String.format("Building Defense: %d", b.getDurability())));
		stats.add(new JLabel(String.format("Building Magic Resistance: %d", b.getResistance())));
		
		JPanel owner = new JPanel(new BorderLayout());
		if (b.getOwner() == null) {
			owner.add(new JLabel("No Owner"));
		} else if (!(b.getOwner().isAlive())) {
			//TODO remove owner
			owner.add(new JLabel("No Owner"));
		} else {
			owner.add(new JLabel("Owner: " + b.getOwner().getDisplayName()), BorderLayout.NORTH);
			owner.add(makePortrait(b.getOwner()));
			JButton examine = new JButton("View Character");
			examine.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					JButton back = enterBuildingButton(b, "BACK");
					switchToPanel(new UnitStatsPanel(b.getOwner(), back), VIEW);
				}
			});
			owner.add(examine, BorderLayout.SOUTH);
		}
		
		JPanel inventory = getInventoryDisplay(b.getMaterials());
		JPanel inventoryInfo = new JPanel(new GridLayout(1, 3));
		inventoryInfo.add(new JLabel("Item Name"));
		inventoryInfo.add(new JLabel("Amount"));
		inventoryInfo.add(new JLabel("Info"));
		JPanel inventoryDisplay = new JPanel(new BorderLayout());
		inventoryDisplay.add(new JScrollPane(inventory));
		inventoryDisplay.add(inventoryInfo, BorderLayout.NORTH);

		
		JPanel ret = new JPanel(new GridLayout(1, 3));
		ret.add(stats);
		ret.add(owner);
		ret.add(inventoryDisplay);
		
		return ret;
	}
	private JPanel getInventoryDisplay(List<int[]> inv) {
		JPanel inventory = new JPanel(new GridLayout(inv.size(), 3));
		for (int q = 0 ; q < inv.size(); q++) {
			int[] i = inv.get(q);
			final Item item = InventoryIndex.getElement(i);
			inventory.add(new JLabel(item.getName()));
			inventory.add(new JLabel("" + i[2]));
			JButton info = new JButton("Info");
			info.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					JOptionPane.showMessageDialog(null, "Here will be a description of " + item.getName());
				}
			});
			inventory.add(info);
		}
		return inventory;
	}
	private JPanel getInventoryDisplay(int[][] inv) {
		JPanel inventory = new JPanel(new GridLayout(inv.length, 3));
		for (int q = 0 ; q < inv.length; q++) {
			int[] i = inv[q];
			final Item item = InventoryIndex.getElement(i);
			inventory.add(new JLabel(item.getName()));
			inventory.add(new JLabel("" + i[2]));
			JButton info = new JButton("Info");
			info.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					JOptionPane.showMessageDialog(null, "Here will be a description of " + item.getName());
				}
			});
			inventory.add(info);
		}
		return inventory;
	}
	private JPanel getAssignedUnitsDisplay(final Assignable a) {
		JPanel info = new JPanel(new GridLayout(1, 4));
		info.add(new JLabel("Name"));
		info.add(new JLabel("Class"));
		info.add(new JLabel("Level (Experience)"));
		info.add(new JLabel("View"));
		JPanel defendersDisplay = new JPanel(new BorderLayout());
		if (a.getAssignedGroup() == null) {
			//Nothing
		} else {
			JPanel defenders = new JPanel(new GridLayout(a.getAssignedGroup().getMembers().size(), 4));
			for (int q = 0; q < a.getAssignedGroup().size(); q++) {
				final Unit u = a.getAssignedGroup().get(q);
				defenders.add(new JLabel(u.getDisplayName()));
				defenders.add(new JLabel(u.getUnitClassName()));
				defenders.add(new JLabel(String.format("%d (%d)", u.getLevel(), u.getExperience())));
				JButton examine = new JButton("View Character");
				examine.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						JButton back = new JButton("BACK");
						back.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								switchToAssignablePanel(a);
							}
						});
						switchToPanel(new UnitStatsPanel(u, back), VIEW);
					}
				});
				defenders.add(examine);
			}
			defendersDisplay.add(info, BorderLayout.NORTH);
			defendersDisplay.add(new JScrollPane(defenders));
		}
		
		JPanel header = new JPanel(new GridLayout(1, 2));
		header.add(new JLabel("Assigned Team"));
		JButton viewGroup = new JButton("View Team");
		viewGroup.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (a.getAssignedGroup() == null) {
					JOptionPane.showMessageDialog(null, "No team is assigned");
				} else {
					JButton back = new JButton("BACK");
					back.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							switchToAssignablePanel(a);
						}
					});
					switchToPanel(new UnitGroupStatsPanel(a.getAssignedGroup(), back), VIEW);
				}
			}
		});
		header.add(viewGroup);
		
		JButton positionDefenders = new JButton("Position Defending Team");
		positionDefenders.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
			}
		});
		JButton positionWeapons = new JButton("Position StationaryWeapons");
		positionWeapons.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
			}
		});
		JButton dismiss = new JButton("Dismiss");
		dismiss.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (a.getAssignedGroup() != null) {
					a.dismissAssignedGroup();
					switchToAssignablePanel(a);
				}
			}
		});
		
		JPanel options = new JPanel(new GridLayout(1, 3));
		options.add(positionDefenders);
		options.add(positionWeapons);
		options.add(dismiss);
		
		JPanel ret = new JPanel(new BorderLayout());
		ret.add(header, BorderLayout.NORTH);
		ret.add(defendersDisplay);
		ret.add(options, BorderLayout.SOUTH);
		return ret;
	}
	private JPanel getDeliveryPanel(final GoodsDeliverer g) {
		JPanel fullDeliveryPanel = new JPanel(new BorderLayout());
		fullDeliveryPanel.add(new JLabel("Deliver Products"), BorderLayout.NORTH);
		JPanel sendProductsPanel = new JPanel(new GridLayout(1, 3));
		sendProductsPanel.add(new JLabel("Send products to:"));
		final List<Building> rec = g.possibleRecipients();
		Building[] opts = new Building[rec.size()];
		for (int q = 0; q < rec.size(); q++) {
			opts[q] = rec.get(q);
		}
		final JComboBox<Building> destinationSelection = new JComboBox<>(opts);
		sendProductsPanel.add(destinationSelection);
		JButton sendProducts = new JButton("Send");
		sendProducts.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (destinationSelection.getItemCount() == 0) {
					JOptionPane.showMessageDialog(null, "No building in this city to receive these crops");
					return;
				}
				Building recipient = rec.get(destinationSelection.getSelectedIndex());
				g.deliverGoods(recipient);
				enterBuilding(g);
			}
		});
		sendProductsPanel.add(sendProducts);
		fullDeliveryPanel.add(sendProductsPanel);
		return fullDeliveryPanel;
	}
	private JButton returnToWorldMap(String title) {
		JButton ret = new JButton(title);
		ret.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				switchToWorldMap();
			}
		});
		return ret;
	}
	
	private JButton enterBuildingButton(final Building b, String buttonName) {
		JButton ret = new JButton(buttonName);
		ret.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				enterBuilding(b);
			}
		});
		return ret;
	}
	private void enterBuilding(Building b) {
		if (b instanceof Coliseum) {
			switchToPanel(new ColiseumStatsPanel((Coliseum)b), VIEW);
		} else if (b instanceof Hospital) {
			switchToPanel(new HospitalStatsPanel((Hospital)b), VIEW);
		} else if (b instanceof Port) {
			switchToPanel(new PortStatsPanel((Port)b), VIEW);
		} else if (b instanceof ResearchCenter) {
			switchToPanel(new ResearchCenterStatsPanel((ResearchCenter)b), VIEW);
		} else if (b instanceof Shipyard) {
			switchToPanel(new ShipyardStatsPanel((Shipyard)b), VIEW);
		} else if (b instanceof Village) {
			switchToPanel(new VillageStatsPanel((Village)b), VIEW);
		} else if (b instanceof WarpPad) {
			//TODO
		} else if (b instanceof Barracks) {
			switchToPanel(new BarracksStatsPanel((Barracks)b), VIEW);
		} else if (b instanceof Castle) {
			switchToPanel(new CastleStatsPanel((Castle)b), VIEW);
		} else if (b instanceof Fortress) {
			switchToPanel(new FortressStatsPanel((Fortress)b), VIEW);
		} else if (b instanceof Prison) {
			switchToPanel(new PrisonStatsPanel((Prison)b), VIEW);
		} else if (b instanceof TrainingFacility) {
			switchToPanel(new TrainingFacilityStatsPanel((TrainingFacility)b), VIEW);
		} else if (b instanceof Factory) {
			switchToPanel(new FactoryStatsPanel((Factory)b), VIEW);
		} else if (b instanceof Farm) {
			switchToPanel(new FarmStatsPanel((Farm)b), VIEW);
		} else if (b instanceof MagicProcessingFacility) {
			switchToPanel(new MagicProcessingFacilityStatsPanel((MagicProcessingFacility)b), VIEW);
		} else if (b instanceof MiningFacility) {
			switchToPanel(new MiningFacilityStatsPanel((MiningFacility)b), VIEW);
		} else if (b instanceof Ranch) {
			switchToPanel(new RanchStatsPanel((Ranch)b), VIEW);
		} else if (b instanceof Storehouse) {
			switchToPanel(new StorehouseStatsPanel((Storehouse)b), VIEW);
		} else if (b instanceof TradeCenter) {
			switchToPanel(new TradeCenterStatsPanel((TradeCenter)b), VIEW);
		}
	}
	
	private void switchToAssignablePanel(Assignable a) {
		if (a instanceof Ship) {
			switchToPanel(new ShipStatsPanel((Ship)a), VIEW);
		} else if (a instanceof Barracks) {
			switchToPanel(new BarracksStatsPanel((Barracks)a), VIEW);
		} else if (a instanceof Castle) {
			switchToPanel(new CastleStatsPanel((Castle)a), VIEW);
		} else if (a instanceof Fortress) {
			switchToPanel(new FortressStatsPanel((Fortress)a), VIEW);
		} else if (a instanceof Prison) {
			switchToPanel(new PrisonStatsPanel((Prison)a), VIEW);
		} else if (a instanceof TrainingFacility) {
			switchToPanel(new TrainingFacilityStatsPanel((TrainingFacility)a), VIEW);
		}
	}
	
	private void switchToPanel(JPanel panel, String name) {
		p.add(panel, name);
		cl.show(p, name);
		validate();
		repaint();
	}
	
	private void switchToPanel(String name) {
		cl.show(p, name);
		validate();
		repaint();
	}
	
	private void showEdit(Component c) {
		c.validate();
		c.repaint();
	}
	
	private void switchToWorldMap() {
		worldMapPanel.refresh();
		worldMapPanel.validate();
		switchToPanel(MAP);
	}
	
	private JButton backButton(final JPanel back) {
		JButton ret = new JButton("BACK");
		ret.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				switchToPanel(back, VIEW);
			}
		});
		return ret;
	}
	
	private boolean confirmChoiceDialog(String message) {
		String[] opts = new String[] {"No", "Yes"};
		int decision = JOptionPane.showOptionDialog(null, message, "CONFIRM", JOptionPane.DEFAULT_OPTION,
				JOptionPane.PLAIN_MESSAGE, null, opts, opts[0]);
		return decision == 1;
	}
	
	private Nation playerNation() {
		return GeneralGameplayManager.getPlayerNation();
	}
	private Human player() {
		return GeneralGameplayManager.getPlayer();
	}
	private WorldMap worldMap() {
		return GeneralGameplayManager.getWorldMap();
	}
	private String validateInputName(String s, String field) {
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
	private int parseValidDigitWithinBounds(String s, int min, int max, String field)
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
