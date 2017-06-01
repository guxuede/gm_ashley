package com.guxuede.gm.gdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guxuede on 2016/5/27 .
 */
public class ActorJsonParse {

    public static final String WALK_DOWN_ANIMATION = "walkDownAnimation";
    public static final String WALK_LEFT_ANIMATION = "walkLeftAnimation";
    public static final String WALK_RIGHT_ANIMATION = "walkRightAnimation";
    public static final String WALK_UP_ANIMATION = "walkUpAnimation";
    public static final String STOP_DOWN_ANIMATION = "stopDownAnimation";
    public static final String STOP_LEFT_ANIMATION = "stopLeftAnimation";
    public static final String STOP_RIGHT_ANIMATION = "stopRightAnimation";
    public static final String STOP_UP_ANIMATION = "stopUpAnimation";
    public static final String DEATH_ANIMATION = "deathAnimation";

    private static class ParseContext {
        protected String textureName;
        protected float frameDuration;
        protected float width;
        protected float height;
        protected float alpha;
        protected float rotation;

        protected float scaleX;
        protected float scaleY;
        //精灵展示位置
        protected float x,y;

        protected float textureOffSetX = 0;
        protected float textureOffSetY = 0;

        protected String sound;
    }

    public static List<AnimationHolder> parse(FileHandle fileHandle){
        JsonReader jsonReader = new JsonReader();
        JsonValue jsonValue = jsonReader.parse(fileHandle);


        List<AnimationHolder> animationHolders= new ArrayList<AnimationHolder>();
        for(JsonValue.JsonIterator it = jsonValue.iterator(); it.hasNext();){
            AnimationHolder animationHolder=parseAnimationHolder(it.next());
            animationHolders.add(animationHolder);
        }
        return animationHolders;
    }

    /**
     * 解析
     * {
             "name" : "Undead",
             "width":80,
             "height":96,
             "texture":"Undead",
             "animations":。。。
        }
     * @param jsonValue
     * @return
     */
    private static AnimationHolder parseAnimationHolder(JsonValue jsonValue){
        ParseContext parseContext = extendParentParseContext(null, jsonValue);
        String name = jsonValue.getString("name");

        AnimationHolder animationHolder= new AnimationHolder();
        animationHolder.width = (int) parseContext.width;
        animationHolder.height = (int) parseContext.height;
        animationHolder.name = name;
        /////////////////////////animations/////////////////////////////
        JsonValue animationsJ = jsonValue.get("animations");
        JsonValue.JsonIterator it = animationsJ.iterator();
        for(;it.hasNext();){
            JsonValue animationJ = it.next();
            String animationName = animationJ.getString("name");
            Animation animation = parseAnimation(parseContext,animationJ);
            if(!animationName.contains(",")){
                animationHolder.addAnimation(animationName.hashCode(),animation);
            }else{
                for(String an : animationName.split(",")){
                    animationHolder.addAnimation(an.hashCode(),animation);
                }
            }
        }
        /////////////////////////sounds/////////////////////////////
        JsonValue soundsJ = jsonValue.get("sounds");
        if(soundsJ!=null){
            JsonValue.JsonIterator sit = soundsJ.iterator();
            for(;sit.hasNext();) {
                JsonValue soundJ = sit.next();
                String soundName= soundJ.getString("name");
                String soundFile = soundJ.getString("sound");
                boolean isLoop = soundJ.getBoolean("loop",false);
                Sound sound = ResourceManager.getSoundOrLoad(soundFile);
                if(!soundName.contains(",")){
                    //animationHolder.addSound(soundName,new SoundHolder(sound,isLoop));
                }else{
                    for(String sn : soundName.split(",")){
                        //animationHolder.addSound(sn,new SoundHolder(sound,isLoop));
                    }
                }
            }
        }
        return animationHolder;
    }

