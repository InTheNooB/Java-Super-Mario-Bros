/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.wrk.map;

import app.wrk.Constantes;
import app.wrk.map.blocks.Block_Mistery_Full;
import app.wrk.map.blocks.Block_Brick;
import app.wrk.map.blocks.Block_Floor;
import app.wrk.Wrk;
import app.wrk.entities.Entity_Coin;
import app.wrk.entities.Entity_End_Flag;
import app.wrk.entities.Entity_Flower;
import app.wrk.entities.Entity_Goomba;
import app.wrk.entities.Entity_Koopa;
import app.wrk.entities.Entity_Mario;
import app.wrk.entities.Entity_Muschroom;
import app.wrk.map.blocks.Block;
import app.wrk.map.blocks.Block_Concrete;
import app.wrk.map.blocks.Block_End_Flag_Body;
import app.wrk.map.blocks.Block_End_Flag_Top;
import app.wrk.map.blocks.Block_Moving_Platform;
import app.wrk.map.blocks.Block_Pipe_Body;
import app.wrk.map.blocks.Block_Pipe_Top;
import app.wrk.map.decorations.Decoration;
import app.wrk.others.ErrorHandler;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author lione_000
 */
public class Map implements Constantes {

    private String mapFolder;
    private Entity_Mario refMario;
    private Wrk refWrk;

    private int width;
    private int height;
    private String name;
    private int mapType;

    private int startX;
    private int startY;

    private ArrayList<Map_Link> links;
    private String mapContent;

    private Decoration[] decorations;
    private Decoration[] groundDecorations;
    private Decoration[] skyDecorations;
    private Decoration[] castles;
    private int decorationNbr;

    public Map(Entity_Mario refMario, Wrk refWrk) {
        this.refWrk = refWrk;
        this.refMario = refMario;
        links = new ArrayList();
        mapFolder = "1-1";
        setup();
    }

    public void setup() {
        loadConfXML();
        loadMap();
        if (checkMapSize()) {
            processMap();
            createMap();
            loadBricksXML();
            loadMovingPlatformsXML();
            addBackground();
        } else {
            ErrorHandler.error("Map can't be loaded correctly");
        }
    }

