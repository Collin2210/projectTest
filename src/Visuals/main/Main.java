package Visuals.main;

import Controller.*;
import Visuals.GUI.GUIRenderer;
import Visuals.GUI.GUITexture;
import Visuals.engine.graphics.Loader;
import Visuals.engine.graphics.MasterRenderer;
import Visuals.engine.graphics.models.RawModel;
import Visuals.engine.graphics.models.TexturedModel;
import Visuals.engine.graphics.textures.ModelTexture;
import Visuals.engine.graphics.textures.TerrainTexture;
import Visuals.engine.graphics.textures.TerrainTexturePack;
import Visuals.engine.graphics.textures.objConverter.ModelData;
import Visuals.engine.graphics.textures.objConverter.OBJFileLoader;
import Visuals.engine.io.Input;
import Visuals.engine.io.Window;
import Visuals.entities.Camera;
import Visuals.entities.Entity;
import Visuals.entities.Light;
import Visuals.entities.Player;
import Visuals.normalMappingObjConverter.NormalMappedObjLoader;
import Visuals.particles.Particle;
import Visuals.particles.ParticleBrain;
import Visuals.particles.ParticleGenerator;
import Visuals.particles.ParticleTexture;
import base.GameController;
import base.GameEndChecker;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL46;
import org.lwjglx.util.vector.Vector2f;
import org.lwjglx.util.vector.Vector3f;
import org.lwjglx.util.vector.Vector4f;
import Visuals.terrain.Terrain;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.newdawn.slick.opengl.renderer.SGL.*;
import static org.newdawn.slick.opengl.renderer.SGL.GL_ONE;

public class Main implements Runnable {

	public Thread game;
	public Window window;
	public static final int WIDTH = 1600, HEIGHT = 900;
	public int i = 0;

	public Loader loader = new Loader();

	public RawModel guardModel;
	public RawModel intruderModel;
	public RawModel wallModel;
	public RawModel portalModel;
	public RawModel traceModel;
	public RawModel goalModel;
	public RawModel towerModel;

	public RawModel grassModel;

	public ModelData modelDataGuard;
	public ModelData modelDataIntruder;
	public ModelData modelDataWall;
	public ModelData modelDataPortal;
	public ModelData modelDataTrace;
	public ModelData modelDataGoal;
	public ModelData modelDataTower;
	public ModelData modelDataGrass;

	public ModelTexture textureGuard;
	public ModelTexture textureIntruder;
	public ModelTexture textureWall;
	public ModelTexture texturePortal;
	public ModelTexture textureTrace;
	public ModelTexture textureGoal;
	public ModelTexture textureGrass;

	public TexturedModel texturedModelGuard;
	public TexturedModel texturedModelIntruder;
	public TexturedModel texturedModelWall;
	public TexturedModel texturedModelPortal;
	public TexturedModel texturedModelTrace;
	public TexturedModel texturedModelgoal;
	public TexturedModel texturedModelTower;
	public TexturedModel texturedModelGrass;

	public MasterRenderer renderer;

	public Player guard;
	public Player intruder;
	public Player camPlayer;
	public Entity goal;

	public Camera camera;

	public List<Light> lights;
	public List<Entity> entities = new ArrayList<>();
	public List<Player> players = new ArrayList<>();
	public List<Entity> normalMappedEntities = new ArrayList<>();
	public static Terrain terrain;

	public TerrainTexture backgroundTexture;
	public TerrainTexture rTexture;
	public TerrainTexture gTexture;
	public TerrainTexture bTexture;
	public TerrainTexturePack texturePack;
	public TerrainTexture blendMap;

	private static final float RED = 0.5f;
	private static final float GREEN = 0.5f;
	private static final float BLUE = 0.5f;


	public List<GUITexture> guis;
	public GUITexture gui;
	public GUIRenderer guiRenderer;


	GameController g = new GameController();

	public static String testMapPath;

