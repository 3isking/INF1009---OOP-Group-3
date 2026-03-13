package io.github.some_example_name.lwjgl3.scenes;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Json;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

import io.github.some_example_name.lwjgl3.collision.iCollisionManager;
import io.github.some_example_name.lwjgl3.entities.iEntityManager;
import io.github.some_example_name.lwjgl3.entities.Answer;
import io.github.some_example_name.lwjgl3.entities.Entity;
import io.github.some_example_name.lwjgl3.entities.PlayableEntity;
import io.github.some_example_name.lwjgl3.entities.Question;
import io.github.some_example_name.lwjgl3.factories.AnswerFactory;
import io.github.some_example_name.lwjgl3.factories.ObstacleFactory;
import io.github.some_example_name.lwjgl3.factories.ObstacleFactory.ObstacleType;
import io.github.some_example_name.lwjgl3.entities.Obstacle;
import io.github.some_example_name.lwjgl3.inputs.iInputManager;
import io.github.some_example_name.lwjgl3.movement.iMovementManager;

public class ClassroomScene extends Scene {

    private iEntityManager entityManager;
    private iInputManager inputManager;
    private iMovementManager movementManager;
    private iCollisionManager collisionManager;

    // Grab the real OrthographicCamera so we can read its actual viewport size at runtime
    private OrthographicCamera camera;

    private Texture backgroundTexture;
    private float bg1X;
    private float bg2X;

    private float spawnTimer = 0f;
    private int lastSpawnSecond = -1;

    private static final float SCROLL_SPEED = 6f;

    // Virtual / design resolution — used for entity placement and image scaling only
    private static final float SCREEN_W = 640f;
    private static final float SCREEN_H = 480f;

    // Real image size
    private static final float IMG_W = 1584f;
    private static final float IMG_H = 672f;

    // Scale so the image HEIGHT exactly fills the screen height.
    private static final float SCALE = SCREEN_H / IMG_H;
    private static final float DRAW_W = (float) Math.ceil(IMG_W * SCALE) + 1f; // +1 to hide seam
    private static final float DRAW_H = SCREEN_H;
    
    private BitmapFont text1;
    private BitmapFont text2;
    private List<Question> questions;
    private int currentQuestion = 0;
    private String currentQuestionText = "";

    public ClassroomScene(iEntityManager entityManager, iInputManager inputManager,
                          iMovementManager movementManager, iCollisionManager collisionManager) {
        super("ClassroomScene");
        this.entityManager = entityManager;
        this.inputManager = inputManager;
        this.movementManager = movementManager;
        this.collisionManager = collisionManager;
        // Grab the live OrthographicCamera — its viewportWidth/Height update on resize
        this.camera = inputManager.getCamera().getCamera();
    }

    // --- Dynamic camera edge helpers ---
    // These read the camera's ACTUAL viewport each frame, so they're always correct
    // whether the window is windowed, fullscreen, or any other size.

    private float camLeft() {
        return camera.position.x - camera.viewportWidth / 2f;
    }

    private float camBottom() {
        return camera.position.y - camera.viewportHeight / 2f;
    }

    private float camRight() {
        return camera.position.x + camera.viewportWidth / 2f;
    }

    @Override
    public void onLoad() {
        backgroundTexture = new Texture(Gdx.files.internal("classroom2.png"));
        bg1X = 0;
        bg2X = DRAW_W;
        Json json = new Json();
        Question[] questionArray = json.fromJson(Question[].class, Gdx.files.internal("questions.json"));
        questions = java.util.Arrays.asList(questionArray);
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Roboto-Black.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter params = new FreeTypeFontGenerator.FreeTypeFontParameter();
        params.size = 24;
        params.color = Color.WHITE;
        params.borderWidth = 1;        // outline thickness
        params.borderColor = Color.BLACK;
        text1 = generator.generateFont(params);
        params.borderWidth = 0; 
        params.color = Color.BLACK;
        text2 = generator.generateFont(params);
        generator.dispose();
    }

    @Override
    public void onEnter() {
        bg1X = 0;
        bg2X = DRAW_W;

        // Place player relative to the virtual design size so it feels consistent
        PlayableEntity player = new PlayableEntity(
                (int)(-SCREEN_W / 2f + 100),
                (int)(-SCREEN_H / 2f + 200),
                movementManager, collisionManager);
        player.setId("player_1");
        addEntity(player);
        entityManager.addEntity(player);

        // Spawn initial obstacles just off the right edge of the VIRTUAL screen
        ObstacleFactory factory = new ObstacleFactory(collisionManager);

        Obstacle eraser = factory.create(ObstacleType.ERASER,
                -SCREEN_W / 2f + SCREEN_W + 160f,
                -SCREEN_H / 2f + 200f);
        Obstacle books = factory.create(ObstacleType.BOOKS,
                -SCREEN_W / 2f + SCREEN_W + 560f,
                -SCREEN_H / 2f + 200f);

        addEntity(eraser);
        entityManager.addEntity(eraser);
        addEntity(books);
        entityManager.addEntity(books);
    }