    private void createMap() {
        Entity_End_Flag flag = null;
        int currentBottom = -1;
        for (int y = 0; y < height * 32; y += 32) {
            for (int x = 0; x <= width * 32; x += 32) {
                switch (getTile(x / 32, y / 32)) {
                    case OTHER_START_POINT:
                        startX = x;
                        startY = y;
                        refWrk.setStartPoint(x, y);
                        break;
                    case OTHER_END_FLAG_TOP:
                        refWrk.getBlocks().add(new Block_End_Flag_Top(refWrk, true, x + 16 / 2, y, false, OTHER_END_FLAG_TOP, 16, 16, mapType));
                        refWrk.getBlocks().add(new Block_End_Flag_Body(refWrk, x + 16 / 2, y + 16, false, OTHER_END_FLAG_BODY, 16, 16, mapType));
                        flag = new Entity_End_Flag(refWrk, refMario, x - 16, y + 16, mapType);
                        refWrk.getEntities().add(flag);
                        currentBottom = y - 8;
                        break;
                    case OTHER_END_FLAG_BODY:
                        refWrk.getBlocks().add(new Block_End_Flag_Body(refWrk, x + 16 / 2, y, false, OTHER_END_FLAG_BODY, 16, 16, mapType));
                        refWrk.getBlocks().add(new Block_End_Flag_Body(refWrk, x + 16 / 2, y + 16, false, OTHER_END_FLAG_BODY, 16, 16, mapType));
                        currentBottom = y - 8;
                        break;
                    case LETTER_BLOCK_CONCRETE:
                        refWrk.getBlocks().add(new Block_Concrete(refWrk, x, y, false, LETTER_BLOCK_CONCRETE, 32, 32, mapType));
                        break;
                    case LETTER_BLOCK_FLOOR:
                        refWrk.getBlocks().add(new Block_Floor(refWrk, x, y, false, LETTER_BLOCK_FLOOR, 32, 32, mapType));
                        break;
                    case LETTER_BLOCK_BRICK:
                        refWrk.getBlocks().add(new Block_Brick(refWrk, x, y, true, LETTER_BLOCK_BRICK, 32, 32, mapType));
                        break;
                    case LETTER_BLOCK_MISTERY_FULL_MUSCHROOM:
                        refWrk.getBlocks().add(new Block_Mistery_Full(refWrk, x, y, true, LETTER_BLOCK_MISTERY_FULL_MUSCHROOM, 32, 32, new Entity_Muschroom(refWrk, refMario, x, y, LEFT, mapType, false), mapType));
                        break;
                    case LETTER_BLOCK_MISTERY_FULL_COIN:
                        refWrk.getBlocks().add(new Block_Mistery_Full(refWrk, x, y, true, LETTER_BLOCK_MISTERY_FULL_COIN, 32, 32, new Entity_Coin(refWrk, refMario, x + 6, y, mapType, true), mapType));
                        break;
                    case LETTER_BLOCK_MISTERY_FULL_FLOWER:
                        refWrk.getBlocks().add(new Block_Mistery_Full(refWrk, x, y, true, LETTER_BLOCK_MISTERY_FULL_FLOWER, 32, 32, new Entity_Flower(refWrk, refMario, x, y, mapType), mapType));
                        break;
                    case LETTER_BLOCK_PIPE:
                        refWrk.getBlocks().add(new Block_Pipe_Body(refWrk, x, y, false, LETTER_BLOCK_PIPE, 64, 32));
                        break;
                    case LETTER_BLOCK_PIPE_TOP:
                        refWrk.getBlocks().add(new Block_Pipe_Top(refWrk, x, y, false, LETTER_BLOCK_PIPE_TOP, 64, 32));
                        break;
                    case NUMBER_ENTITY_GOOMBA:
                        refWrk.getEntities().add(new Entity_Goomba(refWrk, refMario, x, y, 1, mapType));
                        break;
                    case NUMBER_ENTITY_KOOPA:
                        refWrk.getEntities().add(new Entity_Koopa(refWrk, refMario, x, y - 50, 1, mapType));
                        break;
                    case LETTER_ENTITY_COIN:
                        refWrk.getEntities().add(new Entity_Coin(refWrk, refMario, x + 6, y, mapType, false));
                        break;
                    case OTHER_MOVING_PLATFORM:
                        refWrk.getBlocks().add(new Block_Moving_Platform(refWrk, x, y, false, OTHER_MOVING_PLATFORM, 32, 16));
                        break;
                    case LETTER_BLOCK_VOID:
                    default:
                        //Void
                        break;
                }
            }
        }
        if (flag != null) {
            flag.setBottom(currentBottom);
        }

    }

    public void processMap() {
        //Delete "enter" and "space" and "tabs"
        mapContent = mapContent.replaceAll("\n", "").replaceAll("\r", "").replaceAll("\t", "");
    }

    private void loadLinksXML(XPath path, Document document) {
        try {
            // Links
            links.clear();
            String expression = "//link";
            NodeList links = (NodeList) path.evaluate(expression, document, XPathConstants.NODESET);

            for (int i = 0; i < links.getLength(); i++) {
                try {
                    Node n = links.item(i);

                    path.compile("sourceMap/name");
                    String srcName = (String) path.evaluate("sourceMap/name", n, XPathConstants.STRING);

                    path.compile("sourceMap/x");
                    int srcX = ((Double) path.evaluate("sourceMap/x", n, XPathConstants.NUMBER)).intValue();

                    path.compile("sourceMap/y");
                    int srcY = ((Double) path.evaluate("sourceMap/y", n, XPathConstants.NUMBER)).intValue();

                    path.compile("destinationMap/x");
                    int dstX = ((Double) path.evaluate("destinationMap/x", n, XPathConstants.NUMBER)).intValue();

                    path.compile("destinationMap/y");
                    int dstY = ((Double) path.evaluate("destinationMap/y", n, XPathConstants.NUMBER)).intValue();

                    path.compile("destinationMap/name");
                    String dstName = (String) path.evaluate("destinationMap/name", n, XPathConstants.STRING);

                    path.compile("sourceMap/pipe");
                    boolean pipeIn = path.evaluate("sourceMap/pipe", n, XPathConstants.STRING).equals("true") ? true : false;

                    path.compile("destinationMap/pipe");
                    boolean pipeOut = path.evaluate("destinationMap/pipe", n, XPathConstants.STRING).equals("true") ? true : false;

                    if (dstX == 0 && dstY == 0) {
                        this.links.add(new Map_Link(srcName, dstName, srcX * 32 - LINK_WIDTH / 2, srcY * 32 - LINK_HEIGHT / 2, pipeIn, pipeOut));
                    } else {
                        this.links.add(new Map_Link(srcName, dstName, srcX * 32 - LINK_WIDTH / 2, srcY * 32 - LINK_HEIGHT / 2, dstX * 32 - LINK_WIDTH, dstY * 32 - LINK_HEIGHT, pipeIn, pipeOut));
                    }
                } catch (XPathExpressionException ex) {
                    ErrorHandler.error("Error while loading Links in XML file");
                }
            }
        } catch (XPathExpressionException ex) {
            ErrorHandler.error("Error while loading Links in XML file");
        }

    }