    /**
     * 解析
     *  "animations":[
         {
             "name":"walkDownAnimation",
             "frameDuration":0.1,
             "frames":...
         }
     *  ]
     * @param parentParseContext
     * @param animJ
     * @return
     */
    private static Animation parseAnimation(ParseContext parentParseContext, JsonValue animJ){
        ParseContext localParseContext = extendParentParseContext(parentParseContext, animJ);

        JsonValue framesJ = animJ.get("frames");
        final GdxSprite[] frames = new GdxSprite[framesJ.size];
        JsonValue.JsonIterator it = framesJ.iterator();
        for (int i = 0; it.hasNext() ; i++) {
            JsonValue frameJ = it.next();
            if(frameJ.isNumber()){
                frames[i]=parseNumbersSprite(localParseContext, frameJ);
            }else if(frameJ.isArray()){
                frames[i]=parseSingleArraySprite(localParseContext,frameJ);
            }else{
                if(frameJ.has("frame")){
                    frames[i]=parseSingleStyleSprite(localParseContext,frameJ);
                }else{
                    frames[i]=parseMultiSprite(localParseContext,frameJ);
                }
            }
        }

        final Animation animation = new Animation(localParseContext.frameDuration,frames);
        return animation;
    }

    /**
     * 解析 frames 里 直接标明图块位置的，如：
     * "frames"：[1,2,3,4,5]
     * @param parentParseContext
     * @param posInt
     * @return
     */
    private static GdxSprite parseNumbersSprite(ParseContext parentParseContext,JsonValue posInt){
        TextureRegion frame = getTextureRegionByNumber(parentParseContext, posInt.asInt());
        GdxSprite sprite = new GdxSprite(frame);
        sprite.setSize(parentParseContext.width, parentParseContext.height);
        sprite.setOffSet(parentParseContext.x, parentParseContext.y);
        sprite.setScale(parentParseContext.scaleX, parentParseContext.scaleY);
        sprite.setRotation(parentParseContext.rotation);
        sprite.setAlpha(parentParseContext.alpha);
        return sprite;
    }

    /**
     * 解析frames里面只有一层动画的，比如：
         "frames":[
         [0,0,80,96],
         [80,0,80,96],
         [160,0,80,96],
         [240,0,80,96]
         ]
     * @param parentParseContext
     * @param posIntArrayJ
     * @return
     */
    private static GdxSprite parseSingleArraySprite(ParseContext parentParseContext,JsonValue posIntArrayJ){
        TextureRegion frame = getTextureRegion(parentParseContext, posIntArrayJ);
        GdxSprite sprite = new GdxSprite(frame);
        sprite.setSize(parentParseContext.width, parentParseContext.height);
        sprite.setOffSet(parentParseContext.x, parentParseContext.y);
        sprite.setScale(parentParseContext.scaleX, parentParseContext.scaleY);
        sprite.setRotation(parentParseContext.rotation);
        sprite.setAlpha(parentParseContext.alpha);
       return sprite;
    }

    /**
     * 解析：frames 里面有样式动画的，比如
     *   "frames":[
                {
                    "scale":1.5,
                    "y":1,
                    "frame":1
                },
                {
                     "scale":1.5,
                     "y":1,
                     "frame":2
                },
         ]
     * @param parentParseContext
     * @param frameJ
     * @return
     */
    private static GdxSprite parseSingleStyleSprite(ParseContext parentParseContext,JsonValue frameJ){
        ParseContext localParseContext = extendParentParseContext(parentParseContext, frameJ);
        return parseSingleArraySprite(localParseContext,frameJ.get("frame"));
    }