    @Override
    public void update(float deltaTime) {
        float scrollAmount = SCROLL_SPEED * deltaTime * 60f;

        bg1X -= scrollAmount;
        bg2X -= scrollAmount;

        // Leapfrog: when a panel has scrolled fully off the left edge, jump it
        // to just after the other panel.  bg1X/bg2X are offsets from camLeft(),
        // so the off-screen condition is simply offset + DRAW_W <= 0.
        if (bg1X + DRAW_W <= 0) bg1X = bg2X + DRAW_W;
        if (bg2X + DRAW_W <= 0) bg2X = bg1X + DRAW_W;

        // Spawn timer
        spawnTimer += deltaTime;
        int currentSecond = (int) spawnTimer;

        if (currentSecond != lastSpawnSecond) {
            lastSpawnSecond = currentSecond;

            if (currentSecond % 2 == 0) {
                ObstacleFactory factory = new ObstacleFactory(collisionManager);
                float spawnY = -SCREEN_H / 2f + MathUtils.random(100, 400);
                // Spawn just off the real right edge so obstacles appear from the correct side
                float spawnX = camRight() + 40f;
                Obstacle newObstacle = factory.create(
                        MathUtils.randomBoolean() ? ObstacleType.ERASER : ObstacleType.BOOKS,
                        spawnX, spawnY);
                addEntity(newObstacle);
                entityManager.addEntity(newObstacle);
            }
            
            if (currentSecond % 5 == 0) {
            	if (questions.isEmpty()) {
            		return;
            	}

                // Get current question
                Question q = questions.get(currentQuestion);
                currentQuestionText = q.question;
                AnswerFactory answerFactory = new AnswerFactory(collisionManager);
                float spriteHeight = 80f; // match your Answer width/height in the factory

                // Define lane centers
                float topY = -SCREEN_H / 2f + SCREEN_H * 0.85f - spriteHeight / 2f;
                float middleY = -SCREEN_H / 2f + SCREEN_H * 0.5f - spriteHeight / 2f;
                float bottomY = -SCREEN_H / 2f + SCREEN_H * 0.15f - spriteHeight / 2f;
                Answer topAnswer = answerFactory.create(-SCREEN_W / 2f + SCREEN_W + 300f, topY, q.answers[0], 0 == q.correct);
                Answer middleAnswer = answerFactory.create(-SCREEN_W / 2f + SCREEN_W + 300f, middleY, q.answers[1], 1 == q.correct);
                Answer bottomAnswer = answerFactory.create(-SCREEN_W / 2f + SCREEN_W + 300f, bottomY, q.answers[2], 2 == q.correct);
                addEntity(topAnswer);
                addEntity(middleAnswer);
                addEntity(bottomAnswer);
                entityManager.addEntity(topAnswer);
                entityManager.addEntity(middleAnswer);
                entityManager.addEntity(bottomAnswer);
                currentQuestion++;
                if (currentQuestion >= questions.size()) {
                    currentQuestion = 0; // loop back to first question if needed
                }
                
            }
        }

        // Scroll obstacles by same amount as background
        for (Entity entity : entityManager.getAllEntities()) {
            if (entity instanceof Obstacle) {
                ((Obstacle) entity).getPosition().x -= scrollAmount;
            }
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        // Anchor both background copies to the REAL camera left edge every frame.
        // This is the key fix: camLeft() returns the true edge regardless of
        // window/fullscreen size, so there is never a gap on the right.
        float left   = camLeft();
        float bottom = camBottom();

        batch.draw(backgroundTexture, left + bg1X, bottom, DRAW_W, DRAW_H);
        batch.draw(backgroundTexture, left + bg2X, bottom, DRAW_W, DRAW_H);
    }
    
    @Override
    public void renderUI(SpriteBatch batch) {
        // question text and answer text on top of everything
    	for (Entity entity : entityManager.getAllEntities()) {
            if (entity instanceof Answer) {
                Answer answer = (Answer) entity;
                GlyphLayout layout = new GlyphLayout(text2, answer.getText());
                float centerX = answer.getPosition().x + answer.getSprite().getWidth() / 2f;
                float centerY = answer.getPosition().y + answer.getSprite().getHeight() / 2f;
                float textX = centerX - layout.width / 2f;
                float textY = centerY + layout.height / 2f +5f;
                text2.draw(batch, layout, textX, textY);
            }
        }
        if (!currentQuestionText.isEmpty()) {
            GlyphLayout layout = new GlyphLayout(text1, currentQuestionText);
            float x = camLeft() + (camera.viewportWidth / 2f) - layout.width / 2f;
            float y = camBottom() + camera.viewportHeight - 20f;
            text1.draw(batch, layout, x, y);
        }
        
    }

    @Override
    public void onExit() {
        entityManager.clear();
        clearEntityList();
    }

    @Override
    public void onUnload() {
        if (backgroundTexture != null) backgroundTexture.dispose();
    }
}