    private void loadMovingPlatformsXML() {
        try {
            ArrayList<Block_Moving_Platform> platformsList = new ArrayList();
            for (int j = 0; j < refWrk.getBlocks().size(); j++) {
                if (refWrk.getBlocks().get(j).getId() == OTHER_MOVING_PLATFORM) {
                    platformsList.add((Block_Moving_Platform) refWrk.getBlocks().get(j));
                }
            }

            File file = new File("bin/maps/" + mapFolder + "/conf.xml");
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document;
            document = documentBuilder.parse(file);

            XPathFactory xpf = XPathFactory.newInstance();
            XPath path = xpf.newXPath();

            String expression = "//platform";
            NodeList platforms = (NodeList) path.evaluate(expression, document, XPathConstants.NODESET);

            for (int i = 0; i < platforms.getLength(); i++) {
                Node n = platforms.item(i);

                path.compile("startY");
                int startY = (((Double) path.evaluate("startY", n, XPathConstants.NUMBER)).intValue() - 1) * 32;

                path.compile("stopY");
                int stopY = (((Double) path.evaluate("stopY", n, XPathConstants.NUMBER)).intValue() - 1) * 32;

                path.compile("startX");
                int startX = (((Double) path.evaluate("startX", n, XPathConstants.NUMBER)).intValue() - 1) * 32;

                path.compile("length");
                int length = ((Double) path.evaluate("length", n, XPathConstants.NUMBER)).intValue() * 32;

                for (Block_Moving_Platform plat : platformsList) {
                    if (plat.getX() >= startX && plat.getX() <= startX + length) {
                        plat.setStartY(startY);
                        plat.setStopY(stopY);
                    }
                }

                refWrk.getBlocks().addAll(platformsList);

            }
        } catch (Exception ex) {
            ErrorHandler.error("Error while loading Moving Platforms in XML file");
        }

    }

    private void loadGeneralsInfosXML(XPath path, Document document) {
        try {
            // Generales infos
            name = (String) path.evaluate("/map/name", document, XPathConstants.STRING);
            width = ((Double) path.evaluate("/map/width", document, XPathConstants.NUMBER)).intValue();
            height = ((Double) path.evaluate("/map/height", document, XPathConstants.NUMBER)).intValue();
            mapType = ((Double) path.evaluate("/map/mapType", document, XPathConstants.NUMBER)).intValue();
            refWrk.getCam().setLocked(path.evaluate("/map/camLocked", document, XPathConstants.STRING).equals("true") ? true : false);
        } catch (XPathExpressionException ex) {
            ErrorHandler.error("Error while loading Generals Infos in XML file");
        }

    }

    private void loadBricksXML() {

        try {

            File file = new File("bin/maps/" + mapFolder + "/conf.xml");
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document;
            document = documentBuilder.parse(file);

            XPathFactory xpf = XPathFactory.newInstance();
            XPath path = xpf.newXPath();

            // Bricks
            String expression = "//brick";
            NodeList bricks = (NodeList) path.evaluate(expression, document, XPathConstants.NODESET);

            for (int i = 0; i < bricks.getLength(); i++) {
                Node n = bricks.item(i);

                path.compile("x");
                int srcX = ((Double) path.evaluate("x", n, XPathConstants.NUMBER)).intValue() - 1;

                path.compile("y");
                int srcY = ((Double) path.evaluate("y", n, XPathConstants.NUMBER)).intValue() - 1;

                path.compile("content");
                String content = (String) path.evaluate("content", n, XPathConstants.STRING);

                path.compile("quantity");
                int qtt = ((Double) path.evaluate("quantity", n, XPathConstants.NUMBER)).intValue();

                for (Block b : refWrk.getBlocks()) {
                    if (b.getId() == LETTER_BLOCK_BRICK) {
                        if (b.getX() == srcX * 32) {
                            if (b.getY() == srcY * 32) {
                                for (int j = 0; j < qtt; j++) {
                                    switch (content) {
                                        case "coin":
                                            b.getContent().add(new Entity_Coin(refWrk, refMario, b.getX() + 6, b.getY(), mapType, true));
                                            break;
                                        case "muschroom":
                                            b.getContent().add(new Entity_Muschroom(refWrk, refMario, b.getX(), b.getY(), refMario.getDir(), mapType, true));
                                            break;
                                        case "flower":
                                            b.getContent().add(new Entity_Flower(refWrk, refMario, b.getX(), b.getY(), mapType));
                                            break;
                                    }
                                }
                                break;
                            }
                        }
                    }
                }

            }
        } catch (Exception ex) {
            ErrorHandler.error("Error while loading Bricks in XML file");
        }
    }