    /**
     * 解析：frames 里面有多层动画的，比如
     * "frames":[
             {
                 "frameElements":[。。。]
            },
             {
             "frameElements":[。。。]
             },
        ]
     * @param parentParseContext
     * @param frameJ
     * @return
     */
    private static GdxSprite parseMultiSprite(ParseContext parentParseContext,JsonValue frameJ){
        JsonValue frameElementsJ = frameJ.get("frameElements");
        JsonValue.JsonIterator it = frameElementsJ.iterator();

        final GdxSprite[] frames = new GdxSprite[frameElementsJ.size];
        for (int i = 0; it.hasNext() ; i++) {
            /**
             * frameElements 支持一下格式：
             *             "frameElements":[
                             {
                                 "y":20,                    //详细的位置描述
                                 "scale":1.5,
                                 "frame":[576,192,192,192]
                             }
             */
            JsonValue frameElementJ = it.next();
            if(frameElementJ.isObject()){
                ParseContext localParseContext = extendParentParseContext(parentParseContext, frameElementJ);
                TextureRegion textureRegion = getTextureRegion(localParseContext, frameElementJ.get("frame"));
                GdxSprite spriteElement = new GdxSprite(textureRegion);
                applyDefaultValueToSprite(localParseContext,spriteElement);
                frames[i] = spriteElement;
            }else if(frameElementJ.isNumber()){
                TextureRegion textureRegion = getTextureRegionByNumber(parentParseContext, frameElementJ.asInt());
                GdxSprite spriteElement = new GdxSprite(textureRegion);
                applyDefaultValueToSprite(parentParseContext, spriteElement);
                frames[i] = spriteElement;
            }else{
                throw new RuntimeException("frameElements define not correct:"+frameElementJ.toString());
            }
        }

        MultiInOneSprite sprite = new MultiInOneSprite(frames);
        sprite.setRegionHeight((int) parentParseContext.width);
        sprite.setRegionWidth((int) parentParseContext.height);
        return sprite;
    }


    /**
     * 解析 frame 支持以下格式：
     * "frame":1 位置描述
     * "frame":[1] 位置描述
     * "frame":[576,192]            像素级位置描述 --》 [576,192,defaultW,defaultH]
     * "frame":[576,192,192]        像素级位置描述--》 [576,192,192,192]
     * "frame":[576,192,192,192]    像素级位置描述--》 [576,192,192,192]
     * @param parseContext
     * @param posIntArrayJ
     * @return
     */
    private static TextureRegion getTextureRegion(ParseContext parseContext, JsonValue posIntArrayJ){
        if(posIntArrayJ.isNumber()){
            return getTextureRegionByNumber(parseContext,posIntArrayJ.asInt());
        }else if(posIntArrayJ.isArray()){
            int[] xywh = posIntArrayJ.asIntArray();
            if(xywh.length == 1){
                return getTextureRegionByNumber(parseContext,xywh[0]);
            }else if(xywh.length == 2){
                return getTextureRegionByXYWH(parseContext, new int[]{xywh[0], xywh[1], (int) parseContext.width, (int) parseContext.height});
            }else if(xywh.length == 3){
                return getTextureRegionByXYWH(parseContext,new int[]{xywh[0],xywh[1], xywh[2] ,xywh[2]});
            }else if(xywh.length == 4){
                return getTextureRegionByXYWH(parseContext,new int[]{xywh[0],xywh[1], xywh[2] ,xywh[3]});
            }
        }
        throw new RuntimeException("frame define not correct:"+posIntArrayJ.toString());
    }

    private static TextureRegion getTextureRegionByNumber(ParseContext parseContext, int number){
        TextureRegion textureRegion = ResourceManager.getTextureRegion(parseContext.textureName);
        int numOfLine = textureRegion.getRegionWidth() / (int) parseContext.width;//每行有几个图片
        int y = number / numOfLine;//第几行
        int x = number % numOfLine;//第几列
        return getTextureRegionByXYWH(parseContext,new int[]{(int) (x* parseContext.width + parseContext.textureOffSetX),(int) (y* parseContext.height + parseContext.textureOffSetY),(int) (parseContext.width),(int) (parseContext.height)});
    }

    private static TextureRegion getTextureRegionByXYWH(ParseContext parseContext, int[] xywh){
        return ResourceManager.getTextureRegion(parseContext.textureName, xywh[0]+(int) parseContext.textureOffSetX, xywh[1]+(int) parseContext.textureOffSetY, xywh[2], xywh[3]);
    }

