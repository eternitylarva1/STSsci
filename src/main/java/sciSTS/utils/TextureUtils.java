package sciSTS.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public final class TextureUtils {
  public static ShaderProgram GetShader(String vsPath, String fsPath) {
    ShaderProgram shader = new ShaderProgram(Gdx.files.internal(vsPath), Gdx.files.internal(fsPath));
    if (!shader.isCompiled())
      throw new RuntimeException(shader.getLog()); 
    return shader;
  }
  
  public static ShaderProgram GetShader(String fsPath) {
    return GetShader("ZhenghuoResources/shaders/default.vs", fsPath);
  }
}