	public boolean generateMaze = true;
	public int[][] mazeMatrix;
	public int pathIndex = 0;
	public Input input;
	public long lastClick;
	public static int L = (int) (Terrain.SIZE/2);

	public int moveIndex = 0;

	public int moveCount = 0;

	public boolean mainMenuBoolean = false;

	public ArrayList<ArrayList<int[]>> listOfMovesEveryAgent;

	ParticleGenerator particleGen;


	public void start() {
		game = new Thread(this, "Simulation");
		game.start();
	}

	public void init() throws Exception {
		window = new Window(WIDTH, HEIGHT, "Simulation");
		window.setBackgroundColor(RED, GREEN, BLUE);
		window.create();




		renderer = new MasterRenderer(loader, camera);
		ParticleBrain.init(loader,renderer.getProjectionMatrix());
		guiRenderer = new GUIRenderer(loader);
		testMapPath = "res/testmap.txt";

		// create and render small GUI in top right position if your ball landed in the hole
		guis = new ArrayList<>();
		gui = new GUITexture(loader.loadTexture("mainMenu"), new Vector2f(0.25f, 0.25f), new Vector2f(1f, 1f));
		GUITexture shadowMap = new GUITexture(renderer.getShadowMapTexturex(), new Vector2f(0.5f,0.5f), new Vector2f(0.5f,0.5f));
		guis.add(shadowMap);
		guis.add(gui);
		guiRenderer = new GUIRenderer(loader);


		//parser.readFile(testMapPath)

		// Loading in an object:
		// * step 1: get .obj file (from ../res/3D/)

		modelDataGuard = OBJFileLoader.loadOBJ("untitled");
		modelDataIntruder = OBJFileLoader.loadOBJ("mira");
		modelDataWall = OBJFileLoader.loadOBJ("remove");
		modelDataGoal = OBJFileLoader.loadOBJ("chest");
		modelDataTower = OBJFileLoader.loadOBJ("remove");
		modelDataGrass = OBJFileLoader.loadOBJ("tallgrass");


		// * step 2: load the model data

		guardModel = loader.loadToVAO(modelDataGuard.getVertices(), modelDataGuard.getTextureCoords(), modelDataGuard.getNormals(), modelDataGuard.getIndices());
		intruderModel = loader.loadToVAO(modelDataIntruder.getVertices(), modelDataIntruder.getTextureCoords(), modelDataIntruder.getNormals(), modelDataIntruder.getIndices());
		wallModel = loader.loadToVAO(modelDataWall.getVertices(), modelDataWall.getTextureCoords(), modelDataWall.getNormals(), modelDataWall.getIndices());
		goalModel = loader.loadToVAO(modelDataGoal.getVertices(), modelDataGoal.getTextureCoords(), modelDataGoal.getNormals(), modelDataGoal.getIndices());
		towerModel = loader.loadToVAO(modelDataTower.getVertices(), modelDataTower.getTextureCoords(), modelDataTower.getNormals(), modelDataTower.getIndices());
		grassModel = loader.loadToVAO(modelDataGrass.getVertices(), modelDataGrass.getTextureCoords(), modelDataGrass.getNormals(), modelDataGrass.getIndices());

		// create texture for terrain
		backgroundTexture = new TerrainTexture(loader.loadTexture("grasspng"));
		rTexture = new TerrainTexture(loader.loadTexture("dirt"));
		gTexture = new TerrainTexture(loader.loadTexture("pinkFlowers"));
		bTexture = new TerrainTexture(loader.loadTexture("path"));
		texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
		blendMap = new TerrainTexture(loader.loadTexture("blendMap"));

		// * step 3: Add textures and models together.

		texturedModelGuard = new TexturedModel(guardModel, new ModelTexture(loader.loadTexture("floatrobotgreen")));
		texturedModelIntruder = new TexturedModel(intruderModel, new ModelTexture(loader.loadTexture("miraTexture")));
		texturedModelPortal = new TexturedModel(portalModel, new ModelTexture(loader.loadTexture("portalcolor")));
		texturedModelgoal = new TexturedModel(goalModel, new ModelTexture(loader.loadTexture("chest")));
		texturedModelWall = new TexturedModel(wallModel, new ModelTexture(loader.loadTextureTransparent("stoneWall3")));
		texturedModelTower = new TexturedModel(towerModel, new ModelTexture(loader.loadTexture("chest")));
		texturedModelGrass = new TexturedModel(grassModel, new ModelTexture(loader.loadTexture("green_alpha_tall_grass")));


		TexturedModel vulcanNormalModel = new TexturedModel(NormalMappedObjLoader.loadOBJ("vulcan", loader), new ModelTexture(loader.loadTexture("vulcantext")));
		vulcanNormalModel.getTexture().setNormalMap(loader.loadTexture("vulcannormal"));

		//normalMappedEntities.add(new Entity(vulcanNormalModel, new Vector3f(L+15,0,L+15),0,0,0,1.0f, 1));

		TexturedModel teleportNormalModel = new TexturedModel(NormalMappedObjLoader.loadOBJ("teleExit", loader), new ModelTexture(loader.loadTexture("portalcolor")));
		teleportNormalModel.getTexture().setNormalMap(loader.loadTexture("portalNormal"));
		teleportNormalModel.getTexture().setShineDamper(10);
		teleportNormalModel.getTexture().setReflectivity(0.5f);




		//normalMappedEntities.add(new Entity(barrelModel, new Vector3f(L+15,10,L+15),0,0,0,1.0f, 1));

		textureGuard = texturedModelGuard.getTexture();
		textureGuard.setShineDamper(5);
		textureGuard.setReflectivity((float) 0.5);


		textureIntruder = texturedModelIntruder.getTexture();
		textureIntruder.setShineDamper(5);
		textureIntruder.setReflectivity((float) 0.5);

		input = new Input();

		// game
		g.startGame();

		GameController.print();

		//g.moveAgentDumbly();
		g.makeAgentsLearn();
		System.out.println("guards won " + GameController.numOfGuardWins);
		System.out.println("intruders won " + GameController.numOfIntruderWins);
		g.makeAgentsMoveSmartly();
		listOfMovesEveryAgent = GameController.pathOfAllAgents;
		terrain = new Terrain(0, 0, loader, texturePack, blendMap, "heightMap", "grassnormal");
		ArrayList<Entity> walls = createWallsFromFile();

//
//		for (int x = -1; x < GameController.variables.getHeight(); x++) {
//			walls.add(new Entity(texturedModelWall, new Vector3f(x + L, 0, -1 + L), 0, 90, 0, 1, 1));
//		}
//		for (int x = -1; x < GameController.variables.getHeight(); x++) {
//			walls.add(new Entity(texturedModelWall, new Vector3f(x + L, 0, GameController.variables.getWidth() + L), 0, 90, 0, 1, 1));
//		}
//		for (int y = -1; y < GameController.variables.getWidth(); y++) {
//			walls.add(new Entity(texturedModelWall, new Vector3f(-1 + L, 0, y + L), 0, 90, 0, 1, 1));
//		}
//		for (int y = -1; y < GameController.variables.getWidth(); y++) {
//			walls.add(new Entity(texturedModelWall, new Vector3f(GameController.variables.getHeight() + L, 0, y + L), 0, 90, 0, 1, 1));
//		}




		for (Teleport t : GameController.variables.getPortals()) {
			ArrayList<int[]> entrances = t.getPointsIn();
			int[] destination = t.getPointOut();
			float telePosX = (t.getCoordIn().get(0) + t.getCoordIn().get(2)) / 2;
			float telePosY = (t.getCoordIn().get(1) + t.getCoordIn().get(3)) / 2;
			double angle = t.getDegreeOut();
			normalMappedEntities.add(new Entity(teleportNormalModel, new Vector3f(telePosX + L, terrain.getHeightOfTerrain(telePosX+L, telePosY+L), telePosY + L), (float) 0, (float) angle, (float) 0, 4, 1));

			/*for (int[] telePos:entrances) {
				entities.add(new Entity(texturedModelPortal, new Vector3f(telePos[0]+L,0.2f,telePos[1]+L), (float) 0, (float) angle, (float) 0, .3F,1));
			}

			 */

			normalMappedEntities.add(new Entity(teleportNormalModel, new Vector3f(destination[0] + L, terrain.getHeightOfTerrain(telePosX+L, telePosY+L), destination[1] + L), (float) 0, (float) angle, 0, .5F, 1));


		}

		for (int j = 0; j < 500; j++) {
			int randomX = ThreadLocalRandom.current().nextInt(0, 50 + 1);
			int randomY = ThreadLocalRandom.current().nextInt(0, 50 + 1);
			int randomRotation = ThreadLocalRandom.current().nextInt(0, 360 + 1);
			entities.add(new Entity(texturedModelGrass, new Vector3f(L + randomX, terrain.getHeightOfTerrain(L + randomX, L + randomY), randomY + L), 0, randomRotation, 0, 1, 1));
		}


		for (Tile t : GameController.goalTiles) {
			entities.add(new Entity(texturedModelgoal, new Vector3f(t.getXCoord() + L, terrain.getHeightOfTerrain(t.getXCoord()+L, t.getYCoord()+L), t.getYCoord() + L), (float) 0, 90, 0, 1, 1));
		}


		// game


		// generate terrain


		// generate light
		lights = new ArrayList<>();
		lights.add(new Light(new Vector3f(1000000 + L, 1000000, 300000 + L), new Vector3f(1f, 1f, 1f)));


		// generate players
		// * step 4: Generate entities or players.

		for (int i = 0; i < GameController.pathOfAllAgents.size(); i++) {
			if (i < GameController.variables.getNumberOfIntruders()) {
				ArrayList<int[]> pathIntruder = GameController.pathOfAllAgents.get(i);

				players.add(new Player(texturedModelIntruder, new Vector3f(pathIntruder.get(0)[0] + L, terrain.getHeightOfTerrain(pathIntruder.get(0)[0] + L, pathIntruder.get(0)[1] + L), pathIntruder.get(0)[1] + L), 0, 90, 0, 1, i));
//				System.out.println("added intruder at: " + pathIntruder.get(0)[0]+ ", "+ pathIntruder.get(0)[1]) ;
			} else if (i >= GameController.variables.getNumberOfIntruders()) {
				ArrayList<int[]> pathGuard = GameController.pathOfAllAgents.get(i);
				players.add(new Player(texturedModelGuard, new Vector3f(pathGuard.get(0)[0] + L, terrain.getHeightOfTerrain(pathGuard.get(0)[0] + L, pathGuard.get(0)[1] + L), pathGuard.get(0)[1] + L), 0, 90, 0, 1, i));
//				System.out.println("added guard at: " + pathGuard.get(0)[0]+ ", "+ pathGuard.get(0)[1]) ;
			}
		}


		//intruder = new Player(texturedModelGuard, new Vector3f(L,0,L),0,90,0,1,1);  //portal
		//guard = new Player(texturedModelGuard, new Vector3f(variables.getSpawnGuard().x,0,variables.getSpawnGuard().y),0,90,0,1,1);  //portal
		camPlayer = new Player(texturedModelIntruder, new Vector3f(L + 15, 0, L + 15), 0, 90, 0, 1, 1);


//		entities.addAll(walls);

		//players.add(intruder);
		//players.add(guard);

		// put the camera
		camera = new Camera(players.get(0));


		lastClick = System.currentTimeMillis();

		/** Particles System
		 */

		ParticleTexture particleTexture = new ParticleTexture(loader.loadTexture("explosion02"), 5);
		particleGen = new ParticleGenerator(particleTexture,1,5,0.3f,1,20);
		particleGen.randomizeRotation();
		particleGen.setDirection(new Vector3f(0,1,0),0f);
		particleGen.setLifeError(0.1f);
		particleGen.setSpeedError(0.4f);
		particleGen.setScaleError(0f);


	}