    private static ParseContext extendParentParseContext(ParseContext parseContext, JsonValue animJ){
        if(parseContext == null){
            parseContext = new ParseContext();
            parseContext.alpha = 1;
            parseContext.scaleX = 1;
            parseContext.scaleY = 1;
            parseContext.frameDuration = 0.1f;
        }
        float frameDuration = animJ.getFloat("frameDuration", parseContext.frameDuration);
        String textureName = animJ.getString("texture", parseContext.textureName);
        float x = animJ.getFloat("x", parseContext.x);
        float y = animJ.getFloat("y", parseContext.y);
        float width = animJ.getFloat("width", parseContext.width);
        float height = animJ.getFloat("height", parseContext.height);
        float alpha = animJ.getFloat("alpha", parseContext.alpha);
        float rotation = animJ.getFloat("rotation", parseContext.rotation);
        float scaleX = parseContext.scaleX, scaleY = parseContext.scaleY;
        if(animJ.has("scale")){
            scaleX = scaleY = animJ.getFloat("scale", 1);
        }
        if(animJ.has("scaleX")){
            scaleX = animJ.getFloat("scaleX", scaleX);
        }
        if(animJ.has("scaleY")){
            scaleY = animJ.getFloat("scaleY", scaleY);
        }

        float textureOffSetX = animJ.getFloat("textureOffSetX",parseContext.textureOffSetX);
        float textureOffSetY = animJ.getFloat("textureOffSetY",parseContext.textureOffSetY);

        String sound = animJ.getString("sound",parseContext.sound);

        ParseContext localParseContext = new ParseContext();
        localParseContext.textureName = textureName;
        localParseContext.width = width;
        localParseContext.height = height;
        localParseContext.alpha = alpha;
        localParseContext.rotation =rotation;
        localParseContext.scaleX = scaleX;
        localParseContext.scaleY = scaleY;
        localParseContext.x =x;
        localParseContext.y =y;
        localParseContext.frameDuration = frameDuration;
        localParseContext.textureOffSetX = textureOffSetX;
        localParseContext.textureOffSetY = textureOffSetY;
        localParseContext.sound = sound;
        return localParseContext;
    }

    private static void applyDefaultValueToSprite(ParseContext parseContext,GdxSprite sprite){
        sprite.setSize(parseContext.width, parseContext.height);
        sprite.setOffSet(parseContext.x, parseContext.y);
        sprite.setScale(parseContext.scaleX,parseContext.scaleY);
        sprite.setRotation(parseContext.rotation);
        sprite.setAlpha(parseContext.alpha);
    }