    private void loadCastlesXML(XPath path, Document document) {
        try {
            // Castles
            NodeList castles = (NodeList) path.evaluate("//castle", document, XPathConstants.NODESET);
            setupCastles(castles.getLength());

            for (int i = 0; i < castles.getLength(); i++) {
                try {
                    Node n = castles.item(i);

                    path.compile("type");
                    String type = (String) path.evaluate("type", n, XPathConstants.STRING);

                    path.compile("x");
                    int x = ((Double) path.evaluate("x", n, XPathConstants.NUMBER)).intValue() * 32;

                    path.compile("y");
                    int y = ((Double) path.evaluate("y", n, XPathConstants.NUMBER)).intValue() * 32;

                    this.castles[i] = new Decoration(DECORATION_TYPE_CASTLE, "castle_" + type + ".png");
                    this.castles[i].setX(x - this.castles[i].getSprite().getWidth(null) / 2);
                    this.castles[i].setY(y - this.castles[i].getSprite().getHeight(null));
                    this.castles[i].setWidth(this.castles[i].getSprite().getWidth(null));
                    this.castles[i].setHeight(this.castles[i].getSprite().getHeight(null));
                } catch (XPathExpressionException ex) {
                    ErrorHandler.error("Error while loading Castles in XML file");
                }
            }
        } catch (XPathExpressionException ex) {
            ErrorHandler.error("Error while loading Castles in XML file");
        }
    }

    private void loadConfXML() {
        try {
            File file = new File("bin/maps/" + mapFolder + "/conf.xml");
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(file);

            XPathFactory xpf = XPathFactory.newInstance();
            XPath path = xpf.newXPath();

            loadGeneralsInfosXML(path, document);
            loadCastlesXML(path, document);
            loadLinksXML(path, document);

        } catch (Exception e) {
            ErrorHandler.error("Error in XML conf part");
        }
    }

    private void loadMap() {
        mapContent = "";
        //Load map into mapContent
        try (BufferedReader br = new BufferedReader(new FileReader(new File("bin/maps/" + mapFolder + "/" + name + ".txt")))) {
            String line;
            while ((line = br.readLine()) != null) {
                mapContent += line;
            }
        } catch (IOException e) {

        }
    }

    private void addBackground() {

        if (mapType == MAP_TYPE_UNDERGROUND) {
            return;
        }
        Random r = new Random();
        setupGroundDecorations();
        setupSkyDecorations();
        // + and - for castles.length to add them at the end and still have room
        decorationNbr = (int) width * 32 / 1000 * 10 + castles.length;
        decorations = new Decoration[decorationNbr];

        //Map size (width * 32) / screen Size --> Always have
        //a plante / cloud / hill on the screen
        for (int i = 0; i < decorationNbr - castles.length; i += 2) {
            //Sky
            int randomSky = r.nextInt((skyDecorations.length) - 0) + 0;
            int randomSkyX = r.nextInt((i * 500 + 500) - (i * 500)) + i * 500;
            int randomSkyY = r.nextInt(((height * 32) - 564) - (height * 32 - 800)) + (height * 32 - 800);

            //Add sky decoration
            Decoration newSkyDecoration = new Decoration(skyDecorations[randomSky].getType(), skyDecorations[randomSky].getFileName());
            decorations[i] = newSkyDecoration;
            decorations[i].setX(randomSkyX);
            decorations[i].setY(randomSkyY);

            //Ground
            int randomGround = r.nextInt((groundDecorations.length) - 0) + 0;
            int randomGroundX = r.nextInt((i * 500 + 500) - (i * 500)) + i * 500;

            //Add ground decoration
            Decoration newGroundDecoration = new Decoration(groundDecorations[randomGround].getType(), groundDecorations[randomGround].getFileName());
            decorations[i + 1] = newGroundDecoration;
            decorations[i + 1].setHeight(groundDecorations[randomGround].getHeight());
            decorations[i + 1].setX(randomGroundX);
            decorations[i + 1].setY(height * 32 - 64 - decorations[i + 1].getHeight());
        }

        // Add castles
        for (int i = 0; i < castles.length; i++) {
            decorations[decorationNbr - castles.length + i] = castles[i];
        }
    }