	public void run() {
		try {
			init();
		} catch (Exception e) {
			e.printStackTrace();
		}
		while (!window.shouldClose()) {
			update();
			//renderer.renderShadowMap(entities, lights.get(0));
			render();

			if (Input.isKeyDown(GLFW.GLFW_KEY_F11)) window.setFullscreen(!window.isFullscreen());


			if (Input.isKeyDown(GLFW.GLFW_KEY_ESCAPE)) {
				mainMenuBoolean = true;
			}

//			if(Input.isKeyDown(GLFW.GLFW_KEY_R)){
//				new Particle( new Vector3f(players.get(0).getPosition()), new Vector3f(0,30,0),1,4,0,1);
//			}

			if (Input.isKeyDown(GLFW.GLFW_KEY_M) && moveIndex < GameController.pathOfAllAgents.get(0).size()) {


				long currTime = System.currentTimeMillis();

				if (currTime - lastClick > 100) {

					for (int i = 0; i < GameController.pathOfAllAgents.size(); i++) {
						if (i < GameController.variables.getNumberOfIntruders()) {
							ArrayList<int[]> pathIntruder = GameController.pathOfAllAgents.get(i);
							players.get(i).move(new Vector2f(pathIntruder.get(moveIndex)[0] + L, pathIntruder.get(moveIndex)[1] + L), 0);

//						players.add(new Player(texturedModelIntruder, new Vector3f(pathIntruder.get(0)[0]+L,0,pathIntruder.get(0)[1]+L),0,90,0,1,i));
//							System.out.println("added intruder at: " + pathIntruder.get(0)[0] + ", " + pathIntruder.get(0)[1]);
							lastClick = currTime;
						} else if (i >= GameController.variables.getNumberOfIntruders()) {
							ArrayList<int[]> pathGuard = GameController.pathOfAllAgents.get(i);
							players.get(i).move(new Vector2f(pathGuard.get(moveIndex)[0] + L, pathGuard.get(moveIndex)[1] + L), 0);
//						players.add(new Player(texturedModelGuard, new Vector3f(pathGuard.get(0)[0]+L,0,pathGuard.get(0)[1]+L),0,90,0,1,i));
//							System.out.println("added guard at: " + pathGuard.get(0)[0] + ", " + pathGuard.get(0)[1]);
							lastClick = currTime;
						}
					}
					moveIndex++;
				}
			}

			if (Input.isKeyDown(GLFW.GLFW_KEY_N) && moveIndex > 1) {


				long currTime = System.currentTimeMillis();

				if (currTime - lastClick > 100) {

					for (int i = 0; i < GameController.pathOfAllAgents.size(); i++) {
						if (i < GameController.variables.getNumberOfIntruders()) {
							ArrayList<int[]> pathIntruder = GameController.pathOfAllAgents.get(i);
							players.get(i).move(new Vector2f(pathIntruder.get(moveIndex - 1)[0] + L, pathIntruder.get(moveIndex - 1)[1] + L), pathIntruder.get(moveIndex)[2] - 90);

//						players.add(new Player(texturedModelIntruder, new Vector3f(pathIntruder.get(0)[0]+L,0,pathIntruder.get(0)[1]+L),0,90,0,1,i));
//							System.out.println("added intruder at: " + pathIntruder.get(0)[0] + ", " + pathIntruder.get(0)[1]);
							lastClick = currTime;
						} else if (i >= GameController.variables.getNumberOfIntruders()) {
							ArrayList<int[]> pathGuard = GameController.pathOfAllAgents.get(i);
							players.get(i).move(new Vector2f(pathGuard.get(moveIndex - 1)[0] + L, pathGuard.get(moveIndex - 1)[1] + L), pathGuard.get(moveIndex)[2] - 90);
//						players.add(new Player(texturedModelGuard, new Vector3f(pathGuard.get(0)[0]+L,0,pathGuard.get(0)[1]+L),0,90,0,1,i));
//							System.out.println("added guard at: " + pathGuard.get(0)[0] + ", " + pathGuard.get(0)[1]);
							lastClick = currTime;
						}
					}
					moveIndex--;
				}
			}
		}
		ParticleBrain.cleanUp();
		guiRenderer.cleanUp();
		renderer.cleanUp();
		loader.cleanUp();
		window.destroy();
	}