    public static void main(String[] args) {
        int hn = 5;//横向几帧
        int vn = 3;//竖向几帧
        int pw = 960;//图片宽度
        int ph = 576;//图片高度

        int fw = pw/hn;//单帧宽度
        int fh = ph/vn;//单帧高度

        String json = "{\n" +
                "  \"name\" : \"Wagon01\",\n" +
                "  \"width\":"+fw+",\n" +
                "  \"height\":"+fh+",\n" +
                "  \"texture\":\"188-Wagon01\",\n" +
                "  \"animations\":[\n" +
                "     {\n" +
                "       \"name\":\"walkDownAnimation\",\n" +
                "       \"frameDuration\":0.1,\n" +
                "       \"frames\":[\n" +
                "         [0,0,"+fw+","+fh+"],\n" +
                "         ["+fw+",0,"+fw+","+fh+"],\n" +
                "         ["+fw*2+",0,"+fw+","+fh+"],\n" +
                "         ["+fw*3+",0,"+fw+","+fh+"]\n" +
                "       ]\n" +
                "     },\n" +
                "     {\n" +
                "       \"name\":\"walkLeftAnimation\",\n" +
                "       \"frameDuration\":0.1,\n" +
                "       \"frames\":[\n" +
                "         [0,"+fh+","+fw+","+fh+"],\n" +
                "         ["+fw+","+fh+","+fw+","+fh+"],\n" +
                "         ["+fw*2+","+fh+","+fw+","+fh+"],\n" +
                "         ["+fw*3+","+fh+","+fw+","+fh+"]\n" +
                "       ]\n" +
                "     },\n" +
                "     {\n" +
                "       \"name\":\"walkRightAnimation\",\n" +
                "       \"frameDuration\":0.1,\n" +
                "       \"frames\":[\n" +
                "         [0,"+fh*2+","+fw+","+fh+"],\n" +
                "         ["+fw+","+fh*2+","+fw+","+fh+"],\n" +
                "         ["+fw*2+","+fh*2+","+fw+","+fh+"],\n" +
                "         ["+fw*3+","+fh*2+","+fw+","+fh+"]\n" +
                "       ]\n" +
                "     },\n" +
                "     {\n" +
                "       \"name\":\"walkUpAnimation\",\n" +
                "       \"frameDuration\":0.1,\n" +
                "       \"frames\":[\n" +
                "         [0,"+fh*3+","+fw+","+fh+"],\n" +
                "         ["+fw+","+fh*3+","+fw+","+fh+"],\n" +
                "         ["+fw*2+","+fh*3+","+fw+","+fh+"],\n" +
                "         ["+fw*3+","+fh*3+","+fw+","+fh+"]\n" +
                "       ]\n" +
                "     },\n" +
                "     {\n" +
                "       \"name\":\"stopDownAnimation\",\n" +
                "       \"frameDuration\":0.5,\n" +
                "       \"frames\":[\n" +
                "         [0,0,"+fw+","+fh+"],\n" +
                "         ["+fw*2+",0,"+fw+","+fh+"]\n" +
                "       ]\n" +
                "     },\n" +
                "     {\n" +
                "       \"name\":\"stopLeftAnimation\",\n" +
                "       \"frameDuration\":0.1,\n" +
                "       \"frames\":[\n" +
                "         [0,"+fh+","+fw+","+fh+"],\n" +
                "         ["+fw*2+","+fh+","+fw+","+fh+"]\n" +
                "       ]\n" +
                "     },\n" +
                "     {\n" +
                "       \"name\":\"stopRightAnimation\",\n" +
                "       \"frameDuration\":0.1,\n" +
                "       \"frames\":[\n" +
                "         [0,"+fh*2+","+fw+","+fh+"],\n" +
                "         ["+fw*2+","+fh*2+","+fw+","+fh+"]\n" +
                "       ]\n" +
                "     },\n" +
                "     {\n" +
                "       \"name\":\"stopUpAnimation\",\n" +
                "       \"frameDuration\":0.1,\n" +
                "       \"frames\":[\n" +
                "         [0,"+fh*3+","+fw+","+fh+"],\n" +
                "         ["+fw*2+","+fh*3+","+fw+","+fh+"]\n" +
                "       ]\n" +
                "     },\n" +
                "     {\n" +
                "       \"name\":\"deathAnimation\",\n" +
                "       \"frameDuration\":0.1,\n" +
                "       \"frames\":[\n" +
                "         [0,"+fh+","+fw+","+fh+"],\n" +
                "         [0,"+fh+","+fw+","+fh+"],\n" +
                "         [0,"+fh*2+","+fw+","+fh+"],\n" +
                "         [0,256,"+fw+","+fh+"]\n" +
                "       ]\n" +
                "     }\n" +
                "  ]\n" +
                "}";


        System.out.println(json);


//        JsonWriter rootWriter = new JsonWriter(new PrintWriter(System.out));
//        try {
//            JsonWriter firstChild = rootWriter.object();
//            firstChild.set("name", "xxxx");
//            firstChild.set("width", pw / hn);
//            firstChild.set("height", ph / vn);
//            firstChild.set("texture", "");
//
//            JsonWriter animationChild = rootWriter.array("animations");
//            {
//                JsonWriter animationJ = animationChild.object();
//                animationJ.set("name",WALK_DOWN_ANIMATION);
//                animationJ.set("frameDuration",0.1f);
//                JsonWriter frameJ = animationJ.array("frames");
//            }
//            rootWriter.close();
//            rootWriter.flush();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//

    }
}
