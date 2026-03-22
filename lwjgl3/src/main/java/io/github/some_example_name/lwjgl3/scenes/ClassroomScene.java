package io.github.some_example_name.lwjgl3.scenes;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Json;

import io.github.some_example_name.lwjgl3.collision.iCollisionManager;
import io.github.some_example_name.lwjgl3.entities.Answer;
import io.github.some_example_name.lwjgl3.entities.Collectable;
import io.github.some_example_name.lwjgl3.entities.Entity;
import io.github.some_example_name.lwjgl3.entities.Obstacle;
import io.github.some_example_name.lwjgl3.entities.PlayableEntity;
import io.github.some_example_name.lwjgl3.entities.Question;
import io.github.some_example_name.lwjgl3.entities.iEntityManager;
import io.github.some_example_name.lwjgl3.factories.AnswerFactory;
import io.github.some_example_name.lwjgl3.factories.ObstacleFactory;
import io.github.some_example_name.lwjgl3.factories.ObstacleFactory.ObstacleType;
import io.github.some_example_name.lwjgl3.factories.CollectableFactory; 
import io.github.some_example_name.lwjgl3.factories.CollectableFactory.CollectableType;
import io.github.some_example_name.lwjgl3.inputs.iInputManager;
import io.github.some_example_name.lwjgl3.movement.iMovementManager;

public class ClassroomScene extends Scene {

    private iEntityManager entityManager;
    private iInputManager inputManager;
    private iMovementManager movementManager;
    private iCollisionManager collisionManager;
    private iSceneManager sceneManager;

    // Grab the real OrthographicCamera so we can read its actual viewport size at runtime
    private OrthographicCamera camera;

    private Texture backgroundTexture;
    private float bg1X;
    private float bg2X;

    private float spawnTimer = 0f;
    private int lastSpawnSecond = -1;

    private static final float SCROLL_SPEED = 4f;
    private static final float BG_SCROLL_SPEED = 1.2f;

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

    // Scoring system
    private float distanceTravelled = 0f;
    private int score = 0;

    // Multiplier indicator
    private Texture multiplierIcon;      // Default icon
    private Texture multiplierActiveIcon; // Glow/active state
    private float multiplierTimer = 0f;
    private static final float MULTIPLIER_DURATION = 5f;

    // Multiplier effect
    private float blinkTimer = 0f;
    private boolean showMultiplier = true;
    private static final float BLINK_INTERVAL = 0.2f; // speed of blinking

    float spriteHeight = 80f; // match answer factory

    float[] lanes = {
        -SCREEN_H / 2f + SCREEN_H * 0.85f - spriteHeight / 2f,  // top
        -SCREEN_H / 2f + SCREEN_H * 0.5f  - spriteHeight / 2f,  // middle
        -SCREEN_H / 2f + SCREEN_H * 0.15f - spriteHeight / 2f   // bottom
    };


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

    public void setSceneManager(iSceneManager sceneManager) {
        this.sceneManager = sceneManager;
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
        // Background
        backgroundTexture = new Texture(Gdx.files.internal("classroom2_1.png"));
        bg1X = 0;
        bg2X = DRAW_W;

        // Questions JSON
        try {
            Json json = new Json();
            Question[] questionArray = json.fromJson(Question[].class, Gdx.files.internal("questions.json"));
            if (questionArray == null || questionArray.length == 0) {
                System.err.println("Warning: questions.json empty or missing!");
                questions = java.util.Collections.emptyList();
            } else {
                questions = java.util.Arrays.asList(questionArray);
            }
        } catch (Exception e) {
            e.printStackTrace();
            questions = java.util.Collections.emptyList();
        }

        // Fonts
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Roboto-Black.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter params = new FreeTypeFontGenerator.FreeTypeFontParameter();
        params.size = 24;
        params.color = Color.WHITE;
        params.borderWidth = 1;
        params.borderColor = Color.BLACK;
        text1 = generator.generateFont(params);
        params.borderWidth = 0;
        params.color = Color.BLACK;
        text2 = generator.generateFont(params);
        generator.dispose();

        // Power-up icons
        try {
            multiplierIcon = new Texture(Gdx.files.internal("power.jpg"));
            multiplierActiveIcon = new Texture(Gdx.files.internal("power.jpg")); // ensure exists
        } catch (Exception e) {
            e.printStackTrace();
            multiplierIcon = null;
            multiplierActiveIcon = null;
        }
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
    }