	private void update() {
		window.update();
	}

	private void render() {
		camera.move();
		ParticleBrain.update(camera);


		Vector3f particlePos = new Vector3f(players.get(0).getPosition().x,8,players.get(0).getPosition().z);

		if (Input.isKeyDown(GLFW.GLFW_KEY_R)) {
			particleGen.generateParticles(particlePos);
		}



		for (Player pieces : players) {
			renderer.processEntity(pieces);
		}



		for (Entity entity : entities) {
			renderer.processEntity(entity);
		}

		for (Entity entity : normalMappedEntities) {
			renderer.processNormalMapEntity(entity);
		}

		// * step 5: renderer.processEntity(nameOfEntity that you made at step 4.)

		renderer.processTerrain(terrain);

		renderer.render(lights, camera, new Vector4f(0, -1, 0, 100));

		ParticleBrain.renderParticles(camera);

		if (mainMenuBoolean) {
			guiRenderer.render(guis);
		}


		window.swapBuffers();

	}


	private ArrayList<Entity> createWallsFromFile() {
//		ArrayList<ArrayList<Entity>> walls = new ArrayList<>();
		ArrayList<Entity> walls = new ArrayList<>();
//
//		// x,y,z,w: this order.
//
//		for (int j = 0; j < GameController.variables.getWalls().size(); j++) {
//			walls.add(createWallFromParams(texturedModelWall, new Vector3f(startX+L,0,startY + i+L)));
//		}
//
		for (Wall w : GameController.variables.getWalls()) {
			if (w.x1 - w.x3 == 0) {
				for (int[] tile : w.getPoints()) {
					walls.add(new Entity(texturedModelWall, new Vector3f(tile[0] + L, terrain.getHeightOfTerrain(tile[0]+L, tile[1]+L), tile[1] + L), 0, 0, 0, 1, 1));
					System.out.println("getttt");
				}
			}
			if (w.y3 - w.y1 == 0) {
				for (int[] tile : w.getPoints()) {
					walls.add(new Entity(texturedModelWall, new Vector3f(tile[0] + L, terrain.getHeightOfTerrain(tile[0]+L, tile[1]+L), tile[1] + L), 0, 90, 0, 1, 1));
					System.out.println("555555");
				}
			}


//					for(int[] tile : w.getPoints()){
//						walls.add(new Entity(texturedModelWall, new Vector3f(tile[0]+L,0,tile[1]+L),0,90,0,1,1));
//
//					}

//		walls.add(createWallFromParams(GameController.variables.getWalls().get(0), variables.getWall1().y, variables.getWall1().z, variables.getWall1().w));
//		walls.add(createWallFromParams(variables.getWall2().x, variables.getWall2().y, variables.getWall2().z, variables.getWall2().w));
//		walls.add(createWallFromParams(variables.getWall3().x, variables.getWall3().y, variables.getWall3().z, variables.getWall3().w));
//		walls.add(createWallFromParams(variables.getWall4().x, variables.getWall4().y, variables.getWall4().z, variables.getWall4().w));
//		walls.add(createWallFromParams(variables.getWall5().x, variables.getWall5().y, variables.getWall5().z, variables.getWall5().w));
//
//		System.out.println(variables.getWall5().w);
			return walls;
		}
		return walls;
	}



	public static int getWIDTH() {
		return WIDTH;
	}

	public static int getHEIGHT() {
		return HEIGHT;
	}

}
