package org.integracao.databuilder.utils;

public class Reader {

  public static String loadFile(String filename) {
      ClassLoader loader = Reader.class.getClassLoader();
      
      try {
        var in = loader.getResourceAsStream(filename);
        var contents = new byte[1024];

        var bytesRead = 0;
        var strFileContents = "";
        while((bytesRead = in.read(contents)) != -1) { 
          strFileContents += new String(contents, 0, bytesRead);              
        }

        return strFileContents;
      } catch (Exception e) {
        e.printStackTrace();
      }
      return "";
  }

}