    private void setupCastles(int i) {
        castles = new Decoration[i];
    }

    private void setupGroundDecorations() {
        groundDecorations = new Decoration[4];
        groundDecorations[0] = new Decoration(DECORATION_TYPE_GROUND, "bush_small.png");
        groundDecorations[0].setWidth(64);
        groundDecorations[0].setHeight(32);
        groundDecorations[1] = new Decoration(DECORATION_TYPE_GROUND, "bush_big.png");
        groundDecorations[1].setWidth(128);
        groundDecorations[1].setHeight(32);
        groundDecorations[2] = new Decoration(DECORATION_TYPE_GROUND, "hill_small.png");
        groundDecorations[2].setWidth(64);
        groundDecorations[2].setHeight(32);
        groundDecorations[3] = new Decoration(DECORATION_TYPE_GROUND, "hill_big.png");
        groundDecorations[3].setWidth(128);
        groundDecorations[3].setHeight(64);
    }

    private void setupSkyDecorations() {
        skyDecorations = new Decoration[2];
        skyDecorations[0] = new Decoration(DECORATION_TYPE_SKY, "cloud_small.png");
        skyDecorations[1] = new Decoration(DECORATION_TYPE_SKY, "cloud_big.png");
    }

    private boolean checkMapSize() {
        boolean result = true;
        try {
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    getTile(j, i);
                }
            }
        } catch (java.lang.StringIndexOutOfBoundsException e) {
            result = false;
        }
        return result;
    }

    private char getTile(int x, int y) {
        try {
            if (x >= 0 && x < width && y >= 0 && y < height) {
                return mapContent.charAt(y * width + x);
            }
        } catch (NullPointerException e) {
            return '.';
        }
        return '.';
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Wrk getRefWrk() {
        return refWrk;
    }

    public void setRefWrk(Wrk refWrk) {
        this.refWrk = refWrk;
    }

    public String getMapContent() {
        return mapContent;
    }

    public void setMapContent(String mapContent) {
        this.mapContent = mapContent;
    }

    public Entity_Mario getRefMario() {
        return refMario;
    }

    public void setRefMario(Entity_Mario refMario) {
        this.refMario = refMario;
    }

    public int getDecorationNbr() {
        return decorationNbr;
    }

    public void setDecorationNbr(int decorationNbr) {
        this.decorationNbr = decorationNbr;
    }

    public Decoration[] getDecorations() {
        return decorations;
    }

    public void setDecorations(Decoration[] decorations) {
        this.decorations = decorations;
    }

    public Decoration[] getGroundDecorations() {
        return groundDecorations;
    }

    public void setGroundDecorations(Decoration[] groundDecorations) {
        this.groundDecorations = groundDecorations;
    }

    public Decoration[] getSkyDecorations() {
        return skyDecorations;
    }

    public void setSkyDecorations(Decoration[] skyDecorations) {
        this.skyDecorations = skyDecorations;
    }

    public ArrayList<Map_Link> getLinks() {
        return links;
    }

    public void setLinks(ArrayList<Map_Link> links) {
        this.links = links;
    }

    public String getMapFolder() {
        return mapFolder;
    }

    public void setMapFolder(String mapFolder) {
        this.mapFolder = mapFolder;
    }

    public Decoration[] getCastles() {
        return castles;
    }

    public void setCastles(Decoration[] castles) {
        this.castles = castles;
    }

    public int getStartX() {
        return startX;
    }

    public void setStartX(int startX) {
        this.startX = startX;
    }

    public int getStartY() {
        return startY;
    }

    public void setStartY(int startY) {
        this.startY = startY;
    }

    public int getMapType() {
        return mapType;
    }

    public void setMapType(int mapType) {
        this.mapType = mapType;
    }

}
