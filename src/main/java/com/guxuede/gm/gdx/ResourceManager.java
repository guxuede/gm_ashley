package com.guxuede.gm.gdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResourceManager {
    private static final Map<String,Sound> soundMap = new HashMap<String, Sound>();
    private static final Map<String,Texture> TEXTURE_MAP = new HashMap<String, Texture>();
    private static final Map<String,TextureRegion> TEXTURE_REGION_MAP =  new HashMap<String, TextureRegion>();
    private static final TextureAtlas TEXTURE_ATLAS_PACK =new TextureAtlas(Gdx.files.internal("data/pack"));
    private static final List<AnimationHolder> ANIMATION_HOLDER_LIST = ActorJsonParse.parse(Gdx.files.internal("data/actors"));

    public static Sprite shadow = new Sprite(getTextureRegion("data/180-Switch03",96,96,32,32));

    public static Texture getTexture(String name){
        Texture texture = null;
        texture = TEXTURE_MAP.get(name);
        if(texture==null){
            if(name.contains(".")){
                throw new RuntimeException("resource name not support contain dot or suffix");
            }
            FileHandle fileHandle = Gdx.files.internal(name+".PNG");
            if(fileHandle.exists()){
                texture = new Texture(fileHandle);
                TEXTURE_MAP.put(name, texture);
                TEXTURE_REGION_MAP.put(name, new TextureRegion(texture));
            }else{
                System.err.println(name + " resource not found.");
            }
        }
        return texture;
    }

    public static TextureRegion getTextureRegion(String name, int x, int y, int w, int h){
        TextureRegion textureRegion = null;
        String key = name+"_"+x+"x"+y+"x"+"x"+w+"x"+h;
        textureRegion = getTextureRegion(key);
        if(textureRegion==null){
            TextureRegion fullTextureRegion = getTextureRegion(name);
            if(fullTextureRegion!=null){
                textureRegion = new TextureRegion(fullTextureRegion,x,y,w,h);
                TEXTURE_REGION_MAP.put(key, textureRegion);
            }
        }
        return textureRegion;
    }
    public static TextureRegion getTextureRegion(String name){
        TextureRegion textureRegion = null;
        textureRegion = TEXTURE_REGION_MAP.get(name);
        if(textureRegion==null){
            textureRegion = TEXTURE_ATLAS_PACK.findRegion(name);
        }
        if(textureRegion==null){
            Texture texture = getTexture(name);
            if(texture !=null){
                textureRegion = TEXTURE_REGION_MAP.get(name);
            }
        }
        return textureRegion;
    }

    public static AnimationHolder getAnimationHolder(String name){
        for(AnimationHolder animationHolder : ANIMATION_HOLDER_LIST){
            if(name.equals(animationHolder.name)){
                return animationHolder;//.getCopy();
            }
        }
        if("spine".equalsIgnoreCase(name)){

            new AnimationHolder();
        }
        return null;
    }

    public static Sound getSoundOrLoad(String soundFile){
        if(soundMap.containsKey(soundFile)){
            return soundMap.get(soundFile);
        }
        if(Gdx.files.internal("sounds/"+soundFile).exists()){
            Sound sound = Gdx.audio.newSound(Gdx.files.internal("sounds/"+soundFile));
            soundMap.put(soundFile,sound);
        }
        return soundMap.get(soundFile);
    }

}
