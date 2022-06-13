package Visuals.main;

import Controller.*;
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
import base.GameController;
import base.GameEndChecker;
import org.lwjgl.glfw.GLFW;
import org.lwjglx.util.vector.Vector2f;
import org.lwjglx.util.vector.Vector3f;
import org.lwjglx.util.vector.Vector4f;
import Visuals.terrain.Terrain;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.*;

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

	public ModelData modelDataGuard;
	public ModelData modelDataIntruder;
	public ModelData modelDataWall;
	public ModelData modelDataPortal;
	public ModelData modelDataTrace;
	public ModelData modelDataGoal;

	public ModelTexture textureGuard;
	public ModelTexture textureIntruder;
	public ModelTexture textureWall;
	public ModelTexture texturePortal;
	public ModelTexture textureTrace;
	public ModelTexture textureGoal;

	public TexturedModel texturedModelGuard;
	public TexturedModel texturedModelIntruder;
	public TexturedModel texturedModelWall;
	public TexturedModel texturedModelPortal;
	public TexturedModel texturedModelTrace;
	public TexturedModel texturedModelgoal;

	public MasterRenderer renderer;

	public Player guard;
	public Player intruder;
	public Player camPlayer;
	public Entity goal;

	public Camera camera;

	public List<Light> lights;
	public List<Entity> entities = new ArrayList<>();
	public List<Player> players = new ArrayList<>();
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

	GameController g = new GameController();


	public static String testMapPath;

	public boolean generateMaze = true;
	public Position start;
	public Position end;
	public int[][] mazeMatrix;
	public int pathIndex = 0;
	public List<Position> path;
	public Input input;
	public long lastClick;
	public static int L = 150;

	public int moveIndex = 0;

	public ArrayList<ArrayList<int[]>> listOfMovesEveryAgent;


	public void start() {
		game = new Thread(this, "Simulation");
		game.start();
	}

	public void init() throws Exception {
		window = new Window(WIDTH, HEIGHT, "Simulation");
		window.setBackgroundColor(RED,GREEN,BLUE);
		window.create();
		renderer = new MasterRenderer(loader);
		testMapPath = "res/testmap.txt";


		//parser.readFile(testMapPath)

		// Loading in an object:
		// * step 1: get .obj file (from ../res/3D/)

		modelDataGuard = OBJFileLoader.loadOBJ("guard");
		modelDataIntruder = OBJFileLoader.loadOBJ("guard");
		modelDataWall = OBJFileLoader.loadOBJ("wall");
		modelDataPortal = OBJFileLoader.loadOBJ("portal");
		modelDataGoal = OBJFileLoader.loadOBJ("goal");


		// * step 2: load the model data

		guardModel = loader.loadToVAO(modelDataGuard.getVertices(),modelDataGuard.getTextureCoords(),modelDataGuard.getNormals(),modelDataGuard.getIndices());
		intruderModel = loader.loadToVAO(modelDataIntruder.getVertices(),modelDataIntruder.getTextureCoords(),modelDataIntruder.getNormals(),modelDataIntruder.getIndices());
		wallModel = loader.loadToVAO(modelDataWall.getVertices(),modelDataWall.getTextureCoords(),modelDataWall.getNormals(),modelDataWall.getIndices());
		portalModel = loader.loadToVAO(modelDataPortal.getVertices(),modelDataPortal.getTextureCoords(),modelDataPortal.getNormals(),modelDataPortal.getIndices());
		goalModel = loader.loadToVAO(modelDataGoal.getVertices(),modelDataGoal.getTextureCoords(),modelDataGoal.getNormals(),modelDataGoal.getIndices());

		// create texture for terrain
		backgroundTexture = new TerrainTexture(loader.loadTexture("grassy"));
		rTexture = new TerrainTexture(loader.loadTexture("dirt"));
		gTexture = new TerrainTexture(loader.loadTexture("pinkFlowers"));
		bTexture = new TerrainTexture(loader.loadTexture("path"));
		texturePack = new TerrainTexturePack(backgroundTexture, rTexture,gTexture,bTexture);
		blendMap = new TerrainTexture(loader.loadTexture("blendMap"));

		// * step 3: Add textures and models together.

		texturedModelGuard = new TexturedModel(guardModel, new ModelTexture(loader.loadTexture("wallTexture")));
		texturedModelIntruder = new TexturedModel(intruderModel, new ModelTexture(loader.loadTexture("portalTexture")));
		texturedModelWall = new TexturedModel(wallModel, new ModelTexture(loader.loadTexture("stoneWall")));
		texturedModelPortal = new TexturedModel(portalModel, new ModelTexture(loader.loadTexture("portalTexture")));
		texturedModelgoal = new TexturedModel(goalModel, new ModelTexture(loader.loadTexture("portalTexture")));

		textureGuard = texturedModelGuard.getTexture();
		textureGuard.setShineDamper(5);
		textureGuard.setReflectivity((float)0.5);

		textureIntruder = texturedModelIntruder.getTexture();
		textureIntruder.setShineDamper(5);
		textureIntruder.setReflectivity((float)0.5);

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
		ArrayList<Entity> walls = createWallsFromFile();

		for (int x = -1; x < 30; x++) {
			walls.add(new Entity(texturedModelWall, new Vector3f(x+L,0,-1+L),0,90,0,1,1));
		}
		for (int x = -1; x < 30; x++) {
			walls.add(new Entity(texturedModelWall, new Vector3f(x+L,0,30+L),0,90,0,1,1));
		}
		for (int y = -1; y < 30; y++) {
			walls.add(new Entity(texturedModelWall, new Vector3f(-1+L,0,y+L),0,90,0,1,1));
		}
		for (int y = -1; y < 30; y++) {
			walls.add(new Entity(texturedModelWall, new Vector3f(30+L,0,y+L),0,90,0,1,1));
		}

		for(Teleport t : GameController.variables.getPortals()){
			ArrayList<int[]> entrances = t.getPointsIn();
			int[] destination = t.getPointOut();
			double angle = t.getDegreeOut();
			for (int[] telePos:entrances) {
				entities.add(new Entity(texturedModelPortal, new Vector3f(telePos[0]+L,0,telePos[1]+L), (float) 0, (float) angle,0,1,1));
			}
			entities.add(new Entity(texturedModelPortal, new Vector3f(destination[0]+L,0,destination[1]+L), (float) 0, (float) angle,0,1,1));
		}

		for (Tile t : GameController.goalTiles){
			entities.add(new Entity(texturedModelgoal, new Vector3f(t.getXCoord()+L,0,t.getYCoord()+L), (float) 0, 90,0,1,1));
		}


		// game



		// generate terrain
		terrain = new Terrain(0,0,loader,texturePack, blendMap,"heightMap");

		// generate light
		lights = new ArrayList<>();
		lights.add(new Light(new Vector3f(1000+L,1000,300+L), new Vector3f(1f,1f,1f)));


		// generate players
		// * step 4: Generate entities or players.

		for (int i = 0; i < GameController.pathOfAllAgents.size(); i++) {
			if(i<GameController.variables.getNumberOfIntruders()){
				ArrayList<int[]> pathIntruder = GameController.pathOfAllAgents.get(i);

				players.add(new Player(texturedModelIntruder, new Vector3f(pathIntruder.get(0)[0]+L,0,pathIntruder.get(0)[1]+L),0,90,0,1,i));
				System.out.println("added intruder at: " + pathIntruder.get(0)[0]+ ", "+ pathIntruder.get(0)[1]) ;
			}
			else if(i>=GameController.variables.getNumberOfIntruders()){
				ArrayList<int[]> pathGuard = GameController.pathOfAllAgents.get(i);
				players.add(new Player(texturedModelGuard, new Vector3f(pathGuard.get(0)[0]+L,0,pathGuard.get(0)[1]+L),0,90,0,1,i));
				System.out.println("added guard at: " + pathGuard.get(0)[0]+ ", "+ pathGuard.get(0)[1]) ;
			}
		}

		
		//intruder = new Player(texturedModelGuard, new Vector3f(L,0,L),0,90,0,1,1);  //portal
		//guard = new Player(texturedModelGuard, new Vector3f(variables.getSpawnGuard().x,0,variables.getSpawnGuard().y),0,90,0,1,1);  //portal
		camPlayer = new Player(texturedModelIntruder, new Vector3f(L+15,0,L+15),0,90,0,1,1);

		entities.addAll(walls);

		//players.add(intruder);
		//players.add(guard);

		// put the camera
		camera = new Camera(camPlayer);

		lastClick = System.currentTimeMillis();


	}

	public void run() {
		try {
			init();
		} catch (Exception e) {
			e.printStackTrace();
		}
		while (!window.shouldClose() && !Input.isKeyDown(GLFW.GLFW_KEY_ESCAPE)) {
			update();
			render();
			if (Input.isKeyDown(GLFW.GLFW_KEY_F11)) window.setFullscreen(!window.isFullscreen());

			if (Input.isKeyDown(GLFW.GLFW_KEY_M)&&moveIndex<GameController.pathOfAllAgents.get(0).size()-1){


				long currTime = System.currentTimeMillis();

				if(currTime-lastClick > 100) {

					for (int i = 0; i < GameController.pathOfAllAgents.size(); i++) {
						if (i < GameController.variables.getNumberOfIntruders()) {
							ArrayList<int[]> pathIntruder = GameController.pathOfAllAgents.get(i);
							players.get(i).move(new Vector2f(pathIntruder.get(moveIndex)[0] + L, pathIntruder.get(moveIndex)[1] + L));

//						players.add(new Player(texturedModelIntruder, new Vector3f(pathIntruder.get(0)[0]+L,0,pathIntruder.get(0)[1]+L),0,90,0,1,i));
							System.out.println("added intruder at: " + pathIntruder.get(0)[0] + ", " + pathIntruder.get(0)[1]);
							moveIndex++;
							lastClick = currTime;
						} else if (i >= GameController.variables.getNumberOfIntruders()) {
							ArrayList<int[]> pathGuard = GameController.pathOfAllAgents.get(i);
							players.get(i).move(new Vector2f(pathGuard.get(moveIndex)[0] + L, pathGuard.get(moveIndex)[1] + L));
//						players.add(new Player(texturedModelGuard, new Vector3f(pathGuard.get(0)[0]+L,0,pathGuard.get(0)[1]+L),0,90,0,1,i));
							System.out.println("added guard at: " + pathGuard.get(0)[0] + ", " + pathGuard.get(0)[1]);
							moveIndex++;
							lastClick = currTime;
						}
					}
				}



//
//				long currTime = System.currentTimeMillis();
//				if(currTime-lastClick > 100){
//					intruder.move(new Vector2f(+L,pos.getY()+L));
//					pathIndex++;
//
//				}
			}

			if (Input.isKeyDown(GLFW.GLFW_KEY_N) && pathIndex <= path.size() && pathIndex>0) {

				long currTime = System.currentTimeMillis();

				if(currTime-lastClick > 100) {

					for (int i = 0; i < GameController.pathOfAllAgents.size(); i++) {
						if (i < GameController.variables.getNumberOfIntruders()) {
							ArrayList<int[]> pathIntruder = GameController.pathOfAllAgents.get(i);
							players.get(i).move(new Vector2f(pathIntruder.get(moveIndex)[0] + L, pathIntruder.get(moveIndex)[1] + L));

//						players.add(new Player(texturedModelIntruder, new Vector3f(pathIntruder.get(0)[0]+L,0,pathIntruder.get(0)[1]+L),0,90,0,1,i));
							System.out.println("added intruder at: " + pathIntruder.get(0)[0] + ", " + pathIntruder.get(0)[1]);
							moveIndex++;
							lastClick = currTime;
						} else if (i >= GameController.variables.getNumberOfIntruders()) {
							ArrayList<int[]> pathGuard = GameController.pathOfAllAgents.get(i);
							players.get(i).move(new Vector2f(pathGuard.get(moveIndex)[0] + L, pathGuard.get(moveIndex)[1] + L));
//						players.add(new Player(texturedModelGuard, new Vector3f(pathGuard.get(0)[0]+L,0,pathGuard.get(0)[1]+L),0,90,0,1,i));
							System.out.println("added guard at: " + pathGuard.get(0)[0] + ", " + pathGuard.get(0)[1]);
							moveIndex--;
							lastClick = currTime;
						}
					}
				}
			}
		}

		renderer.cleanUp();
		loader.cleanUp();
		window.destroy();
	}



	private void update() {
		window.update();
	}

	private void render() {
		camera.move();
		i++;


		for(Player pieces : players){
			renderer.processEntity(pieces);
		}

		for(Entity entity : entities){
			renderer.processEntity(entity);
		}
		// * step 5: renderer.processEntity(nameOfEntity that you made at step 4.)

		renderer.processTerrain(terrain);

		renderer.render(lights,camera, new Vector4f(0,-1,0, 100));

		window.swapBuffers();

	}


	private ArrayList<Entity> createWallsFromFile(){
//		ArrayList<ArrayList<Entity>> walls = new ArrayList<>();
		ArrayList<Entity> walls = new ArrayList<>();
//
//		// x,y,z,w: this order.
//
//		for (int j = 0; j < GameController.variables.getWalls().size(); j++) {
//			walls.add(createWallFromParams(texturedModelWall, new Vector3f(startX+L,0,startY + i+L)));
//		}
//
		for(Wall w : GameController.variables.getWalls()){
			for(int[] tile : w.getPoints()){
				walls.add(new Entity(texturedModelWall, new Vector3f(tile[0]+L,0,tile[1]+L),0,90,0,1,1));
			}
		}

//		walls.add(createWallFromParams(GameController.variables.getWalls().get(0), variables.getWall1().y, variables.getWall1().z, variables.getWall1().w));
//		walls.add(createWallFromParams(variables.getWall2().x, variables.getWall2().y, variables.getWall2().z, variables.getWall2().w));
//		walls.add(createWallFromParams(variables.getWall3().x, variables.getWall3().y, variables.getWall3().z, variables.getWall3().w));
//		walls.add(createWallFromParams(variables.getWall4().x, variables.getWall4().y, variables.getWall4().z, variables.getWall4().w));
//		walls.add(createWallFromParams(variables.getWall5().x, variables.getWall5().y, variables.getWall5().z, variables.getWall5().w));
//
//		System.out.println(variables.getWall5().w);
		return walls;
	}


	public static int getWIDTH() {
		return WIDTH;
	}

	public static int getHEIGHT() {
		return HEIGHT;
	}

}