    @Override
    public void update(float deltaTime) {
        float scrollAmount = SCROLL_SPEED * deltaTime * 60f;
        float bgScrollAmount = BG_SCROLL_SPEED * deltaTime * 60f;

        // Update all entities
        for (Entity entity : entityManager.getAllEntities()) {
            entity.update(deltaTime);
        }

        // Find player once
        PlayableEntity player = (PlayableEntity) entityManager.getEntity("player_1");
        

        // Scroll background
        bg1X -= bgScrollAmount;
        bg2X -= bgScrollAmount;
        if (bg1X + DRAW_W <= 0) bg1X = bg2X + DRAW_W;
        if (bg2X + DRAW_W <= 0) bg2X = bg1X + DRAW_W;

        // Update spawn timer
        spawnTimer += deltaTime;
        int currentSecond = (int) spawnTimer;
        if (currentSecond != lastSpawnSecond) {
            lastSpawnSecond = currentSecond;

            // Spawn obstacles
            if (currentSecond % 2 == 0 
    && currentSecond != 0
    && (currentSecond % 7 != 0)          // not on question tick
    && ((currentSecond + 1) % 7 != 0)    // not 1 second before question
    && ((currentSecond - 1) % 7 != 0)) { // not 1 second after question
                float spawnX = camRight() + 40f;
                float spawnY = lanes[MathUtils.random(0, 2)];

                ObstacleFactory factory = new ObstacleFactory(collisionManager);
                Obstacle newObstacle = factory.create(
                        MathUtils.randomBoolean() ? ObstacleType.ERASER : ObstacleType.BOOKS,
                        spawnX, spawnY);
                addEntity(newObstacle);
                entityManager.addEntity(newObstacle);

                // Always spawn power-ups in player state
                // Don't spawn if player is currently powered-up
                if (!player.canUsePowerUp()) {
                    float spawnY1;
                    do {
                        spawnY1 = lanes[MathUtils.random(0, 2)];
                    } while (Math.abs(spawnY1 - spawnY) < 0.01f); // choose different lane to avoid overlap

                    float collectableX = spawnX + 80f; 

                    CollectableFactory factory1 = new CollectableFactory(collisionManager);
                    Collectable newCollectable = factory1.create(CollectableType.POWERUP, collectableX, spawnY1);
                    addEntity(newCollectable);
                    entityManager.addEntity(newCollectable);
                }
            }

            // Spawn questions safely
            if (currentSecond % 7 == 0 && !questions.isEmpty()) {
                Question q = questions.get(currentQuestion);
                currentQuestionText = q.question;

                AnswerFactory answerFactory = new AnswerFactory(collisionManager);
                float spriteHeight = 80f;

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

                currentQuestion = (currentQuestion + 1) % questions.size();
            }
        }

        // Multiplier power-up activation logic
        // If player has charges and currently no active multiplier then start one
        if (player != null && player.canUsePowerUp() && !player.isPowerActive()) {
            player.usePowerUp();
            multiplierTimer = MULTIPLIER_DURATION;
        }

        // If multiplier is already active, consuming additional collected charges extends the current timer immediately
        if (player != null && player.isPowerActive()) {
            while (player.getPowerUpCount() > 0) {
                player.consumePowerUpCharge();
                multiplierTimer += MULTIPLIER_DURATION;
            }
        }

        // Handle countdown + blinking EVERY FRAME
        if (player != null && player.isPowerActive()) {

            // Countdown
            multiplierTimer -= deltaTime;

            if (multiplierTimer <= 0f) {
                multiplierTimer = 0f;
                player.deactivatePowerUp();
                showMultiplier = true;
                blinkTimer = 0f;
            } else {
                // Blinking effect
                blinkTimer += deltaTime;

                if (blinkTimer >= BLINK_INTERVAL) {
                    blinkTimer = 0f;
                    showMultiplier = !showMultiplier;
                }
            }

        }
        else {
            // Reset when not active
            showMultiplier = true;
            blinkTimer = 0f;
        }

        // Scroll obstacles & collectables
        for (Entity entity : entityManager.getAllEntities()) {
            if (entity instanceof Obstacle || entity instanceof Collectable) {
                entity.getPosition().x -= scrollAmount;
            }
        }

        // Update distance and score
        float distanceIncrement = scrollAmount / 100f;
        if (player != null && player.isPowerActive()) distanceIncrement *= 2;
        distanceTravelled += distanceIncrement;
        score = (int) (distanceTravelled * 10);

        // Go to Game Over Once HP Turns 0
        if (player != null && player.getHealth() == 0) {
            // 1. Get the GameOverScene from the SceneManager
            Scene gameOver = sceneManager.getScene("GameOverScene");
            
            // 2. Cast it and pass the score over
            if (gameOver instanceof GameOverScene) {
                ((GameOverScene) gameOver).setFinalScore(this.score);
            }
            
            // 3. Open the overlay
            sceneManager.openOverlay("GameOverScene");
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

        // Display score
        text1.draw(batch, "Score: " + score, camLeft() + 20, camBottom() + camera.viewportHeight - 50);
        

        // --- Power-up prompt ---
        PlayableEntity player = null;
        for (Entity entity : entityManager.getAllEntities()) {
            if (entity instanceof PlayableEntity) {
                player = (PlayableEntity) entity;
                break;
            }
        }

        // Display Health & Power-up
        if (player != null) {
            // Display health
            text1.draw(batch, "Health: " + player.getHealth(), camLeft() + 20, camBottom() + camera.viewportHeight - 80);

            float iconWidth = 50;
            float iconHeight = 50;
            float iconX = camLeft() + camera.viewportWidth - iconWidth - 20;
            float iconY = camBottom() + camera.viewportHeight - iconHeight - 20; 

            if (player.canUsePowerUp() && !player.isPowerActive()) {
                batch.draw(multiplierIcon, iconX, iconY, iconWidth, iconHeight);
            } 
            else if (player.isPowerActive()) {
                // Draw blinking icon
                if (showMultiplier) {
                    batch.draw(multiplierActiveIcon, iconX, iconY, iconWidth, iconHeight);
                }

                // Draw timer text beside icon
                String timeText = String.valueOf((int)Math.ceil(multiplierTimer));

                GlyphLayout layout = new GlyphLayout(text1, timeText);

                float textX = iconX - layout.width - 10; // left of icon
                float textY = iconY + iconHeight / 2f + layout.height / 2f;

                text1.draw(batch, layout, textX, textY);
            }
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